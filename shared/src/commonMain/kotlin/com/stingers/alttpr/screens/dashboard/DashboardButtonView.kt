package com.stingers.alttpr.screens.dashboard

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.ic_generator
import alttpr.shared.generated.resources.mystery_game
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.PREFERENCE_PADDING
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun DashboardButtonView(
    modifier: Modifier,
    icon: DrawableResource,
    text: StringResource,
    onClick: () -> Unit
) {
    val textString = stringResource(text)
    OutlinedCard(
        modifier = modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(PREFERENCE_PADDING.dp),
            verticalArrangement = spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "$textString button"
            )
            Text(textString)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardButtonViewLightPreview() {
    PreviewLightTheme {
        Surface {
            DashboardButtonView(
                modifier = Modifier,
                icon = Res.drawable.ic_generator,
                text = Res.string.mystery_game
            ) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardButtonViewDarkPreview() {
    PreviewDarkTheme {
        Surface {
            DashboardButtonView(
                modifier =  Modifier,
                icon = Res.drawable.ic_generator,
                text = Res.string.mystery_game
            ) {}
        }
    }
}
