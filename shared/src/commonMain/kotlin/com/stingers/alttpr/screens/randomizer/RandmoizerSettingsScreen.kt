package com.stingers.alttpr.screens.randomizer

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RandomizerSettingsScreen(
    settings: List<@Composable () -> Unit>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = spacedBy(16.dp),
    ) {
        settings.forEach { it() }
    }
}
