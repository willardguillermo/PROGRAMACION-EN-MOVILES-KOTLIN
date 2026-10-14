package com.tecsup.ayudacampus.data.local

import com.tecsup.ayudacampus.data.local.entity.Categoria
import com.tecsup.ayudacampus.data.local.entity.Comentario
import com.tecsup.ayudacampus.data.local.entity.Ticket
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.model.EstadoTicket
import com.tecsup.ayudacampus.model.Prioridad
import com.tecsup.ayudacampus.model.Rol

object DatosIniciales {

    private const val HORA = 3600 * 1000L

    suspend fun sembrar(db: AyudaDatabase) {
        if (db.usuarioDao().contar() > 0) return

        val ahora = System.currentTimeMillis()

        db.categoriaDao().insertarTodas(
            listOf(
                Categoria(id = 1, nombre = "Hardware", icono = "pc"),
                Categoria(id = 2, nombre = "Software", icono = "code"),
                Categoria(id = 3, nombre = "Red / Wi-Fi", icono = "wifi"),
                Categoria(id = 4, nombre = "Cuenta", icono = "key"),
                Categoria(id = 5, nombre = "Impresión", icono = "printer")
            )
        )

        val estudiante = Usuario(
            id = 1,
            nombre = "Juan Pérez",
            correo = "juan@campus.test",
            clave = "1234",
            rol = Rol.CLIENTE.name
        )
        val soporte = Usuario(
            id = 2,
            nombre = "Soporte TI",
            correo = "soporte@campus.test",
            clave = "admin",
            rol = Rol.ADMIN.name
        )
        db.usuarioDao().insertarTodos(listOf(estudiante, soporte))

        val dao = db.ticketDao()

        dao.insertarTicket(
            Ticket(
                clienteId = 1, categoriaId = 3,
                titulo = "Wi-Fi no conecta en el pabellón B",
                descripcion = "Desde la mañana la red Tecsup-Alumnos no da acceso a internet en el pabellón B.",
                ubicacion = "Pabellón B, aula 305",
                prioridad = Prioridad.ALTA.name,
                estado = EstadoTicket.ABIERTO.name,
                fecha = ahora - 3 * HORA
            )
        )

        val t2 = dao.insertarTicket(
            Ticket(
                clienteId = 1, categoriaId = 1,
                titulo = "Laptop del laboratorio no enciende",
                descripcion = "La laptop número 12 del laboratorio de redes no enciende aunque está conectada.",
                ubicacion = "Laboratorio de redes",
                prioridad = Prioridad.ALTA.name,
                estado = EstadoTicket.EN_PROCESO.name,
                fecha = ahora - 5 * HORA
            )
        ).toInt()

        dao.insertarTicket(
            Ticket(
                clienteId = 1, categoriaId = 2,
                titulo = "No puedo instalar Android Studio",
                descripcion = "Al instalar Android Studio en la PC del aula sale un error de permisos de administrador.",
                ubicacion = "Aula 210",
                prioridad = Prioridad.MEDIA.name,
                estado = EstadoTicket.ABIERTO.name,
                fecha = ahora - HORA / 2
            )
        )

        val t4 = dao.insertarTicket(
            Ticket(
                clienteId = 1, categoriaId = 4,
                titulo = "Cuenta del campus virtual bloqueada",
                descripcion = "Intenté ingresar varias veces y ahora mi cuenta del campus virtual aparece bloqueada.",
                ubicacion = "Remoto",
                prioridad = Prioridad.MEDIA.name,
                estado = EstadoTicket.RESUELTO.name,
                fecha = ahora - 26 * HORA
            )
        ).toInt()

        val t5 = dao.insertarTicket(
            Ticket(
                clienteId = 1, categoriaId = 5,
                titulo = "Impresora de biblioteca atascada",
                descripcion = "La impresora del segundo piso de la biblioteca tiene papel atascado y no imprime.",
                ubicacion = "Biblioteca, 2do piso",
                prioridad = Prioridad.BAJA.name,
                estado = EstadoTicket.CERRADO.name,
                fecha = ahora - 72 * HORA,
                fechaCierre = ahora - 48 * HORA,
                calificacion = 5
            )
        ).toInt()

        dao.insertarTicket(
            Ticket(
                clienteId = 1, categoriaId = 1,
                titulo = "Proyector del aula 204 parpadea",
                descripcion = "El proyector se apaga y prende cada pocos minutos durante la clase.",
                ubicacion = "Aula 204",
                prioridad = Prioridad.BAJA.name,
                estado = EstadoTicket.ABIERTO.name,
                fecha = ahora - 2 * HORA
            )
        )

        dao.insertarComentario(
            Comentario(
                ticketId = t2, autorId = 1,
                autorNombre = estudiante.nombre, autorRol = estudiante.rol,
                texto = "La necesito para mi laboratorio de la tarde.",
                fecha = ahora - 4 * HORA
            )
        )
        dao.insertarComentario(
            Comentario(
                ticketId = t2, autorId = 2,
                autorNombre = soporte.nombre, autorRol = soporte.rol,
                texto = "Ya estamos revisando la fuente de poder, te avisamos en breve.",
                fecha = ahora - 3 * HORA
            )
        )
        dao.insertarComentario(
            Comentario(
                ticketId = t4, autorId = 2,
                autorNombre = soporte.nombre, autorRol = soporte.rol,
                texto = "Desbloqueamos tu cuenta. Intenta ingresar de nuevo y confírmanos.",
                fecha = ahora - 24 * HORA
            )
        )
        dao.insertarComentario(
            Comentario(
                ticketId = t5, autorId = 2,
                autorNombre = soporte.nombre, autorRol = soporte.rol,
                texto = "Retiramos el papel atascado, la impresora ya funciona.",
                fecha = ahora - 50 * HORA
            )
        )
    }
}