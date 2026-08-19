package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.heart_color_red
import alttpr.shared.generated.resources.heart_color_yellow
import alttpr.shared.generated.resources.heart_color_green
import alttpr.shared.generated.resources.heart_color_blue
import org.jetbrains.compose.resources.StringResource

enum class HeartColorApi(val title: StringResource, val value: String) {
    Red(Res.string.heart_color_red, "red"),
    Yellow(Res.string.heart_color_yellow, "yellow"),
    Green(Res.string.heart_color_green, "green"),
    Blue(Res.string.heart_color_blue, "blue"),
}
