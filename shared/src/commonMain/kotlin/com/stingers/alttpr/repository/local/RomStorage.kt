package com.stingers.alttpr.repository.local

import io.github.vinceglb.filekit.PlatformFile

expect object RomStorage {
    suspend fun getBaseRomFile(): PlatformFile?
    suspend fun saveBaseRomBytes(bytes: ByteArray): Result<Unit>
    suspend fun saveGeneratedSeed(filename: String, bytes: ByteArray): Result<Unit>
    suspend fun getGeneratedSeedFile(filename: String): PlatformFile?
    suspend fun saveSpriteFile(filename: String, bytes: ByteArray): Result<Unit>
    suspend fun getSpriteFile(filename: String): PlatformFile?
}
