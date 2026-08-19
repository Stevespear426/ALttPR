package com.stingers.alttpr.screens.randomizer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.model.RandomizerGameMode
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.AlttprRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class RandomizerViewModel(
    private val alttprRepository: AlttprRepository,
    private val navigationManager: NavigationManager,
) : ViewModel() {

    val state = MutableStateFlow(RandomizerState())

    fun processEvent(event: RandomizerEvent) {
        viewModelScope.launch {
            when (event) {
                is RandomizerEvent.GenerateGame -> createRandomizerSeed(event.value)
            }
        }
    }

    fun createRandomizerSeed(mode: RandomizerGameMode) {
        state.value = RandomizerState(loading = true)
        viewModelScope.launch {
            val result = alttprRepository.generateRandomizerSeed(mode.request())
            result.onSuccess { hash ->
                navigationManager.navigateTo(Screen.EditRom(hash))
                state.value = RandomizerState(loading = false)
            }
            result.onFailure {
                state.value = RandomizerState(loading = false)
            }
        }
    }
}

data class RandomizerState(
    val loading: Boolean = false,
)