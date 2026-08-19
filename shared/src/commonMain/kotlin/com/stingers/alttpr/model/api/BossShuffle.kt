package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.boss_shuffle_none
import alttpr.shared.generated.resources.boss_shuffle_simple
import alttpr.shared.generated.resources.boss_shuffle_full
import alttpr.shared.generated.resources.boss_shuffle_random
import org.jetbrains.compose.resources.StringResource

enum class BossShuffle(val title: StringResource, val value: String) {
    None(Res.string.boss_shuffle_none, "none"),
    Simple(Res.string.boss_shuffle_simple, "simple"),
    Full(Res.string.boss_shuffle_full, "full"),
    Random(Res.string.boss_shuffle_random, "random"),
}
