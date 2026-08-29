package com.stingers.alttpr.repository.palette

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PaletteEditorTest {

    @Test
    fun testRawWriteThenReadRoundTripsWithinQuantizationError() {
        val rom = ByteArray(16)
        val color = colorF(1.0, 0.0, 0.5)

        PaletteEditor.writeColor(rom, 4, color)
        // r=0x1F (bits 0-4), g=0x00 (bits 5-9), b=0x10 (bits 10-14) -> packed 0x401F.
        assertEquals(0x1F, rom[4].toInt() and 0xFF)
        assertEquals(0x40, rom[5].toInt() and 0xFF)

        // Round-tripping through the 5-bit-per-channel SNES format is lossy: read back the exact
        // quantized values (r=0x1F, g=0x00, b=0x10, each shifted left 3 and normalized) rather
        // than the original inputs.
        val decoded = PaletteEditor.readColor(rom, 4)
        assertEquals((0x1F shl 3) / 255.0, decoded.r, 1e-12)
        assertEquals(0.0, decoded.g, 1e-12)
        assertEquals((0x10 shl 3) / 255.0, decoded.b, 1e-12)
    }

    @Test
    fun testOamWriteUsesSparseByteLayout() {
        val rom = ByteArray(9)
        val color = colorF(1.0, 1.0, 1.0)

        // Offset -4 addresses OAM bytes starting at index 4.
        PaletteEditor.writeColor(rom, -4, color)

        assertEquals(0x20 or 0x1F, rom[4].toInt() and 0xFF) // r
        assertEquals(0x40 or 0x1F, rom[5].toInt() and 0xFF) // g
        assertEquals(0, rom[6].toInt() and 0xFF) // untouched (no byte written at offset+2)
        assertEquals(0x40 or 0x1F, rom[7].toInt() and 0xFF) // g, written a second time
        assertEquals(0x80 or 0x1F, rom[8].toInt() and 0xFF) // b
    }

    @Test
    fun testOamReadDecodesSparseByteLayout() {
        val rom = ByteArray(9)
        rom[4] = 0x1F // r, at offset+0
        rom[5] = 0x0A // g, at offset+1
        rom[8] = 0x05 // b, at offset+4 (offset+2/+3 are not read)

        val color = PaletteEditor.readColor(rom, -4)
        assertEquals((0x1F shl 3) / 255.0, color.r, 1e-12)
        assertEquals((0x0A shl 3) / 255.0, color.g, 1e-12)
        assertEquals((0x05 shl 3) / 255.0, color.b, 1e-12)
    }

    @Test
    fun testIsInBoundsForRawAndOamOffsets() {
        val rom = ByteArray(10)
        assertTrue(PaletteEditor.isInBounds(rom, 8))
        assertFalse(PaletteEditor.isInBounds(rom, 9))
        assertTrue(PaletteEditor.isInBounds(rom, -5)) // needs offset 5..9
        assertFalse(PaletteEditor.isInBounds(rom, -6)) // needs offset 6..10, out of range
    }
}
