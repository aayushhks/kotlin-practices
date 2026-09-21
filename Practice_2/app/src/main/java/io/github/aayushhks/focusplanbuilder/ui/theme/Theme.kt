package io.github.aayushhks.focusplanbuilder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = IndigoPrimaryLight,
    onPrimary = IndigoOnPrimaryLight,
    primaryContainer = IndigoContainerLight,
    secondary = ClaySecondaryLight,
    background = BackgroundLight,
    surfaceVariant = SurfaceVariantLight,
)

private val DarkColors = darkColorScheme(
    primary = IndigoPrimaryDark,
    onPrimary = IndigoOnPrimaryDark,
    primaryContainer = IndigoContainerDark,
    secondary = ClaySecondaryDark,
    background = BackgroundDark,
    surfaceVariant = SurfaceVariantDark,
)

/** Dynamic colour is not used, so the palette above is what appears on screen. */
@Composable
fun FocusPlanBuilderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = FocusTypography,
        content = content,
    )
}
