package com.stingers.alttpr.common.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PageHeader(title: StringResource) {
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
    )
}