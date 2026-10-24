package id.demonpalace.tuntas

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** What Tuti is doing. */
enum class MascotMood { IDLE, CORRECT, WRONG, DONE }

/**
 * Tuti si Buku, the app's own character (see README, "Karakter").
 * The figure is a 200 x 240 vector; the canvas adds room on the sides for the head shake and on top for hops,
 * so no frame is clipped. IDLE breathes and blinks; CORRECT hops; WRONG shakes; DONE hops twice.
 * Motion only: no text, no confetti.
 */
@Composable
fun Mascot(mood: MascotMood, modifier: Modifier = Modifier, size: Dp = 96.dp) {
    val idle = painterResource(R.drawable.tuti_idle)
    val blink = painterResource(R.drawable.tuti_blink)
    val happy = painterResource(R.drawable.tuti_happy)
    val sad = painterResource(R.drawable.tuti_sad)

    val loop = rememberInfiniteTransition(label = "tuti")
    val breath by loop.animateFloat(0f, 1f, infiniteRepeatable(tween(1600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "breath")

    // Blink on an infinite clock rather than a delay loop, so UI tests can still go idle.
    val clock by loop.animateFloat(0f, 1f, infiniteRepeatable(tween(3600, easing = LinearEasing)), label = "blink")
    val hop = remember { Animatable(0f) }    // 1 = top of a hop
    val shake = remember { Animatable(0f) }  // -1..1, a tilt left or right
    LaunchedEffect(mood) {
        hop.snapTo(0f); shake.snapTo(0f)
        when (mood) {
            MascotMood.IDLE -> Unit
            MascotMood.CORRECT -> hop.animateTo(0f, keyframes { durationMillis = 520; 1f at 200; 0f at 420; 0.12f at 470 })
            MascotMood.WRONG -> shake.animateTo(0f, keyframes { durationMillis = 600; -1f at 100; 1f at 220; -0.6f at 340; 0.3f at 460 })
            MascotMood.DONE -> {
                hop.animateTo(0f, keyframes { durationMillis = 900; 1f at 200; 0f at 400; 0.7f at 580; 0f at 760; 0.1f at 830 })
                shake.animateTo(0f, keyframes { durationMillis = 400; 0.4f at 100; -0.4f at 220; 0.2f at 320 })
            }
        }
    }

    val face = when (mood) {
        MascotMood.IDLE -> if (clock > 0.96f) blink else idle
        MascotMood.CORRECT, MascotMood.DONE -> happy
        MascotMood.WRONG -> sad
    }
    val rim = C.mascotRim
    val rimOn = C.dark
    Canvas(modifier.size(size * 1.3f, size * 1.42f).testTag("mascot")) {
        val w = this.size.width / 1.3f
        val h = w * 1.2f
        val left = (this.size.width - w) / 2
        val top = this.size.height - h
        withTransform({
            translate(left, top - hop.value * h * 0.12f)
            rotate(shake.value * 6f, Offset(w / 2, h))
            scale(1f, 1f + breath * 0.02f, Offset(w / 2, h))
        }) {
            val fig = Size(w, h)
            if (rimOn) {
                val r = 2.dp.toPx()
                for ((dx, dy) in RIM) translate(dx * r, dy * r) {
                    with(face) { draw(fig, colorFilter = ColorFilter.tint(rim)) }
                }
            }
            with(face) { draw(fig) }
        }
    }
}

private val RIM = listOf(1f to 0f, -1f to 0f, 0f to 1f, 0f to -1f, 0.7f to 0.7f, -0.7f to 0.7f, 0.7f to -0.7f, -0.7f to -0.7f)
