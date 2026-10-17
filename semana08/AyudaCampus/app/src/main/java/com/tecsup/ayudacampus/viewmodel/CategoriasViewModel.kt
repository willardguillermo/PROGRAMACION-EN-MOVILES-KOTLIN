package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.entity.Categoria
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FormCategoria(
    val id: Int = 0,
    val nombre: String = "",
    val icono: String = "tag",
    val activa: Boolean = true,
    val tickets: Int = 0,
    val error: String? = null
) {
    fun aCategoria() = Categoria(id = id, nombre = nombre, icono = icono, activa = activa)
}

class CategoriasViewModel(private val repo: AyudaRepository) : ViewModel() {

    val categorias: StateFlow<List<Categoria>> = repo.observarCategorias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _form = MutableStateFlow(FormCategoria())
    val form: StateFlow<FormCategoria> = _form.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _terminado = MutableStateFlow(false)
    val terminado: StateFlow<Boolean> = _terminado.asStateFlow()

    fun alternar(c: Categoria) {
        viewModelScope.launch { repo.cambiarActiva(c.id, !c.activa) }
    }

    fun cargar(id: Int) {
        if (id == 0 || _form.value.id == id) return
        viewModelScope.launch {
            val c = repo.obtenerCategoria(id) ?: return@launch
            _form.value = FormCategoria(
                id = c.id,
                nombre = c.nombre,
                icono = c.icono,
                activa = c.activa,
                tickets = repo.contarTicketsDeCategoria(c.id)
            )
        }
    }

    fun cambiarNombre(texto: String) {
        _form.update { it.copy(nombre = texto, error = null) }
    }

    fun elegirIcono(icono: String) {
        _form.update { it.copy(icono = icono) }
    }

    fun cambiarActiva(activa: Boolean) {
        _form.update { it.copy(activa = activa) }
    }

    fun guardar(categoria: Categoria) {
        if (categoria.nombre.isBlank()) {
            _form.update { it.copy(error = "El nombre es obligatorio") }
            return
        }
        viewModelScope.launch {
            repo.guardarCategoria(categoria.copy(nombre = categoria.nombre.trim()))
            _terminado.value = true
        }
    }

    fun eliminar(categoria: Categoria) {
        viewModelScope.launch {
            if (repo.contarTicketsDeCategoria(categoria.id) > 0) {
                _mensaje.value = "No se puede eliminar \"${categoria.nombre}\" porque ya tiene tickets"
            } else {
                repo.eliminarCategoria(categoria)
                _terminado.value = true
            }
        }
    }
}