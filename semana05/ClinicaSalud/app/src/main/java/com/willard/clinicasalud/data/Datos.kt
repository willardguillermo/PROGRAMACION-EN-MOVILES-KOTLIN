package com.willard.clinicasalud.data

// Representa a un médico de la clínica
data class Medico(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val calificacion: Double,
    val resenas: Int,
    val aniosExperiencia: Int,
    val descripcion: String
)

// Estados de cita
enum class EstadoCita { CONFIRMADA, COMPLETADA }

// cita agendada por paciente
data class Cita(
    val id: Int,
    val medico: Medico,
    val fecha: String,
    val hora: String,
    val estado: EstadoCita
)

// Datos de prueba de la app
object DatosClinica {

    val especialidades = listOf("Todas", "Cardiología", "Pediatría", "Dermatología")

    val medicos = listOf(
        Medico(
            id = 1,
            nombre = "Dra. Ana Torres",
            especialidad = "Cardiología",
            calificacion = 4.9,
            resenas = 124,
            aniosExperiencia = 12,
            descripcion = "Especialista en arritmias e hipertensión. Formación en la Clínica Mayo."
        ),
        Medico(
            id = 2,
            nombre = "Dr. Luis Vega",
            especialidad = "Pediatría",
            calificacion = 4.7,
            resenas = 98,
            aniosExperiencia = 8,
            descripcion = "Atención integral del niño y control de crecimiento."
        ),
        Medico(
            id = 3,
            nombre = "Dra. Rosa Díaz",
            especialidad = "Dermatología",
            calificacion = 4.8,
            resenas = 110,
            aniosExperiencia = 10,
            descripcion = "Tratamiento de acné, dermatitis y dermatología estética."
        ),
        Medico(
            id = 4,
            nombre = "Dr. Carlos Ramos",
            especialidad = "Cardiología",
            calificacion = 4.6,
            resenas = 75,
            aniosExperiencia = 15,
            descripcion = "Prevención cardiovascular y chequeos preventivos."
        )
    )

    // Opciones para la pantalla de agendar cita
    val fechas = listOf("Jue 26", "Vie 27", "Sáb 28")
    val horas = listOf("9:00 am", "10:30 am", "3:00 pm")

    // Busqueda de medico por id
    fun buscarMedico(id: Int): Medico? = medicos.find { it.id == id }
}