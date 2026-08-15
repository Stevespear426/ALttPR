package com.stingers.alttpr

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.stingers.alttpr.common.components.PageLoadingView
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.screens.edit.EditRomScreen
import com.stingers.alttpr.screens.main.MainScreen
import com.stingers.alttpr.screens.upload.UploadRomScreen
import com.stingers.alttpr.theme.ZeldaTheme
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    ZeldaTheme {
        Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
            val viewModel: AppViewModel = koinViewModel()
            val navigationManager: NavigationManager = koinInject()
            val state by viewModel.state.collectAsState()

            if (state.loading) {
                PageLoadingView()
            } else {
                NavDisplay(
                    backStack = navigationManager.backStack,
                    onBack = { navigationManager.pop() },
                    entryProvider = { key ->
                        when (key) {
                            is Screen.UploadRom -> NavEntry(key as Screen) { UploadRomScreen() }
                            is Screen.Main -> NavEntry(key as Screen) {
                                MainScreen(
                                    onCreateDaily = {
                                        viewModel.createDailySeed()
                                    }
                                )
                            }
                            is Screen.EditRom -> NavEntry(key as Screen) {
                                EditRomScreen(hash = key.hash,)
                            }
                        }
                    }
                )
            }
        }
    }
}
