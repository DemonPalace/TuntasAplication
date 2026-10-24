package id.demonpalace.tuntas

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LessonScreen(store: Store, step: Step, onBack: () -> Unit, onStartQuiz: () -> Unit, onFinishReview: () -> Unit) {
    val pages = step.sections.size + 1
    val pager = rememberPagerState { pages }
    val scope = rememberCoroutineScope()
    // Points the reader ticked as understood, keyed "section:point".
    val understood = remember(step.id) { mutableStateListOf<String>() }
    val totalPoints = step.sections.sumOf { it.points.size }

    Column(Modifier.fillMaxSize().background(C.bg).statusBarsPadding().navigationBarsPadding()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Kembali", tint = C.text,
                modifier = Modifier.clip(CircleShape).clickable(onClick = onBack).padding(10.dp).testTag("back"))
            Column(Modifier.weight(1f)) {
                Text("${step.subject.emoji} ${step.subject.label} · Hari ${step.day}", color = step.subject.color, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Text(step.title, color = C.text, style = MaterialTheme.typography.titleMedium, maxLines = 1)
            }
            Pill("${understood.size}/$totalPoints paham", if (understood.size == totalPoints) C.good else C.muted)
            Spacer(Modifier.width(8.dp))
        }
        // Segmented progress like story bars.
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(pages) { i ->
                val on = i <= pager.currentPage
                val c by animateColorAsState(if (on) step.subject.color else C.line, label = "seg")
                Box(Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(50)).background(c))
            }
        }
        HorizontalPager(pager, Modifier.weight(1f).fillMaxWidth(), contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp), pageSpacing = 12.dp) { page ->
            val offset = ((pager.currentPage - page) + pager.currentPageOffsetFraction).absoluteValue
            Box(
                Modifier.fillMaxSize().padding(vertical = 16.dp).graphicsLayer {
                    val s = 1f - 0.08f * offset.coerceIn(0f, 1f)
                    scaleX = s; scaleY = s; alpha = 1f - 0.4f * offset.coerceIn(0f, 1f)
                    rotationZ = (pager.currentPage - page + pager.currentPageOffsetFraction) * -4f
                },
            ) {
                if (page < step.sections.size) {
                    SectionCard(step, page, understood)
                } else {
                    FinalCard(store, step, understood.size, totalPoints, onStartQuiz, onFinishReview)
                }
            }
        }
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            BouncyButton("‹ Sebelumnya", Modifier.weight(1f), enabled = pager.currentPage > 0,
                secondary = true) {
                scope.launch { pager.animateScrollToPage(pager.currentPage - 1) }
            }
            BouncyButton("Berikutnya ›", Modifier.weight(1f).testTag("next_card"), enabled = pager.currentPage < pages - 1,
                color = step.subject.color) {
                scope.launch { pager.animateScrollToPage(pager.currentPage + 1) }
            }
        }
    }
}

@Composable
private fun SectionCard(step: Step, page: Int, understood: MutableList<String>) {
    val sec = step.sections[page]
    Column(
        Modifier.fillMaxSize().clip(RoundedCornerShape(28.dp)).background(C.surface)
            .border(2.dp, step.subject.color.copy(alpha = 0.35f), RoundedCornerShape(28.dp))
            .verticalScroll(rememberScrollState()).padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("KARTU ${page + 1} DARI ${step.sections.size}", color = step.subject.color, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold)
        Text(sec.title, color = C.text, style = MaterialTheme.typography.headlineMedium)
        Text("Ketuk tiap poin kalau sudah paham ✓", color = C.muted, style = MaterialTheme.typography.bodyMedium)
        sec.points.forEachIndexed { i, pt ->
            val key = "$page:$i"
            val on = key in understood
            val bg by animateColorAsState(if (on) step.subject.color.copy(alpha = 0.18f) else C.surface2, label = "pt")
            val tickScale by animateFloatAsState(if (on) 1.1f else 0.9f, spring(Spring.DampingRatioHighBouncy), label = "tick")
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(bg)
                    .clickable { if (on) understood.remove(key) else understood.add(key) }
                    .testTag("point_$key")
                    .padding(14.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Box(
                    Modifier.size(26.dp).scale(tickScale).clip(CircleShape)
                        .background(if (on) step.subject.color else C.line),
                    contentAlignment = Alignment.Center,
                ) { Text(if (on) "✓" else "${i + 1}", color = if (on) Color.White else C.muted, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                Spacer(Modifier.width(12.dp))
                Text(pt, color = C.text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun FinalCard(store: Store, step: Step, understood: Int, total: Int, onStartQuiz: () -> Unit, onFinishReview: () -> Unit) {
    val ready = understood == total
    val glow by animateDpAsState(if (ready) 3.dp else 1.dp, label = "glow")
    Column(
        Modifier.fillMaxSize().clip(RoundedCornerShape(28.dp))
            .background(step.subject.color.copy(alpha = 0.10f))
            .border(glow, step.subject.color, RoundedCornerShape(28.dp))
            .verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Spacer(Modifier.height(10.dp))
        val bob = rememberBob()
        Text(if (step.isReview) "📝" else "🎯", fontSize = 64.sp, modifier = Modifier.graphicsLayer { translationY = bob })
        Text(
            if (step.isReview) "Ringkasan selesai dibaca?" else "Siap uji pemahaman?",
            color = C.text, style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            if (ready) "Mantap, semua poin sudah kamu tandai paham."
            else "Kamu baru menandai $understood dari $total poin. Boleh lanjut, tapi geser balik kalau masih ragu.",
            color = C.muted, style = MaterialTheme.typography.bodyLarge,
        )
        Text("Lingkup materi: ${step.kisi}", color = C.muted, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
        if (step.isReview) {
            BouncyButton("TANDAI SELESAI  ✓", Modifier.fillMaxWidth().testTag("finish_review")) {
                store.finishStep(step, 0); onFinishReview()
            }
        } else {
            BouncyButton("MULAI LATIHAN  (${step.questions.size} soal)", Modifier.fillMaxWidth().testTag("start_quiz"), onClick = onStartQuiz)
        }
    }
}
