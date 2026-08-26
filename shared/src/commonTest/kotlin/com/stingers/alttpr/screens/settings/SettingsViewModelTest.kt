package com.stingers.alttpr.screens.settings

import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.repository.local.AppPrefs
import com.stingers.alttpr.repository.local.LoggerDao
import com.stingers.alttpr.repository.local.SeedDao
import com.stingers.alttpr.repository.local.SpriteDao
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
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
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val seedDao = mock<SeedDao>()
    private val spriteDao = mock<SpriteDao>()
    private val loggerDao = mock<LoggerDao>()
    private val appPrefs = mock<AppPrefs>()
    private val navigationManager = object : NavigationManager() {}

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { appPrefs.debugMode } returns flowOf(false)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initial settings state`() = runTest(testDispatcher) {
        val viewModel = SettingsViewModel(
            seedDao = seedDao,
            spriteDao = spriteDao,
            loggerDao = loggerDao,
            appPrefs = appPrefs,
            navigationManager = navigationManager
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.debugMode)
    }

    @Test
    fun `test clear data`() = runTest(testDispatcher) {
        everySuspend { seedDao.deleteAllSeeds() } returns Unit
        everySuspend { loggerDao.deleteAllLogs() } returns Unit
        everySuspend { spriteDao.deleteAllSprites() } returns Unit

        val viewModel = SettingsViewModel(
            seedDao = seedDao,
            spriteDao = spriteDao,
            loggerDao = loggerDao,
            appPrefs = appPrefs,
            navigationManager = navigationManager
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.clearData()
        testDispatcher.scheduler.advanceUntilIdle()

        verifySuspend { seedDao.deleteAllSeeds() }
        verifySuspend { loggerDao.deleteAllLogs() }
        verifySuspend { spriteDao.deleteAllSprites() }
    }
}
