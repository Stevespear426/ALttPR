package com.stingers.alttpr.model

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.gamemode_beginner
import alttpr.shared.generated.resources.gamemode_crosskeys
import alttpr.shared.generated.resources.gamemode_custom
import alttpr.shared.generated.resources.gamemode_default
import alttpr.shared.generated.resources.gamemode_nightmare
import alttpr.shared.generated.resources.gamemode_owg
import alttpr.shared.generated.resources.gamemode_super_quick
import com.stingers.alttpr.model.api.Accessibility
import com.stingers.alttpr.model.api.BossShuffle
import com.stingers.alttpr.model.api.Crystals
import com.stingers.alttpr.model.api.CrystalsConfig
import com.stingers.alttpr.model.api.EnemizerConfig
import com.stingers.alttpr.model.api.EnemyDamage
import com.stingers.alttpr.model.api.EnemyHealth
import com.stingers.alttpr.model.api.EnemyShuffle
import com.stingers.alttpr.model.api.Entrances
import com.stingers.alttpr.model.api.GenerateSeedRequest
import com.stingers.alttpr.model.api.Glitches
import com.stingers.alttpr.model.api.Goals
import com.stingers.alttpr.model.api.ItemConfig
import com.stingers.alttpr.model.api.ItemFunctionality
import com.stingers.alttpr.model.api.ItemPlacement
import com.stingers.alttpr.model.api.ItemPool
import com.stingers.alttpr.model.api.Keysanity
import com.stingers.alttpr.model.api.Language
import com.stingers.alttpr.model.api.Spoilers
import com.stingers.alttpr.model.api.Toggle
import com.stingers.alttpr.model.api.Weapons
import com.stingers.alttpr.model.api.WorldState
import org.jetbrains.compose.resources.StringResource

enum class RandomizerGameMode(val title: StringResource) {
    DEFAULT(Res.string.gamemode_default) {
        override fun request() = generateRandomizerSeedRequest(
            glitches = Glitches.None,
            itemPlacement = ItemPlacement.Advanced,
            dungeonItems = Keysanity.Standard,
            accessibility = Accessibility.Items,
            goal = Goals.DefeatGanon,
            towerCrystals = Crystals.Seven,
            ganonCrystals = Crystals.Seven,
            worldState = WorldState.Open,
            entrances = Entrances.None,
            bossShuffle = BossShuffle.None,
            enemyShuffle = EnemyShuffle.None,
            hints = Toggle.On,
            weapons = Weapons.Randomized,
            itemPool = ItemPool.Normal,
            itemFunctionality = ItemFunctionality.Normal,
            enemyDamage = EnemyDamage.Default,
            enemyHealth = EnemyHealth.Default
        )
    },
    BEGINNER(Res.string.gamemode_beginner) {
        override fun request() = generateRandomizerSeedRequest(
            glitches = Glitches.None,
            itemPlacement = ItemPlacement.Basic,
            dungeonItems = Keysanity.Standard,
            accessibility = Accessibility.Locations,
            goal = Goals.DefeatGanon,
            towerCrystals = Crystals.Seven,
            ganonCrystals = Crystals.Seven,
            worldState = WorldState.Standard,
            entrances = Entrances.None,
            bossShuffle = BossShuffle.None,
            enemyShuffle = EnemyShuffle.None,
            hints = Toggle.Off,
            weapons = Weapons.Assured,
            itemPool = ItemPool.Normal,
            itemFunctionality = ItemFunctionality.Normal,
            enemyDamage = EnemyDamage.Default,
            enemyHealth = EnemyHealth.Default
        )
    },
    OWG(Res.string.gamemode_owg) {
        override fun request() = generateRandomizerSeedRequest(
            glitches = Glitches.Overworld,
            itemPlacement = ItemPlacement.Basic,
            dungeonItems = Keysanity.Standard,
            accessibility = Accessibility.Locations,
            goal = Goals.FastGanon,
            towerCrystals = Crystals.Seven,
            ganonCrystals = Crystals.Seven,
            worldState = WorldState.Open,
            entrances = Entrances.None,
            bossShuffle = BossShuffle.None,
            enemyShuffle = EnemyShuffle.None,
            hints = Toggle.On,
            weapons = Weapons.Randomized,
            itemPool = ItemPool.Normal,
            itemFunctionality = ItemFunctionality.Normal,
            enemyDamage = EnemyDamage.Default,
            enemyHealth = EnemyHealth.Default
        )
    },
    CROSSKEYS(Res.string.gamemode_crosskeys) {
        override fun request() = generateRandomizerSeedRequest(
            glitches = Glitches.None,
            itemPlacement = ItemPlacement.Advanced,
            dungeonItems = Keysanity.Full,
            accessibility = Accessibility.Items,
            goal = Goals.FastGanon,
            towerCrystals = Crystals.Seven,
            ganonCrystals = Crystals.Seven,
            worldState = WorldState.Open,
            entrances = Entrances.Crossed,
            bossShuffle = BossShuffle.None,
            enemyShuffle = EnemyShuffle.None,
            hints = Toggle.On,
            weapons = Weapons.Randomized,
            itemPool = ItemPool.Normal,
            itemFunctionality = ItemFunctionality.Normal,
            enemyDamage = EnemyDamage.Default,
            enemyHealth = EnemyHealth.Default
        )
    },
    SUPER_QUICK(Res.string.gamemode_super_quick) {
        override fun request() = generateRandomizerSeedRequest(
            glitches = Glitches.None,
            itemPlacement = ItemPlacement.Basic,
            dungeonItems = Keysanity.Standard,
            accessibility = Accessibility.Beatable,
            goal = Goals.FastGanon,
            towerCrystals = Crystals.Zero,
            ganonCrystals = Crystals.Zero,
            worldState = WorldState.Open,
            entrances = Entrances.None,
            bossShuffle = BossShuffle.None,
            enemyShuffle = EnemyShuffle.None,
            hints = Toggle.Off,
            weapons = Weapons.Assured,
            itemPool = ItemPool.Normal,
            itemFunctionality = ItemFunctionality.Normal,
            enemyDamage = EnemyDamage.Default,
            enemyHealth = EnemyHealth.Default
        )
    },
    NIGHTMARE(Res.string.gamemode_nightmare) {
        override fun request() = generateRandomizerSeedRequest(
            glitches = Glitches.None,
            itemPlacement = ItemPlacement.Advanced,
            dungeonItems = Keysanity.Full,
            accessibility = Accessibility.Beatable,
            goal = Goals.DefeatGanon,
            towerCrystals = Crystals.Seven,
            ganonCrystals = Crystals.Seven,
            worldState = WorldState.Inverted,
            entrances = Entrances.Insanity,
            bossShuffle = BossShuffle.Random,
            enemyShuffle = EnemyShuffle.Random,
            hints = Toggle.Off,
            weapons = Weapons.Swordless,
            itemPool = ItemPool.Expert,
            itemFunctionality = ItemFunctionality.Expert,
            enemyDamage = EnemyDamage.Random,
            enemyHealth = EnemyHealth.Expert
        )
    },
    CUSTOM(Res.string.gamemode_custom) {
        override fun request() = generateRandomizerSeedRequest()
    };

    abstract fun request(): GenerateSeedRequest
}



fun generateRandomizerSeedRequest(
    glitches: Glitches? = null,
    itemPlacement: ItemPlacement? = null,
    dungeonItems: Keysanity? = null,
    accessibility: Accessibility? = null,
    goal: Goals? = null,
    towerCrystals: Crystals? = null,
    ganonCrystals: Crystals? = null,
    worldState: WorldState? = null,
    entrances: Entrances? = null,
    bossShuffle: BossShuffle? = null,
    enemyShuffle: EnemyShuffle? = null,
    potShuffle: Toggle? = null,
    enemyDamage: EnemyDamage? = null,
    enemyHealth: EnemyHealth? = null,
    hints: Toggle? = null,
    weapons: Weapons? = null,
    itemPool: ItemPool? = null,
    itemFunctionality: ItemFunctionality? = null,
    tournament: Boolean? = null,
    spoilers: Spoilers? = null,
    allowQuickswap: Boolean? = null,
    overrideStartScreen: Boolean? = null,
    pseudoboots: Boolean? = null,
//    name: String? = null,
    notes: String? = null,
    lang: Language? = null
): GenerateSeedRequest {
    return GenerateSeedRequest(
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
//        name = name,
        notes = notes
    )
}

