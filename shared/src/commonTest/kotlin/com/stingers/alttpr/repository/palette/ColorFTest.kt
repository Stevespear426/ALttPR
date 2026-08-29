package com.stingers.alttpr.repository.palette

import kotlin.test.Test
import kotlin.test.assertEquals

class ColorFTest {

    @Test
    fun testPureRed() {
        val red = colorF(1.0, 0.0, 0.0)
        assertEquals(0.0, red.hue())
        assertEquals(1.0, red.chroma())
        assertEquals(0.299, red.luma(), 1e-12)
        assertEquals(0.5, red.lightness())
        assertEquals(1.0, red.saturation())
    }

    @Test
    fun testPureGreenAndBlueHues() {
        val green = colorF(0.0, 1.0, 0.0)
        val blue = colorF(0.0, 0.0, 1.0)
        assertEquals(1.0 / 3.0, green.hue(), 1e-12)
        assertEquals(2.0 / 3.0, blue.hue(), 1e-12)
    }

    @Test
    fun testWhiteAndBlackHaveNoChromaOrSaturation() {
        val white = colorF(1.0, 1.0, 1.0)
        val black = colorF(0.0, 0.0, 0.0)

        assertEquals(0.0, white.chroma())
        assertEquals(0.0, white.saturation())
        assertEquals(1.0, white.luma(), 1e-12)
        assertEquals(1.0, white.lightness())

        assertEquals(0.0, black.chroma())
        assertEquals(0.0, black.saturation())
        assertEquals(0.0, black.luma())
        assertEquals(0.0, black.lightness())
    }

    @Test
    fun testGrayscaleDropsChromaButKeepsLuma() {
        val color = colorF(0.8, 0.3, 0.1)
        val gray = color.grayscale()
        assertEquals(0.0, gray.chroma(), 1e-12)
        assertEquals(color.luma(), gray.luma(), 1e-9)
    }

    @Test
    fun testInvertRoundTrips() {
        val color = colorF(0.2, 0.6, 0.9)
        val inverted = color.invert()
        assertEquals(1 - color.r, inverted.r, 1e-12)
        assertEquals(1 - color.g, inverted.g, 1e-12)
        assertEquals(1 - color.b, inverted.b, 1e-12)
        val roundTripped = inverted.invert()
        assertEquals(color.r, roundTripped.r, 1e-12)
        assertEquals(color.g, roundTripped.g, 1e-12)
        assertEquals(color.b, roundTripped.b, 1e-12)
    }

    @Test
    fun testFromHcyRoundTripsHueChromaLuma() {
        val hue = 0.4
        val chroma = 0.6
        val luma = 0.5
        val color = ColorF.fromHcy(hue, chroma, luma)
        assertEquals(hue, color.hue(), 1e-9)
        assertEquals(chroma, color.chroma(), 1e-9)
        assertEquals(luma, color.luma(), 1e-9)
    }

    @Test
    fun testClampsOutOfRangeChannels() {
        val color = colorF(1.5, -0.5, 0.5)
        assertEquals(1.0, color.r)
        assertEquals(0.0, color.g)
        assertEquals(0.5, color.b)
    }

    // Vectors cross-checked against a Python re-implementation of color_f.js's hue_blend/
    // luma_blend, fed the same base/blend pair used in PaletteBlendsTest.
    @Test
    fun testHueBlendTakesHueFromSecondArgAndRestFromFirst() {
        val base = colorF(0.5, 0.2, 0.8)
        val blend = Sfc32Random(999).nextColor()

        val result = ColorF.hueBlend(base, blend)

        assertEquals(0.0, result.r, 1e-9)
        assertEquals(0.6000000000000001, result.g, 1e-9)
        assertEquals(0.42553631801897135, result.b, 1e-9)
    }

    @Test
    fun testLumaBlendTakesLumaFromSecondArgAndRestFromFirst() {
        val blend = Sfc32Random(999).nextColor()
        val base = colorF(0.5, 0.2, 0.8)

        // "sick" mode calls this as lumaBlend(blend, base): hue/chroma from blend, luma from base.
        val result = ColorF.lumaBlend(blend, base)

        assertEquals(0.014658283583623921, result.r, 1e-9)
        assertEquals(0.5289066015094068, result.g, 1e-9)
        assertEquals(0.3793771765129356, result.b, 1e-9)
    }
}
