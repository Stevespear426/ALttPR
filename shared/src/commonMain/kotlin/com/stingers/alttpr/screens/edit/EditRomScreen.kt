package com.stingers.alttpr.screens.edit

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.audio_visual
import alttpr.shared.generated.resources.background_music_title
import alttpr.shared.generated.resources.gameplay_toggles
import alttpr.shared.generated.resources.generated_on
import alttpr.shared.generated.resources.hash_code
import alttpr.shared.generated.resources.heart_color_title
import alttpr.shared.generated.resources.heart_speed_title
import alttpr.shared.generated.resources.ic_delete
import alttpr.shared.generated.resources.ic_download
import alttpr.shared.generated.resources.ic_play_arrow
import alttpr.shared.generated.resources.ic_reload
import alttpr.shared.generated.resources.ic_save
import alttpr.shared.generated.resources.menu_speed_title
import alttpr.shared.generated.resources.msu_resume_title
import alttpr.shared.generated.resources.permalink
import alttpr.shared.generated.resources.quick_swap_title
import alttpr.shared.generated.resources.reduce_flashing_title
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stingers.alttpr.common.BASE_ROM_FILENAME
import com.stingers.alttpr.common.PERMA_LINK
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.common.components.HashCodeRow
import com.stingers.alttpr.common.components.HeaderPage
import com.stingers.alttpr.common.components.PageLoadingView
import com.stingers.alttpr.common.preferences.MenuPreference
import com.stingers.alttpr.common.preferences.SpritePreference
import com.stingers.alttpr.common.preferences.SwitchPreference
import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.model.HeartSpeed
import com.stingers.alttpr.model.MenuSpeed
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.model.SpoilerMeta
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.platform.ic_back
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditRomScreen(
    seed: SeedEntity,
) {
    val viewModel: EditRomViewModel =
        koinViewModel(key = seed.hash, parameters = { parametersOf(seed) })
    val state by viewModel.state.collectAsState()
    when {
        state.loading -> PageLoadingView()
        else -> EditRomScreen(state, viewModel::processEvent)
    }
}

@Composable
fun EditRomTopBar(
    seed: SeedEntity,
    isSaved: Boolean,
    processEvent: (event: EditRomEvent) -> Unit,
) {
    with(seed) {
        val title = meta?.getFileName().orEmpty()
        val isPreview = LocalInspectionMode.current
        val navigationManager: NavigationManager? = if (!isPreview) koinInject() else null
        TopAppBar(
            title = {
                Text(
                    text = title,
                    maxLines = 2,
                    autoSize = TextAutoSize.StepBased(minFontSize = 16.sp, maxFontSize = 24.sp)
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        navigationManager?.pop()
                    }
                ) {
                    Icon(
                        painterResource(Res.drawable.ic_back),
                        contentDescription = "Close Button"
                    )
                }
            },
            actions = {
                if (request != null) {
                    IconButton({
                        processEvent(EditRomEvent.ReRollSeed)
                    }) {
                        Icon(
                            painterResource(Res.drawable.ic_reload),
                            contentDescription = "Reroll Button"
                        )
                    }
                }

                IconButton({
                    processEvent(EditRomEvent.PlaySeed)
                }) {
                    Icon(
                        painterResource(Res.drawable.ic_play_arrow),
                        contentDescription = "Play Button"
                    )
                }

                IconButton({
                    processEvent(EditRomEvent.ExportRom)
                }) {
                    Icon(
                        painterResource(Res.drawable.ic_download),
                        contentDescription = "Share Button"
                    )
                }

                Crossfade(targetState = isSaved, label = "cross fade") { saved ->
                    when (saved) {
                        true -> {
                            IconButton(
                                onClick = {
                                    processEvent(EditRomEvent.DeleteSeed)
                                }) {
                                Icon(
                                    painterResource(Res.drawable.ic_delete),
                                    contentDescription = "delete button",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        else -> {
                            IconButton(
                                onClick = {
                                    processEvent(EditRomEvent.SaveSeed)
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
        )
    }
}


@Composable
fun EditRomScreen(
    state: EditRomState,
    processEvent: (event: EditRomEvent) -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    with(state) {
        HeaderPage(
            title = "",
            topBar = { EditRomTopBar(seed, isSaved, processEvent) }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = spacedBy(16.dp),
                contentPadding = PaddingValues(PREFERENCE_PADDING.dp)
            ) {

                seed.getHashCode()?.let {
                    item {
                        OutlinedCard {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp).padding(top = 4.dp),
                                text = stringResource(Res.string.hash_code),
                                fontWeight = FontWeight.Bold
                            )
                            HashCodeRow(
                                list = it,
                                iconSize = 42,
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            )
                        }
                    }
                }

                seed.meta?.let {
                    item { SpoilerMetaView(it) }
                }

                item {
                    SpritePreference(
                        currentItem = selectedSprite,
                    ) {
                        processEvent(EditRomEvent.OpenSpriteSelector)
                    }
                }

                item {
                    Spacer(Modifier)
                }
                item {
                    Text(
                        text = stringResource(Res.string.audio_visual),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                if (seed.meta?.tournament == false) {
                    item {
                        MenuPreference(
                            Res.string.menu_speed_title,
                            currentItem = menuSpeed,
                            items = MenuSpeed.entries,
                            titleResForItem = { it.title }) {
                            processEvent(EditRomEvent.SetMenuSpeed(it))
                        }
                    }
                }
                item {

                    MenuPreference(
                        Res.string.heart_speed_title,
                        currentItem = heartSpeed,
                        items = HeartSpeed.entries,
                        titleResForItem = { it.title }) {
                        processEvent(EditRomEvent.SetHeartSpeed(it))
                    }
                }
                item {
                    MenuPreference(
                        Res.string.heart_color_title,
                        currentItem = heartColor,
                        items = HeartColor.entries,
                        titleResForItem = { it.title }) {
                        processEvent(EditRomEvent.SetHeartColor(it))
                    }
                }

                item {
                    Spacer(Modifier)
                }
                item {
                    Text(
                        text = stringResource(Res.string.gameplay_toggles),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                item {
                    SwitchPreference(
                        title = Res.string.background_music_title,
                        checked = enableMusic
                    ) {
                        processEvent(EditRomEvent.SetEnableMusic(it))
                    }
                }
                item {

                    SwitchPreference(
                        title = Res.string.quick_swap_title,
                        checked = quickSwap
                    ) {
                        processEvent(EditRomEvent.SetQuickSwap(it))
                    }
                }
                item {
                    SwitchPreference(
                        title = Res.string.msu_resume_title,
                        checked = msuResume,
                    ) {
                        processEvent(EditRomEvent.SetMsuResume(it))
                    }
                }
                item {

                    SwitchPreference(
                        title = Res.string.reduce_flashing_title,
                        checked = reduceFlashing
                    ) {
                        processEvent(EditRomEvent.SetReduceFlashing(it))
                    }
                }

                item {
                    Text(
                        text = stringResource(Res.string.generated_on, seed.getGeneratedDate()),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                item {
                    val permalink = stringResource(Res.string.permalink)
                    val link = PERMA_LINK + seed.hash
                    val annotatedLinkString: AnnotatedString = remember(seed.hash) {
                        buildAnnotatedString {

                            val style = SpanStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )

                            val styleCenter = SpanStyle(
                                color = Color(0xff64B5F6),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )

                            withStyle(style = style) {
                                append(permalink)
                            }

                            withLink(LinkAnnotation.Url(url = link)) {
                                withStyle(
                                    style = styleCenter
                                ) {
                                    append(link)
                                }
                            }
                        }
                    }
                    Text(annotatedLinkString)
                }
            }
        }
    }
}

class EditRomStateParameterProvider : PreviewParameterProvider<EditRomState> {
    override val values = sequenceOf(
        EditRomState(
            loading = true,
            seed = SeedEntity(
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
        )
    )
}

@Preview
@Composable
fun EditRomScreenLightPreview(
    @PreviewParameter(EditRomStateParameterProvider::class) item: EditRomState
) {
    PreviewLightTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            EditRomScreen(item) {}
        }
    }
}

@Preview
@Composable
fun EditRomScreenDarkPreview(
    @PreviewParameter(EditRomStateParameterProvider::class) item: EditRomState
) {
    PreviewDarkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            EditRomScreen(item) {}
        }
    }
}
