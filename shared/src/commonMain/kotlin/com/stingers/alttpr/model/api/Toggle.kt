package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.toggle_on
import alttpr.shared.generated.resources.toggle_off
import org.jetbrains.compose.resources.StringResource

enum class Toggle(val title: StringResource, val value: String) {
    On(Res.string.toggle_on, "on"),
    Off(Res.string.toggle_off, "off"),
}
