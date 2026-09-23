package com.willard.clinicasalud.data

// Representa a un médico de la clínica
data class Medico(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val calificacion: Double,
    val reseñas: Int,
    val añosExperiencia: Int,
    val descripcion: String
)

// Estados de cita (CANCELADA se agregó para poder cancelar citas)
enum class EstadoCita { CONFIRMADA, COMPLETADA, CANCELADA }

// Cita agendada por el paciente
data class Cita(
    val id: Int,
    val medico: Medico,
    val fecha: String,
    val hora: String,
    val estado: EstadoCita
)

// Datos de prueba de la app
object DatosClinica {

    val especialidades = listOf(
        "Todas", "Cardiología", "Pediatría", "Dermatología", "Podología", "Oftalmología"
    )

    val medicos = listOf(
        Medico(
            id = 1,
            nombre = "Dra. Ana Torres",
            especialidad = "Cardiología",
            calificacion = 4.9,
            reseñas = 124,
            añosExperiencia = 12,
            descripcion = "Especialista en arritmias e hipertensión. Formación en la Clínica Mayo."
        ),
        Medico(
            id = 2,
            nombre = "Dr. Luis Vega",
            especialidad = "Pediatría",
            calificacion = 4.7,
            reseñas = 98,
            añosExperiencia = 8,
            descripcion = "Atención integral del niño y control de crecimiento."
        ),
        Medico(
            id = 3,
            nombre = "Dra. Rosa Díaz",
            especialidad = "Dermatología",
            calificacion = 4.8,
            reseñas = 110,
            añosExperiencia = 10,
            descripcion = "Tratamiento de acné, dermatitis y dermatología estética."
        ),
        Medico(
            id = 4,
            nombre = "Dr. Carlos Ramos",
            especialidad = "Cardiología",
            calificacion = 4.6,
            reseñas = 75,
            añosExperiencia = 15,
            descripcion = "Prevención cardiovascular y chequeos preventivos."
        ),
        Medico(
            id = 5,
            nombre = "Dr. Guillermo Willard",
            especialidad = "Podología",
            calificacion = 5.0,
            reseñas = 71,
            añosExperiencia = 10,
            descripcion = "Tratamiento de hongos, infecciones o heridas en los pies."
        ),
        Medico(
            id = 6,
            nombre = "Dra. Carmen Salas",
            especialidad = "Oftalmología",
            calificacion = 4.8,
            reseñas = 89,
            añosExperiencia = 9,
            descripcion = "Evaluación de la vista, medida de lentes y control de glaucoma."
        )
    )

    // Opciones para la pantalla de agendar cita
    val fechas = listOf("Jue 26", "Vie 27", "Sáb 28")
    val horas = listOf("9:00 am", "10:30 am", "3:00 pm")

    // Búsqueda de médico por id
    fun buscarMedico(id: Int): Medico? = medicos.find { it.id == id }
}