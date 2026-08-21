package com.stingers.alttpr.screens.edit

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.background_music_title
import alttpr.shared.generated.resources.heart_color_title
import alttpr.shared.generated.resources.heart_speed_title
import alttpr.shared.generated.resources.ic_close
import alttpr.shared.generated.resources.ic_download
import alttpr.shared.generated.resources.ic_reload
import alttpr.shared.generated.resources.ic_save
import alttpr.shared.generated.resources.menu_speed_title
import alttpr.shared.generated.resources.msu_resume_title
import alttpr.shared.generated.resources.play_seed
import alttpr.shared.generated.resources.quick_swap_title
import alttpr.shared.generated.resources.reduce_flashing_title
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.BASE_ROM_FILENAME
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.common.components.HashCodeRow
import com.stingers.alttpr.common.components.HeaderPage
import com.stingers.alttpr.common.components.PageLoadingView
import com.stingers.alttpr.common.components.PrimaryButton
import com.stingers.alttpr.common.preferences.MenuPreference
import com.stingers.alttpr.common.preferences.SpritePreference
import com.stingers.alttpr.common.preferences.SwitchPreference
import com.stingers.alttpr.model.HeartColor
import com.stingers.alttpr.model.HeartSpeed
import com.stingers.alttpr.model.MenuSpeed
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.model.SpoilerMeta
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.painterResource
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
    processEvent: (event: EditRomEvent) -> Unit,
) {
    with(seed) {
        val title = meta?.getFileName().orEmpty()
        val isPreview = LocalInspectionMode.current
        val navigationManager: NavigationManager? = if (!isPreview) koinInject() else null
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                IconButton(
                    onClick = {
                        navigationManager?.pop()
                    }
                ) {
                    Icon(
                        painterResource(Res.drawable.ic_close),
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
                    processEvent(EditRomEvent.SaveSeed)
                }) {
                    Icon(
                        painterResource(Res.drawable.ic_save),
                        contentDescription = "Save Button"
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
            }
        )
    }
}


@Composable
fun EditRomScreen(
    state: EditRomState,
    processEvent: (event: EditRomEvent) -> Unit,
) {
    with(state) {
        HeaderPage(
            title = "",
            topBar = { EditRomTopBar(seed, processEvent) }
        ) { padding ->
            val textModifier = Modifier.padding(horizontal = PREFERENCE_PADDING.dp)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = spacedBy(4.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {

                seed.getHashCode()?.let {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                modifier = textModifier,
                                text = "Hash Code:",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            HashCodeRow(it)
                        }
                    }
                }

                seed.meta?.let {
                    item { SpoilerMetaView(it) }
                }
                item {
                    Text(
                        modifier = textModifier,
                        text = "Created: ${seed.generated}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                if (availableSprites.isNotEmpty()) {
                    item {
                        SpritePreference(
                            currentItem = selectedSprite,
                            items = availableSprites
                        ) {
                            processEvent(EditRomEvent.SetSprite(it))
                        }
                    }
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
                    PrimaryButton(Res.string.play_seed) {
                        processEvent(EditRomEvent.PlaySeed)
                    }
                }
//                item {
//                    SwitchPreference(
//                        title = Res.string.pallet_shuffle_title,
//                        checked = false,
//                    ) {
//                    }
//                }
//
//                item {
//                    SwitchPreference(
//                        title = Res.string.shuffle_sfx_title,
//                        checked = false,
//                    ) {
//                    }
//                }
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
