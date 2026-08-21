package com.stingers.alttpr.screens.edit

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.model.SpoilerMeta
import com.stingers.alttpr.model.SpoilerMetaParameterProvider
import com.stingers.alttpr.model.api.Spoilers
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import com.stingers.alttpr.utils.toEnumOrDefault

@Composable
fun SpoilerMetaView(meta: SpoilerMeta) {
    with(meta) {
        when (spoilers.toEnumOrDefault(Spoilers.Off)) {
            Spoilers.Mystery -> {
                Text(
                    modifier = Modifier.padding(PREFERENCE_PADDING.dp),
                    text = "Mystery Game",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            else -> {
                Column(
                    modifier = Modifier.padding(PREFERENCE_PADDING.dp),
                    verticalArrangement = spacedBy(4.dp)
                ) {
                    Text(
                        text = "Glitched Required: $logic",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "ROM Build: $build",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Accessibility: $accessibility",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "World State: $mode",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Swords: $weapons",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Goal: $goal",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SpoilerMetaViewLightPreview(
    @PreviewParameter(SpoilerMetaParameterProvider::class) item: SpoilerMeta
) {
    PreviewLightTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SpoilerMetaView(item)
        }
    }
}

@Preview
@Composable
fun SpoilerMetaViewDarkPreview(
    @PreviewParameter(SpoilerMetaParameterProvider::class) item: SpoilerMeta
) {
    PreviewDarkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SpoilerMetaView(item)
        }
    }
}