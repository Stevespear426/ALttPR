package com.stingers.alttpr.screens.randomizer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.model.AlttprApi
import com.stingers.alttpr.model.GameMode
import com.stingers.alttpr.model.GameModel
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.usecase.GetCustomizerSeedUseCase
import com.stingers.alttpr.repository.usecase.GetRandomizerSeedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class RandomizerViewModel(
    private val getRandomizerSeedUseCase: GetRandomizerSeedUseCase,
    private val getCustomizerSeedUseCase: GetCustomizerSeedUseCase,
    private val navigationManager: NavigationManager,
) : ViewModel() {

    private val _state = MutableStateFlow(RandomizerState())
    val state = _state.asStateFlow()

    fun processEvent(event: RandomizerEvent) {
        viewModelScope.launch {
            when (event) {
                is RandomizerEvent.GenerateGame -> createRandomizerSeed()
                is RandomizerEvent.GenerateRace -> createRandomizerSeed(true)
                is RandomizerEvent.SetPreset -> {
                    _state.value =
                        RandomizerState(preset = event.value, settings = event.value.model())

                }

                is RandomizerEvent.SetDungeonItems -> {
                    _state.update { it.copy(settings = it.settings.copy(dungeonItems = event.value)) }
                }

                is RandomizerEvent.SetGlitches -> {
                    _state.update { it.copy(settings = it.settings.copy(glitches = event.value)) }
                }

                is RandomizerEvent.SetItemAccessibility -> {
                    _state.update { it.copy(settings = it.settings.copy(accessibility = event.value)) }
                }

                is RandomizerEvent.SetItemPlacement -> {
                    _state.update { it.copy(settings = it.settings.copy(itemPlacement = event.value)) }
                }

                is RandomizerEvent.SetGanonCrystals -> {
                    _state.update { it.copy(settings = it.settings.copy(ganonCrystals = event.value)) }
                }

                is RandomizerEvent.SetGoal -> {
                    _state.update { it.copy(settings = it.settings.copy(goal = event.value)) }
                }

                is RandomizerEvent.SetTowerCrystals -> {
                    _state.update { it.copy(settings = it.settings.copy(towerCrystals = event.value)) }
                }

                is RandomizerEvent.SetBossShuffle -> {
                    _state.update { it.copy(settings = it.settings.copy(bossShuffle = event.value)) }
                }

                is RandomizerEvent.SetEnemyDamage -> {
                    _state.update { it.copy(settings = it.settings.copy(enemyDamage = event.value)) }
                }

                is RandomizerEvent.SetEnemyHealth -> {
                    _state.update { it.copy(settings = it.settings.copy(enemyHealth = event.value)) }
                }

                is RandomizerEvent.SetEnemyShuffle -> {
                    _state.update { it.copy(settings = it.settings.copy(enemyShuffle = event.value)) }
                }

                is RandomizerEvent.SetEntrances -> {
                    _state.update { it.copy(settings = it.settings.copy(entrances = event.value)) }
                }

                is RandomizerEvent.SetHints -> {
                    _state.update { it.copy(settings = it.settings.copy(hints = event.value)) }
                }

                is RandomizerEvent.SetItemFunctionality -> {
                    _state.update { it.copy(settings = it.settings.copy(itemFunctionality = event.value)) }
                }

                is RandomizerEvent.SetItemPool -> {
                    _state.update { it.copy(settings = it.settings.copy(itemPool = event.value)) }
                }

                is RandomizerEvent.SetWeapons -> {
                    _state.update { it.copy(settings = it.settings.copy(weapons = event.value)) }
                }

                is RandomizerEvent.SetWorldState -> {
                    _state.update { it.copy(settings = it.settings.copy(worldState = event.value)) }
                }

                is RandomizerEvent.SetPotShuffle -> {
                    _state.update { it.copy(settings = it.settings.copy(potShuffle = event.value)) }
                }

                is RandomizerEvent.SetPseduoboots -> {
                    _state.update { it.copy(settings = it.settings.copy(pseudoboots = event.value)) }
                }

                is RandomizerEvent.SetName -> {
                    _state.update { it.copy(settings = it.settings.copy(name = event.value)) }
                }
            }
        }
    }

    private suspend fun createRandomizerSeed(tournament: Boolean? = null) {
        _state.update { it.copy(loading = true, error = null) }

        when (state.value.preset.api) {
            AlttprApi.RANDOMIZER -> {
                getRandomizerSeedUseCase(state.value.settings.copy(tournament = tournament == true))
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

            AlttprApi.CUSTOMIZER -> {
                getCustomizerSeedUseCase(state.value.settings.copy(tournament = tournament == true))
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
    }
}

data class RandomizerState(
    val loading: Boolean = false,
    val error: String? = null,
    val preset: GameMode = GameMode.DEFAULT,
    val settings: GameModel = GameMode.DEFAULT.model()
)
