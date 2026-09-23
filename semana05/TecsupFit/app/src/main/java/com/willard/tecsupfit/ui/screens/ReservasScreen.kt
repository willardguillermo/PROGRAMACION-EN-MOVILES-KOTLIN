@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.tecsupfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.willard.tecsupfit.data.EstadoReserva
import com.willard.tecsupfit.data.Reserva

@Composable
fun ReservasScreen(
    reservas: List<Reserva>,                // la lista viene desde AppNavegacion
    barraInferior: @Composable () -> Unit,  // la barra de pestañas también viene desde AppNavegacion
    onCompletar: (Int) -> Unit,             // envía el id de la reserva a completar
    onCancelar: (Int) -> Unit               // envía el id de la reserva a cancelar
) {
    Scaffold(
        //TOP BAR
        topBar = {
            TopAppBar(title = { Text("Mis reservas") })
        },
        //BOTTOM BAR
        bottomBar = barraInferior
    ) { innerPadding ->

        //SIN RESERVAS
        if (reservas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no tienes reservas")
            }
        } else {

            //LISTA DE RESERVAS
            // reversed() para que la más reciente salga primero
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reservas.reversed(), key = { it.id }) { reserva ->
                    TarjetaReserva(
                        reserva = reserva,
                        onCompletar = { onCompletar(reserva.id) },
                        onCancelar = { onCancelar(reserva.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaReserva(
    reserva: Reserva,
    onCompletar: () -> Unit,
    onCancelar: () -> Unit
) {
    val confirmada = reserva.estado == EstadoReserva.CONFIRMADA

    // Controla si el AlertDialog está visible
    // rememberSaveable para que no se cierre solo al girar la pantalla
    var mostrarDialogo by rememberSaveable { mutableStateOf(false) }

    //TEXTO Y COLORES SEGUN ESTADO
    // Confirmada = verde, Completada = gris, Cancelada = rojo
    val textoEstado = when (reserva.estado) {
        EstadoReserva.CONFIRMADA -> "Confirmada"
        EstadoReserva.COMPLETADA -> "Completada"
        EstadoReserva.CANCELADA -> "Cancelada"
    }
    val colorTexto = when (reserva.estado) {
        EstadoReserva.CONFIRMADA -> Color(0xFF1B6B4E)
        EstadoReserva.COMPLETADA -> Color(0xFF616161)
        EstadoReserva.CANCELADA -> Color(0xFFC62828)
    }
    val colorFondo = when (reserva.estado) {
        EstadoReserva.CONFIRMADA -> Color(0xFFD3EFE2)
        EstadoReserva.COMPLETADA -> Color(0xFFEEEEEE)
        EstadoReserva.CANCELADA -> Color(0xFFFFEBEE)
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {

            //BARRA LATERAL
            // Solo las confirmadas llevan la barra verde
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(
                        if (confirmada) MaterialTheme.colorScheme.primary else Color.Transparent
                    )
            )

            //DATOS DE LA RESERVA
            Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                Text(reserva.clase.nombre, fontWeight = FontWeight.Bold)
                Text(
                    "${reserva.clase.dia}, ${reserva.horario}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "${reserva.clase.sala} · ${reserva.clase.instructor}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                //ETIQUETA DE ESTADO
                Spacer(Modifier.height(8.dp))
                Text(
                    text = textoEstado,
                    color = colorTexto,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .background(colorFondo, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )

                //BOTONES DE ACCION
                // Solo aparecen mientras la reserva sigue confirmada
                if (confirmada) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { mostrarDialogo = true }) {
                            Text("Cancelar reserva", color = Color(0xFFC62828))
                        }
                        TextButton(onClick = onCompletar) {
                            Text("Completar")
                        }
                    }
                }
            }
        }
    }

    //DIALOGO DE CONFIRMACION
    // Si toca fuera del diálogo o "No", solo se cierra
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Cancelar reserva") },
            text = {
                Text("¿Seguro que deseas cancelar tu reserva de ${reserva.clase.nombre} (${reserva.horario})? El cupo quedará libre para otra persona.")
            },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogo = false
                    onCancelar()
                }) {
                    Text("Sí, cancelar", color = Color(0xFFC62828))
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("No")
                }
            }
        )
    }
}