package com.stingers.alttpr.model.api

enum class ClockMode(val value: String) {
    Off("off"),
    Stopwatch("stopwatch"),
    OHKO("countdown-ohko"),
    Continue("countdown-continue"),
    Stop("countdown-stop"),
    End("countdown-end"),
}
