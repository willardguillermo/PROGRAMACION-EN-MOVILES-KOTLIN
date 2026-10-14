package com.tecsup.ayudacampus.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecsup.ayudacampus.model.Rol

@Composable
fun AyudaTheme(
    rol: Rol = Rol.CLIENTE,
    oscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (oscuro) esquemaOscuro(rol) else esquemaClaro(rol),
        typography = Typography,
        content = content
    )
}

private fun esquemaClaro(rol: Rol): ColorScheme {
    val cliente = rol == Rol.CLIENTE
    val primario = if (cliente) Cian else Violeta
    val contenedor = if (cliente) CianContenedor else VioletaContenedor
    val sobreContenedor = if (cliente) CianOscuro else VioletaOscuro
    val fondo = if (cliente) CianFondo else VioletaFondo
    return lightColorScheme(
        primary = primario,
        onPrimary = Color.White,
        primaryContainer = contenedor,
        onPrimaryContainer = sobreContenedor,
        secondary = primario,
        onSecondary = Color.White,
        secondaryContainer = contenedor,
        onSecondaryContainer = sobreContenedor,
        background = fondo,
        onBackground = TextoPrincipal,
        surface = Color.White,
        onSurface = TextoPrincipal,
        surfaceVariant = Gris100,
        onSurfaceVariant = TextoSecundario,
        surfaceContainerLowest = Color.White,
        surfaceContainerLow = Color.White,
        surfaceContainer = Color.White,
        surfaceContainerHigh = Gris100,
        surfaceContainerHighest = Gris100,
        outline = Borde,
        outlineVariant = Borde,
        error = PrioridadAlta,
        onError = Color.White
    )
}

private fun esquemaOscuro(rol: Rol): ColorScheme {
    val cliente = rol == Rol.CLIENTE
    val primario = if (cliente) CianClaro else VioletaClaro
    val contenedor = if (cliente) CianOscuro else VioletaOscuro
    val sobreContenedor = if (cliente) CianContenedor else VioletaContenedor
    return darkColorScheme(
        primary = primario,
        onPrimary = FondoOscuro,
        primaryContainer = contenedor,
        onPrimaryContainer = sobreContenedor,
        secondary = primario,
        onSecondary = FondoOscuro,
        secondaryContainer = contenedor,
        onSecondaryContainer = sobreContenedor,
        background = FondoOscuro,
        onBackground = Borde,
        surface = SuperficieOscura,
        onSurface = Borde,
        surfaceVariant = BordeOscuro,
        onSurfaceVariant = Color(0xFF94A3B8),
        surfaceContainerLowest = SuperficieOscura,
        surfaceContainerLow = SuperficieOscura,
        surfaceContainer = SuperficieOscura,
        surfaceContainerHigh = BordeOscuro,
        surfaceContainerHighest = BordeOscuro,
        outline = BordeOscuro,
        outlineVariant = BordeOscuro,
        error = Color(0xFFF87171),
        onError = FondoOscuro
    )
}