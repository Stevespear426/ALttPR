package com.stingers.alttpr.screens.randomizer


sealed interface RandomizerEvent {
    object GenerateGame : RandomizerEvent
}
