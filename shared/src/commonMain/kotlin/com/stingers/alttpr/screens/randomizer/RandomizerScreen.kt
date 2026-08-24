package com.stingers.alttpr.screens.randomizer

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.generate_game
import alttpr.shared.generated.resources.generator_title
import alttpr.shared.generated.resources.select_preset
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.common.components.PageHeader
import com.stingers.alttpr.common.components.PageLoadingView
import com.stingers.alttpr.common.components.PrimaryButton
import com.stingers.alttpr.common.preferences.MenuPreference
import com.stingers.alttpr.model.RandomizerGameMode
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RandomizerScreen(viewModel: RandomizerViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    when {
        state.loading -> PageLoadingView()
        else -> RandomizerScreen(viewModel::processEvent)
    }
}

@Composable
fun RandomizerScreen(
    processEvent: (event: RandomizerEvent) -> Unit
) {
    var currentMode by mutableStateOf(RandomizerGameMode.DEFAULT)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = spacedBy(16.dp),
        contentPadding = PaddingValues(PREFERENCE_PADDING.dp)
    ) {
        item {
            PageHeader(Res.string.generator_title)
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            MenuPreference(
                Res.string.select_preset,
                currentItem = currentMode,
                items = RandomizerGameMode.entries,
                titleResForItem = { it.title }) {
                currentMode = it
            }
        }

        item {
            PrimaryButton(Res.string.generate_game) {
                processEvent(RandomizerEvent.GenerateGame(currentMode))
            }
        }
    }
}

class RandomizerStateParameterProvider : PreviewParameterProvider<RandomizerState> {
    override val values = sequenceOf(
        RandomizerState(
            loading = false
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun RandomizerScreenLightPreview(
    @PreviewParameter(RandomizerStateParameterProvider::class) item: RandomizerState
) {
    PreviewLightTheme {
        RandomizerScreen {}
    }
}

@Preview(showBackground = true)
@Composable
fun RandomizerScreenDarkPreview(
    @PreviewParameter(RandomizerStateParameterProvider::class) item: RandomizerState
) {
    PreviewDarkTheme {
        RandomizerScreen {}
    }
}
