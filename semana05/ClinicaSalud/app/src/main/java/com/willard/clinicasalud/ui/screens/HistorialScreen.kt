@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.clinicasalud.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.willard.clinicasalud.data.Cita
import com.willard.clinicasalud.data.EstadoCita

@Composable
fun HistorialScreen(
    citas: List<Cita>,          // la misma lista de AppNavegacion
    onAbrirMenu: () -> Unit
) {
    // De todas las citas solo me quedo con las completadas
    val completadas = citas.filter { it.estado == EstadoCita.COMPLETADA }

    Scaffold(
        //TOP BAR
        topBar = {
            TopAppBar(
                title = { Text("Historial médico") },
                navigationIcon = {
                    IconButton(onClick = onAbrirMenu) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                }
            )
        }
    ) { innerPadding ->

        //SIN CONSULTAS
        if (completadas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Aún no tienes consultas completadas.\nMárcalas desde Mis citas.",
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                //RESUMEN
                item {
                    Text(
                        "Consultas realizadas: ${completadas.size}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                //LISTA DE CONSULTAS
                // La más reciente primero
                items(completadas.reversed(), key = { it.id }) { cita ->
                    TarjetaHistorial(cita)
                }
            }
        }
    }
}

@Composable
fun TarjetaHistorial(cita: Cita) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //ICONO DE ATENDIDA
            Icon(
                Icons.Default.CheckCircle, contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(12.dp))

            //MEDICO
            Column(modifier = Modifier.weight(1f)) {
                Text(cita.medico.nombre, fontWeight = FontWeight.Bold)
                Text(cita.medico.especialidad, style = MaterialTheme.typography.bodySmall)
            }

            //FECHA Y HORA
            Column(horizontalAlignment = Alignment.End) {
                Text(cita.fecha, style = MaterialTheme.typography.bodyMedium)
                Text(cita.hora, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}