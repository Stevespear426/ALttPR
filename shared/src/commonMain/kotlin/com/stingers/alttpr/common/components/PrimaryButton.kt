package com.stingers.alttpr.common.components

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.ok
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PrimaryButton(
    text: StringResource,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    shape: Shape = CircleShape,
    color: Color = MaterialTheme.colorScheme.secondary,
    onClick: (() -> Unit)
) {
    PrimaryButton(
        text = stringResource(text),
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = color,
        onClick = onClick
    )
}

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    shape: Shape = CircleShape,
    color: Color = MaterialTheme.colorScheme.secondary,
    onClick: (() -> Unit)
) {

    Button(
        onClick = onClick,
        modifier = modifier.testTag("Test PrimaryButton"),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = shape,
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .testTag("Test PrimaryButton Text")
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrimaryButtonLightPreview() {
    PreviewLightTheme {
        Surface(Modifier.fillMaxWidth()) {
            PrimaryButton(Res.string.ok) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrimaryButtonDarkPreview() {
    PreviewDarkTheme {
        Surface(Modifier.fillMaxWidth()) {
            PrimaryButton(Res.string.ok) {}
        }
    }
}