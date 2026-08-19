package com.stingers.alttpr.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.model.api.Hash
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HashCodeRow(
    list: List<Hash>,
    iconSize: Int = 24
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        list.forEach {
            with(it) {
                Image(
                    modifier = Modifier.size(iconSize.dp),
                    painter = painterResource(icon),
                    contentDescription = stringResource(title)
                )
            }
        }
    }
}

class HashCodeRowParameterProvider : PreviewParameterProvider<List<Hash>> {
    override val values = sequenceOf(
        listOf(
            Hash.GreenPotion,
            Hash.Hookshot,
            Hash.Hammer,
            Hash.Shovel,
            Hash.Mushroom
        ),
        listOf(
            Hash.Map,
            Hash.Mail,
            Hash.Mirror,
            Hash.MoonPearl,
            Hash.Bomb
        )
    )
}

@Composable
fun HashCodeRowLightPreview(
    @PreviewParameter(HashCodeRowParameterProvider::class) item: List<Hash>
) {
    PreviewLightTheme {
        Surface(Modifier.fillMaxWidth()) {
            HashCodeRow(item)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HashCodeRowDarkPreview(
    @PreviewParameter(HashCodeRowParameterProvider::class) item: List<Hash>

) {
    PreviewDarkTheme {
        Surface(Modifier.fillMaxWidth()) {
            HashCodeRow(item)
        }
    }
}