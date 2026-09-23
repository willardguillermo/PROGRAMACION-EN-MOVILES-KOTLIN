package com.willard.semana05_navegacion.navigation

// Clase sellada que actúa como contrato central de navegación del portal académico.
// Cada object es una ruta única; Detail lleva un argumento tipado (el id del alumno).
sealed class Screen(val route: String) {

    // Desarrollado por: Guillermo Willard

    // Pantalla de acceso — punto de entrada de la app
    object Login : Screen(route = "login")

    // Pantalla de bienvenida con las opciones del portal
    object Home : Screen(route = "home")

    // Directorio de alumnos (lista)
    object List : Screen(route = "list")

    // Configuración de perfil del usuario
    object Profile : Screen(route = "profile")

    // Expediente académico — ruta con argumento {itemId} (id del alumno)
    object Detail : Screen(route = "detail/{itemId}") {
        // createRoute(3) -> "detail/3"
        fun createRoute(itemId: Int): String = "detail/$itemId"
    }
}