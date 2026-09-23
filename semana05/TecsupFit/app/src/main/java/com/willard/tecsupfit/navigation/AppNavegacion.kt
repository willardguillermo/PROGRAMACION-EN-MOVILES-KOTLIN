package com.willard.tecsupfit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.willard.tecsupfit.data.DatosGimnasio
import com.willard.tecsupfit.data.EstadoReserva
import com.willard.tecsupfit.data.Reserva
import com.willard.tecsupfit.ui.screens.ConfirmacionScreen
import com.willard.tecsupfit.ui.screens.DetalleClaseScreen
import com.willard.tecsupfit.ui.screens.InicioScreen
import com.willard.tecsupfit.ui.screens.ReservarCupoScreen

@Composable
fun AppNavegacion() {
    // El navController guarda el historial de pantallas
    val navController = rememberNavController()

    //LISTA DE RESERVAS
    // La creo aquí para que todas las pantallas usen la misma lista (sin ViewModel)
    val reservas = remember { mutableStateListOf<Reserva>() }

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
                // Paso el mismo id a la pantalla de reservar
                onReservarClick = { id ->
                    navController.navigate(Pantalla.ReservarCupo.crearRuta(id))
                }
            )
        }

        //PANTALLA RESERVAR
        composable(
            route = Pantalla.ReservarCupo.ruta,
            arguments = listOf(
                navArgument("claseId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 0

            ReservarCupoScreen(
                claseId = claseId,
                onVolver = { navController.popBackStack() },
                onConfirmar = { horarioIndex ->
                    // Guardo la reserva en la lista como CONFIRMADA
                    val clase = DatosGimnasio.buscarClase(claseId)
                    if (clase != null) {
                        reservas.add(
                            Reserva(
                                // id = el mayor que exista + 1, así nunca se repite
                                id = (reservas.maxOfOrNull { it.id } ?: 0) + 1,
                                clase = clase,
                                horario = clase.horarios[horarioIndex].hora,
                                estado = EstadoReserva.CONFIRMADA
                            )
                        )
                    }
                    // popUpTo quita Detalle y Reservar del historial,
                    // así con "atrás" no se vuelve a reservar la misma clase
                    navController.navigate(
                        Pantalla.Confirmacion.crearRuta(claseId, horarioIndex)
                    ) {
                        popUpTo(Pantalla.Inicio.ruta)
                    }
                }
            )
        }

        //PANTALLA CONFIRMACION
        composable(
            route = Pantalla.Confirmacion.ruta,
            arguments = listOf(
                navArgument("claseId") { type = NavType.IntType },
                navArgument("horarioIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 0
            val horarioIndex = backStackEntry.arguments?.getInt("horarioIndex") ?: 0

            ConfirmacionScreen(
                claseId = claseId,
                horarioIndex = horarioIndex,
                onVerReservas = { },   // pendiente: ir a la pestaña Reservas
                // Regreso a Inicio sin apilar otra pantalla
                onVolverInicio = {
                    navController.popBackStack(Pantalla.Inicio.ruta, inclusive = false)
                }
            )
        }
    }
}