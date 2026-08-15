package com.stingers.alttpr.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.stingers.alttpr.repository.local.AppDatabase
import com.stingers.alttpr.repository.local.RomDao
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
    fun provideRomDao(database: AppDatabase): RomDao {
        return database.romDao()
    }
}
