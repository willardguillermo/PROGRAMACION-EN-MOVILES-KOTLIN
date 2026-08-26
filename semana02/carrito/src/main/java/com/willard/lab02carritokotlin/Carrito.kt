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

// HERENCIA
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
        return productos.find { it.nombre == nombre }
    }

    fun eliminarProducto(nombre: String): Boolean {
        return productos.removeIf { it.nombre == nombre }
    }

    fun productoMasCaro(): Producto? {
        return productos.maxByOrNull { it.precio }
    }

    fun mostrarDetalle() {
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

fun main() {

    println("=========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println(" VERSION POO - CON IA ")
    println("=========================================")

    val nombreCliente = "Willard Guillermo"

    println("Cliente: $nombreCliente")
    println()

    val carrito = CarritoCompras()

    carrito.agregarProducto(
        ProductoTecnologico(
            "Laptop HP",
            2500.0,
            1,
            "HP"
        )
    )

    carrito.agregarProducto(
        Accesorio(
            "Mouse Logitech",
            45.5,
            2,
            "Mouse"
        )
    )

    carrito.agregarProducto(
        ProductoTecnologico(
            "Audifonos Sony",
            120.0,
            1,
            "Sony"
        )
    )

    carrito.agregarProducto(
        Accesorio(
            "Teclado Redragon",
            180.0,
            2,
            "Teclado"
        )
    )

    for (producto in carrito.obtenerProductos()) {
        println(
            "Producto agregado: ${producto.nombre} - ${producto.mostrarTipo()}"
        )
    }

    println()

    carrito.mostrarDetalle()

    println(
        "Cantidad de productos: ${carrito.cantidadProductos()}"
    )

    println()

    val subtotal = carrito.calcularSubtotal()
    val igv = carrito.calcularIGV()
    val total = carrito.calcularTotal()

    println(
        String.format(
            "Subtotal      : S/ %8.2f",
            subtotal
        )
    )

    println(
        String.format(
            "IGV (18%%)     : S/ %8.2f",
            igv
        )
    )

    println(
        String.format(
            "TOTAL A PAGAR : S/ %8.2f",
            total
        )
    )

    println()

    val masCaro = carrito.productoMasCaro()

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

    // POLIMORFISMO
    val estrategia: EstrategiaDescuento =
        obtenerEstrategiaDescuento(total)

    val descuento = estrategia.calcular(total)
    val totalConDescuento = total - descuento

    println(
        String.format(
            "Descuento aplicado: S/ %.2f",
            descuento
        )
    )

    println(
        String.format(
            "TOTAL CON DESCUENTO: S/ %.2f",
            totalConDescuento
        )
    )

    println()
    println("--------- BUSQUEDA DE PRODUCTO ---------")

    val productoBuscado =
        carrito.buscarProducto("Mouse Logitech")

    if (productoBuscado != null) {
        println(
            "Producto encontrado: ${productoBuscado.nombre}"
        )
    } else {
        println("Producto no encontrado")
    }

    println()
    println("--------- ELIMINAR PRODUCTO ---------")

    val eliminado =
        carrito.eliminarProducto("Audifonos Sony")

    if (eliminado) {
        println("Producto eliminado: Audifonos Sony")
    } else {
        println("Producto no encontrado")
    }

    println()

    carrito.mostrarDetalle()

    println(
        "Cantidad de productos: ${carrito.cantidadProductos()}"
    )

    println()

    val nuevoSubtotal = carrito.calcularSubtotal()
    val nuevoIgv = carrito.calcularIGV()
    val nuevoTotal = carrito.calcularTotal()

    val nuevaEstrategia =
        obtenerEstrategiaDescuento(nuevoTotal)

    val nuevoDescuento =
        nuevaEstrategia.calcular(nuevoTotal)

    val nuevoTotalConDescuento =
        nuevoTotal - nuevoDescuento

    println(
        String.format(
            "Subtotal actualizado      : S/ %8.2f",
            nuevoSubtotal
        )
    )

    println(
        String.format(
            "IGV actualizado (18%%)     : S/ %8.2f",
            nuevoIgv
        )
    )

    println(
        String.format(
            "TOTAL actualizado         : S/ %8.2f",
            nuevoTotal
        )
    )

    println(
        String.format(
            "Descuento actualizado     : S/ %8.2f",
            nuevoDescuento
        )
    )

    println(
        String.format(
            "TOTAL CON DESCUENTO       : S/ %8.2f",
            nuevoTotalConDescuento
        )
    )
}