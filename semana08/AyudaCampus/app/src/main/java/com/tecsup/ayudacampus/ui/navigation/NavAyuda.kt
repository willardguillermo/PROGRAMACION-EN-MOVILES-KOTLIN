package com.tecsup.ayudacampus.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.ui.cliente.LoginScreen
import com.tecsup.ayudacampus.ui.cliente.SplashScreen
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.viewmodel.SesionViewModel

@Composable
fun NavAyuda(sesionVm: SesionViewModel) {
    val nav = rememberNavController()

    val irAGrafo: (Rol) -> Unit = { rol ->
        val destino = if (rol == Rol.ADMIN) Rutas.GRAFO_SOPORTE else Rutas.GRAFO_CLIENTE
        nav.navigate(destino) {
            popUpTo(nav.graph.id) { inclusive = true }
            launchSingleTop = true
        }
    }

    val cerrarSesion: () -> Unit = {
        sesionVm.cerrarSesion()
        nav.navigate(Rutas.LOGIN) {
            popUpTo(nav.graph.id) { inclusive = true }
        }
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        NavHost(
            navController = nav,
            startDestination = Rutas.SPLASH,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {
            composable(Rutas.SPLASH) {
                SplashScreen(
                    vm = sesionVm,
                    alComenzar = {
                        nav.navigate(Rutas.LOGIN) {
                            popUpTo(Rutas.SPLASH) { inclusive = true }
                        }
                    },
                    alEntrar = irAGrafo
                )
            }

            composable(Rutas.LOGIN) {
                LoginScreen(vm = sesionVm, alIngresar = irAGrafo)
            }

            navigation(startDestination = Rutas.MIS_TICKETS, route = Rutas.GRAFO_CLIENTE) {
                composable(Rutas.MIS_TICKETS) {
                    PantallaPendiente("Mis tickets", 7, cerrarSesion)
                }
            }

            navigation(startDestination = Rutas.PANEL, route = Rutas.GRAFO_SOPORTE) {
                composable(Rutas.PANEL) {
                    PantallaPendiente("Panel de soporte", 10, cerrarSesion)
                }
            }
        }
    }
}

@Composable
private fun PantallaPendiente(titulo: String, paso: Int, alCerrarSesion: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Se completa en el Paso $paso", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))
        AyudaButton(texto = "Cerrar sesión", alPulsar = alCerrarSesion, contorno = true)
    }
}