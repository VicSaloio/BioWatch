package br.com.fiap.biowatch.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BioWatchColorScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = BrownSecondary,
    tertiary = OliveTertiary,

    background = BackgroundSand,
    surface = CreamSurface,

    onPrimary = CreamSurface,
    onSecondary = CreamSurface,
    onTertiary = GreenPrimary,

    onBackground = GreenPrimary,
    onSurface = GreenPrimary,

    error = ErrorRed,
    onError = CreamSurface,

    surfaceVariant = CreamSurface,
    onSurfaceVariant = TextSecondary
)

@Composable
fun BioWatchTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BioWatchColorScheme,
        content = content
    )
}