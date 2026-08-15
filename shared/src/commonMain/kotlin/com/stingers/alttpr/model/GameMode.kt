package com.stingers.alttpr.model

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.*
import org.jetbrains.compose.resources.StringResource

enum class GameMode(val titleRes: StringResource) {
    DEFAULT(Res.string.gamemode_default),
    BEGINNER(Res.string.gamemode_beginner),
    OWG(Res.string.gamemode_owg),
    CROSSKEYS(Res.string.gamemode_crosskeys),
    SUPER_QUICK(Res.string.gamemode_super_quick),
    NIGHTMARE(Res.string.gamemode_nightmare),
    CUSTOM(Res.string.gamemode_custom),
    DAILY_CHALLENGE(Res.string.gamemode_daily_challenge),
}
