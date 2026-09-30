package com.willard.Lab04CarritoTecsup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.willard.Lab04CarritoTecsup.ui.theme.Lab04CarritoTecsupTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab04CarritoTecsupTheme {
                AppNavegacion()
            }
        }
    }
}