package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.common.ROM_FILE_EXTENSION_DOT
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.repository.local.RomStorage
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFileWithDefaultApplication
import org.koin.core.annotation.Factory

@Factory
class PlayRomUseCase(
    private val logger: Logger,
    private val saveSeedUseCase: SaveSeedUseCase,
    private val getPatchedRomBytesUseCase: GetPatchedRomBytesUseCase,
) {

    suspend operator fun invoke(seed: SeedEntity): Result<Unit> = runCatching {
        val fileName = seed.getFileName() + ROM_FILE_EXTENSION_DOT
        val savedSeed = saveSeedUseCase(seed).getOrThrow()
        val rom = getPatchedRomBytesUseCase(savedSeed).getOrThrow()
        RomStorage.saveShareRomBytes(fileName, rom).getOrThrow()
        RomStorage.getShareRomFile(fileName)?.let {
            FileKit.openFileWithDefaultApplication(it)
        }
        Unit
    }.onFailure { e ->
        logger.e(TAG, "Failed to open rom: ${seed.hash}", e)
    }

    companion object {
        private const val TAG = "PlayRomUseCase"
    }
}
