package id.demonpalace.tuntas

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** What Tuti is doing. */
enum class MascotMood { IDLE, CORRECT, WRONG, DONE }

private val Outline = Color(0xFF1A1D24)
private val ArmRed = Color(0xFFB23943)
private val PencilBody = Color(0xFFF4B63A)
private val PencilWood = Color(0xFFF2D2A9)
private val LegRed = Color(0xFF953038)

/** Shoulders sit just behind the cover edge, in the 200 x 240 units of the drawing. */
private val RightShoulder = Offset(157f, 104f)
private val LeftShoulder = Offset(49f, 118f)

/** One arm: direction in degrees (0 = right, -90 = up), length, and how far the elbow bows out. */
private class ArmPose(val angle: Float, val length: Float, val bend: Float)

private fun rightPose(m: MascotMood) = when (m) {
    MascotMood.IDLE -> ArmPose(-55f, 48f, -6f)
    MascotMood.CORRECT -> ArmPose(-68f, 52f, -10f)
    MascotMood.WRONG -> ArmPose(62f, 44f, -6f)
    MascotMood.DONE -> ArmPose(-72f, 52f, 0f)
}

private fun leftPose(m: MascotMood) = when (m) {
    MascotMood.IDLE -> ArmPose(120f, 36f, 6f)
    MascotMood.CORRECT -> ArmPose(-140f, 40f, 8f)
    MascotMood.WRONG -> ArmPose(108f, 36f, -4f)
    MascotMood.DONE -> ArmPose(-145f, 40f, 0f)
}

/** How far each arm sways around its pose, in degrees. A sad Tuti barely moves. */
private fun sway(m: MascotMood) = when (m) {
    MascotMood.IDLE -> 8f
    MascotMood.CORRECT, MascotMood.DONE -> 12f
    MascotMood.WRONG -> 2f
}

/** Smooth bump that rises and falls inside [a, b] of a 0..1 cycle; 0 outside it. */
private fun bump(p: Float, a: Float, b: Float): Float =
    if (p < a || p > b) 0f else sin(PI.toFloat() * (p - a) / (b - a))

/** Progress 0..1 inside [a, b] of a cycle, eased; 0 before and 1 after. */
private fun span(p: Float, a: Float, b: Float): Float {
    val t = ((p - a) / (b - a)).coerceIn(0f, 1f)
    return t * t * (3 - 2 * t)
}

/**
 * Tuti si Buku, the app's own character (see README, "Karakter").
 * Body and face are vectors (res/drawable/tuti_body_*); legs, arms and pencil are drawn here so they can move.
 * Idle: breathes, blinks, sways, and every few seconds does one small action in turn (wave, write in the air,
 * stretch, tap a foot, lean side to side, spin the pencil). A mood change springs the arms into a new pose:
 * hop with arms up and a pencil spin for a right answer, a head shake and hanging arms for a wrong one,
 * two hops and fast waving when a quiz is done. Tapping Tuti plays a short reaction.
 * The canvas leaves room on the sides and on top, so no frame is clipped. Motion only: no text, no confetti.
 */
@Composable
fun Mascot(mood: MascotMood, modifier: Modifier = Modifier, size: Dp = 96.dp) {
    val idle = painterResource(R.drawable.tuti_body_idle)
    val blink = painterResource(R.drawable.tuti_body_blink)
    val happy = painterResource(R.drawable.tuti_body_happy)
    val sad = painterResource(R.drawable.tuti_body_sad)

    val loop = rememberInfiniteTransition(label = "tuti")
    val breath by loop.animateFloat(0f, 1f, infiniteRepeatable(tween(1600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "breath")
    val wave by loop.animateFloat(-1f, 1f, infiniteRepeatable(tween(1300, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "wave")
    val flap by loop.animateFloat(-1f, 1f, infiniteRepeatable(tween(220, easing = LinearEasing), RepeatMode.Reverse), label = "flap")
    // Blink and the idle actions run on infinite clocks rather than delay loops, so UI tests can still go idle.
    val clock by loop.animateFloat(0f, 1f, infiniteRepeatable(tween(3600, easing = LinearEasing)), label = "blink")
    val acts by loop.animateFloat(0f, 1f, infiniteRepeatable(tween(15000, easing = LinearEasing)), label = "acts")

    val hop = remember { Animatable(0f) }      // 1 = top of a hop
    val shake = remember { Animatable(0f) }    // -1..1, a tilt left or right
    val squash = remember { Animatable(0f) }   // + = squashed down, - = stretched up
    val spin = remember { Animatable(0f) }     // pencil turns, 1 = one full turn
    val reach = remember { Animatable(0f) }    // 1 = both arms stretched up
    val fast = remember { Animatable(0f) }     // degrees of quick waving
    val idleOn = remember { Animatable(1f) }
    val rA = remember { Animatable(rightPose(MascotMood.IDLE).angle) }
    val rL = remember { Animatable(rightPose(MascotMood.IDLE).length) }
    val rB = remember { Animatable(rightPose(MascotMood.IDLE).bend) }
    val lA = remember { Animatable(leftPose(MascotMood.IDLE).angle) }
    val lL = remember { Animatable(leftPose(MascotMood.IDLE).length) }
    val lB = remember { Animatable(leftPose(MascotMood.IDLE).bend) }
    val amp = remember { Animatable(sway(MascotMood.IDLE)) }

    suspend fun hopOnce() {
        hop.animateTo(0f, keyframes { durationMillis = 520; 1f at 200; 0f at 420; 0.12f at 470 })
        squash.animateTo(0f, keyframes { durationMillis = 260; 0.08f at 60; -0.03f at 160 })
    }

    LaunchedEffect(mood) {
        hop.snapTo(0f); shake.snapTo(0f); spin.snapTo(0f)
        coroutineScope {
            // A loose spring, so the arms overshoot a little and settle like real limbs.
            val arms = spring<Float>(dampingRatio = 0.45f, stiffness = Spring.StiffnessMediumLow)
            val r = rightPose(mood); val l = leftPose(mood)
            listOf(rA to r.angle, rL to r.length, rB to r.bend, lA to l.angle, lL to l.length, lB to l.bend, amp to sway(mood))
                .forEach { (a, v) -> async { a.animateTo(v, arms) } }
            async { idleOn.animateTo(if (mood == MascotMood.IDLE) 1f else 0f, tween(300)) }
            when (mood) {
                MascotMood.IDLE -> fast.animateTo(0f, tween(300))
                MascotMood.CORRECT -> {
                    async { spin.animateTo(1f, tween(700, easing = FastOutSlowInEasing)) }
                    async { fast.animateTo(0f, keyframes { durationMillis = 1200; 12f at 150; 12f at 900 }) }
                    hopOnce()
                }
                MascotMood.WRONG -> {
                    async { squash.animateTo(0f, keyframes { durationMillis = 1200; 0.06f at 500; 0.06f at 800 }) }
                    shake.animateTo(0f, keyframes { durationMillis = 600; -1f at 100; 1f at 220; -0.6f at 340; 0.3f at 460 })
                }
                MascotMood.DONE -> {
                    async { spin.animateTo(2f, tween(1100, easing = FastOutSlowInEasing)) }
                    async { fast.animateTo(16f, tween(200)) }
                    hopOnce(); hopOnce()
                    shake.animateTo(0f, keyframes { durationMillis = 500; 0.5f at 120; -0.5f at 260; 0.25f at 380 })
                }
            }
        }
    }

    // A tap plays one of three short reactions in turn.
    var taps by remember { mutableIntStateOf(0) }
    LaunchedEffect(taps) {
        if (taps == 0) return@LaunchedEffect
        coroutineScope {
            when (taps % 3) {
                1 -> { async { spin.snapTo(0f); spin.animateTo(1f, tween(650, easing = FastOutSlowInEasing)) }; hopOnce() }
                2 -> {
                    async { fast.animateTo(0f, keyframes { durationMillis = 1000; 14f at 120; 14f at 800 }) }
                    shake.animateTo(0f, keyframes { durationMillis = 900; 0.5f at 150; -0.5f at 400; 0.4f at 650 })
                }
                else -> reach.animateTo(0f, keyframes { durationMillis = 1100; 1f at 350; 1f at 700 })
            }
        }
    }

    val body = when (mood) {
        MascotMood.IDLE -> if (clock > 0.96f) blink else idle
        MascotMood.CORRECT, MascotMood.DONE -> happy
        MascotMood.WRONG -> sad
    }
    val rim = C.mascotRim
    val rimOn = C.dark
    Canvas(
        modifier.size(size * 1.4f, size * 1.42f).testTag("mascot")
            .pointerInput(Unit) { detectTapGestures { taps++ } },
    ) {
        val w = this.size.width / 1.4f
        val h = w * 1.2f
        val left = (this.size.width - w) / 2
        val top = this.size.height - h

        // Idle actions, each a short bump inside the 15 s cycle.
        val on = idleOn.value
        val hello = bump(acts, 0.03f, 0.13f) * on
        val write = bump(acts, 0.20f, 0.32f) * on
        val stretch = bump(acts, 0.39f, 0.49f) * on + reach.value
        val tap = bump(acts, 0.56f, 0.66f) * on
        val lean = bump(acts, 0.72f, 0.84f) * on
        val twirl = span(acts, 0.89f, 0.97f) * (if (acts < 0.98f) on else 0f)
        val local = acts * 15f   // seconds into the cycle, drives the quick wiggles

        val rAngle = rA.value + wave * amp.value + flap * fast.value +
            hello * (-25f + sin(local * 2 * PI.toFloat() * 2.5f) * 14f) +
            write * (25f + sin(local * 2 * PI.toFloat() * 4f) * 6f) -
            25f * stretch - 10f * bump(acts, 0.89f, 0.97f) * on
        // Writing pulls the hand in, so the pencil sweeping sideways stays inside the canvas.
        val right = ArmPose(rAngle, rL.value - 12f * write, rB.value + wave * 3f)
        val leftArm = ArmPose(lA.value - wave * amp.value * 0.6f + 100f * stretch, lL.value, lB.value)
        val pencilAngle = rAngle - 30f + 130f * write + 360f * (spin.value + twirl)
        val tuck = hop.value * 5f
        val legs = LegLift(tuck, tuck + tap * maxOf(0f, sin(local * 2 * PI.toFloat() * 3f)) * 7f)
        val sq = squash.value - 0.04f * stretch + tap * 0.015f * sin(local * 2 * PI.toFloat() * 6f)
        val tilt = shake.value * 6f + lean * 5f * sin(local * 2 * PI.toFloat() * 1.1f)

        withTransform({
            translate(left, top - hop.value * h * 0.12f)
            rotate(tilt, Offset(w / 2, h))
            scale(1f + sq, 1f - sq + breath * 0.02f, Offset(w / 2, h))
        }) {
            val fig = Size(w, h)
            if (rimOn) {
                val r = 2.dp.toPx()
                for ((dx, dy) in RIM) translate(dx * r, dy * r) { figure(fig, body, right, leftArm, pencilAngle, legs, rim) }
            }
            figure(fig, body, right, leftArm, pencilAngle, legs, null)
        }
    }
}

/** How far each foot is lifted, in drawing units. */
private class LegLift(val left: Float, val right: Float)

/** Legs, then arms and pencil, then the book on top, so the shoulders tuck behind the cover. [tint] paints a flat rim. */
private fun DrawScope.figure(fig: Size, body: Painter, right: ArmPose, left: ArmPose, pencilAngle: Float, legs: LegLift, tint: Color?) {
    val k = fig.width / 200f
    withTransform({ scale(k, k, Offset.Zero) }) {
        leg(76f, legs.left, tint)
        leg(118f, legs.right, tint)
        arm(LeftShoulder, left, 8f, tint)
        pencil(handOf(RightShoulder, right), pencilAngle, tint)
        arm(RightShoulder, right, 9f, tint)
    }
    with(body) { draw(fig, colorFilter = tint?.let { ColorFilter.tint(it) }) }
}

/** One leg and shoe; [x] is the leg's left edge. The top stays hidden under the book when lifted. */
private fun DrawScope.leg(x: Float, lift: Float, tint: Color?) {
    translate(0f, -lift) {
        val shin = Path().apply { moveTo(x, 186f); lineTo(x, 206f); lineTo(x + 12f, 206f); lineTo(x + 12f, 186f); close() }
        drawPath(shin, tint ?: LegRed)
        drawPath(shin, tint ?: Outline, style = Stroke(4f, join = StrokeJoin.Round))
        val sx = x - 6f
        val shoe = Path().apply {
            moveTo(sx, 214f); quadraticTo(sx, 204f, sx + 12f, 204f); quadraticTo(sx + 26f, 204f, sx + 26f, 214f)
            quadraticTo(sx + 26f, 220f, sx + 20f, 220f); lineTo(sx + 4f, 220f); quadraticTo(sx, 220f, sx, 214f); close()
        }
        drawPath(shoe, tint ?: Outline)
    }
}

private fun handOf(s: Offset, p: ArmPose): Offset {
    val a = Math.toRadians(p.angle.toDouble())
    return Offset(s.x + p.length * cos(a).toFloat(), s.y + p.length * sin(a).toFloat())
}

/** A rubber-hose arm: one curve stroked dark and wide, then red and narrow, ending in a round hand. */
private fun DrawScope.arm(s: Offset, p: ArmPose, handR: Float, tint: Color?) {
    val h = handOf(s, p)
    val a = Math.toRadians(p.angle.toDouble())
    val mid = (s + h) / 2f
    val ctrl = Offset(mid.x - sin(a).toFloat() * p.bend, mid.y + cos(a).toFloat() * p.bend)
    val path = Path().apply { moveTo(s.x, s.y); quadraticTo(ctrl.x, ctrl.y, h.x, h.y) }
    drawPath(path, tint ?: Outline, style = Stroke(14f, cap = StrokeCap.Round))
    drawPath(path, tint ?: ArmRed, style = Stroke(6f, cap = StrokeCap.Round))
    drawCircle(tint ?: ArmRed, handR, h)
    drawCircle(tint ?: Outline, handR, h, style = Stroke(4f))
}

/** The pencil in the right hand, pointing along [angle] degrees. */
private fun DrawScope.pencil(hand: Offset, angle: Float, tint: Color?) {
    val stroke = Stroke(4f, join = StrokeJoin.Round)
    rotate(angle + 90f, hand) {
        translate(hand.x, hand.y) {
            val shaft = Path().apply { moveTo(-7f, -22f); lineTo(7f, -22f); lineTo(7f, 12f); lineTo(-7f, 12f); close() }
            val tip = Path().apply { moveTo(-7f, -22f); lineTo(0f, -38f); lineTo(7f, -22f); close() }
            val lead = Path().apply { moveTo(-2f, -33f); lineTo(0f, -38f); lineTo(2f, -33f); close() }
            drawPath(shaft, tint ?: PencilBody); drawPath(shaft, tint ?: Outline, style = stroke)
            drawPath(tip, tint ?: PencilWood); drawPath(tip, tint ?: Outline, style = stroke)
            drawPath(lead, tint ?: Outline)
        }
    }
}

private val RIM = listOf(1f to 0f, -1f to 0f, 0f to 1f, 0f to -1f, 0.7f to 0.7f, -0.7f to 0.7f, 0.7f to -0.7f, -0.7f to -0.7f)
