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

    //RESERVAR CUPO
    // Sigo enviando el id de la clase para saber qué se reserva
    object ReservarCupo : Pantalla("reservar/{claseId}") {
        fun crearRuta(claseId: Int): String = "reservar/$claseId"   // ej: "reservar/2"
    }
}