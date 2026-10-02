# PROMPTS.md — Fase 2: Mejora con IA (Mi Bodega)

- **Rama:** `mejora-ia-mibodega` (creada a partir de `main`)
- **Asistente de IA usado:** Claude (Anthropic)
- **Mejora obligatoria:** el buscador de la Pantalla 3 (Inicio) filtra los productos en tiempo real
  y se combina con el filtro de categoría del LazyRow (ambos funcionan juntos, no se reemplazan).

---

## Prompt 1 — Buscador en tiempo real (commit "IA 1")

> Estoy desarrollando la app "Mi Bodega" en Kotlin con Jetpack Compose. En la Pantalla 3 (InicioScreen.kt) tengo un `OutlinedTextField` de búsqueda guardado en `var textoBusqueda`, y un `LazyRow` de chips ("Todos", "Bebidas", "Abarrotes", "Snacks") que filtra los productos según `categoriaSeleccionada`. Ahora mismo la lista (`productosFiltrados`) solo se filtra por categoría y el campo de búsqueda no hace nada.
>
> Necesito que el campo de búsqueda filtre la lista de productos en tiempo real a medida que el usuario escribe, combinándose correctamente con el filtro de categoría ya existente: ambos filtros deben funcionar juntos, no reemplazarse. Por ejemplo, si elijo "Bebidas" y escribo "coca", solo debe aparecer Coca-Cola; si borro el texto, deben volver todas las bebidas. No quiero un botón de "buscar". Además, el texto escrito no debería perderse si entro a Detalle y regreso. Dame el código a cambiar en InicioScreen.kt y explícame por qué funciona.

**Qué se aplicó:**
- Filtro con las dos condiciones unidas con `&&` (categoría Y texto), así no se reemplazan.
- `remember(productos, categoriaSeleccionada, textoBusqueda) { ... }` para recalcular la lista
  solo cuando cambia alguna de esas llaves (cada letra escrita).
- `textoBusqueda` pasó de `remember` a `rememberSaveable` para no perder lo escrito al ir a Detalle y volver.

**Qué tuve que corregir / revisar:**
- Verifiqué que con el texto vacío se muestren todos los productos de la categoría.
- Probé que "coca" + Bebidas deje solo Coca-Cola, y que al cambiar de chip el texto se mantenga.

---

## Prompt 2 — Búsqueda sin tildes, en nombre y descripción + pruebas (commit "IA 2")

> El buscador en tiempo real de InicioScreen ya funciona junto con el filtro de categoría, pero tiene problemas de uso real: si escribo "azucar" (sin tilde) no encuentra "Azúcar Rubia", y si escribo "1 L" no encuentra productos cuya presentación está en la descripción (por ejemplo "Leche evaporada entera 1 L"). Haz que la búsqueda ignore tildes y mayúsculas, y que busque tanto en el nombre como en la descripción del producto.
>
> Además, saca la lógica del filtro (categoría + texto) a una función normal de Kotlin, no @Composable, en un archivo aparte dentro de `modelo`, para que InicioScreen solo la llame y para poder probarla con JUnit. Dame también pruebas unitarias que demuestren que: sin texto se muestran todos, la búsqueda ignora tildes y mayúsculas, la categoría y el texto se combinan (no se reemplazan), y un texto inexistente devuelve una lista vacía.

**Qué se aplicó:**
- Nuevo archivo `modelo/FiltroProductos.kt` con `String.normalizarBusqueda()` (usa `java.text.Normalizer`
  para quitar tildes) y `filtrarProductos(productos, categoria, texto)`.
- Nuevo `FiltroProductosTest.kt` con 5 pruebas unitarias: las 5 pasaron.

**Qué tuve que corregir / revisar:**
- Las 5 pruebas pasaron, pero en el emulador "azucar" seguía sin encontrar "Azúcar Rubia": InicioScreen
  todavía usaba el filtro del Prompt 1 y no llamaba a `filtrarProductos()`. Las pruebas validaban la función,
  no la pantalla. Lo corregí conectando InicioScreen a `filtrarProductos()` y reinstalando la app (Stop + Run).

---

## Prompt 3 — Experiencia de usuario del buscador (commit "IA 3")

> El buscador de Inicio ya filtra en tiempo real, ignora tildes y se combina con la categoría. Ahora quiero pulir la experiencia de usuario: (1) un botón "X" dentro del campo de búsqueda para borrar el texto, que solo aparezca cuando hay algo escrito; (2) que la tecla "buscar" del teclado del celular cierre el teclado, ya que el filtro es en tiempo real; (3) cuando la combinación de búsqueda + categoría no tenga productos, mostrar un mensaje como *No encontramos "pizza" en Bebidas* en vez de dejar la pantalla en blanco; (4) que el LazyVerticalGrid use `key` con el id del producto. Dame el InicioScreen.kt completo actualizado.

**Qué se aplicó:**
- InicioScreen ahora usa `filtrarProductos(...)` dentro del `remember`.
- `trailingIcon` con `Icons.Default.Close` que limpia `textoBusqueda` (solo visible si hay texto).
- `KeyboardOptions(imeAction = ImeAction.Search)` + `KeyboardActions(onSearch = { focusManager.clearFocus() })`.
- Composable privado `SinResultados(...)`: muestra p. ej. *No encontramos "pizza" en Bebidas.*
- `key = { it.id }` en el `LazyVerticalGrid`.

**Qué tuve que corregir / revisar:**
- Al probar escribí "1l" (sin espacio) con Bebidas y salía "No encontramos", porque la descripción dice "1 L"
  (con espacio): la búsqueda respetaba los espacios. Ajusté `normalizarBusqueda()` para quitar todos los
  espacios (`Regex("\\s+")`), así "1l" y "1 L" coinciden.
- Probé en el emulador: escribir, cambiar de chip, borrar con la X y volver desde Detalle.

---

## Resumen de commits en `mejora-ia-mibodega`

1. `IA 1: buscador en tiempo real combinado con el filtro de categoría`
2. `IA 2: búsqueda sin tildes en nombre y descripción + pruebas unitarias`
3. `IA 3: Inicio usa filtrarProductos + botón limpiar, tecla buscar, mensaje sin resultados y PROMPTS.md`