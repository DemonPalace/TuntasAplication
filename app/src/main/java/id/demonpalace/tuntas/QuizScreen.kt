package id.demonpalace.tuntas

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun QuizScreen(
    store: Store,
    title: String,
    color: Color,
    questions: List<Question>,
    onExit: () -> Unit,
    onDone: (correct: Int) -> Unit,
    onContinue: () -> Unit,
) {
    var index by remember { mutableIntStateOf(0) }
    // Answer state for the current question: one pick, a set of ticks, or one column per table row.
    var picked by remember { mutableStateOf<Int?>(null) }
    val checks = remember { mutableStateListOf<Int>() }
    val cells = remember { mutableStateMapOf<Int, Int>() }
    var revealed by remember { mutableStateOf(false) }
    var lastOk by remember { mutableStateOf(false) }
    var correct by remember { mutableIntStateOf(0) }
    var combo by remember { mutableIntStateOf(0) }
    var bestCombo by remember { mutableIntStateOf(0) }
    var xpGained by remember { mutableIntStateOf(0) }
    var lastGain by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(questions.isEmpty()) }
    val haptic = LocalHapticFeedback.current

    fun grade(question: Question, ok: Boolean) {
        revealed = true
        lastOk = ok
        if (ok) { correct++; combo++; bestCombo = maxOf(bestCombo, combo) } else combo = 0
        lastGain = store.answer(question, ok, combo.coerceAtLeast(1))
        xpGained += lastGain
        haptic.performHapticFeedback(if (ok) HapticFeedbackType.TextHandleMove else HapticFeedbackType.LongPress)
    }

    Column(Modifier.fillMaxSize().background(C.bg).statusBarsPadding().navigationBarsPadding()) {
        if (finished) {
            ResultView(questions.size, correct, bestCombo, xpGained, color, onContinue)
            return@Column
        }
        val q = questions[index]
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("✕", color = C.muted, fontSize = 22.sp, modifier = Modifier.clip(CircleShape).clickable(onClick = onExit).padding(8.dp).testTag("quiz_exit"))
            Spacer(Modifier.width(6.dp))
            LinearBar((index + if (revealed) 1 else 0) / questions.size.toFloat(), color, Modifier.weight(1f), 14.dp)
            Spacer(Modifier.width(10.dp))
            ComboBadge(combo)
        }
        Text(title, color = C.muted, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(horizontal = 20.dp))

        AnimatedContent(
            targetState = index,
            transitionSpec = { (slideInHorizontally { it } + fadeIn()) togetherWith (slideOutHorizontally { -it } + fadeOut()) },
            label = "q", modifier = Modifier.weight(1f),
        ) { qi ->
            val question = questions[qi]
            val active = qi == index
            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("SOAL ${qi + 1} DARI ${questions.size}", color = color, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.width(8.dp))
                    when {
                        question.isTable -> Pill("PG Kompleks · Kategori", C.blue)
                        question.isMulti -> Pill("PG Kompleks · pilih >1", C.blue)
                        else -> {}
                    }
                }
                question.passage?.let { PassageBox(it) }
                question.image?.let { QuestionImage(it) }
                MathText(question.text, MaterialTheme.typography.titleLarge, color = C.text)
                val shown = active && revealed
                when {
                    question.isTable -> {
                        TableAnswer(question, if (active) cells else emptyMap(), shown, color) { r, c ->
                            if (!revealed) cells[r] = c
                        }
                        if (active && !revealed) BouncyButton(
                            "Periksa", Modifier.fillMaxWidth().testTag("quiz_check"),
                            enabled = cells.size == question.rows.size, color = color,
                        ) { grade(question, question.rows.indices.all { cells[it] == question.rowKeys[it] }) }
                    }
                    question.isMulti -> {
                        question.options.forEachIndexed { i, opt ->
                            CheckRow(i, opt, i in question.multi!!, active && i in checks, shown, color) {
                                if (!revealed) { if (i in checks) checks.remove(i) else checks.add(i) }
                            }
                        }
                        if (active && !revealed) BouncyButton(
                            "Periksa", Modifier.fillMaxWidth().testTag("quiz_check"),
                            enabled = checks.isNotEmpty(), color = color,
                        ) { grade(question, checks.toSet() == question.multi) }
                    }
                    else -> question.options.forEachIndexed { i, opt ->
                        OptionRow(i, opt, question.answer, if (active) picked else null, color) {
                            if (picked != null) return@OptionRow
                            picked = i
                            grade(question, i == question.answer)
                        }
                    }
                }
                Spacer(Modifier.height(140.dp))
            }
        }

        // Feedback sheet slides up after answering.
        AnimatedVisibility(
            visible = revealed,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
        ) {
            val ok = lastOk
            val tone = if (ok) C.good else C.bad
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(C.surface).border(2.dp, tone, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .heightIn(max = 380.dp).verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(if (ok) "Benar" else "Salah", color = tone, style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f).testTag("verdict"))
                    Text("+$lastGain XP", color = C.muted, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                MathText("Kunci: ${q.keyText()}", MaterialTheme.typography.titleMedium, color = C.text)
                MathText(q.explain, MaterialTheme.typography.bodyLarge, color = C.text)
                BouncyButton(
                    if (index == questions.size - 1) "Lihat hasil" else "Lanjut",
                    Modifier.fillMaxWidth().testTag("quiz_next"),
                    color = tone,
                ) {
                    if (index == questions.size - 1) { finished = true; onDone(correct) } else {
                        index++; picked = null; checks.clear(); cells.clear(); revealed = false
                    }
                }
            }
        }
    }
}

/** Figure for a question, from assets. Tap to open it full screen with pinch zoom. */
@Composable
private fun QuestionImage(path: String) {
    val ctx = LocalContext.current
    val bmp = remember(path) {
        runCatching { ctx.assets.open(path).use { BitmapFactory.decodeStream(it) }?.asImageBitmap() }.getOrNull()
    } ?: return
    var zoomed by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            bmp, "Gambar soal",
            Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color.White)
                .border(2.dp, C.line, RoundedCornerShape(16.dp)).clickable { zoomed = true }.testTag("q_image"),
            contentScale = ContentScale.FillWidth,
        )
        Text("Ketuk gambar untuk memperbesar", color = C.faded, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 4.dp))
    }
    if (zoomed) {
        Dialog(onDismissRequest = { zoomed = false }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
            var scale by remember { mutableFloatStateOf(1f) }
            var pan by remember { mutableStateOf(Offset.Zero) }
            Box(
                Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.92f))
                    .pointerInput(Unit) {
                        detectTransformGestures { _, p, z, _ ->
                            scale = (scale * z).coerceIn(1f, 5f)
                            pan = if (scale == 1f) Offset.Zero else pan + p
                        }
                    }
                    .clickable { zoomed = false },
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    bmp, "Gambar soal diperbesar",
                    Modifier.fillMaxWidth().graphicsLayer { scaleX = scale; scaleY = scale; translationX = pan.x; translationY = pan.y },
                    contentScale = ContentScale.FillWidth,
                )
            }
        }
    }
}

/** PG Kompleks: tick every option that is correct, then PERIKSA. */
@Composable
private fun CheckRow(i: Int, text: String, isKey: Boolean, checked: Boolean, revealed: Boolean, accent: Color, onClick: () -> Unit) {
    val tone = when {
        revealed && isKey -> C.good
        revealed && checked -> C.bad
        checked -> accent
        else -> C.line
    }
    val border by animateColorAsState(tone, label = "cb")
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .background(if (checked || (revealed && isKey)) tone.copy(alpha = 0.13f) else C.surface)
            .border(2.dp, border, RoundedCornerShape(12.dp))
            .clickable(enabled = !revealed, onClick = onClick).testTag("chk_$i")
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(30.dp).clip(RoundedCornerShape(8.dp))
                .background(if (checked || (revealed && isKey)) tone else C.surface2)
                .border(2.dp, if (checked) tone else C.line, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                when { revealed && isKey -> "✓"; revealed && checked -> "✕"; checked -> "✓"; else -> "${'A' + i}" },
                color = if (checked || (revealed && isKey)) Color.White else C.muted, fontWeight = FontWeight.Black,
            )
        }
        Spacer(Modifier.width(12.dp))
        MathText(text, MaterialTheme.typography.bodyLarge, Modifier.weight(1f), color = C.text)
    }
}

/** PG Kompleks Kategori: each statement gets one column, e.g. Benar/Salah. */
@Composable
private fun TableAnswer(q: Question, cells: Map<Int, Int>, revealed: Boolean, accent: Color, onPick: (Int, Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        q.rows.forEachIndexed { r, row ->
            val chosen = cells[r]
            val rowOk = chosen == q.rowKeys[r]
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(C.surface)
                    .border(2.dp, if (revealed) (if (rowOk) C.good else C.bad) else C.line, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MathText("${r + 1}. $row", MaterialTheme.typography.bodyLarge, color = C.text)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    q.options.forEachIndexed { c, label ->
                        val on = chosen == c
                        val isKey = revealed && q.rowKeys[r] == c
                        val bg by animateColorAsState(
                            when { isKey -> C.good; revealed && on -> C.bad; on -> accent; else -> C.surface2 }, label = "cell",
                        )
                        Text(
                            label,
                            Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(bg)
                                .clickable(enabled = !revealed) { onPick(r, c) }.testTag("cell_${r}_$c")
                                .padding(vertical = 10.dp, horizontal = 6.dp),
                            color = if (on || isKey) Color.White else C.text, textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
        }
    }
}

/** Run of correct answers in a row, shown from 2. */
@Composable
private fun ComboBadge(combo: Int) {
    if (combo < 2) return
    Text("$combo beruntun", Modifier.clip(RoundedCornerShape(12.dp)).border(2.dp, C.line, RoundedCornerShape(12.dp))
        .padding(horizontal = 10.dp, vertical = 4.dp), color = C.muted, fontWeight = FontWeight.Bold)
}

@Composable
private fun PassageBox(text: String) {
    var open by remember { mutableStateOf(true) }
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(C.surface).border(2.dp, C.line, RoundedCornerShape(12.dp)).clickable { open = !open }.padding(16.dp),
    ) {
        Text(if (open) "Bacaan (ketuk untuk menutup)" else "Bacaan (ketuk untuk membuka)", color = C.muted, style = MaterialTheme.typography.labelMedium)
        AnimatedVisibility(open) {
            Text(text, color = C.text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
private fun OptionRow(i: Int, text: String, answer: Int, picked: Int?, accent: Color, onClick: () -> Unit) {
    val revealed = picked != null
    val isAnswer = i == answer
    val isPicked = i == picked
    val target = when {
        revealed && isAnswer -> C.good
        revealed && isPicked -> C.bad
        else -> C.line
    }
    val border by animateColorAsState(target, label = "ob")
    val bg by animateColorAsState(
        when {
            revealed && isAnswer -> C.good.copy(alpha = 0.15f)
            revealed && isPicked -> C.bad.copy(alpha = 0.15f)
            else -> C.surface
        }, label = "obg",
    )
    val shake = remember { Animatable(0f) }
    LaunchedEffect(revealed) {
        if (revealed && isPicked && !isAnswer) shake.animateTo(0f, keyframes { durationMillis = 420; -16f at 60; 14f at 140; -10f at 220; 6f at 300; 0f at 420 })
    }
    Row(
        Modifier.fillMaxWidth().offset { IntOffset(shake.value.roundToInt(), 0) }
            .clip(RoundedCornerShape(12.dp)).background(bg).border(2.dp, border, RoundedCornerShape(12.dp))
            .clickable(enabled = !revealed, onClick = onClick).testTag("opt_$i")
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(34.dp).clip(CircleShape).background(if (revealed && (isAnswer || isPicked)) target else accent.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                when { revealed && isAnswer -> "✓"; revealed && isPicked -> "✕"; else -> "${'A' + i}" },
                color = if (revealed && (isAnswer || isPicked)) Color.White else accent, fontWeight = FontWeight.Black,
            )
        }
        Spacer(Modifier.width(12.dp))
        MathText(text, MaterialTheme.typography.bodyLarge, Modifier.weight(1f), color = C.text)
    }
}

@Composable
private fun ResultView(total: Int, correct: Int, bestCombo: Int, xp: Int, color: Color, onContinue: () -> Unit) {
    val stars = starsFor(correct, total)
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp).testTag("quiz_result"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(Modifier.height(20.dp))
        if (total == 0) {
            Text("Belum ada soal yang salah.", color = C.text, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        } else {
            Text("$correct dari $total benar", color = C.text, style = MaterialTheme.typography.headlineMedium)
            Text("★".repeat(stars) + "☆".repeat(3 - stars), color = C.gold, fontSize = 36.sp, letterSpacing = 6.sp)
            ProgressRing(correct / total.toFloat(), color, size = 150.dp, stroke = 14.dp) {
                AnimatedNumber(correct * 100 / total, MaterialTheme.typography.headlineLarge, suffix = "%")
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ResultTile("XP", "+$xp", Modifier.weight(1f))
                ResultTile("Benar beruntun", "$bestCombo", Modifier.weight(1f))
            }
            Text("Bintang: 3 jika benar semua, 2 jika minimal 70%, selain itu 1.", color = C.muted,
                style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
            if (correct < total) {
                Text("${total - correct} soal yang salah disimpan di Progres, bagian Ulang soal salah.", color = C.muted, textAlign = TextAlign.Center)
            }
        }
        BouncyButton("Lanjut", Modifier.fillMaxWidth().testTag("result_continue"), onClick = onContinue)
    }
}

@Composable
private fun ResultTile(label: String, value: String, modifier: Modifier) {
    Column(
        modifier.clip(RoundedCornerShape(12.dp)).border(2.dp, C.line, RoundedCornerShape(12.dp)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(label, color = C.muted, style = MaterialTheme.typography.labelMedium)
        Text(value, color = C.text, style = MaterialTheme.typography.headlineMedium)
    }
}
