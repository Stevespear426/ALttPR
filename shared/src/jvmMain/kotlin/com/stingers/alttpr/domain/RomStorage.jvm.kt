package com.stingers.alttpr.domain

import io.github.vinceglb.filekit.PlatformFile
import java.io.File

actual object RomStorage {
    private fun getStorageDir(): File {
        val userHome = System.getProperty("user.home") ?: "."
        val appDir = File(userHome, ".alttpr")
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
