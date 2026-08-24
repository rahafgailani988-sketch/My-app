package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GeometricLightColorScheme = lightColorScheme(
    primary = GeoPrimary,
    onPrimary = GeoOnPrimary,
    primaryContainer = GeoPrimaryContainer,
    onPrimaryContainer = GeoOnPrimaryContainer,
    secondary = GeoSecondary,
    onSecondary = GeoOnSecondary,
    secondaryContainer = GeoSecondaryContainer,
    onSecondaryContainer = GeoOnSecondaryContainer,
    tertiary = GeoTertiary,
    onTertiary = GeoOnTertiary,
    tertiaryContainer = GeoTertiaryContainer,
    onTertiaryContainer = GeoOnTertiaryContainer,
    background = GeoBg,
    onBackground = GeoTextPrimary,
    surface = GeoSurface,
    onSurface = GeoTextPrimary,
    surfaceVariant = GeoSurfaceVariant,
    onSurfaceVariant = GeoTextSecondary,
    outline = GeoBorder,
    outlineVariant = GeoBorderSubtle,
    error = GeoError,
    onError = Color.White,
    errorContainer = GeoErrorContainer,
    onErrorContainer = GeoError
)

private val GeometricDarkColorScheme = darkColorScheme(
    primary = GeoPrimaryContainer,
    onPrimary = GeoOnPrimaryContainer,
    primaryContainer = GeoPrimary,
    onPrimaryContainer = GeoPrimaryContainer,
    secondary = GeoSecondaryContainer,
    onSecondary = GeoOnSecondaryContainer,
    secondaryContainer = GeoSecondary,
    onSecondaryContainer = GeoSecondaryContainer,
    tertiary = GeoTertiaryContainer,
    onTertiary = GeoOnTertiaryContainer,
    background = Color(0xFF1D1B20),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF2B2930),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = GeoBorder,
    error = GeoError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Geometric Balance light aesthetic is the primary design theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) GeometricDarkColorScheme else GeometricLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

