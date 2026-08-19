package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.special_char_cursor
import alttpr.shared.generated.resources.special_char_self
import alttpr.shared.generated.resources.special_char_link
import alttpr.shared.generated.resources.special_char_bird
import alttpr.shared.generated.resources.special_char_ankh
import alttpr.shared.generated.resources.special_char_squigly
import alttpr.shared.generated.resources.special_char_heart_1
import alttpr.shared.generated.resources.special_char_heart_2
import alttpr.shared.generated.resources.special_char_heart_3
import alttpr.shared.generated.resources.special_char_heart_4
import alttpr.shared.generated.resources.special_char_var_1
import alttpr.shared.generated.resources.special_char_var_2
import alttpr.shared.generated.resources.special_char_var_3
import alttpr.shared.generated.resources.special_char_var_4
import org.jetbrains.compose.resources.StringResource

enum class SpecialChar(val title: StringResource, val value: String) {
    Cursor(Res.string.special_char_cursor, "≥"),
    Self(Res.string.special_char_self, "@"),
    Link(Res.string.special_char_link, ">"),
    Bird(Res.string.special_char_bird, "%"),
    Ankh(Res.string.special_char_ankh, "^"),
    Squigly(Res.string.special_char_squigly, "="),
    Heart1(Res.string.special_char_heart_1, "¼"),
    Heart2(Res.string.special_char_heart_2, "½"),
    Heart3(Res.string.special_char_heart_3, "¾"),
    Heart4(Res.string.special_char_heart_4, "♥"),
    Var1(Res.string.special_char_var_1, "ᚋ"),
    Var2(Res.string.special_char_var_2, "ᚌ"),
    Var3(Res.string.special_char_var_3, "ᚍ"),
    Var4(Res.string.special_char_var_4, "ᚎ"),
}
