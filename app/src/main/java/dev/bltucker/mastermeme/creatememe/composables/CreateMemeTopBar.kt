package dev.bltucker.mastermeme.creatememe.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bltucker.mastermeme.common.theme.MasterMemeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMemeTopBar(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text("New meme") },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigate back"
                )
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun CreateMemeTopBarPreview() {
    MasterMemeTheme {
        CreateMemeTopBar(onBackPressed = {},)
    }
}
