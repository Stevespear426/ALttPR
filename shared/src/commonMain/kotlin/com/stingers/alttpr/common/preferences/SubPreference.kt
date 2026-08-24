package com.stingers.alttpr.common.preferences

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.alttp
import alttpr.shared.generated.resources.ic_next
import alttpr.shared.generated.resources.select_rom_file
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SubPreference(
    title: StringResource,
    message: String? = null,
    messageRes: StringResource? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick),
//            .padding(all = PREFERENCE_PADDING.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            PreferenceHeader(stringResource(title))
            message?.let {
                PreferenceMessage(it)
            }
            messageRes?.let {
                PreferenceMessage(stringResource(it))
            }
        }
        Icon(
            painter = painterResource(Res.drawable.ic_next),
            modifier = Modifier.size(25.dp),
            contentDescription = ""
        )

    }
}

data class PreviewSubPreference(
    val title: StringResource,
    val message: String? = null,
    val messageRes: StringResource? = null,
)

class SubPreferenceParameterProvider : PreviewParameterProvider<PreviewSubPreference> {
    override val values = sequenceOf(
        PreviewSubPreference(title = Res.string.alttp),
        PreviewSubPreference(
            title = Res.string.alttp,
            message = "This is a preference message."
        ),
        PreviewSubPreference(
            title = Res.string.alttp,
            messageRes = Res.string.select_rom_file
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSubPreferenceLightPreview(
    @PreviewParameter(SubPreferenceParameterProvider::class) item: PreviewSubPreference
) {
    PreviewLightTheme {
        Surface {
            SubPreference(item.title, item.message, item.messageRes)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreferenceSubDarkPreview(
    @PreviewParameter(SubPreferenceParameterProvider::class) item: PreviewSubPreference
) {
    PreviewDarkTheme {
        Surface {
            SubPreference(item.title, item.message, item.messageRes)
        }
    }
}
