package com.tecsup.ayudacampus.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.ayudacampus.AyudaApp
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.ui.cliente.LoginScreen
import com.tecsup.ayudacampus.ui.cliente.MisTicketsScreen
import com.tecsup.ayudacampus.ui.cliente.SplashScreen
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.viewmodel.AyudaViewModelFactory
import com.tecsup.ayudacampus.viewmodel.MisTicketsViewModel
import com.tecsup.ayudacampus.viewmodel.SesionViewModel

private data class ItemBarra(val ruta: String, val texto: String, val icono: ImageVector)

private val itemsCliente = listOf(
    ItemBarra(Rutas.MIS_TICKETS, "Tickets", Icons.Outlined.ConfirmationNumber),
    ItemBarra(Rutas.NUEVO_TICKET, "Nuevo", Icons.Outlined.AddCircleOutline),
    ItemBarra(Rutas.PERFIL, "Perfil", Icons.Outlined.Person)
)

@Composable
fun NavAyuda(sesionVm: SesionViewModel) {
    val nav = rememberNavController()
    val app = LocalContext.current.applicationContext as AyudaApp
    val usuario by sesionVm.usuarioActual.collectAsStateWithLifecycle()
    val entrada by nav.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route

    val irAGrafo: (Rol) -> Unit = { rol ->
        val destino = if (rol == Rol.ADMIN) Rutas.GRAFO_SOPORTE else Rutas.GRAFO_CLIENTE
        nav.navigate(destino) {
            popUpTo(nav.graph.id) { inclusive = true }
            launchSingleTop = true
        }
    }

    val cerrarSesion: () -> Unit = {
        sesionVm.cerrarSesion()
        nav.navigate(Rutas.LOGIN) {
            popUpTo(nav.graph.id) { inclusive = true }
        }
    }

    val irATabCliente: (String) -> Unit = { ruta ->
        nav.navigate(ruta) {
            popUpTo(Rutas.MIS_TICKETS) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (itemsCliente.any { it.ruta == rutaActual }) {
                BarraInferior(itemsCliente, rutaActual, irATabCliente)
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Rutas.SPLASH,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {
            composable(Rutas.SPLASH) {
                SplashScreen(
                    vm = sesionVm,
                    alComenzar = {
                        nav.navigate(Rutas.LOGIN) {
                            popUpTo(Rutas.SPLASH) { inclusive = true }
                        }
                    },
                    alEntrar = irAGrafo
                )
            }

            composable(Rutas.LOGIN) {
                LoginScreen(vm = sesionVm, alIngresar = irAGrafo)
            }

            navigation(startDestination = Rutas.MIS_TICKETS, route = Rutas.GRAFO_CLIENTE) {
                composable(Rutas.MIS_TICKETS) {
                    val actual = usuario ?: return@composable
                    val vm: MisTicketsViewModel = viewModel(
                        factory = AyudaViewModelFactory {
                            MisTicketsViewModel(app.repository, actual.id)
                        }
                    )
                    MisTicketsScreen(
                        vm = vm,
                        nombre = actual.nombre,
                        alAbrirTicket = { id -> nav.navigate(Rutas.detalle(id)) },
                        alNuevo = { irATabCliente(Rutas.NUEVO_TICKET) }
                    )
                }

                composable(Rutas.NUEVO_TICKET) {
                    PantallaPendiente("Nuevo ticket", 8)
                }

                composable(
                    Rutas.DETALLE,
                    arguments = listOf(navArgument("ticketId") { type = NavType.IntType })
                ) {
                    PantallaPendiente("Detalle del ticket", 9)
                }

                composable(Rutas.PERFIL) {
                    PantallaPendiente("Perfil", 12, cerrarSesion)
                }
            }

            navigation(startDestination = Rutas.PANEL, route = Rutas.GRAFO_SOPORTE) {
                composable(Rutas.PANEL) {
                    PantallaPendiente("Panel de soporte", 10, cerrarSesion)
                }
            }
        }
    }
}

@Composable
private fun BarraInferior(
    items: List<ItemBarra>,
    rutaActual: String?,
    alElegir: (String) -> Unit
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        items.forEach { item ->
            NavigationBarItem(
                selected = rutaActual == item.ruta,
                onClick = { alElegir(item.ruta) },
                icon = { Icon(item.icono, contentDescription = item.texto) },
                label = { Text(item.texto) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

@Composable
private fun PantallaPendiente(
    titulo: String,
    paso: Int,
    alCerrarSesion: (() -> Unit)? = null
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Se completa en el Paso $paso", color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (alCerrarSesion != null) {
            Spacer(Modifier.height(24.dp))
            AyudaButton(texto = "Cerrar sesión", alPulsar = alCerrarSesion, contorno = true)
        }
    }
}