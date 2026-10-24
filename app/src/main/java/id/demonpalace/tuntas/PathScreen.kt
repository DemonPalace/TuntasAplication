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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
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
import kotlin.math.roundToInt
import kotlin.math.sin

private val ROW = 118.dp
private val NODE = 74.dp

/** Horizontal offset pattern of the winding path, as a fraction of the available swing. */
private fun swing(i: Int) = sin(i * 0.9).toFloat()

@Composable
fun PathScreen(store: Store, onOpen: (Int) -> Unit) {
    var subject by remember { mutableStateOf(store.mapSubject) }
    var selected by remember { mutableStateOf<Int?>(null) }
    val current = store.currentNode(subject).takeIf { it >= 0 }
    // Chapters of this subject in path order; each chapter is its run of sub-chapter stops plus its quiz.
    val chapters = remember(subject) {
        Content.nodes.indices.filter { Content.nodes[it].step.subject == subject }.groupBy { Content.nodes[it].step.id }.values.toList()
    }
    val scroll = rememberScrollState()
    val density = LocalDensity.current

    // Bring the current stop into view when the map opens or the subject changes.
    LaunchedEffect(subject) {
        scroll.scrollTo(0)
        val idx = current ?: return@LaunchedEffect
        val ch = chapters.indexOfFirst { idx in it }
        val before = chapters.take(ch).sumOf { it.size } + chapters[ch].indexOf(idx)
        val px = with(density) { (ROW * before + 96.dp * ch + 220.dp - 240.dp).toPx() }.roundToInt().coerceAtLeast(0)
        scroll.animateScrollTo(px, tween(700))
    }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            MapHeader(store, subject) { subject = it; store.mapSubject = it; selected = null }
            Column(
                Modifier.fillMaxSize().verticalScroll(scroll).padding(bottom = 140.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (current != null) ContinueCard(store, current) { onOpen(current) }
                chapters.forEach { idxs ->
                    ChapterHeader(Content.nodes[idxs.first()].step, idxs.size)
                    DayPath(store, idxs, current, selected) { idx ->
                        selected = if (selected == idx) null else idx
                    }
                }
                FinishFlag(store, subject)
            }
        }

        // Popover card for the tapped stop.
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

private val MAP_SUBJECTS = listOf(Subject.MAT, Subject.FIS, Subject.BIN, Subject.BIG, Subject.MIX)

@Composable
private fun MapHeader(store: Store, subject: Subject, onSubject: (Subject) -> Unit) {
    val nodes = Content.nodes.filter { it.step.subject == subject }
    val doneCount = nodes.count { store.nodeDone(it) }
    Column(
        Modifier.fillMaxWidth().background(C.bg).padding(top = 12.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.Bottom) {
            Text("Jalur", color = C.text, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.weight(1f))
            val all = Content.nodes.count { store.nodeDone(it) } * 100 / Content.nodes.size
            Text("${store.streak} hari beruntun · $all% selesai", color = C.muted, style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 6.dp))
        }
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MAP_SUBJECTS.forEach { s ->
                val on = s == subject
                Row(
                    Modifier.clip(RoundedCornerShape(50))
                        .background(if (on) C.text else C.surface)
                        .border(1.dp, if (on) C.text else C.line, RoundedCornerShape(50))
                        .clickable { onSubject(s) }.testTag("subject_${s.name}")
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.size(8.dp).clip(CircleShape).background(s.color))
                    Spacer(Modifier.width(6.dp))
                    Text(s.label, color = if (on) C.bg else C.text, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
        Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            LinearBar(if (nodes.isEmpty()) 0f else doneCount / nodes.size.toFloat(), subject.color, Modifier.fillMaxWidth(), 6.dp)
            Text("$doneCount dari ${nodes.size} langkah ${subject.label} selesai", color = C.muted, style = MaterialTheme.typography.labelMedium)
        }
    }
}

/** The next unfinished stop of the chosen subject, so a session can start without scrolling the map. */
@Composable
private fun ContinueCard(store: Store, idx: Int, onStart: () -> Unit) {
    val node = Content.nodes[idx]
    val step = node.step
    Column(
        Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)).background(C.surface).border(1.dp, C.line, RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Lanjutkan · ${chapterLabel(step)}", color = C.muted, style = MaterialTheme.typography.labelMedium)
                Text(
                    if (node.isQuiz) "Latihan: ${step.title}" else step.sections[node.section].title,
                    color = C.text, style = MaterialTheme.typography.titleLarge, maxLines = 2,
                )
                Text(
                    if (node.isQuiz) "${step.questions.size} soal" else step.title,
                    color = C.muted, style = MaterialTheme.typography.bodyMedium, maxLines = 1,
                )
            }
            Mascot(MascotMood.IDLE, size = 64.dp)
        }
        Spacer(Modifier.height(6.dp))
        BouncyButton("Mulai", Modifier.fillMaxWidth().testTag("continue_step"), onClick = onStart)
    }
}

fun chapterLabel(step: Step): String {
    val ch = chapterNo(step)
    return when {
        ch > 0 -> "Bab $ch"
        step.isPractice -> "Paket soal"
        else -> step.subject.label
    }
}

@Composable
private fun ChapterHeader(step: Step, stops: Int) {
    Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 8.dp).fillMaxWidth()) {
        Text(chapterLabel(step).uppercase(), color = step.subject.color,
            style = MaterialTheme.typography.labelMedium, letterSpacing = 0.8.sp)
        Spacer(Modifier.height(2.dp))
        Text(step.title, color = C.text, style = MaterialTheme.typography.titleLarge, maxLines = 2)
        Text(
            buildString {
                val reading = step.sections.size
                if (reading > 0) append("$reading subbab")
                if (step.questions.isNotEmpty()) { if (isNotEmpty()) append(" · "); append("${step.questions.size} soal") }
                if (isEmpty()) append("$stops langkah")
            },
            color = C.muted, style = MaterialTheme.typography.labelMedium,
        )
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
                    if (doneLeg) C.violet.copy(alpha = 0.5f) else C.line,
                    a, b, strokeWidth = 8f, cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 22f)),
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
        done -> step.subject.color
        unlocked -> step.subject.color
        else -> C.surface2
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isCurrent) {
            Text(
                "Mulai",
                Modifier
                    .clip(RoundedCornerShape(50)).background(C.text)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                color = C.bg, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium,
            )
            Spacer(Modifier.height(6.dp))
        }
        Box(
            Modifier.size(NODE + 18.dp).offset { IntOffset(shake.value.roundToInt(), 0) },
            contentAlignment = Alignment.Center,
        ) {
            Box(
                Modifier
                    .then(if (isCurrent || isSelected) Modifier.border(3.dp, step.subject.color.copy(alpha = 0.35f), CircleShape).padding(6.dp) else Modifier)
                    .size(disc).clip(CircleShape)
                    .background(if (pressed) darken(base, 0.9f) else base)
                    .then(if (done) Modifier.border(3.dp, C.surface, CircleShape) else Modifier)
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
private fun FinishFlag(store: Store, subject: Subject) {
    val left = Content.nodes.count { it.step.subject == subject && !store.nodeDone(it) }
    Text(
        if (left == 0) "Semua langkah ${subject.label} selesai." else "Sisa $left langkah ${subject.label}.",
        Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        color = C.muted, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center,
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
            .clip(RoundedCornerShape(24.dp))
            .background(C.surface)
            .border(1.dp, C.line, RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(step.subject.icon, null, tint = step.subject.color, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("${step.subject.label} · ${chapterLabel(step)}", color = C.muted,
                style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
            Text("✕", color = C.muted, fontSize = 20.sp, modifier = Modifier.clip(CircleShape).clickable(onClick = onClose).padding(6.dp))
        }
        if (node.isQuiz) {
            Text(if (step.isPractice) step.title else "Latihan: ${step.title}", color = C.text, style = MaterialTheme.typography.headlineMedium)
            Text(
                "${step.questions.size} soal" + if (done) " · skor terbaik ${store.bestScore(step.id)}/${step.questions.size}" else "",
                color = C.text, style = MaterialTheme.typography.labelLarge,
            )
        } else {
            val ch = chapterNo(step)
            Text(if (ch > 0) "Bab $ch · subbab ${node.section + 1} dari $nth" else "Ringkasan · bagian ${node.section + 1} dari $nth", color = C.muted, style = MaterialTheme.typography.labelLarge)
            Text(step.sections[node.section].title, color = C.text, style = MaterialTheme.typography.headlineMedium)
            Text(step.title, color = C.muted, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.height(4.dp))
        BouncyButton(
            when {
                node.isQuiz && done -> "Ulangi"
                done -> "Baca lagi"
                else -> "Mulai"
            },
            Modifier.fillMaxWidth().testTag("start_step"),
            onClick = onStart,
        )
    }
}
