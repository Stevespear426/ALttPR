package com.stingers.alttpr.common

actual fun debug(tag: String, message: String) {
    print("\ndebug: $tag: $message")
}

actual fun exception(tag: String, message: String, error: Throwable) {
    print("\nerror: $tag: $message \n ${error.stackTraceToString()}")
}

actual fun info(tag: String, message: String) {
    print("\ninfo: $tag: $message")
}

actual fun warn(tag: String, message: String) {
    print("\nwarn: $tag: $message")
}

actual fun whatTheF(tag: String, message: String) {
    print("\nwtf: $tag: $message")
}