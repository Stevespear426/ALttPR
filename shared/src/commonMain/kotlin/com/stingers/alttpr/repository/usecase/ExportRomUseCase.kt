package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.common.ROM_FILE_EXTENSION
import com.stingers.alttpr.model.SeedEntity
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.write
import org.koin.core.annotation.Factory

@Factory
class ExportRomUseCase(
    private val logger: Logger,
    private val saveSeedUseCase: SaveSeedUseCase,
    private val getPatchedRomBytesUseCase: GetPatchedRomBytesUseCase,
) {

    suspend operator fun invoke(seed: SeedEntity): Result<Unit> = runCatching {
        val savedSeed = saveSeedUseCase(seed).getOrThrow()
        val romBytes = getPatchedRomBytesUseCase(savedSeed).getOrThrow()
        val file = FileKit.openFileSaver(
            suggestedName = savedSeed.getFileName(),
            defaultExtension = ROM_FILE_EXTENSION
        )
        file?.write(romBytes)
        Unit
    }.onFailure { e ->
        logger.e(TAG, "Failed to export rom: ${seed.hash}", e)
    }

    companion object {
        private const val TAG = "ExportRomUseCase"
    }
}
