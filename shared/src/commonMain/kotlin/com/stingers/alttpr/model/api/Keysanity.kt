package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.keysanity_standard
import alttpr.shared.generated.resources.keysanity_mc
import alttpr.shared.generated.resources.keysanity_mcs
import alttpr.shared.generated.resources.keysanity_full
import org.jetbrains.compose.resources.StringResource

enum class Keysanity(val title: StringResource, val value: String) {
    Standard(Res.string.keysanity_standard, "standard"),
    Mc(Res.string.keysanity_mc, "mc"),
    Mcs(Res.string.keysanity_mcs, "mcs"),
    Full(Res.string.keysanity_full, "full"),
}
