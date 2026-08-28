package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.glitches_none
import alttpr.shared.generated.resources.glitches_overworld
import alttpr.shared.generated.resources.glitches_hybrid
import alttpr.shared.generated.resources.glitches_major
import alttpr.shared.generated.resources.glitches_no_logic
import com.stingers.alttpr.model.RandomizerGameModel
import org.jetbrains.compose.resources.StringResource

enum class Glitches(val title: StringResource, val value: String) {
    None(Res.string.glitches_none, "none") {
        override fun fileName() = "NoGlitches"
    },
    Overworld(Res.string.glitches_overworld, "overworld_glitches") {
        override fun fileName() = "OverworldGlitches"
    },
    Hybrid(Res.string.glitches_hybrid, "hybrid_major_glitches") {
        override fun fileName() = "HybridMajorGlitches"
    },
    Major(Res.string.glitches_major, "major_glitches") {
        override fun fileName() = "MajorGlitches"
    },
    NoLogic(Res.string.glitches_no_logic, "no_logic") {
        override fun fileName() = "NoLogic"
    };

    abstract fun fileName(): String
}

