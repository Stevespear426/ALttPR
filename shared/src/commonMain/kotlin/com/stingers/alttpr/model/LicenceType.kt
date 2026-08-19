package com.stingers.alttpr.model

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.apache
import alttpr.shared.generated.resources.custom
import alttpr.shared.generated.resources.gnu
import alttpr.shared.generated.resources.mit
import org.jetbrains.compose.resources.StringResource

enum class LicenceType(
    val title: StringResource,
) {
    MIT(Res.string.mit),
    APACHE(Res.string.apache),
    GNU(Res.string.gnu),
    CUSTOM(Res.string.custom),
}