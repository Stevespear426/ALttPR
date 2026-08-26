package com.stingers.alttpr.screens.upload

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.RomManager
import com.stingers.alttpr.repository.local.AppPrefs
import com.stingers.alttpr.repository.local.LoggerDao
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import io.github.vinceglb.filekit.PlatformFile
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

@OptIn(ExperimentalCoroutinesApi::class)
class UploadViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val appPrefs = mock<AppPrefs>().apply {
        every { debugMode } returns flowOf(false)
    }
    private val loggerDao = mock<LoggerDao>()
    private val logger = Logger(appPrefs, loggerDao)
    private val romManager = mock<RomManager>().apply {
        everySuspend { saveAndVerifyRom(any()) } returns Result.success(Unit)
    }
    private val navigationManager = mock<NavigationManager>().apply {
        every { setRoot(any()) } returns Unit
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
    fun `test save rom failure logs and does not navigate`() = runTest(testDispatcher) {
        val platformFile = PlatformFile("")
        val viewModel = UploadViewModel(
            logger = logger,
            romManager = romManager,
            navigationManager = navigationManager
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.precessEvent(UploadRomEvent.SaveRom(platformFile))
        testDispatcher.scheduler.advanceUntilIdle()

        verifySuspend { romManager.saveAndVerifyRom(platformFile) }
        verify { navigationManager.setRoot(Screen.Main) }
    }
}
