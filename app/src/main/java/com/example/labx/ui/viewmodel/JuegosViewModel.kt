package com.example.labx.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labx.domain.model.JuegoExterno
import com.example.labx.network.JuegosClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JuegosViewModel : ViewModel() {

    private val _juegos = MutableStateFlow<List<JuegoExterno>>(emptyList())
    val juegos: StateFlow<List<JuegoExterno>> = _juegos

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    init {
        cargarJuegos()
    }

    fun cargarJuegos() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                // Llamamos a la API real
                val lista = JuegosClient.service.obtenerJuegos()
                // Tomamos solo los primeros 20 para no saturar la pantalla
                _juegos.value = lista.take(20)
            } catch (e: Exception) {
                e.printStackTrace()
                _juegos.value = emptyList()
            } finally {
                _cargando.value = false
            }
        }
    }
}