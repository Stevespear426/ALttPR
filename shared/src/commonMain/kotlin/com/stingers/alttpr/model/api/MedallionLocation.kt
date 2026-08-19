package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.medallion_location_turtle_rock
import alttpr.shared.generated.resources.medallion_location_misery_mire
import org.jetbrains.compose.resources.StringResource

enum class MedallionLocation(val title: StringResource, val value: String) {
    TurtleRock(Res.string.medallion_location_turtle_rock, "VHVydGxlIFJvY2sgTWVkYWxsaW9uOjE="),
    MiseryMire(Res.string.medallion_location_misery_mire, "TWlzZXJ5IE1pcmUgTWVkYWxsaW9uOjE="),
}
