package com.stingers.alttpr.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.stingers.alttpr.di.AppModule
import com.stingers.alttpr.di.module
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration


val DarkColorPalette = darkColorScheme(
    primary = PrimaryDark,
    primaryContainer = PrimaryVariant,
    background = DarkBackground,
)

val LightColorPalette = lightColorScheme(
    primary = PrimaryLight,
    primaryContainer = PrimaryVariant,
    background = Color.White,
)

@Composable
fun ZeldaTheme(content: @Composable () -> Unit) {
    KoinApplication(
        configuration = koinConfiguration(declaration = { modules(AppModule().module()) }),
        content = {
            ZeldaThemeInternal {
                content()
            }
        }
    )
}

@Composable
private fun ZeldaThemeInternal(
    colorScheme: ColorScheme = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = zeldaTypography(FontFamily.SansSerif),
        shapes = Shapes
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

@Composable
fun PreviewDarkTheme(
    content: @Composable () -> Unit
) {
    ZeldaThemeInternal(colorScheme = DarkColorPalette) {
        content()
    }
}

@Composable
fun PreviewLightTheme(
    content: @Composable () -> Unit
) {
    ZeldaThemeInternal(colorScheme = LightColorPalette) {
        content()
    }
}