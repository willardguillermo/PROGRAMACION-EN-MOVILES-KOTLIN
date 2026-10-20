package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.data.local.entity.Comentario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TicketDetalleViewModel(
    private val repo: AyudaRepository,
    private val ticketId: Int,
    private val usuario: Usuario
) : ViewModel() {

    val ticket: StateFlow<TicketConDetalle?> = repo.observarTicket(ticketId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val comentariosVisibles: StateFlow<List<Comentario>> =
        ticket.map { detalle ->
            detalle?.comentarios?.filter { !it.interno }?.sortedBy { it.fecha } ?: emptyList()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _calificacion = MutableStateFlow(0)
    val calificacion: StateFlow<Int> = _calificacion.asStateFlow()

    private fun estadoActual(): EstadoTicket? =
        ticket.value?.let { EstadoTicket.valueOf(it.ticket.estado) }

    fun comentar(texto: String) {
        if (texto.isBlank()) return
        val estado = estadoActual() ?: return
        if (estado == EstadoTicket.CERRADO) return
        viewModelScope.launch {
            repo.agregarComentario(ticketId, usuario, texto)
        }
    }

    fun elegirCalificacion(valor: Int) {
        _calificacion.value = valor.coerceIn(1, 5)
    }

    fun cerrar(calificacion: Int, comentarioFinal: String = "") {
        if (estadoActual() != EstadoTicket.RESUELTO) return
        if (calificacion !in 1..5) return
        viewModelScope.launch {
            if (comentarioFinal.isNotBlank()) {
                repo.agregarComentario(ticketId, usuario, comentarioFinal)
            }
            repo.cerrarConCalificacion(ticketId, calificacion)
            _calificacion.value = 0
        }
    }

    fun reabrir() {
        if (estadoActual() != EstadoTicket.RESUELTO) return
        viewModelScope.launch {
            repo.reabrir(ticketId)
            _calificacion.value = 0
        }
    }
}