package com.tecsup.ayudacampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.ayudacampus.ui.components.AppTopBar
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.AyudaTextField
import com.tecsup.ayudacampus.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(vm: RegistroViewModel, alVolver: () -> Unit) {
    val nombre by vm.nombre.collectAsStateWithLifecycle()
    val correo by vm.correo.collectAsStateWithLifecycle()
    val clave by vm.clave.collectAsStateWithLifecycle()
    val confirmarClave by vm.confirmarClave.collectAsStateWithLifecycle()
    
    val errorNombre by vm.errorNombre.collectAsStateWithLifecycle()
    val errorCorreo by vm.errorCorreo.collectAsStateWithLifecycle()
    val errorClave by vm.errorClave.collectAsStateWithLifecycle()
    val errorConfirmarClave by vm.errorConfirmarClave.collectAsStateWithLifecycle()
    
    val registrado by vm.registrado.collectAsStateWithLifecycle()

    LaunchedEffect(registrado) {
        if (registrado) {
            alVolver()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(titulo = "Crear cuenta", alVolver = alVolver)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CampoConError(
                valor = nombre,
                alCambiar = vm::onNombreChange,
                etiqueta = "Nombre",
                icono = Icons.Outlined.Person,
                error = errorNombre
            )

            CampoConError(
                valor = correo,
                alCambiar = vm::onCorreoChange,
                etiqueta = "Correo",
                icono = Icons.Outlined.Email,
                teclado = KeyboardType.Email,
                error = errorCorreo
            )

            CampoConError(
                valor = clave,
                alCambiar = vm::onClaveChange,
                etiqueta = "Contraseña",
                icono = Icons.Outlined.Lock,
                esClave = true,
                error = errorClave
            )

            CampoConError(
                valor = confirmarClave,
                alCambiar = vm::onConfirmarClaveChange,
                etiqueta = "Confirmar contraseña",
                icono = Icons.Outlined.Lock,
                esClave = true,
                error = errorConfirmarClave
            )

            AyudaButton(
                texto = "Crear cuenta",
                alPulsar = vm::registrar
            )
        }
    }
}

@Composable
private fun CampoConError(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    teclado: KeyboardType = KeyboardType.Text,
    esClave: Boolean = false,
    error: String?
) {
    AyudaTextField(
        valor = valor,
        alCambiar = alCambiar,
        etiqueta = etiqueta,
        icono = icono,
        teclado = teclado,
        esClave = esClave,
        error = error
    )
}
