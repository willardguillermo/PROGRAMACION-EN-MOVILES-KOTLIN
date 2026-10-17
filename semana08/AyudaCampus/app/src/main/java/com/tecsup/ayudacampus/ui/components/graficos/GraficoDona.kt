package com.tecsup.ayudacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GraficoDona(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    diametro: Dp = 120.dp,
    grosor: Dp = 18.dp
) {
    val total = datos.sumOf { it.valor }
    val vacio = MaterialTheme.colorScheme.surfaceVariant
    val primario = MaterialTheme.colorScheme.primary

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(diametro)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val trazo = grosor.toPx()
                val esquina = Offset(trazo / 2, trazo / 2)
                val tamano = Size(size.width - trazo, size.height - trazo)

                if (total == 0) {
                    drawArc(
                        color = vacio,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = esquina,
                        size = tamano,
                        style = Stroke(width = trazo)
                    )
                } else {
                    var inicio = -90f
                    datos.forEach { d ->
                        val barrido = 360f * d.valor / total
                        if (barrido > 0f) {
                            drawArc(
                                color = if (d.color == Color.Unspecified) primario else d.color,
                                startAngle = inicio,
                                sweepAngle = barrido,
                                useCenter = false,
                                topLeft = esquina,
                                size = tamano,
                                style = Stroke(width = trazo)
                            )
                        }
                        inicio += barrido
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    "tickets",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.width(20.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            datos.forEach { d ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (d.color == Color.Unspecified) primario else d.color)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(d.etiqueta, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                    Text(
                        "${d.valor}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}