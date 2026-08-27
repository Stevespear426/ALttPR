package com.stingers.alttpr.model

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpoilerMeta(
    @SerialName("name") val name: String? = null,
    @SerialName("entry_crystals_ganon") val entryCrystalsGanon: String? = null,
    @SerialName("entry_crystals_tower") val entryCrystalsTower: String? = null,
    @SerialName("worlds") val worlds: Int? = null,
    @SerialName("item_placement") val itemPlacement: String? = null,
    @SerialName("item_pool") val itemPool: String? = null,
    @SerialName("item_functionality") val itemFunctionality: String? = null,
    @SerialName("dungeon_items") val dungeonItems: String? = null,
    @SerialName("logic") val logic: String? = null,
    @SerialName("accessibility") val accessibility: String? = null,
    @SerialName("rom_mode") val romMode: String? = null,
    @SerialName("goal") val goal: String? = null,
    @SerialName("build") val build: String? = null,
    @SerialName("mode") val mode: String? = null,
    @SerialName("weapons") val weapons: String? = null,
    @SerialName("world_id") val worldId: Int? = null,
    @SerialName("tournament") val tournament: Boolean = false,
    @SerialName("size") val size: Int? = null,
    @SerialName("hints") val hints: String? = null,
    @SerialName("shuffle") val entranceShuffle: String? = null,
    @SerialName("spoilers") val spoilers: String? = null,
    @SerialName("allow_quickswap") val allowQuickswap: Boolean? = null,
    @SerialName("pseudoboots") val pseudoboots: Boolean? = null,
    @SerialName("enemizer.boss_shuffle") val enemizerBossShuffle: String? = null,
    @SerialName("enemizer.enemy_shuffle") val enemizerEnemyShuffle: String? = null,
    @SerialName("enemizer.enemy_damage") val enemizerEnemyDamage: String? = null,
    @SerialName("enemizer.enemy_health") val enemizerEnemyHealth: String? = null,
    @SerialName("enemizer.pot_shuffle") val enemizerPotShuffle: String? = null
) {

    fun getFileName(): String {
        return if (name.isNullOrEmpty()) {
            "${logic.orEmpty()}-${mode.orEmpty()}-${goal.orEmpty()}"
        } else {
            name
        }.replace("\n", "")
    }
}


class SpoilerMetaParameterProvider : PreviewParameterProvider<SpoilerMeta> {
    override val values = sequenceOf(
        SpoilerMeta(
            name = "Daily Challenge: Aug 17",
            build = "2023-09-22",
            accessibility = "none",
            mode = "open",
            weapons = "vanilla",
            goal = "fast_ganon",
            logic = "NoGlitches"
        ),
        SpoilerMeta(
            name = "Daily Challenge: Aug 17",
            build = "2023-09-22",
            spoilers = "mystery"
        )
    )
}