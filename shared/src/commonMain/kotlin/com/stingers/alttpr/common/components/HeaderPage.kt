package com.stingers.alttpr.common.components

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.ic_close
import alttpr.shared.generated.resources.ic_save
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.platform.ic_share
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun HeaderPage(
    title: String,
    topBar: @Composable () -> Unit = {
        val isPreview = LocalInspectionMode.current
        val navigationManager: NavigationManager? = if (!isPreview) koinInject() else null
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                IconButton(
                    onClick = {
                        navigationManager?.pop()
                    }
                ) {
                    Icon(
                        painterResource(Res.drawable.ic_close),
                        contentDescription = "Close Button"
                    )
                }
            },
        )
    },
    content: @Composable (padding: PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
           topBar()
        }
    ) { padding ->
        content(padding)
    }
}

