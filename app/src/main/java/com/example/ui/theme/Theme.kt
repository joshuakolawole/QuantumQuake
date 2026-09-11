package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
  primary = QuakePlasmaRed,
  onPrimary = QuakeTextWhite,
  primaryContainer = QuakePlasmaRedMuted,
  onPrimaryContainer = QuakePlasmaRed,
  secondary = QuakeCobalt,
  onSecondary = QuakeTextWhite,
  secondaryContainer = QuakeCobaltMuted,
  onSecondaryContainer = QuakeCyan,
  tertiary = QuakeCyan,
  onTertiary = CarbonBackground,
  background = CarbonBackground,
  onBackground = QuakeTextWhite,
  surface = CarbonSurface,
  onSurface = QuakeTextWhite,
  surfaceVariant = CarbonSurfaceVariant,
  onSurfaceVariant = QuakeTextMuted,
  outline = CarbonBorder,
  outlineVariant = CarbonBorderSubtle,
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Quake Motion Lab is an intentional dark-theme command center
  dynamicColor: Boolean = false, // Preserve high-fidelity carbon/plasma brand colors
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content
  )
}

