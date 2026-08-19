package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.normal
import alttpr.shared.generated.resources.menu_speed_slow
import alttpr.shared.generated.resources.menu_speed_fast
import alttpr.shared.generated.resources.instant
import org.jetbrains.compose.resources.StringResource

enum class MenuSpeedApi(val title: StringResource, val value: String) {
    Normal(Res.string.normal, "normal"),
    Slow(Res.string.menu_speed_slow, "slow"),
    Fast(Res.string.menu_speed_fast, "fast"),
    Instant(Res.string.instant, "instant"),
}
