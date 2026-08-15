package com.stingers.alttpr.domain

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
     * Applies a BPS patch using 100% Kotlin Multiplatform standard library functions.
     * Safe for commonMain (Android, iOS, Desktop, WebAssembly).
     */
    suspend fun applyPatch(bpsBytes: ByteArray): ByteArray {
        // Clone the original ROM bytes so we don't mutate the base ROM in memory
        val baseFile = RomStorage.getBaseRomFile()
        val sourceRom = baseFile?.readBytes() ?: return ByteArray(0)

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

        return targetRom
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
