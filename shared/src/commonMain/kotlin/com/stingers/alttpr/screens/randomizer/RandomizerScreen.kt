package com.stingers.alttpr.screens.randomizer

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.difficulty
import alttpr.shared.generated.resources.gameplay
import alttpr.shared.generated.resources.generate_game
import alttpr.shared.generated.resources.generate_race
import alttpr.shared.generated.resources.generator_title
import alttpr.shared.generated.resources.goals
import alttpr.shared.generated.resources.ic_info
import alttpr.shared.generated.resources.item_placement
import alttpr.shared.generated.resources.select_preset
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.GAME_PLAY_INFO_URL
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.common.components.PageHeader
import com.stingers.alttpr.common.components.PageLoadingView
import com.stingers.alttpr.common.components.TabContentView
import com.stingers.alttpr.common.preferences.MenuPreference
import com.stingers.alttpr.model.RandomizerGameMode
import com.stingers.alttpr.screens.sprites.SpriteEvent
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RandomizerScreen(viewModel: RandomizerViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    when {
        state.loading -> PageLoadingView()
        else -> RandomizerScreen(state, viewModel::processEvent)
    }
}

@Composable
fun RandomizerScreen(
    state: RandomizerState,
    processEvent: (event: RandomizerEvent) -> Unit
) {
    val urlHandler = LocalUriHandler.current
    with(state) {
        val isCustom = preset == RandomizerGameMode.CUSTOM
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = spacedBy(16.dp),
            contentPadding = PaddingValues(PREFERENCE_PADDING.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PageHeader(Res.string.generator_title)
                    IconButton({
                        urlHandler.openUri(GAME_PLAY_INFO_URL)
                    }) {
                        Icon(
                            painterResource(Res.drawable.ic_info),
                            contentDescription = "Options Button"
                        )
                    }
                }
            }
            item {
                Row(horizontalArrangement = spacedBy(4.dp)) {
                    val buttonModifier = Modifier.weight(1f)
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = buttonModifier,
                        onClick = {
                            processEvent(RandomizerEvent.GenerateGame)
                        }) {
                        Text(stringResource(Res.string.generate_game))
                    }
                    OutlinedButton(
                        modifier = buttonModifier,
                        onClick = {
                            processEvent(RandomizerEvent.GenerateRace)
                        }) {
                        Text(stringResource(Res.string.generate_race))
                    }
                }
            }

            item {
                Text(
                    text = "Game Options",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }
            item {
                MenuPreference(
                    Res.string.select_preset,
                    currentItem = preset,
                    items = RandomizerGameMode.entries,
                    titleResForItem = { it.title }) {
                    processEvent(RandomizerEvent.SetPreset(it))
                }
            }
            item {
                OutlinedTextField(
                    value = settings.name,
                    onValueChange = { processEvent(RandomizerEvent.SetName(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Seed Name") },
                    placeholder = { Text("Seed Name") },
                    singleLine = true,
                    shape = CircleShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = MaterialTheme.colorScheme.secondary,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        disabledBorderColor = MaterialTheme.colorScheme.secondary,
                    ),
                )
            }
            item {
                val tabs = listOf(
                    stringResource(Res.string.goals),
                    stringResource(Res.string.item_placement),
                    stringResource(Res.string.gameplay),
                    stringResource(Res.string.difficulty)
                )
                TabContentView(
                    Modifier.fillMaxWidth().wrapContentSize(),
                    tabs
                ) { page ->
                    when (page) {
                        0 -> GoalsScreen(settings, isCustom, processEvent)
                        1 -> ItemsScreen(settings, isCustom, processEvent)
                        2 -> GameplayScreen(settings, isCustom, processEvent)
                        3 -> DifficultyScreen(settings, isCustom, processEvent)
                    }
                }
            }

        }
    }
}

class RandomizerStateParameterProvider : PreviewParameterProvider<RandomizerState> {
    override val values = sequenceOf(
        RandomizerState(
            loading = false
        ),
        RandomizerState(
            loading = false,
            preset = RandomizerGameMode.CUSTOM
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun RandomizerScreenLightPreview(
    @PreviewParameter(RandomizerStateParameterProvider::class) state: RandomizerState
) {
    PreviewLightTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            RandomizerScreen(state) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RandomizerScreenDarkPreview(
    @PreviewParameter(RandomizerStateParameterProvider::class) state: RandomizerState
) {
    PreviewDarkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            RandomizerScreen(state) {}
        }
    }
}
