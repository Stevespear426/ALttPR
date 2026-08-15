package com.stingers.alttpr.model

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.heart_color_blue
import alttpr.shared.generated.resources.heart_color_green
import alttpr.shared.generated.resources.heart_color_red
import alttpr.shared.generated.resources.heart_color_yellow
import org.jetbrains.compose.resources.StringResource

enum class HeartColor(val title: StringResource, val value: Byte) {
    RED(
        title = Res.string.heart_color_red,
        value = 0x00.toByte()
    ),
    BLUE(
        title = Res.string.heart_color_blue,
        value = 0x01.toByte()
    ),
    GREEN(
        title = Res.string.heart_color_green,
        value = 0x02.toByte()
    ),
    YELLOW(
        title = Res.string.heart_color_yellow,
        value = 0x03.toByte()
    )
}
