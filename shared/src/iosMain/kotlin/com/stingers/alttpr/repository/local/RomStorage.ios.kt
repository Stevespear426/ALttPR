package com.stingers.alttpr.repository.local

import com.stingers.alttpr.common.BASE_ROM_FILENAME
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.exists
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
        val shareRomDir = appDir.URLByAppendingPathComponent("share_rom") ?: return null to null
        val generatedDir = appDir.URLByAppendingPathComponent("generated_seeds") ?: return null to null
        val spritesDir = appDir.URLByAppendingPathComponent("sprites") ?: return null to null
        
        val fileManagerInstance = NSFileManager.defaultManager
        listOf(baseRomDir, shareRomDir, generatedDir, spritesDir).forEach { url ->
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
        val fileUrl = baseRomDir?.URLByAppendingPathComponent(BASE_ROM_FILENAME) ?: return null
        return fileUrl.path?.let { PlatformFile(it) }
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual suspend fun saveBaseRomBytes(bytes: ByteArray): Result<Unit> {
        return try {
            val (baseRomDir, _) = getStorageDirs()
            val fileUrl = baseRomDir?.URLByAppendingPathComponent(BASE_ROM_FILENAME) ?: return Result.failure(IllegalStateException("File URL error"))
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

    @OptIn(ExperimentalForeignApi::class)
    private fun getShareRomDir(): NSURL? {
        val fileManager = NSFileManager.defaultManager
        val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
        val documentDirectory = urls.firstOrNull() as? NSURL ?: return null
        val appDir = documentDirectory.URLByAppendingPathComponent("alttpr") ?: return null
        val shareRomDir = appDir.URLByAppendingPathComponent("share_rom") ?: return null
        shareRomDir.path?.let { p ->
            if (!fileManager.fileExistsAtPath(p)) {
                fileManager.createDirectoryAtPath(p, withIntermediateDirectories = true, attributes = null, error = null)
            }
        }
        return shareRomDir
    }

    actual suspend fun getShareRomFile(filename: String): PlatformFile? {
        val shareRomDir = getShareRomDir() ?: return null
        val fileUrl = shareRomDir.URLByAppendingPathComponent(filename) ?: return null
        val path = fileUrl.path ?: return null
        val file = PlatformFile(path)
        return if (file.exists()) file else null
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual suspend fun saveShareRomBytes(filename: String, bytes: ByteArray): Result<Unit> {
        return try {
            val shareRomDir = getShareRomDir() ?: return Result.failure(IllegalStateException("Directory error"))
            val fileUrl = shareRomDir.URLByAppendingPathComponent(filename) ?: return Result.failure(IllegalStateException("File URL error"))
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

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun clearShareRomFiles(): Result<Unit> {
        return try {
            val fileManager = NSFileManager.defaultManager
            val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            val documentDirectory = urls.firstOrNull() as? NSURL ?: return Result.failure(IllegalStateException("Directory error"))
            val appDir = documentDirectory.URLByAppendingPathComponent("alttpr") ?: return Result.failure(IllegalStateException("Directory error"))
            val shareRomDir = appDir.URLByAppendingPathComponent("share_rom") ?: return Result.failure(IllegalStateException("Directory error"))
            
            shareRomDir.path?.let { p ->
                if (fileManager.fileExistsAtPath(p)) {
                    fileManager.removeItemAtPath(p, error = null)
                }
                fileManager.createDirectoryAtPath(p, withIntermediateDirectories = true, attributes = null, error = null)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun clearGeneratedSeedFiles(): Result<Unit> {
        return try {
            val fileManager = NSFileManager.defaultManager
            val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            val documentDirectory = urls.firstOrNull() as? NSURL ?: return Result.failure(IllegalStateException("Directory error"))
            val appDir = documentDirectory.URLByAppendingPathComponent("alttpr") ?: return Result.failure(IllegalStateException("Directory error"))
            val generatedDir = appDir.URLByAppendingPathComponent("generated_seeds") ?: return Result.failure(IllegalStateException("Directory error"))
            
            generatedDir.path?.let { p ->
                if (fileManager.fileExistsAtPath(p)) {
                    fileManager.removeItemAtPath(p, error = null)
                }
                fileManager.createDirectoryAtPath(p, withIntermediateDirectories = true, attributes = null, error = null)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun clearSpriteFiles(): Result<Unit> {
        return try {
            val fileManager = NSFileManager.defaultManager
            val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            val documentDirectory = urls.firstOrNull() as? NSURL ?: return Result.failure(IllegalStateException("Directory error"))
            val appDir = documentDirectory.URLByAppendingPathComponent("alttpr") ?: return Result.failure(IllegalStateException("Directory error"))
            val spritesDir = appDir.URLByAppendingPathComponent("sprites") ?: return Result.failure(IllegalStateException("Directory error"))
            
            spritesDir.path?.let { p ->
                if (fileManager.fileExistsAtPath(p)) {
                    fileManager.removeItemAtPath(p, error = null)
                }
                fileManager.createDirectoryAtPath(p, withIntermediateDirectories = true, attributes = null, error = null)
            }
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
        val file = PlatformFile(path)
        return if (file.exists()) file else null
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun getSpritesDir(): NSURL? {
        val fileManager = NSFileManager.defaultManager
        val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
        val documentDirectory = urls.firstOrNull() as? NSURL ?: return null
        val appDir = documentDirectory.URLByAppendingPathComponent("alttpr") ?: return null
        val spritesDir = appDir.URLByAppendingPathComponent("sprites") ?: return null
        spritesDir.path?.let { p ->
            if (!fileManager.fileExistsAtPath(p)) {
                fileManager.createDirectoryAtPath(p, withIntermediateDirectories = true, attributes = null, error = null)
            }
        }
        return spritesDir
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual suspend fun saveSpriteFile(filename: String, bytes: ByteArray): Result<Unit> {
        return try {
            val spritesDir = getSpritesDir() ?: return Result.failure(IllegalStateException("Directory error"))
            val fileUrl = spritesDir.URLByAppendingPathComponent(filename) ?: return Result.failure(IllegalStateException("File URL error"))
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

    actual suspend fun getSpriteFile(filename: String): PlatformFile? {
        val spritesDir = getSpritesDir() ?: return null
        val fileUrl = spritesDir.URLByAppendingPathComponent(filename) ?: return null
        val path = fileUrl.path ?: return null
        val file = PlatformFile(path)
        return if (file.exists()) file else null
    }
}
