@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.tecsupfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    onCompletar: (Int) -> Unit              // envía el id de la reserva a completar
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
                    TarjetaReserva(reserva = reserva, onCompletar = { onCompletar(reserva.id) })
                }
            }
        }
    }
}

@Composable
fun TarjetaReserva(reserva: Reserva, onCompletar: () -> Unit) {
    val confirmada = reserva.estado == EstadoReserva.CONFIRMADA

    //COLORES SEGUN ESTADO
    // Confirmada = verde, Completada = gris
    val colorTexto = if (confirmada) Color(0xFF1B6B4E) else Color(0xFF616161)
    val colorFondo = if (confirmada) Color(0xFFD3EFE2) else Color(0xFFEEEEEE)

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

                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //ETIQUETA DE ESTADO
                    Text(
                        text = if (confirmada) "Confirmada" else "Completada",
                        color = colorTexto,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .background(colorFondo, RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )

                    //BOTON COMPLETAR
                    // Solo aparece mientras la reserva sigue confirmada
                    if (confirmada) {
                        TextButton(onClick = onCompletar) {
                            Text("Marcar como completada")
                        }
                    }
                }
            }
        }
    }
}