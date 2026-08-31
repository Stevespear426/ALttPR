package com.stingers.alttpr.screens.randomizer

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.boss_shuffle
import alttpr.shared.generated.resources.enemy_shuffle
import alttpr.shared.generated.resources.entrance_shuffle
import alttpr.shared.generated.resources.hints
import alttpr.shared.generated.resources.pot_shuffle
import alttpr.shared.generated.resources.world_state
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.stingers.alttpr.common.preferences.MenuPreference
import com.stingers.alttpr.model.GameMode
import com.stingers.alttpr.model.GameModel
import com.stingers.alttpr.model.api.BossShuffle
import com.stingers.alttpr.model.api.EnemyShuffle
import com.stingers.alttpr.model.api.Entrances
import com.stingers.alttpr.model.api.Toggle
import com.stingers.alttpr.model.api.WorldState
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme

@Composable
fun GameplayScreen(
    settings: GameModel,
    isCustom: Boolean = false,
    processEvent: (event: RandomizerEvent) -> Unit
) {
    with(settings) {
        val settings = mutableListOf<@Composable () -> Unit>()
        worldState?.let {
            settings.add {
                MenuPreference(
                    Res.string.world_state,
                    currentItem = it,
                    items = WorldState.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetWorldState(it))
                }
            }
        }

        entrances?.let {
            settings.add {
                MenuPreference(
                    Res.string.entrance_shuffle,
                    currentItem = it,
                    items = Entrances.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetEntrances(it))
                }
            }
        }

        bossShuffle?.let {
            settings.add {
                MenuPreference(
                    Res.string.boss_shuffle,
                    currentItem = it,
                    items = BossShuffle.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetBossShuffle(it))
                }
            }
        }

        enemyShuffle?.let {
            settings.add {
                MenuPreference(
                    Res.string.enemy_shuffle,
                    currentItem = it,
                    items = EnemyShuffle.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetEnemyShuffle(it))
                }
            }
        }

        potShuffle?.let {
            settings.add {
                MenuPreference(
                    Res.string.pot_shuffle,
                    currentItem = it,
                    items = Toggle.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetPotShuffle(it))
                }
            }
        }

        hints?.let {
            settings.add {
                MenuPreference(
                    Res.string.hints,
                    currentItem = it,
                    items = Toggle.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetHints(it))
                }
            }
        }
        RandomizerSettingsScreen(isCustom, settings)
    }
}


@Preview(showBackground = true)
@Composable
fun GameplayScreenLightPreview(
    @PreviewParameter(RandomizerStateParameterProvider::class) state: RandomizerState
) {
    PreviewLightTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameplayScreen(state.settings, state.preset == GameMode.CUSTOM) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameplayScreenDarkPreview(
    @PreviewParameter(RandomizerStateParameterProvider::class) state: RandomizerState
) {
    PreviewDarkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameplayScreen(state.settings, state.preset == GameMode.CUSTOM) {}
        }
    }
}
