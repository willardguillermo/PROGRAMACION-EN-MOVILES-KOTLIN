package com.willard.clinicasalud.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.willard.clinicasalud.ui.screens.AgendarCitaScreen
import com.willard.clinicasalud.ui.screens.InicioScreen
import com.willard.clinicasalud.ui.screens.PerfilMedicoScreen

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
                // Cuando tocan un médico, mando su id al perfil
                onMedicoClick = { medicoId ->
                    navController.navigate(Pantalla.PerfilMedico.crearRuta(medicoId))
                },
                onAbrirMenu = { }
            )
        }

        //PANTALLA PERFIL
        composable(
            route = Pantalla.PerfilMedico.ruta,
            arguments = listOf(
                navArgument("medicoId") { type = NavType.IntType }   // Se recibe como int
            )
        ) { backStackEntry ->
            // Saco el id que viene en la ruta
            val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0

            PerfilMedicoScreen(
                medicoId = medicoId,
                onVolver = { navController.popBackStack() },
                // Paso el mismo id a la pantalla de agendar
                onAgendarClick = { id ->
                    navController.navigate(Pantalla.AgendarCita.crearRuta(id))
                }
            )
        }

        //PANTALLA AGENDAR
        composable(
            route = Pantalla.AgendarCita.ruta,
            arguments = listOf(
                navArgument("medicoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0

            AgendarCitaScreen(
                medicoId = medicoId,
                onVolver = { navController.popBackStack() },
                onConfirmar = { _, _ -> }   // pendiente: ir a Confirmación
            )
        }
    }
}