package com.willard.prestamolibros.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Prestamo(
    val tituloLibro: String,
    val tipoUsuario: TipoUsuario,
    val fechaPrestamo: LocalDate,
    val fechaDevolucion: LocalDate
) {

    fun calcularDiasAtraso(): Long {
        val dias = ChronoUnit.DAYS.between(
            fechaPrestamo,
            fechaDevolucion
        )

        return if (dias > 0) dias else 0
    }

    fun calcularMultaTotal(): Double {
        return calcularDiasAtraso() * tipoUsuario.multaDiaria
    }

    fun obtenerEstado(): String {
        val diasAtraso = calcularDiasAtraso()

        return if (diasAtraso == 0L) {
            "Devuelto a tiempo. No tiene mora."
        } else {
            "Devuelto con $diasAtraso día(s) de atraso."
        }
    }

    fun generarDetalleMulta(): List<DetalleMulta> {

        val detalles = mutableListOf<DetalleMulta>()

        val diasAtraso = calcularDiasAtraso()

        var acumulado = 0.0

        for (dia in 1..diasAtraso.toInt()) {

            val fechaMulta =
                fechaPrestamo.plusDays(dia.toLong())

            acumulado += tipoUsuario.multaDiaria

            detalles.add(
                DetalleMulta(
                    dia = dia,
                    fecha = fechaMulta,
                    multaDia = tipoUsuario.multaDiaria,
                    acumulado = acumulado
                )
            )
        }

        return detalles
    }
}