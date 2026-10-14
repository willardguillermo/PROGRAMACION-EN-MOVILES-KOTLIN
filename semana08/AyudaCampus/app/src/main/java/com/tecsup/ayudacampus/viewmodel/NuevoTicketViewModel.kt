package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.entity.Categoria
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FormularioTicket(
    val categoriaId: Int? = null,
    val titulo: String = "",
    val ubicacion: String = "",
    val prioridad: Prioridad = Prioridad.MEDIA,
    val descripcion: String = "",
    val errorCategoria: String? = null,
    val errorTitulo: String? = null,
    val errorDescripcion: String? = null,
    val enviando: Boolean = false
)

class NuevoTicketViewModel(
    private val repo: AyudaRepository,
    private val clienteId: Int
) : ViewModel() {

    val categorias: StateFlow<List<Categoria>> = repo.observarCategoriasActivas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _formulario = MutableStateFlow(FormularioTicket())
    val formulario: StateFlow<FormularioTicket> = _formulario.asStateFlow()

    private val _enviado = MutableStateFlow(false)
    val enviado: StateFlow<Boolean> = _enviado.asStateFlow()

    fun elegirCategoria(id: Int) {
        _formulario.update { it.copy(categoriaId = id, errorCategoria = null) }
    }

    fun cambiarTitulo(texto: String) {
        _formulario.update { it.copy(titulo = texto, errorTitulo = null) }
    }

    fun cambiarUbicacion(texto: String) {
        _formulario.update { it.copy(ubicacion = texto) }
    }

    fun elegirPrioridad(prioridad: Prioridad) {
        _formulario.update { it.copy(prioridad = prioridad) }
    }

    fun cambiarDescripcion(texto: String) {
        _formulario.update { it.copy(descripcion = texto, errorDescripcion = null) }
    }

    fun enviar() {
        val f = _formulario.value
        val categoriaId = f.categoriaId
        val categoriaValida = categoriaId != null && categorias.value.any { it.id == categoriaId }

        val errorCategoria = if (!categoriaValida) "Elige una categoría" else null
        val errorTitulo = if (f.titulo.trim().length < 5) "El título debe tener al menos 5 caracteres" else null
        val errorDescripcion = if (f.descripcion.trim().length < 10) "La descripción debe tener al menos 10 caracteres" else null

        if (errorCategoria != null || errorTitulo != null || errorDescripcion != null || categoriaId == null) {
            _formulario.value = f.copy(
                errorCategoria = errorCategoria,
                errorTitulo = errorTitulo,
                errorDescripcion = errorDescripcion
            )
            return
        }

        _formulario.update { it.copy(enviando = true) }
        viewModelScope.launch {
            repo.crearTicket(
                clienteId = clienteId,
                categoriaId = categoriaId,
                titulo = f.titulo,
                descripcion = f.descripcion,
                ubicacion = f.ubicacion,
                prioridad = f.prioridad
            )
            _formulario.value = FormularioTicket()
            _enviado.value = true
        }
    }

    fun confirmarEnvio() {
        _enviado.value = false
    }
}