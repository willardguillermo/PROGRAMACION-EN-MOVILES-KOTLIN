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
import androidx.compose.ui.unit.sp
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

    var fundamentos by remember { mutableStateOf(0f) }
    var poo by remember { mutableStateOf(0f) }
    var moviles by remember { mutableStateOf(0f) }
    var baseDatos by remember { mutableStateOf(0f) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 40.dp,
                start = 16.dp,
                end = 16.dp
            )
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


        // FUNDAMENTOS

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Fundamentos de Programación (20%)"
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Slider(
                value = fundamentos,
                onValueChange = {
                    fundamentos = it
                },
                valueRange = 0f..20f,
                steps = 19,
                modifier = Modifier.weight(1f)
            )


            Text(
                text = fundamentos.toInt().toString()
            )
        }



        // POO

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        Text(
            text = "Programación Orientada a Objetos (25%)"
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Slider(
                value = poo,
                onValueChange = {
                    poo = it
                },
                valueRange = 0f..20f,
                steps = 19,
                modifier = Modifier.weight(1f)
            )


            Text(
                text = poo.toInt().toString()
            )
        }



        // MOVILES

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        Text(
            text = "Programación en Móviles (30%)"
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Slider(
                value = moviles,
                onValueChange = {
                    moviles = it
                },
                valueRange = 0f..20f,
                steps = 19,
                modifier = Modifier.weight(1f)
            )


            Text(
                text = moviles.toInt().toString()
            )
        }



        // BASE DE DATOS

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        Text(
            text = "Base de Datos (25%)"
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Slider(
                value = baseDatos,
                onValueChange = {
                    baseDatos = it
                },
                valueRange = 0f..20f,
                steps = 19,
                modifier = Modifier.weight(1f)
            )


            Text(
                text = baseDatos.toInt().toString()
            )
        }

    }
}