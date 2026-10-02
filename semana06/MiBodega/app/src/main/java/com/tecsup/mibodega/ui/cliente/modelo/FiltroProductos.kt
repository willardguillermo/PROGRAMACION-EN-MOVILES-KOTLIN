package com.tecsup.mibodega.ui.cliente.modelo

import java.text.Normalizer

/**
 * Quita tildes, espacios y pasa a minúsculas: "Azúcar" -> "azucar", "1 L" -> "1l".
 * Así el cliente encuentra el producto aunque escriba sin tildes o sin espacios.
 */
fun String.normalizarBusqueda(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "") // \p{Mn} = marcas diacríticas (tildes)
        .replace(Regex("\\s+"), "")     // quita espacios: "1l" encuentra "1 L"
        .lowercase()

/**
 * Filtro de Inicio: categoría Y texto de búsqueda, aplicados JUNTOS.
 * Es una función normal (no @Composable) para poder probarla con JUnit.
 *
 * - categoria "Todos" = no filtra por categoría.
 * - texto vacío = no filtra por texto.
 * - el texto se busca en nombre y descripción, sin importar tildes, espacios ni mayúsculas.
 */
fun filtrarProductos(
    productos: List<Producto>,
    categoria: String,
    texto: String
): List<Producto> {
    val busqueda = texto.normalizarBusqueda()
    return productos.filter { producto ->
        val coincideCategoria = categoria == "Todos" || producto.categoria == categoria
        val coincideTexto = busqueda.isEmpty() ||
                producto.nombre.normalizarBusqueda().contains(busqueda) ||
                producto.descripcion.normalizarBusqueda().contains(busqueda)
        coincideCategoria && coincideTexto
    }
}