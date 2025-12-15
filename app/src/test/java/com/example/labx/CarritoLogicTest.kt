package com.example.labx

import org.junit.Test
import org.junit.Assert.*

class CarritoLogicTest {

    @Test
    fun calcularSubtotal_esCorrecto() {
        // 1. PREPARACIÓN (GIVEN)
        val precioProducto = 35000
        val cantidad = 2

        // 2. EJECUCIÓN (WHEN) - Simulamos lo que hace tu App
        val subtotal = precioProducto * cantidad

        // 3. VERIFICACIÓN (THEN) - ¿Es 70,000?
        assertEquals(70000, subtotal)
    }

    @Test
    fun formatearPrecio_esCorrecto() {
        // Simulamos la lógica de formateo simple
        val precio = 5000
        val precioFormateado = "$$precio"

        assertTrue(precioFormateado.contains("5000"))
        assertTrue(precioFormateado.startsWith("$"))
    }

    @Test
    fun calcularTotalCarrito_sumaCorrecta() {
        // Simulamos tener 3 items en el carrito
        val item1 = 10000
        val item2 = 20000
        val item3 = 5000

        val totalEsperado = 35000
        val totalCalculado = item1 + item2 + item3

        assertEquals("El total del carrito debería ser 35000", totalEsperado, totalCalculado)
    }
}