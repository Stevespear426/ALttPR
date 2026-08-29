package com.stingers.alttpr.repository.random

import kotlin.test.Test
import kotlin.test.assertEquals

class PrandoTest {

    // Vector taken from the `prando` npm package's own doc comment for Prando.reset().
    @Test
    fun testNumericSeedMatchesDocumentedVector() {
        val rng = Prando.fromSeed(12345678)
        assertEquals(0.6177754114889017, rng.next())
        assertEquals(0.5784605181725837, rng.next())
        rng.reset()
        assertEquals(0.6177754114889017, rng.next())
    }

    @Test
    fun testNumericSeedNextIntFullRange() {
        val rng = Prando.fromSeed(12345678)
        assertEquals(2653325188L, rng.nextInt(0, 4294967295))
    }

    @Test
    fun testStringSeedHashing() {
        val rng = Prando.fromSeed("abc123")
        assertEquals(0.6322481552214008, rng.next())
    }

    @Test
    fun testStringSeedNextIntFullRange() {
        val rng = Prando.fromSeed("abc123")
        assertEquals(2715485149L, rng.nextInt(0, 4294967295))
    }

    @Test
    fun testNextIntSequenceWithShrinkingRange() {
        val rng = Prando.fromSeed("abc123")
        val sequence = (4 downTo 0).map { rng.nextInt(0, it.toLong()) }
        assertEquals(listOf(3L, 3L, 2L, 1L, 0L), sequence)
    }

    @Test
    fun testZeroSeedIsCoercedToOne() {
        // getSafeSeed(0) => 1, matching the JS implementation.
        assertEquals(Prando.fromSeed(1).next(), Prando.fromSeed(0).next())
    }
}
