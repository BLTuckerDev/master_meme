package dev.bltucker.mastermeme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bltucker.mastermeme.home.HOME_SCREEN_ROUTE
import dev.bltucker.mastermeme.home.homeScreen


@Composable
fun MasterMemeNavigationGraph(navController: NavHostController) {
    NavHost(navController = navController,
        startDestination = HOME_SCREEN_ROUTE){


        homeScreen()
    }
}