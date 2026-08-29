package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.palette_algorithm_maseya
import alttpr.shared.generated.resources.palette_algorithm_grayscale
import alttpr.shared.generated.resources.palette_algorithm_negative
import alttpr.shared.generated.resources.palette_algorithm_blackout
import alttpr.shared.generated.resources.palette_algorithm_classic
import alttpr.shared.generated.resources.palette_algorithm_dizzy
import alttpr.shared.generated.resources.palette_algorithm_sick
import alttpr.shared.generated.resources.palette_algorithm_puke
import alttpr.shared.generated.resources.palette_algorithm_random
import org.jetbrains.compose.resources.StringResource

enum class PaletteAlgorithm(val title: StringResource, val value: String) {
    Maseya(Res.string.palette_algorithm_maseya, "maseya"),
    Grayscale(Res.string.palette_algorithm_grayscale, "grayscale"),
    Negative(Res.string.palette_algorithm_negative, "negative"),
    Blackout(Res.string.palette_algorithm_blackout, "blackout"),
    Classic(Res.string.palette_algorithm_classic, "classic"),
    Dizzy(Res.string.palette_algorithm_dizzy, "dizzy"),
    Sick(Res.string.palette_algorithm_sick, "sick"),
    Puke(Res.string.palette_algorithm_puke, "puke"),
    Random(Res.string.palette_algorithm_random, "random"),
}
