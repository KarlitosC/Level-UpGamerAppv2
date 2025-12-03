package com.example.labx.domain.model

import com.google.gson.annotations.SerializedName

data class JuegoExterno(
    val id: Int,
    val title: String,
    @SerializedName("thumbnail") val imagen: String, // Mapeamos el nombre de la API
    @SerializedName("short_description") val descripcion: String,
    val genre: String,
    val platform: String
)