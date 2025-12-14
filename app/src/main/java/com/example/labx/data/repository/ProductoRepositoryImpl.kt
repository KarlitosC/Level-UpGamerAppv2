package com.example.labx.data.repository

import com.example.labx.data.local.dao.ProductoDao
import com.example.labx.data.local.entity.ProductoEntity
import com.example.labx.domain.model.Producto
import com.example.labx.domain.repository.RepositorioProductos
import network.RetrofitClient
import com.example.labx.network.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductoRepositoryImpl(
    private val productoDao: ProductoDao
) : RepositorioProductos {

    private val apiService = RetrofitClient.instance


    override suspend fun obtenerProductosApi(): List<Producto> {
        val listaDtos = apiService.obtenerProductos()
        return listaDtos.map { dto -> dto.toDomain() }
    }


    override fun obtenerProductos(): Flow<List<Producto>> {
        return productoDao.obtenerTodosLosProductos().map { listaEntidades ->
            listaEntidades.map { entidad ->
                entidad.toDomain()
            }
        }
    }

    override suspend fun insertarProducto(producto: Producto): Long {
        return try {
            productoDao.insertarProducto(producto.toEntity())
        } catch (e: Exception) {
            e.printStackTrace()
            -1L
        }
    }


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