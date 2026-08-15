package com.stingers.alttpr.repository

import com.stingers.alttpr.computeMd5Hex
import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.model.HeartSpeed
import com.stingers.alttpr.model.MenuSpeed
import com.stingers.alttpr.model.RomEntity
import com.stingers.alttpr.repository.local.RomStorage
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton

@Singleton
class RomManager {

    suspend fun hasValidBaseRom(): Boolean = withContext(Dispatchers.IO) {
        val baseFile = RomStorage.getBaseRomFile()
        if (baseFile == null || !baseFile.exists()) return@withContext false
        val bytes = try {
            baseFile.readBytes()
        } catch (_: Exception) {
            return@withContext false
        }
        verifyRomBytes(bytes)
    }

    suspend fun saveAndVerifyRom(sourceFile: PlatformFile): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val bytes = sourceFile.readBytes()
                if (!verifyRomBytes(bytes)) {
                    // TODO: Toast error "Invalid Base ROM. Expected Zelda no Densetsu: Kamigami no Triforce (v1.0)"
                    return@withContext Result.failure(IllegalArgumentException("Invalid Base ROM CRC32"))
                }
                RomStorage.saveBaseRomBytes(bytes)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    fun verifyRomBytes(bytes: ByteArray): Boolean {
        val normalizedBytes = if (bytes.size % 1024 == HEADER_SIZE) {
            bytes.copyOfRange(HEADER_SIZE, bytes.size)
        } else {
            bytes
        }

        val actualCrc = calculateCrc32(normalizedBytes)
        return actualCrc == EXPECTED_CRC32
    }

    suspend fun getPatchedRomBytes(
        romEntity: RomEntity,
        hash: String,
        heartSpeed: HeartSpeed = HeartSpeed.NORMAL,
        menuSpeed: MenuSpeed = MenuSpeed.NORMAL,
        heartColor: HeartColor = HeartColor.RED,
        quickSwap: Boolean = true,
        enableMusic: Boolean = true,
        msuResume: Boolean = true,
        reduceFlashing: Boolean = false,
    ): ByteArray? {

        // 1. Get base rom
        val baseFile = RomStorage.getBaseRomFile()
        val sourceRom = baseFile?.readBytes()?.clone() ?: return null

        // 2. Get base rom patch
        val bpsFile = RomStorage.getGeneratedSeedFile(romEntity.localFileName) ?: return null
        val bpsBytes = bpsFile.readBytes()

        // 3. Apply base rom patch
        val basePatchedBytes = applyBasePatch(sourceRom, bpsBytes, romEntity.md5)
        if (basePatchedBytes.isEmpty()) return null

        // 4. Expand rom size if needed
        val expandedPatchedBytes = expandSize(basePatchedBytes, romEntity.size)
        if (expandedPatchedBytes.isEmpty()) return null

        // 5. Apply seed patch
        val seedPatchedBytes = applySeedPatch(expandedPatchedBytes, romEntity.patch)
        if (seedPatchedBytes.isEmpty()) return null

        // 6. Apply settings values
        val finalRomBytes = applySettings(
            romBytes = seedPatchedBytes,
            seedHash = hash,
            heartSpeed = heartSpeed,
            menuSpeed = menuSpeed,
            heartColor = heartColor,
            quickSwap = quickSwap,
            reduceFlashing = reduceFlashing,
            enableMusic = enableMusic,
            msuResume = msuResume
        )
        if (finalRomBytes.isEmpty()) return null

        // 6. Update checksum and return
        return updateChecksum(finalRomBytes)
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

    /**
     * Applies a BPS patch
     */
    fun applyBasePatch(sourceRom: ByteArray, bpsBytes: ByteArray, md5: String): ByteArray {

        var patchOffset = 0

        require(bpsBytes.size >= 16) { "Invalid BPS patch file: too small" }

        // 1. Validate Header ("BPS1") in KMP
        // Decodes the first 4 bytes using standard UTF-8/ASCII decoding
        val header = bpsBytes.decodeToString(startIndex = 0, endIndex = 4)
        require(header == "BPS1") { "Invalid BPS header signature" }
        patchOffset += 4

        // 2. Read Variable-Length Metadata Header Fields
        val sourceSize = readVlq(bpsBytes) { patchOffset++ }
        val targetSize = readVlq(bpsBytes) { patchOffset++ }
        val metadataSize = readVlq(bpsBytes) { patchOffset++ }

        // Skip metadata payload
        patchOffset += metadataSize.toInt()

        // Initialize target ROM array and relative pointers
        val targetRom = ByteArray(targetSize.toInt())
        var targetReadOff = 0
        var sourceRelativeOff = 0
        var targetRelativeOff = 0

        val patchDataEndIndex = bpsBytes.size - 12

        // 3. Process Patch Commands
        while (patchOffset < patchDataEndIndex) {
            val data = readVlq(bpsBytes) { patchOffset++ }
            val action = (data and 3L).toInt()
            val length = ((data shr 2) + 1).toInt()

            when (action) {
                0 -> { // SourceRead: Copy sequentially from source ROM
                    for (i in 0 until length) {
                        targetRom[targetReadOff] = sourceRom[targetReadOff]
                        targetReadOff++
                    }
                }

                1 -> { // TargetRead: Copy bytes directly embedded inside the patch stream
                    // KMP Replacement for System.arraycopy
                    bpsBytes.copyInto(
                        destination = targetRom,
                        destinationOffset = targetReadOff,
                        startIndex = patchOffset,
                        endIndex = patchOffset + length
                    )
                    patchOffset += length
                    targetReadOff += length
                }

                2 -> { // SourceCopy: Relative offset copy from source ROM
                    val offsetData = readVlq(bpsBytes) { patchOffset++ }
                    val negative = (offsetData and 1L) == 1L
                    val delta = (offsetData shr 1).toInt()

                    sourceRelativeOff += if (negative) -delta else delta

                    for (i in 0 until length) {
                        targetRom[targetReadOff++] = sourceRom[sourceRelativeOff++]
                    }
                }

                3 -> { // TargetCopy: Relative offset copy from target output (RLE/duplication)
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
    }

    /**
     * Applies the ALttPR JSON patch dictionary directly to a copy of the base ROM.
     *
     * @param baseRom The unmodified "A Link to the Past (USA).sfc" (1,048,576 bytes)
     * @param patchData The raw patch list returned by the ALttPR API
     * @return A newly patched SFC ROM byte array ready for saving
     */
    fun applySeedPatch(baseRom: ByteArray, patchData: List<Map<String, List<Int>>>): ByteArray {
        // Clone the original ROM bytes so we don't mutate the base ROM in memory
        val patchedRom = baseRom.copyOf()

        for (patchMap in patchData) {
            for ((offsetStr, byteValues) in patchMap) {
                val offset = offsetStr.toIntOrNull() ?: continue

                // Overwrite original bytes with patched seed values at the specified offset
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

    fun expandSize(baseRom: ByteArray, size: Int): ByteArray {
        if (size > 2) {
            val newSize = size * (1024 * 1024)
            val resizeSize = minOf(newSize, baseRom.size)
            val replacement = ByteArray(resizeSize)
            baseRom.copyInto(replacement, 0, 0, resizeSize)
            return replacement
        }
        return baseRom
    }

    /**
     * Mutates raw ROM bytes with user-selected UI settings.
     */
    fun applySettings(
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

        title.encodeToByteArray().copyInto(
            destination = romBytes,
            destinationOffset = 0x7FC0,
            startIndex = 0,
            endIndex = minOf(title.length, 21)
        )

        // 2. Item Quickswap
        romBytes[0x18004B] = if (quickSwap) 0x01.toByte() else 0x00.toByte()

        // 3. Heart Beep Speed
        romBytes[0x180033] = heartSpeed.value

        // 4. Menu Speed
        romBytes[0x180048] = menuSpeed.value

        // 5. Heart Color
        romBytes[0x187020] = heartColor.value

        // 6. Reduce Flashing / Photosensitivity
        if (reduceFlashing) {
            romBytes[0x18017F] = 0x01.toByte()
        }

        // 7. Background Music Toggle
        if (!enableMusic) {
            romBytes[0x18021A] = 0x01.toByte()
        }

        // Menu speed (default: normal)
        val isInstant = menuSpeed == MenuSpeed.INSTANT
        romBytes[0x18021D] = menuSpeed.value
        romBytes[0x6dd9a] = if (isInstant) 0x20.toByte() else 0x11.toByte()
        romBytes[0x6df2a] = if (isInstant) 0x20.toByte() else 0x11.toByte()
        romBytes[0x6e0e9] = if (isInstant) 0x20.toByte() else 0x11.toByte()

        if (!msuResume) {
            romBytes[0x18021D] = 0x00.toByte()
            romBytes[0x18021E] = 0x00.toByte()
        }

        return romBytes
    }

    fun updateChecksum(baseRom: ByteArray): ByteArray {
        val rom = baseRom.copyOf()
        // Checksum fix is done last
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

    companion object {
        private const val EXPECTED_CRC32: UInt = 0x3322EFFCu
        private const val HEADER_SIZE = 512
    }
}