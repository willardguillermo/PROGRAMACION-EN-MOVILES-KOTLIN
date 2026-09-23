@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.tecsupfit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.willard.tecsupfit.data.DatosGimnasio

@Composable
fun ReservarCupoScreen(
    claseId: Int,
    onVolver: () -> Unit,
    onConfirmar: (Int) -> Unit      // envía la posición del horario elegido
) {
    val clase = DatosGimnasio.buscarClase(claseId)

    // Guardo la posición del horario elegido, null = todavía no elige
    var horarioSeleccionado by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        //TOP BAR
        topBar = {
            TopAppBar(
                title = { Text("Reservar cupo") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->

        //CONTENIDO
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {

            //CLASE ELEGIDA
            if (clase != null) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(clase.nombre, fontWeight = FontWeight.Bold)
                        Text(
                            "${clase.dia} · ${clase.sala} · ${clase.duracionMin} min",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                //SELECCION DE HORARIO
                Spacer(Modifier.height(24.dp))
                Text("Selecciona un horario", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    clase.horarios.forEachIndexed { index, horario ->
                        val hayCupos = horario.cuposDisponibles > 0
                        FilterChip(
                            selected = horarioSeleccionado == index,
                            onClick = { horarioSeleccionado = index },   // al elegir uno, los demás se desmarcan
                            enabled = hayCupos,                          // sin cupos no se puede elegir
                            label = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(horario.hora, fontWeight = FontWeight.Bold)
                                    // Debajo de la hora muestro los cupos que quedan
                                    if (hayCupos) {
                                        Text(
                                            "${horario.cuposDisponibles} cupos",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    } else {
                                        Text(
                                            "Lleno",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFFC62828)
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Empujo el botón hasta abajo
            Spacer(Modifier.weight(1f))

            //BOTON CONFIRMAR
            // Solo se activa cuando ya eligió un horario
            val horario = horarioSeleccionado
            Button(
                onClick = { if (horario != null) onConfirmar(horario) },
                enabled = horario != null,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Confirmar reserva")
            }
        }
    }
}