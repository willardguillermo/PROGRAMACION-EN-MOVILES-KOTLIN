package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.EstadoTicket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MisTicketsViewModel(
    repo: AyudaRepository,
    clienteId: Int
) : ViewModel() {

    private val misTickets = repo.observarTicketsCliente(clienteId)

    private val _filtro = MutableStateFlow<EstadoTicket?>(null)
    val filtro: StateFlow<EstadoTicket?> = _filtro.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    val tickets: StateFlow<List<TicketConDetalle>> =
        combine(misTickets, _filtro, _busqueda) { lista, filtro, texto ->
            lista
                .filter { filtro == null || it.ticket.estado == filtro.name }
                .filter { coincide(it, texto) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val abiertos: StateFlow<Int> = misTickets
        .map { lista -> lista.count { it.ticket.estado == EstadoTicket.ABIERTO.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun elegirFiltro(estado: EstadoTicket?) {
        _filtro.value = estado
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    private fun coincide(detalle: TicketConDetalle, texto: String): Boolean {
        val limpio = texto.trim().removePrefix("#")
        if (limpio.isEmpty()) return true
        val numero = limpio.trimStart('0')
        return detalle.ticket.titulo.contains(limpio, ignoreCase = true) ||
                (numero.isNotEmpty() && detalle.ticket.id.toString() == numero)
    }
}