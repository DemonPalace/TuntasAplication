package id.demonpalace.tuntas

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun ProgressScreen(store: Store, onRetry: () -> Unit, onOpenStep: (Int) -> Unit) {
    store.tick // re-read day counters when they change
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text("Progres", color = C.text, style = MaterialTheme.typography.headlineLarge)
        LevelCard(store)
        CountdownCard(store)
        SectionTitle("Per mapel · ketuk untuk detail")
        SubjectRings(store, onOpenStep)
        SectionTitle("Aktivitas harian · ketuk batang")
        ActivityChart(store)
        SectionTitle("Lencana")
        Badges(store)
        SectionTitle("Catatan kesalahan")
        WrongCard(store, onRetry)
        SectionTitle("Ringkasan sesi (foto atau ketikan)")
        PhotoStrip(store)
        SectionTitle("Sumber materi")
        Sources()
        ResetButton(store)
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun LevelCard(store: Store) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(C.violet).padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProgressRing(store.levelProgress, Color.White, size = 96.dp, stroke = 10.dp) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("LEVEL", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelMedium)
                AnimatedNumber(store.level, MaterialTheme.typography.headlineLarge, Color.White)
            }
        }
        Spacer(Modifier.width(18.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            AnimatedNumber(store.xp, MaterialTheme.typography.headlineMedium, Color.White, " XP")
            Text("${100 - store.xp % 100} XP lagi ke level ${store.level + 1}", color = Color.White.copy(alpha = 0.9f))
            Text("${store.streak} hari berturut-turut", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CountdownCard(store: Store) {
    val today = LocalDate.now()
    val toExam = ChronoUnit.DAYS.between(today, LocalDate.parse(Content.EXAM_DATE)).coerceAtLeast(0)
    val toDeadline = ChronoUnit.DAYS.between(today, LocalDate.parse(Content.MATERIAL_DEADLINE)).coerceAtLeast(0)
    val materialSteps = Content.steps.filter { it.day <= 6 }
    val materialDone = materialSteps.count { it.id in store.done }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(C.surface).padding(16.dp)) {
            Text("TKA GELOMBANG 2", color = C.muted, style = MaterialTheme.typography.labelMedium)
            AnimatedNumber(toExam.toInt(), MaterialTheme.typography.headlineLarge, C.good, " hari")
            Text("2 sampai 5 November 2026", color = C.muted, style = MaterialTheme.typography.bodyMedium)
        }
        Column(Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(C.surface).padding(16.dp)) {
            Text("MATERI SELESAI", color = C.muted, style = MaterialTheme.typography.labelMedium)
            AnimatedNumber(materialDone, MaterialTheme.typography.headlineLarge, C.pink, "/${materialSteps.size}")
            Text(if (toDeadline > 0) "target sebelum 30 Okt ($toDeadline hari)" else "batas 30 Okt", color = C.muted, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun SubjectRings(store: Store, onOpenStep: (Int) -> Unit) {
    var open by remember { mutableStateOf<Subject?>(null) }
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(C.surface).padding(14.dp).animateContentSize()) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf(Subject.MAT, Subject.FIS, Subject.BIN, Subject.BIG).forEach { s ->
                val steps = Content.steps.filter { it.subject == s }
                val done = steps.count { it.id in store.done }
                val on = open == s
                Column(
                    Modifier.clip(RoundedCornerShape(12.dp)).background(if (on) s.color.copy(alpha = 0.15f) else Color.Transparent)
                        .clickable { open = if (on) null else s }.testTag("ring_${s.name}").padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ProgressRing(if (steps.isEmpty()) 0f else done / steps.size.toFloat(), s.color, size = 66.dp, stroke = 7.dp) {
                        Icon(s.icon, null, tint = s.color, modifier = Modifier.size(28.dp))
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(s.label, color = C.text, style = MaterialTheme.typography.labelMedium)
                    Text("$done/${steps.size}", color = s.color, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        AnimatedVisibility(open != null) {
            val s = open ?: Subject.MAT
            Column(Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Content.steps.forEachIndexed { idx, st ->
                    if (st.subject != s) return@forEachIndexed
                    val done = st.id in store.done
                    val unlocked = store.isUnlocked(idx)
                    Row(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(C.surface2)
                            .clickable(enabled = unlocked) { onOpenStep(idx) }.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(if (done) "Selesai" else if (unlocked) "Terbuka" else "Terkunci",
                            color = if (done) C.good else C.muted, style = MaterialTheme.typography.labelMedium, modifier = Modifier.width(64.dp))
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(st.title, color = C.text, style = MaterialTheme.typography.titleMedium)
                            Text("Hari ${st.day} · ${st.date}", color = C.muted, style = MaterialTheme.typography.labelMedium)
                        }
                        if (done && st.questions.isNotEmpty()) {
                            val stars = starsFor(store.bestScore(st.id), st.questions.size)
                            Text("★".repeat(stars) + "☆".repeat(3 - stars), color = C.gold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityChart(store: Store) {
    val days = (0..8).map { LocalDate.parse("2026-10-24").plusDays(it.toLong()) }
    val answered = days.map { store.questionsAnswered(it) }
    val minutes = days.map { store.focusMinutes(it) }
    val maxA = (answered.maxOrNull() ?: 0).coerceAtLeast(10)
    var sel by remember { mutableStateOf(days.indexOf(LocalDate.now()).takeIf { it >= 0 } ?: 0) }
    val grow = remember { Animatable(0f) }
    LaunchedEffect(Unit) { grow.animateTo(1f, tween(900, easing = FastOutSlowInEasing)) }
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(C.surface).padding(16.dp)) {
        Text(
            "${days[sel].dayOfMonth} ${if (days[sel].monthValue == 10) "Okt" else "Nov"}: ${answered[sel]} soal dijawab · ${minutes[sel] / 60} j ${minutes[sel] % 60} m fokus",
            color = C.text, style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth().height(130.dp), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.Bottom) {
            days.forEachIndexed { i, d ->
                val frac = (answered[i] / maxA.toFloat()).coerceIn(0.03f, 1f) * grow.value
                val isToday = d == LocalDate.now()
                Column(
                    Modifier.weight(1f).fillMaxHeight().clickable { sel = i }.testTag("bar_$i"),
                    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom,
                ) {
                    Box(
                        Modifier.fillMaxWidth().fillMaxHeight(frac * 0.82f)
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(
                                if (i == sel) C.violet else C.goodWash
                            ),
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("${d.dayOfMonth}", color = if (isToday) C.gold else C.muted, style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isToday) FontWeight.Black else FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun Badges(store: Store) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(C.surface).border(2.dp, C.line, RoundedCornerShape(12.dp)).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("${Badge.entries.count { it.name in store.badges }} dari ${Badge.entries.size} lencana", color = C.muted, style = MaterialTheme.typography.labelMedium)
        Badge.entries.forEach { b ->
            val got = b.name in store.badges
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(12.dp).clip(CircleShape).background(if (got) C.gold else C.line))
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(b.title, color = if (got) C.text else C.muted, style = MaterialTheme.typography.titleSmall)
                    Text(b.desc, color = C.muted, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun WrongCard(store: Store, onRetry: () -> Unit) {
    val n = store.wrong.size
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(C.surface).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedNumber(n, MaterialTheme.typography.headlineLarge, if (n == 0) C.good else C.bad)
            Spacer(Modifier.width(10.dp))
            Text(if (n == 0) "Belum ada soal yang salah." else "soal masih salah. Soal hilang dari daftar setelah dijawab benar.", color = C.muted)
        }
        store.wrong.take(4).mapNotNull { Content.allQuestions[it] }.forEach { (st, q) ->
            Text("• ${st.subject.label}: ${q.text.take(70)}…", color = C.text, style = MaterialTheme.typography.bodyMedium)
        }
        BouncyButton("Ulang soal salah", Modifier.fillMaxWidth().testTag("retry_wrong"), enabled = n > 0, onClick = onRetry)
    }
}

@Composable
private fun PhotoStrip(store: Store) {
    if (store.photos.isEmpty()) {
        Text("Belum ada ringkasan. Ringkasan tersimpan setelah Sesi Harian di tab Fokus selesai.", color = C.muted)
        return
    }
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        store.photos.reversed().forEach { path ->
            if (path.endsWith(".txt")) {
                val txt = remember(path) { runCatching { File(path).readText() }.getOrDefault("") }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(txt, Modifier.size(110.dp, 140.dp).clip(RoundedCornerShape(12.dp)).background(C.surface2).padding(10.dp),
                        color = C.text, style = MaterialTheme.typography.labelMedium, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                    Text(File(path).nameWithoutExtension.removePrefix("ringkasan_").take(10), color = C.muted, style = MaterialTheme.typography.labelMedium)
                }
                return@forEach
            }
            val bmp = remember(path) {
                runCatching {
                    val opts = BitmapFactory.Options().apply { inSampleSize = 8 }
                    BitmapFactory.decodeFile(path, opts)?.asImageBitmap()
                }.getOrNull()
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (bmp != null) {
                    Image(bmp, "Foto ringkasan", Modifier.size(110.dp, 140.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
                } else {
                    Box(Modifier.size(110.dp, 140.dp).clip(RoundedCornerShape(12.dp)).background(C.surface2), contentAlignment = Alignment.Center) { Text("Foto", color = C.muted) }
                }
                Text(File(path).nameWithoutExtension.removePrefix("ringkasan_").take(10), color = C.muted, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun Sources() {
    val ctx = LocalContext.current
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(C.surface).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Materi disusun dari kisi-kisi resmi TKA SMA 2026 (Pusmendik Kemendikdasmen) sebagaimana dikutip media berikut. Ketuk untuk membuka.",
            color = C.muted, style = MaterialTheme.typography.bodyMedium)
        Content.sources.forEach { s ->
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(C.surface2)
                    .clickable { runCatching { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(s.url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) } }
                    .padding(12.dp),
            ) {
                Text(s.title, color = C.text, style = MaterialTheme.typography.titleMedium)
                Text(s.note, color = C.muted, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun ResetButton(store: Store) {
    var confirm by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (!confirm) {
            Text("Reset progres", color = C.muted, modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable { confirm = true }.padding(10.dp))
        } else {
            Text("Semua progres, XP, dan lencana akan dihapus.", color = C.bad)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 8.dp)) {
                BouncyButton("Batal", Modifier.width(140.dp), secondary = true) { confirm = false }
                BouncyButton("Reset", Modifier.width(140.dp), color = C.bad) { store.resetAll(); confirm = false }
            }
        }
    }
}
