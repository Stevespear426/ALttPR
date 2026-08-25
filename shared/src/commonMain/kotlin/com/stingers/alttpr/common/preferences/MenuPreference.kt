package com.stingers.alttpr.common.preferences

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.heart_color_title
import alttpr.shared.generated.resources.ic_arrow_drop_down
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.MENU_PADDING_VALUES
import com.stingers.alttpr.common.SETTINGS_PREFERENCE_PADDING
import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T> MenuPreference(
    title: StringResource,
    message: String? = null,
    currentItem: T,
    items: List<T>,
    titleResForItem: (value: T) -> StringResource,
    menuResForItem: (value: T) -> StringResource = titleResForItem,
    onItemChange: (value: T) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(5.dp),
        modifier = Modifier
            .clickable {
                showMenu = true
            }
            .fillMaxWidth()
    ) {
        Column(Modifier.weight(1f)) {
            PreferenceHeader(stringResource(title))
            message?.let { PreferenceMessage(text = it) }
        }
        Box {
            OutlinedButton(
                onClick = { showMenu = true },
                contentPadding = MENU_PADDING_VALUES,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
            ) {
                Text(
                    text = stringResource(titleResForItem(currentItem)),
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_drop_down),
                    contentDescription = ""
                )
            }
            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(stringResource(menuResForItem(item))) },
                        onClick = {
                            onItemChange(item)
                            showMenu = false
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuPreferenceDarkPreview() {
    PreviewDarkTheme {
        Surface {
            MenuPreference(
                Res.string.heart_color_title,
                currentItem = HeartColor.RED,
                items = HeartColor.entries,
                titleResForItem = { it.title }) {
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuPreferenceLightPreview() {
    PreviewLightTheme {
        Surface {
            MenuPreference(
                Res.string.heart_color_title,
                currentItem = HeartColor.RED,
                items = HeartColor.entries,
                titleResForItem = { it.title }) {
            }
        }
    }
}