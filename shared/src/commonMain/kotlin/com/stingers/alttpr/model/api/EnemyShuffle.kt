package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.enemy_shuffle_none
import alttpr.shared.generated.resources.enemy_shuffle_shuffled
import alttpr.shared.generated.resources.enemy_shuffle_random
import org.jetbrains.compose.resources.StringResource

enum class EnemyShuffle(val title: StringResource, val value: String) {
    None(Res.string.enemy_shuffle_none, "none"),
    Shuffled(Res.string.enemy_shuffle_shuffled, "shuffled"),
    Random(Res.string.enemy_shuffle_random, "random"),
}
