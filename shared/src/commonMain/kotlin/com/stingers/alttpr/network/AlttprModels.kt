package com.stingers.alttpr.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyResponse(
    @SerialName("hash") val hash: String,
    @SerialName("seed") val seed: Long? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("spoiler") val spoiler: Boolean? = null
)

@Serializable
data class SeedResponse(
    @SerialName("hash") val hash: String,
    @SerialName("bpsLocation") val bpsLocation: String,
    @SerialName("seed") val seed: Long? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("game") val game: String? = null,
    @SerialName("rom_mode") val romMode: String? = null,
    @SerialName("logic") val logic: String? = null,
    @SerialName("mode") val mode: String? = null,
    @SerialName("swords") val swords: String? = null,
    @SerialName("item_pool") val itemPool: String? = null,
    @SerialName("item_functionality") val itemFunctionality: String? = null,
    @SerialName("item_placement") val itemPlacement: String? = null,
    @SerialName("dungeon_items") val dungeonItems: String? = null,
    @SerialName("accessibility") val accessibility: String? = null,
    @SerialName("goal") val goal: String? = null,
    @SerialName("crystals_ganon") val crystalsGanon: String? = null,
    @SerialName("crystals_tower") val crystalsTower: String? = null,
    @SerialName("tournament") val tournament: Boolean? = null,
    @SerialName("spoiler") val spoiler: Boolean? = null,
    @SerialName("seed_number") val seedNumber: Long? = null,
    @SerialName("hash_name") val hashName: List<String>? = null,
    @SerialName("notes") val notes: String? = null,
    @SerialName("build") val build: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)
