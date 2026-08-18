package com.stingers.alttpr.platform

actual object NetworkManager {
    actual fun isNetworkConnected(): Boolean {
        // Simplified iOS network check placeholder or reachability
        return true
    }
}
