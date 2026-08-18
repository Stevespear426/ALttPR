package com.stingers.alttpr

enum class PlatformType {
    Android,
    iOS,
    Desktop,
}

enum class Features {
    Share
}

expect val platformType: PlatformType

expect fun getPlatformFeatures(): List<Features>

fun hasFeature(feature: Features) = getPlatformFeatures().contains(feature)

expect fun computeMd5Hex(bytes: ByteArray): String
