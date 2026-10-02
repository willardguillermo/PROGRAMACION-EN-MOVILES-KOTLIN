# PROMPTS.md — Fase 2: Mejora con IA (Mi Bodega)

- **Rama:** `mejora-ia-mibodega` (creada a partir de `main`)
- **Asistente de IA usado:** Claude (Anthropic)
- **Mejora obligatoria:** el buscador de Inicio filtra los productos en tiempo real
  y se combina con el filtro de categoría (no lo reemplaza).

---

## Prompt 1 — Buscador en tiempo real

> (pega aquí el prompt exacto que enviaste)

**Qué se aplicó:**

- Filtro con las dos condiciones unidas con `&&` (categoría Y texto), así no se reemplazan.
- `remember(productos, categoriaSeleccionada, textoBusqueda) { ... }` para recalcular
  la lista solo cuando cambia alguna de esas llaves (cada letra escrita).
- `textoBusqueda` pasó de `remember` a `rememberSaveable` para no perder lo escrito
  al ir a Detalle y volver.

**Qué tuve que corregir / revisar:**
- Verifiqué que con el texto vacío se muestren todos los productos de la categoría.
- Probé que "coca" + Bebidas deje solo Coca-Cola, y que al cambiar de chip el texto se mantenga.
- (agrega aquí lo que hayas notado tú)