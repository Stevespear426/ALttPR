package com.stingers.alttpr.model

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.double
import alttpr.shared.generated.resources.half
import alttpr.shared.generated.resources.instant
import alttpr.shared.generated.resources.normal
import alttpr.shared.generated.resources.quadruple
import alttpr.shared.generated.resources.triple
import org.jetbrains.compose.resources.StringResource

enum class MenuSpeed(val title: StringResource, val value: Byte) {
    INSTANT(
        title = Res.string.instant,
        value = 0xE8.toByte()
    ),
    QUADRUPLE(
        title = Res.string.quadruple,
        value = 0x20.toByte()
    ),
    TRIPLE(
        title = Res.string.triple,
        value = 0x18.toByte()
    ),
    DOUBLE(
        title = Res.string.double,
        value = 0x10.toByte()
    ),
    NORMAL(
        title = Res.string.normal,
        value = 0x08.toByte()
    ),
    HALF(
        title = Res.string.half,
        value = 0x04.toByte()
    )
}