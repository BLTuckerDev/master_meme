package dev.bltucker.mastermeme.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val HOME_SCREEN_ROUTE = "home"


fun NavGraphBuilder.homeScreen(){
    composable(route = HOME_SCREEN_ROUTE) {
        HomeScreen(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun HomeScreen(modifier : Modifier = Modifier){
    Box(modifier = modifier,
        contentAlignment = Alignment.Center){
        Text("Todo: Home")

    }
}