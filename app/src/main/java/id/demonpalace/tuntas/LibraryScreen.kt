package id.demonpalace.tuntas

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val shelf = listOf(Subject.MAT, Subject.BIN, Subject.BIG, Subject.FIS)

private fun bookChapters(s: Subject) = Content.books[s].orEmpty()
private fun subCount(s: Subject) = bookChapters(s).sumOf { it.sections.size }
private fun readCount(store: Store, s: Subject) =
    bookChapters(s).sumOf { st -> st.sections.indices.count { "${st.id}#$it" in store.read } }

/** Bookmark "stepId#section" resolved to a step index and section, if it still exists. */
private fun resolveBookmark(store: Store, s: Subject): Pair<Int, Int>? {
    val mark = store.bookmark(s) ?: return null
    val (id, sec) = mark.split('#').let { it[0] to (it.getOrNull(1)?.toIntOrNull() ?: 0) }
    val idx = Content.steps.indexOfFirst { it.id == id }
    if (idx < 0 || sec !in Content.steps[idx].sections.indices) return null
    return idx to sec
}

/** The "Buku" tab: one textbook per subject, covering the whole TKA kisi-kisi. */
@Composable
fun LibraryScreen(store: Store, onOpenBook: (Subject) -> Unit, onRead: (stepIndex: Int, section: Int) -> Unit) {
    val totalSub = shelf.sumOf { subCount(it) }
    val totalRead = shelf.sumOf { readCount(store, it) }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text("Perpustakaan", color = C.text, style = MaterialTheme.typography.headlineLarge)
        Text("Empat buku sesuai kisi-kisi TKA SMA 2026: tiga mapel wajib dan Fisika.", color = C.muted, style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Pill("$totalRead/$totalSub subbab dibaca", C.good)
            Pill("${Content.questionCount} soal", C.blue)
        }
        shelf.chunked(2).forEach { pair ->
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                pair.forEach { s -> BookCover(store, s, Modifier.weight(1f)) { onOpenBook(s) } }
            }
        }
        // Continue where you left off, per book.
        val marks = shelf.mapNotNull { s -> resolveBookmark(store, s)?.let { s to it } }
        if (marks.isNotEmpty()) {
            Text("Lanjutkan membaca", color = C.text, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 6.dp))
            marks.forEach { (s, at) ->
                val step = Content.steps[at.first]
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(C.surface)
                        .border(2.dp, C.line, RoundedCornerShape(12.dp))
                        .clickable { onRead(at.first, at.second) }.testTag("continue_${s.name}")
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Rounded.Bookmark, null, tint = s.color, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("${s.label} · Bab ${chapterNo(step)}", color = s.color, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold)
                        Text(step.sections[at.second].title, color = C.text, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                    }
                    Icon(Icons.AutoMirrored.Rounded.MenuBook, null, tint = C.faded)
                }
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun BookCover(store: Store, s: Subject, modifier: Modifier, onClick: () -> Unit) {
    val src = remember { MutableInteractionSource() }
    val pressed by src.collectIsPressedAsState()
    val sub = subCount(s)
    val read = readCount(store, s)
    Box(modifier.aspectRatio(0.72f)) {
        // Pages peeking out behind the cover.
        Box(Modifier.fillMaxSize().padding(start = 8.dp, top = 6.dp).clip(RoundedCornerShape(6.dp, 16.dp, 16.dp, 6.dp)).background(Color(0xFFF1EADB)))
        Row(
            Modifier.fillMaxSize().padding(end = 6.dp, bottom = 6.dp)
                .clip(RoundedCornerShape(6.dp, 16.dp, 16.dp, 6.dp))
                .background(if (pressed) darken(s.color, 0.9f) else s.color)
                .clickable(src, null, onClick = onClick).testTag("book_${s.name}"),
        ) {
            Box(Modifier.width(12.dp).fillMaxHeight().background(darken(s.color)))
            Column(Modifier.weight(1f).padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    Modifier.size(48.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.22f)),
                    contentAlignment = Alignment.Center,
                ) { Icon(s.icon, null, tint = Color.White, modifier = Modifier.size(30.dp)) }
                Spacer(Modifier.weight(1f))
                Text("BUKU", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold)
                Text(s.label, color = Color.White, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 22.sp)
                Text("${bookChapters(s).size} bab · $sub subbab", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.labelMedium)
                LinearBar(if (sub == 0) 0f else read / sub.toFloat(), Color.White, Modifier.fillMaxWidth(), 6.dp)
            }
        }
    }
}

/** Table of contents of one book: chapters and their sub-chapters, with what you have read. */
@Composable
fun BookScreen(store: Store, subject: Subject, onBack: () -> Unit, onRead: (stepIndex: Int, section: Int) -> Unit, onQuiz: (stepIndex: Int) -> Unit) {
    val chapters = bookChapters(subject)
    Column(Modifier.fillMaxSize().background(C.bg).statusBarsPadding().navigationBarsPadding()) {
        Box(
            Modifier.fillMaxWidth().background(subject.color)
                .padding(horizontal = 8.dp, vertical = 10.dp),
        ) {
            Column {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Kembali", tint = Color.White,
                    modifier = Modifier.clip(CircleShape).clickable(onClick = onBack).padding(10.dp).testTag("back"))
                Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(subject.icon, null, tint = Color.White, modifier = Modifier.size(40.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Buku ${subject.label}", color = Color.White, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 26.sp)
                        Text("${chapters.size} bab · ${readCount(store, subject)}/${subCount(subject)} subbab dibaca",
                            color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text("DAFTAR ISI", color = C.muted, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold)
            chapters.forEachIndexed { n, step ->
                val idx = Content.steps.indexOf(step)
                Column(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(C.surface)
                        .border(2.dp, C.line, RoundedCornerShape(12.dp)).padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Row(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable { onRead(idx, 0) }.testTag("chapter_$n"),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Bab ${n + 1}", color = subject.color, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(Modifier.width(10.dp))
                        Text(step.title, color = C.text, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    }
                    Text(step.kisi, color = C.muted, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(4.dp))
                    step.sections.forEachIndexed { k, sec ->
                        val read = "${step.id}#$k" in store.read
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable { onRead(idx, k) }
                                .testTag("sub_${idx}_$k").padding(vertical = 8.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                if (read) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked, null,
                                tint = if (read) C.good else C.faded, modifier = Modifier.size(22.dp),
                            )
                            Spacer(Modifier.width(10.dp))
                            Text("${n + 1}.${k + 1}", color = C.muted, style = MaterialTheme.typography.labelLarge, modifier = Modifier.width(40.dp))
                            Text(sec.title, color = C.text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                        }
                    }
                    if (step.questions.isNotEmpty()) {
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(subject.color.copy(alpha = 0.12f))
                                .clickable { onQuiz(idx) }.testTag("chapter_quiz_$n").padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(Icons.Rounded.Quiz, null, tint = subject.color, modifier = Modifier.size(22.dp))
                            Spacer(Modifier.width(10.dp))
                            Text("Latihan bab · ${step.questions.size} soal", color = subject.color, fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
                            val best = store.bestScore(step.id)
                            if (best >= 0) Text("terbaik $best/${step.questions.size}", color = C.muted, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}
