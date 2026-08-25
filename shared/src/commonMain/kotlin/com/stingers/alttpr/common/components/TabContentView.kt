package com.stingers.alttpr.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun TabContentView(
    modifier: Modifier,
    tabs: List<String>,
    content: @Composable (page: Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState =
        rememberPagerState(pageCount = { tabs.size }, initialPage = 0)
    Column(modifier.fillMaxSize()) {
        if (tabs.size > 1) {
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                divider = {
                    Spacer(modifier = Modifier.height(5.dp))
                },
                indicator = {
                    Box(
                        Modifier
                            .tabIndicatorOffset(pagerState.currentPage)
                            .fillMaxHeight()
                            .padding(vertical = 8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape
                            )
                            .zIndex(-1f)
                    )
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                tabs.forEachIndexed { index, tab ->
                    val selected by remember { derivedStateOf { index == pagerState.currentPage } }
                    Tab(
                        selectedContentColor = Color.White,
                        unselectedContentColor = MaterialTheme.colorScheme.secondary,
                        selected = selected,
                        text = { Text(text = tab, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                        onClick = {
                            coroutineScope.launch {
                                val tabDiff =
                                    abs(pagerState.currentPage - index)
                                if (tabDiff > 2) {
                                    pagerState.animateScrollToPage(index)
                                } else {
                                    pagerState.scrollToPage(index)
                                }

                            }
                        },
                    )
                }

            }
        }
        HorizontalPager(
            state = pagerState,
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            beyondViewportPageCount = 1,
            userScrollEnabled = false,
            key = { tabs[it] }
        ) { index ->
            content(index)
        }
    }
}