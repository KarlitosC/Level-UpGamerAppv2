package com.example.labx.ui.screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labx.R

@Composable
fun PortadaScreen(
    onEntrarClick: () -> Unit,
    onLocalClick: () -> Unit,
    onAdminClick: () -> Unit,
    onCamaraClick: () -> Unit,
    avatarBitmap: Bitmap?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            if (avatarBitmap != null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(250.dp)
                        .padding(bottom = 32.dp)
                ) {
                    Image(
                        bitmap = avatarBitmap.asImageBitmap(),
                        contentDescription = "Avatar Usuario",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(220.dp)
                            .clip(CircleShape)
                    )
                }
            } else {

                Image(
                    painter = painterResource(id = R.drawable.logo_pagina),
                    contentDescription = "Logo LevelUp Gamer",
                    modifier = Modifier
                        .size(250.dp)
                        .padding(bottom = 32.dp),
                    contentScale = ContentScale.Fit
                )
            }
            // -------------------------------------------------------

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
                Text(text = "TIENDA ONLINE", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                Text(text = "TIENDA (LOCAL)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = onCamaraClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF03DAC5),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "CREAR AVATAR", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}