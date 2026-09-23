package com.willard.tecsupfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.willard.tecsupfit.ui.screens.InicioScreen
import com.willard.tecsupfit.ui.theme.TecsupFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TecsupFitTheme {
                // Temporal: en el siguiente commit aquí irá el NavHost
                InicioScreen(onClaseClick = {})
            }
        }
    }
}