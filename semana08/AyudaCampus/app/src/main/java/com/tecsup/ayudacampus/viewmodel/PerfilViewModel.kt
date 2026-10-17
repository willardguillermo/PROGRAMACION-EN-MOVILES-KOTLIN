package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.EstadoTicket
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PerfilViewModel(repo: AyudaRepository, clienteId: Int) : ViewModel() {

    private val tickets = repo.observarTicketsCliente(clienteId)

    private val pendientes = setOf(EstadoTicket.ABIERTO.name, EstadoTicket.EN_PROCESO.name)
    private val atendidos = setOf(EstadoTicket.RESUELTO.name, EstadoTicket.CERRADO.name)

    val abiertos: StateFlow<Int> = tickets
        .map { lista -> lista.count { it.ticket.estado in pendientes } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val resueltos: StateFlow<Int> = tickets
        .map { lista -> lista.count { it.ticket.estado in atendidos } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val total: StateFlow<Int> = tickets
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}