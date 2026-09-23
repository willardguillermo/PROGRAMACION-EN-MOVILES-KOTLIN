@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.tecsupfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.willard.tecsupfit.data.DatosGimnasio
import com.willard.tecsupfit.data.Reserva

@Composable
fun DetalleClaseScreen(
    claseId: Int,                     // llega desde el NavHost
    reservas: List<Reserva>,          // para calcular los cupos reales
    onVolver: () -> Unit,
    onReservarClick: (Int) -> Unit    // enviará el id a Reservar cupo
) {
    // Solo me llega el id, así que busco la clase completa en mis datos
    val clase = DatosGimnasio.buscarClase(claseId)

    Scaffold(
        //TOP BAR
        topBar = {
            TopAppBar(
                title = { Text("Detalle de clase") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->

        //CLASE NO ENCONTRADA
        if (clase == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Clase no encontrada")
            }
        } else {
            // Sumo los cupos REALES de todos los horarios para saber si se puede reservar
            val cuposTotales = clase.horarios.sumOf { horario ->
                DatosGimnasio.cuposRestantes(clase, horario, reservas)
            }

            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

                //CONTENIDO CON SCROLL
                // weight(1f) ocupa el espacio libre y deja el botón fijo abajo
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {

                    //BANNER
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.FitnessCenter, contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(64.dp)
                        )
                    }

                    //NOMBRE Y DATOS
                    Spacer(Modifier.height(16.dp))
                    Text(
                        clase.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${clase.dia} · ${clase.sala} · ${clase.duracionMin} min",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Instructor: ${clase.instructor}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    //DESCRIPCION
                    Spacer(Modifier.height(12.dp))
                    Text(clase.descripcion)

                    //HORARIOS Y CUPOS
                    Spacer(Modifier.height(16.dp))
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Horarios", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            clase.horarios.forEach { horario ->
                                // Cupos que quedan después de restar las reservas confirmadas
                                val cupos = DatosGimnasio.cuposRestantes(clase, horario, reservas)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(horario.hora)
                                    // Si no quedan cupos lo muestro en rojo
                                    if (cupos > 0) {
                                        Text("$cupos cupos")
                                    } else {
                                        Text("Sin cupos", color = Color(0xFFC62828))
                                    }
                                }
                            }
                        }
                    }
                }

                //BOTON RESERVAR
                // Se desactiva si ningún horario tiene cupos
                Button(
                    onClick = { onReservarClick(clase.id) },
                    enabled = cuposTotales > 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(52.dp)
                ) {
                    Text("Reservar cupo")
                }
            }
        }
    }
}