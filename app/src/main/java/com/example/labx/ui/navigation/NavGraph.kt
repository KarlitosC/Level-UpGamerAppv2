package com.example.labx.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.labx.data.local.PreferenciasManager
import com.example.labx.data.repository.CarritoRepository
import com.example.labx.data.repository.ProductoRepositoryImpl
import com.example.labx.ui.screen.CarritoScreen
import com.example.labx.ui.screen.DetalleProductoScreen
import com.example.labx.ui.screen.HomeScreen
import com.example.labx.ui.screen.PortadaScreen
// import com.example.labx.ui.screen.JuegosGratisScreen // Ya no lo usamos, lo comentamos
import com.example.labx.ui.viewmodel.ProductoViewModel
import androidx.compose.ui.Modifier

@Composable
fun NavGraph(
    navController: NavHostController,
    productoRepository: ProductoRepositoryImpl,
    carritoRepository: CarritoRepository,
    preferenciasManager: PreferenciasManager,
    productoViewModel: ProductoViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.PORTADA,
        modifier = modifier
    ) {

        // --- Ruta 0: Portada ---
        composable(route = Rutas.PORTADA) {
            PortadaScreen(
                onEntrarClick = {
                    // BOTÓN 1: Ir a la Tienda Online (API)
                    navController.navigate(Rutas.HOME) {
                        // Opcional: Si quieres que al volver atrás salga de la app
                        // popUpTo(Rutas.PORTADA) { inclusive = true }
                    }
                },
                onLocalClick = {
                    // BOTÓN 2: Ir al Inventario Local (Base de Datos)
                    navController.navigate("home_local")
                },
                onAdminClick = {
                    // Botón eliminado o vacío
                }
            )
        }

        // --- Ruta 1: Home ONLINE (API) ---
        composable(route = Rutas.HOME) {
            HomeScreen(
                productoRepository = productoRepository,
                carritoRepository = carritoRepository,
                esModoLocal = false, // <--- IMPORTANTE: Carga de Internet
                onProductoClick = { productoId ->
                    navController.navigate("${Rutas.DETALLE}/$productoId")
                },
                onCarritoClick = {
                    navController.navigate(Rutas.CARRITO)
                },
                onRegistroClick = { },
                onVolverPortada = {
                    navController.popBackStack()
                },
                onJuegosClick = { } // Ya no hace nada porque quitamos la estrella
            )
        }

        // --- Ruta Nueva: Home LOCAL (Base de Datos) ---
        composable(route = "home_local") {
            HomeScreen(
                productoRepository = productoRepository,
                carritoRepository = carritoRepository,
                esModoLocal = true, // <--- IMPORTANTE: Carga de la BD Local
                onProductoClick = { productoId ->
                    navController.navigate("${Rutas.DETALLE}/$productoId")
                },
                onCarritoClick = {
                    navController.navigate(Rutas.CARRITO)
                },
                onRegistroClick = { },
                onVolverPortada = {
                    navController.popBackStack()
                },
                onJuegosClick = { }
            )
        }

        // --- Ruta 2: Detalle de producto ---
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

        // --- Ruta 3: Carrito ---
        composable(route = Rutas.CARRITO) {
            CarritoScreen(
                carritoRepository = carritoRepository,
                onVolverClick = { navController.popBackStack() },
                onProductoClick = { productoId ->
                    navController.navigate("${Rutas.DETALLE}/$productoId")
                }
            )
        }

        // He eliminado la ruta de "juegos_api" antigua para limpiar el código
    }
}