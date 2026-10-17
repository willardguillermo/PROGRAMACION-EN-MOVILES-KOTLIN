package com.tecsup.ayudacampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.ayudacampus.ui.components.AppTopBar
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.BotonCambiarPerfil
import com.tecsup.ayudacampus.ui.components.ContadorColor
import com.tecsup.ayudacampus.ui.components.FilaPreferencia
import com.tecsup.ayudacampus.ui.components.GrupoPreferencias
import com.tecsup.ayudacampus.ui.components.NotaDataStore
import com.tecsup.ayudacampus.ui.components.TarjetaUsuario
import com.tecsup.ayudacampus.ui.theme.EstadoAbierto
import com.tecsup.ayudacampus.ui.theme.EstadoResuelto
import com.tecsup.ayudacampus.viewmodel.PerfilViewModel
import com.tecsup.ayudacampus.viewmodel.SesionViewModel

@Composable
fun PerfilScreen(
    vm: PerfilViewModel,
    sesionVm: SesionViewModel,
    alCambiarPerfil: () -> Unit,
    alCerrarSesion: () -> Unit
) {
    val usuario by sesionVm.usuarioActual.collectAsStateWithLifecycle()
    val modoOscuro by sesionVm.modoOscuro.collectAsStateWithLifecycle()
    val notificaciones by sesionVm.notificaciones.collectAsStateWithLifecycle()
    val abiertos by vm.abiertos.collectAsStateWithLifecycle()
    val resueltos by vm.resueltos.collectAsStateWithLifecycle()
    val total by vm.total.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(titulo = "Perfil")

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            usuario?.let { TarjetaUsuario(it.nombre, it.correo, "Estudiante") }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ContadorColor("Abiertos", abiertos, EstadoAbierto, Modifier.weight(1f))
                ContadorColor("Resueltos", resueltos, EstadoResuelto, Modifier.weight(1f))
                ContadorColor("Total", total, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
            }

            BotonCambiarPerfil(destino = "Soporte TI", alPulsar = alCambiarPerfil)

            GrupoPreferencias {
                FilaPreferencia(
                    titulo = "Modo oscuro",
                    descripcion = "Cambia el tema de la app",
                    activo = modoOscuro,
                    alCambiar = sesionVm::guardarModoOscuro
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                FilaPreferencia(
                    titulo = "Notificaciones",
                    descripcion = "Avisar cambios de estado",
                    activo = notificaciones,
                    alCambiar = sesionVm::guardarNotificaciones
                )
            }

            NotaDataStore()

            AyudaButton(
                texto = "Cerrar sesión",
                alPulsar = alCerrarSesion,
                contorno = true,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}