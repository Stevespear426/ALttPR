package com.stingers.alttpr.platform

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.ic_arrow_back_ios
import alttpr.shared.generated.resources.ic_ios_share
import org.jetbrains.compose.resources.DrawableResource

actual internal val Res.drawable.ic_share: DrawableResource
    get() = Res.drawable.ic_ios_share

actual internal val Res.drawable.ic_back: DrawableResource
    get() = Res.drawable.ic_arrow_back_ios