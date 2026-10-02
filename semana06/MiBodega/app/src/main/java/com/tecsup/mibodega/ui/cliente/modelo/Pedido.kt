package com.tecsup.mibodega.ui.cliente.modelo


const val COSTO_DELIVERY = 4.00


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