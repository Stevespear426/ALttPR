package com.stingers.alttpr.screens.edit

import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.model.HeartSpeed
import com.stingers.alttpr.model.MenuSpeed
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.model.api.PaletteAlgorithm
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.repository.local.RomPrefs
import com.stingers.alttpr.repository.local.SeedDao
import com.stingers.alttpr.repository.usecase.ExportRomUseCase
import com.stingers.alttpr.repository.usecase.GetRandomizerSeedUseCase
import com.stingers.alttpr.repository.usecase.GetSavedSpriteUseCase
import com.stingers.alttpr.repository.usecase.PlayRomUseCase
import com.stingers.alttpr.repository.usecase.SaveSeedUseCase
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class EditRomViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getSavedSpriteUseCase = mock<GetSavedSpriteUseCase>()
    private val seedDao = mock<SeedDao>()
    private val romPrefs = mock<RomPrefs>()
    private val saveSeedUseCase = mock<SaveSeedUseCase>()
    private val exportRomUseCase = mock<ExportRomUseCase>()
    private val playRomUseCase = mock<PlayRomUseCase>()
    private val getRandomizerSeedUseCase = mock<GetRandomizerSeedUseCase>()
    private val navigationManager = object : NavigationManager() {}

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { romPrefs.quickSwap } returns flowOf(true)
        every { romPrefs.reduceFlashing } returns flowOf(false)
        every { romPrefs.enableMusic } returns flowOf(true)
        every { romPrefs.msuResume } returns flowOf(true)
        every { romPrefs.heartSpeed } returns flowOf(HeartSpeed.NORMAL)
        every { romPrefs.menuSpeed } returns flowOf(MenuSpeed.NORMAL)
        every { romPrefs.heartColor } returns flowOf(HeartColor.RED)
        every { romPrefs.shuffleSfx } returns flowOf(false)
        every { romPrefs.paletteShuffle } returns flowOf(false)
        every { romPrefs.paletteAlgorithm } returns flowOf(PaletteAlgorithm.Maseya)
        every { getSavedSpriteUseCase.invoke() } returns flowOf(null)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initial state loads correctly`() = runTest(testDispatcher) {
        val seed = SeedEntity(hash = "hash123")
        every { seedDao.getSeedFlow("hash123") } returns flowOf(null)

        val viewModel = EditRomViewModel(
            seed = seed,
            getSavedSpriteUseCase = getSavedSpriteUseCase,
            seedDao = seedDao,
            romPrefs = romPrefs,
            saveSeedUseCase = saveSeedUseCase,
            exportRomUseCase = exportRomUseCase,
            playRomUseCase = playRomUseCase,
            getRandomizerSeedUseCase = getRandomizerSeedUseCase,
            navigationManager = navigationManager
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(seed, viewModel.state.value.seed)
        assertTrue(viewModel.state.value.quickSwap)
        assertFalse(viewModel.state.value.isSaved)
    }

    @Test
    fun `test process event set quick swap`() = runTest(testDispatcher) {
        val seed = SeedEntity(hash = "hash123")
        every { seedDao.getSeedFlow("hash123") } returns flowOf(null)
        everySuspend { romPrefs.setQuickSwap(false) } returns Unit

        val viewModel = EditRomViewModel(
            seed = seed,
            getSavedSpriteUseCase = getSavedSpriteUseCase,
            seedDao = seedDao,
            romPrefs = romPrefs,
            saveSeedUseCase = saveSeedUseCase,
            exportRomUseCase = exportRomUseCase,
            playRomUseCase = playRomUseCase,
            getRandomizerSeedUseCase = getRandomizerSeedUseCase,
            navigationManager = navigationManager
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.processEvent(EditRomEvent.SetQuickSwap(false))
        testDispatcher.scheduler.advanceUntilIdle()

        dev.mokkery.verifySuspend { romPrefs.setQuickSwap(false) }
    }
}
