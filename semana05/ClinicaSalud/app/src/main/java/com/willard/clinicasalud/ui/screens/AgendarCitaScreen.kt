@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.clinicasalud.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.willard.clinicasalud.data.DatosClinica
import androidx.compose.runtime.saveable.rememberSaveable
@Composable
fun AgendarCitaScreen(
    medicoId: Int,
    onVolver: () -> Unit,
    onConfirmar: (Int, Int) -> Unit   // posición de la fecha y de la hora elegidas
) {
    val medico = DatosClinica.buscarMedico(medicoId)

    // Guardo la posición elegida, null = todavía no elige

    // rememberSaveable para que no se pierda al girar la pantalla
    var fechaSeleccionada by rememberSaveable { mutableStateOf<Int?>(null) }
    var horaSeleccionada by rememberSaveable { mutableStateOf<Int?>(null) }

    Scaffold(
        //TOP BAR
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
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

            //MEDICO ELEGIDO
            if (medico != null) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(medico.nombre, fontWeight = FontWeight.Bold)
                        Text(medico.especialidad, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            //SELECCION DE FECHA
            Spacer(Modifier.height(24.dp))
            Text("Selecciona fecha", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DatosClinica.fechas.forEachIndexed { index, fecha ->
                    FilterChip(
                        selected = fechaSeleccionada == index,
                        onClick = { fechaSeleccionada = index },   // al elegir una, las demás se desmarcan
                        label = {
                            Text(fecha, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            //SELECCION DE HORA
            Spacer(Modifier.height(24.dp))
            Text("Selecciona hora", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DatosClinica.horas.forEachIndexed { index, hora ->
                    FilterChip(
                        selected = horaSeleccionada == index,
                        onClick = { horaSeleccionada = index },
                        label = {
                            Text(hora, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Empujo el botón hasta abajo
            Spacer(Modifier.weight(1f))

            //BOTON CONFIRMAR
            // Solo se activa cuando ya eligió fecha y hora
            val fecha = fechaSeleccionada
            val hora = horaSeleccionada
            Button(
                onClick = { if (fecha != null && hora != null) onConfirmar(fecha, hora) },
                enabled = fecha != null && hora != null,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Confirmar cita")
            }
        }
    }
}