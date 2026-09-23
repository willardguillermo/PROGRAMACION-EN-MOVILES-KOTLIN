@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.clinicasalud.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.willard.clinicasalud.data.Cita
import com.willard.clinicasalud.data.EstadoCita

@Composable
fun MisCitasScreen(
    citas: List<Cita>,              // la lista viene desde AppNavegacion
    onAbrirMenu: () -> Unit,        // abre el menú lateral
    onCompletar: (Int) -> Unit,     // envía el id de la cita a completar
    onCancelar: (Int) -> Unit       // envía el id de la cita a cancelar
) {
    Scaffold(
        //TOP BAR
        topBar = {
            TopAppBar(
                title = { Text("Mis citas") },
                navigationIcon = {
                    IconButton(onClick = onAbrirMenu) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                }
            )
        }
    ) { innerPadding ->

        //SIN CITAS
        if (citas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no tienes citas agendadas")
            }
        } else {

            //LISTA DE CITAS
            // reversed() para que la cita más reciente salga primero
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(citas.reversed(), key = { it.id }) { cita ->
                    TarjetaCita(
                        cita = cita,
                        onCompletar = { onCompletar(cita.id) },
                        onCancelar = { onCancelar(cita.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaCita(
    cita: Cita,
    onCompletar: () -> Unit,
    onCancelar: () -> Unit
) {
    val confirmada = cita.estado == EstadoCita.CONFIRMADA

    // Controla si el AlertDialog está visible
    // rememberSaveable para que no se cierre solo al girar la pantalla
    var mostrarDialogo by rememberSaveable { mutableStateOf(false) }

    //TEXTO Y COLORES SEGUN ESTADO
    // Confirmada = verde, Completada = gris, Cancelada = rojo
    val textoEstado = when (cita.estado) {
        EstadoCita.CONFIRMADA -> "Confirmada"
        EstadoCita.COMPLETADA -> "Completada"
        EstadoCita.CANCELADA -> "Cancelada"
    }
    val colorTexto = when (cita.estado) {
        EstadoCita.CONFIRMADA -> Color(0xFF2E7D32)
        EstadoCita.COMPLETADA -> Color(0xFF616161)
        EstadoCita.CANCELADA -> Color(0xFFC62828)
    }
    val colorFondo = when (cita.estado) {
        EstadoCita.CONFIRMADA -> Color(0xFFE8F5E9)
        EstadoCita.COMPLETADA -> Color(0xFFEEEEEE)
        EstadoCita.CANCELADA -> Color(0xFFFFEBEE)
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {

            //BARRA LATERAL
            // Solo las confirmadas llevan la barra morada
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(
                        if (confirmada) MaterialTheme.colorScheme.primary else Color.Transparent
                    )
            )

            //DATOS DE LA CITA
            Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                Text(cita.medico.nombre, fontWeight = FontWeight.Bold)
                Text(cita.medico.especialidad, style = MaterialTheme.typography.bodySmall)
                Text("${cita.fecha}, ${cita.hora}", style = MaterialTheme.typography.bodyMedium)

                //ETIQUETA DE ESTADO
                Spacer(Modifier.height(8.dp))
                Text(
                    text = textoEstado,
                    color = colorTexto,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .background(colorFondo, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )

                //BOTONES DE ACCION
                // Solo aparecen mientras la cita sigue confirmada
                if (confirmada) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { mostrarDialogo = true }) {
                            Text("Cancelar cita", color = Color(0xFFC62828))
                        }
                        TextButton(onClick = onCompletar) {
                            Text("Marcar como completada")
                        }
                    }
                }
            }
        }
    }

    //DIALOGO DE CONFIRMACION
    // Si toca fuera del diálogo o "No", solo se cierra
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Cancelar cita") },
            text = { Text("¿Estás seguro de que deseas cancelar esta cita?") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogo = false
                    onCancelar()
                }) {
                    Text("Sí, cancelar", color = Color(0xFFC62828))
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("No")
                }
            }
        )
    }
}