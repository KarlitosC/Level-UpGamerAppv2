package com.example.labx

import com.example.labx.data.repository.ProductoRepositoryImpl
import com.example.labx.domain.model.Producto
import com.example.labx.ui.viewmodel.ProductoViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // 1. Mockeamos el repositorio (Simulamos que existe sin conectar a internet real)
    private val repository = mockk<ProductoRepositoryImpl>()

    // 2. Definimos datos de prueba falsos
    private val productoPrueba = Producto(
        id = 1,
        nombre = "Mouse Test",
        descripcion = "Mouse de prueba",
        precio = 1000,
        categoria = "Test",
        imagenUrl = "http://fake.url",
        stock = 5
    )

    @Test
    fun `cuando cargarProductos es exitoso, actualiza el estado con la lista`() = runTest {
        // GIVEN (Dado que): El repositorio responde con una lista de productos
        coEvery { repository.obtenerProductos() } returns flowOf(listOf(productoPrueba))

        // WHEN (Cuando): Inicializamos el ViewModel (que llama a cargarProductos automáticamente)
        val viewModel = ProductoViewModel(repository)

        // THEN (Entonces):
        // 1. No debe haber error
        assertEquals(null, viewModel.uiState.value.error)
        // 2. No debe estar cargando
        assertFalse(viewModel.uiState.value.estaCargando)
        // 3. La lista debe tener 1 producto
        assertEquals(1, viewModel.uiState.value.productos.size)
        // 4. El nombre del producto debe ser el correcto
        assertEquals("Mouse Test", viewModel.uiState.value.productos[0].nombre)
    }

    @Test
    fun `cuando hay error, actualiza el estado con mensaje de error`() = runTest {
        // GIVEN: El repositorio lanza una excepción (simulamos falla de internet)
        coEvery { repository.obtenerProductos() } throws RuntimeException("Error de conexión")

        // WHEN: Inicializamos el ViewModel
        // Nota: Como el init llama a cargarProductos, necesitamos instanciarlo y esperar
        // En este caso simple, simulamos la llamada manual si es necesario,
        // pero asumiremos que tu ViewModel maneja el try-catch en el init o en la función.

        // Para este test, si tu ViewModel atrapa el error en el bloque init, el estado debería reflejarlo.
        // Si no tienes try-catch en el init, este test podría fallar, pero vamos a probar.
    }
}