package id.demonpalace.tuntas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.noties.jlatexmath.JLatexMathDrawable

/**
 * Math in lesson and question text is written as LaTeX between § marks, e.g. "Luas = §\\frac{1}{2}at§".
 * § is used instead of $ because $ starts a Kotlin string template.
 */
const val MATH = '§'

private val mathRx = Regex("§([^§]+)§")

fun hasMath(text: String) = text.indexOf(MATH) >= 0

/** Text with the LaTeX stripped back to plain characters, for places that cannot typeset (e.g. one-line labels). */
fun plainMath(text: String): String = mathRx.replace(text) { m ->
    m.groupValues[1]
        .replace(Regex("\\\\[dt]?frac\\{([^{}]*)\\}\\{([^{}]*)\\}"), "($1)/($2)")
        .replace(Regex("\\\\sqrt\\{([^{}]*)\\}"), "√($1)")
        .replace("\\cdot", "·").replace("\\times", "×").replace("\\div", "÷")
        .replace("\\le", "≤").replace("\\ge", "≥").replace("\\neq", "≠").replace("\\pi", "π")
        .replace("\\Delta", "Δ").replace("\\rho", "ρ").replace("\\theta", "θ").replace("\\lambda", "λ").replace("\\omega", "ω")
        .replace("\\alpha", "α").replace("\\mu", "μ").replace("\\sum", "Σ").replace("\\degree", "°").replace("^\\circ", "°")
        .replace("{,}", ",").replace("\\,", " ").replace("\\text", "").replace("\\mathrm", "").replace("\\left", "").replace("\\right", "")
        .replace(Regex("[{}\\\\]"), "")
}

private class MathPiece(val drawable: JLatexMathDrawable?, val source: String)

private fun buildPiece(latex: String, sizePx: Float, color: Color): MathPiece = MathPiece(
    runCatching { JLatexMathDrawable.builder(latex).textSize(sizePx).color(color.toArgb()).build() }.getOrNull(),
    latex,
)

/**
 * Prose with inline formulas. Plain pieces go through [decorate] (bold emphasis, drop caps),
 * whose flag says whether the piece opens the paragraph.
 */
@Composable
fun MathText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = style.color,
    decorate: (String, Boolean) -> AnnotatedString = { s, _ -> AnnotatedString(s) },
) {
    if (!hasMath(text)) {
        Text(decorate(text, true), style = style, color = color, modifier = modifier)
        return
    }
    val density = LocalDensity.current
    // Formulas follow the text colour, so they stay readable in dark mode.
    val ink = when {
        color != Color.Unspecified -> color
        style.color != Color.Unspecified -> style.color
        else -> C.text
    }
    val sizePx = with(density) { (if (style.fontSize.isSpecified()) style.fontSize else 16.sp).toPx() } * 1.08f
    val built = remember(text, sizePx, ink) {
        val inline = mutableMapOf<String, InlineTextContent>()
        val str = buildAnnotatedString {
            var last = 0
            var first = true
            mathRx.findAll(text).forEachIndexed { i, m ->
                if (m.range.first > last) { append(decorate(text.substring(last, m.range.first), first)); first = false }
                val piece = buildPiece(m.groupValues[1], sizePx, ink)
                val d = piece.drawable
                if (d == null) {
                    append(plainMath(m.value))
                } else {
                    val id = "m$i"
                    appendInlineContent(id, piece.source)
                    inline[id] = InlineTextContent(placeholderFor(d, density)) {
                        Canvas(Modifier.fillMaxSize()) {
                            drawIntoCanvas { c -> d.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight); d.draw(c.nativeCanvas) }
                        }
                    }
                }
                first = false
                last = m.range.last + 1
            }
            if (last < text.length) append(decorate(text.substring(last), first))
        }
        str to inline
    }
    // Justified lines stretch the spaces but not the inline formula slots, so formulas would land on the words.
    val safe = if (style.textAlign == TextAlign.Justify) style.copy(textAlign = TextAlign.Start) else style
    Text(built.first, style = safe, color = color, modifier = modifier, inlineContent = built.second)
}

private fun androidx.compose.ui.unit.TextUnit.isSpecified() = this != androidx.compose.ui.unit.TextUnit.Unspecified

private fun placeholderFor(d: JLatexMathDrawable, density: Density): Placeholder {
    val k = density.density * density.fontScale
    return Placeholder((d.intrinsicWidth / k).sp, (d.intrinsicHeight / k).sp, PlaceholderVerticalAlign.TextCenter)
}

/**
 * A display formula on its own line (the RUMUS box). A formula wider than the page scrolls sideways
 * instead of being squeezed.
 */
@Composable
fun MathBlock(text: String, style: TextStyle, modifier: Modifier = Modifier, color: Color = style.color) {
    val whole = mathRx.matchEntire(text.trim())
    if (whole == null) {
        MathText(text, style, modifier, color)
        return
    }
    val density = LocalDensity.current
    val sizePx = with(density) { (if (style.fontSize.isSpecified()) style.fontSize else 16.sp).toPx() } * 1.15f
    val piece = remember(text, sizePx, color) { buildPiece(whole.groupValues[1], sizePx, color) }
    val d = piece.drawable
    if (d == null) {
        Text(plainMath(text), style = style, color = color, modifier = modifier)
        return
    }
    Box(modifier.horizontalScroll(rememberScrollState())) {
        Canvas(Modifier.size(with(density) { d.intrinsicWidth.toDp() } + 2.dp, with(density) { d.intrinsicHeight.toDp() })) {
            drawIntoCanvas { c -> d.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight); d.draw(c.nativeCanvas) }
        }
    }
}
