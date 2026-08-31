package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.model.GameModel
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

class GetCustomizerSeedUseCaseTest {

    private val appPrefs = mock<AppPrefs>().apply {
        every { debugMode } returns flowOf(false)
    }
    private val loggerDao = mock<LoggerDao>()
    private val logger = Logger(appPrefs, loggerDao)
    private val repository = mock<AlttprRepository>()

    private val useCase = GetCustomizerSeedUseCase(
        logger = logger,
        repository = repository
    )

    @Test
    fun `test get customizer seed success`() = runTest {
        val model = GameModel()
        val expectedSeed = SeedEntity(hash = "custom123")
        everySuspend { repository.generateCustomizerSeed(model) } returns expectedSeed

        val result = useCase(model)

        assertTrue(result.isSuccess)
        assertEquals(expectedSeed, result.getOrNull())
        verifySuspend { repository.generateCustomizerSeed(model) }
    }

    @Test
    fun `test get customizer seed failure`() = runTest {
        val model = GameModel()
        val exception = RuntimeException("Generation failed")
        everySuspend { repository.generateCustomizerSeed(model) } throws exception

        val result = useCase(model)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verifySuspend { repository.generateCustomizerSeed(model) }
    }
}
