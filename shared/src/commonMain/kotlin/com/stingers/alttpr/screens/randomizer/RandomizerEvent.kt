package com.stingers.alttpr.screens.randomizer

import com.stingers.alttpr.model.RandomizerGameMode
import com.stingers.alttpr.model.api.Accessibility
import com.stingers.alttpr.model.api.BossShuffle
import com.stingers.alttpr.model.api.Crystals
import com.stingers.alttpr.model.api.EnemyDamage
import com.stingers.alttpr.model.api.EnemyHealth
import com.stingers.alttpr.model.api.EnemyShuffle
import com.stingers.alttpr.model.api.Entrances
import com.stingers.alttpr.model.api.Glitches
import com.stingers.alttpr.model.api.Goals
import com.stingers.alttpr.model.api.ItemFunctionality
import com.stingers.alttpr.model.api.ItemPlacement
import com.stingers.alttpr.model.api.ItemPool
import com.stingers.alttpr.model.api.Keysanity
import com.stingers.alttpr.model.api.Toggle
import com.stingers.alttpr.model.api.Weapons
import com.stingers.alttpr.model.api.WorldState


sealed interface RandomizerEvent {

    object GenerateGame : RandomizerEvent
    object GenerateRace : RandomizerEvent
    data class SetPreset(val value: RandomizerGameMode) : RandomizerEvent
    data class SetGlitches(val value: Glitches) : RandomizerEvent
    data class SetItemPlacement(val value: ItemPlacement) : RandomizerEvent
    data class SetDungeonItems(val value: Keysanity) : RandomizerEvent
    data class SetItemAccessibility(val value: Accessibility) : RandomizerEvent
    data class SetGoal(val value: Goals) : RandomizerEvent
    data class SetTowerCrystals(val value: Crystals) : RandomizerEvent
    data class SetGanonCrystals(val value: Crystals) : RandomizerEvent
    data class SetWorldState(val value: WorldState) : RandomizerEvent
    data class SetEntrances(val value: Entrances) : RandomizerEvent
    data class SetBossShuffle(val value: BossShuffle) : RandomizerEvent
    data class SetEnemyShuffle(val value: EnemyShuffle) : RandomizerEvent
    data class SetHints(val value: Toggle) : RandomizerEvent

    data class SetWeapons(val value: Weapons) : RandomizerEvent
    data class SetItemPool(val value: ItemPool) : RandomizerEvent
    data class SetItemFunctionality(val value: ItemFunctionality) : RandomizerEvent
    data class SetEnemyDamage(val value: EnemyDamage) : RandomizerEvent
    data class SetEnemyHealth(val value: EnemyHealth) : RandomizerEvent

    data class SetPotShuffle(val value: Toggle) : RandomizerEvent
    data class SetPseduoboots(val value: Boolean) : RandomizerEvent
}
