package com.example.labx.data.repository

import com.example.labx.data.local.dao.ProductoDao
import com.example.labx.data.local.entity.ProductoEntity
import com.example.labx.domain.model.Producto
import com.example.labx.domain.repository.RepositorioProductos
import com.example.labx.network.ProductoDto
import com.example.labx.network.RetrofitClient
import com.example.labx.network.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductoRepositoryImpl(
    private val productoDao: ProductoDao
) : RepositorioProductos {

    // Instancia de la API (La tomamos directamente del cliente que configuramos antes)
    private val apiService = RetrofitClient.instance

    // --- NUEVA FUNCIÓN: Obtener desde la API (Internet) ---
    // Esta función va a GitHub, baja el JSON y lo convierte a Producto
    // En ProductoRepositoryImpl.kt

    override suspend fun obtenerProductosApi(): List<Producto> {
        // ELIMINAMOS EL TRY-CATCH AQUÍ
        // Dejamos que si falla, el error explote hacia el ViewModel
        val listaDtos = apiService.obtenerProductos()
        return listaDtos.map { dto -> dto.toDomain() }
    }

    // --- EXISTENTE: Obtener desde Local (Base de Datos) ---
    override fun obtenerProductos(): Flow<List<Producto>> {
        return productoDao.obtenerTodosLosProductos().map { listaEntidades ->
            listaEntidades.map { entidad ->
                entidad.toDomain()
            }
        }
    }

    // 2. INSERTAR (Local)
    override suspend fun insertarProducto(producto: Producto): Long {
        return try {
            productoDao.insertarProducto(producto.toEntity())
        } catch (e: Exception) {
            e.printStackTrace()
            -1L
        }
    }

    // 3. ELIMINAR (Local)
    override suspend fun eliminarProducto(producto: Producto) {
        productoDao.eliminarProducto(producto.toEntity())
    }

    // 4. ACTUALIZAR (Local)
    override suspend fun actualizarProducto(producto: Producto) {
        productoDao.actualizarProducto(producto.toEntity())
    }

    override suspend fun obtenerProductoPorId(id: Int): Producto? {
        return productoDao.obtenerProductoPorId(id)?.toDomain()
    }

    override suspend fun insertarProductos(productos: List<Producto>) {
        productoDao.insertarProductos(productos.map { it.toEntity() })
    }

    override suspend fun eliminarTodosLosProductos() {
        productoDao.eliminarTodosLosProductos()
    }

    // --- CONVERTIDORES (MAPPERS) ---

    // De Entidad (BD) a Dominio (App)
    private fun ProductoEntity.toDomain(): Producto {
        return Producto(
            id = this.id,
            nombre = this.nombre,
            descripcion = this.descripcion,
            precio = this.precio,
            categoria = this.categoria,
            imagenUrl = this.imagenUrl,
            stock = this.stock
        )
    }

    // De Dominio (App) a Entidad (BD)
    private fun Producto.toEntity(): ProductoEntity {
        return ProductoEntity(
            id = this.id,
            nombre = this.nombre,
            descripcion = this.descripcion,
            precio = this.precio,
            categoria = this.categoria,
            imagenUrl = this.imagenUrl,
            stock = this.stock
        )
    }
}