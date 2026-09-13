package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CairoNoirColorScheme = darkColorScheme(
    primary = AmberGold,
    onPrimary = Color.Black,
    primaryContainer = AmberGoldDim,
    onPrimaryContainer = Color.White,
    secondary = CrimsonThread,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4A1010),
    onSecondaryContainer = CrimsonGlow,
    tertiary = CyanTerminal,
    onTertiary = Color.Black,
    background = NoirObsidian,
    onBackground = TextPrimary,
    surface = NoirDarkCard,
    onSurface = TextPrimary,
    surfaceVariant = NoirSurface,
    onSurfaceVariant = TextSecondary,
    outline = Color(0xFF374151)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = CairoNoirColorScheme,
        typography = Typography,
        content = content
    )
}

