@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.clinicasalud.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.willard.clinicasalud.data.DatosClinica

@Composable
fun ConfirmacionScreen(
    medicoId: Int,
    fechaIndex: Int,
    horaIndex: Int,
    onVolverInicio: () -> Unit,
    onVerMisCitas: () -> Unit
) {
    // Con los 3 datos que llegan por la ruta armo el resumen
    val medico = DatosClinica.buscarMedico(medicoId)
    val fecha = DatosClinica.fechas.getOrNull(fechaIndex) ?: "-"
    val hora = DatosClinica.horas.getOrNull(horaIndex) ?: "-"

    Scaffold(
        //TOP BAR
        // Sin flecha de volver para no regresar a agendar la misma cita
        topBar = {
            TopAppBar(title = { Text("Confirmación") })
        }
    ) { innerPadding ->

        //CONTENIDO
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))

            //ICONO DE EXITO
            Icon(
                Icons.Default.CheckCircle, contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(96.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "¡Cita agendada!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            //RESUMEN DE LA CITA
            Spacer(Modifier.height(24.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaResumen("Médico", medico?.nombre ?: "-")
                    FilaResumen("Especialidad", medico?.especialidad ?: "-")
                    FilaResumen("Fecha", fecha)
                    FilaResumen("Hora", hora)
                }
            }

            Spacer(Modifier.weight(1f))

            //BOTON VOLVER AL INICIO
            Button(
                onClick = onVolverInicio,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Volver al inicio")
            }

            //BOTON VER MIS CITAS
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = onVerMisCitas,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Ver mis citas")
            }
        }
    }
}

// Fila del resumen: etiqueta a la izquierda y valor a la derecha
@Composable
fun FilaResumen(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, fontWeight = FontWeight.Bold)
    }
}