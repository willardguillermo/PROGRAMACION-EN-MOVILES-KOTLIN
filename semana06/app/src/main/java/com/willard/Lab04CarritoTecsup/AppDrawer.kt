package com.willard.Lab04CarritoTecsup

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class OpcionMenu(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
)

@Composable
fun AppDrawer(
    opciones: List<OpcionMenu>,
    rutaActual: String?,
    onOpcionClick: (String) -> Unit
) {
    ModalDrawerSheet {
        Text(
            "Carrito TECSUP",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        HorizontalDivider()

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