package com.willard.tecsupfit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.willard.tecsupfit.ui.screens.DetalleClaseScreen
import com.willard.tecsupfit.ui.screens.InicioScreen

@Composable
fun AppNavegacion() {
    // El navController guarda el historial de pantallas
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Pantalla.Inicio.ruta
    ) {

        //PANTALLA INICIO
        composable(Pantalla.Inicio.ruta) {
            InicioScreen(
                // Cuando tocan una clase, mando su id al detalle
                onClaseClick = { claseId ->
                    navController.navigate(Pantalla.DetalleClase.crearRuta(claseId))
                }
            )
        }

        //PANTALLA DETALLE
        composable(
            route = Pantalla.DetalleClase.ruta,
            arguments = listOf(
                navArgument("claseId") { type = NavType.IntType }   // lo recibo como Int
            )
        ) { backStackEntry ->
            // Saco el id que viene en la ruta
            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 0

            DetalleClaseScreen(
                claseId = claseId,
                onVolver = { navController.popBackStack() },
                onReservarClick = { }   // pendiente: ir a Reservar cupo
            )
        }
    }
}