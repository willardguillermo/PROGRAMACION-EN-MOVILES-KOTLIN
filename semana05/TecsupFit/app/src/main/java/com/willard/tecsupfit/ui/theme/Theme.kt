package com.willard.tecsupfit.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Verde80,
    secondary = VerdeGris80,
    tertiary = Verde80
)

// Colores del tema claro con la paleta verde
private val LightColorScheme = lightColorScheme(
    primary = Verde40,
    onPrimary = Color.White,
    primaryContainer = VerdeClaro90,
    onPrimaryContainer = VerdeOscuro10,
    secondary = VerdeGris40,
    secondaryContainer = VerdeClaro90,
    onSecondaryContainer = VerdeOscuro10,
    tertiary = Verde40
)

@Composable
fun TecsupFitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // En false para usar siempre la paleta verde y no los colores del fondo del teléfono
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}