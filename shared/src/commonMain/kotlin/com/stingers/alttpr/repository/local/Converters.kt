package com.stingers.alttpr.repository.local

import androidx.room3.ColumnTypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    @ColumnTypeConverter
    fun fromPatchList(value: List<Map<String, List<Int>>>?): String? {
        return value?.let { json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toPatchList(value: String?): List<Map<String, List<Int>>>? {
        return value?.let {
            try { json.decodeFromString<List<Map<String, List<Int>>>>(it) } catch (_: Exception) { null }
        }
    }

    @ColumnTypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let {
            try { json.decodeFromString<List<String>>(it) } catch (_: Exception) { emptyList() }
        }
    }
}
