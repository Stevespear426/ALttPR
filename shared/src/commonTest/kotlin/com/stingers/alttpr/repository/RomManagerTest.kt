package com.stingers.alttpr.repository

import dev.mokkery.mock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class RomManagerTest {

    private val mockAlttpRespository = mock<AlttprRepository>()

    val instance: RomManager = RomManager(mockAlttpRespository)

    @Test
    fun testVerifyRomBytesInvalid() {
        val dummyBytes = byteArrayOf(1, 2, 3, 4, 5)
        assertFalse(instance.verifyRomBytes(dummyBytes))
    }

    @Test
    fun testVerifyRomBytesWithCorrectCrc() {
        val emptyBytes = ByteArray(0)
        assertFalse(instance.verifyRomBytes(emptyBytes))
    }

    @Test
    fun testFormatSpriteAuthorEvenLengthCentersExactly() {
        val formatted = instance.formatSpriteAuthor("abcd")
        assertEquals(28, formatted.length)
        assertEquals(" ".repeat(12) + "ABCD" + " ".repeat(12), formatted)
    }

    @Test
    fun testFormatSpriteAuthorOddLengthPadsToTargetWidth() {
        val formatted = instance.formatSpriteAuthor("abc")
        assertEquals(28, formatted.length)
        assertEquals(" ".repeat(12) + "ABC" + " ".repeat(13), formatted)
    }

    @Test
    fun testFormatSpriteAuthorLongNameIsTruncated() {
        val formatted = instance.formatSpriteAuthor("abcdefghijklmnopqrstuvwxyz1234")
        assertEquals(28, formatted.length)
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ12", formatted)
    }

    @Test
    fun testInjectSpriteAuthorSkipsWriteWhenRomLacksAuthorSupport() {
        val rom = ByteArray(0x120000)
        instance.injectSpriteAuthor(rom, "TEST")
        assertEquals(0, rom[0x118002])
        assertEquals(0, rom[0x118020])
    }

    @Test
    fun testInjectSpriteAuthorWritesEncodedNameWhenRomSupportsIt() {
        val rom = ByteArray(0x120000)
        rom[0x118000] = 0x02
        rom[0x118001] = 0x37
        rom[0x11801E] = 0x02
        rom[0x11801F] = 0x37

        instance.injectSpriteAuthor(rom, "AB")

        // "AB" centered in 28 chars lands at indices 13-14, everywhere else is space (0x9F/0x9F).
        assertEquals(0x9F.toByte(), rom[0x118002])
        assertEquals(0x9F.toByte(), rom[0x118020])
        assertEquals(0x5D.toByte(), rom[0x118002 + 13]) // 'A' menu font tile
        assertEquals(0x83.toByte(), rom[0x118020 + 13]) // 'A' file-select font tile
        assertEquals(0x5E.toByte(), rom[0x118002 + 14]) // 'B' menu font tile
        assertEquals(0x84.toByte(), rom[0x118020 + 14]) // 'B' file-select font tile
    }
}