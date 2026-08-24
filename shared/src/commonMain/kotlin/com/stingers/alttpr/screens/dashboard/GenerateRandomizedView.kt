package com.stingers.alttpr.screens.dashboard

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.generate_game
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.common.components.PrimaryButton
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme


@Composable
fun GenerateRandomizedView(generateGame: () -> Unit) {

    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        Column(
            modifier = Modifier.padding(PREFERENCE_PADDING.dp),
            verticalArrangement = spacedBy(16.dp)
        ) {
            Text("Generate Randomized Game")
            PrimaryButton(Res.string.generate_game) {
                generateGame()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GenerateRandomizedViewLightPreview() {
    PreviewLightTheme {
        GenerateRandomizedView {}
    }
}

@Preview(showBackground = true)
@Composable
fun GenerateRandomizedViewDarkPreview() {
    PreviewDarkTheme {
        GenerateRandomizedView {}
    }
}
