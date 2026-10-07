package com.tecsup.mibodega.ui.cliente.modelo


const val COSTO_DELIVERY = 4.00

// esDelivery: true = delivery a domicilio; false = recojo en tienda
// costoEnvio: COSTO_DELIVERY si es delivery, 0.0 si es recojo
data class Pedido(
    val id: Int,
    val items: List<ItemCarrito>,
    val direccion: String,
    val referencia: String,
    val metodoPago: String,
    val esDelivery: Boolean = true,
    val costoEnvio: Double = COSTO_DELIVERY,
    val estado: String = "En preparación"
) {
    // Propiedades calculadas: no se guardan, se calculan a partir de items
    val subtotal: Double get() = items.sumOf { it.producto.precio * it.cantidad }
    val total: Double get() = subtotal + costoEnvio
    val cantidadProductos: Int get() = items.sumOf { it.cantidad }

    // Texto para mostrar dónde se entrega el pedido
    val lugarEntrega: String
        get() = if (esDelivery) {
            if (referencia.isBlank()) direccion else "$direccion ($referencia)"
        } else {
            "Recojo en tienda"
        }
}
