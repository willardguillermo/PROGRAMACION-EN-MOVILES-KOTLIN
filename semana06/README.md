# Lab 06 — Menú y Navegación (DropdownMenu + NavigationDrawer)

**Nombre:** Willard Guillermo
**Curso:** Programación en Móviles - 4to Ciclo
**Docente:** Juan José León Suiyon

## Descripción
Continuación de mi Lab 04 (Carrito TECSUP con LazyColumn) usando la navegación del Lab 05.
Se agregaron dos piezas nuevas:

- **DropdownMenu contextual** en cada tarjeta del carrito (ícono ⋮) con las opciones
  Agregar a favoritos, Compartir y Eliminar, cada una con su ícono y un separador antes
  de Eliminar.
- **NavigationDrawer** como navegación principal, abierto desde el ícono ☰ de la barra
  superior, con los destinos Inicio, Mis pedidos, Favoritos y Perfil, más Cerrar sesión.
  Tiene un encabezado con las iniciales y datos del usuario, y el destino activo se resalta
  con un color de fondo distinto.

La opción "Eliminar" reemplaza a "Reportar" porque tiene más sentido dentro de un carrito.

## Ramas

| Rama | Contenido |
|---|---|
| `main` | **Fase 1:** DropdownMenu y NavigationDrawer desarrollados sin IA |
| `mejora-ia-lab06` | **Fase 2:** badge con contador de favoritos en el drawer, hecho con IA + `PROMPTS.md` |

La rama de la Fase 2 se llama `mejora-ia-lab06` porque en el repositorio ya existía una rama
`mejora-ia` de un laboratorio anterior.

## Mejora con IA (Fase 2)
Al marcar un producto como favorito desde su DropdownMenu, el ítem "Favoritos" del drawer
muestra un badge con la cantidad, y el ícono ☰ muestra un punto. La opción del menú cambia a
"Quitar de favoritos" y aparece un corazón junto al nombre del producto. La sección Favoritos
lista los productos marcados y permite quitarlos.

## Estructura de archivos

| Archivo | Qué hace |
|---|---|
| `MainActivity.kt` | Carga el tema y llama a `AppNavegacion()` |
| `AppNavegacion.kt` | `ModalNavigationDrawer` que envuelve al `Scaffold` y al `NavHost`; guarda las listas de productos y favoritos |
| `AppDrawer.kt` | Contenido del drawer (`ModalDrawerSheet`): encabezado, opciones, badge y cerrar sesión |
| `Pantalla.kt` | Sealed class con las rutas de la app |
| `PantallaCarrito.kt` | Formulario, lista de productos y totales con IGV |
| `TarjetaProducto.kt` | Tarjeta de cada producto con su DropdownMenu |
| `PantallaFavoritos.kt` | Lista de productos marcados como favoritos (Fase 2) |
| `PantallaSimple.kt` | Pantalla genérica para Mis pedidos y Perfil |

## Capturas


## Preguntas de reflexión

**1. ¿Por qué el DropdownMenu se declara dentro de un Box junto al ícono que lo activa?**
Porque el DropdownMenu se posiciona respecto a su contenedor. Al estar en el mismo Box que
el ícono ⋮, el menú se abre pegado al producto que se tocó. Si se declarara en otra parte de
la pantalla, aparecería en una posición sin relación con ese producto.

**2. ¿Qué diferencia de alcance hay entre las opciones del DropdownMenu y las del drawer?**
Las opciones del DropdownMenu actúan solo sobre un producto, y su estado `expanded` es local
a cada tarjeta. Las del drawer afectan a toda la app porque cambian de pantalla, por eso el
drawer vive en AppNavegacion y envuelve al Scaffold y al NavHost.

**3. ¿Cómo se estructuró el código para que el contador de favoritos se entere de lo que pasa en el DropdownMenu?**
Elevando el estado. La lista de favoritos vive en AppNavegacion, que es el ancestro común de
las tarjetas y del drawer. Hacia abajo bajan los datos (`esFavorito`, `cantidadFavoritos`) y
hacia arriba suben los eventos (`onToggleFavorito`). Como la lista es `mutableStateListOf`,
Compose recompone el badge automáticamente cuando cambia.

**4. ¿Qué tuviste que corregir del código que generó la IA?**
No tuve que corregir errores: el código funcionó desde el primer intento. Lo que sí hice fue
revisar cada cambio en el emulador antes de hacer el commit.