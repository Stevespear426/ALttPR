package com.stingers.alttpr.repository.local

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.addressOf
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.writeToFile

actual object RomStorage {
    @OptIn(ExperimentalForeignApi::class)
    private fun getStorageDirs(): Pair<NSURL?, NSURL?> {
        val fileManager = NSFileManager.defaultManager
        val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
        val documentDirectory = urls.firstOrNull() as? NSURL ?: return null to null
        val appDir = documentDirectory.URLByAppendingPathComponent("alttpr") ?: return null to null
        val baseRomDir = appDir.URLByAppendingPathComponent("base_rom") ?: return null to null
        val generatedDir = appDir.URLByAppendingPathComponent("generated_seeds") ?: return null to null
        
        val fileManagerInstance = NSFileManager.defaultManager
        listOf(baseRomDir, generatedDir).forEach { url ->
            url?.path?.let { p ->
                if (!fileManagerInstance.fileExistsAtPath(p)) {
                    fileManagerInstance.createDirectoryAtPath(p, withIntermediateDirectories = true, attributes = null, error = null)
                }
            }
        }
        return baseRomDir to generatedDir
    }

    actual suspend fun getBaseRomFile(): PlatformFile? {
        val (baseRomDir, _) = getStorageDirs()
        val fileUrl = baseRomDir?.URLByAppendingPathComponent("alttp_base.sfc") ?: return null
        return fileUrl.path?.let { PlatformFile(it) }
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual suspend fun saveBaseRomBytes(bytes: ByteArray): Result<Unit> {
        return try {
            val (baseRomDir, _) = getStorageDirs()
            val fileUrl = baseRomDir?.URLByAppendingPathComponent("alttp_base.sfc") ?: return Result.failure(IllegalStateException("File URL error"))
            val path = fileUrl.path ?: return Result.failure(IllegalStateException("File path error"))
            
            val nsData = bytes.usePinned { pinned ->
                NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
            }
            nsData.writeToFile(path, atomically = true)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual suspend fun saveGeneratedSeed(filename: String, bytes: ByteArray): Result<Unit> {
        return try {
            val (_, generatedDir) = getStorageDirs()
            val fileUrl = generatedDir?.URLByAppendingPathComponent(filename) ?: return Result.failure(IllegalStateException("File URL error"))
            val path = fileUrl.path ?: return Result.failure(IllegalStateException("File path error"))
            
            val nsData = bytes.usePinned { pinned ->
                NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
            }
            nsData.writeToFile(path, atomically = true)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    actual suspend fun getGeneratedSeedFile(filename: String): PlatformFile? {
        val (_, generatedDir) = getStorageDirs()
        val fileUrl = generatedDir?.URLByAppendingPathComponent(filename) ?: return null
        val path = fileUrl.path ?: return null
        val file = io.github.vinceglb.filekit.PlatformFile(path)
        return if (file.exists()) file else null
    }
}
