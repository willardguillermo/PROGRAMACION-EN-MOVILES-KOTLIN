package com.tecsup.mibodega.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BodegaColorScheme = lightColorScheme(
    primary = VerdeBodega,
    onPrimary = Blanco,
    secondary = AzulEnlace,
    secondaryContainer = FondoClaro,
    background = Blanco,
    onBackground = AzulTexto,
    surface = Blanco,
    onSurface = AzulTexto,
    surfaceVariant = GrisClaro,
    onSurfaceVariant = GrisTexto,
    outline = GrisBorde,
    error = RojoPrecio
)

/** Versión oscura de la misma paleta: fondos oscuros y textos claros. */
private val BodegaColorSchemeOscuro = darkColorScheme(
    primary = Color(0xFF4CAF5E),          // verde un poco más claro para que resalte en oscuro
    onPrimary = Blanco,
    secondary = Color(0xFF6EA8FF),
    secondaryContainer = Color(0xFF1E2A36),
    background = Color(0xFF121417),
    onBackground = Color(0xFFE6E8EB),
    surface = Color(0xFF1A1D21),
    onSurface = Color(0xFFE6E8EB),
    surfaceVariant = Color(0xFF2A2F36),
    onSurfaceVariant = Color(0xFFAAB2BD),
    outline = Color(0xFF4A525C),
    error = Color(0xFFFF6B5E)
)

/**
 * @param modoOscuro lo controla el Switch de "Perfil" (estado guardado en MainActivity)
 */
@Composable
fun BodegaTheme(
    modoOscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (modoOscuro) BodegaColorSchemeOscuro else BodegaColorScheme,
        typography = BodegaTypography,
        content = content
    )
}
