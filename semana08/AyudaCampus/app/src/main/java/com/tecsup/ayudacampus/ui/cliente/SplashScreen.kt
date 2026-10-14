package com.tecsup.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.viewmodel.SesionViewModel

@Composable
fun SplashScreen(
    vm: SesionViewModel,
    alComenzar: () -> Unit,
    alEntrar: (Rol) -> Unit
) {
    val revisada by vm.sesionRevisada.collectAsStateWithLifecycle()
    val usuario by vm.usuarioActual.collectAsStateWithLifecycle()

    LaunchedEffect(revisada, usuario) {
        val actual = usuario
        if (revisada && actual != null) alEntrar(Rol.valueOf(actual.rol))
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(24.dp)
    ) {
        Spacer(Modifier.weight(1f))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                Icons.Outlined.SupportAgent,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(52.dp)
            )
        }

        Spacer(Modifier.height(20.dp))
        Text(
            "AyudaCampus",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Mesa de ayuda TI del campus",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(40.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            listOf(Icons.Outlined.Computer, Icons.Outlined.Wifi, Icons.Outlined.Print).forEach { icono ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Icon(
                        icono,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        if (revisada && usuario == null) {
            AyudaButton(texto = "Comenzar", alPulsar = alComenzar)
        } else {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }

        Spacer(Modifier.height(12.dp))
        Text(
            "Tecsup · Programación en Móviles",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}