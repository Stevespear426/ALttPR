package com.stingers.alttpr.screens.sprites

import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.repository.AlttprRepository
import com.stingers.alttpr.repository.local.RomPrefs
import com.stingers.alttpr.repository.usecase.GetSavedSpriteUseCase
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

@OptIn(ExperimentalCoroutinesApi::class)
class SpriteViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val alttprRepository = mock<AlttprRepository>()
    private val romPrefs = mock<RomPrefs>()
    private val getSavedSpriteUseCase = mock<GetSavedSpriteUseCase>()
    private val navigationManager = object : NavigationManager() {}

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getSavedSpriteUseCase.invoke() } returns flowOf(null)
        everySuspend { alttprRepository.getSprites() } returns Result.success(
            listOf(
                Sprite(fileUrl = "url1", name = "Link", author = "Nintendo", tags = listOf("hero")),
                Sprite(fileUrl = "url2", name = "Samus", author = "Retro", tags = listOf("bounty"))
            )
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test fetch sprites on init and filter`() = runTest(testDispatcher) {
        val viewModel = SpriteViewModel(
            alttprRepository = alttprRepository,
            romPrefs = romPrefs,
            getSavedSpriteUseCase = getSavedSpriteUseCase,
            navigationManager = navigationManager
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.loading)
        assertEquals(2, viewModel.state.value.sprites.size)

        viewModel.processEvent(SpriteEvent.UpdateSearchQuery("Samus"))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.state.value.sprites.size)
        assertEquals("Samus", viewModel.state.value.sprites[0].name)
    }
}
