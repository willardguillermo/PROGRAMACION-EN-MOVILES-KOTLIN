package com.willard.Lab04CarritoTecsup

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    val productos = remember { mutableStateListOf<Producto>() }
    val favoritos = remember { mutableStateListOf<Long>() }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val onToggleFavorito: (Producto) -> Unit = { producto ->
        if (producto.id in favoritos) {
            favoritos.remove(producto.id)
            Toast.makeText(context, "${producto.nombre} quitado de favoritos", Toast.LENGTH_SHORT).show()
        } else {
            favoritos.add(producto.id)
            Toast.makeText(context, "${producto.nombre} agregado a favoritos", Toast.LENGTH_SHORT).show()
        }
    }

    val onEliminar: (Producto) -> Unit = { producto ->
        productos.remove(producto)
        favoritos.remove(producto.id)
    }

    val opcionesMenu = listOf(
        OpcionMenu(Pantalla.Inicio.ruta, "Inicio", Icons.Default.Home),
        OpcionMenu(Pantalla.MisPedidos.ruta, "Mis pedidos", Icons.Default.ShoppingCart),
        OpcionMenu(Pantalla.Favoritos.ruta, "Favoritos", Icons.Default.Favorite),
        OpcionMenu(Pantalla.Perfil.ruta, "Perfil", Icons.Default.Person)
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                opciones = opcionesMenu,
                rutaActual = rutaActual,
                cantidadFavoritos = favoritos.size,
                onOpcionClick = { ruta ->
                    scope.launch { drawerState.close() }
                    irASeccion(navController, ruta)
                },
                onCerrarSesion = {
                    scope.launch { drawerState.close() }
                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            opcionesMenu.find { it.ruta == rutaActual }
                                ?.takeIf { it.ruta != Pantalla.Inicio.ruta }
                                ?.titulo ?: "Mi Carrito TECSUP"
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            BadgedBox(
                                badge = {
                                    if (favoritos.isNotEmpty()) Badge()
                                }
                            ) {
                                Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Pantalla.Inicio.ruta,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Pantalla.Inicio.ruta) {
                    PantallaCarrito(
                        productos = productos,
                        favoritos = favoritos,
                        onToggleFavorito = onToggleFavorito,
                        onEliminar = onEliminar
                    )
                }
                composable(Pantalla.MisPedidos.ruta) {
                    PantallaSimple("Mis pedidos", Icons.Default.ShoppingCart)
                }
                composable(Pantalla.Favoritos.ruta) {
                    PantallaSimple("Favoritos", Icons.Default.Favorite)
                }
                composable(Pantalla.Perfil.ruta) {
                    PantallaSimple("Perfil", Icons.Default.Person)
                }
            }
        }
    }
}

private fun irASeccion(navController: NavHostController, ruta: String) {
    if (ruta == Pantalla.Inicio.ruta) {
        navController.popBackStack(Pantalla.Inicio.ruta, inclusive = false)
    } else {
        navController.navigate(ruta) {
            popUpTo(Pantalla.Inicio.ruta)
            launchSingleTop = true
        }
    }
}