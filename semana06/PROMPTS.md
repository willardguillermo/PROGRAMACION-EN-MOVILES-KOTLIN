# PROMPTS.md — Lab 06, Fase 2 (rama `mejora-ia-lab06`)

Asistente usado: Claude

## Prompt 1 — Favoritos compartidos entre el menú y el drawer
"Tengo un carrito en Jetpack Compose. Cada TarjetaProducto tiene un DropdownMenu con la
opción Favoritos y la app tiene un ModalNavigationDrawer en AppNavegacion.kt. ¿Cómo hago
para que el drawer sepa cuántos productos marqué como favoritos?"

Resultado: la lista de favoritos (ids) se creó en AppNavegacion con mutableStateListOf y se
pasó hacia abajo a PantallaCarrito y TarjetaProducto, con un callback onToggleFavorito.
Se agregó un id a Producto para distinguir productos con los mismos datos, y al eliminar un
producto del carrito también se quita de favoritos.

## Prompt 2 — Badge en el drawer
"Agrega un Badge con el contador de favoritos al ítem 'Favoritos' del drawer y que no
aparezca cuando es 0."

Resultado: se usó el parámetro badge de NavigationDrawerItem y un BadgedBox en el ícono ☰
que muestra un punto cuando hay al menos un favorito.

## Prompt 3 — Pantalla de favoritos
"Crea una pantalla que liste los productos marcados como favoritos y permita quitarlos."

Resultado: PantallaFavoritos con LazyColumn, conectada a la ruta Favoritos del NavHost.

## Capturas de pantalla

