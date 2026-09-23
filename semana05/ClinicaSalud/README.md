# Clínica Salud+ — Tarea integradora Semanas 1 a 6

App Android de **reserva de citas médicas** hecha con **Kotlin** y **Jetpack Compose**.
Integra lo trabajado en las semanas 1 a 6: layouts y controles, `LazyColumn` / `LazyRow`, navegación secuencial con paso de parámetros y navegación secundaria con menú lateral (drawer).

| | |
|---|---|
| **Curso** | Programación en Móviles — Tecsup |
| **Docente** | Juan León S. |
| **Alumno** | Guillermo Willard |
| **Opción elegida** | A — Clínica Salud+ |
| **Arquitectura** | Sin ViewModel ni MVVM: el estado se maneja con `remember`, `rememberSaveable` y `mutableStateOf` / `mutableStateListOf` |

## Ramas

| Rama | Contenido |
|---|---|
| `main` | **Fase 1:** app completa desarrollada sin IA |
| `mejora-ia-clinica` | **Fase 2:** mejora con IA (cancelar cita con AlertDialog) + `PROMPTS.md` |

La rama de la Fase 2 se llama `mejora-ia-clinica` porque en el repositorio ya existía una rama `mejora-ia` de un laboratorio anterior.

---

## Tecnologías

- Kotlin + Jetpack Compose + Material 3
- Navigation Compose (`NavHost`, rutas con argumentos `Int`)
- `ModalNavigationDrawer` para el menú lateral
- Material Icons Extended

## Estructura del proyecto

```
app/src/main/java/com/willard/clinicasalud/
├── MainActivity.kt              → solo llama a AppNavegacion()
├── data/
│   └── Datos.kt                 → Medico, Cita, EstadoCita y datos de prueba
├── navigation/
│   ├── Pantalla.kt              → sealed class con todas las rutas
│   └── AppNavegacion.kt         → NavHost, menú lateral y lista de citas
└── ui/
    ├── components/
    │   └── MenuLateral.kt       → contenido del drawer
    └── screens/
        ├── InicioScreen.kt
        ├── PerfilMedicoScreen.kt
        ├── AgendarCitaScreen.kt
        ├── ConfirmacionScreen.kt
        ├── MisCitasScreen.kt
        ├── HistorialScreen.kt
        └── PerfilPacienteScreen.kt
```

## Flujo de navegación

**Navegación secuencial** (con paso de parámetros):

```
Inicio ──(medicoId)──► Perfil del médico ──(medicoId)──► Agendar cita ──(medicoId, fecha, hora)──► Confirmación
```

**Navegación secundaria** (menú lateral ☰):

```
Inicio · Mis citas · Historial médico · Perfil
```

---

## Requerimientos funcionales

| Código | Requerimiento | Pantalla |
|---|---|---|
| RF-01 | Mostrar la lista de médicos con nombre, especialidad y calificación. | Inicio |
| RF-02 | Filtrar los médicos por especialidad mediante una fila de chips (6 opciones). | Inicio |
| RF-03 | Ver el perfil de un médico recibiendo su id por parámetro de navegación. | Perfil del médico |
| RF-04 | Elegir **una** fecha (3 opciones) y **una** hora (3 opciones); el botón Confirmar solo se activa cuando ambas están elegidas. | Agendar cita |
| RF-05 | Mostrar el resumen de la cita agendada (médico, especialidad, fecha y hora) con opciones para volver al inicio o ver mis citas. | Confirmación |
| RF-06 | Navegar entre secciones con un menú lateral de 4 destinos que resalta la sección actual. | Menú lateral |
| RF-07 | Listar las citas agendadas diferenciando visualmente los estados Confirmada y Completada. | Mis citas |
| RF-08 | Marcar una cita confirmada como completada. | Mis citas |
| RF-09 | Mostrar el historial con las consultas completadas y su total. | Historial médico |
| RF-10 | Mostrar los datos del paciente y estadísticas de sus citas. | Perfil |
| RF-11 | Conservar el filtro, la selección de fecha y hora y las citas al girar la pantalla. | Todas |
| RF-12 | *(Rama `mejora-ia-clinica`)* Cancelar una cita confirmada previa confirmación en un AlertDialog. | Mis citas |

---

## Capturas de ejecucion:


<img width="435" height="902" alt="image" src="https://github.com/user-attachments/assets/bfaedd50-fd16-48b8-9f94-51b249b1f3b8" />


<img width="423" height="875" alt="image" src="https://github.com/user-attachments/assets/9587a928-aaa5-4678-a584-906f0bcb0322" />


<img width="433" height="901" alt="image" src="https://github.com/user-attachments/assets/fc7eee2a-8198-4f76-8c44-40a9cad3f6f3" />


<img width="453" height="903" alt="image" src="https://github.com/user-attachments/assets/5002d487-0415-4a05-a1ea-667f161526c7" />


<img width="418" height="887" alt="image" src="https://github.com/user-attachments/assets/63fd34e1-e5fb-4fbd-98ac-ea33fa618392" />


<img width="453" height="903" alt="image" src="https://github.com/user-attachments/assets/4fd43740-3db7-42ca-bd41-0ead872345e4" />


---

## Investigación: estructura de un prompt

Un **prompt** es la instrucción que se le da a un modelo de IA. En desarrollo de software, un prompt vago ("hazme un botón de cancelar") produce código genérico que no encaja con el proyecto. Un prompt estructurado produce código que respeta la arquitectura, los nombres y las restricciones del proyecto.

### Elementos de un prompt

| Elemento | Qué es | Ejemplo en desarrollo de software |
|---|---|---|
| **Rol** | Quién debe "ser" la IA al responder. | "Actúa como desarrollador Android experto en Jetpack Compose." |
| **Contexto** | Información que la IA necesita y no conoce: proyecto, tecnologías, estructura, código existente. | "La app no usa ViewModel; la lista de citas está en AppNavegacion." |
| **Tarea** | Qué se quiere lograr, de forma clara y específica. | "Agrega la opción de cancelar una cita confirmada con un AlertDialog." |
| **Restricciones** | Qué se debe hacer y qué evitar. | "Sin ViewModel, sin librerías externas, comentarios en español." |
| **Formato de respuesta** | Cómo se quiere recibir la respuesta. | "Dame el archivo completo de cada pantalla que cambies." |
| **Ejemplos** *(opcional)* | Muestras de lo que se espera, para que la IA siga el mismo patrón. | Una función existente del proyecto como modelo de estilo. |

### Buenas prácticas en proyectos de software

1. **Nombrar archivos, clases y funciones reales** del proyecto, para que la IA no invente nombres nuevos.
2. **Indicar las tecnologías y lo que NO se debe usar**, por ejemplo "sin ViewModel".
3. **Mencionar todo lo que depende del cambio.** Si un dato se usa en varias pantallas, hay que nombrarlas todas.
4. **Pedir archivos completos** en lugar de fragmentos cuando el cambio toca varias partes del archivo.
5. **Iterar:** revisar la respuesta, probarla y volver a preguntar con más detalle si algo falla.
6. **Verificar siempre** el código generado antes de integrarlo: la IA puede equivocarse o dejar partes sin cubrir.

### Aplicación en este proyecto

Para la Fase 2 se usó un prompt con esta estructura (Rol, Contexto, Tarea, Restricciones y Formato). El detalle, junto con lo que se tuvo que corregir, está en `PROMPTS.md`, en la rama `mejora-ia-clinica`.

### Fuentes

- Google AI for Developers — *Prompt design strategies*: https://ai.google.dev/gemini-api/docs/prompting-strategies
- Anthropic — *Prompt engineering overview*: https://platform.claude.com/docs/en/build-with-claude/prompt-engineering/overview

---

## Preguntas de sustentación

**1. ¿Cómo llega el médico elegido en Inicio hasta la pantalla de Confirmación?**

1. En **Inicio**, la tarjeta llama a `onMedicoClick(medico.id)`.
2. `AppNavegacion` arma la ruta `perfil/5` con `Pantalla.PerfilMedico.crearRuta(5)`.
3. El `NavHost` recibe el `5` con `navArgument` de tipo `Int`, y **Perfil** recupera el médico con `buscarMedico(5)`.
4. Perfil envía el mismo id a **Agendar** (`agendar/5`).
5. Agendar envía el id más la **posición** de la fecha y de la hora elegidas (`confirmacion/5/1/1`).
6. **Confirmación** recupera los datos con `buscarMedico(5)`, `fechas[1]` y `horas[1]`.

Se envían números y no textos porque textos como `10:30 am` llevan espacios y `:`, que pueden romper la ruta.

**2. ¿Por qué el drawer envuelve al Scaffold y no es un parámetro más?**

- En Material 3, `Scaffold` no tiene un parámetro para el drawer.
- El menú debe aparecer **encima de todo**, incluida la `topBar`.
- Al envolver el `NavHost`, **un solo menú** sirve para todas las pantallas.

Abrirlo usa `scope.launch { drawerState.open() }`, porque `open()` es una función `suspend` (tiene animación).

**3. ¿Por qué la selección de fecha y hora se comporta como un RadioButton, aunque sean chips?**

Porque cada grupo guarda **una sola posición** en una variable de estado (`fechaSeleccionada`). Cada chip usa `selected = fechaSeleccionada == index`. Al tocar otro chip el valor se reemplaza, así que solo uno cumple la condición a la vez. El comportamiento lo da el estado, no el componente visual.

**4. ¿Qué tuve que corregir del código que generó la IA en la Fase 2?**

- La IA solo cambió los archivos que nombraba el prompt y **no actualizó el Perfil del paciente**: el Total contaba las canceladas y no existía la estadística Canceladas. Se corrigió en un commit aparte.
- Los fragmentos sueltos eran difíciles de integrar, así que pedí el archivo `MisCitasScreen.kt` completo.

El detalle está en `PROMPTS.md`, en la rama `mejora-ia-clinica`.
