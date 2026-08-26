package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.repository.local.AppPrefs
import com.stingers.alttpr.repository.local.LoggerDao
import com.stingers.alttpr.repository.local.RomPrefs
import com.stingers.alttpr.repository.local.SpriteDao
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSavedSpriteUseCaseTest {

    private val appPrefs = mock<AppPrefs>().apply {
        every { debugMode } returns flowOf(false)
    }
    private val loggerDao = mock<LoggerDao>()
    private val logger = Logger(appPrefs, loggerDao)
    private val romPrefs = mock<RomPrefs>()
    private val spriteDao = mock<SpriteDao>()

    private val useCase = GetSavedSpriteUseCase(
        logger = logger,
        romPrefs = romPrefs,
        spriteDao = spriteDao
    )

    @Test
    fun `test get saved sprite when sprite name is null`() = runTest {
        every { romPrefs.sprite } returns flowOf(null)

        val emissions = useCase().toList()

        assertEquals(listOf(null), emissions)
    }

    @Test
    fun `test get saved sprite when sprite name is present`() = runTest {
        val sprite = Sprite(fileUrl = "url", name = "Link")
        every { romPrefs.sprite } returns flowOf("Link")
        every { spriteDao.getSprite("Link") } returns flowOf(sprite)

        val emissions = useCase().toList()

        assertEquals(listOf(sprite), emissions)
    }
}
