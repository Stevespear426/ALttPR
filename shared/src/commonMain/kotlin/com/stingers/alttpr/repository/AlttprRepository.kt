package com.stingers.alttpr.repository

import com.stingers.alttpr.model.RandomizerGameModel
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.model.api.GenerateSeedRequest
import com.stingers.alttpr.platform.NetworkManager
import com.stingers.alttpr.repository.local.RomStorage
import com.stingers.alttpr.repository.local.SeedDao
import com.stingers.alttpr.repository.local.SpriteDao
import com.stingers.alttpr.repository.remote.AlttprService
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton

@Singleton
open class AlttprRepository(
    private val alttprService: AlttprService,
    private val seedDao: SeedDao,
    private val spriteDao: SpriteDao
) {

    open suspend fun getSprites(): Result<List<Sprite>> = withContext(Dispatchers.IO) {
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

    open suspend fun getSpriteBytes(sprite: Sprite): ByteArray? = withContext(Dispatchers.IO) {
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

    open suspend fun createDailySeed(): SeedEntity = withContext(Dispatchers.IO) {
        // GET https://alttpr.com/api/daily -> gets hash
        val dailyResponse = alttprService.getDaily()
        val hash = dailyResponse.hash
        return@withContext fetchPatchAndSeedInfo(hash)
    }

    open suspend fun generateRandomizerSeed(
        model: RandomizerGameModel
    ): SeedEntity = withContext(Dispatchers.IO) {
        val response = alttprService.generateSeed(GenerateSeedRequest.getRequest(model))
        val hash = response.hash
        return@withContext fetchPatchAndSeedInfo(hash).copy(
            request = model
        )
    }

    /*
    * Returns a Bps Patch and md5 hash for verification.
    */
    open suspend fun getBpsPatch(
        hash: String
    ): Pair<String, ByteArray> = withContext(Dispatchers.IO) {
        // GET https://alttpr.com/api/h/{hash} -> retrieves base patch file
        val basePatchResponse = alttprService.getBasePatchInfo(hash)
        val bpsLocation = basePatchResponse.bpsLocation

        // GET https://alttpr.com{bpsLocation} -> downloads raw .bps patch binary
        val bpsBytes = alttprService.getBpsPatch(bpsLocation)
        if (bpsBytes.isEmpty()) {
            throw IllegalStateException("Failed to download BPS patch")
        }
        return@withContext basePatchResponse.md5 to bpsBytes
    }

    open suspend fun fetchPatchAndSeedInfo(hash: String): SeedEntity =
        withContext(Dispatchers.IO) {
            // Check if we already have a saved RomEntity for this hash
            val cachedSeed = seedDao.getSeed(hash)
            if (cachedSeed != null) {
                return@withContext cachedSeed
            }

            // GET https://alttpr.com/hash/{hash} -> retrieves seed patch
            val seedDetails = alttprService.getSeedPatch(hash)

            if (seedDetails.patch.isNullOrEmpty()) {
                throw IllegalStateException("Failed to download JSON patch")
            }

            return@withContext SeedEntity(
                hash = hash,
                logic = seedDetails.logic,
                generated = seedDetails.generated,
                size = seedDetails.size ?: 2,
                meta = seedDetails.spoiler?.meta,
                patch = seedDetails.patch
            )
        }
}
