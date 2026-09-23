package com.willard.tecsupfit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.willard.tecsupfit.data.DatosGimnasio
import com.willard.tecsupfit.data.EstadoReserva
import com.willard.tecsupfit.data.Reserva
import com.willard.tecsupfit.ui.components.BarraInferior
import com.willard.tecsupfit.ui.components.Pestana
import com.willard.tecsupfit.ui.screens.ConfirmacionScreen
import com.willard.tecsupfit.ui.screens.DetalleClaseScreen
import com.willard.tecsupfit.ui.screens.InicioScreen
import com.willard.tecsupfit.ui.screens.PerfilScreen
import com.willard.tecsupfit.ui.screens.ReservarCupoScreen
import com.willard.tecsupfit.ui.screens.ReservasScreen
import com.willard.tecsupfit.ui.screens.RutinasScreen

@Composable
fun AppNavegacion() {
    // El navController guarda el historial de pantallas
    val navController = rememberNavController()

    //LISTA DE RESERVAS
    // La creo aquí para que todas las pantallas usen la misma lista (sin ViewModel)
    // rememberSaveable + reservasSaver para que no se borren al girar la pantalla
    val reservas = rememberSaveable(saver = reservasSaver) { mutableStateListOf<Reserva>() }

    //BARRA INFERIOR
    // Pestañas que aparecen abajo
    val pestanas = listOf(
        Pestana(Pantalla.Inicio.ruta, "Inicio", Icons.Default.Home),
        Pestana(Pantalla.Reservas.ruta, "Reservas", Icons.Default.EventAvailable),
        Pestana(Pantalla.Rutinas.ruta, "Rutinas", Icons.Default.FitnessCenter),
        Pestana(Pantalla.Perfil.ruta, "Perfil", Icons.Default.Person)
    )

    // Ruta de la pantalla actual: con ella la barra sabe qué pestaña resaltar
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    // Armo la barra una sola vez y se la paso a cada pestaña
    val barraInferior: @Composable () -> Unit = {
        BarraInferior(
            pestanas = pestanas,
            rutaActual = rutaActual,
            onPestanaClick = { ruta -> irAPestana(navController, ruta) }
        )
    }

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
                },
                barraInferior = barraInferior
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
                // Voy a la pestaña Reservas
                onVerReservas = { irAPestana(navController, Pantalla.Reservas.ruta) },
                // Regreso a Inicio sin apilar otra pantalla
                onVolverInicio = {
                    navController.popBackStack(Pantalla.Inicio.ruta, inclusive = false)
                }
            )
        }

        //PANTALLA RESERVAS
        composable(Pantalla.Reservas.ruta) {
            ReservasScreen(
                reservas = reservas,
                barraInferior = barraInferior,
                onCompletar = { reservaId ->
                    // Busco la reserva y la reemplazo por una copia con estado COMPLETADA
                    val posicion = reservas.indexOfFirst { it.id == reservaId }
                    if (posicion != -1) {
                        reservas[posicion] = reservas[posicion].copy(estado = EstadoReserva.COMPLETADA)
                    }
                }
            )
        }

        //PANTALLA RUTINAS
        composable(Pantalla.Rutinas.ruta) {
            RutinasScreen(barraInferior = barraInferior)
        }

        //PANTALLA PERFIL
        composable(Pantalla.Perfil.ruta) {
            PerfilScreen(
                reservas = reservas,   // para calcular las estadísticas
                barraInferior = barraInferior
            )
        }
    }
}

//NAVEGAR ENTRE PESTAÑAS
// Voy a una pestaña sin apilar pantallas repetidas
private fun irAPestana(navController: NavHostController, ruta: String) {
    if (ruta == Pantalla.Inicio.ruta) {
        navController.popBackStack(Pantalla.Inicio.ruta, inclusive = false)
    } else {
        navController.navigate(ruta) {
            popUpTo(Pantalla.Inicio.ruta)   // debajo solo queda Inicio
            launchSingleTop = true          // no abre dos veces la misma pantalla
        }
    }
}

//GUARDAR RESERVAS AL GIRAR LA PANTALLA
// Android solo guarda datos simples (números y textos), no objetos Reserva.
// Por eso convierto cada reserva en 4 datos simples y al volver la reconstruyo.
private val reservasSaver = listSaver<SnapshotStateList<Reserva>, Any>(
    save = { lista ->
        // Cada reserva se guarda como: id, id de la clase, horario, estado
        lista.flatMap { reserva ->
            listOf(reserva.id, reserva.clase.id, reserva.horario, reserva.estado.name)
        }
    },
    restore = { guardado ->
        val lista = mutableStateListOf<Reserva>()
        // Tomo los datos de 4 en 4 y armo cada reserva otra vez
        guardado.chunked(4).forEach { datos ->
            val clase = DatosGimnasio.buscarClase(datos[1] as Int)
            if (clase != null) {
                lista.add(
                    Reserva(
                        id = datos[0] as Int,
                        clase = clase,
                        horario = datos[2] as String,
                        estado = EstadoReserva.valueOf(datos[3] as String)
                    )
                )
            }
        }
        lista
    }
)