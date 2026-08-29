package com.stingers.alttpr.repository.palette

/**
 * Reads/writes one SNES palette color from a ROM byte array, ported from `@maseya/z3pr`'s
 * `palette_editor.js`. A non-negative offset addresses a direct little-endian BGR555 palette
 * entry; a negative offset addresses an OAM sprite palette slot with a different byte layout
 * (`-offset` is the real position).
 */
internal object PaletteEditor {

    fun readColor(rom: ByteArray, offset: Int): ColorF =
        if (offset >= 0) readRaw(rom, offset) else readOam(rom, -offset)

    fun writeColor(rom: ByteArray, offset: Int, color: ColorF) {
        if (offset >= 0) writeRaw(rom, offset, color) else writeOam(rom, -offset, color)
    }

    fun isInBounds(rom: ByteArray, offset: Int): Boolean =
        if (offset >= 0) offset + 1 < rom.size else -offset + 4 < rom.size

    private fun readRaw(rom: ByteArray, offset: Int): ColorF {
        val value = leWord(rom, offset)
        val r = value and 0x1F
        val g = (value shr 5) and 0x1F
        val b = (value shr 10) and 0x1F
        return colorF((r shl 3) / 255.0, (g shl 3) / 255.0, (b shl 3) / 255.0)
    }

    private fun readOam(rom: ByteArray, offset: Int): ColorF {
        val r = rom[offset + 0].toInt() and 0x1F
        val g = rom[offset + 1].toInt() and 0x1F
        val b = rom[offset + 4].toInt() and 0x1F
        return colorF((r shl 3) / 255.0, (g shl 3) / 255.0, (b shl 3) / 255.0)
    }

    private fun writeRaw(rom: ByteArray, offset: Int, color: ColorF) {
        val (r, g, b) = snes5BitChannels(color)
        val value = r or (g shl 5) or (b shl 10)
        rom[offset] = (value and 0xFF).toByte()
        rom[offset + 1] = ((value shr 8) and 0xFF).toByte()
    }

    private fun writeOam(rom: ByteArray, offset: Int, color: ColorF) {
        val (r, g, b) = snes5BitChannels(color)
        rom[offset + 0] = (0x20 or r).toByte()
        rom[offset + 1] = (0x40 or g).toByte()
        rom[offset + 3] = (0x40 or g).toByte()
        rom[offset + 4] = (0x80 or b).toByte()
    }

    private fun snes5BitChannels(color: ColorF): Triple<Int, Int, Int> {
        fun channel(x: Double): Int {
            val eightBit = (x * 255 + 0.5).toInt()
            // +4 for 8-boundary rounding, then clamp before dropping to 5 bits.
            return (eightBit + 4).coerceAtMost(255) shr 3
        }
        return Triple(channel(color.r), channel(color.g), channel(color.b))
    }

    private fun leWord(rom: ByteArray, offset: Int): Int =
        (rom[offset].toInt() and 0xFF) or ((rom[offset + 1].toInt() and 0xFF) shl 8)
}
