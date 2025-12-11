package com.example.uasmobileprogram.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PinkDarkPrimary,
    onPrimary = PinkDarkOnPrimary,
    primaryContainer = PinkDarkPrimaryContainer,
    onPrimaryContainer = Color.White,

    secondary = PinkDarkSecondary,
    onSecondary = PinkDarkOnSecondary,

    background = PinkDarkBackground,
    onBackground = PinkDarkOnBackground,

    surface = PinkDarkSurface,
    onSurface = PinkDarkOnSurface,
)

@Composable
fun UASMobileProgramTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
