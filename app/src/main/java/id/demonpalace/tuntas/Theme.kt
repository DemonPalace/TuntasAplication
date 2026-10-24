package id.demonpalace.tuntas

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * Calm, flat palette: warm off-white page, white cards without shadows, one deep blue accent.
 * Subject colors appear only as small accents. Names are kept from the first design so screens
 * did not need renaming: violet is the primary accent, pink the secondary (orange).
 */
object C {
    /** Set by [JalurTheme]; every color below reads it, so a switch recomposes the whole app. */
    var dark by mutableStateOf(false)

    private fun pick(light: Long, night: Long) = Color(if (dark) night else light)

    val bg get() = pick(0xFFF6F5F1, 0xFF121318)          // page
    val surface get() = pick(0xFFFFFFFF, 0xFF1C1E24)     // cards
    val surface2 get() = pick(0xFFEEEDE8, 0xFF272A32)    // inset fills, inactive chips
    val line get() = pick(0xFFE4E2DC, 0xFF30333C)        // 1dp hairlines
    val text get() = pick(0xFF16181D, 0xFFECEDEF)
    val muted get() = pick(0xFF666A72, 0xFFA2A6AE)
    val faded get() = pick(0xFFA3A6AC, 0xFF6C7079)
    val violet get() = pick(0xFF3247D1, 0xFF5B6CF0)      // primary accent: CTA, progress, selection
    val accentWash get() = pick(0xFFE6E9FB, 0xFF262C52)
    val blue get() = violet
    val pink get() = pick(0xFFE8710A, 0xFFF08A2E)
    val good get() = pick(0xFF15924A, 0xFF34B86C)
    val goodWash get() = pick(0xFFDDF5E6, 0xFF173826)
    val bad get() = pick(0xFFD62C2C, 0xFFEE5B5B)
    val gold get() = pick(0xFFE0A100, 0xFFF0B429)
    val ink get() = text
    val warnWash get() = pick(0xFFFFF3E0, 0xFF3A2A12)    // pitfall boxes
    val paper get() = pick(0xFFFFFCF5, 0xFF1E1C19)       // book page
    val paperEdge get() = pick(0xFFEDE6D6, 0xFF34302A)
    val paperInk get() = pick(0xFF34302A, 0xFFE9E2D3)
    val paperInkSoft get() = pick(0xFF6F675A, 0xFFB3AA99)
    /** Sticker rim behind Tuti in dark mode, so the dark outline stays visible. */
    val mascotRim get() = pick(0x00000000, 0xFF555C70)
}

@OptIn(ExperimentalTextApi::class)
private fun jakarta(w: Int) = Font(
    R.font.jakarta, FontWeight(w),
    variationSettings = FontVariation.Settings(FontVariation.weight(w)),
)

@OptIn(ExperimentalTextApi::class)
/** Plus Jakarta Sans (OFL), an Indonesian-made typeface; variable weight 200 to 800. */
val Jakarta = FontFamily(jakarta(400), jakarta(500), jakarta(600), jakarta(700), jakarta(800))

private fun scheme() = (if (C.dark) darkColorScheme(
    primary = C.violet,
    onPrimary = Color.White,
    secondary = C.pink,
    background = C.bg,
    onBackground = C.text,
    surface = C.surface,
    onSurface = C.text,
    surfaceVariant = C.surface,
    onSurfaceVariant = C.muted,
    outline = C.line,
    error = C.bad,
) else lightColorScheme(
    primary = C.violet,
    onPrimary = Color.White,
    secondary = C.pink,
    background = C.bg,
    onBackground = C.text,
    surface = C.surface,
    onSurface = C.text,
    surfaceVariant = C.surface,
    onSurfaceVariant = C.muted,
    outline = C.line,
    error = C.bad,
))

private val type = Typography(
    headlineLarge = TextStyle(fontFamily = Jakarta, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 36.sp, letterSpacing = (-0.03).em),
    headlineMedium = TextStyle(fontFamily = Jakarta, fontSize = 23.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 29.sp, letterSpacing = (-0.02).em),
    titleLarge = TextStyle(fontFamily = Jakarta, fontSize = 19.sp, fontWeight = FontWeight.Bold, lineHeight = 25.sp, letterSpacing = (-0.01).em),
    titleMedium = TextStyle(fontFamily = Jakarta, fontSize = 16.sp, fontWeight = FontWeight.Bold, lineHeight = 22.sp),
    titleSmall = TextStyle(fontFamily = Jakarta, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontFamily = Jakarta, fontSize = 16.sp, fontWeight = FontWeight.Normal, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = Jakarta, fontSize = 14.sp, fontWeight = FontWeight.Normal, lineHeight = 20.sp),
    bodySmall = TextStyle(fontFamily = Jakarta, fontSize = 12.sp, fontWeight = FontWeight.Normal, lineHeight = 17.sp),
    labelLarge = TextStyle(fontFamily = Jakarta, fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
    labelMedium = TextStyle(fontFamily = Jakarta, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.02.em),
)

@Composable
fun JalurTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    C.dark = dark
    MaterialTheme(colorScheme = scheme(), typography = type) {
        androidx.compose.runtime.CompositionLocalProvider(
            androidx.compose.material3.LocalTextStyle provides type.bodyLarge,
            content = content,
        )
    }
}
