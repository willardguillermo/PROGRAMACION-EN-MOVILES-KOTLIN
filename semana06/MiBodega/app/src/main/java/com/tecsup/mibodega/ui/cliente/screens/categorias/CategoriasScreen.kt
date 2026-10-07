package com.tecsup.mibodega.ui.cliente.screens.categorias

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.BarraInferior
import com.tecsup.mibodega.ui.cliente.Rutas
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.tecsup.mibodega.ui.cliente.modelo.imagenRes

/**
 * Destino "Categorías" de la NavigationBar.
 * Muestra los productos agrupados por categoría en un solo LazyColumn:
 * un item() para el título de cada grupo y items() para sus productos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(
    productos: List<Producto> = listaProductosFake,
    onProductoClick: (Producto) -> Unit,
    onNavegarBarra: (String) -> Unit
) {
    // "Todos" no es una categoría real, por eso se salta con drop(1)
    val grupos = listaCategorias.drop(1).map { categoria ->
        categoria to productos.filter { it.categoria == categoria }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Categorías", fontWeight = FontWeight.Bold) }) },
        bottomBar = { BarraInferior(rutaActual = Rutas.CATEGORIAS, onNavegar = onNavegarBarra) }
    ) { paddingInterno ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            grupos.forEach { (categoria, productosDeCategoria) ->
                item(key = "titulo-$categoria") {
                    Text(
                        text = "$categoria (${productosDeCategoria.size})",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }
                items(productosDeCategoria, key = { it.id }) { producto ->
                    FilaProducto(producto = producto, onClick = { onProductoClick(producto) })
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun FilaProducto(producto: Producto, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(producto.imagenRes()),
            contentDescription = producto.nombre,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = producto.nombre,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "S/ %.2f".format(producto.precio),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CategoriasPreview() {
    BodegaTheme {
        CategoriasScreen(onProductoClick = {}, onNavegarBarra = {})
    }
}