package com.stingers.alttpr.screens.seed

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.ic_crystal
import alttpr.shared.generated.resources.ic_door
import alttpr.shared.generated.resources.ic_goal
import alttpr.shared.generated.resources.ic_key
import alttpr.shared.generated.resources.ic_logic
import alttpr.shared.generated.resources.ic_skull
import alttpr.shared.generated.resources.ic_swords
import alttpr.shared.generated.resources.ic_trophy
import alttpr.shared.generated.resources.ic_world
import alttpr.shared.generated.resources.mystery_game
import alttpr.shared.generated.resources.race
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.model.SpoilerMeta
import com.stingers.alttpr.model.SpoilerMetaParameterProvider
import com.stingers.alttpr.model.api.BossShuffle
import com.stingers.alttpr.model.api.Entrances
import com.stingers.alttpr.model.api.Keysanity
import com.stingers.alttpr.model.api.RomMode
import com.stingers.alttpr.model.api.Spoilers
import com.stingers.alttpr.model.api.Weapons
import com.stingers.alttpr.model.api.WorldState
import com.stingers.alttpr.model.api.toCrystal
import com.stingers.alttpr.model.api.toGoal
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import com.stingers.alttpr.utils.toEnumOrDefault
import com.stingers.alttpr.utils.toEnumOrNull
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SeedMetaView(meta: SpoilerMeta) {
    with(meta) {
        FlowRow(
            horizontalArrangement = spacedBy(8.dp),
            verticalArrangement = spacedBy(8.dp),
        ) {
            if (spoilers.toEnumOrDefault(Spoilers.Off) == Spoilers.Mystery) {
                MetaChip(
                    title = Res.string.mystery_game,
                    icon = Res.drawable.ic_logic
                )
            }

            romMode.toEnumOrNull<RomMode>()?.let {
                MetaChip(
                    title = it.title,
                    icon = Res.drawable.ic_logic
                )
            }

            mode.toEnumOrNull<WorldState>()?.let {
                MetaChip(
                    title = it.title,
                    icon = Res.drawable.ic_world
                )
            }

            goal?.toGoal()?.let {
                MetaChip(
                    title = it.title,
                    icon = Res.drawable.ic_goal
                )
            }

            weapons.toEnumOrNull<Weapons>()?.let {
                MetaChip(
                    title = it.title,
                    icon = Res.drawable.ic_swords
                )
            }

            entryCrystalsTower?.toCrystal()?.let {
                MetaChip(
                    title = "Tower: ${stringResource(it.title)}",
                    icon = Res.drawable.ic_crystal
                )
            }
            entryCrystalsGanon?.toCrystal()?.let {
                MetaChip(
                    title = "Ganon: ${stringResource(it.title)}",
                    icon = Res.drawable.ic_crystal
                )
            }
            dungeonItems.toEnumOrNull<Keysanity>()?.let {
                MetaChip(
                    title = it.title,
                    icon = Res.drawable.ic_key
                )
            }
            enemizerBossShuffle.toEnumOrNull<BossShuffle>()?.let {
                MetaChip(
                    title = it.title,
                    icon = Res.drawable.ic_skull
                )
            }
            entranceShuffle.toEnumOrNull<Entrances>()?.let {
                MetaChip(
                    title = it.title,
                    icon = Res.drawable.ic_door
                )
            }

            if (tournament) {
                MetaChip(
                    title = Res.string.race,
                    icon = Res.drawable.ic_trophy
                )
            }
        }
    }
}

@Composable
fun MetaChip(title: StringResource, icon: DrawableResource) {
    MetaChip(
        stringResource(title),
        icon
    )
}

@Composable
fun MetaChip(title: String, icon: DrawableResource) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(painterResource(icon), contentDescription = "")
        Text(
            title,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview
@Composable
fun SeedMetaViewLightPreview(
    @PreviewParameter(SpoilerMetaParameterProvider::class) item: SpoilerMeta
) {
    PreviewLightTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SeedMetaView(item)
        }
    }
}

@Preview
@Composable
fun SeedMetaViewDarkPreview(
    @PreviewParameter(SpoilerMetaParameterProvider::class) item: SpoilerMeta
) {
    PreviewDarkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SeedMetaView(item)
        }
    }
}