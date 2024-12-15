package dev.bltucker.mastermeme.home.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import dev.bltucker.mastermeme.common.theme.MasterMemeTheme

enum class SortMode{
    NEWEST,
    FAVORITES
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(modifier: Modifier = Modifier,
               isInSelectionMode: Boolean = false,
               selectedCount: Int = 0,
               sortMode: SortMode = SortMode.FAVORITES,
               onSortModeChange: (SortMode) -> Unit = {},
               onExitSelectionMode: () -> Unit = {},
               onShareSelectedClicked: () -> Unit = {},
               onDeleteSelectedClicked: () -> Unit = {}
               ) {

    var showSortMenu by remember { mutableStateOf(false) }
    val dropdownIconRotation by animateFloatAsState(
        targetValue = if (showSortMenu) 180f else 0f,
        label = "Dropdown rotation"
    )

    AnimatedContent(
        targetState = isInSelectionMode,
        label = "TopBar content"
    ) { inSelectionMode ->
        if (inSelectionMode) {
            TopAppBar(
                title = { Text("$selectedCount selected") },
                navigationIcon = {
                    IconButton(onClick = onExitSelectionMode) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Exit selection mode"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onShareSelectedClicked) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share selected memes"
                        )
                    }
                    IconButton(onClick = onDeleteSelectedClicked) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete selected memes",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = modifier
            )
        } else {
            TopAppBar(
                title = { Text("Your memes") },
                actions = {
                    TextButton(onClick = { showSortMenu = true }) {
                        Text(text = if (sortMode == SortMode.FAVORITES) "Favorites first" else "Newest first")
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Sort options",
                            modifier = Modifier.rotate(dropdownIconRotation)
                        )

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Favorites first") },
                                onClick = {
                                    onSortModeChange(SortMode.FAVORITES)
                                    showSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Newest first") },
                                onClick = {
                                    onSortModeChange(SortMode.NEWEST)
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                },
                modifier = modifier
            )
        }
    }
}



@Preview(name = "Top Bar - Normal Mode - Favorites First", showBackground = true)
@Composable
private fun HomeTopBarPreview_NormalMode_FavoritesFirst() {
    MasterMemeTheme {
        HomeTopBar(
            isInSelectionMode = false,
            sortMode = SortMode.FAVORITES
        )
    }
}

@Preview(name = "Top Bar - Normal Mode - Newest First", showBackground = true)
@Composable
private fun HomeTopBarPreview_NormalMode_NewestFirst() {
    MasterMemeTheme {
        HomeTopBar(
            isInSelectionMode = false,
            sortMode = SortMode.NEWEST
        )
    }
}

@Preview(name = "Top Bar - Selection Mode - Single Item", showBackground = true)
@Composable
private fun HomeTopBarPreview_SelectionMode_SingleItem() {
    MasterMemeTheme {
        HomeTopBar(
            isInSelectionMode = true,
            selectedCount = 1
        )
    }
}

@Preview(name = "Top Bar - Selection Mode - Multiple Items", showBackground = true)
@Composable
private fun HomeTopBarPreview_SelectionMode_MultipleItems() {
    MasterMemeTheme {
        HomeTopBar(
            isInSelectionMode = true,
            selectedCount = 5
        )
    }
}

@Preview(
    name = "Top Bar - All States",
    showBackground = true,
    widthDp = 400
)
@Composable
private fun HomeTopBarPreview_AllStates() {
    MasterMemeTheme {
        Column() {
            HomeTopBar(
                isInSelectionMode = false,
                sortMode = SortMode.FAVORITES
            )
            HomeTopBar(
                isInSelectionMode = false,
                sortMode = SortMode.NEWEST
            )
            HomeTopBar(
                isInSelectionMode = true,
                selectedCount = 1
            )
            HomeTopBar(
                isInSelectionMode = true,
                selectedCount = 5
            )
        }
    }
}

