package com.stingers.alttpr.model

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
import com.stingers.alttpr.model.api.Language
import com.stingers.alttpr.model.api.Spoilers
import com.stingers.alttpr.model.api.Toggle
import com.stingers.alttpr.model.api.Weapons
import com.stingers.alttpr.model.api.WorldState
import com.stingers.alttpr.utils.currentTimeInMillis
import com.stingers.alttpr.utils.getDateString
import kotlinx.serialization.Serializable

@Serializable
data class RandomizerGameModel(
    val glitches: Glitches? = null,
    val itemPlacement: ItemPlacement? = null,
    val dungeonItems: Keysanity? = null,
    val accessibility: Accessibility? = null,
    val goal: Goals? = null,
    val towerCrystals: Crystals? = null,
    val ganonCrystals: Crystals? = null,
    val worldState: WorldState? = null,
    val entrances: Entrances? = null,
    val bossShuffle: BossShuffle? = null,
    val enemyShuffle: EnemyShuffle? = null,
    val potShuffle: Toggle? = null,
    val enemyDamage: EnemyDamage? = null,
    val enemyHealth: EnemyHealth? = null,
    val hints: Toggle? = null,
    val weapons: Weapons? = null,
    val itemPool: ItemPool? = null,
    val itemFunctionality: ItemFunctionality? = null,
    val tournament: Boolean? = null,
    val spoilers: Spoilers? = null,
    val allowQuickswap: Boolean? = null,
    val overrideStartScreen: Boolean? = null,
    val pseudoboots: Boolean? = null,
    val notes: String? = null,
    val lang: Language? = null,
    val name: String = "${glitches?.fileName().orEmpty()}-${worldState?.name.orEmpty()}-${goal?.name.orEmpty()}-${getDateString(currentTimeInMillis())}",
)

