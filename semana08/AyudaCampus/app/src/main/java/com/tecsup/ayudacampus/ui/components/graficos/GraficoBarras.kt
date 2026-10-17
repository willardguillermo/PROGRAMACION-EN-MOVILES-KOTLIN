package com.tecsup.ayudacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GraficoBarras(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    alto: Dp = 160.dp
) {
    val primario = MaterialTheme.colorScheme.primary
    val suave = primario.copy(alpha = 0.35f)
    val medidor = rememberTextMeasurer()
    val estiloValor = MaterialTheme.typography.labelSmall.copy(
        color = MaterialTheme.colorScheme.onSurface
    )
    val maximo = (datos.maxOfOrNull { it.valor } ?: 0).coerceAtLeast(1)

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(alto)
        ) {
            if (datos.isNotEmpty()) {
                val anchoColumna = size.width / datos.size
                val anchoBarra = anchoColumna * 0.55f
                val altoUtil = size.height - 20.dp.toPx()

                datos.forEachIndexed { i, d ->
                    val altoBarra = altoUtil * d.valor / maximo
                    val x = i * anchoColumna + (anchoColumna - anchoBarra) / 2
                    val y = size.height - altoBarra
                    val color = when {
                        d.color != Color.Unspecified -> d.color
                        d.valor == maximo -> primario
                        else -> suave
                    }

                    drawRoundRect(
                        color = color,
                        topLeft = Offset(x, y),
                        size = Size(anchoBarra, altoBarra),
                        cornerRadius = CornerRadius(8.dp.toPx())
                    )

                    val texto = medidor.measure(d.valor.toString(), estiloValor)
                    drawText(
                        textLayoutResult = texto,
                        topLeft = Offset(
                            i * anchoColumna + (anchoColumna - texto.size.width) / 2,
                            y - texto.size.height - 2.dp.toPx()
                        )
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
        ) {
            datos.forEach { d ->
                Text(
                    d.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}