package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.compass_mode_on
import alttpr.shared.generated.resources.compass_mode_off
import alttpr.shared.generated.resources.compass_mode_pickup
import org.jetbrains.compose.resources.StringResource

enum class CompassMode(val title: StringResource, val value: String) {
    On(Res.string.compass_mode_on, "on"),
    Off(Res.string.compass_mode_off, "off"),
    Pickup(Res.string.compass_mode_pickup, "pickup"),
}
