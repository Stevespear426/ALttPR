package com.stingers.alttpr

import java.security.MessageDigest

actual fun computeMd5Hex(bytes: ByteArray): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(bytes)
    return digest.joinToString("") { "%02x".format(it) }
}

actual val platformType = PlatformType.Android
actual fun getPlatformFeatures() = listOf(Features.Share)