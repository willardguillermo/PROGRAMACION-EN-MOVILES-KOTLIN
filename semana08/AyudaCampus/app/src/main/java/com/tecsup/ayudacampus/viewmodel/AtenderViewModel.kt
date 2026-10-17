package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AtenderViewModel(
    private val repo: AyudaRepository,
    private val ticketId: Int,
    private val usuario: Usuario
) : ViewModel() {

    val ticket: StateFlow<TicketConDetalle?> = repo.observarTicket(ticketId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _estadoElegido = MutableStateFlow<EstadoTicket?>(null)
    val estadoElegido: StateFlow<EstadoTicket?> = _estadoElegido.asStateFlow()

    private val _prioridadElegida = MutableStateFlow<Prioridad?>(null)
    val prioridadElegida: StateFlow<Prioridad?> = _prioridadElegida.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private fun estaCerrado(): Boolean =
        ticket.value?.ticket?.estado == EstadoTicket.CERRADO.name

    fun cambiarEstado(estado: EstadoTicket) {
        if (estado == EstadoTicket.CERRADO || estaCerrado()) return
        _estadoElegido.value = estado
        _mensaje.value = null
    }

    fun cambiarPrioridad(prioridad: Prioridad) {
        if (estaCerrado()) return
        _prioridadElegida.value = prioridad
        _mensaje.value = null
    }

    fun responder(texto: String) {
        if (texto.isBlank() || ticket.value == null || estaCerrado()) return
        viewModelScope.launch {
            repo.agregarComentario(ticketId, usuario, texto)
        }
    }

    fun guardar() {
        val actual = ticket.value?.ticket ?: return
        if (estaCerrado()) return
        val estado = _estadoElegido.value
        val prioridad = _prioridadElegida.value
        viewModelScope.launch {
            if (estado != null && estado.name != actual.estado) {
                repo.cambiarEstado(ticketId, estado)
            }
            if (prioridad != null && prioridad.name != actual.prioridad) {
                repo.cambiarPrioridad(ticketId, prioridad)
            }
            _estadoElegido.value = null
            _prioridadElegida.value = null
            _mensaje.value = "Cambios guardados"
        }
    }
}