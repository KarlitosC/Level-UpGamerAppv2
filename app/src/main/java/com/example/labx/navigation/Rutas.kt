package com.example.labx.ui.navigation

/**
 * Rutas: Nombres de las pantallas para navegación
 * 
 * ¿Por qué usar constantes?
 * - Evita errores de tipeo ("home" vs "Home" vs "HOME")
 * - Fácil de cambiar en un solo lugar
 * - Autocompletado del IDE
 * 
 * Autor: Prof. Sting Adams Parra Silva
 */
object Rutas {
    const val PORTADA = "portada"
    const val HOME = "home"

    const val JUEGOS_API = "juegos_api"
    const val DETALLE = "detalle"
    const val CARRITO = "carrito"
    const val REGISTRO = "registro"
    
    // Funciones helper para pasar argumentos
    fun detalleConId(id: Int) = "detalle/$id"
    fun formularioEditar(id: Int) = "formulario_producto?productoId=$id"
}
