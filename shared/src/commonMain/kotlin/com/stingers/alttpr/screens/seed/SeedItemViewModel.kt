package com.stingers.alttpr.screens.seed

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.generate_rom_failed
import alttpr.shared.generated.resources.save_rom_failed
import alttpr.shared.generated.resources.save_seed_failed
import alttpr.shared.generated.resources.save_seed_success
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.local.SeedDao
import com.stingers.alttpr.repository.usecase.ExportRomUseCase
import com.stingers.alttpr.repository.usecase.PlayRomUseCase
import com.stingers.alttpr.repository.usecase.SaveSeedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SeedItemViewModel(
    @InjectedParam seed: SeedEntity,
    private val seedDao: SeedDao,
    private val saveSeedUseCase: SaveSeedUseCase,
    private val exportRomUseCase: ExportRomUseCase,
    private val playRomUseCase: PlayRomUseCase,
    private val navigationManager: NavigationManager
) : ViewModel() {


    val loading = MutableStateFlow(false)

    val state: StateFlow<SeedItemState> = combine(
        loading,
        seedDao.getSeedFlow(seed.hash)
    ) { loading,
        savedSeed ->
        SeedItemState(
            loading = loading,
            seed = seed,
            isSaved = savedSeed != null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = SeedItemState(seed = seed, loading = false)
    )

    fun processEvent(event: SeedItemEvent) {
        viewModelScope.launch {
            when (event) {
                is SeedItemEvent.ExportRom -> exportRom()
                is SeedItemEvent.PlaySeed -> playRom()
                is SeedItemEvent.SaveSeed -> saveSeed()
                is SeedItemEvent.RemoveSeed -> seedDao.deleteSeed(state.value.seed.hash)
                is SeedItemEvent.OpenEditSeed -> navigationManager.navigateTo(Screen.EditRom(state.value.seed))
            }
        }
    }

    private suspend fun saveSeed(onSuccess: (seed: SeedEntity) -> Unit = {}) {
        saveSeedUseCase(state.value.seed)
            .onSuccess {
                onSuccess(it)
                navigationManager.showToast(getString(Res.string.save_seed_success))
            }
            .onFailure {
                navigationManager.showToast(getString(Res.string.save_seed_failed))
            }
    }

    private suspend fun playRom() {
        playRomUseCase(state.value.seed).onFailure {
            navigationManager.showToast(getString(Res.string.save_rom_failed))
        }
    }

    private suspend fun exportRom() {
        exportRomUseCase(state.value.seed).onFailure {
            navigationManager.showToast(getString(Res.string.generate_rom_failed))
        }
    }

}

data class SeedItemState(
    val seed: SeedEntity,
    val loading: Boolean = false,
    val isSaved: Boolean = false
)
