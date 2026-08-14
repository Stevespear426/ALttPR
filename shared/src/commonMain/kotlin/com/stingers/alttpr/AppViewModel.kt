package com.stingers.alttpr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.domain.RomManager
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class AppViewModel constructor(
    private val romManager: RomManager
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
                    needsRom = !isValid
                )
            }
        }
    }

    fun saveRom(rom: PlatformFile) {
        viewModelScope.launch {
            val result = romManager.saveAndVerifyRom(rom)
            if (result.isSuccess) {
                _state.update { it.copy(needsRom = false) }
            }
        }
    }
}

data class AppState(
    val loading: Boolean = false,
    val needsRom: Boolean = true,
)
