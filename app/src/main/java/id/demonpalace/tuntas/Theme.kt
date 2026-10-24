package id.demonpalace.tuntas

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/** Duolingo-style palette: white paper, one saturated green for progress, blue for secondary actions. */
object C {
    val bg = Color(0xFFFFFFFF)          // Paper White
    val surface = Color(0xFFF7F7F7)     // soft card fill
    val surface2 = Color(0xFFEFEFEF)
    val line = Color(0xFFE5E5E5)        // 2px sticker borders
    val text = Color(0xFF4B4B4B)        // Charcoal
    val muted = Color(0xFF777777)       // Pencil Gray
    val faded = Color(0xFFAFAFAF)       // Faded Gray
    val violet = Color(0xFF58CC02)      // Eager Green: primary, progress, CTA
    val blue = Color(0xFF1CB0F6)        // Spark Blue: links, secondary
    val pink = Color(0xFFFF9600)        // streak / combo flame orange
    val good = Color(0xFF58CC02)
    val goodWash = Color(0xFFD7FFB8)    // Storybook Green
    val bad = Color(0xFFFF4B4B)
    val gold = Color(0xFFFFC800)
    val ink = Color(0xFF000437)         // Night Ink
}

@OptIn(ExperimentalTextApi::class)
private fun nunito(w: Int) = Font(
    R.font.nunito, FontWeight(w),
    variationSettings = FontVariation.Settings(FontVariation.weight(w)),
)

@OptIn(ExperimentalTextApi::class)
val Nunito = FontFamily(nunito(500), nunito(600), nunito(700), nunito(800), nunito(900))

private val scheme = lightColorScheme(
    primary = C.violet,
    onPrimary = Color.White,
    secondary = C.blue,
    background = C.bg,
    onBackground = C.text,
    surface = C.bg,
    onSurface = C.text,
    surfaceVariant = C.surface,
    onSurfaceVariant = C.muted,
    outline = C.line,
    error = C.bad,
)

private val type = Typography(
    headlineLarge = TextStyle(fontFamily = Nunito, fontSize = 30.sp, fontWeight = FontWeight.Black, lineHeight = 36.sp, letterSpacing = (-0.02).em),
    headlineMedium = TextStyle(fontFamily = Nunito, fontSize = 24.sp, fontWeight = FontWeight.Black, lineHeight = 29.sp, letterSpacing = (-0.02).em),
    titleLarge = TextStyle(fontFamily = Nunito, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 26.sp),
    titleMedium = TextStyle(fontFamily = Nunito, fontSize = 17.sp, fontWeight = FontWeight.Bold, lineHeight = 22.sp),
    bodyLarge = TextStyle(fontFamily = Nunito, fontSize = 17.sp, fontWeight = FontWeight.Medium, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = Nunito, fontSize = 15.sp, fontWeight = FontWeight.Medium, lineHeight = 21.sp),
    labelLarge = TextStyle(fontFamily = Nunito, fontSize = 15.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.053.em),
    labelMedium = TextStyle(fontFamily = Nunito, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.04.em),
)

@Composable
fun JalurTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = scheme, typography = type) {
        androidx.compose.runtime.CompositionLocalProvider(
            androidx.compose.material3.LocalTextStyle provides type.bodyLarge,
            content = content,
        )
    }
}
