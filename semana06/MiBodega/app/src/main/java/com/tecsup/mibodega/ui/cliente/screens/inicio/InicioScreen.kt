package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.BarraInferior
import com.tecsup.mibodega.ui.cliente.Rutas
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla 3: Inicio / Productos (mockup "Cliente").
 * La más completa: Scaffold (topBar + bottomBar), LazyRow de categorías
 * y LazyVerticalGrid de productos.
 *
 * @param productos lista completa (fake por ahora, luego vendrá de un ViewModel)
 * @param cantidadCarrito para el badge del carrito en la topBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    productos: List<Producto> = listaProductosFake,
    cantidadCarrito: Int,
    onVerCarrito: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit,
    onNavegarBarra: (String) -> Unit
) {
    // rememberSaveable (y no remember): al ir a Detalle y volver, la categoría
    // elegida se conserva porque Navigation guarda el estado de esta pantalla.
    var categoriaSeleccionada by rememberSaveable { mutableStateOf(listaCategorias.first()) }
    var textoBusqueda by remember { mutableStateOf("") }

    // Fase 1: solo se filtra por categoría.
    // El buscador (textoBusqueda) se conecta en la Fase 2, en la rama de IA.
    val productosFiltrados = productos.filter { producto ->
        categoriaSeleccionada == "Todos" || producto.categoria == categoriaSeleccionada
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = buildAnnotatedString {
                            append("Mi ")
                            withStyle(SpanStyle(color = VerdeBodega)) { append("Bodega") }
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onVerCarrito) {
                        BadgedBox(
                            badge = {
                                if (cantidadCarrito > 0) {
                                    Badge { Text("$cantidadCarrito") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
            )
        },
        bottomBar = { BarraInferior(rutaActual = Rutas.INICIO, onNavegar = onNavegarBarra) }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                placeholder = { Text("Buscar productos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = GrisClaro,
                    focusedContainerColor = GrisClaro,
                    unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                    focusedBorderColor = VerdeBodega
                )
            )

            // LazyRow: lista HORIZONTAL que solo dibuja los chips visibles
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(listaCategorias) { categoria ->
                    ChipCategoria(
                        texto = categoria,
                        icono = iconoCategoria(categoria),
                        seleccionado = categoria == categoriaSeleccionada,
                        onClick = { categoriaSeleccionada = categoria }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (categoriaSeleccionada == "Todos") "Productos destacados" else categoriaSeleccionada,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${productosFiltrados.size} productos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productosFiltrados) { producto ->
                    ProductoCard(
                        producto = producto,
                        onClick = { onProductoClick(producto) },
                        onAgregar = { onAgregarProducto(producto) }
                    )
                }
            }
        }
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

/** Chip cuadrado con ícono arriba y texto abajo, como en el mockup. */
@Composable
private fun ChipCategoria(
    texto: String,
    icono: ImageVector,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val fondo = if (seleccionado) VerdeBodega else GrisClaro
    val contenido = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .width(78.dp)
            .background(fondo, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = if (seleccionado) contenido else VerdeBodega,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = texto,
            color = contenido,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

/** Ícono de cada categoría. */
private fun iconoCategoria(categoria: String): ImageVector = when (categoria) {
    "Bebidas" -> Icons.Default.LocalDrink
    "Abarrotes" -> Icons.Default.Kitchen
    "Snacks" -> Icons.Default.Fastfood
    else -> Icons.Default.Storefront // "Todos"
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InicioPreview() {
    BodegaTheme {
        InicioScreen(
            cantidadCarrito = 3,
            onVerCarrito = {},
            onProductoClick = {},
            onAgregarProducto = {},
            onNavegarBarra = {}
        )
    }
}