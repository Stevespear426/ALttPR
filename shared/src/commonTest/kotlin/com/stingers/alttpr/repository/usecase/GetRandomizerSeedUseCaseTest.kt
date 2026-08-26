package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.model.RandomizerGameModel
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.repository.AlttprRepository
import com.stingers.alttpr.repository.local.AppPrefs
import com.stingers.alttpr.repository.local.LoggerDao
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetRandomizerSeedUseCaseTest {

    private val appPrefs = mock<AppPrefs>().apply {
        every { debugMode } returns flowOf(false)
    }
    private val loggerDao = mock<LoggerDao>()
    private val logger = Logger(appPrefs, loggerDao)
    private val repository = mock<AlttprRepository>()

    private val useCase = GetRandomizerSeedUseCase(
        logger = logger,
        repository = repository
    )

    @Test
    fun `test get randomizer seed success`() = runTest {
        val model = RandomizerGameModel()
        val expectedSeed = SeedEntity(hash = "rand123")
        everySuspend { repository.generateRandomizerSeed(model) } returns expectedSeed

        val result = useCase(model)

        assertTrue(result.isSuccess)
        assertEquals(expectedSeed, result.getOrNull())
        verifySuspend { repository.generateRandomizerSeed(model) }
    }

    @Test
    fun `test get randomizer seed failure`() = runTest {
        val model = RandomizerGameModel()
        val exception = RuntimeException("Generation failed")
        everySuspend { repository.generateRandomizerSeed(model) } throws exception

        val result = useCase(model)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verifySuspend { repository.generateRandomizerSeed(model) }
    }
}
