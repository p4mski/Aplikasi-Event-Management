package com.example.uasmobileprogram.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val PinkColorScheme = lightColorScheme(
    primary = PinkTopBar,
    onPrimary = PinkOnPrimary,

    background = PinkLightBackground,
    onBackground = PinkOnBackground,

    surface = PinkCard,
    onSurface = PinkOnCard
)

@Composable
fun UASMobileProgramTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PinkColorScheme,
        typography = Typography,
        content = content
    )
}
