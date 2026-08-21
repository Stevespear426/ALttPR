package com.stingers.alttpr.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Toast(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Text(
            text = text,
            modifier = Modifier
                .testTag("Toast Text")
                .padding(48.dp)
                .clip(CircleShape)
                .background(Color.DarkGray.copy(alpha = .75f))
                .padding(16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun ToastPreviewLight() {
    PreviewLightTheme {
        Surface(Modifier.fillMaxWidth()) {
            Toast(text = "Test Error")
        }
    }
}

@Preview
@Composable
fun ToastPreviewDark() {
    PreviewDarkTheme {
        Surface(Modifier.fillMaxWidth()) {
            Toast(text = "Longer Toast text maybe a multi line thing...")
        }
    }
}
