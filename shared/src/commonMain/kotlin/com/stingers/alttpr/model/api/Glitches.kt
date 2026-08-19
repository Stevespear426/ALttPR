package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.glitches_none
import alttpr.shared.generated.resources.glitches_overworld
import alttpr.shared.generated.resources.glitches_hybrid
import alttpr.shared.generated.resources.glitches_major
import alttpr.shared.generated.resources.glitches_no_logic
import org.jetbrains.compose.resources.StringResource

enum class Glitches(val title: StringResource, val value: String) {
    None(Res.string.glitches_none, "none"),
    Overworld(Res.string.glitches_overworld, "overworld_glitches"),
    Hybrid(Res.string.glitches_hybrid, "hybrid_major_glitches"),
    Major(Res.string.glitches_major, "major_glitches"),
    NoLogic(Res.string.glitches_no_logic, "no_logic"),
}
