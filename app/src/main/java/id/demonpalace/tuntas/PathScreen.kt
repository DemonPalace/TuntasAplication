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
            HeaderChip("${store.streak} hari beruntun")
            HeaderChip("${store.xp} XP")
            HeaderChip("Level ${store.level}")
            Spacer(Modifier.weight(1f))
            HeaderChip("TKA $daysLeft hari lagi")
        }
        LinearBar(doneCount / Content.nodes.size.toFloat(), C.good, Modifier.fillMaxWidth(), 10.dp)
        Text("$doneCount dari ${Content.nodes.size} langkah selesai", color = C.muted, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun HeaderChip(text: String) {
    Text(text, Modifier.clip(RoundedCornerShape(12.dp)).border(2.dp, C.line, RoundedCornerShape(12.dp))
        .padding(horizontal = 8.dp, vertical = 4.dp), color = C.text, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
}

@Composable
private fun DayBanner(day: Int, steps: List<Step>) {
    val main = steps.first().subject.color
    Box(
        Modifier.padding(horizontal = 16.dp, vertical = 14.dp).fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
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
                "Mulai",
                Modifier
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
            Box(
                Modifier.size(disc).clip(CircleShape)
                    .background(if (pressed) darken(base, 0.9f) else base)
                    .border(if (isCurrent || isSelected) 4.dp else 2.dp, if (isCurrent || isSelected) C.text else darken(base), CircleShape)
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

/** Bonus XP once every stop of a day is done. */
@Composable
private fun Chest(store: Store, day: Int) {
    val ready = store.dayDone(day)
    val claimed = remember(store.tick, store.xp) { store.chestClaimed(day) }
    Box(Modifier.fillMaxWidth().padding(horizontal = 40.dp, vertical = 16.dp), contentAlignment = Alignment.Center) {
        if (ready && !claimed) {
            BouncyButton("Ambil bonus hari $day (+25 XP)", Modifier.fillMaxWidth().testTag("chest_$day"), color = C.gold) {
                store.claimChest(day)
            }
        } else {
            Text(
                if (claimed) "Bonus hari $day sudah diambil (+25 XP)" else "Bonus hari $day: +25 XP setelah semua langkah hari ini selesai",
                color = C.muted, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun FinishFlag(store: Store) {
    val left = Content.nodes.count { !store.nodeDone(it) }
    Text(
        if (left == 0) "Semua langkah selesai. TKA gelombang 2: 2 sampai 5 November 2026."
        else "Sisa $left langkah. TKA gelombang 2: 2 sampai 5 November 2026.",
        Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
        color = C.text, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center,
    )
}

@Composable
private fun StepPopover(store: Store, idx: Int, onClose: () -> Unit, onStart: () -> Unit) {
    val node = Content.nodes[idx]
    val step = node.step
    val done = store.nodeDone(node)
    val nth = Content.nodes.filter { it.step.id == step.id && !it.isQuiz }.size
    Column(
        Modifier.padding(16.dp).fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
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
                node.isQuiz && done -> "Ulangi"
                done -> "Baca lagi"
                else -> "Mulai"
            },
            Modifier.fillMaxWidth().testTag("start_step"),
            color = Color.White,
            onClick = onStart,
            textColor = step.subject.color,
        )
    }
}
