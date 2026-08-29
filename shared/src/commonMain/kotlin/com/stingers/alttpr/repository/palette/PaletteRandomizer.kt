package com.stingers.alttpr.repository.palette

import com.stingers.alttpr.model.api.PaletteAlgorithm
import com.stingers.alttpr.repository.random.Prando

/**
 * Ported from `@maseya/z3pr`'s `index.js` `randomize()`, restricted to the overworld + dungeon
 * palette regions the upstream website actually exercises (`randomize_overworld: true,
 * randomize_dungeon: true`). The website only ever used the "maseya" mode, but all 8 blend modes
 * the library defines are supported here since [PaletteAlgorithm] already exposes them.
 *
 * Each offset group (one entry from the vendored tables) is processed with one of two methods:
 * - "uniform" (maseya/grayscale/negative/blackout/classic): one blend value is drawn and applied
 *   to every offset in the group.
 * - "per color" (dizzy/sick/puke): offsets are first split by their *current* color, and one
 *   blend value is drawn per distinct color (matching `group_values_ordered` in `palette_editor.js`).
 *
 * Groups are processed dungeon-then-overworld (matching the JS `compact([dungeon, ...,
 * overworld])` literal ordering), since the blend generator is a single sequential stream.
 *
 * [PaletteAlgorithm.Random] is our own addition (not part of the upstream library): it resolves
 * to one of the other 7 algorithms, chosen deterministically per-hash — see [resolveAlgorithm].
 */
object PaletteRandomizer {

    fun shufflePalette(rom: ByteArray, hash: String, mode: PaletteAlgorithm) {
        val resolvedMode = resolveAlgorithm(mode, hash)
        val blendFn = blendFunctionFor(resolvedMode)
        val nextBlend = blendGeneratorFor(resolvedMode, hash)
        val groups = DUNGEON_PALETTE_OFFSETS + OVERWORLD_PALETTE_OFFSETS

        when (blendMethodFor(resolvedMode)) {
            BlendMethod.UNIFORM -> groups.forEach { applyUniform(rom, it, blendFn, nextBlend) }
            BlendMethod.PER_COLOR -> groups.forEach { applyPerColor(rom, it, blendFn, nextBlend) }
        }
    }

    /**
     * [PaletteAlgorithm.Random] picks uniformly among the other 7 algorithms, deterministically
     * from [hash] (same rationale as the rest of this class: reproducible per-seed results). A
     * namespaced hash keeps this pick independent from the blend-value seed derived from the
     * plain [hash] in [blendGeneratorFor].
     */
    private fun resolveAlgorithm(mode: PaletteAlgorithm, hash: String): PaletteAlgorithm {
        if (mode != PaletteAlgorithm.Random) return mode

        val candidates = PaletteAlgorithm.entries.filter { it != PaletteAlgorithm.Random }
        val index = Prando.fromSeed("$hash:palette-algorithm").nextInt(0, (candidates.size - 1).toLong()).toInt()
        return candidates[index]
    }

    private fun applyUniform(
        rom: ByteArray,
        offsets: List<Int>,
        blendFn: (ColorF, ColorF) -> ColorF,
        nextBlend: () -> ColorF
    ) {
        val blend = nextBlend()
        for (offset in offsets) {
            if (!PaletteEditor.isInBounds(rom, offset)) continue
            val base = PaletteEditor.readColor(rom, offset)
            PaletteEditor.writeColor(rom, offset, blendFn(base, blend))
        }
    }

    private fun applyPerColor(
        rom: ByteArray,
        offsets: List<Int>,
        blendFn: (ColorF, ColorF) -> ColorF,
        nextBlend: () -> ColorF
    ) {
        val colorGroups = LinkedHashMap<ColorF, MutableList<Int>>()
        for (offset in offsets) {
            if (!PaletteEditor.isInBounds(rom, offset)) continue
            val color = PaletteEditor.readColor(rom, offset)
            colorGroups.getOrPut(color) { mutableListOf() }.add(offset)
        }

        for ((baseColor, groupOffsets) in colorGroups) {
            val result = blendFn(baseColor, nextBlend())
            for (offset in groupOffsets) {
                PaletteEditor.writeColor(rom, offset, result)
            }
        }
    }

    private enum class BlendMethod { UNIFORM, PER_COLOR }

    private fun blendMethodFor(mode: PaletteAlgorithm): BlendMethod = when (mode) {
        PaletteAlgorithm.Dizzy, PaletteAlgorithm.Sick, PaletteAlgorithm.Puke -> BlendMethod.PER_COLOR
        else -> BlendMethod.UNIFORM
    }

    private fun blendFunctionFor(mode: PaletteAlgorithm): (ColorF, ColorF) -> ColorF = when (mode) {
        PaletteAlgorithm.Random -> error("Random must be resolved via resolveAlgorithm() first")
        PaletteAlgorithm.Maseya -> ::maseyaBlend
        PaletteAlgorithm.Grayscale -> { base, _ -> base.grayscale() }
        PaletteAlgorithm.Negative -> { base, _ -> base.invert() }
        PaletteAlgorithm.Blackout -> { _, blend -> blend }
        PaletteAlgorithm.Classic -> ::classicBlend
        PaletteAlgorithm.Dizzy -> { base, blend -> ColorF.hueBlend(base, blend) }
        PaletteAlgorithm.Sick -> { base, blend -> ColorF.lumaBlend(blend, base) }
        PaletteAlgorithm.Puke -> { _, blend -> blend }
    }

    /**
     * Grayscale/negative ignore the drawn value entirely (their [blendFunctionFor] lambda never
     * reads it) and blackout always wants black, so none of the three need real randomness —
     * matching JS's `infinite_null`/`infinite_black` generators, which don't touch the seed.
     */
    private fun blendGeneratorFor(mode: PaletteAlgorithm, hash: String): () -> ColorF {
        if (mode == PaletteAlgorithm.Grayscale ||
            mode == PaletteAlgorithm.Negative ||
            mode == PaletteAlgorithm.Blackout
        ) {
            return { ColorF.BLACK }
        }

        val seed = Prando.fromSeed(hash).nextInt(0, 4294967295).toInt()
        val stream = Sfc32Random(seed)
        return { stream.nextColor() }
    }
}
