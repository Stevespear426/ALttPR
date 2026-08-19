package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.normal
import alttpr.shared.generated.resources.enemy_health_hard
import alttpr.shared.generated.resources.enemy_health_expert
import alttpr.shared.generated.resources.item_functionality_super_expert
import alttpr.shared.generated.resources.item_pool_crowd_control
import org.jetbrains.compose.resources.StringResource

enum class ItemPool(val title: StringResource, val value: String) {
    Normal(Res.string.normal, "normal"),
    Hard(Res.string.enemy_health_hard, "hard"),
    Expert(Res.string.enemy_health_expert, "expert"),
    Superexpert(Res.string.item_functionality_super_expert, "superexpert"),
    CrowdControl(Res.string.item_pool_crowd_control, "crowd_control"),
}
