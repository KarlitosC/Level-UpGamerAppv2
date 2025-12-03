package com.example.labx.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val categoria: String, // Campo nuevo para la tienda Gamer
    val imagenUrl: String, // Campo nuevo para la tienda Gamer
    val stock: Int
)