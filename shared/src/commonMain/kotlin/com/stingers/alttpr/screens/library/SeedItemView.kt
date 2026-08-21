package com.stingers.alttpr.screens.library

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.edit
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.BASE_ROM_FILENAME
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.common.components.HashCodeRow
import com.stingers.alttpr.common.components.PrimaryButton
import com.stingers.alttpr.model.SeedEntity
import com.stingers.alttpr.model.SpoilerMeta
import com.stingers.alttpr.screens.edit.SpoilerMetaView
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme

@Composable
fun SeedItemView(
    seedEntity: SeedEntity,
    processEvent: (event: LibraryEvent) -> Unit
) {
    var expended by mutableStateOf(false)
    with(seedEntity) {
        Card(modifier = Modifier.clickable {
            expended = !expended
        }.wrapContentHeight().fillMaxWidth()) {
            Column(modifier = Modifier.padding(PREFERENCE_PADDING.dp)) {
                meta?.let {
                    Text(it.getFileName())
                }

                seedEntity.getHashCode()?.let {
                    HashCodeRow(it)
                }
                Text(generated.orEmpty())
            }
            if (expended) {
                meta?.let {
                    SpoilerMetaView(it)
                }
                PrimaryButton(Res.string.edit) {
                    processEvent(LibraryEvent.OpenEditSeed(seedEntity))
                }
            }
        }
    }
}

class RomEntityParameterProvider : PreviewParameterProvider<SeedEntity> {
    override val values = sequenceOf(
        SeedEntity(
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
        ),
    )
}


@Preview(showBackground = true)
@Composable
fun SeedItemViewLightPreview(
    @PreviewParameter(RomEntityParameterProvider::class) item: SeedEntity
) {
    PreviewLightTheme {
        SeedItemView(item) {}
    }
}

@Preview(showBackground = true)
@Composable
fun SeedItemViewDarkPreview(
    @PreviewParameter(RomEntityParameterProvider::class) item: SeedEntity
) {
    PreviewDarkTheme {
        SeedItemView(item) {}
    }
}
