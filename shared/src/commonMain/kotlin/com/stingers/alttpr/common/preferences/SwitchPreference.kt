package com.stingers.alttpr.common.preferences

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.alttp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.SETTINGS_PREFERENCE_PADDING
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SwitchPreference(
    title: StringResource,
    checked: Boolean,
    message: String? = null,
//    contentPadding: PaddingValues = SETTINGS_PREFERENCE_PADDING,
    onCheckedChange: (value: Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCheckedChange(!checked)
            }
//            .padding(contentPadding)
    ) {
        Column(Modifier.weight(1f)) {
            PreferenceHeader(stringResource(title))
            message?.let { PreferenceMessage(text = it) }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.secondary,
                checkedThumbColor = Color.White
            )
        )
    }
}

data class PreviewSwitchPreference(
    val title: StringResource,
    val message: String? = null,
    val checked: Boolean = false
)

class SwitchPreferenceParameterProvider : PreviewParameterProvider<PreviewSwitchPreference> {
    override val values = sequenceOf(
        PreviewSwitchPreference(title = Res.string.alttp),
        PreviewSwitchPreference(
            title = Res.string.alttp,
            message = "This is a real long message preference message that will need to take a 2nd line and probably be truncated.",
            checked = true
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun SwitchPreferenceLightPreview(
    @PreviewParameter(SwitchPreferenceParameterProvider::class) item: PreviewSwitchPreference
) {
    PreviewLightTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            SwitchPreference(title = item.title, message = item.message, checked = item.checked) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SwitchPreferenceDarkPreview(
    @PreviewParameter(SwitchPreferenceParameterProvider::class) item: PreviewSwitchPreference
) {
    PreviewDarkTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            SwitchPreference(title = item.title, message = item.message, checked = item.checked) {}
        }
    }
}