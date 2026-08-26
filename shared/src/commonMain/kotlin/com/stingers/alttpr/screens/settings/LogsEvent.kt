package com.stingers.alttpr.screens.settings

import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.model.HeartSpeed
import com.stingers.alttpr.model.LogType
import com.stingers.alttpr.model.MenuSpeed
import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.navigation.Screen


sealed interface LogsEvent {
    object ClearLogs: LogsEvent
    data class SetLogType(val value: LogType?): LogsEvent
    data class SetLogFilter(val value: String): LogsEvent
    object NavigateBack: LogsEvent
}
