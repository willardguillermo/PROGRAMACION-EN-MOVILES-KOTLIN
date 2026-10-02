package com.tecsup.mibodega.ui.cliente.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import com.tecsup.mibodega.ui.cliente.modelo.Usuario
import com.tecsup.mibodega.ui.cliente.modelo.usuarioDemo
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Destino "Perfil" de la NavigationBar: muestra los datos del registro
 * y permite cerrar sesión (volver a Bienvenida limpiando la pila).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    usuario: Usuario?,
    onCerrarSesion: () -> Unit,
    onNavegarBarra: (String) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi perfil", fontWeight = FontWeight.Bold) }) },
        bottomBar = { BarraInferior(rutaActual = Rutas.PERFIL, onNavegar = onNavegarBarra) }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = VerdeBodega,
                modifier = Modifier
                    .size(96.dp)
                    .background(GrisClaro, CircleShape)
                    .padding(4.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = usuario?.nombre ?: "Invitado",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(24.dp))

            DatoPerfil("Teléfono", usuario?.telefono)
            DatoPerfil("Dirección de entrega", usuario?.direccion)
            DatoPerfil("Referencia", usuario?.referencia)

            Spacer(Modifier.weight(1f))
            BotonSecundario(texto = "Cerrar sesión", onClick = onCerrarSesion)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DatoPerfil(etiqueta: String, valor: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = if (valor.isNullOrBlank()) "—" else valor,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PerfilPreview() {
    BodegaTheme {
        PerfilScreen(usuario = usuarioDemo, onCerrarSesion = {}, onNavegarBarra = {})
    }
}