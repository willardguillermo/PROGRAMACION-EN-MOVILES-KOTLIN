package com.willard.Lab04CarritoTecsup

sealed class Pantalla(val ruta: String) {
    object Inicio : Pantalla("inicio")
    object MisPedidos : Pantalla("mis_pedidos")
    object Favoritos : Pantalla("favoritos")
    object Perfil : Pantalla("perfil")
}