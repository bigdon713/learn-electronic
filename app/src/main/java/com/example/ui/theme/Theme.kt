package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = SunburstGold,
    onPrimary = Color.White,
    primaryContainer = SunburstSoft,
    onPrimaryContainer = WarmCharcoal,
    secondary = SkyCerulean,
    onSecondary = Color.White,
    secondaryContainer = SkySoft,
    onSecondaryContainer = WarmCharcoal,
    tertiary = MeadowEmerald,
    onTertiary = Color.White,
    tertiaryContainer = MeadowSoft,
    onTertiaryContainer = WarmCharcoal,
    background = WarmCream,
    onBackground = WarmCharcoal,
    surface = WarmPaper,
    onSurface = WarmCharcoal,
    surfaceVariant = WarmSand,
    onSurfaceVariant = WarmSlate,
    outline = WarmBorder,
    error = CoralBerry,
    onError = Color.White,
    errorContainer = CoralSoft,
    onErrorContainer = WarmCharcoal,
)

private val DarkColorScheme = darkColorScheme(
    primary = SunburstGold,
    onPrimary = NightSky,
    primaryContainer = Color(0xFF4B3208),
    onPrimaryContainer = SunburstSoft,
    secondary = Color(0xFF4CC9F0),
    onSecondary = NightSky,
    secondaryContainer = Color(0xFF14384B),
    onSecondaryContainer = SkySoft,
    tertiary = Color(0xFF52B788),
    onTertiary = NightSky,
    tertiaryContainer = Color(0xFF143C36),
    onTertiaryContainer = MeadowSoft,
    background = NightSky,
    onBackground = NightText,
    surface = NightCard,
    onSurface = NightText,
    surfaceVariant = NightCardElevated,
    onSurfaceVariant = NightMuted,
    outline = Color(0xFF39425E),
    error = Color(0xFFFF856D),
    onError = NightSky,
    errorContainer = Color(0xFF4C2119),
    onErrorContainer = CoralSoft,
)

val SunnyLearnShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
)

@Composable
fun SunnyLearnTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SunnyLearnTypography,
        shapes = SunnyLearnShapes,
        content = content,
    )
}
