package com.stingers.alttpr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.AlttprRepository
import com.stingers.alttpr.repository.RomManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class AppViewModel constructor(
    private val romManager: RomManager,
    private val alttprRepository: AlttprRepository,
    private val navigationManager: NavigationManager
) : ViewModel() {

    private val _state = MutableStateFlow(AppState(loading = true))
    val state = _state.asStateFlow()

    init {
        checkExistingRom()
    }

    private fun checkExistingRom() {
        viewModelScope.launch {
            val isValid = romManager.hasValidBaseRom()
            _state.update {
                it.copy(
                    loading = false,
                    needsBaseRom = !isValid
                )
            }
            if (!isValid) {
                navigationManager.setRoot(Screen.UploadRom)
            } else {
                navigationManager.setRoot(Screen.Main)
            }
        }
    }

    fun createDailySeed() {
        viewModelScope.launch {
            val result = alttprRepository.createDailySeed()
            result.onSuccess { hash ->
                navigationManager.navigateTo(Screen.EditRom(hash))
            }
        }
    }
}

data class AppState(
    val loading: Boolean = false,
    val needsBaseRom: Boolean = true,
)
