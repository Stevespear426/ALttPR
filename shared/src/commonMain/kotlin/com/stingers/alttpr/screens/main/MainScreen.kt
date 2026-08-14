package com.stingers.alttpr.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme


@Composable
fun MainScreen() {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Start Modding...")
    }
}

@Preview(showBackground = true)
@Composable
fun UploadRomViewLightPreview() {
    PreviewLightTheme {
        MainScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun UploadRomViewDarkPreview() {
    PreviewDarkTheme {
        MainScreen()
    }
}
