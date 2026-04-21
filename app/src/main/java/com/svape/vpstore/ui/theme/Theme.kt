package com.svape.vpstore.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object InterColors {
    val DarkBg       = Color(0xFF0D1B2A)
    val DarkSurface  = Color(0xFF152233)
    val DarkCard     = Color(0xFF1C2E42)
    val DarkBorder   = Color(0xFF253A52)

    val Orange       = Color(0xFFFF5C00)
    val OrangeLight  = Color(0xFFFF7A2E)
    val OrangeDim    = Color(0xFF3D1800)

    val White        = Color(0xFFFFFFFF)
    val WhiteSmoke   = Color(0xFFF0F4F8)
    val GrayLight    = Color(0xFFD0D8E0)
    val GrayMedium   = Color(0xFF8A9BB0)
    val GrayDark     = Color(0xFF3A4A5C)

    val GreenSuccess = Color(0xFF00C896)
    val AmberWarn    = Color(0xFFFFB800)
    val RedError     = Color(0xFFFF3B3B)
    val BlueInfo     = Color(0xFF2196F3)
}

private val DarkScheme = darkColorScheme(
    primary            = InterColors.Orange,
    onPrimary          = InterColors.White,
    primaryContainer   = InterColors.OrangeDim,
    onPrimaryContainer = InterColors.OrangeLight,
    secondary          = InterColors.GrayMedium,
    onSecondary        = InterColors.White,
    background         = InterColors.DarkBg,
    onBackground       = InterColors.White,
    surface            = InterColors.DarkSurface,
    onSurface          = InterColors.White,
    surfaceVariant     = InterColors.DarkCard,
    onSurfaceVariant   = InterColors.GrayLight,
    outline            = InterColors.DarkBorder,
    error              = InterColors.RedError,
    onError            = InterColors.White,
)

@Composable
fun VPStoreTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkScheme,
        typography  = InterTypography,
        content     = content
    )
}