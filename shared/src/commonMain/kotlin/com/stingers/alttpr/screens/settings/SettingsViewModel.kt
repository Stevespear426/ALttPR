package com.stingers.alttpr.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.repository.local.AppPrefs
import com.stingers.alttpr.repository.local.RomDao
import com.stingers.alttpr.repository.local.RomStorage
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(
    private val romDao: RomDao,
//    private val romStorage: RomStorage,
    private val appPrefs: AppPrefs,
    private val navigationManager: NavigationManager,
) : ViewModel() {

    val state = appPrefs.debugMode.map {
        SettingsState(debugMode =  it)
    }.stateIn(viewModelScope, SharingStarted.Lazily, SettingsState())

    fun processEvent(event: SettingsEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsEvent.ClearAppData -> {}
                is SettingsEvent.EnableDebugMode -> appPrefs.setDebugMode(event.value)
                is SettingsEvent.NavigateTo -> navigationManager.navigateTo(event.value)
            }
        }
    }
}

data class SettingsState(
    val debugMode: Boolean = false
)