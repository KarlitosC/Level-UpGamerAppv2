package com.example.labx.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.labx.ui.viewmodel.JuegosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuegosGratisScreen(
    viewModel: JuegosViewModel,
    onVolverClick: () -> Unit
) {
    val juegos by viewModel.juegos.collectAsState()
    val cargando by viewModel.cargando.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Juegos Gratis (API Externa)") },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A2E), // Un azul oscuro gamer
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF0F0F1A)) // Fondo oscuro
        ) {
            if (cargando) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(juegos) { juego ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF16213E)),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Column {
                                // Imagen del juego desde internet
                                AsyncImage(
                                    model = juego.imagen,
                                    contentDescription = juego.title,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = juego.title,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${juego.genre} • ${juego.platform}",
                                        fontSize = 14.sp,
                                        color = Color(0xFF00ADB5) // Un color cyan neón
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = juego.descripcion,
                                        fontSize = 12.sp,
                                        color = Color.LightGray,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}