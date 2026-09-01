package com.stingers.alttpr.screens.randomizer

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.enemy_damage
import alttpr.shared.generated.resources.enemy_health
import alttpr.shared.generated.resources.item_functionality
import alttpr.shared.generated.resources.item_pool
import alttpr.shared.generated.resources.pseudoboots
import alttpr.shared.generated.resources.swords
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.stingers.alttpr.common.preferences.MenuPreference
import com.stingers.alttpr.common.preferences.SwitchPreference
import com.stingers.alttpr.model.GameModel
import com.stingers.alttpr.model.GameMode
import com.stingers.alttpr.model.api.EnemyDamage
import com.stingers.alttpr.model.api.EnemyHealth
import com.stingers.alttpr.model.api.ItemFunctionality
import com.stingers.alttpr.model.api.ItemPool
import com.stingers.alttpr.model.api.Weapons
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme

@Composable
fun DifficultyScreen(
    settings: GameModel,
    processEvent: (event: RandomizerEvent) -> Unit
) {
    with(settings) {
        val settings = mutableListOf<@Composable () -> Unit>()
        weapons?.let {
            settings.add {
                MenuPreference(
                    Res.string.swords,
                    currentItem = it,
                    items = Weapons.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetWeapons(it))
                }
            }
        }

        itemPool?.let {
            settings.add {
                MenuPreference(
                    Res.string.item_pool,
                    currentItem = it,
                    items = ItemPool.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetItemPool(it))
                }
            }
        }

        itemFunctionality?.let {
            settings.add {
                MenuPreference(
                    Res.string.item_functionality,
                    currentItem = it,
                    items = ItemFunctionality.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetItemFunctionality(it))
                }
            }
        }

        enemyDamage?.let {
            settings.add {
                MenuPreference(
                    Res.string.enemy_damage,
                    currentItem = it,
                    items = EnemyDamage.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetEnemyDamage(it))
                }
            }
        }

        enemyHealth?.let {
            settings.add {
                MenuPreference(
                    Res.string.enemy_health,
                    currentItem = it,
                    items = EnemyHealth.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetEnemyHealth(it))
                }
            }
        }

        pseudoboots?.let {
            settings.add {
                SwitchPreference(
                    title = Res.string.pseudoboots,
                    checked = it
                ) {
                    processEvent(RandomizerEvent.SetPseduoboots(it))
                }
            }
        }
        RandomizerSettingsScreen(settings)
    }
}


@Preview(showBackground = true)
@Composable
fun DifficultyScreenLightPreview(
    @PreviewParameter(RandomizerStateParameterProvider::class) state: RandomizerState
) {
    PreviewLightTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            DifficultyScreen(state.settings) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DifficultyScreenDarkPreview(
    @PreviewParameter(RandomizerStateParameterProvider::class) state: RandomizerState
) {
    PreviewDarkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            DifficultyScreen(state.settings) {}
        }
    }
}
