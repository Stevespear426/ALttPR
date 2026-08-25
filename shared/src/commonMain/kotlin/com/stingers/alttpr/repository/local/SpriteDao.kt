package com.stingers.alttpr.repository.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.stingers.alttpr.model.Sprite
import kotlinx.coroutines.flow.Flow

@Dao
interface SpriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSprites(sprites: List<Sprite>)

    @Query("SELECT * FROM sprites WHERE name = :name")
    fun getSprite(name: String): Flow<Sprite?>

    @Query("SELECT * FROM sprites")
    suspend fun getAllSprites(): List<Sprite>

    @Query("SELECT * FROM sprites WHERE downloadedFile IS NOT NULL")
    suspend fun getDownloadedSprites(): List<Sprite>

    @Query("DELETE FROM sprites")
    suspend fun deleteAllSprites()
}
