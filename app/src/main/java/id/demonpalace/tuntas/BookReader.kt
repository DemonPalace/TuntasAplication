package id.demonpalace.tuntas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

/** Paper and ink for the book pages. */
private val Paper = Color(0xFFFFFCF5)
private val PaperEdge = Color(0xFFEDE6D6)
private val Ink = Color(0xFF34302A)
private val InkSoft = Color(0xFF6F675A)

private val bodyStyle = TextStyle(fontFamily = FontFamily.Serif, fontSize = 17.sp, lineHeight = 28.sp, color = Ink, textAlign = TextAlign.Justify)

/** What a single page of the book shows. */
private sealed interface BookPage {
    val section: Int
    data class Opener(override val section: Int) : BookPage
    data class Body(override val section: Int) : BookPage
    data class Worked(override val section: Int, val k: Int) : BookPage
    data class Summary(override val section: Int) : BookPage
    data class End(override val section: Int) : BookPage
}

/** Chapter number of [step] inside its subject's book (1-based), or 0 for steps outside the books. */
fun chapterNo(step: Step): Int = (Content.books[step.subject]?.indexOf(step) ?: -1) + 1

private fun pagesFor(step: Step, sections: List<Int>, withOpener: Boolean): List<BookPage> = buildList {
    if (withOpener) add(BookPage.Opener(sections.first()))
    sections.forEach { s ->
        val sec = step.sections[s]
        if (sec.explain.isNotEmpty() || sec.formulas.isNotEmpty()) add(BookPage.Body(s))
        sec.examples.indices.forEach { add(BookPage.Worked(s, it)) }
        add(BookPage.Summary(s))
    }
    add(BookPage.End(sections.last()))
}

/**
 * The reading room: one sub-chapter (from the map) or a whole chapter (from the Buku tab),
 * laid out as book pages you turn sideways.
 */
@Composable
fun BookReader(
    store: Store,
    step: Step,
    startSection: Int,
    wholeChapter: Boolean,
    onBack: () -> Unit,
    onFinished: () -> Unit,
    onStartQuiz: () -> Unit,
) {
    val sections = if (wholeChapter) step.sections.indices.toList() else listOf(startSection)
    val pages = remember(step.id, startSection, wholeChapter) {
        pagesFor(step, sections, withOpener = wholeChapter || startSection == 0)
    }
    val first = if (wholeChapter) pages.indexOfFirst { it !is BookPage.Opener && it.section == startSection }.coerceAtLeast(0) else 0
    val pager = rememberPagerState(initialPage = if (wholeChapter && startSection > 0) first else 0) { pages.size }
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val understood = remember(step.id) { mutableStateListOf<String>() }
    val color = step.subject.color
    val chapter = chapterNo(step)

    // Bookmark the place, and in chapter mode count a sub-chapter as read once its summary is reached.
    LaunchedEffect(pager.currentPage) {
        val p = pages[pager.currentPage]
        store.saveBookmark(step.subject, "${step.id}#${p.section}")
        if (wholeChapter && p is BookPage.Summary) store.markRead(step, p.section)
        if (pager.currentPage > 0) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    Column(Modifier.fillMaxSize().background(PaperEdge).statusBarsPadding().navigationBarsPadding()) {
        Row(Modifier.fillMaxWidth().background(C.bg).padding(horizontal = 6.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Kembali", tint = C.text,
                modifier = Modifier.clip(CircleShape).clickable(onClick = onBack).padding(10.dp).testTag("back"))
            Column(Modifier.weight(1f)) {
                Text(
                    if (chapter > 0) "${step.subject.label.uppercase()} · BAB $chapter" else step.subject.label.uppercase(),
                    color = color, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold,
                )
                val sec = pages[pager.currentPage].section
                Text(step.sections[sec].title, color = C.text, style = MaterialTheme.typography.titleMedium, maxLines = 1)
            }
            Pill("${understood.size} paham", if (understood.isNotEmpty()) C.good else C.muted)
            Spacer(Modifier.width(8.dp))
        }
        LinearBar((pager.currentPage + 1f) / pages.size, color, Modifier.fillMaxWidth().background(C.bg).padding(horizontal = 14.dp, vertical = 6.dp), 8.dp)

        HorizontalPager(
            pager, Modifier.weight(1f).fillMaxWidth().testTag("book_pager"),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp), pageSpacing = 14.dp,
            beyondViewportPageCount = 1,
        ) { page ->
            val off = (pager.currentPage - page) + pager.currentPageOffsetFraction
            Box(
                Modifier.fillMaxSize()
                    .graphicsLayer {
                        // Pages swing around the spine like a book being turned.
                        cameraDistance = 14f * density
                        transformOrigin = TransformOrigin(if (off > 0) 1f else 0f, 0.5f)
                        rotationY = (-off * 55f).coerceIn(-90f, 90f)
                        alpha = 1f - 0.3f * off.absoluteValue.coerceIn(0f, 1f)
                    }
                    .drawWithContent {
                        drawContent()
                        drawRect(Color.Black.copy(alpha = 0.22f * off.absoluteValue.coerceIn(0f, 1f)))
                    },
            ) {
                PaperPage(page + 1, pages.size, color) {
                    when (val p = pages[page]) {
                        is BookPage.Opener -> OpenerPage(step, chapter)
                        is BookPage.Body -> BodyPage(step, p.section, chapter)
                        is BookPage.Worked -> WorkedPage(step, p.section, p.k)
                        is BookPage.Summary -> SummaryPage(step, p.section, understood)
                        is BookPage.End -> EndPage(store, step, p.section, wholeChapter, onFinished, onStartQuiz)
                    }
                }
            }
        }

        Row(
            Modifier.fillMaxWidth().background(C.bg).padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TurnButton(Icons.AutoMirrored.Rounded.KeyboardArrowLeft, "Halaman sebelumnya", pager.currentPage > 0, color, Modifier.testTag("prev_card")) {
                scope.launch { pager.animateScrollToPage(pager.currentPage - 1) }
            }
            Text("hal. ${pager.currentPage + 1} / ${pages.size}", color = C.muted, style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
            TurnButton(Icons.AutoMirrored.Rounded.KeyboardArrowRight, "Halaman berikutnya", pager.currentPage < pages.size - 1, color, Modifier.testTag("next_card")) {
                scope.launch { pager.animateScrollToPage(pager.currentPage + 1) }
            }
        }
    }
}

@Composable
private fun TurnButton(icon: androidx.compose.ui.graphics.vector.ImageVector, desc: String, enabled: Boolean, color: Color, modifier: Modifier, onClick: () -> Unit) {
    val bg by animateColorAsState(if (enabled) color else C.surface2, label = "turn")
    Box(
        modifier.size(54.dp).clip(RoundedCornerShape(12.dp)).background(bg).clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) { Icon(icon, desc, tint = if (enabled) Color.White else C.faded, modifier = Modifier.size(32.dp)) }
}

/** The physical page: cream paper, a spine line in the subject color, page number at the foot. */
@Composable
private fun PaperPage(number: Int, total: Int, color: Color, content: @Composable () -> Unit) {
    Box(
        Modifier.fillMaxSize().clip(RoundedCornerShape(topEnd = 18.dp, bottomEnd = 18.dp, topStart = 4.dp, bottomStart = 4.dp))
            .background(Paper).border(1.dp, PaperEdge, RoundedCornerShape(topEnd = 18.dp, bottomEnd = 18.dp, topStart = 4.dp, bottomStart = 4.dp)),
    ) {
        Box(Modifier.fillMaxHeight().width(3.dp).background(color.copy(alpha = 0.55f)))
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(start = 26.dp, end = 20.dp, top = 20.dp, bottom = 12.dp)) {
            content()
            Spacer(Modifier.height(24.dp))
            Text("$number / $total", color = InkSoft, fontFamily = FontFamily.Serif, fontSize = 13.sp,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}

/** Upper-case words in the lesson text are the author's emphasis; show them bold in the subject color. */
private fun emphasize(text: String, color: Color, dropCap: Boolean = false): AnnotatedString = buildAnnotatedString {
    var start = 0
    if (dropCap && text.isNotEmpty()) {
        withStyle(SpanStyle(fontSize = 38.sp, fontWeight = FontWeight.Bold, color = color, fontFamily = FontFamily.Serif)) { append(text[0]) }
        start = 1
    }
    val rx = Regex("\\b[A-Z]{3,}\\b")
    var last = start
    rx.findAll(text, start).forEach { m ->
        append(text.substring(last, m.range.first))
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = darken(color))) { append(m.value) }
        last = m.range.last + 1
    }
    append(text.substring(last))
}

@Composable
private fun OpenerPage(step: Step, chapter: Int) {
    val color = step.subject.color
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(12.dp))
        Text(if (chapter > 0) "BAB $chapter" else step.subject.label.uppercase(), color = color,
            fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 20.sp, letterSpacing = 4.sp)
        Spacer(Modifier.height(14.dp))
        Box(
            Modifier.size(96.dp).clip(CircleShape).background(color.copy(alpha = 0.15f))
                .border(3.dp, color, CircleShape),
            contentAlignment = Alignment.Center,
        ) { Icon(step.subject.icon, null, tint = color, modifier = Modifier.size(52.dp)) }
        Spacer(Modifier.height(16.dp))
        Text(step.title, color = Ink, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 28.sp,
            lineHeight = 34.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(10.dp))
        Text(step.kisi, color = InkSoft, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic, fontSize = 15.sp,
            lineHeight = 22.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxWidth(0.4f).height(2.dp).background(color.copy(alpha = 0.5f)))
        Spacer(Modifier.height(16.dp))
    }
    Text("ISI BAB", color = color, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.ExtraBold)
    Spacer(Modifier.height(8.dp))
    step.sections.forEachIndexed { i, s ->
        Row(Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalAlignment = Alignment.Top) {
            Text(if (chapter > 0) "$chapter.${i + 1}" else "${i + 1}", color = color, fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.width(44.dp))
            Text(s.title, color = Ink, fontFamily = FontFamily.Serif, fontSize = 16.sp, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun SubHeading(step: Step, section: Int, chapter: Int) {
    val color = step.subject.color
    Text(if (chapter > 0) "$chapter.${section + 1}" else "${section + 1}", color = color, fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Text(step.sections[section].title, color = Ink, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold,
        fontSize = 26.sp, lineHeight = 32.sp)
    Spacer(Modifier.height(6.dp))
    Box(Modifier.width(56.dp).height(3.dp).clip(RoundedCornerShape(50)).background(color))
    Spacer(Modifier.height(14.dp))
}

@Composable
private fun BodyPage(step: Step, section: Int, chapter: Int) {
    val sec = step.sections[section]
    val color = step.subject.color
    SubHeading(step, section, chapter)
    sec.explain.forEachIndexed { i, para ->
        if (para.startsWith(GLOSS)) {
            // Indonesian gloss under an English paragraph (B. Inggris book).
            Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min).testTag("gloss")) {
                Box(Modifier.width(2.dp).fillMaxHeight().background(color))
                Text(emphasize(para.removePrefix(GLOSS), color), style = bodyStyle.copy(fontStyle = FontStyle.Italic,
                    fontSize = 15.sp, lineHeight = 22.sp, color = InkSoft), modifier = Modifier.padding(start = 10.dp))
            }
        } else {
            MathText(para, bodyStyle, decorate = { piece, first -> emphasize(piece, color, dropCap = i == 0 && first) })
        }
        Spacer(Modifier.height(12.dp))
    }
    if (sec.formulas.isNotEmpty()) {
        Column(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(color.copy(alpha = 0.09f))
                .border(2.dp, color.copy(alpha = 0.45f), RoundedCornerShape(14.dp)).padding(14.dp).testTag("formula_box"),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("RUMUS", color = darken(color), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
            sec.formulas.forEach {
                MathBlock(it, TextStyle(color = Ink, fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 26.sp))
            }
        }
    }
}

@Composable
private fun WorkedPage(step: Step, section: Int, k: Int) {
    val ex = step.sections[section].examples[k]
    val color = step.subject.color
    var shown by remember(step.id, section, k) { mutableIntStateOf(0) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("CONTOH SOAL ${k + 1}", color = Color.White, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black,
            modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(color).padding(horizontal = 10.dp, vertical = 4.dp))
        Spacer(Modifier.width(8.dp))
        Text(step.sections[section].title, color = InkSoft, style = MaterialTheme.typography.labelMedium, maxLines = 1)
    }
    Spacer(Modifier.height(14.dp))
    MathText(ex.problem, bodyStyle.copy(fontStyle = FontStyle.Italic, textAlign = TextAlign.Start))
    Spacer(Modifier.height(16.dp))
    Text("Penyelesaian", color = Ink, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 19.sp)
    Spacer(Modifier.height(8.dp))
    ex.steps.forEachIndexed { i, s ->
        AnimatedVisibility(i < shown, enter = fadeIn() + expandVertically()) {
            Row(Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalAlignment = Alignment.Top) {
                Box(Modifier.size(26.dp).clip(CircleShape).background(color.copy(alpha = 0.18f)), contentAlignment = Alignment.Center) {
                    Text("${i + 1}", color = darken(color), fontWeight = FontWeight.Black, fontSize = 13.sp)
                }
                Spacer(Modifier.width(10.dp))
                MathText(s, bodyStyle.copy(textAlign = TextAlign.Start), Modifier.weight(1f))
            }
        }
    }
    AnimatedVisibility(shown >= ex.steps.size, enter = fadeIn() + expandVertically()) {
        Column(
            Modifier.padding(top = 10.dp).fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(C.goodWash)
                .border(2.dp, C.good, RoundedCornerShape(14.dp)).padding(14.dp).testTag("example_answer"),
        ) {
            Text("JAWABAN", color = darken(C.good), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black)
            MathText(ex.answer, TextStyle(color = Ink, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 18.sp))
        }
    }
    if (shown < ex.steps.size) {
        Spacer(Modifier.height(10.dp))
        Text("Kerjakan di kertas dulu, lalu buka langkah satu per satu.", color = InkSoft, fontFamily = FontFamily.Serif,
            fontStyle = FontStyle.Italic, fontSize = 14.sp)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            BouncyButton("Langkah ${shown + 1}", Modifier.weight(1f).testTag("reveal_step"), color = color) { shown++ }
            Text("Lihat semua", color = darken(color), fontWeight = FontWeight.Bold,
                modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable { shown = ex.steps.size }.padding(10.dp).testTag("reveal_all"))
        }
    }
}

@Composable
private fun SummaryPage(step: Step, section: Int, understood: MutableList<String>) {
    val sec = step.sections[section]
    val color = step.subject.color
    if (sec.pitfalls.isNotEmpty()) {
        Column(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFFFFF3E0))
                .border(2.dp, C.pink, RoundedCornerShape(14.dp)).padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Jebakan", color = darken(C.pink), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black)
            sec.pitfalls.forEach { MathText("• $it", bodyStyle.copy(fontSize = 16.sp, lineHeight = 25.sp, textAlign = TextAlign.Start)) }
        }
        Spacer(Modifier.height(18.dp))
    }
    Text("Rangkuman", color = Ink, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 24.sp)
    Text("Ketuk poin yang sudah kamu pahami.", color = InkSoft, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic, fontSize = 14.sp)
    Spacer(Modifier.height(10.dp))
    sec.points.forEachIndexed { i, pt ->
        val key = "$section:$i"
        val on = key in understood
        val bg by animateColorAsState(if (on) color.copy(alpha = 0.16f) else Color.Transparent, label = "pt")
        Row(
            Modifier.fillMaxWidth().padding(vertical = 4.dp).clip(RoundedCornerShape(12.dp)).background(bg)
                .border(1.5.dp, if (on) color else PaperEdge, RoundedCornerShape(12.dp))
                .clickable { if (on) understood.remove(key) else understood.add(key) }
                .testTag("point_$key").padding(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(Modifier.size(26.dp).clip(CircleShape).background(if (on) color else C.line), contentAlignment = Alignment.Center) {
                Text(if (on) "✓" else "${i + 1}", color = if (on) Color.White else C.muted, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(12.dp))
            MathText(pt, bodyStyle.copy(fontSize = 16.sp, lineHeight = 25.sp, textAlign = TextAlign.Start), Modifier.weight(1f))
        }
    }
}

@Composable
private fun EndPage(store: Store, step: Step, section: Int, wholeChapter: Boolean, onFinished: () -> Unit, onStartQuiz: () -> Unit) {
    val color = step.subject.color
    val lastOfStep = section == step.sections.lastIndex
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Spacer(Modifier.height(30.dp))
        Text(if (wholeChapter) "Akhir bab" else "Akhir subbab", color = Ink, fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold, fontSize = 28.sp)
        Text(
            when {
                wholeChapter && step.questions.isNotEmpty() -> "Latihan bab ini: ${step.questions.size} soal."
                wholeChapter -> "Bab ini tidak punya latihan."
                lastOfStep && step.questions.isNotEmpty() -> "Berikutnya: latihan bab, ${step.questions.size} soal."
                else -> "Berikutnya: ${step.sections.getOrNull(section + 1)?.title ?: "langkah berikutnya"}."
            },
            color = InkSoft, fontFamily = FontFamily.Serif, fontSize = 16.sp, textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(10.dp))
        if (wholeChapter) {
            if (step.questions.isNotEmpty()) BouncyButton("Latihan bab ini", Modifier.fillMaxWidth().testTag("start_quiz"), color = color, onClick = onStartQuiz)
            BouncyButton("Kembali ke buku", Modifier.fillMaxWidth().testTag("finish_read"), secondary = true, onClick = onFinished)
        } else {
            val already = "${step.id}#$section" in store.read
            BouncyButton(if (already) "Selesai" else "Selesai (+10 XP)", Modifier.fillMaxWidth().testTag("finish_read"), color = color) {
                store.markRead(step, section); onFinished()
            }
        }
    }
}
