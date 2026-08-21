package com.stingers.alttpr.screens.edit

import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.model.HeartSpeed
import com.stingers.alttpr.model.MenuSpeed
import com.stingers.alttpr.model.Sprite


sealed interface EditRomEvent {
    object ReRollSeed : EditRomEvent
    object SaveSeed : EditRomEvent
    object PlaySeed : EditRomEvent
    object ExportRom : EditRomEvent

    data class SetHeartSpeed(val value: HeartSpeed) : EditRomEvent
    data class SetHeartColor(val value: HeartColor) : EditRomEvent
    data class SetMenuSpeed(val value: MenuSpeed) : EditRomEvent
    data class SetQuickSwap(val value: Boolean) : EditRomEvent
    data class SetReduceFlashing(val value: Boolean) : EditRomEvent
    data class SetEnableMusic(val value: Boolean) : EditRomEvent

    data class SetMsuResume(val value: Boolean) : EditRomEvent
    data class SetSprite(val value: Sprite) : EditRomEvent
}
