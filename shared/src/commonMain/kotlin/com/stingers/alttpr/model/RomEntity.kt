package com.stingers.alttpr.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "roms")
data class RomEntity(
    @PrimaryKey
    val hash: String,
    val createdAt: Long,
    val localFileName: String,
    val gameMode: GameMode
)