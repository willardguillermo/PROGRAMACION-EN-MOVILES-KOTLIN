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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.COSTO_DELIVERY
import com.tecsup.mibodega.ui.cliente.modelo.Usuario
import com.tecsup.mibodega.ui.cliente.modelo.usuarioDemo
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

/** Tipos de entrega (RadioButton). El texto incluye el costo para que el cliente lo vea. */
private const val OPCION_DELIVERY = "Delivery a domicilio (+ S/ 4.00)"
private const val OPCION_RECOJO = "Recojo en tienda (gratis)"
private val tiposEntrega = listOf(OPCION_DELIVERY, OPCION_RECOJO)

/** Métodos de pago del mockup (pantalla 6). */
private val metodosPago = listOf("Efectivo al entregar", "Yape", "Plin")

/**
 * Pantalla 6: Dirección y pago (mockup "Cliente").
 * Los campos llegan pre-llenados con los datos del registro, pero el
 * cliente puede cambiarlos solo para este pedido.
 *
 * Recojo vs delivery: el costo de envío y el total se recalculan solos
 * según el RadioButton elegido. Con "Recojo en tienda" no se pide dirección.
 *
 * @param subtotal suma de los productos del carrito (sin envío), calculada en ClienteApp
 */
@Composable
fun DatosEntregaScreen(
    usuario: Usuario?,
    subtotal: Double,
    onVolver: () -> Unit,
    onConfirmar: (datos: Usuario, metodoPago: String, esDelivery: Boolean) -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf(usuario?.nombre ?: "") }
    var telefono by rememberSaveable { mutableStateOf(usuario?.telefono ?: "") }
    var direccion by rememberSaveable { mutableStateOf(usuario?.direccion ?: "") }
    var referencia by rememberSaveable { mutableStateOf(usuario?.referencia ?: "") }
    var tipoEntrega by rememberSaveable { mutableStateOf(OPCION_DELIVERY) }
    var metodoPago by rememberSaveable { mutableStateOf(metodosPago.first()) }

    // Cálculo reactivo: cambiar el RadioButton cambia envío y total al instante
    val esDelivery = tipoEntrega == OPCION_DELIVERY
    val costoEnvio = if (esDelivery) COSTO_DELIVERY else 0.0
    val total = subtotal + costoEnvio

    // Igual que en Registro: los errores se muestran recién al intentar confirmar
    var mostrarErrores by rememberSaveable { mutableStateOf(false) }
    val nombreValido = nombre.isNotBlank()
    val telefonoValido = telefono.filter { it.isDigit() }.length == 9
    val direccionValida = !esDelivery || direccion.isNotBlank() // solo se exige con delivery

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoEntrega(onVolver = onVolver)

        Spacer(Modifier.height(16.dp))

        Titulo("Tipo de entrega")
        GrupoRadio(
            opciones = tiposEntrega,
            seleccionado = tipoEntrega,
            onSeleccionar = { tipoEntrega = it }
        )

        Spacer(Modifier.height(12.dp))

        CampoTexto(
            etiqueta = "Nombre",
            valor = nombre,
            onValorCambia = { nombre = it },
            esError = mostrarErrores && !nombreValido,
            mensajeError = "Ingresa tu nombre"
        )
        Spacer(Modifier.height(12.dp))
        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = { telefono = it },
            teclado = KeyboardType.Phone,
            esError = mostrarErrores && !telefonoValido,
            mensajeError = "El teléfono debe tener 9 dígitos"
        )
        Spacer(Modifier.height(12.dp))

        if (esDelivery) {
            CampoTexto(
                etiqueta = "Dirección",
                valor = direccion,
                onValorCambia = { direccion = it },
                esError = mostrarErrores && !direccionValida,
                mensajeError = "Ingresa la dirección de entrega"
            )
            Spacer(Modifier.height(12.dp))
            CampoTexto(etiqueta = "Referencia (opcional)", valor = referencia, onValorCambia = { referencia = it })
        } else {
            Text(
                text = "Recoge tu pedido en: Bodega Mi Bodega – Av. Los Olivos 100.\nTe avisaremos cuando esté listo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(20.dp))

        Titulo("Método de pago")
        GrupoRadio(
            opciones = metodosPago,
            seleccionado = metodoPago,
            onSeleccionar = { metodoPago = it }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        FilaMonto("Subtotal", subtotal)
        FilaMonto(if (esDelivery) "Delivery" else "Recojo en tienda", costoEnvio)
        Spacer(Modifier.height(4.dp))
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
            onClick = {
                if (nombreValido && telefonoValido && direccionValida) {
                    val datos = Usuario(usuario?.usuario ?: "", usuario?.clave ?: "", nombre.trim(), telefono.trim(), direccion.trim(), referencia.trim())
                    onConfirmar(datos, metodoPago, esDelivery)
                } else {
                    mostrarErrores = true // no avanza: pinta en rojo lo que falta
                }
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

@Composable
private fun Titulo(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun FilaMonto(etiqueta: String, monto: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("S/ %.2f".format(monto), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/**
 * Grupo de RadioButton reutilizable (tipo de entrega y método de pago):
 * solo una opción puede estar marcada. Toda la fila es clickeable.
 */
@Composable
private fun GrupoRadio(
    opciones: List<String>,
    seleccionado: String,
    onSeleccionar: (String) -> Unit
) {
    Column(modifier = Modifier.selectableGroup()) {
        opciones.forEach { opcion ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = opcion == seleccionado,
                        onClick = { onSeleccionar(opcion) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = opcion == seleccionado,
                    onClick = null, // el click lo maneja la fila completa
                    colors = RadioButtonDefaults.colors(selectedColor = VerdeBodega)
                )
                Text(
                    text = opcion,
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
            subtotal = 21.90,
            onVolver = {},
            onConfirmar = { _, _, _ -> }
        )
    }
}
