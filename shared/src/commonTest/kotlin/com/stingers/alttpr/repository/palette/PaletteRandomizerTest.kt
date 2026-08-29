package com.stingers.alttpr.repository.palette

import com.stingers.alttpr.model.api.PaletteAlgorithm
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PaletteRandomizerTest {

    private fun freshRom(): ByteArray = ByteArray(0x200000)

    // maseyaBlend(black, anything) is always black (chroma/luma both scale a zero base), so an
    // all-zero ROM can't distinguish hashes. Fill it with varied, non-degenerate palette data.
    private fun nonBlackRom(): ByteArray = ByteArray(0x200000) { (it * 37 + 11).toByte() }

    @Test
    fun testShufflePaletteIsDeterministicForSameHash() {
        val romA = freshRom()
        val romB = freshRom()

        PaletteRandomizer.shufflePalette(romA, "somehash", PaletteAlgorithm.Maseya)
        PaletteRandomizer.shufflePalette(romB, "somehash", PaletteAlgorithm.Maseya)

        assertContentEquals(romA, romB)
    }

    @Test
    fun testShufflePaletteChangesDifferentHashesDifferently() {
        val romA = nonBlackRom()
        val romB = nonBlackRom()

        PaletteRandomizer.shufflePalette(romA, "hashOne", PaletteAlgorithm.Maseya)
        PaletteRandomizer.shufflePalette(romB, "hashTwo", PaletteAlgorithm.Maseya)

        assertFalse(romA.contentEquals(romB))
    }

    @Test
    fun testShufflePaletteActuallyWritesBytes() {
        val rom = freshRom()
        PaletteRandomizer.shufflePalette(rom, "somehash", PaletteAlgorithm.Maseya)
        assertTrue(rom.any { it != 0.toByte() })
    }

    @Test
    fun testShufflePaletteDoesNotThrowOnUndersizedRom() {
        // Every offset is out of bounds for a tiny ROM; PaletteEditor.isInBounds should skip
        // every write rather than throwing.
        val rom = ByteArray(4)
        for (mode in PaletteAlgorithm.entries) {
            PaletteRandomizer.shufflePalette(rom, "somehash", mode)
        }
        assertTrue(rom.all { it == 0.toByte() })
    }

    @Test
    fun testShufflePaletteIsDeterministicForEveryAlgorithm() {
        for (mode in PaletteAlgorithm.entries) {
            val romA = nonBlackRom()
            val romB = nonBlackRom()

            PaletteRandomizer.shufflePalette(romA, "somehash", mode)
            PaletteRandomizer.shufflePalette(romB, "somehash", mode)

            assertContentEquals(romA, romB, "mode=$mode")
        }
    }

    @Test
    fun testRandomResolvesToExactlyOneOfTheOtherAlgorithms() {
        val concreteAlgorithms = PaletteAlgorithm.entries.filter { it != PaletteAlgorithm.Random }

        for (hash in listOf("hashOne", "hashTwo", "hashThree", "somehash", "abc123")) {
            val randomRom = nonBlackRom()
            PaletteRandomizer.shufflePalette(randomRom, hash, PaletteAlgorithm.Random)

            val matches = concreteAlgorithms.filter { mode ->
                val direct = nonBlackRom()
                PaletteRandomizer.shufflePalette(direct, hash, mode)
                direct.contentEquals(randomRom)
            }

            assertTrue(matches.size == 1, "hash=$hash matched ${matches.size} algorithms: $matches")
        }
    }

    @Test
    fun testBlackoutWritesBlackToEveryRawPaletteOffset() {
        val rom = nonBlackRom()
        PaletteRandomizer.shufflePalette(rom, "somehash", PaletteAlgorithm.Blackout)

        for (group in DUNGEON_PALETTE_OFFSETS + OVERWORLD_PALETTE_OFFSETS) {
            for (offset in group) {
                if (offset < 0 || !PaletteEditor.isInBounds(rom, offset)) continue
                val color = PaletteEditor.readColor(rom, offset)
                assertEqualsColor(ColorF.BLACK, color)
            }
        }
    }

    @Test
    fun testGrayscaleDropsChromaOnEveryRawPaletteOffset() {
        val rom = nonBlackRom()
        PaletteRandomizer.shufflePalette(rom, "somehash", PaletteAlgorithm.Grayscale)

        for (group in DUNGEON_PALETTE_OFFSETS + OVERWORLD_PALETTE_OFFSETS) {
            for (offset in group) {
                if (offset < 0 || !PaletteEditor.isInBounds(rom, offset)) continue
                val color = PaletteEditor.readColor(rom, offset)
                assertTrue(color.chroma() < 1e-6, "expected zero chroma at offset $offset, got $color")
            }
        }
    }

    @Test
    fun testNegativeInvertsEveryRawPaletteOffset() {
        val original = nonBlackRom()
        val rom = original.copyOf()
        PaletteRandomizer.shufflePalette(rom, "somehash", PaletteAlgorithm.Negative)

        // Spot-check a handful of raw offsets from the real tables against a manual invert.
        val sampleOffsets = (DUNGEON_PALETTE_OFFSETS + OVERWORLD_PALETTE_OFFSETS)
            .flatten()
            .filter { it >= 0 && PaletteEditor.isInBounds(original, it) }
            .take(20)

        for (offset in sampleOffsets) {
            val before = PaletteEditor.readColor(original, offset)
            val after = PaletteEditor.readColor(rom, offset)
            assertEqualsColor(before.invert(), after, offset)
        }
    }

    private fun assertEqualsColor(expected: ColorF, actual: ColorF, context: Any? = null) {
        // Channels are 5-bit (32 levels, ~8/255 apart); reading the original, computing the
        // expected value, and re-reading after a write can each round to an adjacent level, so
        // allow slack for two compounded quantization steps.
        val delta = 2 * (8.0 / 255) + 1e-9
        kotlin.test.assertEquals(expected.r, actual.r, delta, "r mismatch ($context)")
        kotlin.test.assertEquals(expected.g, actual.g, delta, "g mismatch ($context)")
        kotlin.test.assertEquals(expected.b, actual.b, delta, "b mismatch ($context)")
    }
}
