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

## Prompt 4 — Mis pedidos
"Las pantallas Mis pedidos y Perfil solo dicen 'Próximamente'. Quiero completar la app:
agrega un botón para finalizar la compra que guarde el pedido y lo muestre en Mis pedidos."

Resultado: data class Pedido con número, fecha, productos y total con IGV. El botón
"FINALIZAR COMPRA" del carrito registra el pedido, vacía el carrito, limpia los favoritos de
esos productos y navega a Mis pedidos, donde PantallaPedidos los lista del más reciente al
más antiguo.

## Prompt 5 — Perfil
"Completa la pantalla de Perfil con los datos del usuario y un resumen de su actividad."

Resultado: PantallaPerfil con avatar de iniciales, nombre, y tarjetas con productos en el
carrito, favoritos, pedidos realizados, total gastado y el último pedido.

## Capturas de pantalla

