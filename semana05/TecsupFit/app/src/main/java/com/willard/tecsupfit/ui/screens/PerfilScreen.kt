@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.tecsupfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.willard.tecsupfit.data.EstadoReserva
import com.willard.tecsupfit.data.Reserva

@Composable
fun PerfilScreen(
    reservas: List<Reserva>,                // la misma lista de AppNavegacion
    barraInferior: @Composable () -> Unit   // la barra de pestañas viene desde AppNavegacion
) {
    //ESTADISTICAS
    // Clases tomadas = reservas completadas
    val clasesTomadas = reservas.count { it.estado == EstadoReserva.COMPLETADA }

    // Activas = reservas confirmadas que todavía no se hacen
    val activas = reservas.count { it.estado == EstadoReserva.CONFIRMADA }

    // Canceladas = reservas que el usuario canceló
    val canceladas = reservas.count { it.estado == EstadoReserva.CANCELADA }

    // Racha = completadas seguidas contando desde la más reciente.
    // Ignoro las confirmadas porque todavía no se hacen (no rompen la racha).
    // Las CANCELADAS sí pasan el filter, y takeWhile se detiene en ellas: rompen la racha.
    val racha = reservas
        .filter { it.estado != EstadoReserva.CONFIRMADA }
        .reversed()
        .takeWhile { it.estado == EstadoReserva.COMPLETADA }
        .size

    Scaffold(
        //TOP BAR
        topBar = {
            TopAppBar(title = { Text("Mi perfil") })
        },
        //BOTTOM BAR
        bottomBar = barraInferior
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

            //NOMBRE Y PLAN
            Spacer(Modifier.height(12.dp))
            Text(
                "Guillermo Willard",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Plan Premium",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            //TARJETAS DE ESTADISTICAS
            // 4 estadísticas en 2 filas de 2 para que los textos no se amontonen
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaEstadistica("Clases", clasesTomadas, Modifier.weight(1f))
                TarjetaEstadistica("Racha", racha, Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaEstadistica("Activas", activas, Modifier.weight(1f))
                // Las canceladas en rojo para diferenciarlas
                TarjetaEstadistica(
                    "Canceladas", canceladas, Modifier.weight(1f),
                    colorNumero = Color(0xFFC62828)
                )
            }

            //DATOS PERSONALES
            // Reutilizo FilaResumen de ConfirmacionScreen
            Spacer(Modifier.height(24.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Datos personales", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    FilaResumen("Correo", "guillermo.willard@tecsup.edu.pe")
                    FilaResumen("Sede", "Tecsup Lima")
                    FilaResumen("Miembro desde", "Marzo 2026")
                }
            }
        }
    }
}

//TARJETA DE ESTADISTICA
// Número grande arriba y el nombre abajo
// colorNumero es opcional: si no se envía usa el color principal
@Composable
fun TarjetaEstadistica(
    titulo: String,
    valor: Int,
    modifier: Modifier = Modifier,
    colorNumero: Color = MaterialTheme.colorScheme.primary
) {
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
                color = colorNumero
            )
            Text(titulo, style = MaterialTheme.typography.bodySmall)
        }
    }
}