package com.tecsup.mibodega.ui.cliente.screens.carrito

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.COSTO_DELIVERY
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.componentes.SelectorCantidad
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisBorde
import com.tecsup.mibodega.ui.theme.VerdeBodega
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.tecsup.mibodega.ui.cliente.modelo.imagenRes

/**
 * Pantalla 5: Mi carrito (mockup "Cliente").
 * No guarda estado propio: el carrito viene de ClienteApp y cualquier
 * cambio (sumar, restar, eliminar, vaciar) se avisa hacia arriba con callbacks.
 */
@Composable
fun CarritoScreen(
    carrito: List<ItemCarrito>,
    onVolver: () -> Unit,
    onIncrementar: (Producto) -> Unit,
    onDecrementar: (Producto) -> Unit,
    onEliminar: (Producto) -> Unit,
    onVaciar: () -> Unit,
    onContinuarPedido: () -> Unit
) {
    // Cálculo reactivo: NO hay botón "recalcular". Cada vez que ClienteApp
    // cambia el carrito, esta función se vuelve a ejecutar (recomposición)
    // y subtotal/total se calculan de nuevo con las cantidades actuales.
    val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
    val total = subtotal + COSTO_DELIVERY

    // Confirmaciones: guardan QUÉ se quiere borrar hasta que el usuario acepte.
    var productoAEliminar by rememberSaveable { mutableStateOf<Producto?>(null) }
    var confirmarVaciar by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        EncabezadoCarrito(
            mostrarVaciar = carrito.isNotEmpty(),
            onVolver = onVolver,
            onVaciar = { confirmarVaciar = true } // primero pregunta
        )

        if (carrito.isEmpty()) {
            CarritoVacio(onSeguirComprando = onVolver)
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                // key = id del producto: Compose sabe qué fila es cuál al eliminar
                items(carrito, key = { it.producto.id }) { item ->
                    FilaCarrito(
                        item = item,
                        onIncrementar = { onIncrementar(item.producto) },
                        onDecrementar = {
                            // Con cantidad 1, restar = eliminar, así que también pregunta
                            if (item.cantidad == 1) productoAEliminar = item.producto
                            else onDecrementar(item.producto)
                        },
                        onEliminar = { productoAEliminar = item.producto }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }

            ResumenYBoton(
                subtotal = subtotal,
                delivery = COSTO_DELIVERY,
                total = total,
                onContinuarPedido = onContinuarPedido
            )
        }
    }

    // AlertDialog: solo se muestra mientras productoAEliminar no sea null
    productoAEliminar?.let { producto ->
        AlertDialog(
            onDismissRequest = { productoAEliminar = null },
            title = { Text("Eliminar producto") },
            text = { Text("¿Quieres quitar ${producto.nombre} de tu carrito?") },
            confirmButton = {
                TextButton(onClick = {
                    onEliminar(producto)
                    productoAEliminar = null
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { productoAEliminar = null }) { Text("Cancelar") }
            }
        )
    }

    if (confirmarVaciar) {
        AlertDialog(
            onDismissRequest = { confirmarVaciar = false },
            title = { Text("Vaciar carrito") },
            text = { Text("Se quitarán todos los productos. ¿Continuar?") },
            confirmButton = {
                TextButton(onClick = {
                    onVaciar()
                    confirmarVaciar = false
                }) { Text("Vaciar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmarVaciar = false }) { Text("Cancelar") }
            }
        )
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

@Composable
private fun EncabezadoCarrito(
    mostrarVaciar: Boolean,
    onVolver: () -> Unit,
    onVaciar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }
        Text(
            text = "Mi carrito",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        if (mostrarVaciar) {
            IconButton(onClick = onVaciar) {
                Icon(Icons.Default.Delete, contentDescription = "Vaciar carrito")
            }
        }
    }
}

@Composable
private fun CarritoVacio(onSeguirComprando: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingBasket,
            contentDescription = null,
            tint = GrisBorde,
            modifier = Modifier.size(80.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Tu carrito está vacío",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Agrega productos desde Inicio para hacer tu pedido.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        BotonSecundario(texto = "Seguir comprando", onClick = onSeguirComprando)
    }
}

@Composable
private fun FilaCarrito(
    item: ItemCarrito,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onEliminar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(item.producto.imagenRes()),
            contentDescription = item.producto.nombre,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.producto.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "S/ %.2f".format(item.producto.precio),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // minimo = 0: con cantidad 1 el "−" sigue activo y, al tocarlo,
        // ClienteApp quita el producto del carrito.
        SelectorCantidad(
            cantidad = item.cantidad,
            onIncrementar = onIncrementar,
            onDecrementar = onDecrementar,
            minimo = 0
        )

        IconButton(onClick = onEliminar) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar ${item.producto.nombre}",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ResumenYBoton(
    subtotal: Double,
    delivery: Double,
    total: Double,
    onContinuarPedido: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        FilaResumen(etiqueta = "Subtotal", valor = subtotal)
        FilaResumen(etiqueta = "Costo de delivery", valor = delivery)

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.titleMedium
            )
            // AnimatedContent: cuando el total cambia, el número viejo sale
            // hacia arriba y el nuevo entra desde abajo (efecto "contador").
            AnimatedContent(
                targetState = total,
                transitionSpec = {
                    (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
                },
                label = "total"
            ) { totalAnimado ->
                Text(
                    text = "S/ %.2f".format(totalAnimado),
                    style = MaterialTheme.typography.titleMedium,
                    color = VerdeBodega
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        BotonPrimario(
            texto = "Continuar pedido",
            onClick = onContinuarPedido
        )
    }
}

@Composable
private fun FilaResumen(etiqueta: String, valor: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = "S/ %.2f".format(valor), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CarritoPreview() {
    // Buscamos por id (no por posición) porque reordenamos DatosFake.kt
    val carritoEjemplo = listOf(
        ItemCarrito(listaProductosFake.first { it.id == 5 }, 1), // Coca-Cola
        ItemCarrito(listaProductosFake.first { it.id == 1 }, 2), // Arroz Costeño
        ItemCarrito(listaProductosFake.first { it.id == 3 }, 1)  // Leche Gloria
    )
    BodegaTheme {
        CarritoScreen(
            carrito = carritoEjemplo,
            onVolver = {},
            onIncrementar = {},
            onDecrementar = {},
            onEliminar = {},
            onVaciar = {},
            onContinuarPedido = {}
        )
    }
}