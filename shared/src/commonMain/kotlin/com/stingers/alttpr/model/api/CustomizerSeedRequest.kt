package com.stingers.alttpr.model.api

import com.stingers.alttpr.model.GameModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomizerSeedRequest(
    @SerialName("lang") val lang: String? = null,
    @SerialName("glitches") val glitches: String? = null,
    @SerialName("item_placement") val itemPlacement: String? = null,
    @SerialName("dungeon_items") val dungeonItems: String? = null,
    @SerialName("accessibility") val accessibility: String? = null,
    @SerialName("goal") val goal: String? = null,
    @SerialName("crystals") val crystals: CrystalsConfig? = null,
    @SerialName("mode") val mode: String? = null,
    @SerialName("entrances") val entrances: String? = null,
    @SerialName("enemizer") val enemizer: EnemizerConfig? = null,
    @SerialName("hints") val hints: String? = null,
    @SerialName("weapons") val weapons: String? = null,
    @SerialName("item") val item: ItemConfig? = null,
    @SerialName("tournament") val tournament: Boolean? = null,
    @SerialName("spoilers") val spoilers: String? = null,
    @SerialName("allow_quickswap") val allowQuickswap: Boolean? = null,
    @SerialName("override_start_screen") val overrideStartScreen: Boolean? = null,
    @SerialName("pseudoboots") val pseudoboots: Boolean? = null,
    @SerialName("notes") val notes: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("eq") val eq: List<String>? = null,
    // Never omitted: CustomizerController::prepSeed's `ignoreCanKillEscapeThings` does
    // array_key_exists($needle, $request->input('l')) with no `, []` default — an absent `l`
    // passes null into array_key_exists() as a TypeError (uncaught, so a generic 500), unlike
    // every other read of `l`/`eq`/`drops`/`texts`, which all default to `[]` server-side.
    @SerialName("l") val l: Map<String, String>? = null,
    @SerialName("custom") val custom: CustomOverrides? = null,
    @SerialName("drops") val drops: Map<String, Map<String, String>>? = null,
    @SerialName("texts") val texts: Map<String, String>? = null,
) {

    companion object {
        fun getRequest(model: GameModel): CustomizerSeedRequest {
            with(model) {
                return CustomizerSeedRequest(
                    lang = lang?.value,
                    glitches = glitches?.value,
                    itemPlacement = itemPlacement?.value,
                    dungeonItems = dungeonItems?.value,
                    accessibility = accessibility?.value,
                    goal = goal?.value,
                    crystals = CrystalsConfig(
                        tower = towerCrystals?.value,
                        ganon = ganonCrystals?.value
                    ),
                    mode = worldState?.value,
                    entrances = entrances?.value,
                    enemizer = EnemizerConfig(
                        bossShuffle = bossShuffle?.value,
                        enemyShuffle = enemyShuffle?.value,
                        potShuffle = potShuffle?.value,
                        enemyDamage = enemyDamage?.value,
                        enemyHealth = enemyHealth?.value
                    ),
                    hints = hints?.value,
                    weapons = weapons?.value,
                    item = ItemConfig(
                        pool = itemPool?.value,
                        functionality = itemFunctionality?.value
                    ),
                    tournament = tournament,
                    spoilers = spoilers?.value,
                    allowQuickswap = allowQuickswap,
                    overrideStartScreen = overrideStartScreen,
                    pseudoboots = pseudoboots,
                    name = name,
                    notes = notes,
                    eq = startingEquipment.map { it.value }.ifEmpty { null },
                    l = buildPlacements(),
                    drops = buildDrops().ifEmpty { null },
                    texts = textOverrides.entries.associate { (key, value) -> key.value to value }
                        .ifEmpty { null },
                    custom = buildCustomOverrides(),
                )
            }
        }

        private fun GameModel.buildPlacements(): Map<String, String> {
            val placements = mutableMapOf<String, String>()
            itemPlacements.forEach { (location, item) -> placements[location.value] = "${item.value}:1" }
            prizePlacements.forEach { (location, prize) -> placements[location.value] = "${prize.value}:1" }
            medallionPlacements.forEach { (location, medallion) ->
                placements[location.value] = "${medallion.value}:1"
            }
            bottlePlacements.forEach { (location, bottle) -> placements[location.value] = "${bottle.value}:1" }
            return placements
        }

        private fun GameModel.buildDrops(): Map<String, Map<String, String>> {
            return dropOverrides.entries.associate { (pack, slots) ->
                pack.id to slots.entries.associate { (slot, drop) -> slot.toString() to drop.value }
            }
        }

        // Unlike `l`, `eq`, `drops`, and `texts` (all read server-side with an empty-array
        // fallback), the backend does `Arr::dot($request->input('custom'))` with no default —
        // omitting `custom` entirely sends `null` into that foreach and 409s. Always send at
        // least `{}` here.
        private fun GameModel.buildCustomOverrides(): CustomOverrides {
            val itemOverrides = CustomItemOverrides(
                count = itemCounts.entries.associate { (item, count) -> item.value to count }
                    .ifEmpty { null },
                goal = goalRequired?.let { CustomGoalOverrides(required = it) },
                require = CustomItemRequireOverrides(lamp = requireLamp ?: false)
            )

            val prizeOverrides = with(prizeToggles) {
                if (crossWorld != null || shufflePendants != null || shuffleCrystals != null) {
                    CustomPrizeOverrides(
                        crossWorld = crossWorld,
                        shufflePendants = shufflePendants,
                        shuffleCrystals = shuffleCrystals
                    )
                } else {
                    null
                }
            }

            val regionOverrides = with(regionToggles) {
                if (listOfNotNull(
                        bossNormalLocation, pyramidBowUpgrade, bossHaveKey, forceSkullWoodsKey,
                        wildKeys, wildBigKeys, wildMaps, wildCompasses
                    ).isNotEmpty()
                ) {
                    CustomRegionOverrides(
                        bossNormalLocation = bossNormalLocation,
                        pyramidBowUpgrade = pyramidBowUpgrade,
                        bossHaveKey = bossHaveKey,
                        forceSkullWoodsKey = forceSkullWoodsKey,
                        wildKeys = wildKeys,
                        wildBigKeys = wildBigKeys,
                        wildMaps = wildMaps,
                        wildCompasses = wildCompasses
                    )
                } else {
                    null
                }
            }

            val romOverrides = CustomRomOverrides(
                genericKeys = genericKeys,
                hudItemCounter = hudItemCounter,
                dungeonCount = dungeonCount?.value,
                timerMode = timerMode?.value,
                freeItemMenu = false,
                freeItemText = false
            )

            val spoilOverrides = bootsLocationSpoiler?.let { CustomSpoilOverrides(bootsLocation = it) }

            return CustomOverrides(
                item = itemOverrides,
                prize = prizeOverrides,
                region = regionOverrides,
                rom = romOverrides,
                spoil = spoilOverrides
            )
        }
    }
}
