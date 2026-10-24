package id.demonpalace.tuntas

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Timer
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

sealed interface Route {
    data class Home(val tab: Int) : Route
    /** One sub-chapter opened from the map, by node index. */
    data class Read(val node: Int) : Route
    /** Table of contents of one book. */
    data class Book(val subject: Subject) : Route
    /** A whole chapter in the reader, starting at [section]; [back] is where closing it returns. */
    data class Chapter(val index: Int, val section: Int, val back: Route) : Route
    data class Quiz(val index: Int, val back: Route) : Route
    data object Retry : Route
    data object Settings : Route
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = Store.get(this)
        setContent {
            val dark = when (store.themeMode) { 1 -> false; 2 -> true; else -> isSystemInDarkTheme() }
            DisposableEffect(dark) {
                val bar = if (dark) SystemBarStyle.dark(0xFF121318.toInt()) else SystemBarStyle.light(0xFFF6F5F1.toInt(), 0xFFF6F5F1.toInt())
                val nav = if (dark) SystemBarStyle.dark(0xFF1C1E24.toInt()) else SystemBarStyle.light(android.graphics.Color.WHITE, android.graphics.Color.WHITE)
                enableEdgeToEdge(statusBarStyle = bar, navigationBarStyle = nav)
                onDispose {}
            }
            JalurTheme(dark = dark) {
                App(store)
            }
        }
    }

}

private data class TabItem(val label: String, val icon: ImageVector)

private val tabs = listOf(
    TabItem("Jalur", Icons.Rounded.Map),
    TabItem("Buku", Icons.AutoMirrored.Rounded.MenuBook),
    TabItem("Progres", Icons.Rounded.BarChart),
    TabItem("Fokus", Icons.Rounded.Timer),
)

@Composable
fun App(store: Store) {
    var route by remember { mutableStateOf<Route>(Route.Home(0)) }
    var lastTab by remember { mutableStateOf(0) }


    fun go(r: Route) {
        if (r is Route.Home) lastTab = r.tab
        route = r
    }

    BackHandler(enabled = route != Route.Home(0)) {
        go(
            when (val r = route) {
                is Route.Quiz -> r.back
                is Route.Chapter -> r.back
                is Route.Read -> Route.Home(0)
                is Route.Book -> Route.Home(1)
                Route.Retry -> Route.Home(2)
                Route.Settings -> Route.Home(2)
                is Route.Home -> Route.Home(0)
            },
        )
    }

    SessionGuard(store)
    Box(Modifier.fillMaxSize().background(C.bg)) {
        AnimatedContent(
            targetState = route,
            transitionSpec = {
                val forward = depth(targetState) >= depth(initialState)
                if (initialState is Route.Home && targetState is Route.Home) {
                    fadeIn(tween(220)) togetherWith fadeOut(tween(160))
                } else {
                    (slideInHorizontally(tween(320)) { if (forward) it else -it / 3 } + fadeIn()) togetherWith
                        (slideOutHorizontally(tween(320)) { if (forward) -it / 3 else it } + fadeOut())
                }
            },
            label = "route",
            modifier = Modifier.fillMaxSize(),
        ) { r ->
            when (r) {
                is Route.Home -> Column(Modifier.fillMaxSize()) {
                    Box(Modifier.weight(1f).statusBarsPadding()) {
                        when (r.tab) {
                            0 -> PathScreen(store) { i ->
                                val n = Content.nodes[i]
                                go(if (n.isQuiz) Route.Quiz(Content.steps.indexOf(n.step), Route.Home(0)) else Route.Read(i))
                            }
                            1 -> LibraryScreen(store, onOpenBook = { go(Route.Book(it)) }, onRead = { i, s -> go(Route.Chapter(i, s, Route.Home(1))) })
                            2 -> ProgressScreen(store, onRetry = { go(Route.Retry) }, onSettings = { go(Route.Settings) }, onOpenStep = { i ->
                                go(if (Content.steps[i].sections.isNotEmpty()) Route.Chapter(i, 0, Route.Home(2)) else Route.Quiz(i, Route.Home(2)))
                            })
                            else -> FocusScreen(store)
                        }
                    }
                    BottomBar(r.tab) { go(Route.Home(it)) }
                }
                is Route.Read -> {
                    val n = Content.nodes[r.node]
                    BookReader(
                        store, n.step, n.section, wholeChapter = false,
                        onBack = { go(Route.Home(0)) },
                        onFinished = { go(Route.Home(0)) },
                        onStartQuiz = { go(Route.Quiz(Content.steps.indexOf(n.step), Route.Home(0))) },
                    )
                }
                is Route.Book -> BookScreen(
                    store, r.subject,
                    onBack = { go(Route.Home(1)) },
                    onRead = { i, s -> go(Route.Chapter(i, s, r)) },
                    onQuiz = { i -> go(Route.Quiz(i, r)) },
                )
                is Route.Chapter -> BookReader(
                    store, Content.steps[r.index], r.section, wholeChapter = true,
                    onBack = { go(r.back) },
                    onFinished = { go(r.back) },
                    onStartQuiz = { go(Route.Quiz(r.index, r.back)) },
                )
                is Route.Quiz -> {
                    val step = Content.steps[r.index]
                    QuizScreen(
                        store, step.title, step.subject.color, step.questions,
                        onExit = { go(r.back) },
                        onDone = { correct -> store.finishStep(step, correct) },
                        onContinue = { go(r.back) },
                    )
                }
                Route.Settings -> SettingsScreen(store, onBack = { go(Route.Home(2)) })
                Route.Retry -> {
                    val qs = remember { store.wrong.mapNotNull { Content.allQuestions[it]?.second }.shuffled() }
                    QuizScreen(
                        store, "Ulang soal yang salah", C.pink, qs,
                        onExit = { go(Route.Home(2)) },
                        onDone = {},
                        onContinue = { go(Route.Home(2)) },
                    )
                }
            }
        }
    }
}

private fun depth(r: Route) = when (r) {
    is Route.Home -> 0
    is Route.Book -> 1
    is Route.Read, is Route.Chapter, Route.Retry, Route.Settings -> 2
    is Route.Quiz -> 3
}

@Composable
private fun BottomBar(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(C.surface).drawBehind { drawLine(C.line, Offset(0f, 0f), Offset(size.width, 0f), 2f) }
            .navigationBarsPadding().padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        tabs.forEachIndexed { i, t ->
            val on = i == selected
            Column(
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(remember { MutableInteractionSource() }, null) { onSelect(i) }
                    .testTag("tab_$i")
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    Modifier.clip(RoundedCornerShape(50))
                        .background(if (on) C.accentWash else C.surface)
                        .padding(horizontal = 20.dp, vertical = 5.dp),
                ) {
                    Icon(t.icon, t.label, tint = if (on) C.violet else C.muted, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.height(4.dp))
                Text(t.label, color = if (on) C.text else C.muted, style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (on) FontWeight.Bold else FontWeight.Medium)
            }
        }
    }
}
