package com.stingers.alttpr.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class DailyResponse(
    @SerialName("hash") val hash: String,
    @SerialName("daily") val daily: String? = null,
)

@Serializable
data class BasePatchInfoResponse(
    @SerialName("hash") val hash: String,
    @SerialName("bpsLocation") val bpsLocation: String,
    @SerialName("md5") val md5: String,
)

@Serializable
data class SpoilerWrapper(
    @SerialName("meta") val meta: SpoilerMeta? = null
)


@Serializable
data class SeedDetailsResponse(
    @SerialName("logic") val logic: String? = null,
    @SerialName("patch") val patch:List<Map<String, List<Int>>>? = null,
    @SerialName("spoiler") val spoiler: SpoilerWrapper? = null,
    @SerialName("hash") val hash: String,
    @SerialName("generated") val generated: String? = null,
    @SerialName("size") val size: Int? = null
)
