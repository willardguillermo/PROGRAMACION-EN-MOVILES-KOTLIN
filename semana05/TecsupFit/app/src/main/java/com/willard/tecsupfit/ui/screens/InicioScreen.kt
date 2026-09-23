@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.tecsupfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.willard.tecsupfit.data.Clase
import com.willard.tecsupfit.data.DatosGimnasio

@Composable
fun InicioScreen(
    onClaseClick: (Int) -> Unit,            // envía el id de la clase elegida
    barraInferior: @Composable () -> Unit   // la barra de pestañas viene desde AppNavegacion
) {
    // Filtro elegido en la fila de chips, empieza en "Hoy"
    var filtroSeleccionado by remember { mutableStateOf("Hoy") }

    // "Hoy" = solo las clases de hoy, "Esta semana" = todas
    val clasesFiltradas = if (filtroSeleccionado == "Hoy") {
        DatosGimnasio.clases.filter { it.esHoy }
    } else {
        DatosGimnasio.clases
    }

    Scaffold(
        //TOP BAR
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("TECSUP Fit", fontWeight = FontWeight.Bold)
                        Text("Hola, Guillermo", style = MaterialTheme.typography.bodySmall)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        //BOTTOM BAR
        bottomBar = barraInferior
    ) { innerPadding ->

        //CONTENIDO
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            //FILTROS
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(DatosGimnasio.filtros) { filtro ->
                    FilterChip(
                        selected = filtro == filtroSeleccionado,
                        onClick = { filtroSeleccionado = filtro },
                        label = { Text(filtro) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "Clases disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            //LISTA DE CLASES
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(clasesFiltradas, key = { it.id }) { clase ->
                    TarjetaClase(clase = clase, onClick = { onClaseClick(clase.id) })
                }
            }
        }
    }
}

@Composable
fun TarjetaClase(clase: Clase, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //ICONO
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.FitnessCenter, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.width(12.dp))

            //NOMBRE Y HORARIO
            Column {
                Text(clase.nombre, fontWeight = FontWeight.Bold)
                // joinToString une las horas: "7:00 am, 12:00 pm, 7:00 pm"
                Text(
                    clase.horarios.joinToString { it.hora },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "${clase.dia} · ${clase.sala}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}