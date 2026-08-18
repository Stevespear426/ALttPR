package com.stingers.alttpr.repository.local

import io.github.vinceglb.filekit.PlatformFile
import java.io.File

actual object RomStorage {
    private fun getStorageRoot(): File {
        val userHome = System.getProperty("user.home") ?: "."
        return File(userHome, ".alttpr")
    }

    private fun getBaseRomDir(): File {
        val dir = File(getStorageRoot(), "base_rom")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    private fun getGeneratedSeedsDir(): File {
        val dir = File(getStorageRoot(), "generated_seeds")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    private fun getSpritesDir(): File {
        val dir = File(getStorageRoot(), "sprites")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    actual suspend fun getBaseRomFile(): PlatformFile? {
        val file = File(getBaseRomDir(), "alttp_base.sfc")
        return PlatformFile(file)
    }

    actual suspend fun saveBaseRomBytes(bytes: ByteArray): Result<Unit> {
        return try {
            val file = File(getBaseRomDir(), "alttp_base.sfc")
            file.writeBytes(bytes)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    actual suspend fun saveGeneratedSeed(filename: String, bytes: ByteArray): Result<Unit> {
        return try {
            val file = File(getGeneratedSeedsDir(), filename)
            file.writeBytes(bytes)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    actual suspend fun getGeneratedSeedFile(filename: String): PlatformFile? {
        val file = File(getGeneratedSeedsDir(), filename)
        return if (file.exists()) PlatformFile(file) else null
    }

    actual suspend fun saveSpriteFile(filename: String, bytes: ByteArray): Result<Unit> {
        return try {
            val file = File(getSpritesDir(), filename)
            file.writeBytes(bytes)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    actual suspend fun getSpriteFile(filename: String): PlatformFile? {
        val file = File(getSpritesDir(), filename)
        return if (file.exists()) PlatformFile(file) else null
    }
}
