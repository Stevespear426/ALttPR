package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.enemy_health_default
import alttpr.shared.generated.resources.enemy_health_easy
import alttpr.shared.generated.resources.enemy_health_hard
import alttpr.shared.generated.resources.enemy_health_expert
import org.jetbrains.compose.resources.StringResource

enum class EnemyHealth(val title: StringResource, val value: String) {
    Default(Res.string.enemy_health_default, "default"),
    Easy(Res.string.enemy_health_easy, "easy"),
    Hard(Res.string.enemy_health_hard, "hard"),
    Expert(Res.string.enemy_health_expert, "expert"),
}
