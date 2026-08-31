package com.stingers.alttpr.repository

import com.stingers.alttpr.model.BasePatchInfoResponse
import com.stingers.alttpr.model.GameModel
import com.stingers.alttpr.model.DailyResponse
import com.stingers.alttpr.model.SeedDetailsResponse
import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.model.api.GenerateSeedResponse
import com.stingers.alttpr.repository.local.SeedDao
import com.stingers.alttpr.repository.local.SpriteDao
import com.stingers.alttpr.repository.remote.AlttprService
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AlttprRepositoryTest {

    private val alttprService = mock<AlttprService>()
    private val seedDao = mock<SeedDao>()
    private val spriteDao = mock<SpriteDao>()

    private val repository = AlttprRepository(
        alttprService = alttprService,
        seedDao = seedDao,
        spriteDao = spriteDao
    )

    @Test
    fun `test get downloaded sprites`() = runTest {
        val sprites = listOf(Sprite(fileUrl = "url1", name = "Sprite 1"))
        everySuspend { spriteDao.getDownloadedSprites() } returns sprites

        val result = repository.getSprites()

        assertTrue(result.isSuccess)
        assertEquals(sprites, result.getOrNull())
    }

    @Test
    fun `test create daily success`() = runTest {
        val dailyResponse = DailyResponse(hash = "dailyhash")
        val seedDetails = SeedDetailsResponse(
            hash = "dailyhash",
            logic = "no-logic",
            patch = listOf(mapOf("1000" to listOf(1, 2)))
        )

        everySuspend { seedDao.getSeed("dailyhash") } returns null
        everySuspend { alttprService.getDaily() } returns dailyResponse
        everySuspend { alttprService.getSeedPatch("dailyhash") } returns seedDetails

        val result = repository.createDailySeed()

        assertEquals("dailyhash", result.hash)
        assertEquals("no-logic", result.logic)
        verifySuspend { alttprService.getDaily() }
        verifySuspend { alttprService.getSeedPatch("dailyhash") }
    }

    @Test
    fun `test generate customizer seed success`() = runTest {
        val model = GameModel()
        val response = GenerateSeedResponse(hash = "custhash")
        val seedDetails = SeedDetailsResponse(
            hash = "custhash",
            logic = "no-logic",
            patch = listOf(mapOf("1000" to listOf(1, 2)))
        )

        everySuspend { alttprService.generateCustomizerSeed(any()) } returns response
        everySuspend { seedDao.getSeed("custhash") } returns null
        everySuspend { alttprService.getSeedPatch("custhash") } returns seedDetails

        val result = repository.generateCustomizerSeed(model)

        assertEquals("custhash", result.hash)
        assertEquals("no-logic", result.logic)
        verifySuspend { alttprService.generateCustomizerSeed(any()) }
        verifySuspend { alttprService.getSeedPatch("custhash") }
    }

    @Test
    fun `test get bps patch success`() = runTest {
        val basePatchInfo = BasePatchInfoResponse(
            hash = "hash123",
            bpsLocation = "/patch.bps",
            md5 = "md5sum"
        )
        val patchBytes = byteArrayOf(0x01, 0x02, 0x03)

        everySuspend { alttprService.getBasePatchInfo("hash123") } returns basePatchInfo
        everySuspend { alttprService.getBpsPatch("/patch.bps") } returns patchBytes

        val (md5, bytes) = repository.getBpsPatch("hash123")

        assertEquals("md5sum", md5)
        assertEquals(patchBytes.size, bytes.size)
        verifySuspend { alttprService.getBasePatchInfo("hash123") }
        verifySuspend { alttprService.getBpsPatch("/patch.bps") }
    }

    @Test
    fun `test get bps patch empty`() = runTest {
        val basePatchInfo = BasePatchInfoResponse(
            hash = "hash123",
            bpsLocation = "/patch.bps",
            md5 = "md5sum"
        )

        everySuspend { alttprService.getBasePatchInfo("hash123") } returns basePatchInfo
        everySuspend { alttprService.getBpsPatch("/patch.bps") } returns byteArrayOf()

        assertFailsWith<IllegalStateException> {
            repository.getBpsPatch("hash123")
        }
    }
}
