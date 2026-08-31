package com.stingers.alttpr.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomOverrides(
    // `item` and `rom` are never null: CustomizerController::prepSeed directly array-accesses
    // item.require.Lamp / rom.freeItemMenu / rom.freeItemText with no `??` fallback, so those
    // three leaf fields must always be present in the JSON (not just their parent object).
    @SerialName("item") val item: CustomItemOverrides,
    @SerialName("prize") val prize: CustomPrizeOverrides? = null,
    @SerialName("region") val region: CustomRegionOverrides? = null,
    @SerialName("rom") val rom: CustomRomOverrides,
    @SerialName("spoil") val spoil: CustomSpoilOverrides? = null,
)

@Serializable
data class CustomItemOverrides(
    @SerialName("count") val count: Map<String, Int>? = null,
    @SerialName("Goal") val goal: CustomGoalOverrides? = null,
    @SerialName("require") val require: CustomItemRequireOverrides,
)

@Serializable
data class CustomItemRequireOverrides(
    @SerialName("Lamp") val lamp: Boolean,
)

@Serializable
data class CustomGoalOverrides(
    @SerialName("Required") val required: Int? = null,
)

@Serializable
data class CustomPrizeOverrides(
    @SerialName("crossWorld") val crossWorld: Boolean? = null,
    @SerialName("shufflePendants") val shufflePendants: Boolean? = null,
    @SerialName("shuffleCrystals") val shuffleCrystals: Boolean? = null,
)

@Serializable
data class CustomRegionOverrides(
    @SerialName("bossNormalLocation") val bossNormalLocation: Boolean? = null,
    @SerialName("pyramidBowUpgrade") val pyramidBowUpgrade: Boolean? = null,
    @SerialName("bossHaveKey") val bossHaveKey: Boolean? = null,
    @SerialName("forceSkullWoodsKey") val forceSkullWoodsKey: Boolean? = null,
    @SerialName("wildKeys") val wildKeys: Boolean? = null,
    @SerialName("wildBigKeys") val wildBigKeys: Boolean? = null,
    @SerialName("wildMaps") val wildMaps: Boolean? = null,
    @SerialName("wildCompasses") val wildCompasses: Boolean? = null,
)

@Serializable
data class CustomRomOverrides(
    @SerialName("genericKeys") val genericKeys: Boolean? = null,
    @SerialName("hudItemCounter") val hudItemCounter: Boolean? = null,
    @SerialName("dungeonCount") val dungeonCount: String? = null,
    @SerialName("timerMode") val timerMode: String? = null,
    // Always required, no default (see CustomOverrides) — a field with a Kotlin default that
    // equals the value we always send would get silently dropped by encodeDefaults=false,
    // recreating the exact bug this is fixing. Purely a HUD-icon bitmask the server recomputes
    // from region.wild* when true; this app has no lever for it yet, so it's always sent false.
    @SerialName("freeItemMenu") val freeItemMenu: Boolean,
    @SerialName("freeItemText") val freeItemText: Boolean,
)

@Serializable
data class CustomSpoilOverrides(
    @SerialName("BootsLocation") val bootsLocation: Boolean? = null,
)
