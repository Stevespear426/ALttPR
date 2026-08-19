package com.stingers.alttpr.screens.generator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.AlttprRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
class GeneratorViewModel(
    private val alttprRepository: AlttprRepository,
    private val navigationManager: NavigationManager,
) : ViewModel() {

    val state = MutableStateFlow(GeneratorState())

    fun processEvent(event: GeneratorEvent) {
        viewModelScope.launch {
            when (event) {
                GeneratorEvent.GenerateDaily -> createDailySeed()
                GeneratorEvent.GenerateRandom -> {}
                is GeneratorEvent.NavigateTo -> navigationManager.navigateTo(event.value)
            }
        }
    }

    fun createDailySeed() {
        state.value = GeneratorState(loading = true)
        viewModelScope.launch {
            val result = alttprRepository.createDailySeed()
            result.onSuccess { hash ->
                navigationManager.navigateTo(Screen.EditRom(hash))
                delay(5.seconds)
                state.value = GeneratorState(loading = false)
            }
            result.onFailure {
                state.value = GeneratorState(loading = false)
            }
        }
    }
}

data class GeneratorState(
    val loading: Boolean = false
)