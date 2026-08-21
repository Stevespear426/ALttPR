package com.stingers.alttpr.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp


const val SPRITES_URL = "https://alttpr.com/sprites"


const val ROM_FILE_EXTENSION = "sfc"

const val ROM_FILE_EXTENSION_DOT = ".${ROM_FILE_EXTENSION}"

const val BASE_ROM_FILENAME = "alttp_base$ROM_FILE_EXTENSION_DOT"


const val PREFERENCE_PADDING = 16

val SETTINGS_PREFERENCE_PADDING = PaddingValues(horizontal = PREFERENCE_PADDING.dp)

val MENU_PADDING_VALUES = PaddingValues(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 8.dp)

const val STACKTRACE_LENGTH = 1000