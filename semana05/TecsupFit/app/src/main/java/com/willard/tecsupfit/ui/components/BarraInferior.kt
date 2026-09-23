package com.willard.tecsupfit.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

// Cada pestaña tiene su ruta, su texto y su ícono
data class Pestana(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
)

@Composable
fun BarraInferior(
    pestanas: List<Pestana>,
    rutaActual: String?,              // para saber qué pestaña resaltar
    onPestanaClick: (String) -> Unit  // envía la ruta de la pestaña tocada
) {
    NavigationBar {
        //PESTAÑAS
        // La pestaña cuya ruta es igual a la actual sale resaltada
        pestanas.forEach { pestana ->
            NavigationBarItem(
                selected = rutaActual == pestana.ruta,
                onClick = { onPestanaClick(pestana.ruta) },
                icon = { Icon(pestana.icono, contentDescription = pestana.titulo) },
                label = { Text(pestana.titulo) }
            )
        }
    }
}