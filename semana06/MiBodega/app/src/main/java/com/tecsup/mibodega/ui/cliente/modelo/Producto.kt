package com.tecsup.mibodega.ui.cliente.modelo

import java.io.Serializable

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String
) : Serializable

