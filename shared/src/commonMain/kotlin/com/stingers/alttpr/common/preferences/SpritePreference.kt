package com.stingers.alttpr.common.preferences

import alttpr.shared.generated.resources.Res
import alttpr.shared.generated.resources.ic_next
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.stingers.alttpr.model.Sprite
import org.jetbrains.compose.resources.painterResource

@Composable
fun SpritePreference(
    currentItem: Sprite?,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(5.dp),
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
    ) {

        AsyncImage(
            model = currentItem?.previewUrl
                ?: "https://alttpr-assets.s3.us-east-2.amazonaws.com/001.link.1.zspr.png",
            contentDescription = currentItem?.name ?: "Default",
            modifier = Modifier.size(72.dp).padding(end = 4.dp)
        )
        Column {
            Text(
                text = currentItem?.name?.takeIf { it.isNotBlank() } ?: "Default",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Change Sprite",
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(Modifier.weight(1f))
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(Res.drawable.ic_next),
            contentDescription = ""
        )
    }
}
