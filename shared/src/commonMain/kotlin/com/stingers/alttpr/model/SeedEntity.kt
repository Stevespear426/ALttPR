package com.stingers.alttpr.model

import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.Ignore
import androidx.room3.PrimaryKey
import com.stingers.alttpr.model.api.GenerateSeedRequest
import com.stingers.alttpr.model.api.Hash
import com.stingers.alttpr.utils.currentTimeInMillis
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "seed")
data class SeedEntity(
    @PrimaryKey
    val hash: String,
    val logic: String? = null,
    val generated: String? = null,
    val created: Long = currentTimeInMillis(),
    val updated: Long = currentTimeInMillis(),
    val lastPlayed: Long? = null,
    val completed: Long? = null,
    val favorite: Boolean = false,
    val notes: String = "",
    val size: Int = 2,
    @Embedded(prefix = "meta_")
    val meta: SpoilerMeta? = null,
    val patch: List<Map<String, List<Int>>> = emptyList(),
    val md5: String? = null,
    val localFileName: String? = null,
    @Ignore
    val request: GenerateSeedRequest? = null
) {

    fun getHashCode(
        targetOffset: Int = 1573397,
        length: Int = 5
    ): List<Hash>? {
        // 1. Flatten into pairs of (Int address -> List<Int> bytes)
        val entries = patch.flatMap { map ->
            map.map { (key, bytes) -> key.toInt() to bytes }
        }

        // 2. Find the entry with the highest address that is <= targetOffset
        val (baseAddress, bytes) = entries
            .filter { (address, _) -> address <= targetOffset }
            .maxByOrNull { (address, _) -> address }
            ?: return null

        // 3. Calculate how far into this byte array your target address begins
        val seekOffset = targetOffset - baseAddress

        // 4. Ensure the array actually contains enough data for the seek + requested length
        if (seekOffset + length > bytes.size) return null

        // 5. Slice and return the target bytes
        return bytes.subList(seekOffset, seekOffset + length).map { Hash.entries[it] }
    }

    fun getFileName(): String {
        return "alttpr - ${meta?.getFileName().orEmpty()}_${hash}"
    }
}
