package com.stingers.alttpr.model

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.debug_logs
import alttpr.shared.generated.resources.error_logs
import alttpr.shared.generated.resources.info_logs
import alttpr.shared.generated.resources.verbose_logs
import alttpr.shared.generated.resources.warn_logs
import alttpr.shared.generated.resources.wtf_logs
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.StringResource

enum class LogType(val res: StringResource, val color: Color) {
    DEBUG(Res.string.debug_logs, Color.White),
    ERROR(Res.string.error_logs, Color.Red),
    INFO(Res.string.info_logs, Color.Green),
    VERBOSE(Res.string.verbose_logs, Color.Cyan),
    WARN(Res.string.warn_logs, Color.Yellow),
    WTF(Res.string.wtf_logs, Color.Magenta),
}
