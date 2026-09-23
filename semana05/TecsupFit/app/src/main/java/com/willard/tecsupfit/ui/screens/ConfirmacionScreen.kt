@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.tecsupfit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.willard.tecsupfit.data.DatosGimnasio

@Composable
fun ConfirmacionScreen(
    claseId: Int,
    horarioIndex: Int,
    onVerReservas: () -> Unit,
    onVolverInicio: () -> Unit
) {
    // Con los 2 datos que llegan por la ruta armo el resumen
    val clase = DatosGimnasio.buscarClase(claseId)
    val horario = clase?.horarios?.getOrNull(horarioIndex)?.hora ?: "-"

    Scaffold(
        //TOP BAR
        // Sin flecha de volver para no reservar dos veces la misma clase
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
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(96.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "¡Cupo reservado!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            //RESUMEN DE LA RESERVA
            Spacer(Modifier.height(24.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaResumen("Clase", clase?.nombre ?: "-")
                    FilaResumen("Instructor", clase?.instructor ?: "-")
                    FilaResumen("Día", clase?.dia ?: "-")
                    FilaResumen("Horario", horario)
                    FilaResumen("Sala", clase?.sala ?: "-")
                }
            }

            Spacer(Modifier.weight(1f))

            //BOTON VER RESERVAS
            Button(
                onClick = onVerReservas,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Ver mis reservas")
            }

            //BOTON VOLVER AL INICIO
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = onVolverInicio,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Volver al inicio")
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