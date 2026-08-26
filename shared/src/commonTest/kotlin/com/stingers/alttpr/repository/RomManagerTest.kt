package com.stingers.alttpr.repository

import dev.mokkery.mock
import kotlin.test.Test
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
}