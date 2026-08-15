package com.stingers.alttpr.common.preferences

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.alttp
import alttpr.shared.generated.resources.select_rom_file
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.SETTINGS_PREFERENCE_PADDING
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Preference(
    title: StringResource,
    message: String? = null,
    messageRes: StringResource? = null,
    icon: DrawableResource? = null,
    contentPadding: PaddingValues = SETTINGS_PREFERENCE_PADDING,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.clickable(onClick = onClick).padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Box(
                modifier =
                    Modifier.padding(end = 16.dp).size(40.dp).background(
                        MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(it),
                    contentDescription = "Preference Icon $title",
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            PreferenceHeader(stringResource(title))
            message?.let {
                PreferenceMessage(it)
            }
            messageRes?.let {
                PreferenceMessage(stringResource(it))
            }
        }
    }
}

data class PreviewPreference(
    val title: StringResource,
    val message: String? = null,
    val messageRes: StringResource? = null,
    val icon: DrawableResource? = null
)

class PreferenceParameterProvider : PreviewParameterProvider<PreviewPreference> {
    override val values = sequenceOf(
        PreviewPreference(title = Res.string.alttp),
        PreviewPreference(
            title = Res.string.alttp,
            message = "This is a preference message."
        ),
        PreviewPreference(
            title = Res.string.alttp,
            messageRes = Res.string.select_rom_file
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun PreferenceLightPreview(
    @PreviewParameter(PreferenceParameterProvider::class) item: PreviewPreference
) {
    PreviewLightTheme {
        Surface {
            Preference(item.title, item.message, item.messageRes, item.icon)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreferenceDarkPreview(
    @PreviewParameter(PreferenceParameterProvider::class) item: PreviewPreference
) {
    PreviewDarkTheme {
        Surface {
            Preference(item.title, item.message, item.messageRes, item.icon)
        }
    }
}
