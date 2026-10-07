package com.tecsup.mibodega.ui.cliente.screens.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla 2: Registro de datos (mockup "Cliente").
 * Guarda su propio estado de formulario (rememberSaveable) porque solo esta
 * pantalla lo necesita. Al enviar, entrega los datos ya listos.
 *
 * Validación: si el usuario toca "Crear cuenta" con campos vacíos o
 * inválidos, NO avanza y esos campos se marcan en rojo con su mensaje.
 */
@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onCrearCuenta: (usuario: String, clave: String, nombre: String, telefono: String, direccion: String, referencia: String) -> Unit
) {
    var usuario by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var nombre by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var referencia by rememberSaveable { mutableStateOf("") }

    // Se vuelve true la primera vez que intentan enviar el formulario.
    // Así los campos no aparecen en rojo apenas se abre la pantalla.
    var mostrarErrores by rememberSaveable { mutableStateOf(false) }

    val usuarioValido = usuario.isNotBlank()
    val claveValida = clave.isNotBlank()
    val nombreValido = nombre.isNotBlank()
    val telefonoValido = telefono.filter { it.isDigit() }.length == 9
    val direccionValida = direccion.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoRegistro(onVolver = onVolver)

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Foto de perfil",
                tint = VerdeBodega,
                modifier = Modifier
                    .size(84.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    .padding(4.dp)
            )
        }

        Spacer(Modifier.height(28.dp))

        CampoTexto(
            etiqueta = "Usuario",
            valor = usuario,
            onValorCambia = { usuario = it },
            placeholder = "jperez",
            esError = mostrarErrores && !usuarioValido,
            mensajeError = "Ingresa tu usuario"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Contraseña",
            valor = clave,
            onValorCambia = { clave = it },
            placeholder = "********",
            esContrasena = true,
            esError = mostrarErrores && !claveValida,
            mensajeError = "Ingresa una contraseña"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Nombre completo",
            valor = nombre,
            onValorCambia = { nombre = it },
            placeholder = "Juan Pérez",
            esError = mostrarErrores && !nombreValido,
            mensajeError = "Ingresa tu nombre"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = { telefono = it },
            placeholder = "987 654 321",
            teclado = KeyboardType.Phone,
            esError = mostrarErrores && !telefonoValido,
            mensajeError = "El teléfono debe tener 9 dígitos"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Dirección de entrega",
            valor = direccion,
            onValorCambia = { direccion = it },
            placeholder = "Av. Los Olivos 123",
            esError = mostrarErrores && !direccionValida,
            mensajeError = "Ingresa tu dirección"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Referencia (opcional)",
            valor = referencia,
            onValorCambia = { referencia = it },
            placeholder = "Frente al parque"
        )

        Spacer(Modifier.height(28.dp))

        BotonPrimario(
            texto = "Crear cuenta",
            onClick = {
                if (usuarioValido && claveValida && nombreValido && telefonoValido && direccionValida) {
                    onCrearCuenta(usuario.trim(), clave, nombre.trim(), telefono.trim(), direccion.trim(), referencia.trim())
                } else {
                    mostrarErrores = true // no avanza: pinta en rojo lo que falta
                }
            }
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun EncabezadoRegistro(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onVolver,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }
        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f, fill = false)
        )
        Spacer(Modifier.size(48.dp)) // balancea el ancho del ícono de la izquierda
    }
    Text(
        text = "Completa tus datos para continuar",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegistroPreview() {
    BodegaTheme {
        RegistroScreen(onVolver = {}, onCrearCuenta = { _, _, _, _, _, _ -> })
    }
}
