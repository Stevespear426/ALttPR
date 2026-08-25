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
import com.stingers.alttpr.model.api.EnemyDamage
import com.stingers.alttpr.model.api.EnemyHealth
import com.stingers.alttpr.model.api.EnemyShuffle
import com.stingers.alttpr.model.api.Entrances
import com.stingers.alttpr.model.api.Glitches
import com.stingers.alttpr.model.api.Goals
import com.stingers.alttpr.model.api.ItemFunctionality
import com.stingers.alttpr.model.api.ItemPlacement
import com.stingers.alttpr.model.api.ItemPool
import com.stingers.alttpr.model.api.Keysanity
import com.stingers.alttpr.model.api.Toggle
import com.stingers.alttpr.model.api.Weapons
import com.stingers.alttpr.model.api.WorldState
import org.jetbrains.compose.resources.StringResource

enum class RandomizerGameMode(val title: StringResource) {
    DEFAULT(Res.string.gamemode_default) {
        override fun model() = RandomizerGameModel(
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
            potShuffle = Toggle.Off,
            hints = Toggle.On,
            weapons = Weapons.Randomized,
            itemPool = ItemPool.Normal,
            itemFunctionality = ItemFunctionality.Normal,
            enemyDamage = EnemyDamage.Default,
            enemyHealth = EnemyHealth.Default,
            pseudoboots = false
        )
    },
    BEGINNER(Res.string.gamemode_beginner) {
        override fun model() = RandomizerGameModel(
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
            potShuffle = Toggle.Off,
            hints = Toggle.Off,
            weapons = Weapons.Assured,
            itemPool = ItemPool.Normal,
            itemFunctionality = ItemFunctionality.Normal,
            enemyDamage = EnemyDamage.Default,
            enemyHealth = EnemyHealth.Default,
            pseudoboots = false
        )
    },
    OWG(Res.string.gamemode_owg) {
        override fun model() = RandomizerGameModel(
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
            potShuffle = Toggle.Off,
            hints = Toggle.On,
            weapons = Weapons.Randomized,
            itemPool = ItemPool.Normal,
            itemFunctionality = ItemFunctionality.Normal,
            enemyDamage = EnemyDamage.Default,
            enemyHealth = EnemyHealth.Default,
            pseudoboots = false
        )
    },
    CROSSKEYS(Res.string.gamemode_crosskeys) {
        override fun model() = RandomizerGameModel(
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
            potShuffle = Toggle.Off,
            hints = Toggle.On,
            weapons = Weapons.Randomized,
            itemPool = ItemPool.Normal,
            itemFunctionality = ItemFunctionality.Normal,
            enemyDamage = EnemyDamage.Default,
            enemyHealth = EnemyHealth.Default,
            pseudoboots = false
        )
    },
    SUPER_QUICK(Res.string.gamemode_super_quick) {
        override fun model() = RandomizerGameModel(
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
            potShuffle = Toggle.Off,
            hints = Toggle.Off,
            weapons = Weapons.Assured,
            itemPool = ItemPool.Normal,
            itemFunctionality = ItemFunctionality.Normal,
            enemyDamage = EnemyDamage.Default,
            enemyHealth = EnemyHealth.Default,
            pseudoboots = false
        )
    },
    NIGHTMARE(Res.string.gamemode_nightmare) {
        override fun model() = RandomizerGameModel(
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
            potShuffle = Toggle.Off,
            hints = Toggle.Off,
            weapons = Weapons.Swordless,
            itemPool = ItemPool.Expert,
            itemFunctionality = ItemFunctionality.Expert,
            enemyDamage = EnemyDamage.Random,
            enemyHealth = EnemyHealth.Expert,
            pseudoboots = false
        )
    },
    CUSTOM(Res.string.gamemode_custom) {
        override fun model() = RandomizerGameModel()
    };

    abstract fun model(): RandomizerGameModel
}
