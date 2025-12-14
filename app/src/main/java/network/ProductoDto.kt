package com.example.labx.network

import com.google.gson.annotations.SerializedName
import com.example.labx.domain.model.Producto

data class ProductoDto(
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
)

fun ProductoDto.toDomain(): Producto {
    return Producto(
        id = sku?.hashCode() ?: 0,

        nombre = nombre ?: "Producto sin nombre",
        descripcion = descripcion ?: "Sin descripción",
        precio = precio?.toInt() ?: 0,
        categoria = categoria ?: "General",


        imagenUrl = imagen ?: "",


        stock = 10
    )
}