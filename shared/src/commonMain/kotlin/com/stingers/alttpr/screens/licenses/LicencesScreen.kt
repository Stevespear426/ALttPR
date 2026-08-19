package com.stingers.alttpr.screens.licenses

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.licences
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.stingers.alttpr.common.components.HeaderPage
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun LicencesScreen() {
    HeaderPage(stringResource(Res.string.licences)) {
        Box(modifier = Modifier.padding(it)) {
            LicencesListView()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LicencesScreenLightPreview() {
    PreviewLightTheme {
        Surface(Modifier.fillMaxSize()) {
            LicencesScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LicencesScreenDarkPreview() {
    PreviewDarkTheme {
        Surface(Modifier.fillMaxSize()) {
            LicencesScreen()
        }
    }
}
