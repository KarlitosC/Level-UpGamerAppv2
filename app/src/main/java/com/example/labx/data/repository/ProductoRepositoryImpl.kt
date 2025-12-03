package com.example.labx.data.repository

import com.example.labx.data.local.dao.ProductoDao
import com.example.labx.data.local.entity.ProductoEntity
import com.example.labx.domain.model.Producto
import com.example.labx.domain.repository.RepositorioProductos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * REPOSITORIO CORREGIDO:
 * Conecta la Base de Datos Local (Room) con la UI.
 * Ignora Retrofit/Internet para los productos manuales.
 */
class ProductoRepositoryImpl(
    private val productoDao: ProductoDao
) : RepositorioProductos {

    // 1. OBTENER: Escucha la base de datos en tiempo real
    // Cuando insertes algo en el Admin, este Flow avisará a la pantalla Home automáticamente.
    override fun obtenerProductos(): Flow<List<Producto>> {
        return productoDao.obtenerTodosLosProductos().map { listaEntidades ->
            listaEntidades.map { entidad ->
                entidad.toDomain()
            }
        }
    }

    // 2. INSERTAR: Guarda en la memoria del teléfono (Room)
    override suspend fun insertarProducto(producto: Producto): Long {
        return try {
            productoDao.insertarProducto(producto.toEntity())
        } catch (e: Exception) {
            e.printStackTrace()
            -1L
        }
    }

    // 3. ELIMINAR: Borra de la memoria del teléfono
    override suspend fun eliminarProducto(producto: Producto) {
        productoDao.eliminarProducto(producto.toEntity())
    }

    // 4. ACTUALIZAR: Modifica en la memoria del teléfono
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
    // Pasan los datos de "Formato Base de Datos" a "Formato App" y viceversa

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

    private fun Producto.toEntity(): ProductoEntity {
        return ProductoEntity(
            id = this.id, // Si es nuevo (0), Room generará el ID
            nombre = this.nombre,
            descripcion = this.descripcion,
            precio = this.precio,
            categoria = this.categoria,
            imagenUrl = this.imagenUrl,
            stock = this.stock
        )
    }
}