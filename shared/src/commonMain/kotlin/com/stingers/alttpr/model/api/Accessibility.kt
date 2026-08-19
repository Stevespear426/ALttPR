package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.accessibility_items
import alttpr.shared.generated.resources.accessibility_locations
import alttpr.shared.generated.resources.accessibility_beatable
import org.jetbrains.compose.resources.StringResource

enum class Accessibility(val title: StringResource, val value: String) {
    Items(Res.string.accessibility_items, "items"),
    Locations(Res.string.accessibility_locations, "locations"),
    Beatable(Res.string.accessibility_beatable, "none"),
}
