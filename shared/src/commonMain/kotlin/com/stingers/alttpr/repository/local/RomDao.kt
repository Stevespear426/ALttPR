package com.stingers.alttpr.repository.local

import androidx.paging.PagingSource
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.stingers.alttpr.model.RomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRom(rom: RomEntity)

    @Query("SELECT * FROM roms ORDER BY createdAt DESC")
    fun getRoms(): PagingSource<Int, RomEntity>

    @Query("SELECT * FROM roms WHERE hash = :hash")
    suspend fun getRom(hash: String): RomEntity

    @Query("SELECT * FROM roms WHERE hash = :hash")
    fun getRomFlow(hash: String): Flow<RomEntity>

    @Query("DELETE FROM roms WHERE hash = :hash")
    suspend fun deleteRom(hash: String)

    @Query("DELETE FROM roms")
    suspend fun deleteAllRoms()
}
