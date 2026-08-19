package com.stingers.alttpr.screens.randomizer

import com.stingers.alttpr.model.RandomizerGameMode


sealed interface RandomizerEvent {
    data class GenerateGame(val value: RandomizerGameMode) : RandomizerEvent
}
