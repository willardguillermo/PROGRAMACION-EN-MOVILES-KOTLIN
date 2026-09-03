package com.willard.ejercicioregistronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.willard.ejercicioregistronotas.ui.theme.EjercicioRegistroNotasTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            EjercicioRegistroNotasTheme {

                RegistroNotasScreen()

            }
        }
    }
}


@Composable
fun RegistroNotasScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Registro de Notas",
            style = MaterialTheme.typography.headlineSmall
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        Text(
            text = "Notas del ciclo",
            style = MaterialTheme.typography.titleMedium
        )


        Text(
            text = "Desliza para asignar cada nota (0 a 20)"
        )


    }

}