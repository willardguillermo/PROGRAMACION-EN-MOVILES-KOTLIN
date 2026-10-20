package com.tecsup.ayudacampus.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import com.tecsup.ayudacampus.ui.cliente.DetalleTicketScreen
import com.tecsup.ayudacampus.ui.cliente.LoginScreen
import com.tecsup.ayudacampus.ui.cliente.MisTicketsScreen
import com.tecsup.ayudacampus.ui.cliente.NotificacionesScreen
import com.tecsup.ayudacampus.ui.cliente.NuevoTicketScreen
import com.tecsup.ayudacampus.viewmodel.NotificacionesViewModel
import com.tecsup.ayudacampus.ui.cliente.PerfilScreen
import com.tecsup.ayudacampus.ui.cliente.RegistroScreen
import com.tecsup.ayudacampus.ui.cliente.SplashScreen
import com.tecsup.ayudacampus.ui.soporte.AjustesScreen
import com.tecsup.ayudacampus.ui.soporte.AtenderTicketScreen
import com.tecsup.ayudacampus.ui.soporte.BandejaScreen
import com.tecsup.ayudacampus.ui.soporte.CategoriasScreen
import com.tecsup.ayudacampus.ui.soporte.FormularioCategoriaScreen
import com.tecsup.ayudacampus.ui.soporte.PanelScreen
import com.tecsup.ayudacampus.viewmodel.AtenderViewModel
import com.tecsup.ayudacampus.viewmodel.AyudaViewModelFactory
import com.tecsup.ayudacampus.viewmodel.BandejaViewModel
import com.tecsup.ayudacampus.viewmodel.CategoriasViewModel
import com.tecsup.ayudacampus.viewmodel.MisTicketsViewModel
import com.tecsup.ayudacampus.viewmodel.NuevoTicketViewModel
import com.tecsup.ayudacampus.viewmodel.PanelViewModel
import com.tecsup.ayudacampus.viewmodel.PerfilViewModel
import com.tecsup.ayudacampus.viewmodel.RegistroViewModel
import com.tecsup.ayudacampus.viewmodel.SesionViewModel
import com.tecsup.ayudacampus.viewmodel.TicketDetalleViewModel

private data class ItemBarra(val ruta: String, val texto: String, val icono: ImageVector)

private val itemsCliente = listOf(
    ItemBarra(Rutas.MIS_TICKETS, "Tickets", Icons.Outlined.ConfirmationNumber),
    ItemBarra(Rutas.NUEVO_TICKET, "Nuevo", Icons.Outlined.AddCircleOutline),
    ItemBarra(Rutas.PERFIL, "Perfil", Icons.Outlined.Person)
)

private val itemsSoporte = listOf(
    ItemBarra(Rutas.PANEL, "Panel", Icons.Outlined.BarChart),
    ItemBarra(Rutas.BANDEJA, "Tickets", Icons.Outlined.ConfirmationNumber),
    ItemBarra(Rutas.CATEGORIAS, "Categorías", Icons.Outlined.LocalOffer),
    ItemBarra(Rutas.AJUSTES, "Ajustes", Icons.Outlined.Settings)
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

    val cambiarPerfil: () -> Unit = {
        sesionVm.cambiarPerfil(irAGrafo)
    }

    val irATabCliente: (String) -> Unit = { ruta ->
        nav.navigate(ruta) {
            popUpTo(Rutas.MIS_TICKETS) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    val irATabSoporte: (String) -> Unit = { ruta ->
        nav.navigate(ruta) {
            popUpTo(Rutas.PANEL) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            when {
                itemsCliente.any { it.ruta == rutaActual } ->
                    BarraInferior(itemsCliente, rutaActual, irATabCliente)
                itemsSoporte.any { it.ruta == rutaActual } ->
                    BarraInferior(itemsSoporte, rutaActual, irATabSoporte)
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Rutas.SPLASH,
            enterTransition = { fadeIn(tween(250)) + slideInHorizontally(tween(250)) { it / 8 } },
            exitTransition = { fadeOut(tween(250)) },
            popEnterTransition = { fadeIn(tween(250)) + slideInHorizontally(tween(250)) { -it / 8 } },
            popExitTransition = { fadeOut(tween(250)) },
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
                LoginScreen(
                    vm = sesionVm,
                    alIngresar = irAGrafo,
                    alRegistrarse = { nav.navigate(Rutas.REGISTRO) }
                )
            }

            composable(Rutas.REGISTRO) {
                val vm: RegistroViewModel = viewModel(
                    factory = AyudaViewModelFactory { RegistroViewModel(app.repository) }
                )
                RegistroScreen(vm = vm, alVolver = { nav.popBackStack() })
            }

            composable(Rutas.NOTIFICACIONES) {
                val actual = usuario ?: return@composable
                val vm: NotificacionesViewModel = viewModel(
                    key = "notificaciones_root",
                    factory = AyudaViewModelFactory {
                        NotificacionesViewModel(app.repository, app.preferencias, actual)
                    }
                )
                NotificacionesScreen(
                    vm = vm,
                    alAbrirTicket = { id ->
                        val destino = if (actual.rol == Rol.ADMIN.name) Rutas.atender(id) else Rutas.detalle(id)
                        nav.navigate(destino)
                    },
                    alVolver = { nav.popBackStack() }
                )
            }

            navigation(startDestination = Rutas.MIS_TICKETS, route = Rutas.GRAFO_CLIENTE) {
                composable(Rutas.MIS_TICKETS) {
                    val actual = usuario ?: return@composable
                    val vm: MisTicketsViewModel = viewModel(
                        factory = AyudaViewModelFactory {
                            MisTicketsViewModel(app.repository, actual.id)
                        }
                    )
                    val notifVm: NotificacionesViewModel = viewModel(
                        key = "notificaciones",
                        factory = AyudaViewModelFactory {
                            NotificacionesViewModel(app.repository, app.preferencias, actual)
                        }
                    )
                    val noLeidas by notifVm.noLeidas.collectAsStateWithLifecycle()

                    MisTicketsScreen(
                        vm = vm,
                        nombre = actual.nombre,
                        noLeidas = noLeidas,
                        alAbrirTicket = { id -> nav.navigate(Rutas.detalle(id)) },
                        alNuevo = { irATabCliente(Rutas.NUEVO_TICKET) },
                        alAbrirNotificaciones = { nav.navigate(Rutas.NOTIFICACIONES) }
                    )
                }

                composable(Rutas.NUEVO_TICKET) {
                    val actual = usuario ?: return@composable
                    val vm: NuevoTicketViewModel = viewModel(
                        factory = AyudaViewModelFactory {
                            NuevoTicketViewModel(
                                app.repository,
                                actual.id,
                                app.preferencias.recibirTickets
                            )
                        }
                    )
                    NuevoTicketScreen(
                        vm = vm,
                        alVolver = { nav.popBackStack() },
                        alEnviado = { irATabCliente(Rutas.MIS_TICKETS) }
                    )
                }

                composable(
                    Rutas.DETALLE,
                    arguments = listOf(navArgument("ticketId") { type = NavType.IntType })
                ) { backStack ->
                    val actual = usuario ?: return@composable
                    val ticketId = backStack.arguments?.getInt("ticketId") ?: return@composable
                    val vm: TicketDetalleViewModel = viewModel(
                        factory = AyudaViewModelFactory {
                            TicketDetalleViewModel(app.repository, ticketId, actual)
                        }
                    )
                    DetalleTicketScreen(
                        vm = vm,
                        usuarioId = actual.id,
                        alVolver = { nav.popBackStack() }
                    )
                }

                composable(Rutas.PERFIL) {
                    val actual = usuario ?: return@composable
                    val vm: PerfilViewModel = viewModel(
                        factory = AyudaViewModelFactory {
                            PerfilViewModel(app.repository, actual.id)
                        }
                    )
                    PerfilScreen(
                        vm = vm,
                        sesionVm = sesionVm,
                        alCambiarPerfil = cambiarPerfil,
                        alCerrarSesion = cerrarSesion
                    )
                }
            }

            navigation(startDestination = Rutas.PANEL, route = Rutas.GRAFO_SOPORTE) {
                composable(Rutas.PANEL) {
                    val actual = usuario ?: return@composable
                    val vm: PanelViewModel = viewModel(
                        factory = AyudaViewModelFactory { PanelViewModel(app.repository) }
                    )
                    val notifVm: NotificacionesViewModel = viewModel(
                        key = "notificaciones",
                        factory = AyudaViewModelFactory {
                            NotificacionesViewModel(app.repository, app.preferencias, actual)
                        }
                    )
                    val noLeidas by notifVm.noLeidas.collectAsStateWithLifecycle()

                    PanelScreen(
                        vm = vm,
                        noLeidas = noLeidas,
                        alVerBandeja = { irATabSoporte(Rutas.BANDEJA) },
                        alAbrirNotificaciones = { nav.navigate(Rutas.NOTIFICACIONES) }
                    )
                }

                composable(Rutas.BANDEJA) {
                    val vm: BandejaViewModel = viewModel(
                        factory = AyudaViewModelFactory { BandejaViewModel(app.repository) }
                    )
                    BandejaScreen(
                        vm = vm,
                        alAbrirTicket = { id -> nav.navigate(Rutas.atender(id)) }
                    )
                }

                composable(
                    Rutas.ATENDER,
                    arguments = listOf(navArgument("ticketId") { type = NavType.IntType })
                ) { backStack ->
                    val actual = usuario ?: return@composable
                    val ticketId = backStack.arguments?.getInt("ticketId") ?: return@composable
                    val vm: AtenderViewModel = viewModel(
                        factory = AyudaViewModelFactory {
                            AtenderViewModel(app.repository, ticketId, actual)
                        }
                    )
                    AtenderTicketScreen(
                        vm = vm,
                        usuarioId = actual.id,
                        alVolver = { nav.popBackStack() }
                    )
                }

                composable(Rutas.CATEGORIAS) {
                    val vm: CategoriasViewModel = viewModel(
                        factory = AyudaViewModelFactory { CategoriasViewModel(app.repository) }
                    )
                    CategoriasScreen(
                        vm = vm,
                        alNueva = { nav.navigate(Rutas.formCategoria()) },
                        alEditar = { id -> nav.navigate(Rutas.formCategoria(id)) }
                    )
                }

                composable(
                    Rutas.FORM_CATEGORIA,
                    arguments = listOf(navArgument("id") {
                        type = NavType.IntType
                        defaultValue = 0
                    })
                ) { backStack ->
                    val id = backStack.arguments?.getInt("id") ?: 0
                    val vm: CategoriasViewModel = viewModel(
                        factory = AyudaViewModelFactory { CategoriasViewModel(app.repository) }
                    )
                    FormularioCategoriaScreen(
                        vm = vm,
                        id = id,
                        alVolver = { nav.popBackStack() }
                    )
                }

                composable(Rutas.AJUSTES) {
                    AjustesScreen(
                        sesionVm = sesionVm,
                        alCambiarPerfil = cambiarPerfil,
                        alCerrarSesion = cerrarSesion
                    )
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