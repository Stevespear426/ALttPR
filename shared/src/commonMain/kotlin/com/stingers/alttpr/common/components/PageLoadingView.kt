package com.stingers.alttpr.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

@Composable
fun PageLoadingView() {
    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .weight(2f), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                Modifier
                    .size(125.dp)
                    .scale(scaleX = -1f, scaleY = 1f)
                    .rotate(180f)
            )
            CircularProgressIndicator(
                Modifier
                    .size(100.dp)
                    .rotate(90f), color = MaterialTheme.colorScheme.secondary
            )
            CircularProgressIndicator(
                Modifier
                    .size(75.dp)
                    .scale(scaleX = -1f, scaleY = 1f)
            )
            CircularProgressIndicator(
                Modifier
                    .size(50.dp)
                    .rotate(-90f), color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(Modifier.weight(1f))
    }
}