package com.example.labx.data.repository

import com.example.labx.data.local.dao.CarritoDao
import com.example.labx.data.local.entity.CarritoEntity
import com.example.labx.data.local.entity.toItemCarrito // Usamos la nueva función
import com.example.labx.domain.model.ItemCarrito
import com.example.labx.domain.model.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CarritoRepository(private val carritoDao: CarritoDao) {

    /**
     * Obtiene todos los items del carrito
     * CORREGIDO: Usa toItemCarrito() que definimos en el paso anterior
     */
    fun obtenerCarrito(): Flow<List<ItemCarrito>> {
        return carritoDao.obtenerTodo()
            .map { entities ->
                entities.map { entity ->
                    entity.toItemCarrito() // <--- AQUÍ ESTABA EL ERROR
                }
            }
    }

    suspend fun agregarProducto(producto: Producto, cantidad: Int = 1) {
        val existente = carritoDao.obtenerPorProductoId(producto.id)

        if (existente != null) {
            val nuevaCantidad = existente.cantidad + cantidad
            carritoDao.actualizarCantidad(producto.id, nuevaCantidad)
        } else {
            // CORREGIDO: El precio ahora entra directo como Int
            val entity = CarritoEntity(
                productoId = producto.id,
                nombre = producto.nombre,
                descripcion = producto.descripcion,
                precio = producto.precio, // Int
                imagenUrl = producto.imagenUrl,
                categoria = producto.categoria,
                stock = producto.stock,
                cantidad = cantidad
            )
            carritoDao.insertar(entity)
        }
    }

    suspend fun modificarCantidad(productoId: Int, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            eliminarProducto(productoId)
        } else {
            carritoDao.actualizarCantidad(productoId, nuevaCantidad)
        }
    }

    suspend fun eliminarProducto(productoId: Int) {
        carritoDao.eliminarProducto(productoId)
    }

    suspend fun vaciarCarrito() {
        carritoDao.vaciar()
    }

    /**
     * Obtiene el total del carrito
     * CORREGIDO: Ahora devuelve Int (Entero)
     */
    fun obtenerTotal(): Flow<Int> {
        // Mapeamos el resultado a Int. Si el DAO devuelve null, retornamos 0
        return carritoDao.obtenerTotal()
            .map { it?.toInt() ?: 0 }
    }
}