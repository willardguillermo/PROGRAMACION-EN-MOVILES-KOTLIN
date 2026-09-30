package com.willard.Lab04CarritoTecsup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaFavoritos(
    productos: List<Producto>,
    favoritos: List<Long>,
    onToggleFavorito: (Producto) -> Unit
) {
    val productosFavoritos = productos.filter { it.id in favoritos }

    if (productosFavoritos.isEmpty()) {
        PantallaSimple("Aún no tienes favoritos", Icons.Default.Favorite)
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(productosFavoritos, key = { it.id }) { producto ->
            Card {
                ListItem(
                    headlineContent = { Text(producto.nombre) },
                    supportingContent = {
                        Text("S/ ${"%.2f".format(producto.precio)} x ${producto.cantidad}")
                    },
                    trailingContent = {
                        IconButton(onClick = { onToggleFavorito(producto) }) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "Quitar de favoritos",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
            }
        }
    }
}