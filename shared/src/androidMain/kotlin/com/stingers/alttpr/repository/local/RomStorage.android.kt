package com.stingers.alttpr.repository.local

import android.content.Context
import io.github.vinceglb.filekit.PlatformFile
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

actual object RomStorage : KoinComponent {
    private val context: Context by inject()

    private fun getStorageRoot(): File {
        val appDir = File(context.filesDir, "alttpr")
        return appDir
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
}
