package com.tecsup.mibodega

import com.tecsup.mibodega.ui.cliente.modelo.OrdenPrecio
import com.tecsup.mibodega.ui.cliente.modelo.filtrarProductos
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.modelo.ordenarPor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Pruebas del buscador de Inicio (Fase 2). Clic derecho > Run 'FiltroProductosTest'. */
class FiltroProductosTest {

    @Test
    fun sinTextoYTodos_devuelveTodosLosProductos() {
        val resultado = filtrarProductos(listaProductosFake, "Todos", "")
        assertEquals(listaProductosFake.size, resultado.size)
    }

    @Test
    fun buscarSinTilde_encuentraProductoConTilde() {
        val resultado = filtrarProductos(listaProductosFake, "Todos", "azucar")
        assertTrue(resultado.any { it.nombre == "Azúcar Rubia" })
    }

    @Test
    fun buscarEnMayusculas_ignoraMayusculas() {
        val resultado = filtrarProductos(listaProductosFake, "Todos", "COCA")
        assertTrue(resultado.any { it.nombre == "Coca-Cola Original" })
    }

    @Test
    fun categoriaYTexto_seCombinanNoSeReemplazan() {
        // "1 L" aparece en Abarrotes (aceite, leche) y en Bebidas (jugo):
        // con categoría Bebidas solo deben quedar bebidas.
        val resultado = filtrarProductos(listaProductosFake, "Bebidas", "1 L")
        assertTrue(resultado.isNotEmpty())
        assertTrue(resultado.all { it.categoria == "Bebidas" })
    }

    @Test
    fun textoQueNoExiste_devuelveListaVacia() {
        val resultado = filtrarProductos(listaProductosFake, "Todos", "pizza")
        assertTrue(resultado.isEmpty())
    }

    @Test
    fun ordenarMenorAMayor_elPrimeroEsElMasBarato() {
        val resultado = listaProductosFake.ordenarPor(OrdenPrecio.MENOR_A_MAYOR)
        assertEquals(listaProductosFake.minOf { it.precio }, resultado.first().precio, 0.001)
    }

    @Test
    fun ordenarMayorAMenor_elPrimeroEsElMasCaro() {
        val resultado = listaProductosFake.ordenarPor(OrdenPrecio.MAYOR_A_MENOR)
        assertEquals(listaProductosFake.maxOf { it.precio }, resultado.first().precio, 0.001)
    }
}