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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Switch
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
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

    var fundamentos by rememberSaveable { mutableStateOf(0f) }

    var poo by rememberSaveable { mutableStateOf(0f) }

    var moviles by rememberSaveable { mutableStateOf(0f) }

    var baseDatos by rememberSaveable { mutableStateOf(0f) }

    var cursoActivo by rememberSaveable { mutableStateOf(true) }

    var asistenciaRegistrada by rememberSaveable { mutableStateOf(false) }



    val promedioFinal =
        (fundamentos * 0.20f) +
                (poo * 0.25f) +
                (moviles * 0.30f) +
                (baseDatos * 0.25f)


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

        Spacer(
            modifier = Modifier.height(24.dp)
        )


        Text(
            text = "Estado del curso",
            style = MaterialTheme.typography.titleMedium
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = if (cursoActivo) {
                    "Activo"
                } else {
                    "Inactivo"
                }
            )


            Switch(
                checked = cursoActivo,
                onCheckedChange = {
                    cursoActivo = it
                }
            )
        }



        Spacer(
            modifier = Modifier.height(16.dp)
        )


        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Checkbox(
                checked = asistenciaRegistrada,
                onCheckedChange = {
                    asistenciaRegistrada = it
                }
            )


            Text(
                text = "Asistencia registrada",
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )


        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Resumen académico",
                    style = MaterialTheme.typography.titleMedium
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                Text(
                    text = "Promedio final: %.2f".format(promedioFinal)
                )


                Text(
                    text = if (promedioFinal >= 11) {
                        "Estado: APROBADO ✅"
                    } else {
                        "Estado: DESAPROBADO ❌"
                    },
                    color = if (promedioFinal >= 11) {
                        Color(0xFF2E7D32)
                    } else {
                        Color.Red
                    }
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                Text(
                    text = if (cursoActivo) {
                        "Curso: Activo ✅"
                    } else {
                        "Curso: Inactivo ⚠️"
                    }
                )


                Text(
                    text = if (asistenciaRegistrada) {
                        "Asistencia: Registrada ✅"
                    } else {
                        "Asistencia: Pendiente ⚠️"
                    }
                )

            }
        }

    }
}