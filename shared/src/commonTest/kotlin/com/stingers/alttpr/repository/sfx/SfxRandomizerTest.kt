package com.stingers.alttpr.repository.sfx

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SfxRandomizerTest {

    private val sfxTable = mapOf(2 to 0x1A8BD0, 3 to 0x1A8CCC)

    private fun snesToPc(value: Int): Int = ((value and 0x7F0000) shr 1) or (value and 0x7FFF)

    private fun freshRom(): ByteArray = ByteArray(0x200000)

    @Test
    fun testShuffleSfxSkippedWhenBuildTooOld() {
        val rom = freshRom()
        SfxRandomizer.shuffleSfx(rom, "somehash", "2021-01-01")
        assertTrue(rom.all { it == 0.toByte() })
    }

    @Test
    fun testShuffleSfxSkippedWhenBuildMissing() {
        val rom = freshRom()
        SfxRandomizer.shuffleSfx(rom, "somehash", null)
        assertTrue(rom.all { it == 0.toByte() })
    }

    @Test
    fun testShuffleSfxIsDeterministicForSameHash() {
        val romA = freshRom()
        val romB = freshRom()
        SfxRandomizer.shuffleSfx(romA, "somehash", "2022-01-01")
        SfxRandomizer.shuffleSfx(romB, "somehash", "2022-01-01")
        assertContentEquals(romA, romB)
    }

    @Test
    fun testShuffleSfxWritesPermutationOfOriginalAddresses() {
        // Chain/accompaniment slots can move a sound to the *other* sfx set's tables (the
        // upstream `candidates.find`/`accompaniment_map[chosen_slot.set]` lookups aren't
        // restricted to the original set), so the only guaranteed invariant is a permutation of
        // the full combined 126-entry addr multiset, not a closed permutation per set.
        val rom = freshRom()
        SfxRandomizer.shuffleSfx(rom, "somehash", "2022-01-01")

        val expectedAddrs = SFX_ENTRIES.map { it.addr }.sorted()
        val writtenAddrs = listOf(2, 3).flatMap { set ->
            val baseAddress = snesToPc(sfxTable.getValue(set))
            (1..63).map { id ->
                val offset = baseAddress + id * 2 - 2
                (rom[offset].toInt() and 0xff) or ((rom[offset + 1].toInt() and 0xff) shl 8)
            }
        }.sorted()
        assertEquals(expectedAddrs, writtenAddrs)
    }
}
