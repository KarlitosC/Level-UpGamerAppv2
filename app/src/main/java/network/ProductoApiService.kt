package com.example.labx.network

import retrofit2.http.GET

interface ProductoApiService {

    // Ahora sí reconoce ProductoDto porque son "vecinos" de carpeta
    @GET("productos.json")
    suspend fun obtenerProductos(): List<ProductoDto>

}