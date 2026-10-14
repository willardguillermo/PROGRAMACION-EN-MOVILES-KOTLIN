package com.tecsup.ayudacampus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AyudaButton(
    texto: String,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    icono: ImageVector? = null,
    contorno: Boolean = false,
    color: Color = MaterialTheme.colorScheme.primary,
    colorTexto: Color = MaterialTheme.colorScheme.onPrimary
) {
    val forma = RoundedCornerShape(14.dp)
    val tamano = modifier
        .fillMaxWidth()
        .height(52.dp)

    if (contorno) {
        OutlinedButton(
            onClick = alPulsar,
            enabled = habilitado,
            shape = forma,
            border = BorderStroke(1.5.dp, if (habilitado) color else color.copy(alpha = 0.4f)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
            modifier = tamano
        ) {
            ContenidoBoton(texto, icono)
        }
    } else {
        Button(
            onClick = alPulsar,
            enabled = habilitado,
            shape = forma,
            colors = ButtonDefaults.buttonColors(
                containerColor = color,
                contentColor = colorTexto
            ),
            modifier = tamano
        ) {
            ContenidoBoton(texto, icono)
        }
    }
}

@Composable
private fun ContenidoBoton(texto: String, icono: ImageVector?) {
    if (icono != null) {
        Icon(icono, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
    }
    Text(texto, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleSmall)
}