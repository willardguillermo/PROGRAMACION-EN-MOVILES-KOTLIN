package com.tecsup.mibodega.ui.cliente

/**
 * Todas las rutas de la app en un solo lugar.
 * Así no escribimos strings "sueltos" en cada navigate():
 * un error de tipeo en una ruta compila bien pero revienta en ejecución.
 */
object Rutas {
    // Acceso
    const val BIENVENIDA = "bienvenida"
    const val REGISTRO = "registro"
    const val LOGIN = "login"

    // Destinos del menú inferior (NavigationBar)
    const val INICIO = "inicio"
    const val CATEGORIAS = "categorias"
    const val PEDIDOS = "pedidos"
    const val PERFIL = "perfil"

    // Flujo de compra
    const val DETALLE = "detalle/{productoId}"
    const val CARRITO = "carrito"
    const val ENTREGA = "entrega"
    const val CONFIRMACION = "confirmacion/{pedidoId}"
    const val FAVORITOS = "favoritos"

    // Arman la ruta real reemplazando el parámetro
    fun detalle(productoId: Int) = "detalle/$productoId"
    fun confirmacion(pedidoId: Int) = "confirmacion/$pedidoId"
}