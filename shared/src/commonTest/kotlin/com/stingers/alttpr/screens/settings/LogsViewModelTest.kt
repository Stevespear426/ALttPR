package com.stingers.alttpr.screens.settings

import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.repository.local.LoggerDao
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
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
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LogsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val loggerDao = mock<LoggerDao>()
    private val navigationManager = object : NavigationManager() {}

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { loggerDao.getLogs(any<String?>(), any<String>()) } returns flowOf(emptyList())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initial state logs viewmodel`() = runTest(testDispatcher) {
        val viewModel = LogsViewModel(
            loggerDao = loggerDao,
            navigationManager = navigationManager
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("", viewModel.state.value.filter)
        assertEquals(null, viewModel.state.value.logType)
        assertEquals(emptyList(), viewModel.state.value.logs)
    }

    @Test
    fun `test clear logs event`() = runTest(testDispatcher) {
        everySuspend { loggerDao.deleteAllLogs() } returns Unit

        val viewModel = LogsViewModel(
            loggerDao = loggerDao,
            navigationManager = navigationManager
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.processEvent(LogsEvent.ClearLogs)
        testDispatcher.scheduler.advanceUntilIdle()

        verifySuspend { loggerDao.deleteAllLogs() }
    }
}
