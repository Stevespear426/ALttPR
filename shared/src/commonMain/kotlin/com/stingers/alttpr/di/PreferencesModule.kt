package com.stingers.alttpr.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.stingers.alttpr.repository.local.createPrefsDataStore
import org.koin.core.annotation.Named
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
class PreferencesModule {

    @Singleton
    @Named("appPrefs")
    fun provideAppPrefsDataStore(): DataStore<Preferences> {
        return createPrefsDataStore("app_prefs.preferences_pb")
    }

    @Singleton
    @Named("romPrefs")
    fun provideRomPrefsDataStore(): DataStore<Preferences> {
        return createPrefsDataStore("rom_prefs.preferences_pb")
    }
}
