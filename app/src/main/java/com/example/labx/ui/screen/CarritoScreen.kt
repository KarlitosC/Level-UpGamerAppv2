package com.example.labx.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.labx.data.repository.CarritoRepository
import com.example.labx.domain.model.ItemCarrito
import kotlinx.coroutines.launch
import java.util.Date // Import necesario para la fecha de la boleta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    carritoRepository: CarritoRepository,
    onVolverClick: () -> Unit,
    onProductoClick: (Int) -> Unit
) {
    // Observar items del carrito
    val itemsCarrito by carritoRepository.obtenerCarrito().collectAsState(initial = emptyList())
    val total by carritoRepository.obtenerTotal().collectAsState(initial = 0)
    val scope = rememberCoroutineScope()

    // --- ESTADOS PARA LA BOLETA ---
    var mostrarBoleta by remember { mutableStateOf(false) }
    var listaComprada by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }
    var totalPagado by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito (${itemsCarrito.size})") },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (itemsCarrito.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                scope.launch { carritoRepository.vaciarCarrito() }
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Vaciar", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        },
        bottomBar = {
            // --- NUEVA BARRA INFERIOR CON BOTÓN DE PAGO ---
            if (itemsCarrito.isNotEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 16.dp,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total a Pagar:", fontSize = 18.sp, color = Color.Gray)
                            Text(
                                text = formatearPrecio(total),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                // 1. Guardamos los datos actuales para la boleta
                                listaComprada = itemsCarrito
                                totalPagado = total

                                // 2. Vaciamos el carrito y mostramos la alerta
                                scope.launch {
                                    carritoRepository.vaciarCarrito()
                                    mostrarBoleta = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("PAGAR AHORA", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (itemsCarrito.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🛒", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Tu carrito está vacío", fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onVolverClick) { Text("Ir a comprar") }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(itemsCarrito) { item ->
                        CarritoItemCard(
                            item = item,
                            onCantidadChange = { nuevaCantidad ->
                                scope.launch {
                                    carritoRepository.modificarCantidad(item.producto.id, nuevaCantidad)
                                }
                            },
                            onEliminarClick = {
                                scope.launch {
                                    carritoRepository.eliminarProducto(item.producto.id)
                                }
                            },
                            onClick = { onProductoClick(item.producto.id) }
                        )
                    }
                }
            }

            // --- VENTANA EMERGENTE: BOLETA ---
            if (mostrarBoleta) {
                AlertDialog(
                    onDismissRequest = { mostrarBoleta = false },
                    icon = {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50), // Verde éxito
                            modifier = Modifier.size(48.dp)
                        )
                    },
                    title = {
                        Text("¡Compra Exitosa!", textAlign = TextAlign.Center)
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFF8E1), RoundedCornerShape(8.dp)) // Fondo tipo papel recibo
                                .padding(16.dp)
                        ) {
                            Text(
                                "BOLETA ELECTRÓNICA",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.Black
                            )
                            Text(
                                "Fecha: ${Date()}",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Black)

                            // Lista de items comprados (Scrollable si es muy larga)
                            LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                                items(listaComprada) { item ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "${item.cantidad}x ${item.producto.nombre.take(15)}",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 12.sp,
                                            color = Color.Black
                                        )
                                        Text(
                                            formatearPrecio(item.subtotal),
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 12.sp,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }

                            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Black)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("TOTAL", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color.Black)
                                Text(formatearPrecio(totalPagado), fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color.Black)
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { mostrarBoleta = false },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Aceptar")
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                )
            }
        }
    }
}

@Composable
fun CarritoItemCard(
    item: ItemCarrito,
    onCantidadChange: (Int) -> Unit,
    onEliminarClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current
            // Intentar cargar imagen desde recursos (para productos locales antiguos) o URL
            val imageResId = context.resources.getIdentifier(
                item.producto.imagenUrl,
                "drawable",
                context.packageName
            )

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(if (imageResId != 0) imageResId else item.producto.imagenUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = item.producto.nombre,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = item.producto.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Precio: ${formatearPrecio(item.producto.precio)}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { if (item.cantidad > 1) onCantidadChange(item.cantidad - 1) },
                        modifier = Modifier.size(32.dp),
                        enabled = item.cantidad > 1
                    ) {
                        Icon(
                            Icons.Default.Delete, // O remove si prefieres
                            contentDescription = "Disminuir",
                            tint = if (item.cantidad > 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }

                    Text(
                        text = "${item.cantidad}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .widthIn(min = 30.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )

                    IconButton(
                        onClick = { onCantidadChange(item.cantidad + 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar", tint = MaterialTheme.colorScheme.primary)
                    }
                }

                Text(
                    text = "Subtotal: ${formatearPrecio(item.subtotal)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onEliminarClick) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

fun formatearPrecio(precio: Int): String {
    val format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("es", "CL"))
    format.maximumFractionDigits = 0 // CLP no usa centavos
    return format.format(precio)
}