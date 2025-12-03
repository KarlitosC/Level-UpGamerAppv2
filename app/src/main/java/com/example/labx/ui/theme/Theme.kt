package com.example.labx.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Definimos el esquema de colores oscuros
private val GamerColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = BackgroundDark, // Texto sobre color neon
    secondary = NeonPurple,
    onSecondary = TextWhite,
    background = BackgroundDark,
    onBackground = TextWhite,
    surface = SurfaceDark,
    onSurface = TextWhite,
    error = ErrorRed
)

@Composable
fun LabXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+ but we turn it off
    // to keep our Gamer look consistent
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = GamerColorScheme // Usamos siempre el esquema Gamer

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Pintamos la barra de estado del color de fondo
            window.statusBarColor = colorScheme.background.toArgb()
            // Iconos de la barra blancos
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}