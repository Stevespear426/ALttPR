package com.stingers.alttpr.screens.generator

import com.stingers.alttpr.navigation.Screen

sealed interface GeneratorEvent {
    object GenerateRandom : GeneratorEvent
    data class NavigateTo(val value: Screen) : GeneratorEvent
}
