package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.enemy_group_heart
import alttpr.shared.generated.resources.enemy_group_rupee
import alttpr.shared.generated.resources.enemy_group_magic
import alttpr.shared.generated.resources.enemy_group_bomb
import alttpr.shared.generated.resources.enemy_group_arrow
import alttpr.shared.generated.resources.enemy_group_small_variety
import alttpr.shared.generated.resources.enemy_group_big_variety
import org.jetbrains.compose.resources.StringResource

enum class EnemyGroup(val title: StringResource, val value: String) {
    Heart(Res.string.enemy_group_heart, "Heart"),
    Rupee(Res.string.enemy_group_rupee, "Rupee"),
    Magic(Res.string.enemy_group_magic, "Magic"),
    Bomb(Res.string.enemy_group_bomb, "Bomb"),
    Arrow(Res.string.enemy_group_arrow, "Arrow"),
    SmallVariety(Res.string.enemy_group_small_variety, "SmallVariety"),
    BigVariety(Res.string.enemy_group_big_variety, "BigVariety"),
}
