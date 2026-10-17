package com.tecsup.ayudacampus.ui.soporte

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.ayudacampus.ui.components.AppTopBar
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.AyudaTextField
import com.tecsup.ayudacampus.ui.components.iconoCategoria
import com.tecsup.ayudacampus.viewmodel.CategoriasViewModel

private val iconos = listOf("pc", "code", "wifi", "key", "printer", "tag")

@Composable
fun FormularioCategoriaScreen(
    vm: CategoriasViewModel,
    id: Int,
    alVolver: () -> Unit
) {
    val form by vm.form.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val terminado by vm.terminado.collectAsStateWithLifecycle()
    val editando = id != 0

    LaunchedEffect(id) { vm.cargar(id) }
    LaunchedEffect(terminado) { if (terminado) alVolver() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        AppTopBar(
            titulo = if (editando) "Editar categoría" else "Nueva categoría",
            alVolver = alVolver
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            AyudaTextField(
                valor = form.nombre,
                alCambiar = vm::cambiarNombre,
                etiqueta = "Nombre de la categoría",
                error = form.error
            )

            Text("Icono", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                iconos.chunked(3).forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        fila.forEach { icono ->
                            val elegido = icono == form.icono
                            val forma = RoundedCornerShape(14.dp)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp)
                                    .clip(forma)
                                    .background(
                                        if (elegido) MaterialTheme.colorScheme.primaryContainer
                                        else MaterialTheme.colorScheme.surface
                                    )
                                    .border(
                                        if (elegido) 2.dp else 1.dp,
                                        if (elegido) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.outline,
                                        forma
                                    )
                                    .clickable { vm.elegirIcono(icono) }
                            ) {
                                Icon(
                                    iconoCategoria(icono),
                                    contentDescription = icono,
                                    tint = if (elegido) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Categoría activa", fontWeight = FontWeight.SemiBold)
                        Text(
                            "Los estudiantes podrán elegirla",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = form.activa, onCheckedChange = vm::cambiarActiva)
                }
            }

            Text("Vista previa", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(
                            iconoCategoria(form.icono),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(form.nombre.ifBlank { "Nombre" }, fontWeight = FontWeight.SemiBold)
                        Text(
                            "${form.tickets} tickets",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            mensaje?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            AyudaButton(texto = "Guardar categoría", alPulsar = { vm.guardar(form.aCategoria()) })
            AyudaButton(texto = "Cancelar", alPulsar = alVolver, contorno = true)
            if (editando) {
                AyudaButton(
                    texto = "Eliminar categoría",
                    alPulsar = { vm.eliminar(form.aCategoria()) },
                    contorno = true,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}