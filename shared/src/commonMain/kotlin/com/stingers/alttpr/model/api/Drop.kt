package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.drop_random
import alttpr.shared.generated.resources.drop_bee_swarm
import alttpr.shared.generated.resources.drop_good_bee
import alttpr.shared.generated.resources.drop_heart
import alttpr.shared.generated.resources.drop_green_rupee
import alttpr.shared.generated.resources.drop_blue_rupee
import alttpr.shared.generated.resources.drop_red_rupee
import alttpr.shared.generated.resources.drop_one_bomb
import alttpr.shared.generated.resources.drop_four_bombs
import alttpr.shared.generated.resources.drop_eight_bombs
import alttpr.shared.generated.resources.drop_small_magic
import alttpr.shared.generated.resources.drop_full_magic
import alttpr.shared.generated.resources.drop_five_arrows
import alttpr.shared.generated.resources.drop_ten_arrows
import alttpr.shared.generated.resources.drop_fairy
import org.jetbrains.compose.resources.StringResource

enum class Drop(val title: StringResource, val value: String) {
    Random(Res.string.drop_random, "auto_fill"),
    BeeSwarm(Res.string.drop_bee_swarm, "Bee"),
    GoodBee(Res.string.drop_good_bee, "BeeGood"),
    Heart(Res.string.drop_heart, "Heart"),
    GreenRupee(Res.string.drop_green_rupee, "RupeeGreen"),
    BlueRupee(Res.string.drop_blue_rupee, "RupeeBlue"),
    RedRupee(Res.string.drop_red_rupee, "RupeeRed"),
    OneBomb(Res.string.drop_one_bomb, "BombRefill1"),
    FourBombs(Res.string.drop_four_bombs, "BombRefill4"),
    EightBombs(Res.string.drop_eight_bombs, "BombRefill8"),
    SmallMagic(Res.string.drop_small_magic, "MagicRefillSmall"),
    FullMagic(Res.string.drop_full_magic, "MagicRefillFull"),
    FiveArrows(Res.string.drop_five_arrows, "ArrowRefill5"),
    TenArrows(Res.string.drop_ten_arrows, "ArrowRefill10"),
    Fairy(Res.string.drop_fairy, "Fairy"),
}
