package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.world_state_open
import alttpr.shared.generated.resources.world_state_standard
import alttpr.shared.generated.resources.world_state_inverted
import alttpr.shared.generated.resources.world_state_retro
import org.jetbrains.compose.resources.StringResource

enum class WorldState(val title: StringResource, val value: String) {
    Open(Res.string.world_state_open, "open"),
    Standard(Res.string.world_state_standard, "standard"),
    Inverted(Res.string.world_state_inverted, "inverted"),
    Retro(Res.string.world_state_retro, "retro"),
}
