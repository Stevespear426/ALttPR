package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.repository.AlttprRepository
import com.stingers.alttpr.repository.local.AppPrefs
import com.stingers.alttpr.repository.local.LoggerDao
import com.stingers.alttpr.repository.local.SeedDao
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SaveSeedUseCaseTest {

    private val appPrefs = mock<AppPrefs>().apply {
        every { debugMode } returns flowOf(false)
    }
    private val loggerDao = mock<LoggerDao>()
    private val logger = Logger(appPrefs, loggerDao)
    private val repository = mock<AlttprRepository>()
    private val seedDao = mock<SeedDao>()

    private val useCase = SaveSeedUseCase(
        logger = logger,
        repository = repository,
        seedDao = seedDao
    )

    @Test
    fun `test save seed when md5 is present and local file exists`() = runTest {
        val seed = SeedEntity(hash = "hash123", md5 = null, localFileName = null)
        val patchBytes = byteArrayOf(0x01, 0x02)

        everySuspend { repository.getBpsPatch("hash123") } returns Pair("md5sum", patchBytes)
        everySuspend { seedDao.insertSeed(any<SeedEntity>()) } returns Unit

        val result = useCase(seed)

        assertTrue(result.isSuccess)
        val savedSeed = result.getOrNull()
        assertEquals("md5sum", savedSeed?.md5)
        assertEquals("hash123.bps", savedSeed?.localFileName)
        verifySuspend { repository.getBpsPatch("hash123") }
        verifySuspend { seedDao.insertSeed(any<SeedEntity>()) }
    }
}
