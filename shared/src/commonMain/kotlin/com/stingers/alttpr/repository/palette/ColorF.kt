package com.stingers.alttpr.repository.palette

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val LUMA_R = 0.299
private const val LUMA_G = 0.587
private const val LUMA_B = 0.114

/**
 * Immutable floating-point RGB color (each channel clamped to [0,1]), ported from
 * `@maseya/z3pr`'s `color_f.js`. Construct via [colorF] rather than the constructor directly so
 * channels are always clamped, matching the JS constructor's behavior.
 */
@ConsistentCopyVisibility
data class ColorF internal constructor(val r: Double, val g: Double, val b: Double) {

    fun hue(): Double {
        val c = chroma()
        if (c == 0.0) return 0.0

        val maxChannel = max(max(r, g), b)
        val h = when (maxChannel) {
            r -> {
                val v = (g - b) / c
                if (v < 0) v + 6 else v
            }
            g -> ((b - r) / c) + 2
            else -> ((r - g) / c) + 4
        }
        return h / 6
    }

    fun chroma(): Double = max(max(r, g), b) - min(min(r, g), b)

    fun saturation(): Double {
        val l = lightness()
        if (l == 0.0 || l == 1.0) return 0.0
        return (chroma() / (1 - abs(l * 2 - 1))).coerceIn(0.0, 1.0)
    }

    fun luma(): Double = (LUMA_R * r) + (LUMA_G * g) + (LUMA_B * b)

    fun lightness(): Double = (max(max(r, g), b) + min(min(r, g), b)) / 2

    fun grayscale(): ColorF = fromHcy(0.0, 0.0, luma())

    fun invert(): ColorF = colorF(1 - r, 1 - g, 1 - b)

    companion object {
        val BLACK: ColorF = colorF(0.0, 0.0, 0.0)

        fun hueBlend(a: ColorF, b: ColorF): ColorF = fromHcy(b.hue(), a.chroma(), a.luma())

        fun lumaBlend(a: ColorF, b: ColorF): ColorF = fromHcy(a.hue(), a.chroma(), b.luma())

        fun fromHcy(hue: Double, chroma: Double, luma: Double): ColorF {
            val c = chroma.coerceIn(0.0, 1.0)
            val y = luma.coerceIn(0.0, 1.0)
            val base = colorFromHc(hue, c)
            val extra = max(y - base.luma(), 0.0)
            return colorF(extra + base.r, extra + base.g, extra + base.b)
        }

        fun fromHsl(hue: Double, saturation: Double, lightness: Double): ColorF {
            val s = saturation.coerceIn(0.0, 1.0)
            val l = lightness.coerceIn(0.0, 1.0)
            val chroma = (1 - abs(l * 2 - 1)) * s
            val base = colorFromHc(hue, chroma)
            val match = l - (chroma / 2)
            return colorF(match + base.r, match + base.g, match + base.b)
        }

        private fun colorFromHc(hueIn: Double, chroma: Double): ColorF {
            if (chroma == 0.0) return colorF(0.0, 0.0, 0.0)

            var hue = hueIn
            while (hue < 0) hue += 1
            while (hue >= 1) hue -= 1

            hue *= 6
            val x = chroma * (1 - abs((hue % 2) - 1))
            val (cr, cg, cb) = when {
                hue <= 1 -> Triple(chroma, x, 0.0)
                hue <= 2 -> Triple(x, chroma, 0.0)
                hue <= 3 -> Triple(0.0, chroma, x)
                hue <= 4 -> Triple(0.0, x, chroma)
                hue <= 5 -> Triple(x, 0.0, chroma)
                else -> Triple(chroma, 0.0, x)
            }
            return colorF(cr, cg, cb)
        }
    }
}

/** Always clamps each channel to [0,1], matching the `color_f()` JS constructor. */
fun colorF(r: Double, g: Double, b: Double): ColorF =
    ColorF(r.coerceIn(0.0, 1.0), g.coerceIn(0.0, 1.0), b.coerceIn(0.0, 1.0))
