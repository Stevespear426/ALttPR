package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.clock_mode_off
import alttpr.shared.generated.resources.clock_mode_stopwatch
import alttpr.shared.generated.resources.clock_mode_ohko
import alttpr.shared.generated.resources.clock_mode_continue
import alttpr.shared.generated.resources.clock_mode_stop
import alttpr.shared.generated.resources.clock_mode_end
import org.jetbrains.compose.resources.StringResource

enum class ClockMode(val title: StringResource, val value: String) {
    Off(Res.string.clock_mode_off, "off"),
    Stopwatch(Res.string.clock_mode_stopwatch, "stopwatch"),
    OHKO(Res.string.clock_mode_ohko, "countdown-ohko"),
    Continue(Res.string.clock_mode_continue, "countdown-continue"),
    Stop(Res.string.clock_mode_stop, "countdown-stop"),
    End(Res.string.clock_mode_end, "countdown-end"),
}
