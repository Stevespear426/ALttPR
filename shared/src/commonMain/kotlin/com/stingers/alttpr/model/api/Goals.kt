package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.goals_defeat_ganon
import alttpr.shared.generated.resources.goals_fast_ganon
import alttpr.shared.generated.resources.goals_pedestal
import alttpr.shared.generated.resources.goals_dungeons
import alttpr.shared.generated.resources.goals_triforce_hunt
import alttpr.shared.generated.resources.goals_ganonhunt
import alttpr.shared.generated.resources.goals_completionist
import com.stingers.alttpr.model.api.Goals.entries
import org.jetbrains.compose.resources.StringResource

enum class Goals(val title: StringResource, val value: String) {
    DefeatGanon(Res.string.goals_defeat_ganon, "ganon"),
    FastGanon(Res.string.goals_fast_ganon, "fast_ganon"),
    Pedestal(Res.string.goals_pedestal, "pedestal"),
    Dungeons(Res.string.goals_dungeons, "dungeons"),
    TriforceHunt(Res.string.goals_triforce_hunt, "triforce-hunt"),
    Ganonhunt(Res.string.goals_ganonhunt, "ganonhunt"),
    Completionist(Res.string.goals_completionist, "completionist");

}

fun String.toGoal(): Goals? {
    return entries.find { it.value == this }
}
