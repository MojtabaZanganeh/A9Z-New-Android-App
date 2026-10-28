package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFF00E5FF),
    onPrimary = Color(0xFF001E22),
    secondary = AccentTeal,
    onSecondary = Color(0xFF002010),
    tertiary = GlowPurple,
    background = Color(0xFF0A0F1D),
    surface = Color(0xFF151D30),
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFF1F293F),
    onSurfaceVariant = TextSecondary,
    error = ErrorRed
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Color(0xFF0284C7),
    onPrimary = Color.White,
    secondary = Color(0xFF0D9488),
    onSecondary = Color.White,
    tertiary = Color(0xFF7C3AED),
    background = Color(0xFFF1F5F9),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    error = Color(0xFFDC2626)
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isAppDarkTheme,
  content: @Composable () -> Unit,
) {
  isAppDarkTheme = darkTheme
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as? Activity)?.window
      if (window != null) {
        val insetsController = WindowCompat.getInsetsController(window, view)
        insetsController.isAppearanceLightStatusBars = !darkTheme
        insetsController.isAppearanceLightNavigationBars = !darkTheme
      }
    }
  }

  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography) {
    CompositionLocalProvider(
      LocalTextStyle provides Typography.bodyLarge
    ) {
      content()
    }
  }
}
