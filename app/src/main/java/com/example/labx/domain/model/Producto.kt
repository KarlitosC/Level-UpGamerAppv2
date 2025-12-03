package com.example.labx.domain.model

import java.text.NumberFormat
import java.util.Locale

data class Producto(
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val categoria: String,
    val imagenUrl: String,
    val stock: Int = 0
) {
    // Estas son las funciones que el diseño necesita y que antes faltaban:

    val hayStock: Boolean
        get() = stock > 0

    fun precioFormateado(): String {
        return try {
            val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            format.format(precio)
        } catch (e: Exception) {
            "$$precio"
        }
    }
}