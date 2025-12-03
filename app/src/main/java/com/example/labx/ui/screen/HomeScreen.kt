package com.example.labx.ui.screen

// --- TODOS LOS IMPORTS NECESARIOS ---
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.labx.data.repository.CarritoRepository
import com.example.labx.data.repository.ProductoRepositoryImpl
import com.example.labx.domain.model.Producto
import com.example.labx.ui.viewmodel.ProductoViewModel
import com.example.labx.ui.viewmodel.ProductoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    productoRepository: ProductoRepositoryImpl,
    carritoRepository: CarritoRepository,
    onProductoClick: (Int) -> Unit,
    onCarritoClick: () -> Unit,
    onRegistroClick: () -> Unit,
    onVolverPortada: () -> Unit,
    onJuegosClick: () -> Unit
) {
    val viewModel: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(productoRepository)
    )
    val uiState by viewModel.uiState.collectAsState()

    var textoBusqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf<String?>(null) }

    val productosFiltrados = remember(uiState.productos, textoBusqueda, categoriaSeleccionada) {
        uiState.productos.filter { producto ->
            val coincideTexto = textoBusqueda.isBlank() ||
                    producto.nombre.contains(textoBusqueda, ignoreCase = true) ||
                    producto.descripcion.contains(textoBusqueda, ignoreCase = true)
            val coincideCategoria = categoriaSeleccionada == null ||
                    producto.categoria == categoriaSeleccionada
            coincideTexto && coincideCategoria
        }
    }

    val categorias = remember(uiState.productos) {
        uiState.productos.map { it.categoria }.distinct().sorted()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Tienda Gamer",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolverPortada) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onJuegosClick) {
                        Icon(Icons.Default.Star, contentDescription = "Juegos Gratis", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onRegistroClick) {
                        Icon(Icons.Default.Person, contentDescription = "Registro")
                    }
                    IconButton(onClick = onCarritoClick) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.estaCargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.cargarProductos() }) { Text("Reintentar") }
                    }
                }
                uiState.productos.isEmpty() -> {
                    Text("No hay productos disponibles", modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Buscador
                        OutlinedTextField(
                            value = textoBusqueda,
                            onValueChange = { textoBusqueda = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            placeholder = { Text("Buscar...") },
                            leadingIcon = { Icon(Icons.Default.Search, "") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            ),
                            singleLine = true
                        )

                        // Categorías
                        if (categorias.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterChip(
                                    selected = categoriaSeleccionada == null,
                                    onClick = { categoriaSeleccionada = null },
                                    label = { Text("Todos") }
                                )
                                categorias.forEach { categoria ->
                                    FilterChip(
                                        selected = categoriaSeleccionada == categoria,
                                        onClick = { categoriaSeleccionada = if (categoriaSeleccionada == categoria) null else categoria },
                                        label = { Text(categoria) }
                                    )
                                }
                            }
                        }

                        // Lista
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(productosFiltrados) { producto ->
                                ProductoCard(producto = producto, onClick = { onProductoClick(producto.id) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: Producto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current
            val imageResId = context.resources.getIdentifier(producto.imagenUrl, "drawable", context.packageName)

            Card(
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(if (imageResId != 0) imageResId else producto.imagenUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = producto.nombre,
                    modifier = Modifier.size(90.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = producto.categoria.uppercase(),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = producto.precioFormateado(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                if (producto.hayStock) {
                    Surface(color = Color.Green, shape = CircleShape, modifier = Modifier.size(10.dp)) {}
                    Text("Stock: ${producto.stock}", fontSize = 10.sp, color = Color.Gray)
                } else {
                    Surface(color = MaterialTheme.colorScheme.error, shape = CircleShape, modifier = Modifier.size(10.dp)) {}
                    Text("Agotado", fontSize = 10.sp, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}