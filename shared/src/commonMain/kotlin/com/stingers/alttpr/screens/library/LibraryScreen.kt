package com.stingers.alttpr.screens.library

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.stingers.alttpr.common.SETTINGS_PREFERENCE_PADDING
import com.stingers.alttpr.model.RomEntity
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LibraryScreen(viewModel: LibraryViewModel = koinViewModel()) {
    val pagingItems = viewModel.romsFlow.collectAsLazyPagingItems()
    LibraryScreen(pagingItems, viewModel::processEvent)
}

@Composable
fun LibraryScreen(
    pagingItems: LazyPagingItems<RomEntity>,
    processEvent: (event: LibraryEvent) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = SETTINGS_PREFERENCE_PADDING, verticalArrangement = spacedBy(16.dp)) {
        items(
            count = pagingItems.itemCount,
            key = { index -> pagingItems[index]?.hash ?: index }
        ) { index ->
            val seed = pagingItems[index]
            if (seed != null) {
                SeedItemView(seed)
            }
        }
    }
}
