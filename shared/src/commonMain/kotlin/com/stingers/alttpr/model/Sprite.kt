package com.stingers.alttpr.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "sprites")
data class Sprite(
    @PrimaryKey
    @SerialName("file") val fileUrl: String,
    @SerialName("name") val name: String = "",
    @SerialName("author") val author: String = "",
    @SerialName("preview") val previewUrl: String? = null,
    @SerialName("tags") val tags: List<String> = emptyList(),
    @SerialName("usage") val usage: List<String> = emptyList(),
    @SerialName("version") val version: Int = 0,
    val downloadedFile: String? = null
)
