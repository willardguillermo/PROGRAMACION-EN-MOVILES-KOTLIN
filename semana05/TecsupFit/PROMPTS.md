# PROMPTS.md — Mejora con IA (Fase 2)

**Proyecto:** TECSUP Fit (Opción B)
**Rama:** `mejora-ia-tecsupfit` (creada a partir de `main`)
**Mejoras implementadas:**
1. Cupos calculados en tiempo real: al reservar se descuenta un cupo y al cancelar se devuelve.
2. Cancelar una reserva con un AlertDialog de confirmación; la cancelación rompe la racha del Perfil.

**Herramientas usadas:**
- **Gemini:** para generar un prompt bien estructurado.
- **Claude:** para generar el código de la mejora. También me ayudó a redactar la descripción del proyecto que le di a Gemini.

---

## Prompt 1 — A Gemini: generar el prompt

**Qué le pedí:** le di una descripción de mi proyecto (modelos, rutas, cómo se guardan las reservas, cómo funcionan Detalle, Reservar, Reservas y Perfil, y el problema de que los cupos eran fijos) junto con la mejora que quería, y le pedí:

> Con esta descripción, genera un prompt bien estructurado (rol, contexto, tarea, restricciones y formato de respuesta) para pedirle a otra IA que implemente esta mejora en mi proyecto.

**Lo que apliqué de la Fase 2 de la Clínica Salud+:**
- Pedí **archivos completos** en lugar de fragmentos, porque en la Clínica integrar fragmentos fue confuso.
- Nombré **todas las pantallas que dependen del cambio** (Detalle, Reservar, Reservas y Perfil), porque en la Clínica la IA no actualizó el Perfil al no estar mencionado.

**Resultado:** un prompt dividido en Rol y Contexto, Tarea, Restricciones y Formato de respuesta. Es el Prompt 2.

---

## Prompt 2 — A Claude: implementar la mejora

**Qué le pedí (prompt generado por Gemini):**

```
# Rol y Contexto
Eres un experto desarrollador Android especializado en Kotlin, Jetpack Compose
y Material 3. Estás ayudando a un estudiante a modificar una aplicación Android
llamada "TECSUP Fit" (reserva de gimnasio).
La arquitectura del proyecto es intencionalmente minimalista: NO utiliza
ViewModel ni MVVM. Todo el estado se gestiona localmente con remember,
rememberSaveable, mutableStateOf y mutableStateListOf, y la navegación se
realiza con Navigation Compose.

# Tarea
Modifica el código actual para implementar las siguientes mejoras funcionales:
1. Cancelación de reservas: Agregar la capacidad de cancelar una reserva
   CONFIRMADA. Al presionar "Cancelar reserva", debe mostrarse un AlertDialog
   de confirmación. Si el usuario confirma, la reserva pasa a un nuevo estado
   CANCELADA (mostrándose con una etiqueta roja); si cancela el diálogo, no
   ocurre nada.
2. Dinámica de cupos en tiempo real: Los cupos disponibles de cada horario en
   DatosGimnasio ya no deben ser estáticos. Deben calcularse de forma dinámica
   restando las reservas con estado CONFIRMADA asociadas a esa clase y a ese
   horario. Al confirmar una reserva se debe descontar un cupo, y al
   cancelarla, el cupo debe devolverse. Este cambio debe reflejarse
   correctamente en DetalleClaseScreen y ReservarCupoScreen.
3. Actualización en el Perfil:
   - Modificar el cálculo de la racha para que una reserva con estado
     CANCELADA rompa la secuencia actual.
   - Agregar una nueva estadística en PerfilScreen que muestre el total de
     reservas canceladas.
4. Actualización de Estados y Persistencia: Actualizar el enum class
   EstadoReserva (añadiendo CANCELADA) y asegurar que el reservasSaver (o la
   lógica de guardado) soporte correctamente este nuevo estado para mantener
   la persistencia al rotar la pantalla.

# Restricciones y Reglas Técnicas
- Cero ViewModel / MVVM: Mantén toda la gestión de estado basada en
  rememberSaveable, mutableStateListOf y lambdas elevadas (estado hoisted).
- Alcance académico: Utiliza estrictamente conceptos de Jetpack Compose
  básicos/intermedios (semanas 1 a 6), sin arquitecturas complejas ni
  librerías externas adicionales.
- Persistencia en rotación: Todo debe sobrevivir al giro de pantalla
  (asegúrate de ajustar el listSaver o saver correspondiente para el estado
  CANCELADA).
- Comentarios: Incluye comentarios cortos y claros en español donde sea
  necesario.

# Formato de Respuesta
Proporciona el código completo de cada uno de los archivos modificados (por
ejemplo: Datos.kt, AppNavegacion.kt, ReservasScreen.kt, PerfilScreen.kt,
DetalleClaseScreen.kt, ReservarCupoScreen.kt, etc.), de modo que se puedan
copiar y pegar directamente sin perder fragmentos.
```

**Qué me respondió:**
- `Datos.kt`: agregó `CANCELADA` al enum y una función `cuposRestantes(clase, horario, reservas)` que calcula los cupos base menos las reservas CONFIRMADAS de esa clase y ese horario.
- `DetalleClaseScreen.kt` y `ReservarCupoScreen.kt`: ahora reciben la lista de reservas y muestran los cupos reales.
- `AppNavegacion.kt`: pasa `reservas` a Detalle y Reservar, vuelve a validar que quede cupo antes de guardar y agrega `onCancelar`, que cambia el estado con `copy(estado = CANCELADA)`.
- `ReservasScreen.kt`: botón "Cancelar reserva", `AlertDialog` con "Sí, cancelar" y "No", y etiqueta roja.
- `PerfilScreen.kt`: estadística **Canceladas** en rojo y las 4 estadísticas ordenadas en 2 filas.
- Me explicó que **el `reservasSaver` no necesitaba cambios**: guarda el estado con `estado.name` y lo recupera con `EstadoReserva.valueOf()`.
- Me explicó que **la fórmula de la racha ya funcionaba** con `CANCELADA`: el `filter` deja pasar las canceladas y el `takeWhile` se detiene en ellas. Solo actualizó el comentario.

---

## Prompt 3 — A Claude: separar los cambios

**Qué le pedí:**

> mejor separa los cambios en 2 commits

**Por qué:** la respuesta traía las dos mejoras mezcladas en 6 archivos. Quería que cada commit explicara una sola mejora: primero los cupos en tiempo real y después la cancelación.

**Resultado:** me indicó qué partes de `Datos.kt` y `AppNavegacion.kt` iban en cada commit, porque esos dos archivos tenían cambios de ambas mejoras.

---

## Prompt 4 — A Claude: pedir de nuevo los archivos completos

**Qué le pedí:**

> me vas a tener que mandar de nuevo porque me estoy confundiendo, ya hice el cambio en Datos.kt

**Por qué:** al separar los cambios, las instrucciones de "reemplaza este bloque" en `AppNavegacion.kt` se volvieron confusas. Pedí los archivos completos de cada commit por separado.

**Resultado:** me envió los archivos completos del commit 1 (cupos) y, en otro mensaje, los del commit 2 (cancelación).

---

## Qué tuve que corregir o revisar

| # | Situación | Qué hice | Commit |
|---|---|---|---|
| 1 | La respuesta mezclaba las dos mejoras en los mismos archivos. | Pedí separarlas (Prompt 3) y aplicarlas en dos pasos. | `calcula los cupos…` y `agrega cancelación…` |
| 2 | Las instrucciones por bloques para `AppNavegacion.kt` eran confusas. | Pedí los archivos completos de cada paso (Prompt 4). | — |
| 3 | La IA cambió el texto "Marcar como completada" por "Completar" sin que se lo pidiera, porque con dos botones el texto largo no entraba. | Revisé que se viera bien y lo dejé. | `agrega cancelación…` |
| 4 | El prompt pedía ajustar el `reservasSaver` y cambiar la racha, pero no hacía falta. | Lo comprobé girando la pantalla con una reserva cancelada y revisando la racha en el Perfil. | — |
| 5 | Solo las reservas CONFIRMADAS ocupan cupo, así que una reserva **completada** también libera su cupo. | Lo revisé al probar: tiene sentido porque la clase ya pasó. | — |

---

## Pruebas realizadas

- Reservar el mismo horario hasta agotar los cupos: el Detalle baja 3 → 2 → 1 → "Sin cupos" y en Reservar aparece "Lleno".
- Cancelar → **No**: la reserva no cambia.
- Cancelar → **Sí, cancelar**: la etiqueta pasa a roja **Cancelada** y el horario recupera un cupo.
- Perfil: completar 2, cancelar 1 y completar otra → **Racha 1** y **Canceladas 1**.
- Girar la pantalla en Reservas, en Perfil y con el diálogo abierto: todo se mantiene.

## Capturas de pruebas realizadas

---

## Lo que aprendí

- Aplicar lo aprendido en un prompt anterior (pedir archivos completos y nombrar todas las pantallas afectadas) evitó los errores que tuve en la Clínica.
- Cuando un prompt pide varias mejoras a la vez, conviene pedir que se entreguen **separadas**, para poder probarlas y hacer un commit por cada una.
- Un buen diseño hace que algunas mejoras "salgan solas": guardar el estado como texto en el Saver y calcular los cupos a partir de la lista de reservas evitó tener que escribir código extra.