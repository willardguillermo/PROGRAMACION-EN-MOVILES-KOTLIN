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

    //CONFIRMACION
    // Aquí viajan 2 datos: la clase y la posición del horario elegido
    object Confirmacion : Pantalla("confirmacion/{claseId}/{horarioIndex}") {
        fun crearRuta(claseId: Int, horarioIndex: Int): String =
            "confirmacion/$claseId/$horarioIndex"   // ej: "confirmacion/2/1"
    }

    //RESERVAS (pestaña)
    object Reservas : Pantalla("reservas")

    //RUTINAS (pestaña)
    object Rutinas : Pantalla("rutinas")
}