package com.stingers.alttpr.screens.randomizer

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.randomizer
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.common.components.HeaderPage
import com.stingers.alttpr.common.components.PageLoadingView
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RandomizerScreen(viewModel: RandomizerViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    when {
        state.loading -> PageLoadingView()
        else -> HeaderPage(stringResource(Res.string.randomizer)) {
            Box(modifier = Modifier.padding(it)) {
                RandomizerScreen(state, viewModel::processEvent)
            }
        }
    }
}

@Composable
fun RandomizerScreen(
    randomizerState: RandomizerState,
    processEvent: (event: RandomizerEvent) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = spacedBy(16.dp),
        contentPadding = PaddingValues(PREFERENCE_PADDING.dp)
    ) {
        item {
            Text("RandomizerScreen")
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
        RandomizerScreen(item) {}
    }
}

@Preview(showBackground = true)
@Composable
fun RandomizerScreenDarkPreview(
    @PreviewParameter(RandomizerStateParameterProvider::class) item: RandomizerState
) {
    PreviewDarkTheme {
        RandomizerScreen(item) {}
    }
}
