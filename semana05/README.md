# Portal Académico — App de Navegación (Jetpack Compose)

App móvil desarrollada en **Kotlin** con **Jetpack Compose** y **Navigation Compose**,
como mejora visual del laboratorio de navegación (Semana 05). La aplicación simula un
**Portal Académico** con acceso, directorio de alumnos, expediente y perfil, aplicando
Material 3 y un flujo de navegación con paso de argumentos y manejo del back stack.

---

## Descripción de lo que se hizo

Partiendo de la app base de navegación (Home, Lista, Detalle y Perfil), se rediseñó la
presentación para convertirla en un **Portal Académico** con una interfaz más pulida
usando **Material 3**. Se agregó una **pantalla de Login** como punto de entrada, una
**bienvenida** con accesos en tarjetas, un **directorio de alumnos** en lista, un
**expediente académico** que recibe el id del alumno como argumento tipado, y una
**configuración de perfil**. Toda la navegación entre pantallas se maneja con
`NavHost`, rutas definidas en una `sealed class`, un argumento tipado (`Int`) y el
control del back stack con `popBackStack()` y `popUpTo(inclusive = true)` al cerrar
sesión.

---

## Requerimientos funcionales

> Estos requerimientos describen exactamente el comportamiento implementado en la app.

- **RF01 — Acceso (Login):** La app presenta una pantalla de inicio de sesión donde el
  usuario ingresa un correo y una contraseña, con opción de **mostrar/ocultar** la
  contraseña. Al presionar **"Iniciar Sesión"**, accede a la pantalla principal.
  *(El acceso es directo, no realiza validación real de credenciales.)*
- **RF02 — Pantalla de bienvenida:** Tras iniciar sesión, la app muestra un saludo con
  el nombre del usuario y dos accesos en tarjetas: **"Directorio de Alumnos"** y
  **"Mi Perfil Académico"**.
- **RF03 — Directorio de alumnos:** La app muestra una lista de **5 estudiantes de
  ejemplo** (avatar, nombre y carrera). Al tocar un estudiante, navega a su expediente.
- **RF04 — Expediente académico:** La app muestra el detalle del estudiante seleccionado
  (nombre, carrera, ID de estudiante, correo, facultad y biografía). El identificador del
  alumno viaja entre pantallas como **argumento tipado (Int)**.
- **RF05 — Perfil / Configuración:** La app muestra la información **personal** (nombre,
  correo, teléfono) y **académica** (carrera y ciclo) del usuario.
- **RF06 — Navegación y retorno:** La app permite volver a la pantalla anterior con el
  botón de retroceso (flecha) de la barra superior (`popBackStack()`).
- **RF07 — Cierre de sesión:** Desde la bienvenida y desde el perfil, el usuario puede
  **cerrar sesión** y volver a la pantalla de Login. Al hacerlo, se **limpia el historial
  de navegación** (`popUpTo(inclusive = true)`), por lo que no se puede regresar con el
  botón atrás.

---

## Tecnologías

- Kotlin
- Jetpack Compose (Material 3)
- Navigation Compose (`androidx.navigation:navigation-compose`)
- Íconos: `androidx.compose.material:material-icons-extended`
- Minimum SDK: API 24

---

## Estructura del proyecto

```
com.willard.semana05_navegacion
├── data
│   └── Student.kt              // Modelo de datos y lista de estudiantes de ejemplo
├── navigation
│   ├── Screen.kt               // Rutas de la app (sealed class), incluida detail/{itemId}
│   └── AppNavigation.kt        // NavHost, startDestination = Login, argumento tipado
├── screens
│   ├── LoginScreen.kt          // Acceso al portal
│   ├── HomeScreen.kt           // Bienvenida con tarjetas de acceso
│   ├── ListScreen.kt           // Directorio de Alumnos
│   ├── DetailScreen.kt         // Expediente Académico (recibe itemId: Int)
│   └── ProfileScreen.kt        // Configuración de Perfil
└── MainActivity.kt             // setContent { AppNavigation() }
```

---

## Prompt utilizado con Gemini

Prompt empleado para generar la mejora de presentación. Está redactado para que Gemini
produzca **exactamente** la app descrita en este README:

```
Actúa como desarrollador Android senior experto en Kotlin, Jetpack Compose y Material 3.
Genera una app llamada "Portal Académico" usando Navigation Compose, con estas 5 pantallas
y esta navegación exacta. Mantén la lógica de navegación con una sealed class de rutas y un
argumento tipado Int.

RUTAS (sealed class Screen):
- Login  -> "login"   (pantalla inicial / startDestination)
- Home   -> "home"
- List   -> "list"
- Profile-> "profile"
- Detail -> "detail/{itemId}"  con función createRoute(itemId: Int) y NavType.IntType

MODELO DE DATOS:
- data class Student(id: Int, nombre, carrera, correo, facultad, biografia)
- Una lista de 5 estudiantes de ejemplo y una función para buscar por id.

PANTALLAS:
1) LoginScreen: fondo con degradado suave, una Card centrada con título "Portal Académico"
   y subtítulo "Accede a tu cuenta"; campo de correo (con ícono) y campo de contraseña
   (con ícono y botón para mostrar/ocultar); botón "INICIAR SESIÓN" que navega a Home y
   limpia el login del back stack con popUpTo(login){ inclusive = true }; y un TextButton
   "¿Olvidaste tu contraseña?" (sin acción real).
2) HomeScreen: saludo "Bienvenido, <nombre>" con subtítulo "¿Qué deseas gestionar hoy?";
   dos ElevatedCard con ícono, título y subtítulo: "Directorio de Alumnos" (va a List) y
   "Mi Perfil Académico" (va a Profile); abajo un botón de texto "Cerrar Sesión Segura"
   que vuelve al Login limpiando el back stack.
3) ListScreen: Scaffold con TopAppBar "Directorio de Alumnos" y flecha de retroceso
   (popBackStack); LazyColumn de tarjetas de estudiantes (avatar circular, nombre, carrera
   y una flecha ">"); al tocar una tarjeta navega al detalle pasando el id del alumno.
4) DetailScreen(itemId: Int): Scaffold con TopAppBar "Expediente Académico" y flecha de
   retroceso; cabecera con banda de color y avatar circular centrado, nombre y carrera;
   una Card con ID de estudiante, correo y facultad (cada uno con su ícono) y una sección
   "Biografía". El itemId llega tipado como Int desde el NavHost.
5) ProfileScreen: Scaffold con TopAppBar "Configuración de Perfil" y flecha de retroceso;
   cabecera con banda de color, avatar y nombre; sección "Información Personal" (nombre,
   correo, teléfono) y sección "Académico" (carrera, ciclo); un botón "Cerrar Sesión" que
   vuelve al Login limpiando el back stack.

REQUISITOS TÉCNICOS:
- Material 3 (Scaffold, TopAppBar, Card/ElevatedCard, ListItem/Row, OutlinedTextField,
  Button, OutlinedButton, TextButton, Icon).
- Esquema de color morado/violeta en modo claro, esquinas redondeadas y sombras suaves.
- Avatares con un ícono de persona dentro de un círculo (sin necesidad de imágenes).
- Íconos de material-icons-extended.
- Datos de estudiantes y de perfil hardcodeados (de ejemplo).
- Entrega el código completo de cada archivo .kt por separado, con sus imports y listo
  para pegar, e indica si hay que añadir alguna dependencia.
```

# EVIDENCIAS DE EJECUCION SIN IA:

<img width="382" height="802" alt="image" src="https://github.com/user-attachments/assets/1af27550-0634-4ef5-b559-98018e845cc1" />

<img width="367" height="781" alt="image" src="https://github.com/user-attachments/assets/fa697686-1e3e-4390-a9be-5c184de82ee6" />

<img width="362" height="782" alt="image" src="https://github.com/user-attachments/assets/33913508-23bf-4246-8210-aefa6aeab38e" />

<img width="376" height="780" alt="image" src="https://github.com/user-attachments/assets/b7739548-f35a-4815-8088-0219b12b9fa6" />

# EVIDENCIAS DE EJECUCION CON IA:

<img width="360" height="766" alt="image" src="https://github.com/user-attachments/assets/b80afd4f-d5b8-47dd-96ac-daec77940261" />

<img width="356" height="752" alt="image" src="https://github.com/user-attachments/assets/a05b2029-3b8b-47d2-80f7-26f5af61b1df" />

<img width="351" height="780" alt="image" src="https://github.com/user-attachments/assets/8623cbd8-779d-4687-9794-af66194a2969" />

<img width="366" height="778" alt="image" src="https://github.com/user-attachments/assets/0734cc7e-20fd-46d1-8a51-9500fffd78d2" />

<img width="367" height="787" alt="image" src="https://github.com/user-attachments/assets/21df4fca-3039-4020-9510-68032c93482f" />

# LOG DE COMMITS:

<img width="1361" height="410" alt="image" src="https://github.com/user-attachments/assets/aa92a3c3-fe18-45ef-a785-dafd05d6af2d" />









