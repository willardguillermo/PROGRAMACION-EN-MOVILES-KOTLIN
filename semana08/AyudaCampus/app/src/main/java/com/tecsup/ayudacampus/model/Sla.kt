package com.tecsup.ayudacampus.model

import kotlin.math.abs

data class InfoSla(val texto: String, val vencido: Boolean)

fun horasSla(prioridad: Prioridad): Int = when (prioridad) {
    Prioridad.ALTA -> 4
    Prioridad.MEDIA -> 24
    Prioridad.BAJA -> 72
}

fun calcularSla(prioridad: String, estado: String, fecha: Long, ahora: Long): InfoSla? {
    val estadoTicket = EstadoTicket.valueOf(estado)
    if (estadoTicket == EstadoTicket.RESUELTO || estadoTicket == EstadoTicket.CERRADO) return null

    val limite = fecha + horasSla(Prioridad.valueOf(prioridad)) * 60 * 60 * 1000L
    val restante = limite - ahora
    val vencido = restante < 0
    val minutosTotales = abs(restante) / 60_000
    val tiempo = "${minutosTotales / 60} h ${minutosTotales % 60} min"

    return InfoSla(
        texto = if (vencido) "Vencido hace $tiempo" else "Vence en $tiempo",
        vencido = vencido
    )
}