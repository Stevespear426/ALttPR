package com.stingers.alttpr.screens.settings

import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.model.HeartSpeed
import com.stingers.alttpr.model.MenuSpeed
import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.navigation.Screen


sealed interface SettingsEvent {
    object ClearAppData: SettingsEvent
    data class EnableDebugMode(val value: Boolean): SettingsEvent
    data class NavigateTo(val value: Screen): SettingsEvent
}
