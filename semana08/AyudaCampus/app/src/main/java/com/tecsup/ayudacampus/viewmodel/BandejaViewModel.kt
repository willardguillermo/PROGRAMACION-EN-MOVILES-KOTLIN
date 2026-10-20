package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.model.InfoSla
import com.tecsup.ayudacampus.model.calcularSla
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class BandejaViewModel(repo: AyudaRepository) : ViewModel() {

    private val todosLosTickets = repo.observarTodosLosTickets()

    private val _filtro = MutableStateFlow<EstadoTicket?>(null)
    val filtro: StateFlow<EstadoTicket?> = _filtro.asStateFlow()

    val tickets: StateFlow<List<TicketConDetalle>> =
        combine(todosLosTickets, _filtro) { lista, filtro ->
            if (filtro == null) lista else lista.filter { it.ticket.estado == filtro.name }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val reloj = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(60_000)
        }
    }

    val slaTickets: StateFlow<Map<Int, InfoSla>> = combine(todosLosTickets, reloj) { lista, ahora ->
        lista.mapNotNull { detalle ->
            val ticket = detalle.ticket
            calcularSla(ticket.prioridad, ticket.estado, ticket.fecha, ahora)?.let { ticket.id to it }
        }.toMap()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun elegirFiltro(estado: EstadoTicket?) {
        _filtro.value = estado
    }
}