package com.stingers.alttpr.screens.seed

import com.stingers.alttpr.model.SeedEntity


sealed interface SeedItemEvent {
    object SaveSeed : SeedItemEvent
    object PlaySeed : SeedItemEvent
    object ExportRom : SeedItemEvent
    object OpenEditSeed : SeedItemEvent
    object RemoveSeed : SeedItemEvent

}
