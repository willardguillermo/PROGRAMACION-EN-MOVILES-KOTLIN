# Lab 04 - Mi Carrito TECSUP con LazyColumn

**Nombre:** Willard Guillermo
**Curso:** Programación en Móviles - 4to Ciclo

## Descripción
App de carrito de compras hecha con Jetpack Compose. Permite agregar productos
(nombre, precio, cantidad) a una lista observable, mostrarlos en una LazyColumn
con tarjetas individuales, eliminarlos, y ver el subtotal, IGV (18%) y total
calculados en tiempo real. Muestra un estado vacío cuando no hay productos.

## Capturas
![Carrito vacío](ruta/a/tu/imagen1.png)
![Carrito con productos](ruta/a/tu/imagen2.png)

## Respuestas conceptuales

**(a) ¿Por qué mutableStateListOf y no una MutableList normal?**
mutableStateListOf crea una lista que Compose "observa": cuando agregas o quitas algo, 
Compose se entera y redibuja la pantalla automáticamente. 
Una MutableList normal (como mutableListOf) cambia su contenido igual, 
pero Compose no tiene forma de detectarlo, así que la UI no se actualiza sola.

**(b) ¿Por qué la lista se declara con val?**
val congela la referencia a la lista (nunca vas a escribir productos = otraLista), 
pero el contenido interno sí puede cambiar con add/remove porque eso no reasigna la variable, 
solo modifica lo que hay dentro del objeto.

**(c) ¿Qué hace weight(1f) en la LazyColumn?**
weight(1f) le dice a la lista que ocupe todo el espacio vertical que sobra dentro del Column, 
después de restarle lo que ya ocupan el formulario y el panel de totales. 
Así la lista se hace scrolleable dentro de su espacio y el panel de totales queda siempre visible y fijo abajo.