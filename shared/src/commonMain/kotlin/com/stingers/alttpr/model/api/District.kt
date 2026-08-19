package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.district_big_chests
import alttpr.shared.generated.resources.district_bosses
import alttpr.shared.generated.resources.district_fetch_quests
import alttpr.shared.generated.resources.district_water_locked
import alttpr.shared.generated.resources.district_death_mountain
import alttpr.shared.generated.resources.district_kakariko_village
import alttpr.shared.generated.resources.district_hyrule_castle
import alttpr.shared.generated.resources.district_eastern_palace
import alttpr.shared.generated.resources.district_desert_palace
import alttpr.shared.generated.resources.district_tower_of_hera
import alttpr.shared.generated.resources.district_castle_tower
import alttpr.shared.generated.resources.district_dark_palace
import alttpr.shared.generated.resources.district_swamp_palace
import alttpr.shared.generated.resources.district_skull_woods
import alttpr.shared.generated.resources.district_thieves_town
import alttpr.shared.generated.resources.district_ice_palace
import alttpr.shared.generated.resources.district_misery_mire
import alttpr.shared.generated.resources.district_turtle_rock
import alttpr.shared.generated.resources.district_ganons_tower_no_bk
import alttpr.shared.generated.resources.district_ganons_tower
import org.jetbrains.compose.resources.StringResource

enum class District(val title: StringResource) {
    BigChests(Res.string.district_big_chests),
    Bosses(Res.string.district_bosses),
    FetchQuests(Res.string.district_fetch_quests),
    WaterLocked(Res.string.district_water_locked),
    DeathMountain(Res.string.district_death_mountain),
    KakarikoVillage(Res.string.district_kakariko_village),
    HyruleCastle(Res.string.district_hyrule_castle),
    EasternPalace(Res.string.district_eastern_palace),
    DesertPalace(Res.string.district_desert_palace),
    TowerOfHera(Res.string.district_tower_of_hera),
    CastleTower(Res.string.district_castle_tower),
    DarkPalace(Res.string.district_dark_palace),
    SwampPalace(Res.string.district_swamp_palace),
    SkullWoods(Res.string.district_skull_woods),
    ThievesTown(Res.string.district_thieves_town),
    IcePalace(Res.string.district_ice_palace),
    MiseryMire(Res.string.district_misery_mire),
    TurtleRock(Res.string.district_turtle_rock),
    GanonsTowerNoBK(Res.string.district_ganons_tower_no_bk),
    GanonsTower(Res.string.district_ganons_tower),
}
