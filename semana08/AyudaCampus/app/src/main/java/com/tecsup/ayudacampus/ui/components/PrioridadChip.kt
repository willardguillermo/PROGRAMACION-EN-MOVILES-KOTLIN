package com.tecsup.ayudacampus.ui.components

import androidx.compose.runtime.Composable
import com.tecsup.ayudacampus.model.Prioridad
import com.tecsup.ayudacampus.ui.theme.colorPrioridad

@Composable
fun PrioridadChip(prioridad: Prioridad) {
    Etiqueta(texto = prioridad.etiqueta, color = colorPrioridad(prioridad))
}