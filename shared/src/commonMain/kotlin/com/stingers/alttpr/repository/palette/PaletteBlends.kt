package com.stingers.alttpr.repository.palette

import kotlin.math.max
import kotlin.math.sqrt

/** Ported from `@maseya/z3pr`'s `blends.js`. */
internal fun maseyaBlend(base: ColorF, blend: ColorF): ColorF {
    val hue = (blend.r * 0.95) + 0.025 + base.hue()

    val chromaShift = blend.g - 0.5
    val baseChroma = base.chroma()
    var chroma = baseChroma
    chroma *= if (chromaShift > 0) {
        // Put heavy limitations on oversaturating colors.
        1 + ((1 - baseChroma) * chromaShift * 0.5)
    } else {
        // No limitation on desaturating, but bias towards only a little desaturation.
        val shiftDoubled = chromaShift * 2
        sqrt(1 - (shiftDoubled * shiftDoubled))
    }

    val lumaShift = blend.b - 0.5
    val baseLuma = base.luma()
    var luma = baseLuma
    luma *= if (lumaShift > 0) {
        // Do not heavily brighten, unless a lot of saturation was removed.
        val chromaDiff = max(baseChroma - chroma, 0.0)
        1 + ((1 - baseLuma) * lumaShift * (1 + chromaDiff))
    } else {
        // Do not let colors get too dark.
        1 + (lumaShift / 2)
    }

    return ColorF.fromHcy(hue, chroma, luma)
}

/** "Change a color value but allow it to occasionally look ugly" — ported from `blends.js`. */
internal fun classicBlend(base: ColorF, blend: ColorF): ColorF {
    // Restrict the blend channels to [60/255, 240/255] to avoid degenerate white/black results.
    fun constrict(value: Double): Double = (value * (240.0 - 60.0) / 255.0) + (60.0 / 255.0)
    val constrictedBlend = colorF(constrict(blend.r), constrict(blend.g), constrict(blend.b))

    return ColorF.fromHsl(
        base.hue() + constrictedBlend.hue(),
        (base.saturation() + constrictedBlend.saturation()) / 2,
        base.lightness() * (1.25 - constrictedBlend.lightness()),
    )
}
