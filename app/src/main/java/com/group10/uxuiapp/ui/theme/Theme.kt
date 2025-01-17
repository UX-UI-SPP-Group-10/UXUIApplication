package com.group10.uxuiapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    background = Black,
    surface = White,
    onSurface = Black

)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0D47A1),       // Blue-grey (primary)
    onPrimary = Color(0xFFFFFFFF),    // White (text on primary)

    secondary = Color(0xFF90CAF9),    // Soft blue (secondary)
    onSecondary = Color(0xFFFFFFFF), // White (text on secondary)

    tertiary = Color(0xFFBBDEFB),     // Deep blue-grey (tertiary)
    onTertiary = Color(0xFFFFFFFF),  // White (text on tertiary)

    background = Color(0xFFD5F0FF),   // White background
    onBackground = Color(0xFF384959), // Deep blue-grey (text on background)

    surface = Color(0xFF2196F3),      // Slightly off-white surface
    onSurface = Color(0xFF000000),   // Deep blue-grey (text on surface)
)


@Composable
fun UXUIApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        LightColorScheme    // Forced lightmode for now
        //DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
