package com.stingers.alttpr.repository

import com.stingers.alttpr.computeMd5Hex
import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.model.HeartSpeed
import com.stingers.alttpr.model.MenuSpeed
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.repository.local.RomStorage
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton

@Singleton
open class RomManager(
    private val alttprRepository: AlttprRepository
) {

    open suspend fun hasValidBaseRom(): Boolean = withContext(Dispatchers.IO) {
        val baseFile = RomStorage.getBaseRomFile()
        if (baseFile == null || !baseFile.exists()) return@withContext false
        val bytes = try {
            baseFile.readBytes()
        } catch (_: Exception) {
            return@withContext false
        }
        verifyRomBytes(bytes)
    }

    open suspend fun saveAndVerifyRom(sourceFile: PlatformFile): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val bytes = sourceFile.readBytes()
                if (!verifyRomBytes(bytes)) {
                    return@withContext Result.failure(
                        IllegalArgumentException("Invalid Base ROM CRC32")
                    )
                }
                RomStorage.saveBaseRomBytes(bytes)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    open fun verifyRomBytes(bytes: ByteArray): Boolean {
        val normalizedBytes = if (bytes.size % 1024 == HEADER_SIZE) {
            bytes.copyOfRange(HEADER_SIZE, bytes.size)
        } else {
            bytes
        }

        val actualCrc = calculateCrc32(normalizedBytes)
        return actualCrc == EXPECTED_CRC32
    }


    open suspend fun getPatchedRomBytes(
        seedEntity: SeedEntity,
        heartSpeed: HeartSpeed = HeartSpeed.NORMAL,
        menuSpeed: MenuSpeed = MenuSpeed.NORMAL,
        heartColor: HeartColor = HeartColor.RED,
        quickSwap: Boolean = true,
        enableMusic: Boolean = true,
        msuResume: Boolean = true,
        reduceFlashing: Boolean = false,
        sprite: Sprite? = null,
    ): ByteArray? = withContext(Dispatchers.IO) {

        // 1. Get base rom
        val baseFile = RomStorage.getBaseRomFile()
        val sourceRom = baseFile?.readBytes()?.copyOf() ?: return@withContext null

        // 2. Get base rom patch
        val bpsFile = RomStorage.getGeneratedSeedFile(seedEntity.localFileName.orEmpty())
            ?: return@withContext null
        val bpsBytes = bpsFile.readBytes()

        // 3. Apply base rom patch
        val basePatchedBytes = applyBasePatch(sourceRom, bpsBytes, seedEntity.md5.orEmpty())
        if (basePatchedBytes.isEmpty()) return@withContext null

        // 4. Expand rom size to target size (e.g., 2MB or 4MB) BEFORE applying seed patch
        val expandedPatchedBytes = expandSize(basePatchedBytes, seedEntity.size)
        if (expandedPatchedBytes.isEmpty()) return@withContext null

        // 5. Apply seed patch payload onto expanded rom
        var currentRomBytes = applySeedPatch(expandedPatchedBytes, seedEntity.patch)
        if (currentRomBytes.isEmpty()) return@withContext null

        // 6. Inject custom sprite if provided
        if (sprite != null && sprite.name != "Link") {
            val spriteBytes = alttprRepository.getSpriteBytes(sprite)
            if (spriteBytes != null && spriteBytes.isNotEmpty()) {
                val injected = injectSprite(currentRomBytes, spriteBytes)
                if (injected.isNotEmpty()) {
                    currentRomBytes = injected
                }
            }
        }

        // 7. Apply UI settings values
        val finalRomBytes = applySettings(
            romBytes = currentRomBytes,
            seedHash = seedEntity.hash,
            heartSpeed = heartSpeed,
            menuSpeed = menuSpeed,
            heartColor = heartColor,
            quickSwap = quickSwap,
            reduceFlashing = reduceFlashing,
            enableMusic = enableMusic,
            msuResume = msuResume
        )
        if (finalRomBytes.isEmpty()) return@withContext null

        // 8. Update checksum
        return@withContext updateChecksum(finalRomBytes)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun calculateCrc32(bytes: ByteArray): UInt {
        var crc = 0xFFFFFFFFu
        for (b in bytes) {
            val index = ((crc xor b.toUInt()) and 0xFFu).toInt()
            crc = (crc shr 8) xor CRC_TABLE[index]
        }
        return crc xor 0xFFFFFFFFu
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private val CRC_TABLE: UIntArray = UIntArray(256).apply {
        for (i in 0 until 256) {
            var c = i.toUInt()
            repeat(8) {
                c = if ((c and 1u) != 0u) {
                    (c shr 1) xor 0xEDB88320u
                } else {
                    c shr 1
                }
            }
            this[i] = c
        }
    }

    open fun applyBasePatch(sourceRom: ByteArray, bpsBytes: ByteArray, md5: String): ByteArray {
        try {
            if (bpsBytes.size < 16) return ByteArray(0)

            var patchOffset = 0

            val header = bpsBytes.decodeToString(startIndex = 0, endIndex = 4)
            if (header != "BPS1") return ByteArray(0)
            patchOffset += 4

            val sourceSize = readVlq(bpsBytes) { patchOffset++ }
            val targetSize = readVlq(bpsBytes) { patchOffset++ }
            val metadataSize = readVlq(bpsBytes) { patchOffset++ }

            if (sourceRom.size.toLong() != sourceSize) return ByteArray(0)

            patchOffset += metadataSize.toInt()

            val targetRom = ByteArray(targetSize.toInt())
            var targetReadOff = 0
            var sourceRelativeOff = 0
            var targetRelativeOff = 0

            val patchDataEndIndex = bpsBytes.size - 12

            while (patchOffset < patchDataEndIndex) {
                val data = readVlq(bpsBytes) { patchOffset++ }
                val action = (data and 3L).toInt()
                val length = ((data shr 2) + 1).toInt()

                when (action) {
                    0 -> { // SourceRead
                        for (i in 0 until length) {
                            targetRom[targetReadOff] = sourceRom[targetReadOff]
                            targetReadOff++
                        }
                    }

                    1 -> { // TargetRead
                        bpsBytes.copyInto(
                            destination = targetRom,
                            destinationOffset = targetReadOff,
                            startIndex = patchOffset,
                            endIndex = patchOffset + length
                        )
                        patchOffset += length
                        targetReadOff += length
                    }

                    2 -> { // SourceCopy
                        val offsetData = readVlq(bpsBytes) { patchOffset++ }
                        val negative = (offsetData and 1L) == 1L
                        val delta = (offsetData shr 1).toInt()

                        sourceRelativeOff += if (negative) -delta else delta

                        for (i in 0 until length) {
                            targetRom[targetReadOff++] = sourceRom[sourceRelativeOff++]
                        }
                    }

                    3 -> { // TargetCopy
                        val offsetData = readVlq(bpsBytes) { patchOffset++ }
                        val negative = (offsetData and 1L) == 1L
                        val delta = (offsetData shr 1).toInt()

                        targetRelativeOff += if (negative) -delta else delta

                        for (i in 0 until length) {
                            targetRom[targetReadOff++] = targetRom[targetRelativeOff++]
                        }
                    }
                }
            }

            val computedMd5 = computeMd5Hex(targetRom)
            if (!computedMd5.equals(md5, ignoreCase = true)) {
                return ByteArray(0)
            }
            return targetRom
        } catch (_: Exception) {
            return ByteArray(0)
        }
    }

    open fun applySeedPatch(baseRom: ByteArray, patchData: List<Map<String, List<Int>>>): ByteArray {
        val patchedRom = baseRom.copyOf()

        for (patchMap in patchData) {
            for ((offsetStr, byteValues) in patchMap) {
                val offset = offsetStr.toIntOrNull() ?: continue

                for (i in byteValues.indices) {
                    val targetIndex = offset + i
                    if (targetIndex < patchedRom.size) {
                        patchedRom[targetIndex] = byteValues[i].toByte()
                    }
                }
            }
        }

        return patchedRom
    }

    open fun expandSize(baseRom: ByteArray, sizeInMb: Int): ByteArray {
        val targetSize = sizeInMb * (1024 * 1024)
        if (targetSize <= baseRom.size) return baseRom

        val expanded = ByteArray(targetSize)
        baseRom.copyInto(expanded, destinationOffset = 0, startIndex = 0, endIndex = baseRom.size)
        return expanded
    }

    open fun applySettings(
        romBytes: ByteArray,
        seedHash: String,
        title: String = "VT ${seedHash.take(18)}".padEnd(21, ' '),
        heartSpeed: HeartSpeed = HeartSpeed.OFF,
        menuSpeed: MenuSpeed = MenuSpeed.NORMAL,
        heartColor: HeartColor = HeartColor.RED,
        quickSwap: Boolean = true,
        reduceFlashing: Boolean = false,
        enableMusic: Boolean = false,
        msuResume: Boolean = true
    ): ByteArray {

        if (0x7FC0 + 21 <= romBytes.size) {
            title.encodeToByteArray().copyInto(
                destination = romBytes,
                destinationOffset = 0x7FC0,
                startIndex = 0,
                endIndex = minOf(title.length, 21)
            )
        }

        if (0x18004B < romBytes.size) {
            romBytes[0x18004B] = if (quickSwap) 0x01.toByte() else 0x00.toByte()
        }

        if (0x180033 < romBytes.size) {
            romBytes[0x180033] = heartSpeed.value
        }

        if (0x180048 < romBytes.size) {
            romBytes[0x180048] = menuSpeed.value
        }

        if (0x187020 < romBytes.size) {
            romBytes[0x187020] = heartColor.value
        }

        if (reduceFlashing && 0x18017F < romBytes.size) {
            romBytes[0x18017F] = 0x01.toByte()
        }

        if (!enableMusic && 0x18021A < romBytes.size) {
            romBytes[0x18021A] = 0x01.toByte()
        }

        val isInstant = menuSpeed == MenuSpeed.INSTANT
        if (0x18021D < romBytes.size) romBytes[0x18021D] = menuSpeed.value
        if (0x6DD9A < romBytes.size) romBytes[0x6DD9A] =
            if (isInstant) 0x20.toByte() else 0x11.toByte()
        if (0x6DF2A < romBytes.size) romBytes[0x6DF2A] =
            if (isInstant) 0x20.toByte() else 0x11.toByte()
        if (0x6E0E9 < romBytes.size) romBytes[0x6E0E9] =
            if (isInstant) 0x20.toByte() else 0x11.toByte()

        if (!msuResume) {
            if (0x18021D < romBytes.size) romBytes[0x18021D] = 0x00.toByte()
            if (0x18021E < romBytes.size) romBytes[0x18021E] = 0x00.toByte()
        }

        return romBytes
    }

    open fun updateChecksum(baseRom: ByteArray): ByteArray {
        val rom = baseRom.copyOf()
        var total = 0
        for (i in rom.indices) {
            if (i !in 0x7FDC..0x7FDF) {
                total += rom[i].toInt() and 0xFF
            }
        }
        val checksum = (total + 0x1FE) and 0xFFFF
        val inverse = checksum xor 0xFFFF

        rom[0x7FDC] = (inverse and 0xFF).toByte()
        rom[0x7FDD] = (inverse shr 8).toByte()
        rom[0x7FDE] = (checksum and 0xFF).toByte()
        rom[0x7FDF] = (checksum shr 8).toByte()

        return rom
    }

    private inline fun readVlq(bytes: ByteArray, onReadByte: () -> Int): Long {
        var value = 0L
        var shift = 0
        while (true) {
            val b = bytes[onReadByte()].toInt() and 0xFF
            value += (b and 0x7F).toLong() shl shift
            if ((b and 0x80) != 0) break
            shift += 7
            value += 1L shl shift
        }
        return value
    }

    open fun injectSprite(originalRom: ByteArray, spr: ByteArray): ByteArray {
        if (spr.size < 0x7056) return originalRom
        val targetRom = originalRom.copyOf()

        val headBytes = if (spr.size >= 4) {
            spr.decodeToString(startIndex = 0, endIndex = 4)
        } else ""

        if (headBytes == "ZSPR") {
            val gfxOffset = (spr[9].toInt() and 0xFF) or
                    ((spr[10].toInt() and 0xFF) shl 8) or
                    ((spr[11].toInt() and 0xFF) shl 16) or
                    ((spr[12].toInt() and 0xFF) shl 24)

            val paletteOffset = (spr[15].toInt() and 0xFF) or
                    ((spr[16].toInt() and 0xFF) shl 8) or
                    ((spr[17].toInt() and 0xFF) shl 16) or
                    ((spr[18].toInt() and 0xFF) shl 24)

            if (gfxOffset != 0xFFFFFFFF.toInt() && spr.size >= gfxOffset + 0x7000) {
                for (i in 0 until 0x7000) {
                    if (0x80000 + i < targetRom.size) {
                        targetRom[0x80000 + i] = spr[gfxOffset + i]
                    }
                }
            }

            if (spr.size >= paletteOffset + 120) {
                for (i in 0 until 120) {
                    if (0xDD308 + i < targetRom.size) {
                        targetRom[0xDD308 + i] = spr[paletteOffset + i]
                    }
                }
            }

            if (spr.size >= paletteOffset + 124) {
                for (i in 0 until 4) {
                    if (0xDEDF5 + i < targetRom.size) {
                        targetRom[0xDEDF5 + i] = spr[paletteOffset + 120 + i]
                    }
                }
            }
        } else {
            for (i in 0 until 0x7000) {
                if (0x80000 + i < targetRom.size) {
                    targetRom[0x80000 + i] = spr[i]
                }
            }
            for (i in 0 until 120) {
                if (0xDD308 + i < targetRom.size) {
                    targetRom[0xDD308 + i] = spr[0x7000 + i]
                }
            }
            if (0xDEDF8 < targetRom.size) {
                targetRom[0xDEDF5] = spr[0x7036]
                targetRom[0xDEDF6] = spr[0x7037]
                targetRom[0xDEDF7] = spr[0x7054]
                targetRom[0xDEDF8] = spr[0x7055]
            }
        }

        return targetRom
    }

    companion object {
        private const val EXPECTED_CRC32: UInt = 0x3322EFFCu
        private const val HEADER_SIZE = 512
    }
}
