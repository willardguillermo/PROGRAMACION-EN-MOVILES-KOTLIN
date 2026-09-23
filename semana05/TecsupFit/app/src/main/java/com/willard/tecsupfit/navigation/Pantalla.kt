package com.willard.tecsupfit.navigation

// Uso una sealed class para tener todas las rutas en un solo lugar
sealed class Pantalla(val ruta: String) {

    //INICIO
    object Inicio : Pantalla("inicio")

    //DETALLE DE CLASE
    // {claseId} se reemplaza por el id real al navegar
    object DetalleClase : Pantalla("detalle/{claseId}") {
        fun crearRuta(claseId: Int): String = "detalle/$claseId"   // ej: "detalle/2"
    }
}