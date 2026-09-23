@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.tecsupfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.willard.tecsupfit.data.DatosGimnasio
import com.willard.tecsupfit.data.Rutina

@Composable
fun RutinasScreen(
    barraInferior: @Composable () -> Unit   // la barra de pestañas viene desde AppNavegacion
) {
    Scaffold(
        //TOP BAR
        topBar = {
            TopAppBar(title = { Text("Rutinas") })
        },
        //BOTTOM BAR
        bottomBar = barraInferior
    ) { innerPadding ->

        //LISTA DE RUTINAS
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Rutinas sugeridas para entrenar por tu cuenta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            items(DatosGimnasio.rutinas, key = { it.id }) { rutina ->
                TarjetaRutina(rutina)
            }
        }
    }
}

@Composable
fun TarjetaRutina(rutina: Rutina) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {

            //ENCABEZADO
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.DirectionsRun, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(rutina.nombre, fontWeight = FontWeight.Bold)
                    Text(
                        "${rutina.nivel} · ${rutina.duracionMin} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            //EJERCICIOS
            // Muestro cada ejercicio en su propia línea con una viñeta
            Spacer(Modifier.height(8.dp))
            rutina.ejercicios.forEach { ejercicio ->
                Text("• $ejercicio", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}