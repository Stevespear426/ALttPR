package com.stingers.alttpr.model

import androidx.room3.Entity
import androidx.room3.Fts5

@Fts5(contentEntity = LogEntity::class)
@Entity
data class LogSearchIndex(
    val tag: String,
    val message: String,
    val stacktrace: String
)
