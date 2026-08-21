package com.stingers.alttpr.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.stingers.alttpr.utils.currentTimeInMillis

@Entity
data class LogEntity(
    var type: String = LogType.DEBUG.name,
    var tag: String = "",
    var message: String = "",
    var stacktrace: String = "",
    var timestamp: Long = currentTimeInMillis(),
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
)
