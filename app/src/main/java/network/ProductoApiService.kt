package network

import com.example.labx.network.ProductoDto
import retrofit2.http.GET

interface ProductoApiService {
    @GET("productos.json")
    suspend fun obtenerProductos(): List<ProductoDto>

}