package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.ui.components.graficos.DatoGrafico
import com.tecsup.ayudacampus.ui.theme.colorEstado
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class PanelViewModel(private val repo: AyudaRepository) : ViewModel() {

    val abiertos: StateFlow<Int> = cantidad(EstadoTicket.ABIERTO).enEstado(0)
    val enProceso: StateFlow<Int> = cantidad(EstadoTicket.EN_PROCESO).enEstado(0)
    val resueltos: StateFlow<Int> = cantidad(EstadoTicket.RESUELTO).enEstado(0)

    val porEstado: StateFlow<List<DatoGrafico>> = combine(
        cantidad(EstadoTicket.ABIERTO),
        cantidad(EstadoTicket.EN_PROCESO),
        cantidad(EstadoTicket.RESUELTO),
        cantidad(EstadoTicket.CERRADO)
    ) { a, p, r, c ->
        listOf(
            EstadoTicket.ABIERTO to a,
            EstadoTicket.EN_PROCESO to p,
            EstadoTicket.RESUELTO to r,
            EstadoTicket.CERRADO to c
        ).map { (estado, valor) ->
            DatoGrafico(estado.etiqueta, valor, colorEstado(estado))
        }
    }.enEstado(emptyList())

    val porCategoria: StateFlow<List<DatoGrafico>> = repo.observarCantidadPorCategoria()
        .map { lista ->
            lista.map { DatoGrafico(it.nombre.substringBefore(" "), it.cantidad) }
        }
        .enEstado(emptyList())

    private val reloj: Flow<Long> = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(60_000)
        }
    }

    val altasSinAtender: StateFlow<Int> = reloj
        .flatMapLatest { ahora -> repo.observarAltasSinAtender(ahora - UNA_HORA) }
        .enEstado(0)

    val tiempoPromedioResolucion: StateFlow<String> = repo.observarTiempoPromedioResolucion()
        .map { ms -> formatearDuracion(ms) }
        .enEstado("Sin datos")

    private fun cantidad(estado: EstadoTicket): Flow<Int> =
        repo.observarCantidadPorEstado(estado)

    private fun formatearDuracion(ms: Long?): String {
        if (ms == null) return "Sin datos"
        val minutosTotales = ms / 60_000
        return "${minutosTotales / 60} h ${minutosTotales % 60} min"
    }

    private fun <T> Flow<T>.enEstado(inicial: T): StateFlow<T> =
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), inicial)

    private companion object {
        const val UNA_HORA = 60 * 60 * 1000L
    }
}