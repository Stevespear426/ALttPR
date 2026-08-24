package com.stingers.alttpr.screens.library

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.library_title
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.common.components.PageHeader
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.screens.seed.SeedItemView
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LibraryScreen(viewModel: LibraryViewModel = koinViewModel()) {
    val pagingItems = viewModel.romsFlow.collectAsLazyPagingItems()
    LibraryScreen(pagingItems)
}

@Composable
fun LibraryScreen(
    pagingItems: LazyPagingItems<SeedEntity>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(PREFERENCE_PADDING.dp),
        verticalArrangement = spacedBy(16.dp)
    ) {

        item {
            PageHeader(Res.string.library_title)
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(
            count = pagingItems.itemCount,
            key = { index -> pagingItems[index]?.hash ?: index }
        ) { index ->
            val seed = pagingItems[index]
            if (seed != null) {
                Box(modifier = Modifier.animateItem()) {
                    SeedItemView(seed)
                }
            }
        }
    }
}
