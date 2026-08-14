package com.stingers.alttpr.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily

private val defaultTypography = Typography()

fun zeldaTypography(fontFamily: FontFamily): Typography {
    return Typography(
        // size: 57.sp weight: W400
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = fontFamily),
        // size: 45.sp weight: W400
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = fontFamily),
        // size: 36.sp weight: W400
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = fontFamily),
        // size: 32.sp weight: W400
        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = fontFamily),
        // size: 28.sp weight: W400
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = fontFamily),
        // size: 24.sp weight: W400
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = fontFamily),
        // size: 22.sp weight: W400
        titleLarge = defaultTypography.titleLarge.copy(fontFamily = fontFamily),
        // size: 16.sp weight: W500
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = fontFamily),
        // size: 14.sp weight: W500
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = fontFamily),
        // size: 16.sp weight: W400
        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = fontFamily),
        // size: 14.sp weight: W400
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = fontFamily),
        // size: 12.sp weight: W400
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = fontFamily),
        // size: 14.sp weight: W500
        labelLarge = defaultTypography.labelLarge.copy(fontFamily = fontFamily),
        // size: 12.sp weight: W500
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = fontFamily),
        // size: 11.sp weight: W500
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = fontFamily),
    )
}