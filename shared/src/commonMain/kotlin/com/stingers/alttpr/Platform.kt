package com.stingers.alttpr

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun computeMd5Hex(bytes: ByteArray): String
