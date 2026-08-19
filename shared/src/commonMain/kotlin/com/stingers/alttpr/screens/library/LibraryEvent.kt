package com.stingers.alttpr.screens.library

import com.stingers.alttpr.model.RomEntity

sealed interface LibraryEvent {
    data class OpenEditSeed(val value: RomEntity) : LibraryEvent
    data class RemoveSeed(val value: RomEntity) : LibraryEvent
}
