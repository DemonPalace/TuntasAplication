package id.demonpalace.tuntas

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val MIN_TYPED = 150

/** Always-composed helper: advances the session timer, pins the app and keeps the screen awake while a session runs. */
@Composable
fun SessionGuard(store: Store) {
    val ctx = LocalContext.current
    val activity = ctx as? Activity
    LaunchedEffect(Unit) {
        while (true) { store.advance(); delay(1000) }
    }
    val strict = store.phase == 1 || store.phase == 2
    DisposableEffect(strict) {
        val window = activity?.window
        if (strict) {
            window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            if (!isPinned(ctx)) runCatching { activity?.startLockTask() }
        } else {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            if (isPinned(ctx)) runCatching { activity?.stopLockTask() }
        }
        onDispose { }
    }
}

fun isPinned(ctx: Context): Boolean {
    val am = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return am.lockTaskModeState != ActivityManager.LOCK_TASK_MODE_NONE
}

@Composable
fun FocusScreen(store: Store) {
    Box(Modifier.fillMaxSize()) {
        AnimatedContent(store.phase, transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(200)) }, label = "phase") { p ->
            when (p) {
                0 -> SessionSetup(store)
                1, 2 -> SessionRunning(store)
                else -> SessionSummary(store)
            }
        }
    }
}

/** Quick picks under each duration; any value in between is reachable with the minus and plus buttons. */
private val studyQuick = listOf(25, 60, 120, 180, 300)
private val practiceQuick = listOf(10, 30, 60)

@Composable
private fun SessionSetup(store: Store) {
    val ctx = LocalContext.current
    store.tick
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text("Sesi Harian", color = C.text, style = MaterialTheme.typography.headlineLarge)
        Text("Urutan sesi: belajar, latihan soal, lalu ringkasan. Selama sesi, aplikasi disematkan dan layar tetap menyala.",
            color = C.muted, style = MaterialTheme.typography.bodyLarge)

        // The three phases in order.
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PhaseChip("1. Belajar", fmtMin(store.studyMin), C.violet, Modifier.weight(1f))
            PhaseChip("2. Soal", fmtMin(store.practiceMin), C.pink, Modifier.weight(1f))
            PhaseChip("3. Ringkasan", "foto/ketik", C.good, Modifier.weight(1f))
        }

        SectionTitle("Durasi")
        DurationCard("Belajar", store.studyMin, 5..600, studyQuick, "study") { store.setDurations(it, store.practiceMin) }
        DurationCard("Latihan soal", store.practiceMin, 0..240, practiceQuick, "practice") { store.setDurations(store.studyMin, it) }

        Text("Saat mulai, Android akan bertanya \"Sematkan aplikasi?\". Pilih Ya atau Mengerti. Kalau tidak muncul, aktifkan di Setelan, Keamanan, Sematkan aplikasi.",
            color = C.muted, style = MaterialTheme.typography.bodyMedium)

        BouncyButton("Mulai", Modifier.fillMaxWidth().testTag("start_session")) {
            store.startSession()
        }

        SectionTitle("Hari ini")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MiniStat("Sesi tuntas", "${store.sessionsDone()}", C.good, Modifier.weight(1f))
            MiniStat("Waktu fokus", fmtMin(store.focusMinutes()), C.violet, Modifier.weight(1f))
        }
    }
}

/** One duration: minus and plus in 5-minute steps, plus a row of quick picks. Saved as soon as it changes. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DurationCard(label: String, minutes: Int, range: IntRange, quick: List<Int>, tag: String, onChange: (Int) -> Unit) {
    Card {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(label, color = C.text, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                StepButton("−", minutes > range.first, "${tag}_minus") { onChange((minutes - 5).coerceIn(range)) }
                Text(fmtMin(minutes), color = C.text, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center,
                    modifier = Modifier.width(96.dp).testTag("${tag}_value"))
                StepButton("+", minutes < range.last, "${tag}_plus") { onChange((minutes + 5).coerceIn(range)) }
            }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                quick.forEach { q ->
                    val on = q == minutes
                    Box(
                        Modifier.clip(RoundedCornerShape(50)).background(if (on) C.accentWash else C.surface2)
                            .clickable { onChange(q) }.testTag("${tag}_q_$q").padding(horizontal = 12.dp, vertical = 8.dp),
                    ) {
                        Text(fmtMin(q), color = if (on) C.violet else C.muted, style = MaterialTheme.typography.labelLarge, maxLines = 1, softWrap = false)
                    }
                }
            }
        }
    }
}

@Composable
private fun StepButton(sign: String, enabled: Boolean, tag: String, onClick: () -> Unit) {
    Box(
        Modifier.size(40.dp).clip(CircleShape).background(C.surface2)
            .clickable(enabled = enabled, onClick = onClick).testTag(tag),
        contentAlignment = Alignment.Center,
    ) {
        Text(sign, color = if (enabled) C.text else C.faded, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun PhaseChip(label: String, value: String, color: Color, modifier: Modifier) {
    Column(
        modifier.clip(RoundedCornerShape(16.dp)).border(1.dp, C.line, RoundedCornerShape(16.dp)).padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(label, color = C.text, style = MaterialTheme.typography.labelLarge)
        Text(value, color = color, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MiniStat(label: String, value: String, color: Color, modifier: Modifier) {
    Column(modifier.clip(RoundedCornerShape(16.dp)).background(C.surface).border(1.dp, C.line, RoundedCornerShape(16.dp)).padding(14.dp)) {
        Text(label, color = C.muted, style = MaterialTheme.typography.labelMedium)
        Text(value, color = color, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun SessionRunning(store: Store) {
    val ctx = LocalContext.current
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var pinned by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true) { now = System.currentTimeMillis(); pinned = isPinned(ctx); delay(1000) }
    }
    val total = (if (store.phase == 1) store.studyMin else store.practiceMin) * 60_000L
    val left = (store.phaseEndAt - now).coerceAtLeast(0)
    val frac = 1f - left / total.toFloat()
    val color = if (store.phase == 1) C.violet else C.pink
    val endsAt = remember(store.phaseEndAt) {
        java.time.Instant.ofEpochMilli(store.phaseEndAt).atZone(java.time.ZoneId.systemDefault()).toLocalTime()
            .format(DateTimeFormatter.ofPattern("HH:mm"))
    }
    val current = Content.steps.firstOrNull { it.id !in store.done }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StepDot("1. Belajar", store.phase == 1, store.phase > 1, C.violet, Modifier.weight(1f))
            StepDot("2. Soal", store.phase == 2, store.phase > 2, C.pink, Modifier.weight(1f))
            StepDot("3. Ringkasan", false, false, C.good, Modifier.weight(1f))
        }
        Text(if (store.phase == 1) "WAKTU BELAJAR" else "WAKTU LATIHAN SOAL", color = color, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black)
        Box(Modifier.size(260.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) {
                val s = 22f
                val tl = Offset(s, s)
                val sz = Size(size.width - 2 * s, size.height - 2 * s)
                drawArc(C.line, 0f, 360f, false, tl, sz, style = Stroke(s))
                drawArc(color, -90f, 360f * frac, false, tl, sz, style = Stroke(s, cap = StrokeCap.Round))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(fmtClock(left), color = C.text, fontSize = 46.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                Text("tersisa", color = C.muted)
            }
        }
        Text("Fase ini berakhir pukul $endsAt.", color = C.text, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)

        Card(Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(if (store.phase == 1) "YANG DIPELAJARI SEKARANG" else "YANG DIKERJAKAN SEKARANG", color = C.muted, style = MaterialTheme.typography.labelMedium)
                Text(
                    if (store.phase == 1) (current?.let { "${it.subject.label}: ${it.title}" } ?: "Semua materi selesai. Ulang rumus inti.")
                    else "Kuis bab yang sedang dipelajari, tryout, atau Progres, bagian Ulang soal salah.",
                    color = C.text, style = MaterialTheme.typography.titleMedium,
                )
                Text("Materi dan soal ada di tab Jalur.", color = C.muted, style = MaterialTheme.typography.bodyMedium)
            }
        }

        if (!pinned) {
            BouncyButton("Sematkan lagi", Modifier.fillMaxWidth(),
                color = C.gold) {
                runCatching { (ctx as? Activity)?.startLockTask() }
            }
        }
        EmergencyButton { store.abortSession() }
    }
}

@Composable
private fun StepDot(label: String, active: Boolean, done: Boolean, color: Color, modifier: Modifier) {
    Text(
        (if (done) "✓ " else "") + label,
        modifier.clip(RoundedCornerShape(16.dp)).background(if (active) color else if (done) color.copy(alpha = 0.3f) else C.surface)
            .padding(vertical = 8.dp),
        color = if (active) Color.White else if (done) C.text else C.muted, textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelLarge,
    )
}

/** Must be held for 5 seconds; ends the session without counting it. */
@Composable
private fun EmergencyButton(onExit: () -> Unit) {
    val hold = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }
    Box(
        Modifier.fillMaxWidth().height(54.dp).clip(RoundedCornerShape(16.dp)).background(C.surface).border(1.dp, C.line, RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    job = scope.launch {
                        hold.animateTo(1f, tween(5000, easing = LinearEasing))
                        onExit()
                    }
                    tryAwaitRelease()
                    job?.cancel()
                    scope.launch { hold.animateTo(0f, tween(250)) }
                })
            }
            .testTag("emergency"),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(Modifier.fillMaxWidth(hold.value).height(54.dp).background(C.bad.copy(alpha = 0.6f)))
        Text(
            if (hold.value > 0f) "Tahan ${(5 - hold.value * 5).toInt() + 1} detik lagi" else "Keluar darurat: tahan 5 detik",
            color = C.text, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@Composable
private fun SessionSummary(store: Store) {
    val ctx = LocalContext.current
    var mode by remember { mutableIntStateOf(0) } // 0 choose, 1 typing
    var text by remember { mutableStateOf("") }
    var pendingFile by remember { mutableStateOf<File?>(null) }
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        val f = pendingFile
        if (ok && f != null && f.length() > 0) store.completeSession(f.absolutePath)
        else f?.delete()
    }
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text("Tahap 3 dari 3", color = C.good, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black)
            Text("Ringkasan hari ini", color = C.text, style = MaterialTheme.typography.headlineLarge)
            Text("Tulis rumus, konsep, dan soal yang salah hari ini. Pin dilepas agar kamera bisa dibuka. Sesi tercatat selesai setelah ringkasan tersimpan.",
                color = C.muted, style = MaterialTheme.typography.bodyLarge)
            if (mode == 0) {
                BouncyButton("Foto catatan", Modifier.fillMaxWidth().testTag("take_photo"),
                    color = C.good) {
                    val dir = File(ctx.filesDir, "ringkasan").apply { mkdirs() }
                    val f = File(dir, "ringkasan_${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss"))}.jpg")
                    pendingFile = f
                    val uri: Uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.files", f)
                    runCatching { camera.launch(uri) }
                }
                BouncyButton("Ketik catatan", Modifier.fillMaxWidth().testTag("type_notes"),
                    secondary = true) { mode = 1 }
            } else {
                OutlinedTextField(
                    text, { text = it },
                    Modifier.fillMaxWidth().heightIn(min = 220.dp).testTag("notes_field"),
                    placeholder = { Text("Contoh: GLBB s = v₀t + ½at². Tadi salah soal impuls karena lupa tanda negatif...", color = C.muted) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = C.good, unfocusedBorderColor = C.line, focusedTextColor = C.text, unfocusedTextColor = C.text),
                )
                val enough = text.trim().length >= MIN_TYPED
                LinearBar(text.trim().length / MIN_TYPED.toFloat(), if (enough) C.good else C.gold)
                Text(if (enough) "${text.trim().length} karakter. Sudah bisa disimpan." else "Minimal $MIN_TYPED karakter (${text.trim().length}/$MIN_TYPED).",
                    color = if (enough) C.good else C.muted, style = MaterialTheme.typography.bodyMedium)
                BouncyButton("Simpan", Modifier.fillMaxWidth().testTag("save_notes"), enabled = enough) {
                    val dir = File(ctx.filesDir, "ringkasan").apply { mkdirs() }
                    val f = File(dir, "ringkasan_${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss"))}.txt")
                    f.writeText(text.trim())
                    store.completeSession(f.absolutePath)
                }
                Text("Pakai foto", color = C.muted, modifier = Modifier.clip(RoundedCornerShape(10.dp)).clickable { mode = 0 }.padding(8.dp))
            }
        }
    }
}

fun fmtMin(min: Int): String = when {
    min >= 60 && min % 60 == 0 -> "${min / 60} jam"
    min >= 60 -> "${min / 60} j ${min % 60} m"
    else -> "$min menit"
}

fun fmtClock(ms: Long): String {
    val s = ms / 1000
    return "%d:%02d:%02d".format(s / 3600, (s % 3600) / 60, s % 60)
}
