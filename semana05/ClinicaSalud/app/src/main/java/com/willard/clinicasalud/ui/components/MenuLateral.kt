package com.willard.clinicasalud.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Cada opción del menú tiene su ruta, su texto y su ícono
data class OpcionMenu(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
)

@Composable
fun MenuLateral(
    opciones: List<OpcionMenu>,
    rutaActual: String?,              // para saber qué opción resaltar
    onOpcionClick: (String) -> Unit   // envía la ruta elegida
) {
    ModalDrawerSheet {

        //CABECERA DEL MENU
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "GW",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Guillermo Willard", fontWeight = FontWeight.Bold)
                Text("Paciente", style = MaterialTheme.typography.bodySmall)
            }
        }

        HorizontalDivider()
        Spacer(Modifier.height(8.dp))

        //OPCIONES DEL MENU
        // La opción de la pantalla actual sale resaltada
        opciones.forEach { opcion ->
            NavigationDrawerItem(
                label = { Text(opcion.titulo) },
                icon = { Icon(opcion.icono, contentDescription = null) },
                selected = rutaActual == opcion.ruta,
                onClick = { onOpcionClick(opcion.ruta) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}