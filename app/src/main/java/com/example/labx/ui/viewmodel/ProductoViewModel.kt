package com.example.labx.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.labx.data.repository.ProductoRepositoryImpl
// Si esta línea te da error, borrala y asegúrate de tener la data class definida
// o defínela aquí abajo como hicimos antes.
import com.example.labx.ui.state.ProductoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductoViewModel(
    private val repositorio: ProductoRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductoUiState())
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    fun cargarProductos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(estaCargando = true, error = null)
            try {
                val productosApi = repositorio.obtenerProductosApi()

                _uiState.value = _uiState.value.copy(
                    estaCargando = false,
                    productos = productosApi
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    estaCargando = false,
                    error = "Error API: ${e.message}"
                )
            }
        }
    }
    fun cargarProductosLocales() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(estaCargando = true)

            repositorio.obtenerProductos()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        estaCargando = false,
                        error = exception.message ?: "Error desconocido"
                    )
                }
                .collect { productos ->
                    _uiState.value = _uiState.value.copy(
                        estaCargando = false,
                        productos = productos,
                        error = null
                    )
                }
        }
    }



    suspend fun obtenerProductoPorId(id: Int) = repositorio.obtenerProductoPorId(id)

    fun agregarProducto(producto: com.example.labx.domain.model.Producto) {
        viewModelScope.launch { repositorio.insertarProducto(producto) }
    }

    fun actualizarProducto(producto: com.example.labx.domain.model.Producto) {
        viewModelScope.launch { repositorio.actualizarProducto(producto) }
    }

    fun eliminarProducto(producto: com.example.labx.domain.model.Producto) {
        viewModelScope.launch { repositorio.eliminarProducto(producto) }
    }
    fun guardarTodoEnLocal() {
        viewModelScope.launch {
            val productosActuales = _uiState.value.productos
            if (productosActuales.isNotEmpty()) {
                // Usamos la función de insertar lista que ya tenías en el repositorio
                repositorio.insertarProductos(productosActuales)
            }
        }
    }
}


class ProductoViewModelFactory(
    private val repositorio: ProductoRepositoryImpl
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            return ProductoViewModel(repositorio) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}