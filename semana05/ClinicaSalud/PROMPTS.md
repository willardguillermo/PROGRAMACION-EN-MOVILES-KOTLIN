# PROMPTS.md — Mejora con IA (Fase 2)

**Proyecto:** Clínica Salud+ (Opción A)
**Rama:** `mejora-ia-clinica` (creada a partir de `main`)
**Mejora implementada:** cancelar una cita confirmada con un AlertDialog de confirmación.
**Herramientas usadas:**
- **Gemini:** para generar un prompt bien estructurado.
- **Claude:** para generar el código de la mejora. También me ayudó a redactar la descripción del proyecto que le di a Gemini.

---

## Prompt 1 — A Gemini: generar el prompt

**Qué le pedí:** le di una descripción de mi proyecto (estructura de archivos, modelos, navegación, cómo funciona Mis citas y la mejora que quería) y le pedí:

> Con esta descripción, genera un prompt bien estructurado (rol, contexto, tarea, restricciones y formato de respuesta) para pedirle a otra IA que implemente esta mejora en mi proyecto.

**Resultado:** un prompt dividido en Rol, Contexto, Tarea, Restricciones y Formato de respuesta. Es el Prompt 2.

---

## Prompt 2 — A Claude: implementar la mejora

**Qué le pedí (prompt generado por Gemini):**

```
Rol:
Eres un experto desarrollador Android especializado en Kotlin, Jetpack Compose y
Material 3, con amplia experiencia en el manejo de estado local (remember,
rememberSaveable, mutableStateListOf), navegación y persistencia en memoria
mediante listSaver.

Contexto:
Estoy desarrollando una aplicación Android llamada "Clínica Salud+" utilizando
únicamente Jetpack Compose y Material 3. No utilizamos ViewModel ni
arquitecturas como MVVM. Todo el estado de la aplicación se gestiona de forma
local con remember, rememberSaveable y mutableStateOf / mutableStateListOf.
La navegación se realiza con Navigation Compose (usando rutas con argumentos y
un NavHost dentro de un ModalNavigationDrawer). La lista de citas se encuentra
en AppNavegacion y se preserva ante cambios de configuración (como giros de
pantalla) gracias a un citasSaver personalizado (listSaver). Las pantallas
reciben las listas y funciones lambda para comunicarse.
Actualmente, las citas tienen un enum class EstadoCita { CONFIRMADA, COMPLETADA }.
En MisCitasScreen, las citas confirmadas muestran un botón para marcarlas como
completadas.

Tarea:
Implementar la funcionalidad para cancelar una cita confirmada, cumpliendo con
los siguientes requerimientos técnicos:
1. Actualizar el Modelo (data/Datos.kt):
   - Agregar el estado CANCELADA al enum class EstadoCita.
   - Asegurar que el citasSaver en AppNavegacion soporte y guarde correctamente
     este nuevo estado al transformar los datos.
2. Modificar la Lógica en AppNavegacion (navigation/AppNavegacion.kt):
   - Crear una función lambda o lógica de cancelación (por ejemplo,
     onCancelar: (Int) -> Unit) que busque la cita por su id, cambie su estado
     a EstadoCita.CANCELADA usando .copy() y actualice la lista reactiva.
3. Actualizar la Interfaz en MisCitasScreen (ui/screens/MisCitasScreen.kt):
   - En las tarjetas con estado CONFIRMADA, además del botón de completar,
     agregar un botón (o TextButton) para "Cancelar cita".
   - Al presionar este botón, debe mostrarse un AlertDialog de Material 3
     pidiendo confirmación ("¿Estás seguro de que deseas cancelar esta cita?"
     con opciones "Sí, cancelar" y "No").
   - Si el usuario confirma, se invoca la función de cancelación. Si cancela el
     diálogo, no ocurre nada.
   - Diseñar la etiqueta visual para el estado CANCELADA (por ejemplo, texto
     rojo con fondo rojo claro).

Restricciones:
- Cero ViewModels o librerías externas de arquitectura.
- Las citas canceladas no deben perderse al girar la pantalla.
- Agregar comentarios cortos en español explicando los puntos clave.

Formato de respuesta:
Proporciona únicamente los fragmentos de código modificados o nuevos necesarios
en cada archivo (data/Datos.kt, navigation/AppNavegacion.kt y
ui/screens/MisCitasScreen.kt), indicando claramente a qué archivo corresponde
cada bloque.
```

**Qué me respondió:**
- `Datos.kt`: agregó `CANCELADA` al enum.
- `AppNavegacion.kt`: agregó `onCancelar`, que busca la cita con `indexOfFirst` y la reemplaza con `copy(estado = EstadoCita.CANCELADA)`.
- `MisCitasScreen.kt`: botón "Cancelar cita", `AlertDialog` con "Sí, cancelar" y "No", y etiqueta roja para las canceladas. Usó `when` para elegir el texto y los colores según el estado.
- Me explicó que el `citasSaver` **no necesitaba cambios**: guarda el estado con `estado.name` y lo recupera con `EstadoCita.valueOf()`, así que `CANCELADA` funciona sin tocarlo.

---

## Prompt 3 — A Claude: pedir el archivo completo

**Qué le pedí:**

> dame el código corregido de miscitasscreen

**Por qué:** como el Prompt 2 pedía solo fragmentos, integrarlos a mano en `MisCitasScreen.kt` (imports, parámetros, la llamada a `TarjetaCita` y la función completa) fue confuso. Pedí el archivo completo para reemplazarlo de una vez.

---

## Qué tuve que corregir

| # | Problema | Cómo lo resolví | Commit |
|---|---|---|---|
| 1 | Con fragmentos sueltos era fácil pegar código en el lugar equivocado. | Pedí el archivo `MisCitasScreen.kt` completo (Prompt 3). | `agrega cancelación de citas con AlertDialog` |
| 2 | La IA no modificó `PerfilPacienteScreen`, que también depende del estado de las citas. El **Total** contaba las canceladas y no existía la estadística **Canceladas**. Lo detecté al revisar la respuesta: la misma IA lo mencionó como nota aparte, fuera de lo que le pedí. | Agregué el conteo de canceladas, la tarjeta **Canceladas** en rojo y ordené las 4 estadísticas en 2 filas. | `corrige estadísticas del perfil para incluir citas canceladas` |
| 3 | El prompt pedía "asegurar" el `citasSaver`, pero no hacía falta cambiarlo. | Lo verifiqué girando la pantalla con una cita cancelada: se mantiene. | — |

---

## Pruebas realizadas

- Cancelar → **No**: la cita no cambia.
- Cancelar → **Sí, cancelar**: la etiqueta pasa a roja **Cancelada** y desaparecen los botones.
- Girar la pantalla con una cita cancelada: sigue cancelada.
- Girar con el diálogo abierto: el diálogo sigue abierto.
- **Historial médico** no muestra las canceladas, solo las completadas.
- **Perfil** muestra correctamente Total, Confirmadas, Completadas y Canceladas.

---

## Lo que aprendí

- Si el prompt solo nombra algunos archivos, la IA solo cambia esos. Hay que mencionar **todas las pantallas que usan el dato**, en este caso también el Perfil.
- Para integrar sin errores, conviene pedir **archivos completos** en lugar de fragmentos.
- Un prompt con estructura (rol, contexto, tarea, restricciones y formato) da respuestas mucho más precisas que una pregunta suelta.