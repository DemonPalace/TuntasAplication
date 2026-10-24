package id.demonpalace.tuntas

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
    data class Lesson(val index: Int) : Route
    data class Quiz(val index: Int) : Route
    data object Retry : Route
}

class MainActivity : ComponentActivity() {
    private var blockedPkg by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(android.graphics.Color.WHITE, android.graphics.Color.WHITE),
            navigationBarStyle = SystemBarStyle.light(android.graphics.Color.WHITE, android.graphics.Color.WHITE),
        )
        blockedPkg = intent?.getStringExtra(EXTRA_BLOCKED)
        setContent {
            JalurTheme {
                App(Store.get(this), blockedPkg) { blockedPkg = null }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.getStringExtra(EXTRA_BLOCKED)?.let { blockedPkg = it }
    }

    companion object {
        const val EXTRA_BLOCKED = "blocked_pkg"
    }
}

private data class TabItem(val label: String, val icon: ImageVector)

private val tabs = listOf(
    TabItem("Jalur", Icons.Rounded.Map),
    TabItem("Progres", Icons.Rounded.BarChart),
    TabItem("Fokus", Icons.Rounded.Timer),
    TabItem("Blokir", Icons.Rounded.Shield),
)

@Composable
fun App(store: Store, blockedPkg: String?, onDismissBlocked: () -> Unit) {
    var route by remember { mutableStateOf<Route>(Route.Home(0)) }
    var lastTab by remember { mutableStateOf(0) }

    // A blocked app sends you straight to the focus timer.
    LaunchedEffect(blockedPkg) { if (blockedPkg != null) route = Route.Home(2).also { lastTab = 2 } }

    BackHandler(enabled = route != Route.Home(0)) {
        route = when (val r = route) {
            is Route.Quiz -> Route.Lesson(r.index)
            is Route.Lesson, Route.Retry -> Route.Home(lastTab)
            is Route.Home -> Route.Home(0).also { lastTab = 0 }
        }
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
                            0 -> PathScreen(store) { route = Route.Lesson(it) }
                            1 -> ProgressScreen(store, onRetry = { route = Route.Retry }, onOpenStep = { route = Route.Lesson(it) })
                            2 -> FocusScreen(store, blockedPkg, onDismissBlocked)
                            else -> BlockScreen(store)
                        }
                    }
                    BottomBar(r.tab) { lastTab = it; route = Route.Home(it) }
                }
                is Route.Lesson -> LessonScreen(
                    store, Content.steps[r.index],
                    onBack = { route = Route.Home(lastTab) },
                    onStartQuiz = { route = Route.Quiz(r.index) },
                    onFinishReview = { route = Route.Home(1).also { lastTab = 1 } },
                )
                is Route.Quiz -> {
                    val step = Content.steps[r.index]
                    QuizScreen(
                        store, step.title, step.subject.color, step.questions,
                        onExit = { route = Route.Lesson(r.index) },
                        onDone = { correct -> store.finishStep(step, correct) },
                        onContinue = {
                            val next = r.index + 1
                            route = if (next < Content.steps.size) Route.Home(0) else Route.Home(1)
                            lastTab = if (next < Content.steps.size) 0 else 1
                        },
                    )
                }
                Route.Retry -> {
                    val qs = remember { store.wrong.mapNotNull { Content.allQuestions[it]?.second }.shuffled() }
                    QuizScreen(
                        store, "Ulang soal yang salah", C.pink, qs,
                        onExit = { route = Route.Home(1) },
                        onDone = {},
                        onContinue = { route = Route.Home(1) },
                    )
                }
            }
        }
    }
}

private fun depth(r: Route) = when (r) {
    is Route.Home -> 0
    is Route.Lesson, Route.Retry -> 1
    is Route.Quiz -> 2
}

@Composable
private fun BottomBar(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(C.bg).drawBehind { drawLine(C.line, Offset(0f, 0f), Offset(size.width, 0f), 4f) }
            .navigationBarsPadding().padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        tabs.forEachIndexed { i, t ->
            val on = i == selected
            val scale by animateFloatAsState(if (on) 1.12f else 1f, spring(Spring.DampingRatioMediumBouncy), label = "tab")
            Column(
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(remember { MutableInteractionSource() }, null) { onSelect(i) }
                    .testTag("tab_$i")
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    Modifier.scale(scale).clip(RoundedCornerShape(50))
                        .background(if (on) C.blue.copy(alpha = 0.12f) else C.bg)
                        .border(2.dp, if (on) C.blue.copy(alpha = 0.6f) else C.bg, RoundedCornerShape(50))
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                ) {
                    Icon(t.icon, t.label, tint = if (on) C.blue else C.faded, modifier = Modifier.size(24.dp))
                }
                Spacer(Modifier.height(2.dp))
                Text(t.label.uppercase(), color = if (on) C.blue else C.faded, style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (on) FontWeight.Bold else FontWeight.Medium)
            }
        }
    }
}
