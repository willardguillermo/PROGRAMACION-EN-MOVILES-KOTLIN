package com.willard.tecsupfit.data

// Un horario de la clase con los cupos que le quedan
data class Horario(
    val hora: String,
    val cuposDisponibles: Int
)

// Clase del gimnasio
data class Clase(
    val id: Int,
    val nombre: String,
    val instructor: String,
    val sala: String,
    val duracionMin: Int,
    val esHoy: Boolean,             // true = se dicta hoy (para el filtro "Hoy")
    val dia: String,                // día que se muestra en la tarjeta
    val descripcion: String,
    val horarios: List<Horario>     // entre estos elige el usuario (selección única)
)

// Estados de una reserva
enum class EstadoReserva { CONFIRMADA, COMPLETADA }

// Reserva hecha por el usuario
data class Reserva(
    val id: Int,
    val clase: Clase,
    val horario: String,
    val estado: EstadoReserva
)

// Rutina de entrenamiento sugerida
data class Rutina(
    val id: Int,
    val nombre: String,
    val nivel: String,
    val duracionMin: Int,
    val ejercicios: List<String>
)

// Datos de prueba de la app
object DatosGimnasio {

    // Filtros de la fila de chips
    val filtros = listOf("Hoy", "Esta semana")

    val clases = listOf(
        Clase(
            id = 1,
            nombre = "Yoga funcional",
            instructor = "Lucía Paredes",
            sala = "Sala 2",
            duracionMin = 50,
            esHoy = true,
            dia = "Hoy",
            descripcion = "Posturas de yoga combinadas con fuerza y movilidad.",
            horarios = listOf(Horario("7:00 am", 5), Horario("12:00 pm", 8), Horario("7:00 pm", 0))
        ),
        Clase(
            id = 2,
            nombre = "Cross Training",
            instructor = "Marco Salas",
            sala = "Sala 1",
            duracionMin = 45,
            esHoy = true,
            dia = "Hoy",
            descripcion = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
            horarios = listOf(Horario("6:00 am", 3), Horario("6:00 pm", 8), Horario("8:00 pm", 12))
        ),
        Clase(
            id = 3,
            nombre = "Spinning",
            instructor = "Andrea Torres",
            sala = "Sala 3",
            duracionMin = 45,
            esHoy = true,
            dia = "Hoy",
            descripcion = "Ciclismo indoor con música para mejorar la resistencia.",
            horarios = listOf(Horario("7:30 am", 0), Horario("1:00 pm", 6), Horario("7:30 pm", 10))
        ),
        Clase(
            id = 4,
            nombre = "Pilates",
            instructor = "Carla Mendoza",
            sala = "Sala 2",
            duracionMin = 55,
            esHoy = false,
            dia = "Jueves",
            descripcion = "Control del core, postura y flexibilidad.",
            horarios = listOf(Horario("8:00 am", 9), Horario("5:00 pm", 4), Horario("7:00 pm", 7))
        ),
        Clase(
            id = 5,
            nombre = "Boxeo fitness",
            instructor = "Renzo Quispe",
            sala = "Sala 1",
            duracionMin = 60,
            esHoy = false,
            dia = "Viernes",
            descripcion = "Técnica de golpes y cardio sin contacto.",
            horarios = listOf(Horario("6:30 am", 6), Horario("6:30 pm", 2), Horario("8:30 pm", 11))
        ),
        Clase(
            id = 6,
            nombre = "Zumba",
            instructor = "Valeria Ríos",
            sala = "Sala 3",
            duracionMin = 50,
            esHoy = false,
            dia = "Sábado",
            descripcion = "Baile aeróbico con ritmos latinos.",
            horarios = listOf(Horario("9:00 am", 15), Horario("11:00 am", 12), Horario("4:00 pm", 8))
        )
    )

    // Rutinas que se muestran en la pestaña Rutinas
    val rutinas = listOf(
        Rutina(
            id = 1,
            nombre = "Full body básico",
            nivel = "Principiante",
            duracionMin = 30,
            ejercicios = listOf("Sentadillas 3x12", "Flexiones 3x10", "Plancha 3x30 s", "Zancadas 3x10")
        ),
        Rutina(
            id = 2,
            nombre = "Tren superior",
            nivel = "Intermedio",
            duracionMin = 40,
            ejercicios = listOf("Press de banca 4x10", "Remo con mancuerna 4x12", "Press militar 3x10", "Fondos 3x12")
        ),
        Rutina(
            id = 3,
            nombre = "Piernas y glúteos",
            nivel = "Intermedio",
            duracionMin = 45,
            ejercicios = listOf("Sentadilla con barra 4x10", "Peso muerto rumano 4x10", "Hip thrust 3x12", "Prensa 3x15")
        ),
        Rutina(
            id = 4,
            nombre = "Cardio HIIT",
            nivel = "Avanzado",
            duracionMin = 25,
            ejercicios = listOf("Burpees 40 s", "Saltos de tijera 40 s", "Escaladores 40 s", "Descanso 20 s entre series")
        )
    )

    // Busca una clase por su id (se usa al recibir el parámetro de navegación)
    fun buscarClase(id: Int): Clase? = clases.find { it.id == id }
}