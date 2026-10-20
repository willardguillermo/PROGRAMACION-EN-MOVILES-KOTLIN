package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.entity.Categoria
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.model.InfoSla
import com.tecsup.ayudacampus.model.Prioridad
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

    private val _prioridad = MutableStateFlow<Prioridad?>(null)
    val prioridad: StateFlow<Prioridad?> = _prioridad.asStateFlow()

    private val _categoriaId = MutableStateFlow<Int?>(null)
    val categoriaId: StateFlow<Int?> = _categoriaId.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    val categorias: StateFlow<List<Categoria>> = repo.observarCategorias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val hayFiltros: StateFlow<Boolean> =
        combine(_filtro, _prioridad, _categoriaId, _busqueda) { estado, prio, catId, texto ->
            estado != null || prio != null || catId != null || texto.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val tickets: StateFlow<List<TicketConDetalle>> =
        combine(todosLosTickets, _filtro, _prioridad, _categoriaId, _busqueda) { lista, estado, prio, catId, texto ->
            lista.filter { detalle ->
                val t = detalle.ticket
                val matchEstado = estado == null || t.estado == estado.name
                val matchPrioridad = prio == null || t.prioridad == prio.name
                val matchCat = catId == null || t.categoriaId == catId
                val matchTexto = coincide(detalle, texto)
                matchEstado && matchPrioridad && matchCat && matchTexto
            }
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

    fun elegirFiltro(estado: EstadoTicket?) { _filtro.value = estado }
    fun elegirPrioridad(prio: Prioridad?) { _prioridad.value = prio }
    fun elegirCategoria(id: Int?) { _categoriaId.value = id }
    fun buscar(texto: String) { _busqueda.value = texto }
    
    fun limpiarFiltros() {
        _filtro.value = null
        _prioridad.value = null
        _categoriaId.value = null
        _busqueda.value = ""
    }

    private fun coincide(detalle: TicketConDetalle, texto: String): Boolean {
        val limpio = texto.trim().removePrefix("#")
        if (limpio.isEmpty()) return true
        val numero = limpio.trimStart('0')
        val matchId = numero.isNotEmpty() && detalle.ticket.id.toString() == numero
        val matchTitulo = detalle.ticket.titulo.contains(limpio, ignoreCase = true)
        val matchCliente = detalle.cliente?.nombre?.contains(limpio, ignoreCase = true) ?: false
        return matchId || matchTitulo || matchCliente
    }
}
