package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.normal
import alttpr.shared.generated.resources.half
import alttpr.shared.generated.resources.quarter
import alttpr.shared.generated.resources.double
import alttpr.shared.generated.resources.off
import org.jetbrains.compose.resources.StringResource

enum class HeartSpeedApi(val title: StringResource, val value: String) {
    Normal(Res.string.normal, "normal"),
    Half(Res.string.half, "half"),
    Quarter(Res.string.quarter, "quarter"),
    Double(Res.string.double, "double"),
    Off(Res.string.off, "off"),
}
