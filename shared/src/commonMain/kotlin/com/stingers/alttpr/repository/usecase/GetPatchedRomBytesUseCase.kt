 package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.repository.AlttprRepository
import com.stingers.alttpr.repository.RomManager
import com.stingers.alttpr.repository.local.RomPrefs
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
class GetPatchedRomBytesUseCase(
    private val logger: Logger,
    private val romManager: RomManager,
    private val romPrefs: RomPrefs,
    private val alttprRepository: AlttprRepository
) {

    suspend operator fun invoke(seedEntity: SeedEntity): Result<ByteArray> = runCatching {
        val heartSpeed = romPrefs.heartSpeed.first()
        val menuSpeed = romPrefs.menuSpeed.first()
        val heartColor = romPrefs.heartColor.first()
        val quickSwap = romPrefs.quickSwap.first()
        val enableMusic = romPrefs.enableMusic.first()
        val msuResume = romPrefs.msuResume.first()
        val reduceFlashing = romPrefs.reduceFlashing.first()
        val spriteName = romPrefs.sprite.first()

        val spritesResult = alttprRepository.getSprites()
        val sprites = spritesResult.getOrDefault(emptyList())
        val selectedSprite = sprites.find { it.name == spriteName }

        val romBytes = romManager.getPatchedRomBytes(
            seedEntity = seedEntity,
            heartSpeed = heartSpeed,
            menuSpeed = menuSpeed,
            heartColor = heartColor,
            quickSwap = quickSwap,
            enableMusic = enableMusic,
            msuResume = msuResume,
            reduceFlashing = reduceFlashing,
            sprite = selectedSprite
        )

        if (romBytes == null || romBytes.isEmpty()) {
            throw IllegalStateException("Failed to generate patched ROM bytes")
        }

        romBytes
    }.onFailure { e ->
        logger.e(TAG, "Failed to get patched ROM bytes", e)
    }

    companion object {
        private const val TAG = "GetPatchedRomBytesUseCase"
    }
}
