@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.clinicasalud.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.willard.clinicasalud.data.DatosClinica
import com.willard.clinicasalud.data.Medico

@Composable
fun InicioScreen(
    onMedicoClick: (Int) -> Unit,   // envía el id del médico elegido
    onAbrirMenu: () -> Unit         // abre menu lateral
) {
    // Estado: especialidad elegida en la fila de chips
    var especialidadSeleccionada by remember { mutableStateOf("Todas") }

    // Se recalcula en cada recomposición según el chip elegido
    val medicosFiltrados = if (especialidadSeleccionada == "Todas") {
        DatosClinica.medicos
    } else {
        DatosClinica.medicos.filter { it.especialidad == especialidadSeleccionada }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Clínica Salud+", fontWeight = FontWeight.Bold)
                        Text("Hola, Guillermo Willard", style = MaterialTheme.typography.bodySmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onAbrirMenu) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)          // padding del Scaffold
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            // filtros por especialidad
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(DatosClinica.especialidades) { especialidad ->
                    FilterChip(
                        selected = especialidad == especialidadSeleccionada,
                        onClick = { especialidadSeleccionada = especialidad },
                        label = { Text(especialidad) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "Médicos disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            // Lista vertical de médicos
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(medicosFiltrados, key = { it.id }) { medico ->
                    TarjetaMedico(medico = medico, onClick = { onMedicoClick(medico.id) })
                }
            }
        }
    }
}

@Composable
fun TarjetaMedico(medico: Medico, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Add, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(medico.nombre, fontWeight = FontWeight.Bold)
                Text(medico.especialidad, style = MaterialTheme.typography.bodySmall)
            }
            Icon(
                Icons.Default.Star, contentDescription = null,
                tint = Color(0xFFFFB300),
                modifier = Modifier.size(18.dp)
            )
            Text(medico.calificacion.toString())
        }
    }
}