package com.stingers.alttpr.repository

import com.stingers.alttpr.model.GameMode
import com.stingers.alttpr.model.RomEntity
import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.platform.NetworkManager
import com.stingers.alttpr.repository.local.RomDao
import com.stingers.alttpr.repository.local.RomStorage
import com.stingers.alttpr.repository.local.SpriteDao
import com.stingers.alttpr.repository.remote.AlttprService
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton
import kotlin.time.Clock

@Singleton
class AlttprRepository(
    private val alttprService: AlttprService,
    private val romDao: RomDao,
    private val spriteDao: SpriteDao
) {

    suspend fun getSprites(): Result<List<Sprite>> = withContext(Dispatchers.IO) {
        try {
            if (NetworkManager.isNetworkConnected()) {
                val sprites = alttprService.getSprites()
                spriteDao.insertSprites(sprites)
                Result.success(sprites)
            } else {
                val downloadedSprites = spriteDao.getDownloadedSprites()
                Result.success(downloadedSprites)
            }
        } catch (e: Exception) {
            try {
                // Fallback to downloaded sprites in case of network exception
                val downloadedSprites = spriteDao.getDownloadedSprites()
                Result.success(downloadedSprites)
            } catch (dbEx: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getSpriteBytes(sprite: Sprite): ByteArray? = withContext(Dispatchers.IO) {
        try {
            val fileName = "sprite_${sprite.name.hashCode()}_v${sprite.version}.zspr"
            val existingFile = RomStorage.getSpriteFile(fileName)
            
            if (existingFile != null && existingFile.exists() && sprite.downloadedFile == fileName) {
                return@withContext existingFile.readBytes()
            }

            if (!NetworkManager.isNetworkConnected()) {
                return@withContext existingFile?.readBytes()
            }

            val fileUrl = sprite.fileUrl
            if (fileUrl.isBlank()) return@withContext null

            val bytes = alttprService.getSpriteFile(fileUrl)
            if (bytes.isNotEmpty()) {
                RomStorage.saveSpriteFile(fileName, bytes)
                val updatedSprite = sprite.copy(downloadedFile = fileName)
                spriteDao.insertSprites(listOf(updatedSprite))
                return@withContext bytes
            }
            null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createDailySeed(): Result<String> = withContext(Dispatchers.IO) {
        try {
            // GET https://alttpr.com/api/daily -> gets hash
            val dailyResponse = alttprService.getDaily()
            val hash = dailyResponse.hash
            return@withContext fetchPatchAndSeedInfo(hash)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchPatchAndSeedInfo(hash: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Check if we already have a saved RomEntity for this hash
            val existingRom = romDao.getRom(hash)
            if (existingRom != null) {
                return@withContext Result.success(hash)
            }

            // 1. GET https://alttpr.com/api/h/{hash} -> retrieves base patch file
            val basePatchResponse = alttprService.getBasePatchInfo(hash)
            val bpsLocation = basePatchResponse.bpsLocation


            // 2. GET https://alttpr.com{bpsLocation} -> downloads raw .bps patch binary
            val bpsBytes = alttprService.getBpsPatch(bpsLocation)
            if (bpsBytes.isEmpty()) {
                return@withContext Result.failure(IllegalStateException("Failed to download BPS patch"))
            }

            // 3. Format filename: ALTTPR_(hash).bps
            val filename = "ALTTPR_${hash}.bps"

            // 4. Save BPS patch to generated seeds bucket
            RomStorage.saveGeneratedSeed(filename, bpsBytes)

            // 5. GET https://alttpr.com/hash/{hash} -> retrieves seed patch
            val seedDetails = alttprService.getSeedPatch(hash)

            if (seedDetails.patch.isNullOrEmpty()) {
                return@withContext Result.failure(IllegalStateException("Failed to download JSON patch"))
            }

            // 6. Save record into database
            romDao.insertRom(
                RomEntity(
                    hash = hash,
                    md5 = basePatchResponse.md5,
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    localFileName = filename,
                    gameMode = GameMode.DAILY_CHALLENGE,
                    logic = seedDetails.logic,
                    generated = seedDetails.generated,
                    size = seedDetails.size ?: 2,
                    meta = seedDetails.spoiler?.meta,
                    patch = seedDetails.patch
                )
            )
            Result.success(hash)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
