package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.bottle_empty
import alttpr.shared.generated.resources.bottle_red_potion
import alttpr.shared.generated.resources.bottle_green_potion
import alttpr.shared.generated.resources.bottle_blue_potion
import alttpr.shared.generated.resources.bottle_bee
import alttpr.shared.generated.resources.bottle_fairy
import alttpr.shared.generated.resources.bottle_gold_bee
import org.jetbrains.compose.resources.StringResource

enum class BottleApi(val title: StringResource, val value: String) {
    BottleEmpty(Res.string.bottle_empty, "Bottle"),
    BottleRedPotion(Res.string.bottle_red_potion, "BottleWithRedPotion"),
    BottleGreenPotion(Res.string.bottle_green_potion, "BottleWithGreenPotion"),
    BottleBluePotion(Res.string.bottle_blue_potion, "BottleWithBluePotion"),
    BottleBee(Res.string.bottle_bee, "BottleWithBee"),
    BottleFairy(Res.string.bottle_fairy, "BottleWithFairy"),
    BottleGoldBee(Res.string.bottle_gold_bee, "BottleWithGoldBee"),
}
