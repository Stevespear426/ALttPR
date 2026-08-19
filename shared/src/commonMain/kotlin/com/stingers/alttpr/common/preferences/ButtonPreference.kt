package com.stingers.alttpr.common.preferences

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.alttp
import alttpr.shared.generated.resources.ic_save
import alttpr.shared.generated.resources.ok
import alttpr.shared.generated.resources.select_rom_file
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.platform.ic_share
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ButtonPreference(
    title: StringResource,
    message: String? = null,
    buttonRes: StringResource,
    leaningIcon: DrawableResource? = null,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(5.dp),
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(PREFERENCE_PADDING.dp)
    ) {
        Column(Modifier.weight(1f)) {
            PreferenceHeader(stringResource(title))
            message?.let { PreferenceMessage(text = it) }
        }
        OutlinedButton(
            onClick = onClick,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
        ) {
            leaningIcon?.let {
                Icon(painterResource(leaningIcon), contentDescription = "Leading Icon")
                Spacer(Modifier.width(4.dp))
            }
            Text(text = stringResource(buttonRes))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreferenceDarkPreview() {
    PreviewDarkTheme {
        Surface {
            ButtonPreference(
                title = Res.string.alttp,
                message = stringResource(Res.string.select_rom_file),
                buttonRes = Res.string.ok
            ) {}
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreferenceLightPreview() {
    PreviewLightTheme {
        Surface {
            ButtonPreference(
                title = Res.string.alttp,
                message = stringResource(Res.string.select_rom_file),
                buttonRes = Res.string.ok
            ) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreferenceIconDarkPreview() {
    PreviewDarkTheme {
        Surface {
            ButtonPreference(
                title = Res.string.alttp,
                message = stringResource(Res.string.select_rom_file),
                buttonRes = Res.string.ok,
                leaningIcon = Res.drawable.ic_save
            ) {}
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreferenceLIconNightPreview() {
    PreviewLightTheme {
        Surface {
            ButtonPreference(
                title = Res.string.alttp,
                message = stringResource(Res.string.select_rom_file),
                buttonRes = Res.string.ok,
                leaningIcon = Res.drawable.ic_share
            ) {}
        }
    }
}