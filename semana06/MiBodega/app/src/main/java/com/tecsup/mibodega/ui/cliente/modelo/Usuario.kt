package com.tecsup.mibodega.ui.cliente.modelo


data class Usuario(
    val nombre: String,
    val telefono: String,
    val direccion: String,
    val referencia: String
)


val usuarioDemo = Usuario(
    nombre = "Juan Pérez",
    telefono = "987 654 321",
    direccion = "Av. Los Olivos 123",
    referencia = "Frente al parque"
)