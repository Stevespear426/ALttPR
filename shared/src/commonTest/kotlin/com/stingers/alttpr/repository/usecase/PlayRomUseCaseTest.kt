package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.repository.AlttprRepository
import com.stingers.alttpr.repository.RomManager
import com.stingers.alttpr.repository.local.AppPrefs
import com.stingers.alttpr.repository.local.LoggerDao
import com.stingers.alttpr.repository.local.RomPrefs
import com.stingers.alttpr.repository.local.SeedDao
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class PlayRomUseCaseTest {

    private val appPrefs = mock<AppPrefs>().apply {
        every { debugMode } returns flowOf(false)
    }
    private val loggerDao = mock<LoggerDao>()
    private val logger = Logger(appPrefs, loggerDao)
    private val alttprRepo = mock<AlttprRepository>()
    private val seedDao = mock<SeedDao>()
    private val saveSeedUseCase = SaveSeedUseCase(logger, alttprRepo, seedDao)
    private val romManager = RomManager(alttprRepo)
    private val romPrefs = mock<RomPrefs>()
    private val getPatchedRomBytesUseCase = GetPatchedRomBytesUseCase(logger, romManager, romPrefs, alttprRepo)

    private val useCase = PlayRomUseCase(
        logger = logger,
        saveSeedUseCase = saveSeedUseCase,
        getPatchedRomBytesUseCase = getPatchedRomBytesUseCase
    )

    @Test
    fun `test play rom failure when save seed fails`() = runTest {
        val seed = SeedEntity(hash = "hash123")
        everySuspend { alttprRepo.getBpsPatch("hash123") } returns Pair("md5sum", byteArrayOf())

        val result = useCase(seed)

        assertTrue(result.isFailure)
    }
}
