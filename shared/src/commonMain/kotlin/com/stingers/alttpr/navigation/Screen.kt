package com.stingers.alttpr.navigation

import androidx.navigation3.runtime.NavKey
import com.stingers.alttpr.model.SeedEntity
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object UploadRom : Screen

    @Serializable
    data object Main : Screen

    @Serializable
    data class EditRom(val seed: SeedEntity) : Screen

    @Serializable
    data object Licenses : Screen

    @Serializable
    data object Randomizer : Screen

    @Serializable
    data object Sprites : Screen

    @Serializable
    data object Logs : Screen
}