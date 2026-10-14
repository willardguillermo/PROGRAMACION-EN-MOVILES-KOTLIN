package com.tecsup.ayudacampus.ui.theme

import androidx.compose.ui.graphics.Color
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.model.Prioridad

val Cian = Color(0xFF0891B2)
val CianContenedor = Color(0xFFCFFAFE)
val CianFondo = Color(0xFFF7FDFE)
val CianOscuro = Color(0xFF164E63)
val CianClaro = Color(0xFF67E8F9)

val Violeta = Color(0xFF7C3AED)
val VioletaContenedor = Color(0xFFEDE4FF)
val VioletaFondo = Color(0xFFFAF8FF)
val VioletaOscuro = Color(0xFF4C1D95)
val VioletaClaro = Color(0xFFC4B5FD)

val EstadoAbierto = Color(0xFFD97706)
val EstadoEnProceso = Color(0xFF2563EB)
val EstadoResuelto = Color(0xFF16A34A)
val EstadoCerrado = Color(0xFF6B7280)

val PrioridadAlta = Color(0xFFDC2626)
val PrioridadMedia = Color(0xFFB45309)
val PrioridadBaja = Color(0xFF6B7280)

val TextoPrincipal = Color(0xFF0F172A)
val TextoSecundario = Color(0xFF64748B)
val Borde = Color(0xFFE2E8F0)
val Gris100 = Color(0xFFF1F5F9)

val FondoOscuro = Color(0xFF0B1220)
val SuperficieOscura = Color(0xFF162032)
val BordeOscuro = Color(0xFF2A3A52)

fun colorEstado(estado: EstadoTicket): Color = when (estado) {
    EstadoTicket.ABIERTO -> EstadoAbierto
    EstadoTicket.EN_PROCESO -> EstadoEnProceso
    EstadoTicket.RESUELTO -> EstadoResuelto
    EstadoTicket.CERRADO -> EstadoCerrado
}

fun colorPrioridad(prioridad: Prioridad): Color = when (prioridad) {
    Prioridad.ALTA -> PrioridadAlta
    Prioridad.MEDIA -> PrioridadMedia
    Prioridad.BAJA -> PrioridadBaja
}