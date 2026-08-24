package com.stingers.alttpr.screens.randomizer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.model.RandomizerGameMode
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.usecase.GetRandomizerSeedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class RandomizerViewModel(
    private val getRandomizerSeedUseCase: GetRandomizerSeedUseCase,
    private val navigationManager: NavigationManager,
) : ViewModel() {

    private val _state = MutableStateFlow(RandomizerState())
    val state = _state.asStateFlow()

    fun processEvent(event: RandomizerEvent) {
        viewModelScope.launch {
            when (event) {
                is RandomizerEvent.GenerateGame -> createRandomizerSeed(event.value)
            }
        }
    }

    private suspend fun createRandomizerSeed(mode: RandomizerGameMode) {
        _state.update { it.copy(loading = true, error = null) }

        getRandomizerSeedUseCase(mode.model())
            .onSuccess { seed ->
                _state.update { it.copy(loading = false) }
                navigationManager.navigateTo(Screen.EditRom(seed))
            }
            .onFailure { throwable ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = throwable.message ?: "Failed to generate random seed"
                    )
                }
            }
    }
}

data class RandomizerState(
    val loading: Boolean = false,
    val error: String? = null
)
