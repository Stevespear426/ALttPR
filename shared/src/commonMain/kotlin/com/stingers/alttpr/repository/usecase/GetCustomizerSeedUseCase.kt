package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.model.GameModel
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.repository.AlttprRepository
import org.koin.core.annotation.Factory

@Factory
open class GetCustomizerSeedUseCase(
    private val logger: Logger,
    private val repository: AlttprRepository,
) {

    open suspend operator fun invoke(model: GameModel): Result<SeedEntity> = runCatching {
        repository.generateCustomizerSeed(model)
    }.onFailure { e ->
        logger.e(TAG, "Failed to generate customizer seed", e)
    }

    companion object {
        private const val TAG = "GetCustomizerSeedUseCase"
    }
}
