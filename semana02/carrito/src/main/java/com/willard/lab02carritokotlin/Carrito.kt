package com.willard.lab02carritokotlin

// =========================
// ABSTRACCION
// =========================

abstract class Producto(
    val nombre: String,
    val precio: Double,
    var stock: Int
) {
    abstract fun mostrarTipo(): String
}

// =========================
// HERENCIA
// =========================

class ProductoTecnologico(
    nombre: String,
    precio: Double,
    stock: Int,
    private val marca: String
) : Producto(nombre, precio, stock) {

    override fun mostrarTipo(): String {
        return "Tecnologico - $marca"
    }
}

class Accesorio(
    nombre: String,
    precio: Double,
    stock: Int,
    private val categoria: String
) : Producto(nombre, precio, stock) {

    override fun mostrarTipo(): String {
        return "Accesorio - $categoria"
    }
}

// =========================
// USUARIOS
// =========================

abstract class Usuario(
    val nombre: String
) {
    abstract fun mostrarRol(): String
}

class Vendedor(nombre: String) : Usuario(nombre) {

    override fun mostrarRol(): String {
        return "Vendedor"
    }
}

class Cliente(nombre: String) : Usuario(nombre) {

    override fun mostrarRol(): String {
        return "Cliente"
    }
}

// =========================
// POLIMORFISMO
// =========================

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

// =========================
// ITEM DEL CARRITO
// =========================

data class ItemCarrito(
    val producto: Producto,
    var cantidad: Int
) {
    fun calcularImporte(): Double {
        return producto.precio * cantidad
    }
}

// =========================
// TIENDA - ENCAPSULAMIENTO
// =========================

class Tienda {

    private val catalogo = mutableListOf<Producto>()

    fun registrarProducto(producto: Producto) {
        catalogo.add(producto)

        println()
        println("Producto registrado correctamente.")
    }

    fun obtenerCatalogo(): List<Producto> {
        return catalogo.toList()
    }

    fun buscarProducto(nombre: String): Producto? {
        return catalogo.find {
            it.nombre.equals(nombre, ignoreCase = true)
        }
    }

    fun eliminarProducto(nombre: String): Boolean {
        val producto = buscarProducto(nombre)

        return if (producto != null) {
            catalogo.remove(producto)
            true
        } else {
            false
        }
    }

    fun mostrarCatalogo() {

        println()
        println("=============== CATALOGO ===============")

        if (catalogo.isEmpty()) {
            println("No existen productos registrados.")
        } else {

            for ((indice, producto) in catalogo.withIndex()) {

                println(
                    "${indice + 1}. ${producto.nombre}"
                )

                println(
                    "   Precio: S/ %.2f".format(producto.precio)
                )

                println(
                    "   Stock: ${producto.stock}"
                )

                println(
                    "   Tipo: ${producto.mostrarTipo()}"
                )

                println()
            }
        }

        println("========================================")
    }
}

// =========================
// CARRITO
// =========================

class CarritoCompras {

    private val items = mutableListOf<ItemCarrito>()

    fun agregarProducto(
        producto: Producto,
        cantidad: Int
    ): Boolean {

        if (cantidad <= 0) {
            return false
        }

        if (cantidad > producto.stock) {
            return false
        }

        val existente = items.find {
            it.producto.nombre.equals(
                producto.nombre,
                ignoreCase = true
            )
        }

        if (existente != null) {

            if (existente.cantidad + cantidad > producto.stock) {
                return false
            }

            existente.cantidad += cantidad

        } else {

            items.add(
                ItemCarrito(
                    producto,
                    cantidad
                )
            )
        }

        return true
    }

    fun eliminarProducto(nombre: String): Boolean {

        val item = items.find {
            it.producto.nombre.equals(
                nombre,
                ignoreCase = true
            )
        }

        return if (item != null) {
            items.remove(item)
            true
        } else {
            false
        }
    }

    fun calcularSubtotal(): Double {

        var subtotal = 0.0

        for (item in items) {
            subtotal += item.calcularImporte()
        }

        return subtotal
    }

    fun calcularIGV(): Double {
        return calcularSubtotal() * 0.18
    }

    fun calcularTotal(): Double {
        return calcularSubtotal() + calcularIGV()
    }

    fun estaVacio(): Boolean {
        return items.isEmpty()
    }

    fun productoMasCaro(): Producto? {

        return items.maxByOrNull {
            it.producto.precio
        }?.producto
    }

    fun mostrarCarrito() {

        println()
        println("=============== CARRITO ===============")

        if (items.isEmpty()) {

            println("El carrito esta vacio.")

        } else {

            for ((indice, item) in items.withIndex()) {

                println(
                    String.format(
                        "%d. %-20s x%d  S/ %.2f",
                        indice + 1,
                        item.producto.nombre,
                        item.cantidad,
                        item.calcularImporte()
                    )
                )
            }
        }

        println("=======================================")
    }

    fun finalizarCompra() {

        for (item in items) {
            item.producto.stock -= item.cantidad
        }
    }
}

// =========================
// DESCUENTO
// =========================

fun obtenerEstrategiaDescuento(
    total: Double
): EstrategiaDescuento {

    return when {

        total > 5000 ->
            DescuentoDiezPorCiento()

        total > 3000 ->
            DescuentoCincoPorCiento()

        else ->
            SinDescuento()
    }
}

// =========================
// TOTALES
// =========================

fun mostrarTotales(
    carrito: CarritoCompras
) {

    val subtotal =
        carrito.calcularSubtotal()

    val igv =
        carrito.calcularIGV()

    val total =
        carrito.calcularTotal()

    val estrategia =
        obtenerEstrategiaDescuento(total)

    val descuento =
        estrategia.calcular(total)

    val totalFinal =
        total - descuento

    println()

    println(
        "Subtotal: S/ %.2f".format(subtotal)
    )

    println(
        "IGV (18%%): S/ %.2f".format(igv)
    )

    println(
        "Total: S/ %.2f".format(total)
    )

    println(
        "Descuento: S/ %.2f".format(descuento)
    )

    println(
        "TOTAL A PAGAR: S/ %.2f".format(totalFinal)
    )
}

// =========================
// MENU VENDEDOR
// =========================

fun menuVendedor(
    vendedor: Vendedor,
    tienda: Tienda
) {

    var opcion: Int

    do {

        println()
        println("========================================")
        println(" VENDEDOR: ${vendedor.nombre}")
        println("========================================")
        println("1. Registrar producto")
        println("2. Ver catalogo")
        println("3. Buscar producto")
        println("4. Eliminar producto")
        println("5. Salir")

        print("Seleccione una opcion: ")

        opcion =
            readlnOrNull()?.toIntOrNull() ?: 0

        when (opcion) {

            1 -> registrarProducto(tienda)

            2 -> tienda.mostrarCatalogo()

            3 -> {

                print(
                    "Ingrese el nombre del producto: "
                )

                val nombre =
                    readln()

                val producto =
                    tienda.buscarProducto(nombre)

                if (producto != null) {

                    println()
                    println("Producto encontrado:")
                    println("Nombre: ${producto.nombre}")

                    println(
                        "Precio: S/ %.2f".format(
                            producto.precio
                        )
                    )

                    println(
                        "Stock: ${producto.stock}"
                    )

                    println(
                        "Tipo: ${producto.mostrarTipo()}"
                    )

                } else {

                    println(
                        "Producto no encontrado."
                    )
                }
            }

            4 -> {

                print(
                    "Producto que desea eliminar: "
                )

                val nombre =
                    readln()

                if (tienda.eliminarProducto(nombre)) {

                    println(
                        "Producto eliminado."
                    )

                } else {

                    println(
                        "Producto no encontrado."
                    )
                }
            }

            5 -> println(
                "Cerrando sesion del vendedor..."
            )

            else -> println(
                "Opcion no valida."
            )
        }

    } while (opcion != 5)
}

// =========================
// REGISTRAR PRODUCTO
// =========================

fun registrarProducto(
    tienda: Tienda
) {

    println()
    println("--------- NUEVO PRODUCTO ---------")

    print("Nombre: ")
    val nombre =
        readln()

    print("Precio: S/ ")
    val precio =
        readlnOrNull()?.toDoubleOrNull()

    print("Stock: ")
    val stock =
        readlnOrNull()?.toIntOrNull()

    if (
        precio == null ||
        precio <= 0 ||
        stock == null ||
        stock < 0
    ) {

        println(
            "Precio o stock no valido."
        )

        return
    }

    println()
    println("Tipo:")
    println("1. Producto tecnologico")
    println("2. Accesorio")

    print("Opcion: ")

    val tipo =
        readlnOrNull()?.toIntOrNull()

    when (tipo) {

        1 -> {

            print("Marca: ")

            val marca =
                readln()

            tienda.registrarProducto(
                ProductoTecnologico(
                    nombre,
                    precio,
                    stock,
                    marca
                )
            )
        }

        2 -> {

            print("Categoria: ")

            val categoria =
                readln()

            tienda.registrarProducto(
                Accesorio(
                    nombre,
                    precio,
                    stock,
                    categoria
                )
            )
        }

        else -> println(
            "Tipo de producto no valido."
        )
    }
}

// =========================
// MENU CLIENTE
// =========================

fun menuCliente(
    cliente: Cliente,
    tienda: Tienda
) {

    val carrito =
        CarritoCompras()

    var opcion: Int

    do {

        println()
        println("========================================")
        println(" CLIENTE: ${cliente.nombre}")
        println("========================================")
        println("1. Ver productos")
        println("2. Buscar producto")
        println("3. Agregar producto al carrito")
        println("4. Ver carrito")
        println("5. Eliminar producto del carrito")
        println("6. Finalizar compra")
        println("7. Salir")

        print("Seleccione una opcion: ")

        opcion =
            readlnOrNull()?.toIntOrNull() ?: 0

        when (opcion) {

            1 -> tienda.mostrarCatalogo()

            2 -> {

                print(
                    "Nombre del producto: "
                )

                val nombre =
                    readln()

                val producto =
                    tienda.buscarProducto(nombre)

                if (producto != null) {

                    println()
                    println(
                        "Producto: ${producto.nombre}"
                    )

                    println(
                        "Precio: S/ %.2f".format(
                            producto.precio
                        )
                    )

                    println(
                        "Stock disponible: ${producto.stock}"
                    )

                } else {

                    println(
                        "Producto no encontrado."
                    )
                }
            }

            3 -> {

                tienda.mostrarCatalogo()

                print(
                    "Ingrese el nombre del producto: "
                )

                val nombre =
                    readln()

                val producto =
                    tienda.buscarProducto(nombre)

                if (producto == null) {

                    println(
                        "Producto no encontrado."
                    )

                } else {

                    print("Cantidad: ")

                    val cantidad =
                        readlnOrNull()?.toIntOrNull()

                    if (
                        cantidad == null ||
                        cantidad <= 0
                    ) {

                        println(
                            "Cantidad no valida."
                        )

                    } else {

                        if (
                            carrito.agregarProducto(
                                producto,
                                cantidad
                            )
                        ) {

                            println(
                                "Producto agregado al carrito."
                            )

                        } else {

                            println(
                                "No existe stock suficiente."
                            )
                        }
                    }
                }
            }

            4 -> {

                carrito.mostrarCarrito()

                if (!carrito.estaVacio()) {
                    mostrarTotales(carrito)
                }
            }

            5 -> {

                print(
                    "Producto que desea retirar: "
                )

                val nombre =
                    readln()

                if (
                    carrito.eliminarProducto(nombre)
                ) {

                    println(
                        "Producto eliminado del carrito."
                    )

                } else {

                    println(
                        "Producto no encontrado en el carrito."
                    )
                }
            }

            6 -> {

                if (carrito.estaVacio()) {

                    println(
                        "No puede finalizar una compra con el carrito vacio."
                    )

                } else {

                    carrito.mostrarCarrito()

                    mostrarTotales(carrito)

                    val masCaro =
                        carrito.productoMasCaro()

                    if (masCaro != null) {

                        println()

                        println(
                            "Producto mas caro: " +
                                    "${masCaro.nombre} - " +
                                    "S/ %.2f".format(
                                        masCaro.precio
                                    )
                        )
                    }

                    print(
                        "\n¿Confirmar compra? (S/N): "
                    )

                    val confirmar =
                        readln()

                    if (
                        confirmar.equals(
                            "S",
                            ignoreCase = true
                        )
                    ) {

                        carrito.finalizarCompra()

                        println()
                        println(
                            "Compra realizada correctamente."
                        )

                        println(
                            "Gracias por su compra, ${cliente.nombre}."
                        )

                        return

                    } else {

                        println(
                            "Compra cancelada."
                        )
                    }
                }
            }

            7 -> println(
                "Saliendo del menu cliente..."
            )

            else -> println(
                "Opcion no valida."
            )
        }

    } while (opcion != 7)
}

// =========================
// MAIN
// =========================

fun main() {

    val tienda =
        Tienda()

    // Productos iniciales para que el cliente tenga
    // un catalogo disponible desde el inicio.

    tienda.registrarProducto(
        ProductoTecnologico(
            "Laptop HP",
            2500.0,
            5,
            "HP"
        )
    )

    tienda.registrarProducto(
        Accesorio(
            "Mouse Logitech",
            45.50,
            10,
            "Mouse"
        )
    )

    tienda.registrarProducto(
        ProductoTecnologico(
            "Audifonos Sony",
            120.0,
            8,
            "Sony"
        )
    )

    tienda.registrarProducto(
        Accesorio(
            "Teclado Redragon",
            180.0,
            6,
            "Teclado"
        )
    )

    var opcionPrincipal: Int

    do {

        println()
        println("========================================")
        println("          TIENDA TECSUP")
        println("========================================")
        println("1. Ingresar como vendedor")
        println("2. Ingresar como cliente")
        println("3. Salir")

        print("Seleccione una opcion: ")

        opcionPrincipal =
            readlnOrNull()?.toIntOrNull() ?: 0

        when (opcionPrincipal) {

            1 -> {

                print(
                    "Ingrese nombre del vendedor: "
                )

                val nombre =
                    readln()

                val vendedor =
                    Vendedor(nombre)

                menuVendedor(
                    vendedor,
                    tienda
                )
            }

            2 -> {

                print(
                    "Ingrese nombre del cliente: "
                )

                val nombre =
                    readln()

                val cliente =
                    Cliente(nombre)

                menuCliente(
                    cliente,
                    tienda
                )
            }

            3 -> {

                println()
                println(
                    "Gracias por utilizar Tienda TECSUP."
                )
            }

            else -> {

                println(
                    "Opcion no valida."
                )
            }
        }

    } while (opcionPrincipal != 3)
}