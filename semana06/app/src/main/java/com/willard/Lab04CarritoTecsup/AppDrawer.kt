package com.willard.Lab04CarritoTecsup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun AppDrawer(
    opciones: List<OpcionMenu>,
    rutaActual: String?,
    onOpcionClick: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    ModalDrawerSheet {

        //ENCABEZADO DEL USUARIO
        EncabezadoDrawer()
        Spacer(Modifier.height(12.dp))

        //OPCIONES DEL MENU
        // La opción de la pantalla actual sale resaltada con color de fondo distinto
        opciones.forEach { opcion ->
            NavigationDrawerItem(
                label = { Text(opcion.titulo) },
                icon = { Icon(opcion.icono, contentDescription = null) },
                selected = rutaActual == opcion.ruta,
                onClick = { onOpcionClick(opcion.ruta) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }

        //CERRAR SESION (abajo del todo)
        // weight(1f) ocupa todo el espacio libre y empuja lo siguiente al fondo
        Spacer(Modifier.weight(1f))
        HorizontalDivider()
        NavigationDrawerItem(
            label = { Text("Cerrar sesión") },
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
            selected = false,
            onClick = onCerrarSesion,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun EncabezadoDrawer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(24.dp)
    ) {
        // Avatar con iniciales
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(MaterialTheme.colorScheme.onPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "GW",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            "Guillermo Willard",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            "Cliente · Carrito TECSUP",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}