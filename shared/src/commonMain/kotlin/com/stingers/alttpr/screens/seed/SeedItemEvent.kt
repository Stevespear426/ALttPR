package com.stingers.alttpr.screens.seed

import com.stingers.alttpr.model.SeedEntity


sealed interface SeedItemEvent {
    object SaveSeed : SeedItemEvent
    object PlaySeed : SeedItemEvent
    object ExportRom : SeedItemEvent
    data class OpenEditSeed(val value: SeedEntity) : SeedItemEvent
    data class RemoveSeed(val value: SeedEntity) : SeedItemEvent
}
