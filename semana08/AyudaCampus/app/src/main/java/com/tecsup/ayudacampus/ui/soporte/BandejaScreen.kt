package com.tecsup.ayudacampus.ui.soporte

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.ui.components.AppTopBar
import com.tecsup.ayudacampus.ui.components.TicketCard
import com.tecsup.ayudacampus.viewmodel.BandejaViewModel

private val filtrosBandeja = listOf(
    null to "Todos",
    EstadoTicket.ABIERTO to "Abiertos",
    EstadoTicket.EN_PROCESO to "En proceso",
    EstadoTicket.RESUELTO to "Resueltos"
)

@Composable
fun BandejaScreen(vm: BandejaViewModel, alAbrirTicket: (Int) -> Unit) {
    val tickets by vm.tickets.collectAsStateWithLifecycle()
    val filtro by vm.filtro.collectAsStateWithLifecycle()
    val slas by vm.slaTickets.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(titulo = "Tickets")

        ChipsBandeja(seleccionado = filtro, alElegir = vm::elegirFiltro)

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (tickets.isEmpty()) {
                item {
                    Text(
                        "No hay tickets con este filtro",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            items(tickets, key = { it.ticket.id }) { detalle ->
                TicketCard(
                    detalle = detalle,
                    alPulsar = { alAbrirTicket(detalle.ticket.id) },
                    mostrarCliente = true,
                    sla = slas[detalle.ticket.id]
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChipsBandeja(seleccionado: EstadoTicket?, alElegir: (EstadoTicket?) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        filtrosBandeja.forEach { (estado, texto) ->
            FilterChip(
                selected = seleccionado == estado,
                onClick = { alElegir(estado) },
                label = { Text(texto) },
                shape = RoundedCornerShape(50),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}