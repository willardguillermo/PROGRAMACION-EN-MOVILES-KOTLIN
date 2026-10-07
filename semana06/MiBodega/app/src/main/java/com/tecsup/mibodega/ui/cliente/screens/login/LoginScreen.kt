package com.tecsup.mibodega.ui.cliente.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.CLAVE_LOGIN
import com.tecsup.mibodega.ui.cliente.modelo.USUARIO_LOGIN
import com.tecsup.mibodega.ui.cliente.modelo.Usuario
import com.tecsup.mibodega.ui.cliente.modelo.usuarioDemo
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme

/**
 * Login con usuario y contraseña (valida contra el registrado o el demo).
 * Si no coinciden, muestra el error en rojo y NO deja entrar.
 */
@Composable
fun LoginScreen(
    usuarioRegistrado: Usuario?,
    onVolver: () -> Unit,
    onIngresar: (Usuario) -> Unit
) {
    var usuario by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Text("Iniciar sesión", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(8.dp))
        if (usuarioRegistrado != null) {
            Text(
                text = "Inicia sesión con tu cuenta recién creada",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Text(
                text = "Usuario de prueba: $USUARIO_LOGIN  ·  Contraseña: $CLAVE_LOGIN",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(24.dp))

        CampoTexto(
            etiqueta = "Usuario",
            valor = usuario,
            onValorCambia = { usuario = it; error = null }, // al escribir se borra el error
            placeholder = "cliente",
            esError = error != null
        )
        Spacer(Modifier.height(16.dp))
        CampoTexto(
            etiqueta = "Contraseña",
            valor = clave,
            onValorCambia = { clave = it; error = null },
            esContrasena = true,
            esError = error != null,
            mensajeError = error
        )

        Spacer(Modifier.height(28.dp))

        BotonPrimario(
            texto = "Ingresar",
            onClick = {
                val inputUsuario = usuario.trim()
                error = when {
                    inputUsuario.isBlank() || clave.isBlank() -> "Completa usuario y contraseña"
                    usuarioRegistrado != null && inputUsuario == usuarioRegistrado.usuario && clave == usuarioRegistrado.clave -> null // Login con cuenta registrada
                    usuarioRegistrado == null && inputUsuario == USUARIO_LOGIN && clave == CLAVE_LOGIN -> null // Login con cuenta demo
                    else -> "Usuario o contraseña incorrectos"
                }

                if (error == null) {
                    val userToLog = if (usuarioRegistrado != null) usuarioRegistrado else usuarioDemo
                    onIngresar(userToLog)
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginPreview() {
    BodegaTheme {
        LoginScreen(usuarioRegistrado = null, onVolver = {}, onIngresar = {})
    }
}
