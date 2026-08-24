package com.stingers.alttpr.screens.dashboard

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.ic_mystery
import alttpr.shared.generated.resources.ic_trophy
import alttpr.shared.generated.resources.mystery_game
import alttpr.shared.generated.resources.race_game
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.stingers.alttpr.screens.seed.SeedItemView
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    when {
        state.loading -> PageLoadingView()
        else -> DashboardScreen(state, viewModel::processEvent)
    }
}

@Composable
fun DashboardScreen(state: DashboardState, processEvent: (event: DashboardEvent) -> Unit) {
    val haptic = LocalHapticFeedback.current

    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = {
            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
            processEvent(DashboardEvent.RefreshData)
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

            state.dailySeed?.let {
                item {
                    Text(
                        text = "Daily Challenge",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                item {
                    SeedItemView(it)
                }

                item {
                    Spacer(modifier = Modifier)
                }
            }

            item {
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = spacedBy(16.dp)) {
                    DashboardButtonView(
                        modifier = Modifier.weight(1f),
                        icon = Res.drawable.ic_mystery,
                        text = Res.string.mystery_game
                    ) {
                        processEvent(DashboardEvent.GenerateMysteryGame)
                    }

                    DashboardButtonView(
                        modifier = Modifier.weight(1f),
                        icon = Res.drawable.ic_trophy,
                        text = Res.string.race_game
                    ) {
                        processEvent(DashboardEvent.GenerateRaceGame)
                    }
                }
            }
            state.recentSeed?.let {
                item {
                    Spacer(modifier = Modifier)
                }
                item {
                    Text(
                        text = "Recent Game",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                item {
                    SeedItemView(it)
                }
            }
        }
    }
}

class DashboardStateParameterProvider : PreviewParameterProvider<DashboardState> {
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
        DashboardState(dailySeed = seed)
    )
}


@Preview(showBackground = true)
@Composable
fun GeneratorScreenLightPreview(
    @PreviewParameter(DashboardStateParameterProvider::class) item: DashboardState
) {
    PreviewLightTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            DashboardScreen(item) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GeneratorScreenDarkPreview(
    @PreviewParameter(DashboardStateParameterProvider::class) item: DashboardState
) {
    PreviewDarkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            DashboardScreen(item) {}
        }
    }
}
