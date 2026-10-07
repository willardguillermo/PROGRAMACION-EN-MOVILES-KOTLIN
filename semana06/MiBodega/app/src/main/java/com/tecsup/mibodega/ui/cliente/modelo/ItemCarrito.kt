package com.tecsup.mibodega.ui.cliente.modelo

import java.io.Serializable

data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int
) : Serializable

