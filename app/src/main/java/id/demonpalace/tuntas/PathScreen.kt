package id.demonpalace.tuntas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt
import kotlin.math.sin

private val ROW = 118.dp
private val NODE = 74.dp

/** Horizontal offset pattern of the winding path, as a fraction of the available swing. */
private fun swing(i: Int) = sin(i * 0.9).toFloat()

@Composable
fun PathScreen(store: Store, onOpen: (Int) -> Unit) {
    val scroll = rememberScrollState()
    var selected by remember { mutableStateOf<Int?>(null) }
    val current = store.currentNode.takeIf { it >= 0 }
    val density = LocalDensity.current

    // Bring the current stop into view when the map opens.
    LaunchedEffect(Unit) {
        val idx = current ?: return@LaunchedEffect
        val day = Content.nodes[idx].step.day
        val px = with(density) { (ROW * idx + 150.dp * day - 260.dp).toPx() }.roundToInt().coerceAtLeast(0)
        scroll.animateScrollTo(px, tween(900))
    }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            MapHeader(store)
            Column(
                Modifier.fillMaxSize().verticalScroll(scroll).padding(bottom = 140.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Content.nodes.indices.groupBy { Content.nodes[it].step.day }.forEach { (day, idxs) ->
                    DayBanner(day, idxs.map { Content.nodes[it].step }.distinct())
                    DayPath(store, idxs, current, selected) { idx ->
                        selected = if (selected == idx) null else idx
                    }
                    Chest(store, day)
                }
                FinishFlag(store)
            }
        }

        // Popover card for the tapped stop, Duolingo style.
        val sel = selected
        AnimatedVisibility(
            visible = sel != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            val idx = sel ?: current ?: 0
            StepPopover(store, idx, onClose = { selected = null }, onStart = { selected = null; onOpen(idx) })
        }
    }
}

@Composable
private fun MapHeader(store: Store) {
    val daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(Content.EXAM_DATE)).coerceAtLeast(0)
    val doneCount = Content.nodes.count { store.nodeDone(it) }
    Column(
        Modifier.fillMaxWidth().background(C.bg).padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HeaderChip("🔥", "${store.streak}", C.pink)
            HeaderChip("⭐", "${store.xp} XP", C.gold)
            HeaderChip("🏅", "Lv ${store.level}", C.violet)
            Spacer(Modifier.weight(1f))
            HeaderChip("⏳", "$daysLeft hari", C.good)
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            LinearBar(doneCount / Content.nodes.size.toFloat(), C.good, Modifier.weight(1f), 12.dp)
            Text("$doneCount/${Content.nodes.size}", color = C.muted, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun HeaderChip(icon: String, text: String, color: Color) {
    Row(
        Modifier.clip(RoundedCornerShape(50)).background(color.copy(alpha = 0.14f)).padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(icon, fontSize = 16.sp)
        Spacer(Modifier.width(4.dp))
        Text(text, color = color, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
private fun DayBanner(day: Int, steps: List<Step>) {
    val main = steps.first().subject.color
    Box(
        Modifier.padding(horizontal = 16.dp, vertical = 14.dp).fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(main)
            .padding(horizontal = 18.dp, vertical = 14.dp),
    ) {
        Column {
            Text("HARI $day · ${steps.first().date.uppercase()}", color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold)
            Text(steps.filter { !it.isPractice }.ifEmpty { steps }.joinToString(" · ") { it.title }, color = Color.White, style = MaterialTheme.typography.titleMedium, maxLines = 2)
            Text("${Content.nodes.count { it.step.day == day }} langkah · ${steps.sumOf { it.questions.size }} soal",
                color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun DayPath(store: Store, indices: List<Int>, current: Int?, selected: Int?, onTap: (Int) -> Unit) {
    BoxWithConstraints(Modifier.fillMaxWidth().height(ROW * indices.size)) {
        val swingPx = with(LocalDensity.current) { (maxWidth / 2 - NODE).toPx() * 0.7f }
        val rowPx = with(LocalDensity.current) { ROW.toPx() }

        // Dotted trail through the node centres.
        Canvas(Modifier.fillMaxSize()) {
            for (k in 0 until indices.size - 1) {
                val a = Offset(size.width / 2 + swing(indices[k]) * swingPx, rowPx * (k + 0.5f))
                val b = Offset(size.width / 2 + swing(indices[k + 1]) * swingPx, rowPx * (k + 1.5f))
                val doneLeg = store.nodeDone(Content.nodes[indices[k]])
                drawLine(
                    if (doneLeg) C.gold.copy(alpha = 0.8f) else C.line,
                    a, b, strokeWidth = 10f, cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 26f)),
                )
            }
        }
        indices.forEachIndexed { k, idx ->
            Box(
                Modifier.offset { IntOffset((swing(idx) * swingPx).roundToInt(), (rowPx * k).roundToInt()) }
                    .align(Alignment.TopCenter).height(ROW),
                contentAlignment = Alignment.Center,
            ) {
                MapStop(store, idx, isCurrent = idx == current, isSelected = idx == selected) { onTap(idx) }
            }
        }
    }
}

@Composable
private fun MapStop(store: Store, idx: Int, isCurrent: Boolean, isSelected: Boolean, onTap: () -> Unit) {
    val node = Content.nodes[idx]
    val step = node.step
    val done = store.nodeDone(node)
    val unlocked = store.nodeUnlocked(idx)
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val shake = remember { Animatable(0f) }
    val src = remember { MutableInteractionSource() }
    val pressed by src.collectIsPressedAsState()
    val press by animateFloatAsState(if (pressed) 0.9f else if (isSelected) 1.08f else 1f, spring(Spring.DampingRatioMediumBouncy), label = "node")
    val bob = if (isCurrent) rememberBob(5f) else 0f
    val pulse = if (isCurrent) rememberPulse() else 0f
    // Quizzes are bigger stops than reading stops, so the rhythm of the map shows.
    val disc = if (node.isQuiz) NODE else NODE - 10.dp

    val base = when {
        done -> C.gold
        unlocked -> step.subject.color
        else -> C.surface2
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isCurrent) {
            Text(
                "MULAI",
                Modifier.offset { IntOffset(0, bob.roundToInt()) }
                    .clip(RoundedCornerShape(12.dp)).background(C.surface).border(2.dp, step.subject.color, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                color = step.subject.color, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.labelLarge,
            )
            Spacer(Modifier.height(6.dp))
        }
        Box(
            Modifier.size(NODE + 18.dp).offset { IntOffset(shake.value.roundToInt(), 0) },
            contentAlignment = Alignment.Center,
        ) {
            if (isCurrent) {
                Canvas(Modifier.fillMaxSize()) {
                    drawCircle(step.subject.color.copy(alpha = 0.45f * (1 - pulse)), radius = disc.toPx() / 2 * (1.05f + 0.25f * pulse), style = Stroke(6f))
                }
            }
            // 3D button: darker disc underneath, face on top.
            Box(Modifier.size(disc).offset(y = 6.dp).clip(CircleShape).background(darken(base)))
            Box(
                Modifier.size(disc).scale(press).clip(CircleShape)
                    .background(base)
                    .clickable(src, null) {
                        if (unlocked) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onTap()
                        } else {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            scope.launch {
                                shake.animateTo(0f, keyframes { durationMillis = 400; -14f at 50; 14f at 120; -10f at 200; 8f at 280; 0f at 400 })
                            }
                        }
                    }
                    .testTag("node_$idx"),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    nodeIcon(node, done, unlocked), null,
                    tint = if (unlocked || done) Color.White else C.faded,
                    modifier = Modifier.size(if (node.isQuiz) 36.dp else 30.dp),
                )
            }
        }
        if (done && node.isQuiz) {
            val stars = starsFor(store.bestScore(step.id), step.questions.size)
            Text("★".repeat(stars) + "☆".repeat(3 - stars), color = C.gold, fontSize = 14.sp, letterSpacing = 2.sp)
        }
    }
}

fun starsFor(correct: Int, total: Int): Int = when {
    total == 0 || correct < 0 -> 0
    correct == total -> 3
    correct * 10 >= total * 7 -> 2
    else -> 1
}

@Composable
private fun Chest(store: Store, day: Int) {
    val ready = store.dayDone(day)
    val claimed = remember(store.tick, store.xp) { store.chestClaimed(day) }
    var reward by remember { mutableIntStateOf(0) }
    val wobble = if (ready && !claimed) rememberBob(8f, 500) else 0f
    Box(Modifier.fillMaxWidth().height(110.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier.size(64.dp).rotate(wobble).clip(RoundedCornerShape(18.dp))
                    .background(if (ready && !claimed) C.gold.copy(alpha = 0.25f) else C.surface)
                    .clickable(enabled = ready && !claimed) { reward = store.claimChest(day) }
                    .testTag("chest_$day"),
                contentAlignment = Alignment.Center,
            ) { Text(if (claimed) "📭" else "🎁", fontSize = 32.sp) }
            Text(
                when {
                    claimed -> "Hadiah hari $day sudah diambil"
                    ready -> "Ketuk untuk buka hadiah!"
                    else -> "Selesaikan hari $day untuk membuka"
                },
                color = if (ready && !claimed) C.gold else C.muted, style = MaterialTheme.typography.labelMedium,
            )
        }
        if (reward > 0) {
            Confetti(reward)
            AnimatedVisibility(true, enter = scaleIn(spring(Spring.DampingRatioHighBouncy)) + fadeIn()) {
                Text("+$reward XP", color = C.gold, fontSize = 22.sp, fontWeight = FontWeight.Black,
                    modifier = Modifier.offset(x = 70.dp))
            }
        }
    }
}

@Composable
private fun FinishFlag(store: Store) {
    val all = Content.nodes.all { store.nodeDone(it) }
    Column(Modifier.padding(top = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(if (all) "🏆" else "🏁", fontSize = 56.sp, modifier = Modifier.offset(y = rememberBob(4f).dp))
        Text(
            if (all) "Khatam! Siap TKA 2 November" else "TKA gelombang 2 · 2–5 November",
            color = C.text, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun StepPopover(store: Store, idx: Int, onClose: () -> Unit, onStart: () -> Unit) {
    val node = Content.nodes[idx]
    val step = node.step
    val done = store.nodeDone(node)
    val nth = Content.nodes.filter { it.step.id == step.id && !it.isQuiz }.size
    Column(
        Modifier.padding(16.dp).fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(step.subject.color)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(step.subject.icon, null, tint = Color.White.copy(alpha = 0.9f), modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("${step.subject.label.uppercase()} · HARI ${step.day}", color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
            Text("✕", color = Color.White, fontSize = 20.sp, modifier = Modifier.clip(CircleShape).clickable(onClick = onClose).padding(6.dp))
        }
        if (node.isQuiz) {
            Text(if (step.isPractice) step.title else "Latihan: ${step.title}", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            Text(
                "${step.questions.size} soal" + if (done) " · skor terbaik ${store.bestScore(step.id)}/${step.questions.size}" else "",
                color = Color.White, style = MaterialTheme.typography.labelLarge,
            )
        } else {
            val ch = chapterNo(step)
            Text(if (ch > 0) "Bab $ch · subbab ${node.section + 1} dari $nth" else "Ringkasan · bagian ${node.section + 1} dari $nth", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.labelLarge)
            Text(step.sections[node.section].title, color = Color.White, style = MaterialTheme.typography.headlineMedium)
            Text(step.title, color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.height(4.dp))
        BouncyButton(
            when {
                node.isQuiz && done -> "ULANGI  (+XP lagi)"
                node.isQuiz -> "MULAI LATIHAN"
                done -> "BACA LAGI"
                else -> "MULAI MEMBACA"
            },
            Modifier.fillMaxWidth().testTag("start_step"),
            color = Color.White,
            onClick = onStart,
            textColor = step.subject.color,
        )
    }
}
