package com.stingers.alttpr.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.model.RandomizerGameMode
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.model.api.Spoilers
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.local.SeedDao
import com.stingers.alttpr.repository.usecase.GetDailySeedUseCase
import com.stingers.alttpr.repository.usecase.GetRandomizerSeedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class DashboardViewModel(
    private val navigationManager: NavigationManager,
    private val getDailySeedUseCase: GetDailySeedUseCase,
    private val getRandomizerSeedUseCase: GetRandomizerSeedUseCase,
    private val seedDao: SeedDao
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    fun processEvent(event: DashboardEvent) {
        viewModelScope.launch {
            when (event) {
                is DashboardEvent.GenerateRaceGame -> createRaceSeed()
                is DashboardEvent.GenerateMysteryGame -> createMysterySeed()
                is DashboardEvent.RefreshData -> createDailySeed()
                is DashboardEvent.NavigateTo -> navigationManager.navigateTo(event.value)
            }
        }
    }

    private fun createDailySeed() {
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            getDailySeedUseCase()
                .onSuccess { seed ->
                    _state.update { it.copy(dailySeed = seed, loading = false, error = null) }
                }
                .onFailure { throwable ->
                    _state.update {
                        it.copy(
                            dailySeed = null,
                            loading = false,
                            error = throwable.message ?: "Failed to generate daily seed"
                        )
                    }
                }
        }
    }

    private suspend fun createMysterySeed() {
        _state.update { it.copy(loading = true, error = null) }

        val randomMode = RandomizerGameMode.entries
            .filterNot { it == RandomizerGameMode.CUSTOM }
            .random()

        getRandomizerSeedUseCase(randomMode.request().copy(spoilers = Spoilers.Mystery.value))
            .onSuccess { seed ->
                _state.update { it.copy(loading = false) }
                navigationManager.navigateTo(Screen.EditRom(seed))
            }
            .onFailure { throwable ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = throwable.message ?: "Failed to generate mystery seed"
                    )
                }
            }
    }

    private suspend fun createRaceSeed() {
        _state.update { it.copy(loading = true, error = null) }

        val randomMode = RandomizerGameMode.entries
            .filterNot { it == RandomizerGameMode.CUSTOM }
            .random()

        getRandomizerSeedUseCase(randomMode.request().copy(tournament = true))
            .onSuccess { seed ->
                _state.update { it.copy(loading = false) }
                navigationManager.navigateTo(Screen.EditRom(seed))
            }
            .onFailure { throwable ->
                _state.update {
                    it.copy(
                        loading = false,
                        error = throwable.message ?: "Failed to generate race seed"
                    )
                }
            }
    }

    private fun getRecentSeed() {
        seedDao.getRecentSeed().onEach { seed ->
            _state.update { it.copy(recentSeed = seed) }
        }.launchIn(viewModelScope)
    }

    init {
        createDailySeed()
        getRecentSeed()
    }
}

data class DashboardState(
    val loading: Boolean = true,
    val error: String? = null,
    val dailySeed: SeedEntity? = null,
    val recentSeed: SeedEntity? = null
)
