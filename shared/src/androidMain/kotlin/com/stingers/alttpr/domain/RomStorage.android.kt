package com.stingers.alttpr.domain

import android.content.Context
import io.github.vinceglb.filekit.PlatformFile
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

actual object RomStorage : KoinComponent {
    private val context: Context by inject()

    private fun getStorageDir(): File {
        val appDir = File(context.filesDir, "alttpr")
        val baseRomDir = File(appDir, "base_rom")
        val generatedDir = File(appDir, "generated_seeds")
        if (!baseRomDir.exists()) baseRomDir.mkdirs()
        if (!generatedDir.exists()) generatedDir.mkdirs()
        return baseRomDir
    }

    actual suspend fun getBaseRomFile(): PlatformFile? {
        val file = File(getStorageDir(), "alttp_base.sfc")
        return PlatformFile(file)
    }

    actual suspend fun saveBaseRomBytes(bytes: ByteArray): Result<Unit> {
        return try {
            val file = File(getStorageDir(), "alttp_base.sfc")
            file.writeBytes(bytes)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
