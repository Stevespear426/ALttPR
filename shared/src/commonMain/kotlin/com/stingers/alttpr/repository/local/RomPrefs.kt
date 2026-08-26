package com.stingers.alttpr.repository.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.model.HeartSpeed
import com.stingers.alttpr.model.MenuSpeed
import com.stingers.alttpr.utils.toEnumOrDefault
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton

@Singleton
open class RomPrefs(
    @Named("romPrefs") private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val QUICK_SWAP = booleanPreferencesKey("quick_swap")
        val REDUCE_FLASHING = booleanPreferencesKey("reduce_flashing")
        val ENABLE_MUSIC = booleanPreferencesKey("enable_music")
        val HEART_SPEED = stringPreferencesKey("heart_speed")
        val MENU_SPEED = stringPreferencesKey("menu_speed")
        val HEART_COLOR = stringPreferencesKey("heart_color")
        val MSU_RESUME = booleanPreferencesKey("msu_resume")
        val SPRITE = stringPreferencesKey("sprite")
    }

    open val msuResume: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.MSU_RESUME] ?: true
        }

    open val quickSwap: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.QUICK_SWAP] ?: true
        }

    open val reduceFlashing: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.REDUCE_FLASHING] ?: false
        }

    open val enableMusic: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.ENABLE_MUSIC] ?: true
        }

    open val heartSpeed: Flow<HeartSpeed> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.HEART_SPEED] ?: HeartSpeed.NORMAL.name
    }.map { it.toEnumOrDefault(HeartSpeed.NORMAL) }

    open val menuSpeed: Flow<MenuSpeed> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.MENU_SPEED] ?: MenuSpeed.NORMAL.name
        }.map { it.toEnumOrDefault(MenuSpeed.NORMAL) }

    open val heartColor: Flow<HeartColor> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.HEART_COLOR] ?: HeartColor.RED.name
        }.map { it.toEnumOrDefault(HeartColor.RED) }

    open val sprite: Flow<String?> = dataStore.data
        .map { preferences -> preferences[PreferencesKeys.SPRITE] }


    open suspend fun setQuickSwap(quickSwap: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.QUICK_SWAP] = quickSwap
        }
    }

    open suspend fun setReduceFlashing(reduceFlashing: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.REDUCE_FLASHING] = reduceFlashing
        }
    }

    open suspend fun setMsuResume(msuResume: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MSU_RESUME] = msuResume
        }
    }

    open suspend fun setEnableMusic(enableMusic: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ENABLE_MUSIC] = enableMusic
        }
    }

    open suspend fun setHeartSpeed(heartSpeed: HeartSpeed) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HEART_SPEED] = heartSpeed.name
        }
    }

    open suspend fun setMenuSpeed(menuSpeed: MenuSpeed) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MENU_SPEED] = menuSpeed.name
        }
    }

    open suspend fun setHeartColor(heartColor: HeartColor) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HEART_COLOR] = heartColor.name
        }
    }

    open suspend fun setSprite(name: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SPRITE] = name
        }
    }
}