package com.stingers.alttpr.screens.randomizer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.AlttprRepository
import com.stingers.alttpr.screens.generator.GeneratorEvent
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
                RandomizerEvent.GenerateGame -> {}
            }
        }
    }
}

data class RandomizerState(
    val loading: Boolean = false
)