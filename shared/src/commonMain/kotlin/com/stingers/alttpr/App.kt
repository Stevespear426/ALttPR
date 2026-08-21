package com.stingers.alttpr

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.stingers.alttpr.common.components.PageLoadingView
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.repository.local.RomStorage
import com.stingers.alttpr.theme.ZeldaTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            RomStorage.clearShareRomFiles()
        }
    }
    ZeldaTheme {
        Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
            val viewModel: AppViewModel = koinViewModel()
            val navigationManager: NavigationManager = koinInject()
            val state by viewModel.state.collectAsState()

            if (state.loading) {
                PageLoadingView()
            } else {
                navigationManager.NavigationView()
            }
        }
    }
}
