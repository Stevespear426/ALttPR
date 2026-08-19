package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.drop_byte_bee
import alttpr.shared.generated.resources.drop_byte_good_bee
import alttpr.shared.generated.resources.drop_byte_heart
import alttpr.shared.generated.resources.drop_byte_one_rupee
import alttpr.shared.generated.resources.drop_byte_five_rupees
import alttpr.shared.generated.resources.drop_byte_twenty_rupees
import alttpr.shared.generated.resources.drop_byte_one_bomb
import alttpr.shared.generated.resources.drop_byte_four_bombs
import alttpr.shared.generated.resources.drop_byte_eight_bombs
import alttpr.shared.generated.resources.drop_byte_small_magic
import alttpr.shared.generated.resources.drop_byte_large_magic
import alttpr.shared.generated.resources.drop_byte_five_arrows
import alttpr.shared.generated.resources.drop_byte_ten_arrows
import alttpr.shared.generated.resources.drop_byte_fairy
import org.jetbrains.compose.resources.StringResource

enum class DropByte(val title: StringResource, val byteValue: Int) {
    Bee(Res.string.drop_byte_bee, 121),
    GoodBee(Res.string.drop_byte_good_bee, 178),
    Heart(Res.string.drop_byte_heart, 216),
    OneRupee(Res.string.drop_byte_one_rupee, 0),
    FiveRupees(Res.string.drop_byte_five_rupees, 0),
    TwentyRupees(Res.string.drop_byte_twenty_rupees, 0),
    OneBomb(Res.string.drop_byte_one_bomb, 0),
    FourBombs(Res.string.drop_byte_four_bombs, 0),
    EightBombs(Res.string.drop_byte_eight_bombs, 0),
    SmallMagic(Res.string.drop_byte_small_magic, 0),
    LargeMagic(Res.string.drop_byte_large_magic, 0),
    FiveArrows(Res.string.drop_byte_five_arrows, 0),
    TenArrows(Res.string.drop_byte_ten_arrows, 0),
    Fairy(Res.string.drop_byte_fairy, 0),
}
