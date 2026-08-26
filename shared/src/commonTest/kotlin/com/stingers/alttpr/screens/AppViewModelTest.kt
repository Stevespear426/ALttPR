package com.stingers.alttpr.screens

import com.stingers.alttpr.AppViewModel
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.RomManager
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val romManager = mock<RomManager>()
    private val navigationManager = object : NavigationManager() {
        var setRootCalledWith: Screen? = null
        override fun setRoot(screen: Screen) {
            setRootCalledWith = screen
        }
    }

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test base rom missing navigates to UploadRom`() = runTest(testDispatcher) {
        everySuspend { romManager.hasValidBaseRom() } returns false

        val viewModel = AppViewModel(romManager, navigationManager)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.loading)
        assertTrue(viewModel.state.value.needsBaseRom)
        assertTrue(navigationManager.setRootCalledWith is Screen.UploadRom)
    }

    @Test
    fun `test valid base rom navigates to Main`() = runTest(testDispatcher) {
        everySuspend { romManager.hasValidBaseRom() } returns true

        val viewModel = AppViewModel(romManager, navigationManager)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.loading)
        assertFalse(viewModel.state.value.needsBaseRom)
        assertTrue(navigationManager.setRootCalledWith is Screen.Main)
    }
}
