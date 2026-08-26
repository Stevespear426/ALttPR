package com.stingers.alttpr

import alttpr.desktopapp.generated.resources.Res
import alttpr.desktopapp.generated.resources.ic_launcher
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

import org.jetbrains.compose.resources.painterResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ALttPR",
        icon = painterResource(Res.drawable.ic_launcher)
    ) {
        App()
    }
}