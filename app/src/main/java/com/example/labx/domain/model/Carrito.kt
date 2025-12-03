package com.example.labx.domain.model

/**
 * Representa el carrito de compras completo
 * Versión corregida para precios Enteros (Int)
 */
data class Carrito(
    val items: List<ItemCarrito> = emptyList()
) {
    // Cantidad total de items en el carrito
    val cantidadTotal: Int
        get() = items.sumOf { it.cantidad }

    // Precio total del carrito
    // CAMBIO: Ahora devuelve Int porque la suma de enteros da un entero
    val precioTotal: Int
        get() = items.sumOf { it.subtotal }

    // Verifica si el carrito está vacío
    val estaVacio: Boolean
        get() = items.isEmpty()
}