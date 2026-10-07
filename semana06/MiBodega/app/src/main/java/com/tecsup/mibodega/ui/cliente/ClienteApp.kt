package com.tecsup.mibodega.ui.cliente

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.COSTO_DELIVERY
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Pedido
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.Usuario
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.categorias.CategoriasScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.favoritos.FavoritosScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.login.LoginScreen
import com.tecsup.mibodega.ui.cliente.screens.pedidos.PedidosScreen
import com.tecsup.mibodega.ui.cliente.screens.perfil.PerfilScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen

/** Duración (ms) de la animación entre pantallas. */
private const val DURACION_ANIMACION = 300

/**
 * "Director de orquesta" de la app cliente:
 * - Tiene el NavHost con las rutas de cada pantalla.
 * - Tiene el estado del carrito (List<ItemCarrito>), que se reparte
 *   hacia abajo a Inicio, Detalle, Carrito y Entrega.
 * Ninguna Screen navega sola ni modifica el carrito directamente:
 * todas reciben funciones (lambdas) desde aquí (state hoisting).
 */
@Composable
fun ClienteApp(
    modoOscuro: Boolean = false,
    onCambiarModoOscuro: (Boolean) -> Unit = {}
) {
    val navController = rememberNavController()

    // El carrito vive aquí arriba, no en ninguna Screen.
    // rememberSaveable (y no remember): sobrevive al girar la pantalla.
    // Funciona porque Producto, ItemCarrito, Usuario y Pedido son Serializable.
    var carrito by rememberSaveable { mutableStateOf<List<ItemCarrito>>(emptyList()) }

    // Usuario "logueado" (null = nadie ha entrado todavía).
    var usuario by rememberSaveable { mutableStateOf<Usuario?>(null) }
    
    // Cuenta registrada en la app (para validar en el login)
    var usuarioRegistrado by rememberSaveable { mutableStateOf<Usuario?>(null) }

    // Historial de pedidos confirmados. Se agrega con "pedidos + pedido" (lista nueva).
    var pedidos by rememberSaveable { mutableStateOf<List<Pedido>>(emptyList()) }

    // Ids de los productos marcados con ❤️ (Set: no se repiten).
    var favoritos by rememberSaveable { mutableStateOf<Set<Int>>(emptySet()) }
    val alternarFavorito: (Producto) -> Unit = { producto ->
        favoritos = if (producto.id in favoritos) favoritos - producto.id else favoritos + producto.id
    }

    // Navegación del menú inferior: Inicio queda siempre como base de la pila,
    // así las pestañas no se apilan y "atrás" desde cualquiera vuelve a Inicio.
    val navegarBarra: (String) -> Unit = { ruta ->
        navController.navigate(ruta) {
            popUpTo(Rutas.INICIO)
            launchSingleTop = true // no duplicar la pestaña si ya estoy en ella
        }
    }

    // NavHost anima cada cambio de pantalla con AnimatedContent por dentro.
    // Aquí le decimos CÓMO animar: deslizar + desvanecer en 300 ms.
    //  - enter/exit: al avanzar (navigate) la nueva entra desde la derecha.
    //  - popEnter/popExit: al volver (atrás) la anterior regresa desde la izquierda.
    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(DURACION_ANIMACION)) +
                fadeIn(tween(DURACION_ANIMACION))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(DURACION_ANIMACION)) +
                fadeOut(tween(DURACION_ANIMACION))
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(DURACION_ANIMACION)) +
                fadeIn(tween(DURACION_ANIMACION))
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(DURACION_ANIMACION)) +
                fadeOut(tween(DURACION_ANIMACION))
        }
    ) {
        composable(Rutas.BIENVENIDA) {
            var mostrarTerminos by rememberSaveable { mutableStateOf(false) }

            BienvenidaScreen(
                onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                onIniciarSesion = { navController.navigate(Rutas.LOGIN) },
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

        composable(Rutas.LOGIN) {
            LoginScreen(
                usuarioRegistrado = usuarioRegistrado,
                onVolver = { navController.popBackStack() },
                onIngresar = { userToLog ->
                    // Usuario y contraseña correctos: entramos con los datos validados
                    usuario = userToLog
                    navController.navigate(Rutas.INICIO) {
                        // Bienvenida y Login salen de la pila: "atrás" desde Inicio cierra la app
                        popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onVolver = { navController.popBackStack() },
                onCrearCuenta = { usuarioReg, claveReg, nombre, telefono, direccion, referencia ->
                    // Guardamos la cuenta registrada
                    usuarioRegistrado = Usuario(usuarioReg, claveReg, nombre, telefono, direccion, referencia)
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.BIENVENIDA)
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
                onVerFavoritos = { navController.navigate(Rutas.FAVORITOS) },
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
                modoOscuro = modoOscuro,
                onCambiarModoOscuro = onCambiarModoOscuro,
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
                    esFavorito = producto.id in favoritos,
                    onFavorito = { alternarFavorito(producto) },
                    onVolver = { navController.popBackStack() },
                    onAgregarAlCarrito = { productoSeleccionado, cantidad ->
                        carrito = agregarOSumarProducto(carrito, productoSeleccionado, cantidad)
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(Rutas.FAVORITOS) {
            FavoritosScreen(
                // Convertimos los ids guardados en productos para mostrarlos
                favoritos = listaProductosFake.filter { it.id in favoritos },
                onVolver = { navController.popBackStack() },
                onProductoClick = { producto ->
                    navController.navigate(Rutas.detalle(producto.id))
                },
                onQuitarFavorito = { producto -> favoritos = favoritos - producto.id }
            )
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
                onContinuarPedido = { navController.navigate(Rutas.ENTREGA) }
            )
        }

        composable(Rutas.ENTREGA) {
            DatosEntregaScreen(
                usuario = usuario,
                subtotal = carrito.sumOf { it.producto.precio * it.cantidad },
                onVolver = { navController.popBackStack() },
                onConfirmar = { datos, metodoPago, esDelivery ->
                    val pedido = Pedido(
                        id = 1024 + pedidos.size, // correlativo simple: #1024, #1025...
                        items = carrito,
                        direccion = datos.direccion,
                        referencia = datos.referencia,
                        metodoPago = metodoPago,
                        esDelivery = esDelivery,
                        costoEnvio = if (esDelivery) COSTO_DELIVERY else 0.0
                    )
                    pedidos = pedidos + pedido
                    usuario = datos        // recordamos los últimos datos de entrega
                    carrito = emptyList()  // la compra terminó: carrito limpio

                    navController.navigate(Rutas.confirmacion(pedido.id)) {
                        // popUpTo: saca Detalle, Carrito y Entrega de la pila.
                        // Así "atrás" en Confirmación vuelve a Inicio y no a un
                        // formulario de un pedido que ya se envió.
                        popUpTo(Rutas.INICIO)
                    }
                }
            )
        }

        composable(
            route = Rutas.CONFIRMACION,
            arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getInt("pedidoId") ?: 0
            val pedido = pedidos.find { it.id == pedidoId }

            if (pedido != null) {
                ConfirmacionScreen(
                    pedido = pedido,
                    onVerEstado = { navegarBarra(Rutas.PEDIDOS) },
                    onVolverInicio = { navegarBarra(Rutas.INICIO) }
                )
            }
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