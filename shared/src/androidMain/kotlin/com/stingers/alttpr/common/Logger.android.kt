package com.stingers.alttpr.common

import android.util.Log

actual fun debug(tag: String, message: String) {
    Log.d(tag, message)
}

actual fun info(tag: String, message: String) {
    Log.i(tag, message)
}

actual fun warn(tag: String, message: String) {
    Log.w(tag, message)
}

actual fun whatTheF(tag: String, message: String) {
    Log.wtf(tag, message)
}

actual fun exception(tag: String, message: String, error: Throwable) {
    Log.e(tag, message, error)
}