package dev.bltucker.mastermeme.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.bltucker.mastermeme.R
import dev.bltucker.mastermeme.common.theme.MasterMemeTheme
import dev.bltucker.mastermeme.home.composables.HomeTopBar
import dev.bltucker.mastermeme.home.composables.SortMode

const val HOME_SCREEN_ROUTE = "home"


fun NavGraphBuilder.homeScreen(){
    composable(route = HOME_SCREEN_ROUTE) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val model by viewModel.observableModel.collectAsStateWithLifecycle()

        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            model = model,
            onCreateMemeClick = {},
            onSortModeChange = viewModel::onUpdateSortMode
        )
    }
}

@Composable
private fun HomeScreen(modifier : Modifier = Modifier,
                       model: HomeModel,
                       onCreateMemeClick: () -> Unit = {},
                       onSortModeChange: (SortMode) -> Unit = {},){
    Scaffold(
        topBar = {
            HomeTopBar(
                sortMode = model.sortMode,
                onSortModeChange = { sortMode -> onSortModeChange(sortMode) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateMemeClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create new meme"
                )
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.no_memes_home_icon),
                contentDescription = null,
                modifier = Modifier.size(160.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Tap + button to create your first meme",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp)
            )
        }
    }
}



@Preview(showSystemUi = true)
@Composable
private fun HomeScreenEmptyStatePreview() {
    MasterMemeTheme {
        val previewModel = HomeModel()

        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            model = previewModel,
            onCreateMemeClick = {},
            onSortModeChange = {}
        )
    }
}