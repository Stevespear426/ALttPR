package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.model.HeartSpeed
import com.stingers.alttpr.model.MenuSpeed
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.repository.AlttprRepository
import com.stingers.alttpr.repository.RomManager
import com.stingers.alttpr.repository.local.AppPrefs
import com.stingers.alttpr.repository.local.LoggerDao
import com.stingers.alttpr.repository.local.RomPrefs
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.FileNotFoundException
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GetPatchedRomBytesUseCaseTest {

    private val appPrefs = mock<AppPrefs>().apply {
        every { debugMode } returns flowOf(false)
    }
    private val loggerDao = mock<LoggerDao>()
    private val logger = Logger(appPrefs, loggerDao)
    private val alttprRepositoryForRom = mock<AlttprRepository>()
    private val romManager = RomManager(alttprRepositoryForRom)
    private val romPrefs = mock<RomPrefs>()
    private val alttprRepository = mock<AlttprRepository>()

    private val useCase = GetPatchedRomBytesUseCase(
        logger = logger,
        romManager = romManager,
        romPrefs = romPrefs,
        alttprRepository = alttprRepository
    )

    @Test
    fun `test get patched rom bytes failure when null or empty`() = runTest {
        val seed = SeedEntity(hash = "hash123")
        val sprites = emptyList<Sprite>()

        every { romPrefs.heartSpeed } returns flowOf(HeartSpeed.NORMAL)
        every { romPrefs.menuSpeed } returns flowOf(MenuSpeed.NORMAL)
        every { romPrefs.heartColor } returns flowOf(HeartColor.RED)
        every { romPrefs.quickSwap } returns flowOf(true)
        every { romPrefs.enableMusic } returns flowOf(true)
        every { romPrefs.msuResume } returns flowOf(false)
        every { romPrefs.reduceFlashing } returns flowOf(false)
        every { romPrefs.sprite } returns flowOf(null)

        everySuspend { alttprRepository.getSprites() } returns Result.success(sprites)

        val result = useCase(seed)

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }
}
