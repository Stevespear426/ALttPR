package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.rom_mode_no_glitches
import alttpr.shared.generated.resources.rom_mode_overworld_glitches
import alttpr.shared.generated.resources.rom_mode_major_glitches
import alttpr.shared.generated.resources.rom_mode_no_logic
import org.jetbrains.compose.resources.StringResource

enum class RomMode(val title: StringResource, val value: String) {
    NoGlitches(Res.string.rom_mode_no_glitches, "NoGlitches"),
    OverworldGlitches(Res.string.rom_mode_overworld_glitches, "OverworldGlitches"),
    MajorGlitches(Res.string.rom_mode_major_glitches, "MajorGlitches"),
    NoLogic(Res.string.rom_mode_no_logic, "NoLogic"),
}
