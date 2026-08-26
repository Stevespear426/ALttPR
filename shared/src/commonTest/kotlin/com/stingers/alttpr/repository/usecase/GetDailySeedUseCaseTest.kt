package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
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

class GetDailySeedUseCaseTest {

    private val appPrefs = mock<AppPrefs>().apply {
        every { debugMode } returns flowOf(false)
    }
    private val loggerDao = mock<LoggerDao>()
    private val logger = Logger(appPrefs, loggerDao)
    private val repository = mock<AlttprRepository>()

    private val useCase = GetDailySeedUseCase(
        logger = logger,
        repository = repository
    )

    @Test
    fun `test get daily seed success`() = runTest {
        val expectedSeed = SeedEntity(hash = "daily123", logic = "no-logic")
        everySuspend { repository.createDailySeed() } returns expectedSeed

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(expectedSeed, result.getOrNull())
        verifySuspend { repository.createDailySeed() }
    }

    @Test
    fun `test get daily seed failure`() = runTest {
        val exception = RuntimeException("Network error")
        everySuspend { repository.createDailySeed() } throws exception

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verifySuspend { repository.createDailySeed() }
    }
}
