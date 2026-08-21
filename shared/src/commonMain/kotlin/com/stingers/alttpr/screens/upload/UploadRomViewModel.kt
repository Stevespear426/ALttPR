package com.stingers.alttpr.screens.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.repository.RomManager
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class UploadViewModel constructor(
    private val logger: Logger,
    private val romManager: RomManager,
    private val navigationManager: NavigationManager
) : ViewModel() {


    fun precessEvent(event: UploadRomEvent) {
        viewModelScope.launch {
            when (event) {
                is UploadRomEvent.SaveRom -> {
                    val result = romManager.saveAndVerifyRom(event.value)
                    if (result.isSuccess) {
                        logger.d(TAG, "Base Rom Saved.")
                        navigationManager.setRoot(Screen.Main)
                    }
                    if (result.isFailure) {
                        logger.d(TAG, "Save Base Rom failed.")
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "UploadViewModel"
    }
}
