package com.tecsup.mibodega.ui.cliente.modelo

/** Costo fijo de delivery (mockup: S/ 4.00). Lo usan Carrito, Entrega y Pedido. */
const val COSTO_DELIVERY = 4.00

/**
 * Un pedido ya confirmado. Vive en memoria (mutableStateListOf en ClienteApp),
 * así que se pierde al cerrar la app: es intencional hasta ver Room.
 */
data class Pedido(
    val id: Int,
    val items: List<ItemCarrito>,
    val direccion: String,
    val referencia: String,
    val metodoPago: String,
    val estado: String = "En preparación"
) {
    // Propiedades calculadas: no se guardan, se calculan a partir de items
    val subtotal: Double get() = items.sumOf { it.producto.precio * it.cantidad }
    val total: Double get() = subtotal + COSTO_DELIVERY
    val cantidadProductos: Int get() = items.sumOf { it.cantidad }
}