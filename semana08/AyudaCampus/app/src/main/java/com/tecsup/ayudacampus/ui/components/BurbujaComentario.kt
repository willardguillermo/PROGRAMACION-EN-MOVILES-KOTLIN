package com.tecsup.ayudacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import com.tecsup.ayudacampus.data.local.entity.Comentario
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.ui.theme.CianContenedor
import com.tecsup.ayudacampus.ui.theme.EstadoAbierto
import com.tecsup.ayudacampus.ui.theme.TextoPrincipal
import com.tecsup.ayudacampus.ui.theme.VioletaContenedor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BurbujaComentario(comentario: Comentario, propio: Boolean) {
    val fondo = when {
        comentario.interno -> Color(0xFFFEF3C7)
        propio -> MaterialTheme.colorScheme.primary
        comentario.autorRol == Rol.ADMIN.name -> VioletaContenedor
        else -> CianContenedor
    }
    val colorTexto = if (propio && !comentario.interno) MaterialTheme.colorScheme.onPrimary else TextoPrincipal
    val forma = if (propio) {
        RoundedCornerShape(16.dp, 4.dp, 16.dp, 16.dp)
    } else {
        RoundedCornerShape(4.dp, 16.dp, 16.dp, 16.dp)
    }
    val autor = if (propio) "Tú" else comentario.autorNombre

    Row(
        horizontalArrangement = if (propio) Arrangement.End else Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(forma)
                .background(fondo)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            if (comentario.interno) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Lock,
                        contentDescription = "Nota interna",
                        modifier = Modifier.size(12.dp),
                        tint = colorTexto.copy(alpha = 0.75f)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Nota interna · $autor · ${horaComentario(comentario.fecha)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = colorTexto.copy(alpha = 0.75f)
                    )
                }
            } else {
                Text(
                    "$autor · ${horaComentario(comentario.fecha)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorTexto.copy(alpha = 0.75f)
                )
            }
            Text(
                comentario.texto,
                style = MaterialTheme.typography.bodyMedium,
                color = colorTexto
            )
        }
    }
}

private fun horaComentario(fecha: Long): String {
    val dia = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    val patron = if (dia.format(Date(fecha)) == dia.format(Date())) "HH:mm" else "dd/MM HH:mm"
    return SimpleDateFormat(patron, Locale.getDefault()).format(Date(fecha))
}