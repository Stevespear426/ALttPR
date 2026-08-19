package com.stingers.alttpr.screens.library

import com.stingers.alttpr.model.RomEntity

sealed interface LibraryEvent {
    data class RemoveSeed(val value: RomEntity) : LibraryEvent
}
