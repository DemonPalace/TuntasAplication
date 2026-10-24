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

private val quotes = listOf(
    "HP bisa menunggu. TKA tidak.",
    "Satu jam fokus lebih berharga dari tiga jam sambil scroll.",
    "Kamu sedang mengalahkan versi dirimu yang malas.",
    "Rasa bosan itu tanda otakmu sedang bekerja.",
    "2 November makin dekat. Gas!",
    "Sedikit lagi. Jangan menyerah sekarang.",
)

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
fun FocusScreen(store: Store, blockedPkg: String?, onDismissBlocked: () -> Unit) {
    Box(Modifier.fillMaxSize()) {
        AnimatedContent(store.phase, transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(200)) }, label = "phase") { p ->
            when (p) {
                0 -> SessionSetup(store)
                1, 2 -> SessionRunning(store)
                else -> SessionSummary(store)
            }
        }
        AnimatedVisibility(blockedPkg != null, modifier = Modifier.align(Alignment.TopCenter)) {
            BlockedBanner(blockedPkg.orEmpty(), onDismissBlocked)
        }
    }
}

@Composable
private fun BlockedBanner(pkg: String, onDismiss: () -> Unit) {
    val ctx = LocalContext.current
    val label = remember(pkg) { appLabel(ctx, pkg) }
    Row(
        Modifier.padding(12.dp).fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(C.bad).clickable(onClick = onDismiss).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("🚫", fontSize = 28.sp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("$label diblokir", color = Color.White, style = MaterialTheme.typography.titleMedium)
            Text("Balik belajar dulu ya. Ketuk untuk menutup.", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

private data class Preset(val label: String, val study: Int, val practice: Int, val note: String)

private val presets = listOf(
    Preset("Sesi TKA", 300, 60, "5 jam belajar + 1 jam soal"),
    Preset("Setengah", 120, 30, "2 jam belajar + 30 menit soal"),
    Preset("Coba dulu", 25, 10, "25 menit + 10 menit"),
)

@Composable
private fun SessionSetup(store: Store) {
    val ctx = LocalContext.current
    store.tick
    var pick by remember { mutableIntStateOf(presets.indexOfFirst { it.study == store.studyMin && it.practice == store.practiceMin }.coerceAtLeast(0)) }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text("Sesi Harian", color = C.text, style = MaterialTheme.typography.headlineLarge)
        Text("Belajar → latihan soal → tulis ringkasan. Selama sesi berjalan, aplikasi disematkan, layar tetap menyala, dan aplikasi lain ditutup otomatis.",
            color = C.muted, style = MaterialTheme.typography.bodyLarge)

        // Visual timeline of the three phases.
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PhaseChip("📚", "Belajar", fmtMin(presets[pick].study), C.violet, Modifier.weight(1f))
            PhaseChip("✍️", "Soal", fmtMin(presets[pick].practice), C.pink, Modifier.weight(1f))
            PhaseChip("📷", "Ringkasan", "foto/ketik", C.good, Modifier.weight(1f))
        }

        SectionTitle("Pilih durasi")
        presets.forEachIndexed { i, p ->
            val on = i == pick
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp))
                    .background(if (on) C.violet.copy(alpha = 0.18f) else C.surface)
                    .border(2.dp, if (on) C.violet else C.surface, RoundedCornerShape(18.dp))
                    .clickable { pick = i }.testTag("preset_$i").padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text(p.label, color = C.text, style = MaterialTheme.typography.titleMedium)
                    Text(p.note, color = C.muted, style = MaterialTheme.typography.bodyMedium)
                }
                Text(if (on) "●" else "○", color = if (on) C.violet else C.muted, fontSize = 22.sp)
            }
        }

        val blockerOn = isBlockerEnabled(ctx)
        if (!blockerOn) {
            Text("⚠️ Pemblokir belum aktif, jadi aplikasi lain belum bisa ditutup otomatis. Aktifkan di tab Blokir.",
                color = C.gold, style = MaterialTheme.typography.bodyMedium)
        }
        Text("Saat mulai, Android akan bertanya \"Sematkan aplikasi?\". Pilih Ya / Mengerti. Kalau tidak muncul, aktifkan Setelan → Keamanan → Sematkan aplikasi.",
            color = C.muted, style = MaterialTheme.typography.bodyMedium)

        BouncyButton("MULAI SESI SEKARANG", Modifier.fillMaxWidth().testTag("start_session")) {
            store.setDurations(presets[pick].study, presets[pick].practice)
            store.startSession()
        }

        SectionTitle("Hari ini")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MiniStat("Sesi tuntas", "${store.sessionsDone()}", C.good, Modifier.weight(1f))
            MiniStat("Waktu fokus", fmtMin(store.focusMinutes()), C.violet, Modifier.weight(1f))
            MiniStat("Diblokir", "${store.blockedHits()}x", C.bad, Modifier.weight(1f))
        }
    }
}

@Composable
private fun PhaseChip(icon: String, label: String, value: String, color: Color, modifier: Modifier) {
    Column(
        modifier.clip(RoundedCornerShape(18.dp)).background(color.copy(alpha = 0.16f)).padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(icon, fontSize = 24.sp)
        Text(label, color = C.text, style = MaterialTheme.typography.labelLarge)
        Text(value, color = color, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MiniStat(label: String, value: String, color: Color, modifier: Modifier) {
    Column(modifier.clip(RoundedCornerShape(18.dp)).background(C.surface).padding(14.dp)) {
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
    val quote = quotes[((now / 60_000L) % quotes.size).toInt()]
    val current = Content.steps.firstOrNull { it.id !in store.done }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StepDot("📚 Belajar", store.phase == 1, store.phase > 1, C.violet, Modifier.weight(1f))
            StepDot("✍️ Soal", store.phase == 2, store.phase > 2, C.pink, Modifier.weight(1f))
            StepDot("📷 Ringkas", false, false, C.good, Modifier.weight(1f))
        }
        Text(if (store.phase == 1) "WAKTU BELAJAR" else "WAKTU LATIHAN SOAL", color = color, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black)
        Box(Modifier.size(260.dp), contentAlignment = Alignment.Center) {
            val pulse = rememberPulse()
            Canvas(Modifier.fillMaxSize()) {
                val s = 22f
                val tl = Offset(s, s)
                val sz = Size(size.width - 2 * s, size.height - 2 * s)
                drawCircle(color.copy(alpha = 0.12f * (1 - pulse)), radius = size.minDimension / 2 * (0.9f + 0.1f * pulse))
                drawArc(C.line, 0f, 360f, false, tl, sz, style = Stroke(s))
                drawArc(Brush.sweepGradient(listOf(color, C.gold, color)), -90f, 360f * frac, false, tl, sz, style = Stroke(s, cap = StrokeCap.Round))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(fmtClock(left), color = C.text, fontSize = 46.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                Text("tersisa", color = C.muted)
            }
        }
        Text("“$quote”", color = C.text, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)

        Card(Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(if (store.phase == 1) "YANG DIPELAJARI SEKARANG" else "YANG DIKERJAKAN SEKARANG", color = C.muted, style = MaterialTheme.typography.labelMedium)
                Text(
                    if (store.phase == 1) (current?.let { "${it.subject.emoji} ${it.title}" } ?: "Semua materi selesai, ulang rumus inti")
                    else "Kerjakan kuis langkah yang sedang dipelajari, tryout, atau Progres → Ulang soal salah.",
                    color = C.text, style = MaterialTheme.typography.titleMedium,
                )
                Text("Buka tab Jalur untuk materi dan soal. Kamu tetap di dalam aplikasi ini.", color = C.muted, style = MaterialTheme.typography.bodyMedium)
            }
        }

        if (!pinned) {
            BouncyButton("📌 Pin terlepas, sematkan lagi", Modifier.fillMaxWidth(),
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
        modifier.clip(RoundedCornerShape(50)).background(if (active) color else if (done) color.copy(alpha = 0.3f) else C.surface)
            .padding(vertical = 8.dp),
        color = if (active) Color.White else if (done) C.text else C.muted, textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelLarge,
    )
}

/** Must be held for 5 seconds; ends the session without XP. */
@Composable
private fun EmergencyButton(onExit: () -> Unit) {
    val hold = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }
    Box(
        Modifier.fillMaxWidth().height(54.dp).clip(RoundedCornerShape(18.dp)).background(C.surface)
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
            if (hold.value > 0f) "Tahan terus… ${(5 - hold.value * 5).toInt() + 1} detik" else "🆘 Darurat: tahan 5 detik untuk keluar (tanpa XP)",
            color = C.text, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@Composable
private fun SessionSummary(store: Store) {
    val ctx = LocalContext.current
    var mode by remember { mutableIntStateOf(0) } // 0 choose, 1 typing
    var text by remember { mutableStateOf("") }
    var celebrate by remember { mutableStateOf(false) }
    var pendingFile by remember { mutableStateOf<File?>(null) }
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        val f = pendingFile
        if (ok && f != null && f.length() > 0) { celebrate = true; store.completeSession(f.absolutePath) }
        else f?.delete()
    }
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text("✍️ Tahap terakhir", color = C.good, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black)
            Text("Tulis ringkasan hari ini", color = C.text, style = MaterialTheme.typography.headlineLarge)
            Text("Tulis rumus, konsep, dan soal yang salah tadi. Pin sudah dilepas supaya kamera bisa dibuka. Sesi baru tuntas (dan +100 XP) setelah ringkasan tersimpan.",
                color = C.muted, style = MaterialTheme.typography.bodyLarge)
            if (mode == 0) {
                BouncyButton("📷  FOTO TULISAN DI BUKU", Modifier.fillMaxWidth().testTag("take_photo"),
                    color = C.good) {
                    val dir = File(ctx.filesDir, "ringkasan").apply { mkdirs() }
                    val f = File(dir, "ringkasan_${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss"))}.jpg")
                    pendingFile = f
                    val uri: Uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.files", f)
                    runCatching { camera.launch(uri) }
                }
                BouncyButton("⌨️  TIDAK ADA BUKU? KETIK SAJA", Modifier.fillMaxWidth().testTag("type_notes"),
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
                Text(if (enough) "Cukup! Simpan sekarang." else "Minimal $MIN_TYPED karakter (${text.trim().length}/$MIN_TYPED). Tulis yang benar-benar kamu pelajari.",
                    color = if (enough) C.good else C.muted, style = MaterialTheme.typography.bodyMedium)
                BouncyButton("SIMPAN RINGKASAN", Modifier.fillMaxWidth().testTag("save_notes"), enabled = enough) {
                    val dir = File(ctx.filesDir, "ringkasan").apply { mkdirs() }
                    val f = File(dir, "ringkasan_${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss"))}.txt")
                    f.writeText(text.trim())
                    celebrate = true
                    store.completeSession(f.absolutePath)
                }
                Text("‹ Pakai foto saja", color = C.muted, modifier = Modifier.clip(RoundedCornerShape(10.dp)).clickable { mode = 0 }.padding(8.dp))
            }
        }
        if (celebrate) Confetti(Unit)
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
