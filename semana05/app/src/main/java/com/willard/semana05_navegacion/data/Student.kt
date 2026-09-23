package com.willard.semana05_navegacion.data

// Modelo de datos de un estudiante del portal académico.
data class Student(
    val id: Int,
    val nombre: String,
    val carrera: String,
    val correo: String,
    val facultad: String,
    val biografia: String
)

// Fuente de datos de ejemplo. La consumen el Directorio y el Expediente.
val estudiantes = listOf(
    Student(
        id = 1,
        nombre = "Juan León",
        carrera = "Ingeniería de Sistemas",
        correo = "juan.leon@example.com",
        facultad = "Ingeniería y Tecnología",
        biografia = "Estudiante destacado con interés en desarrollo Android."
    ),
    Student(
        id = 2,
        nombre = "María García",
        carrera = "Arquitectura",
        correo = "maria.garcia@example.com",
        facultad = "Arquitectura y Urbanismo",
        biografia = "Apasionada por el diseño sostenible y los espacios urbanos."
    ),
    Student(
        id = 3,
        nombre = "Carlos Pérez",
        carrera = "Medicina",
        correo = "carlos.perez@example.com",
        facultad = "Ciencias de la Salud",
        biografia = "Interesado en la investigación clínica y la salud pública."
    ),
    Student(
        id = 4,
        nombre = "Ana López",
        carrera = "Derecho",
        correo = "ana.lopez@example.com",
        facultad = "Ciencias Jurídicas",
        biografia = "Enfocada en derecho digital y protección de datos."
    ),
    Student(
        id = 5,
        nombre = "Luis Ramírez",
        carrera = "Administración",
        correo = "luis.ramirez@example.com",
        facultad = "Ciencias Empresariales",
        biografia = "Emprendedor con interés en gestión de proyectos tecnológicos."
    )
)

// Busca un estudiante por su id; si no existe, devuelve el primero.
fun estudiantePorId(id: Int): Student =
    estudiantes.firstOrNull { it.id == id } ?: estudiantes.first()