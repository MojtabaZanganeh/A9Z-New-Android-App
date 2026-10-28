package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

// Ravi Variable Font family with explicit variation settings for each weight coordinate
@OptIn(ExperimentalTextApi::class)
val RaviFontFamily = FontFamily(
    Font(
        resId = R.font.ravi_vf,
        weight = FontWeight.Thin,
        style = FontStyle.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(100)
        )
    ),
    Font(
        resId = R.font.ravi_vf,
        weight = FontWeight.ExtraLight,
        style = FontStyle.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(250)
        )
    ),
    Font(
        resId = R.font.ravi_vf,
        weight = FontWeight.Light,
        style = FontStyle.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(300)
        )
    ),
    Font(
        resId = R.font.ravi_vf,
        weight = FontWeight.Normal,
        style = FontStyle.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400)
        )
    ),
    Font(
        resId = R.font.ravi_vf,
        weight = FontWeight.Medium,
        style = FontStyle.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(500)
        )
    ),
    Font(
        resId = R.font.ravi_vf,
        weight = FontWeight.SemiBold,
        style = FontStyle.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(600)
        )
    ),
    Font(
        resId = R.font.ravi_vf,
        weight = FontWeight.Bold,
        style = FontStyle.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(700)
        )
    ),
    Font(
        resId = R.font.ravi_vf,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(800)
        )
    ),
    Font(
        resId = R.font.ravi_vf,
        weight = FontWeight.Black,
        style = FontStyle.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(950)
        )
    )
)

// Primary typography using Ravi Variable Font family
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = RaviFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
