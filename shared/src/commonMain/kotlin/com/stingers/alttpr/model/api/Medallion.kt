package com.stingers.alttpr.model.api

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.hash_bombos
import alttpr.shared.generated.resources.hash_ether
import alttpr.shared.generated.resources.hash_quake
import org.jetbrains.compose.resources.StringResource

enum class Medallion(val title: StringResource, val value: String) {
    Bombos(Res.string.hash_bombos, "Bombos"),
    Ether(Res.string.hash_ether, "Ether"),
    Quake(Res.string.hash_quake, "Quake"),
}
