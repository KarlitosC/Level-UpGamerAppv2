package com.example.labx.ui.navigation

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.labx.data.local.PreferenciasManager
import com.example.labx.data.repository.CarritoRepository
import com.example.labx.data.repository.ProductoRepositoryImpl
import com.example.labx.ui.screen.CamaraScreen
import com.example.labx.ui.screen.CarritoScreen
import com.example.labx.ui.screen.DetalleProductoScreen
import com.example.labx.ui.screen.HomeScreen
import com.example.labx.ui.screen.PortadaScreen
import com.example.labx.ui.viewmodel.ProductoViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    productoRepository: ProductoRepositoryImpl,
    carritoRepository: CarritoRepository,
    preferenciasManager: PreferenciasManager,
    productoViewModel: ProductoViewModel,
    modifier: Modifier = Modifier
) {
    // 1. ESTADO ELEVADO: Aquí guardamos la foto para toda la app
    var avatarBitmap by remember { mutableStateOf<Bitmap?>(null) }

    NavHost(
        navController = navController,
        startDestination = Rutas.PORTADA,
        modifier = modifier
    ) {

        // --- Ruta 1: Portada ---
        composable(route = Rutas.PORTADA) {
            PortadaScreen(
                avatarBitmap = avatarBitmap, // Le pasamos la foto guardada
                onEntrarClick = { navController.navigate(Rutas.HOME) },
                onLocalClick = { navController.navigate("home_local") },
                onAdminClick = { },
                onCamaraClick = { navController.navigate(Rutas.CAMARA) }
            )
        }

        // --- Ruta 2: Home Online ---
        composable(route = Rutas.HOME) {
            HomeScreen(
                productoRepository = productoRepository,
                carritoRepository = carritoRepository,
                esModoLocal = false,
                onProductoClick = { id -> navController.navigate("${Rutas.DETALLE}/$id") },
                onCarritoClick = { navController.navigate(Rutas.CARRITO) },
                onRegistroClick = { },
                onVolverPortada = { navController.popBackStack() },
                onJuegosClick = { }
            )
        }

        // --- Ruta 3: Home Local ---
        composable(route = "home_local") {
            HomeScreen(
                productoRepository = productoRepository,
                carritoRepository = carritoRepository,
                esModoLocal = true,
                onProductoClick = { id -> navController.navigate("${Rutas.DETALLE}/$id") },
                onCarritoClick = { navController.navigate(Rutas.CARRITO) },
                onRegistroClick = { },
                onVolverPortada = { navController.popBackStack() },
                onJuegosClick = { }
            )
        }

        // --- Ruta 4: Detalle ---
        composable(
            route = "${Rutas.DETALLE}/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            DetalleProductoScreen(
                productoId = productoId,
                productoRepository = productoRepository,
                carritoRepository = carritoRepository,
                onVolverClick = { navController.popBackStack() }
            )
        }

        // --- Ruta 5: Carrito ---
        composable(route = Rutas.CARRITO) {
            CarritoScreen(
                carritoRepository = carritoRepository,
                onVolverClick = { navController.popBackStack() },
                onProductoClick = { id -> navController.navigate("${Rutas.DETALLE}/$id") }
            )
        }

        // --- Ruta 6: CÁMARA ---
        composable(route = Rutas.CAMARA) {
            CamaraScreen(
                navController = navController,
                onPhotoTaken = { nuevaFoto ->
                    avatarBitmap = nuevaFoto // Guardamos la foto aquí arriba
                }
            )
        }
    }
}