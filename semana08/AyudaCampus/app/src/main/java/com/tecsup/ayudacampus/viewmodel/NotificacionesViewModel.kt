package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.data.prefs.PreferenciasStore
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.Notificacion
import com.tecsup.ayudacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificacionesViewModel(
    repo: AyudaRepository,
    private val prefs: PreferenciasStore,
    private val usuario: Usuario
) : ViewModel() {

    private val rolUsuario = Rol.valueOf(usuario.rol)

    private val tickets = if (rolUsuario == Rol.CLIENTE) {
        repo.observarTicketsCliente(usuario.id)
    } else {
        repo.observarTodosLosTickets()
    }

    private val visitaAlAbrir = MutableStateFlow<Long?>(null)

    init {
        viewModelScope.launch {
            visitaAlAbrir.value = prefs.ultimaVisita(rolUsuario).first()
        }
    }

    val notificaciones: StateFlow<List<Notificacion>> =
        combine(tickets, visitaAlAbrir.filterNotNull()) { lista, ultima ->
            construir(lista, ultima)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val noLeidas: StateFlow<Int> =
        combine(notificaciones, prefs.ultimaVisita(rolUsuario), prefs.notificaciones) { lista, ultima, activas ->
            if (!activas) 0 else lista.count { it.fecha > ultima }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun marcarLeidas() {
        viewModelScope.launch {
            prefs.guardarUltimaVisita(rolUsuario, System.currentTimeMillis())
        }
    }

    private fun construir(lista: List<TicketConDetalle>, ultima: Long): List<Notificacion> {
        val resultados = mutableListOf<Notificacion>()

        for (detalle in lista) {
            val t = detalle.ticket
            val numero = "#${t.id.toString().padStart(4, '0')}"

            if (rolUsuario == Rol.ADMIN) {
                resultados.add(
                    Notificacion(
                        ticketId = t.id,
                        titulo = "Nuevo ticket $numero de ${detalle.cliente?.nombre ?: "un estudiante"}",
                        mensaje = t.titulo,
                        fecha = t.fecha,
                        nueva = t.fecha > ultima
                    )
                )
            }

            for (c in detalle.comentarios) {
                val paraCliente = rolUsuario == Rol.CLIENTE && c.autorRol == Rol.ADMIN.name && !c.interno
                val paraSoporte = rolUsuario == Rol.ADMIN && c.autorRol == Rol.CLIENTE.name

                if (paraCliente || paraSoporte) {
                    resultados.add(
                        Notificacion(
                            ticketId = t.id,
                            titulo = if (paraCliente) "Soporte respondió en $numero" else "${c.autorNombre} comentó en $numero",
                            mensaje = c.texto,
                            fecha = c.fecha,
                            nueva = c.fecha > ultima
                        )
                    )
                }
            }
        }

        return resultados.sortedByDescending { it.fecha }.take(30)
    }
}