package com.stingers.alttpr.screens.settings

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.clear
import alttpr.shared.generated.resources.clear_app_data
import alttpr.shared.generated.resources.clear_app_data_message
import alttpr.shared.generated.resources.debug_mode
import alttpr.shared.generated.resources.licences
import alttpr.shared.generated.resources.licences_message
import alttpr.shared.generated.resources.settings_title
import alttpr.shared.generated.resources.version
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.BuildKonfig
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.common.components.PageHeader
import com.stingers.alttpr.common.preferences.ButtonPreference
import com.stingers.alttpr.common.preferences.Preference
import com.stingers.alttpr.common.preferences.SubPreference
import com.stingers.alttpr.common.preferences.SwitchPreference
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    SettingsScreen(state, viewModel::processEvent)
}

@Composable
fun SettingsScreen(state: SettingsState, processEvent: (event: SettingsEvent) -> Unit) {
    with(state) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().testTag("Test SettingsScreen"),
            verticalArrangement = spacedBy(16.dp),
            contentPadding = PaddingValues(PREFERENCE_PADDING.dp)
        ) {

            item {
                PageHeader(Res.string.settings_title)
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                SwitchPreference(
                    title = Res.string.debug_mode,
                    debugMode
                ) {
                    processEvent(SettingsEvent.EnableDebugMode(it))
                }
            }
            item {
                ButtonPreference(
                    title = Res.string.clear_app_data,
                    message = stringResource(Res.string.clear_app_data_message),
                    buttonRes = Res.string.clear
                ) {
                    processEvent(SettingsEvent.ClearAppData)
                }
            }

            item {
                SubPreference(
                    Res.string.licences,
                    stringResource(Res.string.licences_message)
                ) {
                    processEvent(SettingsEvent.NavigateTo(Screen.Licenses))
                }
            }

            item {
                Preference(
                    Res.string.version,
                    BuildKonfig.VERSION_NAME
                )
            }
        }
    }
}

class SettingsStateParameterProvider : PreviewParameterProvider<SettingsState> {
    override val values = sequenceOf(
        SettingsState(
            debugMode = true
        ),
        SettingsState(
            debugMode = false
        )
    )
}

@Preview(showBackground = true)
@Composable
fun UploadRomViewLightPreview(
    @PreviewParameter(SettingsStateParameterProvider::class) item: SettingsState
) {
    PreviewLightTheme {
        SettingsScreen(item) {}
    }
}

@Preview(showBackground = true)
@Composable
fun UploadRomViewDarkPreview(
    @PreviewParameter(SettingsStateParameterProvider::class) item: SettingsState

) {
    PreviewDarkTheme {
        SettingsScreen(item) {}
    }
}
