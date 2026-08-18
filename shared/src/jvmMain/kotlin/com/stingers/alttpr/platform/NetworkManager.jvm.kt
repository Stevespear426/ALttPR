package com.stingers.alttpr.platform

actual object NetworkManager {
    actual fun isNetworkConnected(): Boolean {
        return true
    }
}
