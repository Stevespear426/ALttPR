package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.language_english
import alttpr.shared.generated.resources.language_francais
import alttpr.shared.generated.resources.language_deutsch
import alttpr.shared.generated.resources.language_espanol
import org.jetbrains.compose.resources.StringResource

enum class Language(val title: StringResource, val value: String) {
    English(Res.string.language_english, "en"),
    Francais(Res.string.language_francais, "fr"),
    Deutsch(Res.string.language_deutsch, "de"),
    Espanol(Res.string.language_espanol, "es"),
}
