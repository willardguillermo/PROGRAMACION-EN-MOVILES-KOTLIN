package com.willard.prestamolibros.model

import java.time.LocalDate

data class DetalleMulta(
    val dia: Int,
    val fecha: LocalDate,
    val multaDia: Double,
    val acumulado: Double
)