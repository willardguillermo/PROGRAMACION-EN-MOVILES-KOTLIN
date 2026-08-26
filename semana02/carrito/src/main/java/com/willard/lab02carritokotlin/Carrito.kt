package com.willard.lab02carritokotlin

// ABSTRACCION
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

// HERENCIA
class ProductoTecnologico(
    nombre: String,
    precio: Double,
    cantidad: Int,
    private val marca: String
) : Producto(nombre, precio, cantidad) {

    override fun mostrarTipo(): String {
        return "Tecnologico - $marca"
    }
}

class Accesorio(
    nombre: String,
    precio: Double,
    cantidad: Int,
    private val categoria: String
) : Producto(nombre, precio, cantidad) {

    override fun mostrarTipo(): String {
        return "Accesorio - $categoria"
    }
}

// ABSTRACCION + POLIMORFISMO
interface EstrategiaDescuento {
    fun calcular(total: Double): Double
}

class SinDescuento : EstrategiaDescuento {
    override fun calcular(total: Double): Double {
        return 0.0
    }
}

class DescuentoCincoPorCiento : EstrategiaDescuento {
    override fun calcular(total: Double): Double {
        return total * 0.05
    }
}

class DescuentoDiezPorCiento : EstrategiaDescuento {
    override fun calcular(total: Double): Double {
        return total * 0.10
    }
}

// ENCAPSULAMIENTO
class CarritoCompras {

    private val productos = mutableListOf<Producto>()

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
    }

    fun obtenerProductos(): List<Producto> {
        return productos.toList()
    }

    fun cantidadProductos(): Int {
        return productos.size
    }

    fun calcularSubtotal(): Double {
        var subtotal = 0.0

        for (producto in productos) {
            subtotal += producto.calcularImporte()
        }

        return subtotal
    }

    fun calcularIGV(): Double {
        return calcularSubtotal() * 0.18
    }

    fun calcularTotal(): Double {
        return calcularSubtotal() + calcularIGV()
    }

    fun buscarProducto(nombre: String): Producto? {
        return productos.find {
            it.nombre.equals(nombre, ignoreCase = true)
        }
    }

    fun eliminarProducto(nombre: String): Boolean {
        return productos.removeIf {
            it.nombre.equals(nombre, ignoreCase = true)
        }
    }

    fun productoMasCaro(): Producto? {
        return productos.maxByOrNull { it.precio }
    }

    fun mostrarDetalle() {

        println()
        println("--------- DETALLE DEL CARRITO ---------")

        var i = 1

        for (producto in productos) {

            println(
                String.format(
                    "%d. %-20s x%d S/ %8.2f | %s",
                    i,
                    producto.nombre,
                    producto.cantidad,
                    producto.calcularImporte(),
                    producto.mostrarTipo()
                )
            )

            i++
        }

        println("---------------------------------------")
    }
}

fun obtenerEstrategiaDescuento(total: Double): EstrategiaDescuento {

    return when {
        total > 5000 -> DescuentoDiezPorCiento()
        total > 3000 -> DescuentoCincoPorCiento()
        else -> SinDescuento()
    }
}

fun mostrarTotales(carrito: CarritoCompras) {

    val subtotal = carrito.calcularSubtotal()
    val igv = carrito.calcularIGV()
    val total = carrito.calcularTotal()

    val estrategia =
        obtenerEstrategiaDescuento(total)

    val descuento =
        estrategia.calcular(total)

    val totalConDescuento =
        total - descuento

    println()

    println(
        String.format(
            "Subtotal             : S/ %8.2f",
            subtotal
        )
    )

    println(
        String.format(
            "IGV (18%%)            : S/ %8.2f",
            igv
        )
    )

    println(
        String.format(
            "TOTAL A PAGAR        : S/ %8.2f",
            total
        )
    )

    println(
        String.format(
            "Descuento aplicado   : S/ %8.2f",
            descuento
        )
    )

    println(
        String.format(
            "TOTAL CON DESCUENTO  : S/ %8.2f",
            totalConDescuento
        )
    )
}

fun main() {

    println("=========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println(" VERSION POO - CON IA ")
    println("=========================================")

    print("Ingrese su nombre: ")
    val nombreCliente = readln()

    println()
    println("Cliente: $nombreCliente")

    val carrito = CarritoCompras()

    print("\n¿Cuantos productos desea agregar?: ")
    val cantidadProductos = readln().toInt()

    for (i in 1..cantidadProductos) {

        println()
        println("--------- PRODUCTO $i ---------")

        print("Nombre del producto: ")
        val nombre = readln()

        print("Precio: S/ ")
        val precio = readln().toDouble()

        print("Cantidad: ")
        val cantidad = readln().toInt()

        println()
        println("Tipo de producto:")
        println("1. Producto tecnologico")
        println("2. Accesorio")

        print("Seleccione una opcion: ")
        val opcion = readln().toInt()

        when (opcion) {

            1 -> {

                print("Marca: ")
                val marca = readln()

                val producto =
                    ProductoTecnologico(
                        nombre,
                        precio,
                        cantidad,
                        marca
                    )

                carrito.agregarProducto(producto)
            }

            2 -> {

                print("Categoria: ")
                val categoria = readln()

                val producto =
                    Accesorio(
                        nombre,
                        precio,
                        cantidad,
                        categoria
                    )

                carrito.agregarProducto(producto)
            }

            else -> {
                println("Opcion no valida.")
            }
        }
    }

    println()
    println("=========================================")
    println(" PRODUCTOS REGISTRADOS ")
    println("=========================================")

    for (producto in carrito.obtenerProductos()) {

        println(
            "Producto agregado: ${producto.nombre} - " +
                    producto.mostrarTipo()
        )
    }

    carrito.mostrarDetalle()

    println(
        "Cantidad de productos: ${carrito.cantidadProductos()}"
    )

    mostrarTotales(carrito)

    println()

    val masCaro =
        carrito.productoMasCaro()

    if (masCaro != null) {

        println(
            "Producto mas caro: ${masCaro.nombre} " +
                    String.format(
                        "(S/ %.2f)",
                        masCaro.precio
                    )
        )
    }

    println()
    println("--------- BUSCAR PRODUCTO ---------")

    print("Ingrese el nombre del producto a buscar: ")
    val nombreBusqueda = readln()

    val productoBuscado =
        carrito.buscarProducto(nombreBusqueda)

    if (productoBuscado != null) {

        println(
            "Producto encontrado: ${productoBuscado.nombre}"
        )

        println(
            "Precio: S/ ${productoBuscado.precio}"
        )

        println(
            "Cantidad: ${productoBuscado.cantidad}"
        )

        println(
            "Tipo: ${productoBuscado.mostrarTipo()}"
        )

    } else {

        println("Producto no encontrado.")
    }

    println()
    println("--------- ELIMINAR PRODUCTO ---------")

    print("Ingrese el nombre del producto a eliminar: ")
    val nombreEliminar = readln()

    val eliminado =
        carrito.eliminarProducto(nombreEliminar)

    if (eliminado) {

        println(
            "Producto eliminado correctamente: $nombreEliminar"
        )

    } else {

        println(
            "No se encontro el producto."
        )
    }

    println()
    println("--------- CARRITO ACTUALIZADO ---------")

    carrito.mostrarDetalle()

    println(
        "Cantidad de productos: ${carrito.cantidadProductos()}"
    )

    mostrarTotales(carrito)

    println()
    println("Programa finalizado.")
}