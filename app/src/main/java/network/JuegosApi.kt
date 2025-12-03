package com.example.labx.network

import com.example.labx.domain.model.JuegoExterno
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Interfaz para definir el endpoint
interface JuegosApiService {
    @GET("api/games")
    suspend fun obtenerJuegos(): List<JuegoExterno>
}

// Objeto Singleton para la conexión (URL distinta a la de tu backend)
object JuegosClient {
    private const val BASE_URL = "https://www.freetogame.com/"

    val service: JuegosApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JuegosApiService::class.java)
    }
}