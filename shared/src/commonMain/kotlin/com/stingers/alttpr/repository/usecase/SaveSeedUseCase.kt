package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.repository.AlttprRepository
import com.stingers.alttpr.repository.local.RomStorage
import com.stingers.alttpr.repository.local.SeedDao
import com.stingers.alttpr.utils.currentTimeInMillis
import io.github.vinceglb.filekit.exists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
open class SaveSeedUseCase(
    private val logger: Logger,
    private val repository: AlttprRepository,
    private val seedDao: SeedDao,
) {

    open suspend operator fun invoke(seed: SeedEntity): Result<SeedEntity> = runCatching {
        val fileExists = !seed.localFileName.isNullOrEmpty() &&
                RomStorage.getGeneratedSeedFile(seed.localFileName)?.exists() == true

        if (!seed.md5.isNullOrEmpty() && fileExists) {
            seedDao.insertSeed(seed)
            return@runCatching seed
        }

        val (md5, patchBytes) = repository.getBpsPatch(seed.hash)
        val filename = "${seed.hash}.bps"

        withContext(Dispatchers.IO) {
            RomStorage.saveGeneratedSeed(filename, patchBytes)
        }

        val updatedSeed = seed.copy(
            md5 = md5,
            localFileName = filename,
            updated = currentTimeInMillis()
        )

        seedDao.insertSeed(updatedSeed)
        updatedSeed
    }.onFailure { e ->
        logger.e(TAG, "Failed to save seed: ${seed.hash}", e)
    }

    companion object {
        private const val TAG = "SaveSeedUseCase"
    }
}
