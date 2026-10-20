package com.tecsup.ayudacampus.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistroViewModel(private val repo: AyudaRepository) : ViewModel() {
    private val _nombre = MutableStateFlow("")
    val nombre = _nombre.asStateFlow()

    private val _correo = MutableStateFlow("")
    val correo = _correo.asStateFlow()

    private val _clave = MutableStateFlow("")
    val clave = _clave.asStateFlow()

    private val _confirmarClave = MutableStateFlow("")
    val confirmarClave = _confirmarClave.asStateFlow()

    private val _errorNombre = MutableStateFlow<String?>(null)
    val errorNombre = _errorNombre.asStateFlow()

    private val _errorCorreo = MutableStateFlow<String?>(null)
    val errorCorreo = _errorCorreo.asStateFlow()

    private val _errorClave = MutableStateFlow<String?>(null)
    val errorClave = _errorClave.asStateFlow()

    private val _errorConfirmarClave = MutableStateFlow<String?>(null)
    val errorConfirmarClave = _errorConfirmarClave.asStateFlow()

    private val _registrado = MutableStateFlow(false)
    val registrado = _registrado.asStateFlow()

    fun onNombreChange(valor: String) {
        _nombre.value = valor
        _errorNombre.value = null
    }

    fun onCorreoChange(valor: String) {
        _correo.value = valor
        _errorCorreo.value = null
    }

    fun onClaveChange(valor: String) {
        _clave.value = valor
        _errorClave.value = null
    }

    fun onConfirmarClaveChange(valor: String) {
        _confirmarClave.value = valor
        _errorConfirmarClave.value = null
    }

    fun registrar() {
        var valido = true

        val nom = _nombre.value.trim()
        if (nom.length < 3) {
            _errorNombre.value = "Nombre debe tener al menos 3 caracteres"
            valido = false
        }

        val cor = _correo.value.trim().lowercase().replace(" ", "")
        if (!Patterns.EMAIL_ADDRESS.matcher(cor).matches()) {
            _errorCorreo.value = "Formato de correo inválido"
            valido = false
        }

        val cla = _clave.value
        if (cla.length < 4) {
            _errorClave.value = "Contraseña debe tener al menos 4 caracteres"
            valido = false
        }

        val confCla = _confirmarClave.value
        if (cla != confCla) {
            _errorConfirmarClave.value = "Las contraseñas no coinciden"
            valido = false
        }

        if (!valido) return

        viewModelScope.launch {
            if (repo.existeCorreo(cor)) {
                _errorCorreo.value = "Este correo ya está registrado"
                return@launch
            }
            repo.registrarEstudiante(nom, cor, cla)
            _registrado.value = true
        }
    }
}
