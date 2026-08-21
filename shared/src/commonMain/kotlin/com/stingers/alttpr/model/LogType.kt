package com.stingers.alttpr.model

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.debug_logs
import alttpr.shared.generated.resources.error_logs
import alttpr.shared.generated.resources.info_logs
import alttpr.shared.generated.resources.verbose_logs
import alttpr.shared.generated.resources.warn_logs
import alttpr.shared.generated.resources.wtf_logs
import org.jetbrains.compose.resources.StringResource

enum class LogType(val res: StringResource) {
    DEBUG(Res.string.debug_logs),
    ERROR(Res.string.error_logs),
    INFO(Res.string.info_logs),
    VERBOSE(Res.string.verbose_logs),
    WARN(Res.string.warn_logs),
    WTF(Res.string.wtf_logs),
}