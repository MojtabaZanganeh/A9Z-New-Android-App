package com.example.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

var isAppDarkTheme by mutableStateOf(true)

val MidnightBg: Color
    get() = if (isAppDarkTheme) Color(0xFF0A0F1D) else Color(0xFFF1F5F9)

val MidnightSurface: Color
    get() = if (isAppDarkTheme) Color(0xFF151D30) else Color(0xFFFFFFFF)

val MidnightSurfaceVariant: Color
    get() = if (isAppDarkTheme) Color(0xFF1F293F) else Color(0xFFF8FAFC)

val ActiveNeonCyan: Color
    get() = if (isAppDarkTheme) Color(0xFF00E5FF) else Color(0xFF0284C7)

val ThemeTextPrimary: Color
    get() = if (isAppDarkTheme) Color.White else Color(0xFF0F172A)

val ThemeTextSecondary: Color
    get() = if (isAppDarkTheme) Color(0xFF9CA3AF) else Color(0xFF475569)

val ThemeOnAccent: Color
    get() = if (isAppDarkTheme) Color.Black else Color.White

val ThemeCodeBoxBg: Color
    get() = if (isAppDarkTheme) Color.Black.copy(alpha = 0.35f) else Color(0xFFF1F5F9)

val ThemeCardBorder: Color
    get() = if (isAppDarkTheme) Color.White.copy(alpha = 0.08f) else Color(0xFFCBD5E1)

val ThemeSuccessGreen: Color
    get() = if (isAppDarkTheme) Color(0xFF00E676) else Color(0xFF16A34A)

val ThemeWarningAmber: Color
    get() = if (isAppDarkTheme) Color(0xFFFFB300) else Color(0xFFD97706)

val ThemeErrorRed: Color
    get() = if (isAppDarkTheme) Color(0xFFFF5252) else Color(0xFFDC2626)

val ThemePurpleAccent: Color
    get() = if (isAppDarkTheme) Color(0xFFB388FF) else Color(0xFF7C3AED)

val AccentTeal = Color(0xFF00FFA3)
val GlowPurple = Color(0xFF9F5FFF)
val ErrorRed = Color(0xFFFF4861)
val TextPrimary = Color(0xFFF9FAFB)
val TextSecondary = Color(0xFF9CA3AF)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
