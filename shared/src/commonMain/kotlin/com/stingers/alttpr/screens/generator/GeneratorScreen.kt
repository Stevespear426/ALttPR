package com.stingers.alttpr.screens.generator

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.common.components.PageLoadingView
import com.stingers.alttpr.navigation.Screen
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GeneratorScreen(viewModel: GeneratorViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    when {
        state.loading -> PageLoadingView()
        else -> GeneratorScreen(viewModel::processEvent)
    }
}

@Composable
fun GeneratorScreen(processEvent: (event: GeneratorEvent) -> Unit) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = spacedBy(16.dp),
        contentPadding = PaddingValues(PREFERENCE_PADDING.dp)
    ) {
        item {
            CreateDailyView {
                processEvent(GeneratorEvent.GenerateDaily)
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

@Preview(showBackground = true)
@Composable
fun GeneratorScreenLightPreview() {
    PreviewLightTheme {
        GeneratorScreen {}
    }
}

@Preview(showBackground = true)
@Composable
fun GeneratorScreenDarkPreview() {
    PreviewDarkTheme {
        GeneratorScreen {}
    }
}
