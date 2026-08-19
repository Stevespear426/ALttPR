package com.stingers.alttpr.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CrystalsConfig(
    @SerialName("tower") val tower: String? = null,
    @SerialName("ganon") val ganon: String? = null
)

@Serializable
data class EnemizerConfig(
    @SerialName("boss_shuffle") val bossShuffle: String? = null,
    @SerialName("enemy_shuffle") val enemyShuffle: String? = null,
    @SerialName("pot_shuffle") val potShuffle: String? = null,
    @SerialName("enemy_damage") val enemyDamage: String? = null,
    @SerialName("enemy_health") val enemyHealth: String? = null
)

@Serializable
data class ItemConfig(
    @SerialName("pool") val pool: String? = null,
    @SerialName("functionality") val functionality: String? = null
)

@Serializable
data class GenerateSeedRequest(
    @SerialName("lang") val lang: String? = null,
    @SerialName("glitches") val glitches: String? = null,
    @SerialName("item_placement") val itemPlacement: String? = null,
    @SerialName("dungeon_items") val dungeonItems: String? = null,
    @SerialName("accessibility") val accessibility: String? = null,
    @SerialName("goal") val goal: String? = null,
    @SerialName("crystals") val crystals: CrystalsConfig? = null,
    @SerialName("mode") val mode: String? = null,
    @SerialName("entrances") val entrances: String? = null,
    @SerialName("enemizer") val enemizer: EnemizerConfig? = null,
    @SerialName("hints") val hints: String? = null,
    @SerialName("weapons") val weapons: String? = null,
    @SerialName("item") val item: ItemConfig? = null,
    @SerialName("tournament") val tournament: Boolean? = null,
    @SerialName("spoilers") val spoilers: String? = null,
    @SerialName("allow_quickswap") val allowQuickswap: Boolean? = null,
    @SerialName("override_start_screen") val overrideStartScreen: Boolean? = null,
    @SerialName("pseudoboots") val pseudoboots: Boolean? = null,
    @SerialName("notes") val notes: String? = null,
    @SerialName("name") val name: String = "${glitches.orEmpty()}-${mode.orEmpty()}-${goal.orEmpty()}",
)


