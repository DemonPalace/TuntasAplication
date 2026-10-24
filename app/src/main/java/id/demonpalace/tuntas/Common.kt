package id.demonpalace.tuntas

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/** Ring that animates from 0 to [progress] the first time it appears and whenever it changes. */
@Composable
fun ProgressRing(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 72.dp,
    stroke: Dp = 8.dp,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val anim = remember { Animatable(0f) }
    LaunchedEffect(progress) { anim.animateTo(progress.coerceIn(0f, 1f), tween(900, easing = FastOutSlowInEasing)) }
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val s = stroke.toPx()
            val arcSize = Size(this.size.width - s, this.size.height - s)
            val tl = Offset(s / 2, s / 2)
            drawArc(C.line, 0f, 360f, false, tl, arcSize, style = Stroke(s))
            drawArc(
                Brush.sweepGradient(listOf(color.copy(alpha = 0.6f), color, color)),
                -90f, 360f * anim.value, false, tl, arcSize, style = Stroke(s, cap = StrokeCap.Round),
            )
        }
        content()
    }
}

@Composable
fun AnimatedNumber(value: Int, style: androidx.compose.ui.text.TextStyle, color: Color = C.text, suffix: String = "") {
    val shown by animateIntAsState(value, tween(800), label = "num")
    Text("$shown$suffix", style = style, color = color)
}

@Composable
fun LinearBar(progress: Float, color: Color, modifier: Modifier = Modifier, height: Dp = 10.dp) {
    val p by animateFloatAsState(progress.coerceIn(0f, 1f), tween(800, easing = FastOutSlowInEasing), label = "bar")
    Box(modifier.fillMaxWidth().height(height).clip(RoundedCornerShape(50)).background(C.line)) {
        Box(
            Modifier.fillMaxWidth(p).height(height).clip(RoundedCornerShape(50))
                .background(color)
        )
    }
}

/** Chunky Duolingo-style button: solid fill on a darker lip that presses down. */
@Composable
fun BouncyButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = C.violet,
    textColor: Color = Color.White,
    secondary: Boolean = false,
    onClick: () -> Unit,
) {
    val src = remember { MutableInteractionSource() }
    val pressed by src.collectIsPressedAsState()
    val press by animateFloatAsState(if (pressed && enabled) 1f else 0f, spring(Spring.DampingRatioMediumBouncy), label = "press")
    val face = when { !enabled -> C.surface2; secondary -> Color.White; else -> color }
    val lip = when { !enabled -> C.line; secondary -> C.line; color == Color.White -> Color(0xFFE5E5E5); else -> darken(color) }
    val label = when { !enabled -> C.faded; secondary -> C.blue; else -> textColor }
    Box(modifier.padding(bottom = 4.dp)) {
        Box(
            Modifier.matchParentSize().offset(y = 4.dp).clip(RoundedCornerShape(14.dp)).background(lip)
        )
        Box(
            Modifier
                .fillMaxWidth()
                .offset(y = (4 * press).dp)
                .clip(RoundedCornerShape(14.dp))
                .background(face)
                .then(if (secondary) Modifier.border(2.dp, C.line, RoundedCornerShape(14.dp)) else Modifier)
                .clickable(src, null, enabled = enabled, onClick = onClick)
                .padding(horizontal = 18.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text.uppercase(), color = label, style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold, letterSpacing = 0.8.sp, textAlign = TextAlign.Center)
        }
    }
}

fun darken(c: Color, f: Float = 0.78f) = Color(red = c.red * f, green = c.green * f, blue = c.blue * f, alpha = c.alpha)

fun lighten(c: Color, f: Float = 0.25f) = Color(
    red = c.red + (1 - c.red) * f, green = c.green + (1 - c.green) * f, blue = c.blue + (1 - c.blue) * f, alpha = c.alpha,
)

@Composable
fun Pill(text: String, color: Color, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier.clip(RoundedCornerShape(50)).background(color.copy(alpha = 0.18f)).padding(horizontal = 10.dp, vertical = 4.dp),
        color = color, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold,
    )
}

@Composable
fun Card(modifier: Modifier = Modifier, color: Color = C.surface, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier.clip(RoundedCornerShape(16.dp)).background(color).border(2.dp, C.line, RoundedCornerShape(16.dp)).padding(18.dp),
        content = content,
    )
}

/** Gentle up-down float, used on the current step and on mascots. */
@Composable
fun rememberBob(amplitude: Float = 6f, period: Int = 1400): Float {
    val t = rememberInfiniteTransition(label = "bob")
    val v by t.animateFloat(-amplitude, amplitude, infiniteRepeatable(tween(period, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "bobv")
    return v
}

@Composable
fun rememberPulse(): Float {
    val t = rememberInfiniteTransition(label = "pulse")
    val v by t.animateFloat(0f, 1f, infiniteRepeatable(tween(1600, easing = LinearEasing)), label = "pulsev")
    return v
}

private data class Particle(val angle: Float, val speed: Float, val color: Color, val spin: Float, val w: Float)

/** One-shot confetti burst drawn on a Canvas; restarts whenever [key] changes. */
@Composable
fun Confetti(key: Any, modifier: Modifier = Modifier) {
    val particles = remember(key) {
        val colors = listOf(C.violet, C.pink, C.gold, C.good, Color(0xFF5B8CFF), Color.White)
        List(90) {
            Particle(
                angle = Random.nextFloat() * 360f,
                speed = 0.35f + Random.nextFloat() * 0.75f,
                color = colors.random(),
                spin = Random.nextFloat() * 720f - 360f,
                w = 6f + Random.nextFloat() * 8f,
            )
        }
    }
    val t = remember(key) { Animatable(0f) }
    LaunchedEffect(key) { t.animateTo(1f, tween(2200, easing = LinearEasing)) }
    if (t.value >= 1f) return
    Canvas(modifier.fillMaxSize()) {
        val origin = Offset(size.width / 2, size.height * 0.35f)
        val maxR = size.minDimension * 0.75f
        particles.forEach { p ->
            val rad = Math.toRadians(p.angle.toDouble())
            val d = maxR * p.speed * (1 - (1 - t.value) * (1 - t.value))
            val x = origin.x + (cos(rad) * d).toFloat()
            val y = origin.y + (sin(rad) * d).toFloat() + 900f * t.value * t.value
            rotate(p.spin * t.value, Offset(x, y)) {
                drawRect(p.color.copy(alpha = 1f - t.value * 0.8f), Offset(x, y), Size(p.w, p.w * 0.55f))
            }
        }
    }
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(text.uppercase(), modifier.padding(top = 8.dp), color = C.muted, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
}

@Composable
fun StatRow(content: @Composable () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) { content() }
}
