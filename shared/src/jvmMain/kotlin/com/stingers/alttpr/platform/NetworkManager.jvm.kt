package com.stingers.alttpr.platform

import java.net.NetworkInterface

actual object NetworkManager {

    actual fun isNetworkConnected(): Boolean {
        return try {
            NetworkInterface.getNetworkInterfaces().asSequence().any {
                it.isUp && !it.isLoopback
            }
        } catch (e: Exception) {
            false
        }
    }
}
