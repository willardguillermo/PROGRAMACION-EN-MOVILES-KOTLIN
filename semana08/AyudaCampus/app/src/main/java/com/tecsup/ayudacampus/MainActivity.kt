package com.tecsup.ayudacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.ui.navigation.NavAyuda
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
import com.tecsup.ayudacampus.viewmodel.AyudaViewModelFactory
import com.tecsup.ayudacampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as AyudaApp

        setContent {
            val sesionVm: SesionViewModel = viewModel(
                factory = AyudaViewModelFactory {
                    SesionViewModel(app.repository, app.preferencias)
                }
            )
            val usuario by sesionVm.usuarioActual.collectAsStateWithLifecycle()
            val oscuro by sesionVm.modoOscuro.collectAsStateWithLifecycle()
            val rol = usuario?.let { Rol.valueOf(it.rol) } ?: Rol.CLIENTE

            AyudaTheme(rol = rol, oscuro = oscuro) {
                NavAyuda(sesionVm)
            }
        }
    }
}