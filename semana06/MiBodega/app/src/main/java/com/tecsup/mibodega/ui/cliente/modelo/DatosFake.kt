package com.tecsup.mibodega.ui.cliente.modelo


val listaCategorias = listOf("Todos", "Bebidas", "Abarrotes", "Snacks")

val listaProductosFake = listOf(
    // ---------- Abarrotes ----------
    Producto(
        id = 1,
        nombre = "Arroz Costeño",
        descripcion = "Arroz extra, grano largo, ideal para el día a día.",
        precio = 4.50,
        categoria = "Abarrotes"
    ),
    Producto(
        id = 2,
        nombre = "Aceite Primor",
        descripcion = "Aceite vegetal 1 L, alto en vitamina E.",
        precio = 8.90,
        categoria = "Abarrotes"
    ),
    Producto(
        id = 3,
        nombre = "Leche Gloria",
        descripcion = "Leche evaporada entera 1 L.",
        precio = 5.20,
        categoria = "Abarrotes"
    ),
    Producto(
        id = 9,
        nombre = "Azúcar Rubia",
        descripcion = "Azúcar rubia doméstica 1 kg.",
        precio = 4.20,
        categoria = "Abarrotes"
    ),
    Producto(
        id = 10,
        nombre = "Fideos Spaghetti",
        descripcion = "Fideos largos de trigo 500 g.",
        precio = 3.10,
        categoria = "Abarrotes"
    ),

    // ---------- Snacks ----------
    Producto(
        id = 4,
        nombre = "Galleta Oreo",
        descripcion = "Galletas de chocolate rellenas 126 g.",
        precio = 3.50,
        categoria = "Snacks"
    ),
    Producto(
        id = 11,
        nombre = "Papas Lay's",
        descripcion = "Papas fritas clásicas 42 g.",
        precio = 2.80,
        categoria = "Snacks"
    ),
    Producto(
        id = 12,
        nombre = "Chocolate Sublime",
        descripcion = "Chocolate con leche y maní 30 g.",
        precio = 2.00,
        categoria = "Snacks"
    ),

    // ---------- Bebidas ----------
    Producto(
        id = 5,
        nombre = "Coca-Cola Original",
        descripcion = "Bebida gaseosa sabor cola. Ideal para compartir en familia.",
        precio = 6.50,
        categoria = "Bebidas"
    ),
    Producto(
        id = 6,
        nombre = "Inca Kola",
        descripcion = "Gaseosa sabor nacional 1.5 L.",
        precio = 6.50,
        categoria = "Bebidas"
    ),
    Producto(
        id = 7,
        nombre = "Agua San Luis",
        descripcion = "Agua de mesa sin gas 625 ml.",
        precio = 1.50,
        categoria = "Bebidas"
    ),
    Producto(
        id = 8,
        nombre = "Jugo Frugos",
        descripcion = "Néctar de durazno 1 L.",
        precio = 4.80,
        categoria = "Bebidas"
    )
)