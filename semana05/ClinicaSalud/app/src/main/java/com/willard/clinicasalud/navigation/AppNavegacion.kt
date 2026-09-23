package com.willard.clinicasalud.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.willard.clinicasalud.data.Cita
import com.willard.clinicasalud.data.DatosClinica
import com.willard.clinicasalud.data.EstadoCita
import com.willard.clinicasalud.ui.components.MenuLateral
import com.willard.clinicasalud.ui.components.OpcionMenu
import com.willard.clinicasalud.ui.screens.AgendarCitaScreen
import com.willard.clinicasalud.ui.screens.ConfirmacionScreen
import com.willard.clinicasalud.ui.screens.InicioScreen
import com.willard.clinicasalud.ui.screens.MisCitasScreen
import com.willard.clinicasalud.ui.screens.PerfilMedicoScreen
import androidx.compose.material.icons.filled.History
import com.willard.clinicasalud.ui.screens.HistorialScreen
import kotlinx.coroutines.launch
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.material.icons.filled.Person
import com.willard.clinicasalud.ui.screens.PerfilPacienteScreen
@Composable
fun AppNavegacion() {
    // El navController guarda el historial de pantallas
    val navController = rememberNavController()

    //LISTA DE CITAS
    // La creo aquí para que todas las pantallas usen la misma lista (sin ViewModel)
    // rememberSaveable + citasSaver para que no se borren al girar la pantalla
    val citas = rememberSaveable(saver = citasSaver) { mutableStateListOf<Cita>() }

    //MENU LATERAL
    // drawerState guarda si el menú está abierto o cerrado
    // scope lo necesito porque abrir y cerrar el menú tiene animación
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Opciones que aparecen en el menú
    val opcionesMenu = listOf(
        OpcionMenu(Pantalla.Inicio.ruta, "Inicio", Icons.Default.Home),
        OpcionMenu(Pantalla.MisCitas.ruta, "Mis citas", Icons.Default.DateRange),
        OpcionMenu(Pantalla.Historial.ruta, "Historial médico", Icons.Default.History),
        OpcionMenu(Pantalla.PerfilPaciente.ruta, "Perfil", Icons.Default.Person)
    )

    // Ruta de la pantalla actual, para resaltar la opción del menú
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    // El drawer envuelve al NavHost para que un solo menú sirva en todas las pantallas
    ModalNavigationDrawer(
        drawerState = drawerState,
        // El menú solo se desliza en las secciones, no mientras se agenda una cita
        gesturesEnabled = opcionesMenu.any { it.ruta == rutaActual },
        drawerContent = {
            MenuLateral(
                opciones = opcionesMenu,
                rutaActual = rutaActual,
                onOpcionClick = { ruta ->
                    scope.launch { drawerState.close() }
                    irASeccion(navController, ruta)
                }
            )
        }
    ) {
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
                    onAbrirMenu = { scope.launch { drawerState.open() } }
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
                    onConfirmar = { fechaIndex, horaIndex ->
                        // Guardo la cita en la lista como CONFIRMADA
                        val medico = DatosClinica.buscarMedico(medicoId)
                        if (medico != null) {
                            citas.add(
                                Cita(
                                    // id = el mayor que exista + 1, así nunca se repite
                                    id = (citas.maxOfOrNull { it.id } ?: 0) + 1,
                                    medico = medico,
                                    fecha = DatosClinica.fechas[fechaIndex],
                                    hora = DatosClinica.horas[horaIndex],
                                    estado = EstadoCita.CONFIRMADA
                                )
                            )
                        }
                        // popUpTo quita Perfil y Agendar del historial,
                        // así con "atrás" no se vuelve a agendar la misma cita
                        navController.navigate(
                            Pantalla.Confirmacion.crearRuta(medicoId, fechaIndex, horaIndex)
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
                    navArgument("medicoId") { type = NavType.IntType },
                    navArgument("fechaIndex") { type = NavType.IntType },
                    navArgument("horaIndex") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0
                val fechaIndex = backStackEntry.arguments?.getInt("fechaIndex") ?: 0
                val horaIndex = backStackEntry.arguments?.getInt("horaIndex") ?: 0

                ConfirmacionScreen(
                    medicoId = medicoId,
                    fechaIndex = fechaIndex,
                    horaIndex = horaIndex,
                    // Regreso a Inicio sin apilar otra pantalla
                    onVolverInicio = {
                        navController.popBackStack(Pantalla.Inicio.ruta, inclusive = false)
                    },
                    // Voy a Mis citas; con "atrás" se vuelve a Inicio
                    onVerMisCitas = {
                        navController.navigate(Pantalla.MisCitas.ruta) {
                            popUpTo(Pantalla.Inicio.ruta)
                        }
                    }
                )
            }

            //PANTALLA MIS CITAS
            composable(Pantalla.MisCitas.ruta) {
                MisCitasScreen(
                    citas = citas,
                    onAbrirMenu = { scope.launch { drawerState.open() } },
                    onCompletar = { citaId ->
                        // Busco la cita y la reemplazo por una copia con estado COMPLETADA
                        val posicion = citas.indexOfFirst { it.id == citaId }
                        if (posicion != -1) {
                            citas[posicion] = citas[posicion].copy(estado = EstadoCita.COMPLETADA)
                        }
                    }
                )
            }

            //PANTALLA HISTORIAL
            composable(Pantalla.Historial.ruta) {
                HistorialScreen(
                    citas = citas,   // uso la misma lista, así ve las que se completaron
                    onAbrirMenu = { scope.launch { drawerState.open() } }
                )
            }


            //PANTALLA PERFIL DEL PACIENTE
            composable(Pantalla.PerfilPaciente.ruta) {
                PerfilPacienteScreen(
                    citas = citas,   // para calcular las estadísticas
                    onAbrirMenu = { scope.launch { drawerState.open() } }
                )
            }
        }
    }
}

//NAVEGAR DESDE EL MENU
// Voy a una sección sin apilar pantallas repetidas
private fun irASeccion(navController: NavHostController, ruta: String) {
    if (ruta == Pantalla.Inicio.ruta) {
        navController.popBackStack(Pantalla.Inicio.ruta, inclusive = false)
    } else {
        navController.navigate(ruta) {
            popUpTo(Pantalla.Inicio.ruta)   // debajo solo queda Inicio
            launchSingleTop = true          // no abre dos veces la misma pantalla
        }
    }


}



//GUARDAR CITAS AL GIRAR LA PANTALLA
// Android solo guarda datos simples (números y textos), no objetos Cita.
// Por eso convierto cada cita en 5 datos simples y al volver la reconstruyo.
private val citasSaver = listSaver<SnapshotStateList<Cita>, Any>(
    save = { lista ->
        // Cada cita se guarda como: id, id del médico, fecha, hora, estado
        lista.flatMap { cita ->
            listOf(cita.id, cita.medico.id, cita.fecha, cita.hora, cita.estado.name)
        }
    },
    restore = { guardado ->
        val lista = mutableStateListOf<Cita>()
        // Tomo los datos de 5 en 5 y armo cada cita otra vez
        guardado.chunked(5).forEach { datos ->
            val medico = DatosClinica.buscarMedico(datos[1] as Int)
            if (medico != null) {
                lista.add(
                    Cita(
                        id = datos[0] as Int,
                        medico = medico,
                        fecha = datos[2] as String,
                        hora = datos[3] as String,
                        estado = EstadoCita.valueOf(datos[4] as String)
                    )
                )
            }
        }
        lista
    }
)