package com.stingers.alttpr.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.stingers.alttpr.screens.dashboard.DashboardScreen
import com.stingers.alttpr.screens.library.LibraryScreen
import com.stingers.alttpr.screens.randomizer.RandomizerScreen
import com.stingers.alttpr.screens.settings.SettingsScreen
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import kotlinx.coroutines.launch


@Composable
fun MainScreen() {

    val screens: List<@Composable () -> Unit> = listOf(
        { DashboardScreen() },
        { RandomizerScreen() },
        { LibraryScreen() },
        { SettingsScreen() }
    )

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { screens.size })
    val setCurrentPage: (index: Int) -> Unit = {
        scope.launch {
            if (it != pagerState.currentPage) {
                pagerState.scrollToPage(it)
            }
        }
    }
    Column(Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            beyondViewportPageCount = 0,
            userScrollEnabled = false,
        ) { index ->
            screens[index]()
        }
        BottomBar(pagerState.currentPage, setCurrentPage)
    }

}

@Preview(showBackground = true)
@Composable
fun UploadRomViewLightPreview() {
    PreviewLightTheme {
        MainScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun UploadRomViewDarkPreview() {
    PreviewDarkTheme {
        MainScreen()
    }
}
