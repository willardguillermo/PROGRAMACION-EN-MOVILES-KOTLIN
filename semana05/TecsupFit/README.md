# TECSUP Fit — Tarea integradora Semanas 1 a 6

App Android de **reserva de clases de gimnasio** hecha con **Kotlin** y **Jetpack Compose**.
Integra lo trabajado en las semanas 1 a 6: layouts y controles, `LazyColumn` / `LazyRow`, navegación secuencial con paso de parámetros y navegación secundaria con barra inferior (bottomBar).

| | |
|---|---|
| **Curso** | Programación en Móviles — Tecsup |
| **Docente** | Juan León S. |
| **Alumno** | Guillermo Willard |
| **Opción** | B — TECSUP Fit |
| **Arquitectura** | Sin ViewModel ni MVVM: el estado se maneja con `remember`, `rememberSaveable` y `mutableStateOf` / `mutableStateListOf` |

## Ramas

| Rama | Contenido |
|---|---|
| `main` | **Fase 1:** app completa desarrollada sin IA |
| `mejora-ia-tecsupfit` | **Fase 2:** mejora con IA + `PROMPTS.md` |

---

## Tecnologías

- Kotlin + Jetpack Compose + Material 3
- Navigation Compose (`NavHost`, rutas con argumentos `Int`)
- `NavigationBar` en el `bottomBar` del `Scaffold`
- Material Icons Extended

## Estructura del proyecto

```
app/src/main/java/com/willard/tecsupfit/
├── MainActivity.kt              → solo llama a AppNavegacion()
├── data/
│   └── Datos.kt                 → Clase, Horario, Reserva, Rutina y datos de prueba
├── navigation/
│   ├── Pantalla.kt              → sealed class con todas las rutas
│   └── AppNavegacion.kt         → NavHost, barra inferior y lista de reservas
└── ui/
    ├── components/
    │   └── BarraInferior.kt     → NavigationBar con las 4 pestañas
    ├── screens/
    │   ├── InicioScreen.kt
    │   ├── DetalleClaseScreen.kt
    │   ├── ReservarCupoScreen.kt
    │   ├── ConfirmacionScreen.kt
    │   ├── ReservasScreen.kt
    │   ├── RutinasScreen.kt
    │   └── PerfilScreen.kt
    └── theme/                   → paleta verde
```

## Flujo de navegación

**Navegación secuencial** (con paso de parámetros):

```
Inicio ──(claseId)──► Detalle de clase ──(claseId)──► Reservar cupo ──(claseId, horario)──► Confirmación
```

**Navegación secundaria** (barra inferior, visible en las 4 secciones):

```
Inicio · Reservas · Rutinas · Perfil
```

---

## Requerimientos funcionales

| Código | Requerimiento | Pantalla |
|---|---|---|
| RF-01 | Mostrar la lista de clases con nombre, horarios, día y sala. | Inicio |
| RF-02 | Filtrar las clases con los chips **Hoy** y **Esta semana**. | Inicio |
| RF-03 | Ver el detalle de una clase (instructor, duración, descripción y cupos por horario) recibiendo su id por parámetro de navegación. | Detalle de clase |
| RF-04 | Desactivar el botón Reservar cupo si ningún horario tiene cupos. | Detalle de clase |
| RF-05 | Elegir **un solo** horario; los horarios sin cupos aparecen como "Lleno" y no se pueden elegir. El botón Confirmar solo se activa con un horario elegido. | Reservar cupo |
| RF-06 | Mostrar el resumen de la reserva (clase, instructor, día, horario y sala) con opciones para ver reservas o volver al inicio. | Confirmación |
| RF-07 | Navegar entre secciones con una barra inferior de 4 pestañas que resalta la pestaña actual. | Barra inferior |
| RF-08 | Listar las reservas diferenciando visualmente los estados Confirmada y Completada. | Reservas |
| RF-09 | Marcar una reserva confirmada como completada. | Reservas |
| RF-10 | Mostrar rutinas sugeridas con nivel, duración y ejercicios. | Rutinas |
| RF-11 | Mostrar los datos del usuario y sus estadísticas: clases tomadas, racha y reservas activas. | Perfil |
| RF-12 | Conservar el filtro, el horario elegido y las reservas al girar la pantalla. | Todas |

---

## Capturas de ejecucion


<img width="1344" height="2992" alt="image" src="https://github.com/user-attachments/assets/c214f8be-b875-4d47-9e96-123dfb976a68" />

<img width="1344" height="2992" alt="image" src="https://github.com/user-attachments/assets/9c895eca-5a66-428e-94b2-58e24c4d763a" />

<img width="1344" height="2992" alt="image" src="https://github.com/user-attachments/assets/2dc63444-3c5a-4649-bc9b-411c2b63ffb3" />

<img width="1344" height="2992" alt="image" src="https://github.com/user-attachments/assets/30839cdf-e5e0-475f-92b0-d52b8345c768" />

<img width="1344" height="2992" alt="image" src="https://github.com/user-attachments/assets/a7dd63b2-a26b-49e1-a75d-867d9ae88ff3" />

<img width="1344" height="2992" alt="image" src="https://github.com/user-attachments/assets/f98229c5-2e8c-4974-8b40-a0e19b90e735" />

<img width="1344" height="2992" alt="image" src="https://github.com/user-attachments/assets/184a851e-9d9b-4945-8eaf-086e9dd51227" />

<img width="1344" height="2992" alt="image" src="https://github.com/user-attachments/assets/cab5efd6-5703-4250-89c4-62915d1e3393" />

<img width="1344" height="2992" alt="image" src="https://github.com/user-attachments/assets/8464de43-d6c0-45ba-83e9-ebcf3f5e5ee4" />

<img width="1344" height="2992" alt="image" src="https://github.com/user-attachments/assets/8af121c0-d75c-4a4e-b387-84fdc8e271cf" />

---

## Investigación: estructura de un prompt

Un **prompt** es la instrucción que se le da a un modelo de IA. En desarrollo de software, un prompt vago produce código genérico que no encaja con el proyecto. Un prompt estructurado produce código que respeta la arquitectura, los nombres y las restricciones del proyecto.

### Elementos de un prompt

| Elemento | Qué es | Ejemplo en desarrollo de software |
|---|---|---|
| **Rol** | Quién debe "ser" la IA al responder. | "Actúa como desarrollador Android experto en Jetpack Compose." |
| **Contexto** | Información que la IA necesita y no conoce: proyecto, tecnologías, estructura, código existente. | "La app no usa ViewModel; la lista de reservas está en AppNavegacion." |
| **Tarea** | Qué se quiere lograr, de forma clara y específica. | "Agrega la opción de cancelar una reserva confirmada." |
| **Restricciones** | Qué se debe hacer y qué evitar. | "Sin ViewModel, sin librerías externas, comentarios en español." |
| **Formato de respuesta** | Cómo se quiere recibir la respuesta. | "Dame el archivo completo de cada pantalla que cambies." |
| **Ejemplos** *(opcional)* | Muestras de lo que se espera, para que la IA siga el mismo patrón. | Una función existente del proyecto como modelo de estilo. |

### Buenas prácticas en proyectos de software

1. **Nombrar archivos, clases y funciones reales** del proyecto, para que la IA no invente nombres nuevos.
2. **Indicar las tecnologías y lo que NO se debe usar**, por ejemplo "sin ViewModel".
3. **Mencionar todo lo que depende del cambio.** Si un dato se usa en varias pantallas, hay que nombrarlas todas.
4. **Pedir archivos completos** en lugar de fragmentos cuando el cambio toca varias partes del archivo.
5. **Iterar:** revisar la respuesta, probarla y volver a preguntar con más detalle si algo falla.
6. **Verificar siempre** el código generado antes de integrarlo.

### Fuentes

- Google AI for Developers — *Prompt design strategies*: https://ai.google.dev/gemini-api/docs/prompting-strategies
- Anthropic — *Prompt engineering overview*: https://platform.claude.com/docs/en/build-with-claude/prompt-engineering/overview

---

## Preguntas de sustentación

**1. ¿Cómo llega la clase elegida en Inicio hasta la pantalla de Confirmación?**

1. En **Inicio**, la tarjeta llama a `onClaseClick(clase.id)`.
2. `AppNavegacion` arma la ruta `detalle/2` con `Pantalla.DetalleClase.crearRuta(2)`.
3. El `NavHost` recibe el `2` con `navArgument` de tipo `Int`, y **Detalle** recupera la clase con `buscarClase(2)`.
4. Detalle envía el mismo id a **Reservar** (`reservar/2`).
5. Reservar envía el id más la **posición** del horario elegido (`confirmacion/2/1`).
6. **Confirmación** recupera los datos con `buscarClase(2)` y `clase.horarios[1]`.

Se envían números y no textos porque un horario como `6:00 pm` lleva espacios y `:`, que pueden romper la ruta.

**2. ¿Cómo sabe el bottomBar cuál ícono resaltar en cada pantalla?**

1. En `AppNavegacion`, `currentBackStackEntryAsState()` observa la pantalla actual del `navController`.
2. De ahí se obtiene `rutaActual`, por ejemplo `"reservas"`.
3. Cada `NavigationBarItem` usa `selected = rutaActual == pestana.ruta`, así que solo se resalta la pestaña que coincide.
4. Como es un **estado**, al cambiar de pantalla la barra se recompone sola con la pestaña correcta.

**3. ¿Por qué la selección de horario se comporta como un RadioButton, aunque sean chips?**

Porque `horarioSeleccionado` guarda **una sola posición**. Cada chip usa `selected = horarioSeleccionado == index`. Al tocar otro, el valor se reemplaza y solo un chip cumple la condición. Además, los horarios sin cupos tienen `enabled = false`, así que no se pueden elegir.

**4. ¿Qué tuve que corregir del código que generó la IA en la Fase 2?**

*(Se completa al terminar la rama `mejora-ia-tecsupfit`. El detalle queda en `PROMPTS.md`.)*
