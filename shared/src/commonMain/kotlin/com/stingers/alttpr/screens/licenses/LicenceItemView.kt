package com.stingers.alttpr.screens.licenses

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.ic_github
import alttpr.shared.generated.resources.ic_open_in
import alttpr.shared.generated.resources.ic_website
import alttpr.shared.generated.resources.licence_version
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.BuildKonfig
import com.stingers.alttpr.model.Licence
import com.stingers.alttpr.model.LicenceType
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LicenceItemView(
    licence: Licence,
    onClick: () -> Unit
) {
    val iconModifier = Modifier.size(24.dp)
    val sourceIcon =
        if (licence.url.startsWith("https://www.github.com") ||
            licence.url.startsWith("https://github.com")
        ) Res.drawable.ic_github else Res.drawable.ic_website
    with(licence) {
        OutlinedCard(
            modifier = Modifier
                .wrapContentHeight()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = spacedBy(8.dp)
            ) {
                Column(verticalArrangement = spacedBy(4.dp)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = spacedBy(8.dp)
                    ) {
                        Text(
                            modifier = Modifier.clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary.copy(alpha = .25f))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            text = stringResource(type.title),
                            style = MaterialTheme.typography.labelSmall
                        )
                        version?.let {
                            Text(
                                text = stringResource(Res.string.licence_version, it),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                Icon(
                    modifier = iconModifier,
                    painter = painterResource(sourceIcon),
                    contentDescription = ""
                )
                Icon(
                    modifier = iconModifier,
                    painter = painterResource(Res.drawable.ic_open_in),
                    contentDescription = ""
                )
            }
        }
    }
}

class LicenceItemViewParameterProvider : PreviewParameterProvider<Licence> {
    override val values = sequenceOf(
        Licence(
            name = "Ktor",
            type = LicenceType.APACHE,
            url = "https://www.github.com/ktorio/ktor",
            version = BuildKonfig.KTOR_VERSION
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LicenceItemViewLightPreview(
    @PreviewParameter(LicenceItemViewParameterProvider::class) item: Licence
) {
    PreviewLightTheme {
        Surface(Modifier.fillMaxWidth()) {
            LicenceItemView(item) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LicenceItemViewDarkPreview(
    @PreviewParameter(LicenceItemViewParameterProvider::class) item: Licence
) {
    PreviewDarkTheme {
        Surface(Modifier.fillMaxWidth()) {
            LicenceItemView(item) {}
        }
    }
}
