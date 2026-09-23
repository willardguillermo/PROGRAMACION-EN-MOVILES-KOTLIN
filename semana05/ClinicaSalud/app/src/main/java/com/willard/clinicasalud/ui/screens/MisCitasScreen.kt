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
    onAbrirMenu: () -> Unit,        // abrirá el menú lateral
    onCompletar: (Int) -> Unit      // envía el id de la cita a completar
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
                    TarjetaCita(cita = cita, onCompletar = { onCompletar(cita.id) })
                }
            }
        }
    }
}

@Composable
fun TarjetaCita(cita: Cita, onCompletar: () -> Unit) {
    val confirmada = cita.estado == EstadoCita.CONFIRMADA

    //COLORES SEGUN ESTADO
    // Confirmada = verde, Completada = gris
    val colorTexto = if (confirmada) Color(0xFF2E7D32) else Color(0xFF616161)
    val colorFondo = if (confirmada) Color(0xFFE8F5E9) else Color(0xFFEEEEEE)

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

                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //ETIQUETA DE ESTADO
                    Text(
                        text = if (confirmada) "Confirmada" else "Completada",
                        color = colorTexto,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .background(colorFondo, RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )

                    //BOTON COMPLETAR
                    // Solo aparece mientras la cita sigue confirmada
                    if (confirmada) {
                        TextButton(onClick = onCompletar) {
                            Text("Marcar como completada")
                        }
                    }
                }
            }
        }
    }
}