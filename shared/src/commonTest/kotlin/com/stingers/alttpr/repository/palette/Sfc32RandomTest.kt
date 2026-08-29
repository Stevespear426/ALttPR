package com.stingers.alttpr.repository.palette

import kotlin.test.Test
import kotlin.test.assertEquals

class Sfc32RandomTest {

    // Vectors cross-checked against a Python re-implementation of sfc32.js's single-seed path.
    @Test
    fun testNext32SequenceMatchesReferenceVector() {
        val rng = Sfc32Random(12345)
        val sequence = List(5) { rng.next32() }
        assertEquals(listOf(235160590, -1327706133, 116171463, -1412642393, 362604721), sequence)
    }

    @Test
    fun testNextDoubleSequenceMatchesReferenceVector() {
        val rng = Sfc32Random(12345)
        assertEquals(0.05475259154447182, rng.nextDouble(), 1e-12)
        assertEquals(0.6908693266312753, rng.nextDouble(), 1e-12)
        assertEquals(0.027048276510799368, rng.nextDouble(), 1e-12)
    }

    @Test
    fun testNextColorDrawsThreeDoublesInRgbOrder() {
        val rng = Sfc32Random(999)
        val color = rng.nextColor()
        assertEquals(0.009810982041482577, color.r, 1e-12)
        assertEquals(0.5240592999672655, color.g, 1e-12)
        assertEquals(0.37452987497079415, color.b, 1e-12)
    }
}
