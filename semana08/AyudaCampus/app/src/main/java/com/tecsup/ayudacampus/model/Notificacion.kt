package com.tecsup.ayudacampus.model

data class Notificacion(
    val ticketId: Int,
    val titulo: String,
    val mensaje: String,
    val fecha: Long,
    val nueva: Boolean
)
