package com.tecsup.mibodega.ui.cliente.modelo

import java.io.Serializable


data class Usuario(
    val usuario: String,
    val clave: String,
    val nombre: String,
    val telefono: String,
    val direccion: String,
    val referencia: String
) : Serializable


// Credenciales FIJAS del login (no hay backend todavía)
const val USUARIO_LOGIN = "cliente"
const val CLAVE_LOGIN = "1234"

// Datos del usuario que entra con USUARIO_LOGIN / CLAVE_LOGIN
val usuarioDemo = Usuario(
    usuario = USUARIO_LOGIN,
    clave = CLAVE_LOGIN,
    nombre = "Juan Pérez",
    telefono = "987 654 321",
    direccion = "Av. Los Olivos 123",
    referencia = "Frente al parque"
)