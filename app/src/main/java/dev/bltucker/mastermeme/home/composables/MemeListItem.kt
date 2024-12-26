package dev.bltucker.mastermeme.home.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.bltucker.mastermeme.common.room.MemeEntity
import dev.bltucker.mastermeme.common.theme.MasterMemeTheme
import java.time.Instant

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MemeListItem(
    modifier: Modifier = Modifier,
    meme: MemeEntity,
    isSelected: Boolean = false,
    isSelectionMode: Boolean = false,
    onLongClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = {},
                    onLongClick = onLongClick
                )
        ) {
            AsyncImage(
                model = meme.filepath,
                contentDescription = meme.templateName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (!isSelectionMode) {
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (meme.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (meme.isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (meme.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemeListItemPreview_Normal() {
    MasterMemeTheme {
        MemeListItem(
            modifier = Modifier.size(200.dp),
            meme = MemeEntity(
                templateName = "Test Meme",
                createdDate = Instant.now(),
                isFavorite = false,
                filepath = ""
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MemeListItemPreview_Favorite() {
    MasterMemeTheme {
        MemeListItem(
            modifier = Modifier.size(200.dp),
            meme = MemeEntity(
                templateName = "Test Meme",
                createdDate = Instant.now(),
                isFavorite = true,
                filepath = ""
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MemeListItemPreview_Selected() {
    MasterMemeTheme {
        MemeListItem(
            modifier = Modifier.size(200.dp),
            meme = MemeEntity(
                templateName = "Test Meme",
                createdDate = Instant.now(),
                isFavorite = true,
                filepath = ""
            ),
            isSelected = true,
            isSelectionMode = true
        )
    }
}