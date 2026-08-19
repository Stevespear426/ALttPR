package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.prize_green_pendant
import alttpr.shared.generated.resources.prize_red_pendant
import alttpr.shared.generated.resources.prize_blue_pendant
import alttpr.shared.generated.resources.prize_crystal_1
import alttpr.shared.generated.resources.prize_crystal_2
import alttpr.shared.generated.resources.prize_crystal_3
import alttpr.shared.generated.resources.prize_crystal_4
import alttpr.shared.generated.resources.prize_crystal_5
import alttpr.shared.generated.resources.prize_crystal_6
import alttpr.shared.generated.resources.prize_crystal_7
import org.jetbrains.compose.resources.StringResource

enum class Prize(val title: StringResource, val value: String) {
    GreenPendant(Res.string.prize_green_pendant, "PendantOfCourage"),
    RedPendant(Res.string.prize_red_pendant, "PendantOfWisdom"),
    BluePendant(Res.string.prize_blue_pendant, "PendantOfPower"),
    Crystal1(Res.string.prize_crystal_1, "Crystal1"),
    Crystal2(Res.string.prize_crystal_2, "Crystal2"),
    Crystal3(Res.string.prize_crystal_3, "Crystal3"),
    Crystal4(Res.string.prize_crystal_4, "Crystal4"),
    Crystal5(Res.string.prize_crystal_5, "Crystal5"),
    Crystal6(Res.string.prize_crystal_6, "Crystal6"),
    Crystal7(Res.string.prize_crystal_7, "Crystal7"),
}
