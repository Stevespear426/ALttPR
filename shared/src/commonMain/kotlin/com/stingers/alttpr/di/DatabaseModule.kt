package com.stingers.alttpr.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.stingers.alttpr.repository.local.AppDatabase
import com.stingers.alttpr.repository.local.LoggerDao
import com.stingers.alttpr.repository.local.SeedDao
import com.stingers.alttpr.repository.local.SpriteDao
import com.stingers.alttpr.repository.local.getDatabaseBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
class DatabaseModule {

    @Singleton
    fun provideAppDatabase(): AppDatabase {
        return getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    @Singleton
    fun provideSeedDao(database: AppDatabase): SeedDao {
        return database.seedDao()
    }

    @Singleton
    fun provideSpriteDao(database: AppDatabase): SpriteDao {
        return database.spriteDao()
    }

    @Singleton
    fun provideLoggerDao(database: AppDatabase): LoggerDao {
        return database.loggerDao()
    }
}
