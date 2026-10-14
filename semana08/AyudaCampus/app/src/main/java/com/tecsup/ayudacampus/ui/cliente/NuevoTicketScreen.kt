package com.tecsup.ayudacampus.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.ayudacampus.data.local.entity.Categoria
import com.tecsup.ayudacampus.model.Prioridad
import com.tecsup.ayudacampus.ui.components.AppTopBar
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.AyudaTextField
import com.tecsup.ayudacampus.ui.components.iconoCategoria
import com.tecsup.ayudacampus.ui.theme.colorPrioridad
import com.tecsup.ayudacampus.viewmodel.NuevoTicketViewModel

@Composable
fun NuevoTicketScreen(
    vm: NuevoTicketViewModel,
    alVolver: () -> Unit,
    alEnviado: () -> Unit
) {
    val categorias by vm.categorias.collectAsStateWithLifecycle()
    val form by vm.formulario.collectAsStateWithLifecycle()
    val enviado by vm.enviado.collectAsStateWithLifecycle()

    LaunchedEffect(enviado) {
        if (enviado) {
            vm.confirmarEnvio()
            alEnviado()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        AppTopBar(titulo = "Nuevo ticket", alVolver = alVolver)

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Categoría", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            ChipsCategoria(
                categorias = categorias,
                seleccionada = form.categoriaId,
                alElegir = vm::elegirCategoria
            )
            form.errorCategoria?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            AyudaTextField(
                valor = form.titulo,
                alCambiar = vm::cambiarTitulo,
                etiqueta = "Título",
                error = form.errorTitulo
            )

            AyudaTextField(
                valor = form.ubicacion,
                alCambiar = vm::cambiarUbicacion,
                etiqueta = "Ubicación",
                icono = Icons.Outlined.Place
            )

            Text("Prioridad", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            SelectorPrioridad(seleccionada = form.prioridad, alElegir = vm::elegirPrioridad)

            AyudaTextField(
                valor = form.descripcion,
                alCambiar = vm::cambiarDescripcion,
                etiqueta = "Descripción",
                error = form.errorDescripcion,
                lineas = 4
            )

            TiempoEstimado(form.prioridad)
        }

        AyudaButton(
            texto = "Enviar ticket",
            alPulsar = vm::enviar,
            habilitado = !form.enviando,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ChipsCategoria(
    categorias: List<Categoria>,
    seleccionada: Int?,
    alElegir: (Int) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categorias.forEach { c ->
            FilterChip(
                selected = c.id == seleccionada,
                onClick = { alElegir(c.id) },
                label = { Text(c.nombre) },
                leadingIcon = {
                    Icon(iconoCategoria(c.icono), contentDescription = null, modifier = Modifier.size(18.dp))
                },
                shape = RoundedCornerShape(50),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@Composable
private fun SelectorPrioridad(seleccionada: Prioridad, alElegir: (Prioridad) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Prioridad.entries.forEach { p ->
            val activa = p == seleccionada
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (activa) colorPrioridad(p) else MaterialTheme.colorScheme.surface)
                    .border(
                        BorderStroke(1.dp, if (activa) colorPrioridad(p) else MaterialTheme.colorScheme.outline),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { alElegir(p) }
            ) {
                Text(
                    p.etiqueta,
                    fontWeight = FontWeight.SemiBold,
                    color = if (activa) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun TiempoEstimado(prioridad: Prioridad) {
    val horas = when (prioridad) {
        Prioridad.ALTA -> 4
        Prioridad.MEDIA -> 24
        Prioridad.BAJA -> 72
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f))
            .padding(12.dp)
    ) {
        Icon(
            Icons.Outlined.Schedule,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "Tiempo de respuesta estimado: $horas horas",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}