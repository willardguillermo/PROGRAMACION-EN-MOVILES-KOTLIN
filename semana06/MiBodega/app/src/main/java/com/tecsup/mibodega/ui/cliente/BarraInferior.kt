package com.tecsup.mibodega.ui.cliente

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecsup.mibodega.ui.theme.VerdeBodega

/** Un destino del menú inferior: a qué ruta va, qué texto y qué ícono muestra. */
data class DestinoBarra(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)

/** Los 4 destinos de la NavigationBar (mockup pantalla 3). */
val destinosBarra = listOf(
    DestinoBarra(Rutas.INICIO, "Inicio", Icons.Default.Home),
    DestinoBarra(Rutas.CATEGORIAS, "Categorías", Icons.Default.GridView),
    DestinoBarra(Rutas.PEDIDOS, "Pedidos", Icons.Default.Receipt),
    DestinoBarra(Rutas.PERFIL, "Perfil", Icons.Default.Person)
)

/**
 * Menú inferior compartido por Inicio, Categorías, Pedidos y Perfil.
 * No navega solo: avisa a ClienteApp qué ruta tocaron (state hoisting).
 *
 * @param rutaActual ruta de la pantalla que la está mostrando (para pintar el ítem activo)
 */
@Composable
fun BarraInferior(
    rutaActual: String,
    onNavegar: (String) -> Unit
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        destinosBarra.forEach { destino ->
            NavigationBarItem(
                selected = rutaActual == destino.ruta,
                onClick = { onNavegar(destino.ruta) },
                icon = { Icon(destino.icono, contentDescription = destino.etiqueta) },
                label = { Text(destino.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VerdeBodega,
                    selectedTextColor = VerdeBodega,
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}