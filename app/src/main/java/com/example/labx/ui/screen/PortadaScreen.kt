package com.example.labx.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labx.R // Asegúrate de que este R sea el de tu paquete

@Composable
fun PortadaScreen(
    onEntrarClick: () -> Unit,      // Ir a la API
    onLocalClick: () -> Unit,       // Ir al Local (Nueva función)
    onAdminClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Fondo oscuro gamer
    ) {
        // Imagen de fondo (opcional, si tienes una)
        // Image(...)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // LOGO (Usa tu logo actual)
            Image(
                painter = painterResource(id = R.drawable.logo_pagina), // Tu logo morado
                contentDescription = "Logo StingCommerce",
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 32.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "BIENVENIDO A",
                fontSize = 20.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Light
            )

            Text(
                text = "LEVEL-UP GAMER",
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            Button(
                onClick = onEntrarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "TIENDA", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedButton(
                onClick = onLocalClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
                border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "TIENDA(LOCAL)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}