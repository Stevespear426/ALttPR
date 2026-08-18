package com.stingers.alttpr.repository.local

import androidx.room3.ColumnTypeConverters
import androidx.room3.ConstructedBy
import androidx.room3.DaoReturnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.room3.paging.PagingSourceDaoReturnTypeConverter
import com.stingers.alttpr.model.RomEntity
import com.stingers.alttpr.model.Sprite

@Database(
    entities = [RomEntity::class, Sprite::class],
    version = 2,
    autoMigrations = [],
    exportSchema = true
)
@ColumnTypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
@DaoReturnTypeConverters(PagingSourceDaoReturnTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun romDao(): RomDao
    abstract fun spriteDao(): SpriteDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

