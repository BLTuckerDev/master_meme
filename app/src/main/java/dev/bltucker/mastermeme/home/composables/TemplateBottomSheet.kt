package dev.bltucker.mastermeme.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.bltucker.mastermeme.common.templates.MemeTemplate
import dev.bltucker.mastermeme.common.theme.MasterMemeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateBottomSheet(
    templates: List<MemeTemplate>,
    searchQuery: String,
    isSearching: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onTemplateSelected: (MemeTemplate) -> Unit,
    onOpenSearch: () -> Unit,
    onCloseSearch: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        SheetTopBar(
            searchQuery = searchQuery,
            showSearchField = isSearching,
            onSearchQueryChange = onSearchQueryChange,
            onOpenSearch = onOpenSearch,
            onCloseSearch = onCloseSearch
        )

        if(searchQuery.isNotBlank()){
            Text(
                text = "${templates.size} templates available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        } else {
            Text(
                text = "Choose template for your next meme masterpiece",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(templates) { template ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable { onTemplateSelected(template) }
                ) {
                    AsyncImage(
                        model = template.resourceId,
                        contentDescription = template.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SheetTopBar(
    searchQuery: String,
    showSearchField: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onOpenSearch: () -> Unit,
    onCloseSearch: () -> Unit,
) {
    TopAppBar(
        title = {
            if (showSearchField) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Search templates") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    )
                )
            } else {
                Text("Choose template")
            }
        },
        navigationIcon = {
            if (showSearchField) {
                IconButton(onClick = {
                    onCloseSearch()
                    onSearchQueryChange("")
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Exit search"
                    )
                }
            }
        },
        actions = {
            if (showSearchField) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search"
                    )
                }
            } else {
                IconButton(onClick = { onOpenSearch() }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search templates"
                    )
                }
            }
        }
    )
}


@Preview(showBackground = true, widthDp = 400, heightDp = 700)
@Composable
private fun TemplateBottomSheetPreview() {
    val previewTemplates = listOf(
        MemeTemplate(
            resourceId = android.R.drawable.ic_menu_gallery,
            name = "Drake Hotline Bling",
            searchTerms = setOf("drake", "reject", "accept")
        ),
        MemeTemplate(
            resourceId = android.R.drawable.ic_menu_gallery,
            name = "Distracted Boyfriend",
            searchTerms = setOf("guy", "looking", "back")
        ),
        MemeTemplate(
            resourceId = android.R.drawable.ic_menu_gallery,
            name = "Two Buttons",
            searchTerms = setOf("choice", "decision", "sweating")
        ),
        MemeTemplate(
            resourceId = android.R.drawable.ic_menu_gallery,
            name = "Change My Mind",
            searchTerms = setOf("debate", "crowder", "table")
        )
    )

    MasterMemeTheme {
        Surface {
            TemplateBottomSheet(
                templates = previewTemplates,
                searchQuery = "",
                onSearchQueryChange = {},
                onTemplateSelected = {},
                onOpenSearch = {},
                onCloseSearch = {},
                isSearching = false
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 700)
@Composable
private fun TemplateBottomSheetPreview_Searching() {
    val previewTemplates = listOf(
        MemeTemplate(
            resourceId = android.R.drawable.ic_menu_gallery,
            name = "Drake Hotline Bling",
            searchTerms = setOf("drake", "reject", "accept")
        ),
        MemeTemplate(
            resourceId = android.R.drawable.ic_menu_gallery,
            name = "Distracted Boyfriend",
            searchTerms = setOf("guy", "looking", "back")
        )
    )

    MasterMemeTheme {
        Surface {
            TemplateBottomSheet(
                templates = previewTemplates,
                searchQuery = "dra",
                onSearchQueryChange = {},
                onTemplateSelected = {},
                onOpenSearch = {},
                onCloseSearch = {},
                isSearching = true
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 700)
@Composable
private fun TemplateBottomSheetPreview_Empty() {
    MasterMemeTheme {
        Surface {
            TemplateBottomSheet(
                templates = emptyList(),
                searchQuery = "nonexistent",
                onSearchQueryChange = {},
                onTemplateSelected = {},
                onOpenSearch = {},
                onCloseSearch = {},
                isSearching = true
            )
        }
    }
}