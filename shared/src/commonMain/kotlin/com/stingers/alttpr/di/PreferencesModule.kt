package com.stingers.alttpr.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.stingers.alttpr.repository.local.createDataStore
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
class PreferencesModule {

    @Singleton
    fun providePreferencesDataStore(): DataStore<Preferences> {
        return createDataStore()
    }
}
