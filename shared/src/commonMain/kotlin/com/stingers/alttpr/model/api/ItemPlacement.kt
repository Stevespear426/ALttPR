package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.item_placement_basic
import alttpr.shared.generated.resources.item_placement_advanced
import org.jetbrains.compose.resources.StringResource

enum class ItemPlacement(val title: StringResource, val value: String) {
    Basic(Res.string.item_placement_basic, "basic"),
    Advanced(Res.string.item_placement_advanced, "advanced"),
}
