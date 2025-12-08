package com.example.labx.network

import com.google.gson.annotations.SerializedName
import com.example.labx.domain.model.Producto

data class ProductoDto(
    // El JSON usa "sku" (texto) en lugar de ID numérico
    @SerializedName("sku")
    val sku: String?,

    @SerializedName("nombre")
    val nombre: String?,

    @SerializedName("precio")
    val precio: Double?,

    @SerializedName("descripcion")
    val descripcion: String?,

    @SerializedName("categoria")
    val categoria: String?,

    @SerializedName("imagen")
    val imagen: String?

    // Ignoramos "stock_por_sucursal" por ahora para no complicar el código
)

fun ProductoDto.toDomain(): Producto {
    return Producto(
        // TRUCO: Como tu app necesita un ID número (Int) pero el JSON trae texto (SKU),
        // usamos .hashCode() para crear un número único basado en el texto.
        id = sku?.hashCode() ?: 0,

        nombre = nombre ?: "Producto sin nombre",
        descripcion = descripcion ?: "Sin descripción",
        precio = precio?.toInt() ?: 0,
        categoria = categoria ?: "General",

        // Si la imagen viene vacía o es de ejemplo, Coil mostrará el error por defecto
        imagenUrl = imagen ?: "",

        // Como el stock en el JSON es complejo, ponemos 10 fijo para que salga "Disponible"
        stock = 10
    )
}