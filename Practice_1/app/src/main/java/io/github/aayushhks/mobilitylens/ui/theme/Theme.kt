package io.github.aayushhks.mobilitylens.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = TealPrimaryLight,
    onPrimary = TealOnPrimaryLight,
    primaryContainer = TealContainerLight,
    secondary = AmberSecondaryLight,
    background = BackgroundLight,
    surfaceVariant = SurfaceVariantLight,
)

private val DarkColors = darkColorScheme(
    primary = TealPrimaryDark,
    onPrimary = TealOnPrimaryDark,
    primaryContainer = TealContainerDark,
    secondary = AmberSecondaryDark,
    background = BackgroundDark,
    surfaceVariant = SurfaceVariantDark,
)

/**
 * Dynamic colour is intentionally not used, so the custom palette above is the
 * one that actually appears on screen on Android 12 and later.
 */
@Composable
fun MobilityLensTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = MobilityTypography,
        content = content,
    )
}
