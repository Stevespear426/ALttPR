package com.stingers.alttpr.screens.upload

import io.github.vinceglb.filekit.PlatformFile

sealed interface UploadRomEvent {
    data class SaveRom(val value: PlatformFile) : UploadRomEvent
}
