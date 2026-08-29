package com.stingers.alttpr.repository.palette

import kotlin.test.Test
import kotlin.test.assertEquals

class PaletteBlendsTest {

    // Vector cross-checked against a Python re-implementation of blends.js's maseya_blend,
    // fed the exact ColorF that Sfc32Random(999).nextColor() produces (see Sfc32RandomTest).
    @Test
    fun testMaseyaBlendMatchesReferenceVector() {
        val base = colorF(0.5, 0.2, 0.8)
        val blend = Sfc32Random(999).nextColor()

        val result = maseyaBlend(base, blend)

        assertEquals(0.5652451818328207, result.r, 1e-9)
        assertEquals(0.13965354282732542, result.g, 1e-9)
        assertEquals(0.7425406588233974, result.b, 1e-9)
    }

    // Vector cross-checked against a Python re-implementation of blends.js's classic_blend, fed
    // the same base/blend pair as testMaseyaBlendMatchesReferenceVector.
    @Test
    fun testClassicBlendMatchesReferenceVector() {
        val base = colorF(0.5, 0.2, 0.8)
        val blend = Sfc32Random(999).nextColor()

        val result = classicBlend(base, blend)

        assertEquals(0.5366760889084286, result.r, 1e-9)
        assertEquals(0.6255667850723075, result.g, 1e-9)
        assertEquals(0.20071429186578144, result.b, 1e-9)
    }
}
