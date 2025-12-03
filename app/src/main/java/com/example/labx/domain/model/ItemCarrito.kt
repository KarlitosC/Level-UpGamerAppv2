package com.example.labx.domain.model

data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int = 1
) {
    // CAMBIO: Ahora devuelve Int porque el precio es Int
    val subtotal: Int
        get() = producto.precio * cantidad
}