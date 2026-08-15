package com.stingers.alttpr.domain

import com.stingers.alttpr.network.AlttprService
import com.stingers.alttpr.data.db.RomDao
import com.stingers.alttpr.data.db.RomEntity
import com.stingers.alttpr.domain.model.GameMode
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton

@Singleton
class AlttprRepository(
    private val alttprService: AlttprService,
    private val romManager: RomManager,
    private val romDao: RomDao
) {

    suspend fun createDailySeed(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // 1. GET https://alttpr.com/api/daily -> gets hash
            val dailyResponse = alttprService.getDaily()
            val hash = dailyResponse.hash

            // 2. GET https://alttpr.com/api/h/{hash} -> retrieves bpsLocation
            val seedResponse = alttprService.getSeed(hash)
            val bpsLocation = seedResponse.bpsLocation

            // 3. GET https://alttpr.com{bpsLocation} -> downloads raw .bps patch binary
            val bpsBytes = alttprService.getBpsPatch(bpsLocation)

            // 4. RomManager.applyPatch(bpsBytes) -> outputs patched .sfc ROM
            val patchedRomBytes = romManager.applyPatch(bpsBytes)
            if (patchedRomBytes.isEmpty()) {
                return@withContext Result.failure(IllegalStateException("Failed to apply BPS patch or base ROM missing"))
            }

            // 5. Format filename: ALTTPR_(Month day year HH:MM:SS)_(hash from first api call).sfc
            val timestamp = getCurrentTimestampFormatted()
            val filename = "ALTTPR_${timestamp}_${hash}.sfc"

            // 6. Save to generated seeds bucket
            RomStorage.saveGeneratedSeed(filename, patchedRomBytes)

            // 7. Save record into database
            romDao.insertRom(
                RomEntity(
                    hash = hash,
                    createdAt = kotlin.time.Clock.System.now().toEpochMilliseconds(),
                    localFileName = filename,
                    gameMode = GameMode.DAILY_CHALLENGE
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getCurrentTimestampFormatted(): String {
        val currentInstant = kotlin.time.Clock.System.now()
        val dateTime = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault())
        val month = dateTime.monthNumber.toString().padStart(2, '0')
        val day = dateTime.dayOfMonth.toString().padStart(2, '0')
        val year = dateTime.year.toString()
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')
        val second = dateTime.second.toString().padStart(2, '0')
        return "${month}_${day}_${year}_${hour}_${minute}_${second}"
    }
}
