package com.stingers.alttpr

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CC_MD5
import platform.CoreCrypto.CC_MD5_DIGEST_LENGTH

@OptIn(ExperimentalForeignApi::class)
actual fun computeMd5Hex(bytes: ByteArray): String {
    val digest = ByteArray(CC_MD5_DIGEST_LENGTH)
    bytes.usePinned { srcPinned ->
        digest.usePinned { dstPinned ->
            CC_MD5(
                srcPinned.addressOf(0),
                bytes.size.toUInt(),
                dstPinned.addressOf(0).reinterpret()
            )
        }
    }

    // Pure Kotlin hex string conversion (no JVM String.format)
    return digest.joinToString("") {
        (it.toInt() and 0xFF).toString(16).padStart(2, '0')
    }
}

actual val platformType = PlatformType.iOS
actual fun getPlatformFeatures() = listOf(Features.Share)