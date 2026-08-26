package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.repository.AlttprRepository
import org.koin.core.annotation.Factory

@Factory
open class GetDailySeedUseCase(
    private val logger: Logger,
    private val repository: AlttprRepository,
) {

    open suspend operator fun invoke(): Result<SeedEntity> = runCatching {
        repository.createDailySeed()
    }.onFailure { e ->
        logger.e(TAG, "Failed to retrieve Daily Seed", e)
    }

    companion object {
        private const val TAG = "GetDailySeedUseCase"
    }
}
