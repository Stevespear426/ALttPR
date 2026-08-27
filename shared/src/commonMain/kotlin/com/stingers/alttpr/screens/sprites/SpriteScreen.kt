package com.stingers.alttpr.screens.sprites

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.ic_next
import alttpr.shared.generated.resources.search_sprites
import alttpr.shared.generated.resources.select_sprite
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.common.components.HeaderPage
import com.stingers.alttpr.common.components.PageLoadingView
import com.stingers.alttpr.model.Sprite
import com.stingers.alttpr.navigation.NavigationManager
import com.stingers.alttpr.platform.ic_back
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SpriteScreen() {
    val viewModel: SpriteViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    when {
        state.loading -> PageLoadingView()
        else -> SpriteScreen(state, viewModel::processEvent)
    }
}

@Composable
fun SpriteTopBar() {
    val navigationManager: NavigationManager = koinInject()
    TopAppBar(
        title = { Text(stringResource(Res.string.select_sprite)) },
        navigationIcon = {
            IconButton(
                onClick = {
                    navigationManager.pop()
                }
            ) {
                Icon(
                    painterResource(Res.drawable.ic_back),
                    contentDescription = "Back Button"
                )
            }
        }
    )
}

@Composable
fun SpriteScreen(
    state: SpriteState,
    processEvent: (event: SpriteEvent) -> Unit
) {
    HeaderPage(
        title = "",
        topBar = { SpriteTopBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { processEvent(SpriteEvent.UpdateSearchQuery(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PREFERENCE_PADDING.dp, vertical = 8.dp),
                placeholder = { Text(stringResource(Res.string.search_sprites)) },
                singleLine = true,
                shape = CircleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    disabledBorderColor = MaterialTheme.colorScheme.secondary,
                ),
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = spacedBy(12.dp),
                horizontalArrangement = spacedBy(12.dp),
                contentPadding = PaddingValues(PREFERENCE_PADDING.dp)
            ) {
                items(state.sprites) { sprite ->
                    val isSelected = state.selectedSprite?.name == sprite.name
                    SpriteCard(
                        sprite = sprite,
                        isSelected = isSelected
                    ) {
                        processEvent(SpriteEvent.SelectSprite(sprite))
                    }
                }
            }
        }
    }
}

@Composable
fun SpriteCard(
    sprite: Sprite,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.secondary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = sprite.previewUrl ?: sprite.fileUrl,
                    contentDescription = sprite.name,
                    modifier = Modifier.padding(16.dp).fillMaxSize()
                )
            }
            Text(
                text = sprite.name.ifBlank { "Default" },
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

class SpriteStateParameterProvider : PreviewParameterProvider<SpriteState> {
    override val values = sequenceOf(
        SpriteState(
            loading = false,
            sprites = listOf(
                Sprite(fileUrl = "url1", name = "Link", previewUrl = "https://alttpr-assets.s3.us-east-2.amazonaws.com/001.link.1.zspr.png"),
                Sprite(fileUrl = "url2", name = "Samus", previewUrl = "https://alttpr-assets.s3.us-east-2.amazonaws.com/001.link.1.zspr.png"),
                Sprite(fileUrl = "url3", name = "Mario", previewUrl = "https://alttpr-assets.s3.us-east-2.amazonaws.com/001.link.1.zspr.png")
            ),
            selectedSprite = Sprite(fileUrl = "url1", name = "Link", previewUrl = null)
        )
    )
}

@Preview
@Composable
fun SpriteScreenLightPreview(
    @PreviewParameter(SpriteStateParameterProvider::class) item: SpriteState
) {
    PreviewLightTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SpriteScreen(item) {}
        }
    }
}

@Preview
@Composable
fun SpriteScreenDarkPreview(
    @PreviewParameter(SpriteStateParameterProvider::class) item: SpriteState
) {
    PreviewDarkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SpriteScreen(item) {}
        }
    }
}
