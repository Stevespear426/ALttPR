package com.stingers.alttpr.repository.local

import androidx.paging.PagingSource
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.stingers.alttpr.model.SeedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeed(seed: SeedEntity)

    @Query("SELECT * FROM seed ORDER BY created DESC")
    fun getSeeds(): PagingSource<Int, SeedEntity>

    @Query("SELECT * FROM seed WHERE hash = :hash")
    suspend fun getSeed(hash: String): SeedEntity?

    @Query("SELECT * FROM seed WHERE hash = :hash")
    fun getSeedFlow(hash: String): Flow<SeedEntity?>

    @Query("DELETE FROM seed WHERE hash = :hash")
    suspend fun deleteSeed(hash: String)

    @Query("DELETE FROM seed")
    suspend fun deleteAllSeeds()
}
