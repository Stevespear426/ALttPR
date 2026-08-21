package com.stingers.alttpr.screens.library

import com.stingers.alttpr.model.SeedEntity

sealed interface LibraryEvent {
    data class OpenEditSeed(val value: SeedEntity) : LibraryEvent
    data class RemoveSeed(val value: SeedEntity) : LibraryEvent
}
