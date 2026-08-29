package com.stingers.alttpr.repository.sfx

import com.stingers.alttpr.repository.random.Prando

/**
 * Ported from the upstream web randomizer's `resources/js/sfx.js` `SFX` class. Shuffles which
 * sound clip plays for which in-game event by permuting two pointer tables (and their
 * accompaniment/chain tables) in the ROM, deterministically seeded from the seed hash.
 */
object SfxRandomizer {

    private val SFX_TABLE = mapOf(2 to 0x1A8BD0, 3 to 0x1A8CCC)
    private val ACCOMPANIMENT_TABLE = mapOf(2 to 0x1A8C4E, 3 to 0x1A8D4A)

    /** Matches the upstream `if (this.build >= "2021-12-21")` gate in `sfx.js`'s `randomizeSfx`. */
    private const val MIN_BUILD = "2021-12-21"

    fun shuffleSfx(rom: ByteArray, hash: String, build: String?) {
        if (build == null || build < MIN_BUILD) return

        val sfxSeed = Prando.fromSeed(hash).nextInt(0, 4294967295)
        val rand = Prando.fromSeed(sfxSeed.toInt())
        val sfxMap = shuffleSfxData(rand)
        writeSfxMap(rom, sfxMap)
    }

    private fun writeSfxMap(rom: ByteArray, sfxMap: Map<Int, Map<Int, SfxAssignment>>) {
        for (set in listOf(2, 3)) {
            val shuffled = sfxMap.getValue(set)
            for (origId in shuffled.keys.sorted()) {
                val assignment = shuffled.getValue(origId)

                val baseAddress = snesToPc(SFX_TABLE.getValue(assignment.targetSet))
                val writeOffset = baseAddress + assignment.targetId * 2 - 2
                if (writeOffset + 1 < rom.size) {
                    rom[writeOffset] = (assignment.entry.addr and 0xff).toByte()
                    rom[writeOffset + 1] = ((assignment.entry.addr shr 8) and 0xff).toByte()
                }

                val accompanimentBase = snesToPc(ACCOMPANIMENT_TABLE.getValue(assignment.targetSet))
                var last = assignment.targetId
                for (chained in assignment.targetChain) {
                    if (accompanimentBase + last - 1 < rom.size) {
                        rom[accompanimentBase + last - 1] = chained.toByte()
                    }
                    last = chained
                }
                if (accompanimentBase + last - 1 < rom.size) {
                    rom[accompanimentBase + last - 1] = 0
                }
            }
        }
    }

    private fun snesToPc(value: Int): Int = ((value and 0x7F0000) shr 1) or (value and 0x7FFF)
}

/** Mutable per-run shuffle result for one [SfxEntry], mirroring the JS code mutating it in place. */
private class SfxAssignment(val entry: SfxEntry) {
    var targetSet: Int = entry.sfxSet
    var targetId: Int = entry.origId
    val targetChain: MutableList<Int> = mutableListOf()
}

private data class CandidateSlot(val set: Int, val id: Int)

private fun shuffleSfxData(rand: Prando): Map<Int, Map<Int, SfxAssignment>> {
    val sfxPool = SFX_ENTRIES.map { SfxAssignment(it) }.toMutableList()
    val sfxMap = mapOf(2 to mutableMapOf<Int, SfxAssignment>(), 3 to mutableMapOf())
    val accompanimentMap = mapOf(2 to mutableListOf<Int>(), 3 to mutableListOf<Int>())
    var candidates = mutableListOf<CandidateSlot>()

    for (assignment in sfxPool) {
        val entry = assignment.entry
        sfxMap.getValue(entry.sfxSet)[entry.origId] = assignment
        if (!entry.accomp) {
            candidates.add(CandidateSlot(entry.sfxSet, entry.origId))
        } else {
            accompanimentMap.getValue(entry.sfxSet).add(entry.origId)
        }
    }

    var chainedSfx = sfxPool.filter { it.entry.chain.isNotEmpty() }.toMutableList()

    candidates = fyShuffle(candidates, rand)
    chainedSfx = fyShuffle(chainedSfx, rand)
    chainedSfx.sortByDescending { it.entry.chain.size }

    for (chained in chainedSfx) {
        val chosenSlot = candidates.firstOrNull {
            accompanimentMap.getValue(it.set).size - chained.entry.chain.size >= 0
        } ?: error("Something went wrong with sfx chains")

        chained.targetSet = chosenSlot.set
        chained.targetId = chosenSlot.id

        for (downstream in chained.entry.chain) {
            val accompanimentSlots = accompanimentMap.getValue(chosenSlot.set)
            val nextSlot = accompanimentSlots.removeAt(accompanimentSlots.lastIndex)
            val downstreamAssignment = sfxMap.getValue(chained.entry.sfxSet).getValue(downstream)
            downstreamAssignment.targetSet = chosenSlot.set
            downstreamAssignment.targetId = nextSlot
            chained.targetChain.add(nextSlot)
        }

        candidates.remove(chosenSlot)
        sfxPool.remove(chained)
    }

    for (sfx in sfxPool.filter { !it.entry.accomp }) {
        val chosenSlot = candidates.removeAt(candidates.lastIndex)
        sfx.targetSet = chosenSlot.set
        sfx.targetId = chosenSlot.id
    }

    return sfxMap
}

private fun <T> fyShuffle(list: List<T>, rand: Prando): MutableList<T> {
    val result = list.toMutableList()
    for (i in result.indices.reversed()) {
        val r = rand.nextInt(0, i.toLong()).toInt()
        val tmp = result[i]
        result[i] = result[r]
        result[r] = tmp
    }
    return result
}
