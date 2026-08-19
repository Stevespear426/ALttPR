package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.bottle_location_waterfall_fairy
import alttpr.shared.generated.resources.bottle_location_pyramid_fairy
import org.jetbrains.compose.resources.StringResource

enum class BottleLocation(val title: StringResource, val value: String) {
    WaterfallFairy(Res.string.bottle_location_waterfall_fairy, "V2F0ZXJmYWxsIEJvdHRsZTox"),
    PyramidFairy(Res.string.bottle_location_pyramid_fairy, "UHlyYW1pZCBCb3R0bGU6MQ=="),
}
