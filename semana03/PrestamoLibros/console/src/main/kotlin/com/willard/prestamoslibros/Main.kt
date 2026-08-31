package com.willard.prestamolibros

import com.willard.prestamolibros.model.Prestamo
import com.willard.prestamolibros.model.TipoUsuario
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

val formatoFecha: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")


// --------------------------------------------------
// SELECCIONAR TIPO DE USUARIO
// --------------------------------------------------

fun seleccionarTipoUsuario(): TipoUsuario {

    while (true) {

        println()
        println("TIPO DE USUARIO")
        println("1. Alumno  - S/ 1.50 por día")
        println("2. Docente - S/ 2.00 por día")
        print("Seleccione una opción: ")

        when (readln().trim()) {

            "1" -> return TipoUsuario.ALUMNO

            "2" -> return TipoUsuario.DOCENTE

            else -> {
                println("Opción inválida. Intente nuevamente.")
            }
        }
    }
}


// --------------------------------------------------
// LEER FECHA
// --------------------------------------------------

fun leerFecha(mensaje: String): LocalDate {

    while (true) {

        print(mensaje)

        val entrada = readln().trim()

        try {

            return LocalDate.parse(
                entrada,
                formatoFecha
            )

        } catch (e: DateTimeParseException) {

            println(
                "Fecha inválida. Use el formato dd/MM/yyyy."
            )
        }
    }
}


// --------------------------------------------------
// REGISTRAR PRÉSTAMO
// --------------------------------------------------

fun registrarPrestamo(): Prestamo {

    println()
    println("========================================")
    println("        REGISTRAR PRÉSTAMO")
    println("========================================")

    var tituloLibro: String

    while (true) {

        print("Título del libro: ")

        tituloLibro = readln().trim()

        if (tituloLibro.isNotEmpty()) {
            break
        }

        println("El título del libro no puede estar vacío.")
    }


    val tipoUsuario = seleccionarTipoUsuario()


    println()

    val fechaPrestamo =
        leerFecha(
            "Fecha de préstamo (dd/MM/yyyy): "
        )


    var fechaDevolucion: LocalDate

    while (true) {

        fechaDevolucion =
            leerFecha(
                "Fecha de devolución (dd/MM/yyyy): "
            )

        if (
            fechaDevolucion.isBefore(
                fechaPrestamo
            )
        ) {

            println(
                "La fecha de devolución no puede ser anterior a la fecha de préstamo."
            )

        } else {

            break
        }
    }


    return Prestamo(
        tituloLibro = tituloLibro,
        tipoUsuario = tipoUsuario,
        fechaPrestamo = fechaPrestamo,
        fechaDevolucion = fechaDevolucion
    )
}


// --------------------------------------------------
// MOSTRAR RESULTADO DEL PRÉSTAMO
// --------------------------------------------------

fun mostrarPrestamo(prestamo: Prestamo) {

    println()
    println("========================================")
    println("          DETALLE DEL PRÉSTAMO")
    println("========================================")

    println("Libro: ${prestamo.tituloLibro}")

    println(
        "Tipo de usuario: ${
            when (prestamo.tipoUsuario) {

                TipoUsuario.ALUMNO ->
                    "Alumno"

                TipoUsuario.DOCENTE ->
                    "Docente"
            }
        }"
    )

    println(
        "Multa por día: S/ %.2f".format(
            prestamo.tipoUsuario.multaDiaria
        )
    )

    println(
        "Fecha de préstamo: ${
            prestamo.fechaPrestamo.format(
                formatoFecha
            )
        }"
    )

    println(
        "Fecha de devolución: ${
            prestamo.fechaDevolucion.format(
                formatoFecha
            )
        }"
    )


    println()
    println("----------------------------------------")
    println("ESTADO")
    println("----------------------------------------")

    println(
        prestamo.obtenerEstado()
    )


    val detalles =
        prestamo.generarDetalleMulta()


    if (detalles.isEmpty()) {

        println()
        println("No existe mora.")
        println("Multa total: S/ 0.00")

    } else {

        println()
        println("----------------------------------------")
        println("             DETALLE DE MORA")
        println("----------------------------------------")

        println(
            "%-5s %-12s %-12s %-12s".format(
                "Día",
                "Fecha",
                "Multa día",
                "Acumulado"
            )
        )

        println(
            "---------------------------------------------"
        )


        detalles.forEach { detalle ->

            println(
                "%-5d %-12s S/%-10.2f S/%-10.2f".format(
                    detalle.dia,
                    detalle.fecha.format(
                        formatoFecha
                    ),
                    detalle.multaDia,
                    detalle.acumulado
                )
            )
        }


        println(
            "---------------------------------------------"
        )

        println(
            "MULTA TOTAL: S/ %.2f".format(
                prestamo.calcularMultaTotal()
            )
        )
    }


    println("========================================")
}


// --------------------------------------------------
// MOSTRAR HISTORIAL
// --------------------------------------------------

fun mostrarHistorial(
    prestamos: List<Prestamo>
) {

    println()
    println("========================================")
    println("        HISTORIAL DE PRÉSTAMOS")
    println("========================================")


    if (prestamos.isEmpty()) {

        println(
            "No existen préstamos registrados."
        )

        return
    }


    prestamos.forEachIndexed {
            indice,
            prestamo ->

        println()
        println(
            "PRÉSTAMO N.º ${indice + 1}"
        )

        mostrarPrestamo(prestamo)
    }
}


// --------------------------------------------------
// FUNCIÓN PRINCIPAL
// --------------------------------------------------

fun main() {

    val prestamos =
        mutableListOf<Prestamo>()


    var continuar = true


    while (continuar) {

        println()
        println("========================================")
        println("     SISTEMA DE PRÉSTAMO DE LIBROS")
        println("========================================")
        println("1. Registrar préstamo")
        println("2. Mostrar historial")
        println("3. Salir")
        println("----------------------------------------")

        print("Seleccione una opción: ")


        when (readln().trim()) {

            "1" -> {

                val prestamo =
                    registrarPrestamo()

                prestamos.add(
                    prestamo
                )

                mostrarPrestamo(
                    prestamo
                )
            }


            "2" -> {

                mostrarHistorial(
                    prestamos
                )
            }


            "3" -> {

                continuar = false

                println()
                println(
                    "Programa finalizado."
                )
            }


            else -> {

                println()
                println(
                    "Opción inválida. Intente nuevamente."
                )
            }
        }
    }
}