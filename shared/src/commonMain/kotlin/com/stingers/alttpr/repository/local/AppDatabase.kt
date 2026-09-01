package com.stingers.alttpr.repository.local

import androidx.room3.AutoMigration
import androidx.room3.ColumnTypeConverters
import androidx.room3.ConstructedBy
import androidx.room3.DaoReturnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.room3.paging.PagingSourceDaoReturnTypeConverter
import com.stingers.alttpr.model.LogEntity
import com.stingers.alttpr.model.LogSearchIndex
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.model.Sprite

@Database(
    entities = [
        LogEntity::class,
        LogSearchIndex::class,
        SeedEntity::class,
        Sprite::class
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    exportSchema = true
)
@ColumnTypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
@DaoReturnTypeConverters(PagingSourceDaoReturnTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun loggerDao(): LoggerDao
    abstract fun seedDao(): SeedDao
    abstract fun spriteDao(): SpriteDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

