package com.stingers.alttpr.common

import org.slf4j.LoggerFactory

val logger: org.slf4j.Logger = LoggerFactory.getLogger("Logger")

actual fun debug(tag: String, message: String) {
    logger.debug("[$tag] $message")
}

actual fun exception(tag: String, message: String, error: Throwable) {
    logger.error("[$tag] $message", error)
}

actual fun info(tag: String, message: String) {
    logger.info("[$tag] $message")
}

actual fun warn(tag: String, message: String) {
    logger.warn("[$tag] $message")
}

actual fun whatTheF(tag: String, message: String) {
    logger.error("WTF: [$tag] $message")
}