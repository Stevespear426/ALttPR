package com.stingers.alttpr.repository.random

import kotlin.math.floor

/**
 * Kotlin port of the `prando` npm package (github.com/zeh/prando), used by the upstream web
 * randomizer to deterministically seed cosmetic ROM randomization (palette/SFX shuffle) from a
 * seed hash. Bitwise ops on Kotlin `Int` wrap at 32 bits the same way JS's `|0`-coerced bitwise
 * ops do, so the xorshift core translates directly.
 */
class Prando private constructor(rawSeed: Int) {

    private val seed: Int = if (rawSeed == 0) 1 else rawSeed
    private var value: Int = seed

    fun next(min: Double = 0.0, max: Double = 1.0): Double {
        recalculate()
        return map(value.toDouble(), MIN, MAX, min, max)
    }

    /** Inclusive of both [min] and [max], matching the JS implementation. */
    fun nextInt(min: Long = 10, max: Long = 100): Long {
        recalculate()
        return floor(map(value.toDouble(), MIN, MAX, min.toDouble(), (max + 1).toDouble())).toLong()
    }

    fun reset() {
        value = seed
    }

    private fun recalculate() {
        value = xorshift(value)
    }

    companion object {
        private const val MIN = -2147483648.0
        private const val MAX = 2147483647.0

        fun fromSeed(seed: Int): Prando = Prando(seed)

        fun fromSeed(seed: String): Prando = Prando(hashCode(seed))

        internal fun xorshift(v: Int): Int {
            var value = v
            value = value xor (value shl 13)
            value = value xor (value shr 17)
            value = value xor (value shl 5)
            return value
        }

        internal fun hashCode(str: String): Int {
            var hash = 0
            for (c in str) {
                hash = (hash shl 5) - hash + c.code
                hash = xorshift(hash)
            }
            return hash
        }

        private fun map(v: Double, minFrom: Double, maxFrom: Double, minTo: Double, maxTo: Double): Double {
            return ((v - minFrom) / (maxFrom - minFrom)) * (maxTo - minTo) + minTo
        }
    }
}
