package com.stingers.alttpr.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Singleton

@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object UploadRom : Screen

    @Serializable
    data object Main : Screen

    @Serializable
    data class EditRom(val hash: String) : Screen
}

@Singleton
class NavigationManager {
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
}
