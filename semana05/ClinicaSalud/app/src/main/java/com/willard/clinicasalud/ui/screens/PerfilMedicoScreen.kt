@file:OptIn(ExperimentalMaterial3Api::class)

package com.willard.clinicasalud.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.willard.clinicasalud.data.DatosClinica

@Composable
fun PerfilMedicoScreen(
    medicoId: Int,
    onVolver: () -> Unit,
    onAgendarClick: (Int) -> Unit
) {
    // Solo llega el id, se busca el medico completo en los datos
    val medico = DatosClinica.buscarMedico(medicoId)

    Scaffold(
        //TOP BAR
        topBar = {
            TopAppBar(
                title = { Text("Perfil del médico") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->

        // MÉDICO NO ENCONTRADO
        // Por si llega un id que no existe
        if (medico == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Médico no encontrado")
            }
        } else {

            //CONTENIDO
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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
                    Icon(
                        Icons.Default.Add, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(56.dp)
                    )
                }

                //NOMBRE Y ESPECIALIDAD
                Spacer(Modifier.height(16.dp))
                Text(
                    medico.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${medico.especialidad} · ${medico.añosExperiencia} años de experiencia",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                //CALIFICACIÓN
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star, contentDescription = null,
                        tint = Color(0xFFFFB300), modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("${medico.calificacion} (${medico.reseñas} reseñas)")
                }

                //DESCRIPCIÓN
                Spacer(Modifier.height(24.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Acerca del médico", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text(medico.descripcion)
                    }
                }

                // Con weight(1f) empujo el botón hasta abajo
                Spacer(Modifier.weight(1f))

                //BOTÓN AGENDAR
                Button(
                    onClick = { onAgendarClick(medico.id) },
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text("Agendar cita")
                }
            }
        }
    }
}