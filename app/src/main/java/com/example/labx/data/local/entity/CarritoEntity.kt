package com.example.labx.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.labx.domain.model.ItemCarrito
import com.example.labx.domain.model.Producto

/**
 * Entidad Room para tabla "carrito"
 * Versión CORREGIDA para soportar precios enteros (Backend)
 */
@Entity(tableName = "carrito")
data class CarritoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productoId: Int,
    val nombre: String,
    val descripcion: String, // Mantenemos tu campo original
    val precio: Int,         // <--- CAMBIO CRÍTICO: De Double a Int
    val imagenUrl: String,
    val categoria: String,
    val stock: Int,          // Mantenemos tu campo original
    val cantidad: Int = 1
)

/**
 * Convierte la entidad plana de la base de datos
 * al objeto complejo ItemCarrito (que contiene un Producto dentro)
 */
fun CarritoEntity.toItemCarrito(): ItemCarrito {
    return ItemCarrito(
        producto = Producto(
            id = productoId,
            nombre = nombre,
            descripcion = descripcion,
            precio = precio, // Aquí pasamos Int a Int (Sin error)
            categoria = categoria,
            imagenUrl = imagenUrl,
            stock = stock
        ),
        cantidad = cantidad
    )
}

/**
 * Convierte el objeto ItemCarrito para guardarlo en la base de datos
 */
fun ItemCarrito.toEntity(): CarritoEntity {
    return CarritoEntity(
        productoId = producto.id,
        nombre = producto.nombre,
        descripcion = producto.descripcion,
        precio = producto.precio, // Int a Int
        imagenUrl = producto.imagenUrl,
        categoria = producto.categoria,
        stock = producto.stock,
        cantidad = cantidad
    )
}