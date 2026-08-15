package com.stingers.alttpr.data.db

import androidx.paging.PagingSource
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query

@Dao
interface RomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRom(rom: RomEntity)

    @Query("SELECT * FROM roms ORDER BY createdAt DESC")
    fun getRoms(): PagingSource<Int, RomEntity>

    @Query("SELECT * FROM roms WHERE hash = :hash")
    suspend fun getRom(hash: String): RomEntity

    @Query("DELETE FROM roms WHERE hash = :hash")
    suspend fun deleteRom(hash: String)

    @Query("DELETE FROM roms")
    suspend fun deleteAllRoms()
}
