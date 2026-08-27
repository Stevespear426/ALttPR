package com.stingers.alttpr.screens.seed

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.generated
import alttpr.shared.generated.resources.ic_delete
import alttpr.shared.generated.resources.ic_download
import alttpr.shared.generated.resources.ic_edit
import alttpr.shared.generated.resources.ic_link
import alttpr.shared.generated.resources.ic_play_arrow
import alttpr.shared.generated.resources.ic_save
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.BASE_ROM_FILENAME
import com.stingers.alttpr.common.PERMA_LINK
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.common.components.HashCodeRow
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.model.SpoilerMeta
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SeedItemView(
    seed: SeedEntity,
) {
    val viewModel: SeedItemViewModel =
        koinViewModel(key = seed.hash, parameters = { parametersOf(seed) })

    val state by viewModel.state.collectAsState()
    SeedItemView(state, viewModel::processEvent)
}

@Composable
fun SeedItemView(
    state: SeedItemState,
    processEvent: (event: SeedItemEvent) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    with(state.seed) {
        Card(modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(PREFERENCE_PADDING.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    getHashCode()?.let {
                        HashCodeRow(
                            modifier = Modifier.weight(1f),
                            list = it,
                            iconSize = 42
                        )
                    } ?: run {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(Res.string.generated),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = LocalContentColor.current.copy(alpha = 0.6f)
                        )
                        Text(
                            text = getGeneratedDate(),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = hash,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = LocalContentColor.current.copy(alpha = 0.6f)
                        )
                    }
                }

                meta?.let {
                    Text(
                        text = it.getFileName(),
                        style = MaterialTheme.typography.titleLargeEmphasized.copy(fontWeight = FontWeight.Bold)
                    )

                    SeedMetaView(it)
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = PREFERENCE_PADDING.dp)
            )
            Row(modifier = Modifier.padding(PREFERENCE_PADDING.dp)) {

                FilledIconButton(
                    onClick = {
                        processEvent(SeedItemEvent.PlaySeed)
                    }) {
                    Icon(
                        painterResource(Res.drawable.ic_play_arrow),
                        contentDescription = "play button",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                FilledIconButton(
                    onClick = {
                        processEvent(SeedItemEvent.ExportRom)
                    }) {
                    Icon(
                        painterResource(Res.drawable.ic_download),
                        contentDescription = "download button",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                FilledIconButton(
                    onClick = {
                        processEvent(SeedItemEvent.OpenEditSeed)
                    }) {
                    Icon(
                        painterResource(Res.drawable.ic_edit),
                        contentDescription = "edit button",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                FilledIconButton(
                    onClick = {
                        uriHandler.openUri(uri = PERMA_LINK + hash)
                    }) {
                    Icon(
                        painterResource(Res.drawable.ic_link),
                        contentDescription = "permalink button",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Crossfade(targetState = state.isSaved, label = "cross fade") { saved ->
                    when (saved) {
                        true -> {
                            FilledIconButton(
                                onClick = {
                                    processEvent(SeedItemEvent.RemoveSeed)
                                }) {
                                Icon(
                                    painterResource(Res.drawable.ic_delete),
                                    contentDescription = "delete button",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        else -> {
                            FilledIconButton(
                                onClick = {
                                    processEvent(SeedItemEvent.SaveSeed)
                                }) {
                                Icon(
                                    painterResource(Res.drawable.ic_save),
                                    contentDescription = "Save button",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

class RomEntityParameterProvider : PreviewParameterProvider<SeedItemState> {
    val seed = SeedEntity(
        hash = "1234",
        md5 = "12412",
        localFileName = BASE_ROM_FILENAME,
        logic = "NoGlitch",
        generated = "2026-08-17T00:01:00+00:00",
        patch = listOf(mapOf("1573397" to listOf(1, 2, 3, 4, 5))),
        meta = SpoilerMeta(
            name = "Daily Challenge: Aug 17",
            build = "2023-09-22",
            accessibility = "none",
            mode = "open",
            weapons = "vanilla",
            goal = "fast_ganon",
            logic = "NoGlitches"
        ),
    )
    override val values = sequenceOf(
        SeedItemState(seed = seed)
    )
}


@Preview(showBackground = true)
@Composable
fun SeedItemViewLightPreview(
    @PreviewParameter(RomEntityParameterProvider::class) item: SeedItemState
) {
    PreviewLightTheme {
        Surface {
            SeedItemView(item) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SeedItemViewDarkPreview(
    @PreviewParameter(RomEntityParameterProvider::class) item: SeedItemState
) {
    PreviewDarkTheme {
        Surface {
            SeedItemView(item) {}
        }
    }
}
