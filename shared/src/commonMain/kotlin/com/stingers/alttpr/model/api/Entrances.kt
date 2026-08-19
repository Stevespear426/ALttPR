package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.entrances_none
import alttpr.shared.generated.resources.entrances_simple
import alttpr.shared.generated.resources.entrances_restricted
import alttpr.shared.generated.resources.entrances_full
import alttpr.shared.generated.resources.entrances_crossed
import alttpr.shared.generated.resources.entrances_insanity
import org.jetbrains.compose.resources.StringResource

enum class Entrances(val title: StringResource, val value: String) {
    None(Res.string.entrances_none, "none"),
    Simple(Res.string.entrances_simple, "simple"),
    Restricted(Res.string.entrances_restricted, "restricted"),
    Full(Res.string.entrances_full, "full"),
    Crossed(Res.string.entrances_crossed, "crossed"),
    Insanity(Res.string.entrances_insanity, "insanity"),
}
