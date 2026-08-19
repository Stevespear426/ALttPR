package com.stingers.alttpr.screens.main

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.generator
import alttpr.shared.generated.resources.ic_generator
import alttpr.shared.generated.resources.ic_library
import alttpr.shared.generated.resources.ic_settings
import alttpr.shared.generated.resources.library
import alttpr.shared.generated.resources.settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomBar(pageIndex: Int, setCurrentPage: (index: Int) -> Unit) {
    NavigationBar {
        val colors =
            NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.secondary)
        NavigationBarItem(
            colors = colors,
            selected = pageIndex == 0,
            onClick = {
                setCurrentPage(0)
            },
            icon = {
                Icon(
                    painterResource(Res.drawable.ic_generator),
                    contentDescription = "dash_page",
                )
            },
            label = {
                Text(stringResource(Res.string.generator))
            }
        )
        NavigationBarItem(
            colors = colors,
            selected = pageIndex == 1,
            onClick = {
                setCurrentPage(1)
            },
            icon = {
                Icon(
                    painterResource(Res.drawable.ic_library),
                    contentDescription = "headlines_page",
                )
            },
            label = {
                Text(stringResource(Res.string.library),)
            }
        )
        NavigationBarItem(
            colors = colors,
            selected = pageIndex == 2,
            onClick = {
                setCurrentPage(2)
            },
            icon = {
                Icon(
                    painterResource(Res.drawable.ic_settings),
                    contentDescription = "following_page",
                )
            },
            label = {
                Text(stringResource(Res.string.settings),)
            }
        )
    }
}