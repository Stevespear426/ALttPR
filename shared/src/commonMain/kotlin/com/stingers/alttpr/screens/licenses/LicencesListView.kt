package com.stingers.alttpr.screens.licenses

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.all_trademarks
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.model.Licence
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun LicencesListView(items: List<Licence> = licences, onLicenceClick: (licence: Licence) -> Unit = {}) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = PREFERENCE_PADDING.dp,
            start = PREFERENCE_PADDING.dp,
            end = PREFERENCE_PADDING.dp,
            bottom = 48.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items) { licence ->
            LicenceItemView(licence) {
                onLicenceClick(licence)
            }
        }

        item { Spacer(Modifier.height(8.dp)) }
        item { HorizontalDivider() }
        item { Spacer(Modifier.height(8.dp)) }
        item {
            Text(
                text = stringResource(Res.string.all_trademarks),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LicencesListViewLightPreview() {
    PreviewLightTheme {
        Surface(Modifier.fillMaxSize()) {
            LicencesListView {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LicencesListViewDarkPreview() {
    PreviewDarkTheme {
        Surface(Modifier.fillMaxSize()) {
            LicencesListView {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LicencesListViewShortLightPreview() {
    PreviewLightTheme {
        Surface(Modifier.fillMaxSize()) {
            LicencesListView(licences.take(4)) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LicencesListViewShortDarkPreview() {
    PreviewDarkTheme {
        Surface(Modifier.fillMaxSize()) {
            LicencesListView(licences.take(4)) {}
        }
    }
}