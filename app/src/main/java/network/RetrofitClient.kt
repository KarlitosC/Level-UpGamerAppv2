package com.example.labx.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Si usas EMULADOR usa 10.0.2.2
    // Si usas CELULAR FÍSICO usa la IP de tu PC (ej: 192.168.1.X)
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val apiService: ProductoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductoApiService::class.java)
    }
}