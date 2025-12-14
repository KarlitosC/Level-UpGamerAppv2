package com.example.labx.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.labx.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductoDao {
    

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun obtenerTodosLosProductos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtenerProductoPorId(id: Int): ProductoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProductos(productos: List<ProductoEntity>)
    

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(producto: ProductoEntity): Long
    

    @Update
    suspend fun actualizarProducto(producto: ProductoEntity)
    

    @Delete
    suspend fun eliminarProducto(producto: ProductoEntity)

    @Query("DELETE FROM productos")
    suspend fun eliminarTodosLosProductos()
}
