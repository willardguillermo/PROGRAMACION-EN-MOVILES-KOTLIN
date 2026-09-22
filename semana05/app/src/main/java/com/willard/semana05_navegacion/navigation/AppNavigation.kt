package com.willard.semana05_navegacion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.willard.semana05_navegacion.screens.DetailScreen
import com.willard.semana05_navegacion.screens.HomeScreen
import com.willard.semana05_navegacion.screens.ListScreen
import com.willard.semana05_navegacion.screens.ProfileScreen

@Composable
fun AppNavigation() {
    // Crea y recuerda el controlador que maneja todo el back stack
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Ruta simple: home
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        // Ruta simple: list
        composable(Screen.List.route) {
            ListScreen(navController)
        }
        // Ruta simple: profile
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
        // Ruta con argumento tipado: detail/{itemId}
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument(name = "itemId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
            DetailScreen(navController, itemId)
        }
    }
}
