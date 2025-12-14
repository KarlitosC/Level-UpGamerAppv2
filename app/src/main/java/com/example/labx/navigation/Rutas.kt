package com.example.labx.ui.navigation

object Rutas {
    const val PORTADA = "portada"
    const val HOME = "home"

    const val JUEGOS_API = "juegos_api"
    const val DETALLE = "detalle"
    const val CARRITO = "carrito"
    const val REGISTRO = "registro"
    const val CAMARA = "camara"

    fun detalleConId(id: Int) = "detalle/$id"
    fun formularioEditar(id: Int) = "formulario_producto?productoId=$id"
}
