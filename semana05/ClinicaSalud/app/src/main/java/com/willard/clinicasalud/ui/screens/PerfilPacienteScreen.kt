@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.clinicasalud.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.willard.clinicasalud.data.Cita
import com.willard.clinicasalud.data.EstadoCita

@Composable
fun PerfilPacienteScreen(
    citas: List<Cita>,          // la misma lista de AppNavegacion
    onAbrirMenu: () -> Unit
) {
    // Cuento las citas según su estado para las estadísticas
    val confirmadas = citas.count { it.estado == EstadoCita.CONFIRMADA }
    val completadas = citas.count { it.estado == EstadoCita.COMPLETADA }

    Scaffold(
        //TOP BAR
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil") },
                navigationIcon = {
                    IconButton(onClick = onAbrirMenu) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                }
            )
        }
    ) { innerPadding ->

        //CONTENIDO
        // verticalScroll para que se pueda bajar si giro la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //AVATAR
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "GW",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            //NOMBRE
            Spacer(Modifier.height(12.dp))
            Text(
                "Guillermo Willard",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Paciente · Plan Salud+ Premium",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            //ESTADISTICAS
            // Se calculan con la lista de citas, cambian al agendar o completar
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaEstadistica("Total", citas.size, Modifier.weight(1f))
                TarjetaEstadistica("Confirmadas", confirmadas, Modifier.weight(1f))
                TarjetaEstadistica("Completadas", completadas, Modifier.weight(1f))
            }

            //DATOS PERSONALES
            // Reutilizo FilaResumen de ConfirmacionScreen
            Spacer(Modifier.height(24.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Datos personales", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    FilaResumen("Correo", "guillermo.willard@tecsup.edu.pe")
                    FilaResumen("Teléfono", "913 351 627")
                    FilaResumen("Ciudad", "Lima")
                }
            }
        }
    }
}

//TARJETA DE ESTADISTICA
// Número grande arriba y el nombre abajo
@Composable
fun TarjetaEstadistica(titulo: String, valor: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                valor.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(titulo, style = MaterialTheme.typography.bodySmall)
        }
    }
}