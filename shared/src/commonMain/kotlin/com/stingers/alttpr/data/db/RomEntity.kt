package com.stingers.alttpr.data.db

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.stingers.alttpr.domain.model.GameMode

@Entity(tableName = "roms")
data class RomEntity(
    @PrimaryKey
    val hash: String,
    val createdAt: Long,
    val localFileName: String,
    val gameMode: GameMode
)
