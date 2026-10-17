package com.tecsup.ayudacampus.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val Dorado = Color(0xFFF59E0B)

@Composable
fun Estrellas(
    valor: Int,
    alCambiar: ((Int) -> Unit)? = null,
    tamano: Dp = 40.dp
) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        (1..5).forEach { n ->
            val base = Modifier.size(tamano)
            Icon(
                Icons.Filled.Star,
                contentDescription = "$n estrellas",
                tint = if (n <= valor) Dorado else MaterialTheme.colorScheme.outline,
                modifier = if (alCambiar != null) base.clickable { alCambiar(n) } else base
            )
        }
    }
}