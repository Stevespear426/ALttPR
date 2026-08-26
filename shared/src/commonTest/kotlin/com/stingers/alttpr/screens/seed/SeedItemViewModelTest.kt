package com.stingers.alttpr.screens.seed

import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.repository.local.SeedDao
import com.stingers.alttpr.repository.usecase.ExportRomUseCase
import com.stingers.alttpr.repository.usecase.PlayRomUseCase
import com.stingers.alttpr.repository.usecase.SaveSeedUseCase
import dev.mokkery.answering.returns
import dev.mokkery.every
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

@OptIn(ExperimentalCoroutinesApi::class)
class SeedItemViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val seedDao = mock<SeedDao>()
    private val saveSeedUseCase = mock<SaveSeedUseCase>()
    private val exportRomUseCase = mock<ExportRomUseCase>()
    private val playRomUseCase = mock<PlayRomUseCase>()
    private val navigationManager = object : NavigationManager() {}

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initial state seed item`() = runTest(testDispatcher) {
        val seed = SeedEntity(hash = "seed123")
        every { seedDao.getSeedFlow("seed123") } returns flowOf(seed)

        val viewModel = SeedItemViewModel(
            seed = seed,
            seedDao = seedDao,
            saveSeedUseCase = saveSeedUseCase,
            exportRomUseCase = exportRomUseCase,
            playRomUseCase = playRomUseCase,
            navigationManager = navigationManager
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(seed, viewModel.state.value.seed)
        assertFalse(viewModel.state.value.loading)
    }
}
