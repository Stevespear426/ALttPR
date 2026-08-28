package com.stingers.alttpr.screens.upload

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.alttp
import alttpr.shared.generated.resources.begin_journey
import alttpr.shared.generated.resources.need_help
import alttpr.shared.generated.resources.select_base_rom
import alttpr.shared.generated.resources.select_rom_file
import alttpr.shared.generated.resources.select_rom_legal
import alttpr.shared.generated.resources.upload_file
import alttpr.shared.generated.resources.welcome_hero
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stingers.alttpr.common.GET_STARTED_URL
import com.stingers.alttpr.common.ROM_FILE_EXTENSION
import com.stingers.alttpr.common.components.PrimaryButton
import com.stingers.alttpr.theme.PreviewDarkTheme
import com.stingers.alttpr.theme.PreviewLightTheme
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun UploadRomScreen(
    viewModel: UploadViewModel = koinViewModel()
) {
    UploadRomScreen(viewModel::precessEvent)
}

@Composable
fun UploadRomScreen(processEvent: (event: UploadRomEvent) -> Unit) {
    val urlHandler = LocalUriHandler.current
    val romRequest = rememberFilePickerLauncher(
        type = FileKitType.File(extensions = listOf("smc", ROM_FILE_EXTENSION)),
        mode = FileKitMode.Single,
    ) { file ->
        file?.let {
            processEvent(UploadRomEvent.SaveRom(it))
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = stringResource(Res.string.welcome_hero),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )

        Text(
            text = stringResource(Res.string.begin_journey, stringResource(Res.string.alttp)),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    modifier = Modifier.border(2.dp, Color.DarkGray, CircleShape).padding(16.dp)
                        .size(32.dp),
                    painter = painterResource(Res.drawable.upload_file),
                    contentDescription = "Upload File"
                )
                Text(
                    text = stringResource(Res.string.select_base_rom),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    text = stringResource(
                        Res.string.select_rom_legal,
                        stringResource(Res.string.alttp)
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                PrimaryButton(Res.string.select_rom_file) {
                    romRequest.launch()
                }
            }
        }

        TextButton({
            urlHandler.openUri(GET_STARTED_URL)
        }) {
            Text(
                text = stringResource(Res.string.need_help),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UploadRomScreenLightPreview() {
    PreviewLightTheme {
        UploadRomScreen {}
    }
}

@Preview(showBackground = true)
@Composable
fun UploadRomScreenDarkPreview() {
    PreviewDarkTheme {
        UploadRomScreen {}
    }
}
