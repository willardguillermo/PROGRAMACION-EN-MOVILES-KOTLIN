package com.willard.Lab04CarritoTecsup

data class Pedido(
    val numero: Int,
    val fecha: String,
    val productos: List<Producto>,
    val total: Double
)