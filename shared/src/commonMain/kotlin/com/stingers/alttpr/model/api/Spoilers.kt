package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.spoilers_on
import alttpr.shared.generated.resources.spoilers_off
import alttpr.shared.generated.resources.spoilers_generate
import alttpr.shared.generated.resources.spoilers_mystery
import org.jetbrains.compose.resources.StringResource

enum class Spoilers(val title: StringResource, val value: String) {
    On(Res.string.spoilers_on, "on"),
    Off(Res.string.spoilers_off, "off"),
    Generate(Res.string.spoilers_generate, "generate"),
    Mystery(Res.string.spoilers_mystery, "mystery"),
}
