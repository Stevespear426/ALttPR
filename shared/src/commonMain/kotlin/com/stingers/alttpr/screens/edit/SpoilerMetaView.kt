package com.stingers.alttpr.screens.edit

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.boss_shuffle
import alttpr.shared.generated.resources.dungeon_item_shuffle
import alttpr.shared.generated.resources.enemy_shuffle
import alttpr.shared.generated.resources.entrance_shuffle
import alttpr.shared.generated.resources.ganon_vulnerable
import alttpr.shared.generated.resources.goals
import alttpr.shared.generated.resources.logic
import alttpr.shared.generated.resources.mystery_game
import alttpr.shared.generated.resources.open_tower
import alttpr.shared.generated.resources.swords
import alttpr.shared.generated.resources.world_state
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.model.SpoilerMeta
import com.stingers.alttpr.model.SpoilerMetaParameterProvider
import com.stingers.alttpr.model.api.BossShuffle
import com.stingers.alttpr.model.api.EnemyShuffle
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
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun SpoilerMetaView(meta: SpoilerMeta) {
    with(meta) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = spacedBy(8.dp),
            verticalArrangement = spacedBy(8.dp),
            maxItemsInEachRow = 2
        ) {
            if (spoilers.toEnumOrDefault(Spoilers.Off) == Spoilers.Mystery) {
                MetaChip(
                    modifier = Modifier.weight(1f),
                    title = Res.string.mystery_game,
                    header = Res.string.logic,
                )
            }

            romMode.toEnumOrNull<RomMode>()?.let {
                MetaChip(
                    modifier = Modifier.weight(1f),
                    title = it.title,
                    header = Res.string.logic,
                )
            }

            mode.toEnumOrNull<WorldState>()?.let {
                MetaChip(
                    modifier = Modifier.weight(1f),
                    title = it.title,
                    header = Res.string.world_state,
                )
            }

            goal?.toGoal()?.let {
                MetaChip(
                    modifier = Modifier.weight(1f),
                    title = it.title,
                    header = Res.string.goals,
                )
            }

            weapons.toEnumOrNull<Weapons>()?.let {
                MetaChip(
                    modifier = Modifier.weight(1f),
                    title = it.title,
                    header = Res.string.swords,
                )
            }

            entryCrystalsTower?.toCrystal()?.let {
                MetaChip(
                    modifier = Modifier.weight(1f),
                    title = it.title,
                    header = Res.string.open_tower,
                )
            }
            entryCrystalsGanon?.toCrystal()?.let {
                MetaChip(
                    modifier = Modifier.weight(1f),
                    title = it.title,
                    header = Res.string.ganon_vulnerable,
                )
            }
            dungeonItems.toEnumOrNull<Keysanity>()?.let {
                MetaChip(
                    modifier = Modifier.weight(1f),
                    title = it.title,
                    header = Res.string.dungeon_item_shuffle,
                )
            }
            enemizerBossShuffle.toEnumOrNull<BossShuffle>()?.let {
                MetaChip(
                    modifier = Modifier.weight(1f),
                    title = it.title,
                    header = Res.string.boss_shuffle,
                )
            }
            enemizerEnemyShuffle.toEnumOrNull<EnemyShuffle>()?.let {
                MetaChip(
                    modifier = Modifier.weight(1f),
                    title = it.title,
                    header = Res.string.enemy_shuffle,
                )
            }
            entranceShuffle.toEnumOrNull<Entrances>()?.let {
                MetaChip(
                    modifier = Modifier.weight(1f),
                    title = it.title,
                    header = Res.string.entrance_shuffle,
                )
            }

//            if (tournament) {
//                MetaChip(
//                    modifier = Modifier.weight(1f),
//                    title = Res.string.race,
//                    header = Res.string.race,
//                )
//            }
        }
    }
}


@Composable
fun MetaChip(
    modifier: Modifier,
    header: StringResource,
    title: StringResource
) {
    val locale = LocalLocale.current
    OutlinedCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(header).capitalize(locale),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = LocalContentColor.current.copy(alpha = 0.75f)
            )
            Text(
                text = stringResource(title),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Preview
@Composable
fun SpoilerMetaViewLightPreview(
    @PreviewParameter(SpoilerMetaParameterProvider::class) item: SpoilerMeta
) {
    PreviewLightTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SpoilerMetaView(item)
        }
    }
}

@Preview
@Composable
fun SpoilerMetaViewDarkPreview(
    @PreviewParameter(SpoilerMetaParameterProvider::class) item: SpoilerMeta
) {
    PreviewDarkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SpoilerMetaView(item)
        }
    }
}