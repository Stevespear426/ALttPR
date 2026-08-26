package com.stingers.alttpr.repository.usecase

import com.stingers.alttpr.common.Logger
import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.repository.local.RomPrefs
import com.stingers.alttpr.repository.local.SpriteDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

@Factory
open class GetSavedSpriteUseCase(
    private val logger: Logger,
    private val romPrefs: RomPrefs,
    private val spriteDao: SpriteDao
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    open operator fun invoke(): Flow<Sprite?> = romPrefs.sprite.flatMapLatest { name ->
        if (name != null) {
            spriteDao.getSprite(name)
        } else {
            flowOf(null)
        }
    }.catch { e ->
        logger.e(TAG, "Error fetching saved sprite", e)
        emit(null)
    }

    companion object {
        private const val TAG = "GetSavedSpriteUseCase"
    }
}
