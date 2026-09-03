# Lab03 Registro de Producto - Jetpack Compose

## Datos del estudiante

Nombre:
TU NOMBRE

Curso:
Programación en Móviles

## Descripción

Aplicación móvil desarrollada con Jetpack Compose que permite registrar productos ingresando nombre, precio y cantidad.

La aplicación utiliza:
- Column y Row para organizar la interfaz.
- OutlinedTextField para ingreso de datos.
- Button para ejecutar acciones.
- Card para mostrar el resumen del producto.
- remember y mutableStateOf para manejar el estado.

## Capturas

### Pantalla inicial

<img width="386" height="513" alt="pantalla-vacia" src="https://github.com/user-attachments/assets/ee3daa18-1bac-4f1d-bfae-71bc60bd19ad" />


### Producto registrado

<img width="385" height="824" alt="producto-registrado" src="https://github.com/user-attachments/assets/64039360-ed5e-4d8f-b208-03f132c6e9bc" />


# Mejora con IA

## Descripción

En la rama `mejora-ia` se agregó una mejora al registro de productos:
- Validación de campos vacíos.
- Validación de valores numéricos.
- Botón limpiar formulario.

## Tabla de decisiones

| Prompt que usé | Qué generó Gemini | Qué acepté o corregí |
|---|---|---|
| Agregar validación de campos vacíos y botón limpiar en PantallaRegistro sin modificar la estructura existente | Generó la validación de campos vacíos y un botón Limpiar para reiniciar los valores del formulario | Acepté la lógica del botón Limpiar y ajusté los mensajes de validación |
| Revisar la validación de precio y cantidad | Propuso convertir los valores usando toDoubleOrNull y toIntOrNull | Corregí la lógica porque el uso de ?: 0.0 ocultaba errores de datos inválidos. Implementé una validación con null antes de calcular el importe |



## Pregunta de reflexión

### ¿Qué pasaría si declaras las variables de los campos SIN remember?

Si las variables se declaran sin remember, Compose no conservaría el estado de los datos ingresados. Al recomponerse la pantalla, los valores volverían a su estado inicial y el usuario perdería lo escrito en los campos.

`remember` permite mantener los valores mientras la composición permanezca activa.
