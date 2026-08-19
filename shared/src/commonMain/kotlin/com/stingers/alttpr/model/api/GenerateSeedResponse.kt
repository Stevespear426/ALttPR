package com.stingers.alttpr.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class GenerateSeedResponse(
    @SerialName("logic") val logic: String? = null,
    @SerialName("patch") val patch: List<Map<String, List<Int>>>? = null,
    @SerialName("spoiler") val spoiler: JsonObject? = null,
    @SerialName("hash") val hash: String,
    @SerialName("generated") val generated: String? = null,
    @SerialName("size") val size: Int? = null,
    @SerialName("current_rom_hash") val currentRomHash: String? = null
)
