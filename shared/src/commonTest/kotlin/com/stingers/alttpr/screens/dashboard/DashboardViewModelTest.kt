package com.stingers.alttpr.screens.dashboard

import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.local.SeedDao
import com.stingers.alttpr.repository.usecase.GetDailySeedUseCase
import com.stingers.alttpr.repository.usecase.GetRandomizerSeedUseCase
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val navigationManager = object : NavigationManager() {
        var navigatedToScreen: Screen? = null
        override fun navigateTo(screen: Screen) {
            navigatedToScreen = screen
        }
    }
    private val getDailySeedUseCase = mock<GetDailySeedUseCase>()
    private val getRandomizerSeedUseCase = mock<GetRandomizerSeedUseCase>()
    private val seedDao = mock<SeedDao>()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { seedDao.getRecentSeed() } returns flowOf(null)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test daily seed success on init`() = runTest(testDispatcher) {
        val seed = SeedEntity(hash = "daily123")
        everySuspend { getDailySeedUseCase() } returns Result.success(seed)

        val viewModel = DashboardViewModel(
            navigationManager = navigationManager,
            getDailySeedUseCase = getDailySeedUseCase,
            getRandomizerSeedUseCase = getRandomizerSeedUseCase,
            seedDao = seedDao
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.loading)
        assertEquals(seed, viewModel.state.value.dailySeed)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `test daily seed failure on init`() = runTest(testDispatcher) {
        everySuspend { getDailySeedUseCase() } returns Result.failure(RuntimeException("Network error"))

        val viewModel = DashboardViewModel(
            navigationManager = navigationManager,
            getDailySeedUseCase = getDailySeedUseCase,
            getRandomizerSeedUseCase = getRandomizerSeedUseCase,
            seedDao = seedDao
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.loading)
        assertNull(viewModel.state.value.dailySeed)
        assertEquals("Network error", viewModel.state.value.error)
    }

//    @Test
//    fun `test generate race game event`() = runTest(testDispatcher) {
//        val seed = SeedEntity(hash = "race123")
//        everySuspend { getDailySeedUseCase() } returns Result.failure(RuntimeException())
//        everySuspend { getRandomizerSeedUseCase(any()) } returns Result.success(seed)
//
//        val viewModel = DashboardViewModel(
//            navigationManager = navigationManager,
//            getDailySeedUseCase = getDailySeedUseCase,
//            getRandomizerSeedUseCase = getRandomizerSeedUseCase,
//            seedDao = seedDao
//        )
//        testDispatcher.scheduler.advanceUntilIdle()
//
//            viewModel.processEvent(DashboardEvent.GenerateRaceGame)
//            testDispatcher.scheduler.advanceUntilIdle()
//            assertTrue(navigationManager.navigatedToScreen is Screen.EditRom)
//    }
//
//    @Test
//    fun `test generate mystery game event`() = runTest(testDispatcher) {
//        val seed = SeedEntity(hash = "mystery123")
//        everySuspend { getDailySeedUseCase() } returns Result.failure(RuntimeException())
//        everySuspend { getRandomizerSeedUseCase(any()) } returns Result.success(seed)
//
//        val viewModel = DashboardViewModel(
//            navigationManager = navigationManager,
//            getDailySeedUseCase = getDailySeedUseCase,
//            getRandomizerSeedUseCase = getRandomizerSeedUseCase,
//            seedDao = seedDao
//        )
//        testDispatcher.scheduler.advanceUntilIdle()
//
//            viewModel.processEvent(DashboardEvent.GenerateMysteryGame)
//            testDispatcher.scheduler.advanceUntilIdle()
//            assertTrue(navigationManager.navigatedToScreen is Screen.EditRom)
//    }
}
