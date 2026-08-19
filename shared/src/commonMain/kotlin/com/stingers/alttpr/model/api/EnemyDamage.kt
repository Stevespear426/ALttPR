package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.enemy_damage_default
import alttpr.shared.generated.resources.enemy_damage_shuffled
import alttpr.shared.generated.resources.enemy_damage_random
import org.jetbrains.compose.resources.StringResource

enum class EnemyDamage(val title: StringResource, val value: String) {
    Default(Res.string.enemy_damage_default, "default"),
    Shuffled(Res.string.enemy_damage_shuffled, "shuffled"),
    Random(Res.string.enemy_damage_random, "random"),
}
