package com.stingers.alttpr.screens.generator

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.create_daily
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
fun CreateDailyView(createDaily: () -> Unit) {

    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        Column(
            modifier = Modifier.padding(PREFERENCE_PADDING.dp),
            verticalArrangement = spacedBy(16.dp)
        ) {
            Text("Daily Challenge")
            PrimaryButton(Res.string.create_daily) {
                createDaily()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateDailyViewLightPreview() {
    PreviewLightTheme {
        CreateDailyView {}
    }
}

@Preview(showBackground = true)
@Composable
fun CreateDailyViewDarkPreview() {
    PreviewDarkTheme {
        CreateDailyView {}
    }
}
