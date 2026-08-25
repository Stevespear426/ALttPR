package com.stingers.alttpr.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.stingers.alttpr.common.ToastDuration
import com.stingers.alttpr.common.components.Toast
import com.stingers.alttpr.screens.edit.EditRomScreen
import com.stingers.alttpr.screens.licenses.LicencesScreen
import com.stingers.alttpr.screens.main.MainScreen
import com.stingers.alttpr.screens.randomizer.RandomizerScreen
import com.stingers.alttpr.screens.sprites.SpriteScreen
import com.stingers.alttpr.screens.upload.UploadRomScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.annotation.Singleton
import kotlin.time.Duration.Companion.milliseconds


@Singleton
class NavigationManager {

    val lifecycleScope = CoroutineScope(Job() + Dispatchers.Default)

    private val toastState: MutableState<String?> = mutableStateOf(null)

    val backStack = mutableStateListOf<Screen>(Screen.Main)

    fun navigateTo(screen: Screen) {
        backStack.add(screen)
    }

    fun pop() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.size - 1)
        }
    }

    fun setRoot(screen: Screen) {
        backStack.clear()
        backStack.add(screen)
    }

    fun showToast(text: String, duration: ToastDuration = ToastDuration.SHORT) {
        toastState.value = text
        lifecycleScope.launch {
            delay(duration.value.milliseconds)
            toastState.value = null
        }
    }

    @Composable
    fun NavigationView() {
        NavDisplay(
            backStack = backStack,
            onBack = { pop() },
            entryProvider = { key ->
                when (key) {
                    is Screen.UploadRom -> NavEntry(key as Screen) {
                        UploadRomScreen()
                    }

                    is Screen.Main -> NavEntry(key as Screen) {
                        MainScreen()
                    }

                    is Screen.EditRom -> NavEntry(key as Screen) {
                        EditRomScreen(seed = key.seed)
                    }

                    is Screen.Licenses -> NavEntry(key as Screen) {
                        LicencesScreen()
                    }

                    is Screen.Randomizer -> NavEntry(key as Screen) {
                        RandomizerScreen()
                    }

                    is Screen.Sprites -> NavEntry(key as Screen) {
                        SpriteScreen()
                    }
                }
            }
        )

        AnimatedVisibility(
            visible = !toastState.value.isNullOrEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Toast(toastState.value.orEmpty())
        }
    }
}
