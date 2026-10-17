package io.github.aayushhks.campuseventexplorer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = ScarletPrimaryLight,
    onPrimary = ScarletOnPrimaryLight,
    primaryContainer = ScarletContainerLight,
    onPrimaryContainer = ScarletOnContainerLight,
    secondary = SlateSecondaryLight,
    background = BackgroundLight,
    surface = BackgroundLight,
    surfaceVariant = SurfaceVariantLight,
)

private val DarkColors = darkColorScheme(
    primary = ScarletPrimaryDark,
    onPrimary = ScarletOnPrimaryDark,
    primaryContainer = ScarletContainerDark,
    onPrimaryContainer = ScarletOnContainerDark,
    secondary = SlateSecondaryDark,
    background = BackgroundDark,
    surface = BackgroundDark,
    surfaceVariant = SurfaceVariantDark,
)

/** Dynamic colour is not used, so the palette above is what appears on screen. */
@Composable
fun CampusEventTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = CampusTypography,
        content = content,
    )
}
