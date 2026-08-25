package com.stingers.alttpr.screens.sprites

import com.stingers.alttpr.model.Sprite

sealed interface SpriteEvent {
    data class SelectSprite(val sprite: Sprite) : SpriteEvent
    data class UpdateSearchQuery(val query: String) : SpriteEvent
    object OnBackClick : SpriteEvent
}
