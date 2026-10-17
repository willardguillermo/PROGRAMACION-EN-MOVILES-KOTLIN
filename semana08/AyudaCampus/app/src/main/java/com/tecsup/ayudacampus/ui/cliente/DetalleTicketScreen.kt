package com.tecsup.ayudacampus.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.model.Prioridad
import com.tecsup.ayudacampus.ui.components.AppTopBar
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.AyudaTextField
import com.tecsup.ayudacampus.ui.components.BurbujaComentario
import com.tecsup.ayudacampus.ui.components.EstadoChip
import com.tecsup.ayudacampus.ui.components.Estrellas
import com.tecsup.ayudacampus.ui.components.PrioridadChip
import com.tecsup.ayudacampus.ui.components.StepperTicket
import com.tecsup.ayudacampus.ui.components.iconoCategoria
import com.tecsup.ayudacampus.ui.components.numeroTicket
import com.tecsup.ayudacampus.ui.components.tiempoRelativo
import com.tecsup.ayudacampus.ui.theme.EstadoResuelto
import com.tecsup.ayudacampus.viewmodel.TicketDetalleViewModel

@Composable
fun DetalleTicketScreen(
    vm: TicketDetalleViewModel,
    usuarioId: Int,
    alVolver: () -> Unit
) {
    val detalle by vm.ticket.collectAsStateWithLifecycle()
    val calificacion by vm.calificacion.collectAsStateWithLifecycle()
    var texto by rememberSaveable { mutableStateOf("") }
    var comentarioFinal by rememberSaveable { mutableStateOf("") }

    val actual = detalle

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        AppTopBar(
            titulo = if (actual != null) "Ticket ${numeroTicket(actual.ticket.id)}" else "Ticket",
            alVolver = alVolver
        )

        if (actual == null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            val estado = EstadoTicket.valueOf(actual.ticket.estado)
            val comentarios = actual.comentarios.sortedBy { it.fecha }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                item { Cabecera(actual) }
                item { StepperTicket(estado) }

                if (estado == EstadoTicket.RESUELTO) {
                    item { AvisoResuelto() }
                }

                item {
                    Text(
                        "Conversación",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (comentarios.isEmpty()) {
                    item {
                        Text(
                            "Aún no hay comentarios",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                items(comentarios, key = { it.id }) { comentario ->
                    BurbujaComentario(
                        comentario = comentario,
                        propio = comentario.autorId == usuarioId
                    )
                }

                if (estado == EstadoTicket.RESUELTO) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            TarjetaCalificar(
                                calificacion = calificacion,
                                alCambiar = vm::elegirCalificacion
                            )
                            AyudaTextField(
                                valor = comentarioFinal,
                                alCambiar = { comentarioFinal = it },
                                etiqueta = "Comentario final (opcional)"
                            )
                            AyudaButton(
                                texto = "Confirmar y cerrar",
                                habilitado = calificacion > 0,
                                alPulsar = {
                                    vm.cerrar(calificacion, comentarioFinal)
                                    comentarioFinal = ""
                                }
                            )
                            AyudaButton(
                                texto = "Reabrir ticket",
                                contorno = true,
                                color = MaterialTheme.colorScheme.error,
                                alPulsar = vm::reabrir
                            )
                        }
                    }
                }

                if (estado == EstadoTicket.CERRADO) {
                    item { TarjetaCerrado(actual.ticket.calificacion ?: 0) }
                }
            }

            if (estado != EstadoTicket.CERRADO) {
                CampoComentario(
                    texto = texto,
                    alCambiar = { texto = it },
                    alEnviar = {
                        vm.comentar(texto)
                        texto = ""
                    }
                )
            }
        }
    }
}

@Composable
private fun Cabecera(detalle: TicketConDetalle) {
    val ticket = detalle.ticket
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(ticket.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                EstadoChip(EstadoTicket.valueOf(ticket.estado))
                PrioridadChip(Prioridad.valueOf(ticket.prioridad))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    iconoCategoria(detalle.categoria?.icono ?: "tag"),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                val partes = listOfNotNull(
                    detalle.categoria?.nombre,
                    ticket.ubicacion.ifBlank { null },
                    tiempoRelativo(ticket.fecha)
                )
                Text(
                    partes.joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                ticket.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AvisoResuelto() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(EstadoResuelto.copy(alpha = 0.12f))
            .padding(14.dp)
    ) {
        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = EstadoResuelto)
        Spacer(Modifier.width(10.dp))
        Text(
            "Soporte marcó este ticket como resuelto. ¿El problema se solucionó?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun TarjetaCalificar(calificacion: Int, alCambiar: (Int) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Califica la atención", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Estrellas(valor = calificacion, alCambiar = alCambiar)
            Text(
                if (calificacion == 0) "Toca una estrella" else "$calificacion de 5 estrellas",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TarjetaCerrado(calificacion: Int) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Ticket cerrado", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Estrellas(valor = calificacion, tamano = 28.dp)
            Text(
                "Calificación recibida: $calificacion de 5",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CampoComentario(
    texto: String,
    alCambiar: (String) -> Unit,
    alEnviar: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        OutlinedTextField(
            value = texto,
            onValueChange = alCambiar,
            placeholder = { Text("Escribe un comentario...") },
            maxLines = 3,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        FilledIconButton(
            onClick = alEnviar,
            enabled = texto.isNotBlank(),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.size(52.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
        }
    }
    Spacer(Modifier.height(4.dp))
}