package com.tecsup.mibodega.ui.cliente.modelo

import com.tecsup.mibodega.R


fun Producto.imagenRes(): Int = when (id) {
    1 -> R.drawable.arroz_costeno
    2 -> R.drawable.aceite_primor
    3 -> R.drawable.leche_gloria
    4 -> R.drawable.galleta_oreo
    5 -> R.drawable.coca_cola_original
    6 -> R.drawable.inca_kola
    7 -> R.drawable.agua_san_luis
    8 -> R.drawable.jugo_frugos
    9 -> R.drawable.azucar_rubia
    10 -> R.drawable.fideos_spaghetti
    11 -> R.drawable.papas_lays
    12 -> R.drawable.chocolate_sublime
    else -> R.drawable.ilustracion_bodega
}