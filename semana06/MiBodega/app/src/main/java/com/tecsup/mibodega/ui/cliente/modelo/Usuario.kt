package com.tecsup.mibodega.ui.cliente.modelo

/**
 * Datos del cliente que se llenan en "Crear cuenta".
 * Luego se reutilizan para pre-llenar "Datos de entrega" y en "Perfil".
 */
data class Usuario(
    val nombre: String,
    val telefono: String,
    val direccion: String,
    val referencia: String
)

/**
 * Usuario de prueba para el "Iniciar sesión" simulado
 * (todavía no hay backend ni base de datos que validen credenciales).
 */
val usuarioDemo = Usuario(
    nombre = "Juan Pérez",
    telefono = "987 654 321",
    direccion = "Av. Los Olivos 123",
    referencia = "Frente al parque"
)