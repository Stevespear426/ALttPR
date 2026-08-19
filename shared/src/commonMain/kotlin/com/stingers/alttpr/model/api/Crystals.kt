package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.crystals_zero
import alttpr.shared.generated.resources.crystals_one
import alttpr.shared.generated.resources.crystals_two
import alttpr.shared.generated.resources.crystals_three
import alttpr.shared.generated.resources.crystals_four
import alttpr.shared.generated.resources.crystals_five
import alttpr.shared.generated.resources.crystals_six
import alttpr.shared.generated.resources.crystals_seven
import alttpr.shared.generated.resources.crystals_random
import org.jetbrains.compose.resources.StringResource

enum class Crystals(val title: StringResource, val value: String) {
    Zero(Res.string.crystals_zero, "0"),
    One(Res.string.crystals_one, "1"),
    Two(Res.string.crystals_two, "2"),
    Three(Res.string.crystals_three, "3"),
    Four(Res.string.crystals_four, "4"),
    Five(Res.string.crystals_five, "5"),
    Six(Res.string.crystals_six, "6"),
    Seven(Res.string.crystals_seven, "7"),
    Random(Res.string.crystals_random, "random"),
}
