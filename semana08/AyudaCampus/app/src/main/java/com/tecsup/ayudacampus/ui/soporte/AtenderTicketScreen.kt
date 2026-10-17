package com.tecsup.ayudacampus.ui.soporte

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Lock
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.model.Prioridad
import com.tecsup.ayudacampus.ui.components.AppTopBar
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.BurbujaComentario
import com.tecsup.ayudacampus.ui.components.numeroTicket
import com.tecsup.ayudacampus.ui.theme.EstadoResuelto
import com.tecsup.ayudacampus.ui.theme.colorEstado
import com.tecsup.ayudacampus.ui.theme.colorPrioridad
import com.tecsup.ayudacampus.viewmodel.AtenderViewModel

@Composable
fun AtenderTicketScreen(
    vm: AtenderViewModel,
    usuarioId: Int,
    alVolver: () -> Unit
) {
    val detalle by vm.ticket.collectAsStateWithLifecycle()
    val estadoElegido by vm.estadoElegido.collectAsStateWithLifecycle()
    val prioridadElegida by vm.prioridadElegida.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    var respuesta by rememberSaveable { mutableStateOf("") }

    val actual = detalle

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        AppTopBar(
            titulo = if (actual != null) "Atender ticket ${numeroTicket(actual.ticket.id)}" else "Atender ticket",
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
            val estadoActual = EstadoTicket.valueOf(actual.ticket.estado)
            val cerrado = estadoActual == EstadoTicket.CERRADO
            val estado = estadoElegido ?: estadoActual
            val prioridad = prioridadElegida ?: Prioridad.valueOf(actual.ticket.prioridad)
            val hayCambios = estadoElegido != null || prioridadElegida != null
            val comentarios = actual.comentarios.sortedBy { it.fecha }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                item { DatosTicket(actual) }

                if (cerrado) {
                    item { AvisoCerrado(actual.ticket.calificacion ?: 0) }
                }

                item { Titulo("Estado") }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        EstadoTicket.entries.forEach { e ->
                            OpcionPastilla(
                                texto = e.etiqueta,
                                seleccionada = e == estado,
                                color = colorEstado(e),
                                habilitada = !cerrado && e != EstadoTicket.CERRADO,
                                alPulsar = { vm.cambiarEstado(e) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                item { Titulo("Prioridad") }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Prioridad.entries.forEach { p ->
                            OpcionPastilla(
                                texto = p.etiqueta,
                                seleccionada = p == prioridad,
                                color = colorPrioridad(p),
                                habilitada = !cerrado,
                                alPulsar = { vm.cambiarPrioridad(p) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                item { Titulo("Conversación") }

                if (comentarios.isEmpty()) {
                    item {
                        Text("Aún no hay comentarios", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                items(comentarios, key = { it.id }) { comentario ->
                    BurbujaComentario(
                        comentario = comentario,
                        propio = comentario.autorId == usuarioId
                    )
                }
            }

            if (!cerrado) {
                CampoRespuesta(
                    texto = respuesta,
                    alCambiar = { respuesta = it },
                    alEnviar = {
                        vm.responder(respuesta)
                        respuesta = ""
                    }
                )
                mensaje?.let {
                    Text(
                        it,
                        color = EstadoResuelto,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                AyudaButton(
                    texto = "Guardar cambios",
                    alPulsar = vm::guardar,
                    habilitado = hayCambios,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun Titulo(texto: String) {
    Text(texto, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
}

@Composable
private fun DatosTicket(detalle: TicketConDetalle) {
    val ticket = detalle.ticket
    val partes = listOfNotNull(
        detalle.cliente?.nombre,
        ticket.ubicacion.ifBlank { null },
        detalle.categoria?.nombre
    )
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(ticket.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                partes.joinToString(" · "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(ticket.descripcion, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun AvisoCerrado(calificacion: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(14.dp)
    ) {
        Icon(Icons.Outlined.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(10.dp))
        Text(
            "Cerrado por el estudiante · $calificacion de 5 estrellas",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun OpcionPastilla(
    texto: String,
    seleccionada: Boolean,
    color: Color,
    habilitada: Boolean,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(50)
    val colorTexto = when {
        seleccionada -> Color.White
        !habilitada -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.onSurface
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(40.dp)
            .clip(forma)
            .background(if (seleccionada) color else MaterialTheme.colorScheme.surface)
            .border(1.dp, if (seleccionada) color else MaterialTheme.colorScheme.outline, forma)
            .clickable(enabled = habilitada, onClick = alPulsar)
    ) {
        Text(
            texto,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = colorTexto,
            maxLines = 1
        )
    }
}

@Composable
private fun CampoRespuesta(
    texto: String,
    alCambiar: (String) -> Unit,
    alEnviar: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = texto,
            onValueChange = alCambiar,
            placeholder = { Text("Responder al cliente...") },
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
}