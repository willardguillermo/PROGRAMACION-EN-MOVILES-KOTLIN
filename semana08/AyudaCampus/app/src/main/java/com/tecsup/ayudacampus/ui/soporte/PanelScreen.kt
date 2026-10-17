package com.tecsup.ayudacampus.ui.soporte

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.ayudacampus.ui.components.graficos.GraficoBarras
import com.tecsup.ayudacampus.ui.components.graficos.GraficoDona
import com.tecsup.ayudacampus.ui.theme.EstadoAbierto
import com.tecsup.ayudacampus.ui.theme.EstadoEnProceso
import com.tecsup.ayudacampus.ui.theme.EstadoResuelto
import com.tecsup.ayudacampus.ui.theme.PrioridadAlta
import com.tecsup.ayudacampus.viewmodel.PanelViewModel

@Composable
fun PanelScreen(vm: PanelViewModel, alVerBandeja: () -> Unit) {
    val abiertos by vm.abiertos.collectAsStateWithLifecycle()
    val enProceso by vm.enProceso.collectAsStateWithLifecycle()
    val resueltos by vm.resueltos.collectAsStateWithLifecycle()
    val porEstado by vm.porEstado.collectAsStateWithLifecycle()
    val porCategoria by vm.porCategoria.collectAsStateWithLifecycle()
    val altas by vm.altasSinAtender.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Panel de soporte",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text("Mesa de ayuda TI · Hoy", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Contador("Abiertos", abiertos, EstadoAbierto, Modifier.weight(1f))
            Contador("En proceso", enProceso, EstadoEnProceso, Modifier.weight(1f))
            Contador("Resueltos", resueltos, EstadoResuelto, Modifier.weight(1f))
        }

        Seccion("Tickets por estado") {
            GraficoDona(datos = porEstado)
        }

        Seccion("Tickets por categoría") {
            GraficoBarras(datos = porCategoria)
        }

        if (altas > 0) {
            AlertaAltas(cantidad = altas, alVer = alVerBandeja)
        }
    }
}

@Composable
private fun Contador(texto: String, valor: Int, color: Color, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.14f))
            .padding(vertical = 16.dp)
    ) {
        Text(
            "$valor",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(texto, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

@Composable
private fun Seccion(titulo: String, contenido: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(titulo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                contenido()
            }
        }
    }
}

@Composable
private fun AlertaAltas(cantidad: Int, alVer: () -> Unit) {
    val titulo = if (cantidad == 1) {
        "1 ticket de prioridad Alta"
    } else {
        "$cantidad tickets de prioridad Alta"
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(PrioridadAlta.copy(alpha = 0.12f))
            .padding(16.dp)
    ) {
        Icon(Icons.Filled.Warning, contentDescription = null, tint = PrioridadAlta)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PrioridadAlta)
            Text(
                "sin atender hace más de 1 hora",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Button(
            onClick = alVer,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrioridadAlta,
                contentColor = Color.White
            )
        ) {
            Text("Ver", fontWeight = FontWeight.SemiBold)
        }
    }
}