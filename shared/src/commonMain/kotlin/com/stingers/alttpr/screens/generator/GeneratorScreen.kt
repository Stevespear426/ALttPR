package com.stingers.alttpr.screens.generator

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.BASE_ROM_FILENAME
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.common.components.PageLoadingView
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.model.SpoilerMeta
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.screens.seed.SeedItemView
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GeneratorScreen(viewModel: GeneratorViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    when {
        state.loading -> PageLoadingView()
        else -> GeneratorScreen(state, viewModel::processEvent)
    }
}

@Composable
fun GeneratorScreen(state: GeneratorState, processEvent: (event: GeneratorEvent) -> Unit) {
    val haptic = LocalHapticFeedback.current

    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = {
            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
            processEvent(GeneratorEvent.RefreshData)
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = spacedBy(16.dp),
            contentPadding = PaddingValues(PREFERENCE_PADDING.dp)
        ) {

            item {
                Text(
                    text = "ALTTPR Generator",
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                state.dailySeed?.let {
                    SeedItemView(it)
                }
            }

            item {
                RandomChallengeView {
                    processEvent(GeneratorEvent.GenerateRandom)
                }
            }

            item {
                GenerateRandomizedView {
                    processEvent(GeneratorEvent.NavigateTo(Screen.Randomizer))
                }
            }
        }
    }
}

class GeneratorStateParameterProvider : PreviewParameterProvider<GeneratorState> {
    val seed = SeedEntity(
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
    override val values = sequenceOf(
        GeneratorState(dailySeed = seed)
    )
}


@Preview(showBackground = true)
@Composable
fun GeneratorScreenLightPreview(
    @PreviewParameter(GeneratorStateParameterProvider::class) item: GeneratorState
) {
    PreviewLightTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GeneratorScreen(item) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GeneratorScreenDarkPreview(
    @PreviewParameter(GeneratorStateParameterProvider::class) item: GeneratorState
) {
    PreviewDarkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GeneratorScreen(item) {}
        }
    }
}
