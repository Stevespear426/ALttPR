package com.stingers.alttpr.screens.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.RomManager
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class UploadViewModel constructor(
    private val romManager: RomManager,
    private val navigationManager: NavigationManager
) : ViewModel() {


    fun precessEvent(event: UploadRomEvent) {
        viewModelScope.launch {
            when (event) {
                is UploadRomEvent.SaveRom -> {
                    val result = romManager.saveAndVerifyRom(event.value)
                    if (result.isSuccess) {
                        navigationManager.setRoot(Screen.Main)
                    }
                }
            }
        }
    }
}
