package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.model.RandomizerGameModel
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.model.api.GenerateSeedRequest
import com.stingers.alttpr.repository.AlttprRepository
import org.koin.core.annotation.Factory

@Factory
open class GetRandomizerSeedUseCase(
    private val logger: Logger,
    private val repository: AlttprRepository,
) {

    open suspend operator fun invoke(model: RandomizerGameModel): Result<SeedEntity> = runCatching {
        repository.generateRandomizerSeed(model)
    }.onFailure { e ->
        logger.e(TAG, "Failed to generate Seed", e)
    }

    companion object {
        private const val TAG = "GetRandomizerSeedUseCase"
    }
}
