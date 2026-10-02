package com.tecsup.mibodega.ui.cliente

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Pedido
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.Usuario
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.modelo.usuarioDemo
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.categorias.CategoriasScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.pedidos.PedidosScreen
import com.tecsup.mibodega.ui.cliente.screens.perfil.PerfilScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen
import androidx.compose.runtime.LaunchedEffect

/**
 * "Director de orquesta" de la app cliente:
 * - Tiene el NavHost con las rutas de cada pantalla.
 * - Tiene el estado del carrito (List<ItemCarrito>), que se reparte
 *   hacia abajo a Inicio, Detalle, Carrito y Entrega.
 * Ninguna Screen navega sola ni modifica el carrito directamente:
 * todas reciben funciones (lambdas) desde aquí (state hoisting).
 */
@Composable
fun ClienteApp() {
    val navController = rememberNavController()

    // El carrito vive aquí arriba, no en ninguna Screen.
    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }

    // Usuario "logueado" (null = nadie ha entrado todavía).
    var usuario by remember { mutableStateOf<Usuario?>(null) }

    // Historial de pedidos confirmados (lista observable: add() redibuja la UI).
    val pedidos = remember { mutableStateListOf<Pedido>() }

    // Navegación del menú inferior: Inicio queda siempre como base de la pila,
    // así las pestañas no se apilan y "atrás" desde cualquiera vuelve a Inicio.
    val navegarBarra: (String) -> Unit = { ruta ->
        navController.navigate(ruta) {
            popUpTo(Rutas.INICIO)
            launchSingleTop = true // no duplicar la pestaña si ya estoy en ella
        }
    }

    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA
    ) {
        composable(Rutas.BIENVENIDA) {
            var mostrarTerminos by remember { mutableStateOf(false) }

            BienvenidaScreen(
                onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                onIniciarSesion = {
                    // Login simulado: sin backend, entramos con un usuario de prueba
                    usuario = usuarioDemo
                    navController.navigate(Rutas.INICIO) {
                        // Bienvenida sale de la pila: "atrás" desde Inicio cierra la app
                        popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                    }
                },
                onTerminos = { mostrarTerminos = true }
            )

            if (mostrarTerminos) {
                AlertDialog(
                    onDismissRequest = { mostrarTerminos = false },
                    title = { Text("Términos y Condiciones") },
                    text = {
                        Text(
                            "Mi Bodega es una app de práctica del curso Programación en Móviles. " +
                                    "Los productos, precios y pedidos son de ejemplo y no generan compras reales."
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { mostrarTerminos = false }) { Text("Entendido") }
                    }
                )
            }
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onVolver = { navController.popBackStack() },
                onCrearCuenta = { nombre, telefono, direccion, referencia ->
                    // Guardamos los datos en memoria (se pierden al cerrar la app)
                    usuario = Usuario(nombre, telefono, direccion, referencia)
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.INICIO) {
            InicioScreen(
                cantidadCarrito = carrito.sumOf { it.cantidad },
                onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                onProductoClick = { producto ->
                    navController.navigate(Rutas.detalle(producto.id))
                },
                onAgregarProducto = { producto ->
                    carrito = agregarOSumarProducto(carrito, producto, 1)
                },
                onNavegarBarra = navegarBarra
            )
        }

        composable(Rutas.CATEGORIAS) {
            CategoriasScreen(
                onProductoClick = { producto ->
                    navController.navigate(Rutas.detalle(producto.id))
                },
                onNavegarBarra = navegarBarra
            )
        }

        composable(Rutas.PEDIDOS) {
            PedidosScreen(
                pedidos = pedidos,
                onNavegarBarra = navegarBarra
            )
        }

        composable(Rutas.PERFIL) {
            PerfilScreen(
                usuario = usuario,
                onCerrarSesion = {
                    usuario = null
                    carrito = emptyList()
                    navController.navigate(Rutas.BIENVENIDA) {
                        // Limpia TODA la pila: "atrás" no debe regresar a la app logueada
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onNavegarBarra = navegarBarra
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            // 1) Leemos el parámetro que viene en la ruta "detalle/{productoId}"
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            // 2) Buscamos el producto. find() devuelve null si no existe
            //    (first() lanzaría una excepción y cerraría la app).
            val producto = listaProductosFake.find { it.id == productoId }

            if (producto == null) {
                // Id inválido: no hay nada que mostrar, regresamos
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                DetalleProductoScreen(
                    producto = producto,
                    onVolver = { navController.popBackStack() },
                    onAgregarAlCarrito = { productoSeleccionado, cantidad ->
                        carrito = agregarOSumarProducto(carrito, productoSeleccionado, cantidad)
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(Rutas.CARRITO) {
            CarritoScreen(
                carrito = carrito,
                onVolver = { navController.popBackStack() },
                onIncrementar = { producto ->
                    carrito = carrito.map {
                        if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                    }
                },
                onDecrementar = { producto ->
                    carrito = carrito.mapNotNull {
                        when {
                            it.producto.id != producto.id -> it
                            it.cantidad > 1 -> it.copy(cantidad = it.cantidad - 1)
                            else -> null // si llega a 0, se elimina de la lista
                        }
                    }
                },
                onEliminar = { producto ->
                    carrito = carrito.filterNot { it.producto.id == producto.id }
                },
                onVaciar = { carrito = emptyList() },
                onContinuarPedido = { /* TODO: navegar a DatosEntregaScreen */ }
            )
        }
    }
}

/**
 * Si el producto ya está en el carrito, le suma la cantidad;
 * si no, lo agrega como un ItemCarrito nuevo.
 */
private fun agregarOSumarProducto(
    carrito: List<ItemCarrito>,
    producto: Producto,
    cantidad: Int
): List<ItemCarrito> {
    val itemExistente = carrito.find { it.producto.id == producto.id }
    return if (itemExistente != null) {
        carrito.map {
            if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + cantidad) else it
        }
    } else {
        carrito + ItemCarrito(producto = producto, cantidad = cantidad)
    }
}