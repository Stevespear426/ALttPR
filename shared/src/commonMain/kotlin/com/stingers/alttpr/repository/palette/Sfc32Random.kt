package com.stingers.alttpr.repository.palette

/**
 * Ported from `@maseya/z3pr`'s `random/sfc32.js`, seeded with a single 32-bit int (the only call
 * pattern the library's own `random()` wrapper uses when given one seed value, which is always
 * the case here). Kotlin `Int` arithmetic wraps at 32 bits the same way JS's bitwise-coerced
 * arithmetic does, so the recurrence translates directly; `ushr` matches JS's `>>>`.
 */
internal class Sfc32Random(seed: Int) {
    private var a: Int = 0
    private var b: Int = seed
    private var c: Int = 0
    private var n: Int = 1

    init {
        repeat(12) { next32() }
    }

    fun next32(): Int {
        val t = a + b + n
        a = b xor (b ushr 9)
        b = c + (c shl 3)
        c = ((c shl 21) or (c ushr 11)) + t
        n += 1
        return t
    }

    /** Uniform double in [0,1], matching JS's `next32() / 0xFFFFFFFF` (unsigned interpretation). */
    fun nextDouble(): Double {
        val unsignedBits = next32().toLong() and 0xFFFFFFFFL
        return unsignedBits.toDouble() / 4294967295.0
    }

    /** Order matches JS's `color_f(rnd(), rnd(), rnd())` — r, then g, then b. */
    fun nextColor(): ColorF = colorF(nextDouble(), nextDouble(), nextDouble())
}
