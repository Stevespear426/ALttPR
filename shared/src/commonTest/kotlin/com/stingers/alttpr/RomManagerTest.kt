package com.stingers.alttpr

import com.stingers.alttpr.domain.RomManager
import kotlin.test.Test
import kotlin.test.assertFalse

class RomManagerTest {

    @Test
    fun testVerifyRomBytesInvalid() {
        val dummyBytes = byteArrayOf(1, 2, 3, 4, 5)
        assertFalse(RomManager.verifyRomBytes(dummyBytes))
    }

    @Test
    fun testVerifyRomBytesWithCorrectCrc() {
        val emptyBytes = ByteArray(0)
        assertFalse(RomManager.verifyRomBytes(emptyBytes))
    }
}
