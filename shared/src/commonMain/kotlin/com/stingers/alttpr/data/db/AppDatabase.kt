package com.stingers.alttpr.data.db

import androidx.room3.ConstructedBy
import androidx.room3.DaoReturnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.room3.paging.PagingSourceDaoReturnTypeConverter

@Database(
    entities = [RomEntity::class],
    version = 1,
    autoMigrations = [],
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
@DaoReturnTypeConverters(PagingSourceDaoReturnTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun romDao(): RomDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

