## Laboratorio 02 - Carrito de Compras en Kotlin con POO

## Datos del estudiante

Nombre: Willard Guillermo
Curso: Programación en Móviles
Laboratorio: 02
Lenguaje: Kotlin
Rama: con-ia

## Descripción

En esta versión del laboratorio se desarrolló la lógica de un carrito de compras en Kotlin aplicando Programación Orientada a Objetos.

El programa conserva las funcionalidades principales del laboratorio original:

* Registro de productos.
* Cálculo del subtotal.
* Cálculo del IGV del 18%.
* Cálculo del total.
* Reporte detallado del carrito.
* Producto más caro.
* Descuento según el monto total.
* Búsqueda de productos.
* Eliminación de productos.
* Recalculo de totales.

Además, esta versión implementa los principios de:

* Abstracción.
* Herencia.
* Encapsulamiento.
* Polimorfismo.

## Abstracción

Se utilizó una clase abstracta llamada Producto.

abstract class Producto(
val nombre: String,
val precio: Double,
var cantidad: Int
) {
abstract fun mostrarTipo(): String
open fun calcularImporte(): Double {
return precio * cantidad
}
}

La clase Producto representa las características comunes que comparten todos los productos.

Además, el método:

abstract fun mostrarTipo(): String

obliga a las clases hijas a definir su propia forma de indicar qué tipo de producto representan.

También se utilizó la interfaz:

interface EstrategiaDescuento {
fun calcular(total: Double): Double
}

Esta interfaz define el comportamiento general que deben tener las diferentes estrategias de descuento.

## Herencia

Se crearon clases que heredan de Producto.

Por ejemplo:

class ProductoTecnologico(
nombre: String,
precio: Double,
cantidad: Int,
private val marca: String
) : Producto(nombre, precio, cantidad)

También:

class Accesorio(
nombre: String,
precio: Double,
cantidad: Int,
private val categoria: String
) : Producto(nombre, precio, cantidad)

Esto permite reutilizar los atributos y métodos definidos en la clase Producto.

## Encapsulamiento

La clase CarritoCompras mantiene internamente la lista de productos:

private val productos = mutableListOf<Producto>()

La palabra private evita que la lista pueda ser modificada directamente desde fuera de la clase.

Para interactuar con el carrito se utilizan métodos como:

fun agregarProducto(producto: Producto)
fun eliminarProducto(nombre: String): Boolean
fun buscarProducto(nombre: String): Producto?
fun calcularSubtotal(): Double

De esta manera, la clase controla cómo se accede y modifica su información.

## Polimorfismo

El polimorfismo se utiliza principalmente en las estrategias de descuento.

Todas estas clases implementan la misma interfaz:

class SinDescuento : EstrategiaDescuento
class DescuentoCincoPorCiento : EstrategiaDescuento
class DescuentoDiezPorCiento : EstrategiaDescuento

Cada clase implementa el mismo método:

fun calcular(total: Double): Double

pero realiza un cálculo diferente.

Por ejemplo:

class DescuentoCincoPorCiento : EstrategiaDescuento {
override fun calcular(total: Double): Double {
return total * 0.05
}
}

y:

class DescuentoDiezPorCiento : EstrategiaDescuento {
override fun calcular(total: Double): Double {
return total * 0.10
}
}

En main() se utiliza una referencia del tipo general:

val estrategia: EstrategiaDescuento =
obtenerEstrategiaDescuento(total)

La variable estrategia puede contener diferentes implementaciones de EstrategiaDescuento, y el método calcular() se comportará de acuerdo con el objeto concreto recibido.

Lógica del descuento

La estrategia se selecciona utilizando when:

fun obtenerEstrategiaDescuento(total: Double): EstrategiaDescuento {
return when {
total > 5000 -> DescuentoDiezPorCiento()
total > 3000 -> DescuentoCincoPorCiento()
else -> SinDescuento()
}
}

Esto mantiene la misma regla del laboratorio original:

* Más de S/ 5000: 10%.
* Más de S/ 3000: 5%.
* En otro caso: sin descuento.

## Prompt utilizado con Inteligencia Artificial

Para desarrollar la versión con IA se utilizó el siguiente prompt:

> Adapta el laboratorio de carrito de compras en Kotlin a Programación Orientada a Objetos, manteniendo todas las funcionalidades del laboratorio original.
>
> La solución debe demostrar de forma clara los cuatro principios de POO:
>
> - Encapsulamiento
> - Herencia
> - Abstracción
> - Polimorfismo
>
> Mantén las funcionalidades de:
>
> - Registro de productos.
> - Cálculo de subtotal.
> - Cálculo del IGV del 18%.
> - Cálculo del total.
> - Reporte del carrito.
> - Producto más caro.
> - Descuento de 5% si supera S/ 3000 y 10% si supera S/ 5000.
> - Búsqueda de productos.
> - Eliminación de productos.
> - Recalculo de totales.
>
> Utiliza Kotlin y ejecuta el programa por consola.
>
> La solución debe ser sencilla de explicar en una defensa oral y debe indicar claramente en qué parte del código se aplica cada principio de Programación Orientada a Objetos.

## Uso de la IA

La Inteligencia Artificial fue utilizada para proponer una reorganización del código original aplicando Programación Orientada a Objetos.

La versión original del laboratorio se conserva en la rama `sin-ia`, mientras que esta rama `con-ia` contiene la versión adaptada con abstracción, herencia, encapsulamiento y polimorfismo.

## Conclusión

La versión con-ia mantiene la funcionalidad del carrito desarrollado en el laboratorio, pero reorganiza el código utilizando Programación Orientada a Objetos.

La aplicación de abstracción, herencia, encapsulamiento y polimorfismo permite separar responsabilidades, reutilizar código y facilitar futuras modificaciones del sistema.