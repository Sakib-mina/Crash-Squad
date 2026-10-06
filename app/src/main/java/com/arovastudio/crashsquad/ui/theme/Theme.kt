package com.arovastudio.crashsquad.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TropicalSunYellow,
    secondary = TropicalOrange,
    tertiary = GemPurpleLight,
    background = Color(0xFF102A43),
    surface = Color(0xFF1F3A52),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    tertiary = TertiaryColor,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = OnPrimaryColor,
    onSecondary = OnSecondaryColor,
    onTertiary = Color.White,
    onBackground = Color(0xFF263238),
    onSurface = Color(0xFF263238)
)

@Composable
fun CrashSquadTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
