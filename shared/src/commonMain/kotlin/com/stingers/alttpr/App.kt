package com.stingers.alttpr

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.stingers.alttpr.common.components.PageLoadingView
import com.stingers.alttpr.screens.main.MainScreen
import com.stingers.alttpr.screens.upload.UploadRomView
import com.stingers.alttpr.theme.ZeldaTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    ZeldaTheme {
        Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
            val viewModel: AppViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()
            when {
                state.loading -> PageLoadingView()
                state.needsRom -> UploadRomView {
                    viewModel.saveRom(it)
                }

                else -> MainScreen()
            }
        }
    }
}