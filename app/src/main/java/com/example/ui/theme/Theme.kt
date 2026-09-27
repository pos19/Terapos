package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = OrthoTealPrimaryDark,
    onPrimary = Color(0xFF042F2E),
    primaryContainer = OrthoTealDark,
    onPrimaryContainer = OrthoTealLight,
    secondary = OrthoCoralSecondaryDark,
    onSecondary = Color(0xFF431407),
    secondaryContainer = Color(0xFF7C2D12),
    onSecondaryContainer = OrthoCoralContainer,
    tertiary = OrthoAmberTertiaryDark,
    onTertiary = Color(0xFF451A03),
    tertiaryContainer = Color(0xFF78350F),
    onTertiaryContainer = OrthoAmberLight,
    background = OrthoBackgroundDark,
    onBackground = Color(0xFFF1F5F9),
    surface = OrthoSurfaceDark,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = OrthoSurfaceVariantDark,
    onSurfaceVariant = Color(0xFFCBD5E1)
)

private val LightColorScheme = lightColorScheme(
    primary = OrthoTealPrimary,
    onPrimary = Color.White,
    primaryContainer = OrthoTealContainer,
    onPrimaryContainer = OnOrthoTealContainer,
    secondary = OrthoCoralSecondary,
    onSecondary = Color.White,
    secondaryContainer = OrthoCoralContainer,
    onSecondaryContainer = OnOrthoCoralContainer,
    tertiary = OrthoAmberTertiary,
    onTertiary = Color.White,
    tertiaryContainer = OrthoAmberLight,
    onTertiaryContainer = OnOrthoAmberContainer,
    background = OrthoBackgroundLight,
    onBackground = OrthoOnSurfaceLight,
    surface = OrthoSurfaceLight,
    onSurface = OrthoOnSurfaceLight,
    surfaceVariant = OrthoSurfaceVariantLight,
    onSurfaceVariant = OrthoOnSurfaceVariantLight
)

val OrthoShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Force RTL direction for authentic Arabic orthophonic UI
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = OrthoShapes,
            content = content
        )
    }
}
