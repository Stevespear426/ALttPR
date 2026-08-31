package com.stingers.alttpr.model

import com.stingers.alttpr.model.api.Accessibility
import com.stingers.alttpr.model.api.BossShuffle
import com.stingers.alttpr.model.api.BottleApi
import com.stingers.alttpr.model.api.BottleLocation
import com.stingers.alttpr.model.api.ClockMode
import com.stingers.alttpr.model.api.CompassMode
import com.stingers.alttpr.model.api.Crystals
import com.stingers.alttpr.model.api.Drop
import com.stingers.alttpr.model.api.EnemyDamage
import com.stingers.alttpr.model.api.EnemyHealth
import com.stingers.alttpr.model.api.EnemyShuffle
import com.stingers.alttpr.model.api.Entrances
import com.stingers.alttpr.model.api.Glitches
import com.stingers.alttpr.model.api.Goals
import com.stingers.alttpr.model.api.Item
import com.stingers.alttpr.model.api.ItemFunctionality
import com.stingers.alttpr.model.api.ItemLocation
import com.stingers.alttpr.model.api.ItemPlacement
import com.stingers.alttpr.model.api.ItemPool
import com.stingers.alttpr.model.api.Keysanity
import com.stingers.alttpr.model.api.Language
import com.stingers.alttpr.model.api.Medallion
import com.stingers.alttpr.model.api.MedallionLocation
import com.stingers.alttpr.model.api.Prize
import com.stingers.alttpr.model.api.PrizeLocation
import com.stingers.alttpr.model.api.PrizePack
import com.stingers.alttpr.model.api.Spoilers
import com.stingers.alttpr.model.api.TextDialog
import com.stingers.alttpr.model.api.Toggle
import com.stingers.alttpr.model.api.Weapons
import com.stingers.alttpr.model.api.WorldState
import com.stingers.alttpr.utils.currentTimeInMillis
import com.stingers.alttpr.utils.getDateString
import kotlinx.serialization.Serializable

@Serializable
data class GameModel(
    // shared with RandomizerGameModel
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
    val spoilers: Spoilers? = null,
    val allowQuickswap: Boolean? = null,
    val overrideStartScreen: Boolean? = null,
    val pseudoboots: Boolean? = null,
    val notes: String? = null,
    val lang: Language? = null,
    // Customizer defaults tournament to true server-side; force it false so full
    // patch/spoiler data always comes back, matching how every other seed in this app behaves.
    val tournament: Boolean = false,
    val name: String = "${glitches?.fileName().orEmpty()}-${worldState?.name.orEmpty()}-${goal?.name.orEmpty()}-${getDateString(currentTimeInMillis())}",


    // customizer-only
    val startingEquipment: Set<Item> = emptySet(),
    val itemPlacements: Map<ItemLocation, Item> = emptyMap(),
    val prizePlacements: Map<PrizeLocation, Prize> = emptyMap(),
    val medallionPlacements: Map<MedallionLocation, Medallion> = emptyMap(),
    val bottlePlacements: Map<BottleLocation, BottleApi> = emptyMap(),
    val dropOverrides: Map<PrizePack, Map<Int, Drop>> = emptyMap(),
    val textOverrides: Map<TextDialog, String> = emptyMap(),
    val itemCounts: Map<Item, Int> = emptyMap(),
    val goalRequired: Int? = null,
    // "Allow dark room navigation" in the site's UI — true skips the lamp requirement in
    // dark rooms. Defaults to false (lamp required) to match the site's own default.
    val requireLamp: Boolean? = null,
    val prizeToggles: CustomPrizeToggles = CustomPrizeToggles(),
    val regionToggles: CustomRegionToggles = CustomRegionToggles(),
    val genericKeys: Boolean? = null,
    val hudItemCounter: Boolean? = null,
    val dungeonCount: CompassMode? = null,
    val timerMode: ClockMode? = null,
    val bootsLocationSpoiler: Boolean? = null,
)

@Serializable
data class CustomPrizeToggles(
    val crossWorld: Boolean? = null,
    val shufflePendants: Boolean? = null,
    val shuffleCrystals: Boolean? = null,
)

@Serializable
data class CustomRegionToggles(
    val bossNormalLocation: Boolean? = null,
    val pyramidBowUpgrade: Boolean? = null,
    val bossHaveKey: Boolean? = null,
    val forceSkullWoodsKey: Boolean? = null,
    val wildKeys: Boolean? = null,
    val wildBigKeys: Boolean? = null,
    val wildMaps: Boolean? = null,
    val wildCompasses: Boolean? = null,
)
