package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Usuario
import com.tecsup.mibodega.ui.cliente.modelo.usuarioDemo
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

/** Métodos de pago del mockup (pantalla 6). */
private val metodosPago = listOf("Efectivo al entregar", "Yape", "Plin")

/**
 * Pantalla 6: Dirección y pago (mockup "Cliente").
 * Los campos llegan pre-llenados con los datos del registro, pero el
 * cliente puede cambiarlos solo para este pedido. Al confirmar, entrega
 * los datos ya listos a ClienteApp, que crea el Pedido y navega.
 *
 * @param total total a pagar (subtotal + delivery), calculado en ClienteApp
 */
@Composable
fun DatosEntregaScreen(
    usuario: Usuario?,
    total: Double,
    onVolver: () -> Unit,
    onConfirmar: (datos: Usuario, metodoPago: String) -> Unit
) {
    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var telefono by remember { mutableStateOf(usuario?.telefono ?: "") }
    var direccion by remember { mutableStateOf(usuario?.direccion ?: "") }
    var referencia by remember { mutableStateOf(usuario?.referencia ?: "") }
    var metodoPago by remember { mutableStateOf(metodosPago.first()) }

    val formularioValido = nombre.isNotBlank() &&
            telefono.filter { it.isDigit() }.length == 9 &&
            direccion.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoEntrega(onVolver = onVolver)

        Spacer(Modifier.height(16.dp))

        CampoTexto(etiqueta = "Nombre", valor = nombre, onValorCambia = { nombre = it })
        Spacer(Modifier.height(12.dp))
        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = { telefono = it },
            teclado = KeyboardType.Phone
        )
        Spacer(Modifier.height(12.dp))
        CampoTexto(etiqueta = "Dirección", valor = direccion, onValorCambia = { direccion = it })
        Spacer(Modifier.height(12.dp))
        CampoTexto(etiqueta = "Referencia", valor = referencia, onValorCambia = { referencia = it })

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Método de pago",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )
        SelectorMetodoPago(
            seleccionado = metodoPago,
            onSeleccionar = { metodoPago = it }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total a pagar", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "S/ %.2f".format(total),
                style = MaterialTheme.typography.titleMedium,
                color = VerdeBodega
            )
        }

        Spacer(Modifier.height(20.dp))

        BotonPrimario(
            texto = "Confirmar pedido",
            habilitado = formularioValido,
            onClick = {
                val datos = Usuario(nombre.trim(), telefono.trim(), direccion.trim(), referencia.trim())
                onConfirmar(datos, metodoPago)
            }
        )

        Spacer(Modifier.height(24.dp))
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

@Composable
private fun EncabezadoEntrega(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }
        Text(
            text = "Datos de entrega",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

/**
 * Grupo de RadioButton: solo uno puede estar marcado.
 * Toda la fila es clickeable (selectable), no solo el circulito.
 */
@Composable
private fun SelectorMetodoPago(
    seleccionado: String,
    onSeleccionar: (String) -> Unit
) {
    Column(modifier = Modifier.selectableGroup()) {
        metodosPago.forEach { metodo ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = metodo == seleccionado,
                        onClick = { onSeleccionar(metodo) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = metodo == seleccionado,
                    onClick = null, // el click lo maneja la fila completa
                    colors = RadioButtonDefaults.colors(selectedColor = VerdeBodega)
                )
                Text(
                    text = metodo,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DatosEntregaPreview() {
    BodegaTheme {
        DatosEntregaScreen(
            usuario = usuarioDemo,
            total = 25.90,
            onVolver = {},
            onConfirmar = { _, _ -> }
        )
    }
}