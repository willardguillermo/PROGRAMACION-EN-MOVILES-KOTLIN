package com.tecsup.ayudacampus.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.model.EstadoTicket

@Composable
fun StepperTicket(estado: EstadoTicket, modifier: Modifier = Modifier) {
    val actual = estado.ordinal
    val ultimo = EstadoTicket.entries.lastIndex
    val activo = MaterialTheme.colorScheme.primary
    val inactivo = MaterialTheme.colorScheme.outline

    Row(modifier = modifier.fillMaxWidth()) {
        EstadoTicket.entries.forEachIndexed { i, paso ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val colorLineaIzq by animateColorAsState(
                        targetValue = when {
                            i == 0 -> Color.Transparent
                            i <= actual -> activo
                            else -> inactivo
                        }, label = "lineaIzq"
                    )
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .background(colorLineaIzq)
                    )
                    Circulo(i = i, actual = actual, activo = activo, inactivo = inactivo)
                    
                    val colorLineaDer by animateColorAsState(
                        targetValue = when {
                            i == ultimo -> Color.Transparent
                            i < actual -> activo
                            else -> inactivo
                        }, label = "lineaDer"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .background(colorLineaDer)
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    paso.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (i == actual) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (i <= actual) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

@Composable
private fun Circulo(i: Int, actual: Int, activo: Color, inactivo: Color) {
    val colorBorde by animateColorAsState(
        targetValue = if (i <= actual) activo else inactivo,
        label = "bordeCirculo"
    )
    val colorFondo by animateColorAsState(
        targetValue = if (i <= actual) activo else Color.Transparent,
        label = "fondoCirculo"
    )

    val transition = rememberInfiniteTransition(label = "pulse")
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val currentScale = if (i == actual && actual != EstadoTicket.entries.lastIndex) scale else 1f

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .scale(currentScale)
            .size(24.dp)
            .clip(CircleShape)
            .background(colorFondo)
            .border(2.dp, colorBorde, CircleShape)
    ) {
        if (i < actual) {
            Icon(
                Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(14.dp)
            )
        } else if (i == actual) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary)
            )
        }
    }
}