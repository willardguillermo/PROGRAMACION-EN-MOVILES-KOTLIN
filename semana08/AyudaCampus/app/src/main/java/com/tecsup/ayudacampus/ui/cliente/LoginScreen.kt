package com.tecsup.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.AyudaTextField
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
import com.tecsup.ayudacampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(vm: SesionViewModel, alIngresar: (Rol) -> Unit, alRegistrarse: () -> Unit) {
    val usuario by vm.usuarioActual.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    val oscuro by vm.modoOscuro.collectAsStateWithLifecycle()

    var rol by rememberSaveable { mutableStateOf(Rol.CLIENTE) }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(usuario) {
        usuario?.let { alIngresar(Rol.valueOf(it.rol)) }
    }

    AyudaTheme(rol = rol, oscuro = oscuro) {
        Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 40.dp)
            ) {
                Column {
                    Text(
                        "Bienvenido",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Ingresa para continuar",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                SelectorRol(
                    seleccionado = rol,
                    alElegir = {
                        rol = it
                        vm.limpiarError()
                    }
                )

                AyudaTextField(
                    valor = correo,
                    alCambiar = { correo = it },
                    etiqueta = "Correo",
                    icono = Icons.Outlined.Email,
                    teclado = KeyboardType.Email
                )

                AyudaTextField(
                    valor = clave,
                    alCambiar = { clave = it },
                    etiqueta = "Contraseña",
                    icono = Icons.Outlined.Lock,
                    esClave = true
                )

                Text(
                    "¿Olvidaste tu contraseña?",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )

                error?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                AyudaButton(
                    texto = "Ingresar",
                    alPulsar = { vm.login(correo, clave, rol) }
                )

                if (rol == Rol.CLIENTE) {
                    Text(
                        "¿No tienes cuenta? Regístrate",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { alRegistrarse() }
                            .padding(vertical = 8.dp)
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(16.dp)
                    ) {
                        Text(
                            "Modo práctica",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectorRol(seleccionado: Rol, alElegir: (Rol) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
    ) {
        Rol.entries.forEach { opcion ->
            val activo = opcion == seleccionado
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activo) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable { alElegir(opcion) }
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    opcion.etiqueta,
                    fontWeight = FontWeight.SemiBold,
                    color = if (activo) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}