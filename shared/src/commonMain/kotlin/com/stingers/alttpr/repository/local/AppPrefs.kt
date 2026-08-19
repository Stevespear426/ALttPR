package com.stingers.alttpr.repository.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Singleton

@Singleton
class AppPrefs(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val DEBUG_MODE = booleanPreferencesKey("debug_mode")
    }

    val debugMode: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DEBUG_MODE] ?: false
        }

    suspend fun setDebugMode(debugMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DEBUG_MODE] = debugMode
        }
    }
}