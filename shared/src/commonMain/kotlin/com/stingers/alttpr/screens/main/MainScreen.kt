package com.stingers.alttpr.screens.main

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.create_daily
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.stingers.alttpr.common.components.PrimaryButton
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme

@Composable
fun MainScreen(onCreateDaily: () -> Unit = {}) {

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Start Modding...")

        PrimaryButton(Res.string.create_daily) {
            onCreateDaily()
        }
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
