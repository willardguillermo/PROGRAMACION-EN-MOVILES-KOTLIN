package com.willard.clinicasalud.navigation

// Se utiliza una sealed class para tener las rutas en un solo lugar
sealed class Pantalla(val ruta: String) {

    //INICIO
    object Inicio : Pantalla("inicio")

    //PERFIL
    // medicoId se reemplaza por el id real cuando navego
    object PerfilMedico : Pantalla("perfil/{medicoId}") {
        fun crearRuta(medicoId: Int): String = "perfil/$medicoId"   // ej: "perfil/5"
    }

    //AGENDAR
    // Sigo enviando el id del médico para saber con quién es la cita
    object AgendarCita : Pantalla("agendar/{medicoId}") {
        fun crearRuta(medicoId: Int): String = "agendar/$medicoId"   // ej: "agendar/5"
    }
}