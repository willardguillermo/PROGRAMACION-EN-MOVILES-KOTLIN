# Registro de Notas - Jetpack Compose

## Descripción

Aplicación móvil desarrollada con Jetpack Compose que permite registrar notas de cursos, calcular un promedio ponderado y mostrar el estado académico del estudiante.

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Material 3
- Android Studio

## Funcionalidades

- Registro de notas mediante Slider (0 a 20).
- Cursos con porcentajes ponderados.
- Estado del curso mediante Switch.
- Registro de asistencia mediante Checkbox.
- Cálculo automático del promedio final.
- Conservación de datos mediante rememberSaveable.
- Resumen académico visual.

## Cursos evaluados

- Fundamentos de Programación (20%)
- Programación Orientada a Objetos (25%)
- Programación en Móviles (30%)
- Base de Datos (25%)

## Capturas

![Screenshot 2026-09-03 162657.png](capturas/Screenshot%202026-09-03%20162657.png)

![Screenshot 2026-09-03 163140.png](capturas/Screenshot%202026-09-03%20163140.png)

![Screenshot 2026-09-03 171312.png](capturas/Screenshot%202026-09-03%20171312.png)

![Screenshot 2026-09-03 171819.png](capturas/Screenshot%202026-09-03%20171819.png)

## Conceptos aplicados

### Estado en Compose

El estado permite que la interfaz se actualice automáticamente cuando cambian los valores almacenados.

### rememberSaveable

Permite conservar los valores cuando ocurre una recomposición o rotación de pantalla.

## Historial de commits

1. Crear estructura inicial registro de notas.
2. Agregar sliders de notas por curso.
3. Agregar estado del curso y asistencia.
4. Agregar cálculo de promedio ponderado y resultado final.
5. Agregar validaciones de estado y asistencia.
6. Conservar datos al rotar pantalla usando rememberSaveable.
7. Mejorar diseño visual del resumen académico.