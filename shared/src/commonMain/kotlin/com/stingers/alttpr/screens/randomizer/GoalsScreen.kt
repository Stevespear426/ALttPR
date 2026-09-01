package com.stingers.alttpr.screens.randomizer

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.ganon_vulnerable
import alttpr.shared.generated.resources.goals
import alttpr.shared.generated.resources.open_tower
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.stingers.alttpr.common.preferences.MenuPreference
import com.stingers.alttpr.model.GameMode
import com.stingers.alttpr.model.GameModel
import com.stingers.alttpr.model.api.Crystals
import com.stingers.alttpr.model.api.Goals
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme

@Composable
fun GoalsScreen(
    settings: GameModel,
    processEvent: (event: RandomizerEvent) -> Unit
) {
    with(settings) {
        val settings = mutableListOf<@Composable () -> Unit>()
        goal?.let {
            settings.add {
                MenuPreference(
                    Res.string.goals,
                    currentItem = it,
                    items = Goals.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetGoal(it))
                }
            }
        }
        towerCrystals?.let {
            settings.add {
                MenuPreference(
                    Res.string.open_tower,
                    currentItem = it,
                    items = Crystals.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetTowerCrystals(it))
                }
            }
        }
        ganonCrystals?.let {
            settings.add {
                MenuPreference(
                    Res.string.ganon_vulnerable,
                    currentItem = it,
                    items = Crystals.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetGanonCrystals(it))
                }
            }
        }
        RandomizerSettingsScreen(settings)
    }
}


@Preview(showBackground = true)
@Composable
fun GoalsScreenLightPreview(
    @PreviewParameter(RandomizerStateParameterProvider::class) state: RandomizerState
) {
    PreviewLightTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GoalsScreen(state.settings) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GoalsScreenDarkPreview(
    @PreviewParameter(RandomizerStateParameterProvider::class) state: RandomizerState
) {
    PreviewDarkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GoalsScreen(state.settings) {}
        }
    }
}
