package com.stingers.alttpr.model

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.double
import alttpr.shared.generated.resources.half
import alttpr.shared.generated.resources.normal
import alttpr.shared.generated.resources.off
import alttpr.shared.generated.resources.quarter
import org.jetbrains.compose.resources.StringResource

enum class HeartSpeed(val title: StringResource, val value: Byte) {
    OFF(
        title = Res.string.off,
        value=0x00.toByte()
    ),
    DOUBLE(
        title = Res.string.double,
        value=0x10.toByte()
    ),
    NORMAL(
        title = Res.string.normal,
        value=0x20.toByte()
    ),
    HALF(
        title = Res.string.half,
        value=0x40.toByte()
    ),
    QUARTER(
        title = Res.string.quarter,
        value=0x80.toByte()
    )
}