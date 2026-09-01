package com.stingers.alttpr.screens.randomizer

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.accessibility
import alttpr.shared.generated.resources.dungeon_item_shuffle
import alttpr.shared.generated.resources.glitches_required
import alttpr.shared.generated.resources.item_placement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.stingers.alttpr.common.preferences.MenuPreference
import com.stingers.alttpr.model.GameMode
import com.stingers.alttpr.model.GameModel
import com.stingers.alttpr.model.api.Accessibility
import com.stingers.alttpr.model.api.Glitches
import com.stingers.alttpr.model.api.ItemPlacement
import com.stingers.alttpr.model.api.Keysanity
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme

@Composable
fun ItemsScreen(
    settings: GameModel,
    processEvent: (event: RandomizerEvent) -> Unit
) {
    with(settings) {
        val settings = mutableListOf<@Composable () -> Unit>()
        glitches?.let {
            settings.add {
                MenuPreference(
                    Res.string.glitches_required,
                    currentItem = it,
                    items = Glitches.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetGlitches(it))
                }
            }
        }

        itemPlacement?.let {
            settings.add {
                MenuPreference(
                    Res.string.item_placement,
                    currentItem = it,
                    items = ItemPlacement.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetItemPlacement(it))
                }
            }
        }
        dungeonItems?.let {
            settings.add {
                MenuPreference(
                    Res.string.dungeon_item_shuffle,
                    currentItem = it,
                    items = Keysanity.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetDungeonItems(it))
                }
            }
        }

        accessibility?.let {
            settings.add {
                MenuPreference(
                    Res.string.accessibility,
                    currentItem = it,
                    items = Accessibility.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetItemAccessibility(it))
                }
            }
        }
        RandomizerSettingsScreen(settings)
    }
}


@Preview(showBackground = true)
@Composable
fun ItemsScreenLightPreview(
    @PreviewParameter(RandomizerStateParameterProvider::class) state: RandomizerState
) {
    PreviewLightTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ItemsScreen(state.settings) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemsScreenDarkPreview(
    @PreviewParameter(RandomizerStateParameterProvider::class) state: RandomizerState
) {
    PreviewDarkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ItemsScreen(state.settings) {}
        }
    }
}
