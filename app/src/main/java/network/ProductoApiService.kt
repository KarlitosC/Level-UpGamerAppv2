package com.example.labx.network

import com.example.labx.domain.model.Producto
import retrofit2.Call
import retrofit2.http.*

interface ProductoApiService {
    @GET("api/productos")
    suspend fun obtenerProductos(): List<Producto>

    @POST("api/productos")
    suspend fun crearProducto(@Body producto: Producto): Producto

    @PUT("api/productos/{id}")
    suspend fun actualizarProducto(@Path("id") id: Int, @Body producto: Producto): Producto

    @DELETE("api/productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Int)
}