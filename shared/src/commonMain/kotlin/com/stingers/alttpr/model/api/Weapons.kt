package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.weapons_randomized
import alttpr.shared.generated.resources.weapons_assured
import alttpr.shared.generated.resources.weapons_vanilla
import alttpr.shared.generated.resources.weapons_swordless
import org.jetbrains.compose.resources.StringResource

enum class Weapons(val title: StringResource, val value: String) {
    Randomized(Res.string.weapons_randomized, "randomized"),
    Assured(Res.string.weapons_assured, "assured"),
    Vanilla(Res.string.weapons_vanilla, "vanilla"),
    Swordless(Res.string.weapons_swordless, "swordless"),
}
