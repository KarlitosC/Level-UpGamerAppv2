package com.example.labx.ui.screen

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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    esModoLocal: Boolean = false, // <--- NUEVO: Para saber qué cargar
    onProductoClick: (Int) -> Unit,
    onCarritoClick: () -> Unit,
    onRegistroClick: () -> Unit,
    onVolverPortada: () -> Unit,
    onJuegosClick: () -> Unit
) {
    val viewModel: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(productoRepository)
    )

    // --- LÓGICA DE CARGA INTELIGENTE ---
    // Si es modo local, carga BD. Si no, carga API.
    LaunchedEffect(esModoLocal) {
        if (esModoLocal) {
            viewModel.cargarProductosLocales()
        } else {
            viewModel.cargarProductos()
        }
    }
    // -----------------------------------

    val uiState by viewModel.uiState.collectAsState()

    // Lógica de búsqueda y filtrado
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

    // Obtener categorías automáticamente
    val categorias = remember(uiState.productos) {
        uiState.productos.map { it.categoria }.distinct().sorted()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Título dinámico
                    Text(
                        text = if (esModoLocal) "Inventario Local" else "Catálogo Online",
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
                    // 1. BOTÓN "GUARDAR TODO" (Solo visible en modo Online)
                    if (!esModoLocal) {
                        IconButton(onClick = {
                            // Llama a la función del ViewModel que creaste en el paso anterior
                            viewModel.guardarTodoEnLocal()
                        }) {
                            // Usamos un icono de flecha hacia abajo (Descarga)
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Descargar Catálogo",
                                tint = MaterialTheme.colorScheme.primary // Color morado neón
                            )
                        }
                    }

                    // 2. BOTÓN CARRITO (Siempre visible)
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
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Mensaje de error personalizado según el modo
                        Text(
                            text = if (esModoLocal) "Inventario vacío o error" else "Error de conexión",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if(esModoLocal) viewModel.cargarProductosLocales()
                                else viewModel.cargarProductos()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
                uiState.productos.isEmpty() -> {
                    Text(
                        text = if (esModoLocal) "No has guardado productos aún" else "No hay productos disponibles",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Buscador estilo Gamer
                        OutlinedTextField(
                            value = textoBusqueda,
                            onValueChange = { textoBusqueda = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            placeholder = { Text("Buscar hardware...") },
                            leadingIcon = { Icon(Icons.Default.Search, "") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                cursorColor = MaterialTheme.colorScheme.secondary
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Categorías (Chips)
                        if (categorias.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterChip(
                                    selected = categoriaSeleccionada == null,
                                    onClick = { categoriaSeleccionada = null },
                                    label = { Text("Todos") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                        selectedLabelColor = Color.Black
                                    )
                                )
                                categorias.forEach { categoria ->
                                    FilterChip(
                                        selected = categoriaSeleccionada == categoria,
                                        onClick = { categoriaSeleccionada = if (categoriaSeleccionada == categoria) null else categoria },
                                        label = { Text(categoria) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                            selectedLabelColor = Color.Black
                                        )
                                    )
                                }
                            }
                        }

                        // Lista de Productos
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

// La función ProductoCard la dejé igual porque está perfecta
@Composable
fun ProductoCard(producto: Producto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current

            // Imagen con borde neón sutil
            Card(
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(producto.imagenUrl)
                        .crossfade(true)
                        .error(android.R.drawable.ic_menu_report_image)
                        .build(),
                    contentDescription = producto.nombre,
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = producto.categoria.uppercase(),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = producto.precioFormateado(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                if (producto.hayStock) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Disponible",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Surface(color = MaterialTheme.colorScheme.error, shape = CircleShape, modifier = Modifier.size(10.dp)) {}
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Agotado", fontSize = 10.sp, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}