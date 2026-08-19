package com.stingers.alttpr.model

import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "roms")
data class RomEntity(
    @PrimaryKey
    val hash: String,
    val md5: String,
    val createdAt: Long,
    val localFileName: String,
    val gameMode: GameMode,
    val logic: String? = null,
    val generated: String? = null,
    val size: Int = 2,
    @Embedded(prefix = "meta_")
    val meta: SpoilerMeta? = null,
    val patch: List<Map<String, List<Int>>> = emptyList(),
)
