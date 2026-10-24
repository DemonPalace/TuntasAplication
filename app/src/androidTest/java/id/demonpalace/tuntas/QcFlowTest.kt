package id.demonpalace.tuntas

import android.graphics.Bitmap
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Drives the main flows on a real emulator and saves screenshots for visual QC.
 * Screenshots land in the app's external files dir under qc/, pulled by CI.
 */
@RunWith(AndroidJUnit4::class)
class QcFlowTest {
    @get:Rule val rule = createAndroidComposeRule<MainActivity>()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext
    private val store get() = Store.get(ctx)

    @Before fun reset() {
        rule.runOnUiThread { store.resetAll() }
        rule.waitForIdle()
    }

    /** Emulator noise: launcher ANR dialogs and the one-time "App is pinned" tip cover the app. */
    private fun dismissSystemDialogs() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        listOf("Wait", "WAIT", "Got it", "GOT IT").forEach { label ->
            device.findObject(By.text(label))?.let { runCatching { it.click() } }
        }
    }

    private fun shot(name: String, settleMs: Long = 600) {
        dismissSystemDialogs()
        rule.mainClock.advanceTimeBy(settleMs)
        rule.waitForIdle()
        Thread.sleep(250)
        val bmp: Bitmap = InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot() ?: return
        // AGP pulls this folder back to the build machine after the run.
        val dir = (InstrumentationRegistry.getArguments().getString("additionalTestOutputDir")?.let(::File)
            ?: File(ctx.getExternalFilesDir(null), "qc")).apply { mkdirs() }
        File(dir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 90, it) }
    }

    private fun displayed(tag: String) = runCatching { rule.onNodeWithTag(tag).assertIsDisplayed() }.isSuccess

    private fun turnPage() { rule.onNodeWithTag("next_card").performClick(); rule.mainClock.advanceTimeBy(600); rule.waitForIdle() }

    /** Marks every stop before [node] as finished so the map opens up to it. */
    private fun unlockUpTo(node: Int) = rule.runOnUiThread {
        Content.nodes.take(node).forEach { n ->
            if (n.isQuiz) store.finishStep(n.step, n.step.questions.size) else store.markRead(n.step, n.section)
        }
    }

    /** Answers one question of any type; [right] = false picks a wrong answer. */
    private fun answer(q: Question, right: Boolean) {
        when {
            q.isTable -> {
                q.rows.indices.forEach { r ->
                    val c = if (right || r > 0) q.rowKeys[r] else (q.rowKeys[r] + 1) % q.options.size
                    rule.onNodeWithTag("cell_${r}_$c").performScrollTo().performClick()
                }
                rule.onNodeWithTag("quiz_check").performScrollTo().performClick()
            }
            q.isMulti -> {
                val keys = q.multi!!.sorted()
                (if (right) keys else keys.drop(1)).forEach { rule.onNodeWithTag("chk_$it").performScrollTo().performClick() }
                rule.onNodeWithTag("quiz_check").performScrollTo().performClick()
            }
            else -> {
                val pick = if (right) q.answer else (q.answer + 1) % q.options.size
                rule.onNodeWithTag("opt_$pick").performScrollTo().performClick()
            }
        }
    }

    @Test fun a_readOnTheMap() {
        shot("01_map_start", 1200)
        rule.onNodeWithTag("node_0").performClick()
        shot("02_map_popover")
        rule.onNodeWithTag("start_step").performClick()
        shot("03_book_opener", 900)
        var revealed = false
        var ticked = false
        repeat(20) { page ->
            if (displayed("finish_read")) return@repeat
            if (page == 1) shot("04_book_body")
            if (!revealed && displayed("reveal_step")) {
                shot("05_book_example")
                rule.onNodeWithTag("reveal_step").performClick()
                rule.onNodeWithTag("reveal_step").performClick()
                shot("06_book_example_steps")
                revealed = true
            }
            if (!ticked && displayed("point_0:0")) {
                rule.onNodeWithTag("point_0:0").performClick()
                shot("07_book_summary")
                ticked = true
            }
            turnPage()
        }
        shot("08_book_end")
        rule.onNodeWithTag("finish_read").performClick()
        shot("09_map_after_read", 1500)
        assertTrue("${Content.steps[0].id}#0" in store.read)
        assertEquals(10, store.xp)
        assertTrue(store.nodeUnlocked(1))
        assertTrue(!store.nodeUnlocked(2))
    }

    @Test fun b_lockedNodeShakes() {
        rule.onNodeWithTag("node_3").performClick()
        shot("10_locked_shake", 100)
    }

    @Test fun c_chapterQuizFromMap() {
        val qi = Content.nodes.indexOfFirst { it.isQuiz }
        unlockUpTo(qi)
        val step = Content.nodes[qi].step
        rule.onNodeWithTag("node_$qi").performScrollTo()
        shot("11_map_quiz_node", 900)
        rule.onNodeWithTag("node_$qi").performClick()
        rule.onNodeWithTag("start_step").performClick()
        shot("12_quiz_q1")
        step.questions.forEachIndexed { i, q ->
            // Answer the second question wrong to exercise the shake + correction path.
            answer(q, right = i != 1)
            if (i == 0) shot("13_quiz_correct", 500)
            if (i == 1) shot("14_quiz_wrong", 600)
            rule.onNodeWithTag("quiz_next").performClick()
        }
        shot("15_result", 1200)
        assertEquals(step.questions.size - 1, store.bestScore(step.id))
        assertTrue(step.id in store.done)
        assertEquals(1, store.wrong.size)
        rule.onNodeWithTag("result_continue").performClick()
        shot("16_map_after_quiz", 1500)
        assertTrue(store.nodeUnlocked(qi + 1))
    }

    @Test fun d_complexQuestionsWithFigures() {
        val si = Content.steps.indexOfFirst { it.id == "rbig1" }
        val step = Content.steps[si]
        val ni = Content.nodes.indexOfFirst { it.isQuiz && it.step.id == "rbig1" }
        unlockUpTo(ni)
        rule.onNodeWithTag("node_$ni").performScrollTo().performClick()
        rule.onNodeWithTag("start_step").performClick()
        var multiShot = false
        var tableShot = false
        step.questions.forEachIndexed { i, q ->
            rule.mainClock.advanceTimeBy(500)
            when {
                q.isMulti && !multiShot -> {
                    val keys = q.multi!!.sorted()
                    rule.onNodeWithTag("chk_${keys[0]}").performScrollTo().performClick()
                    shot("17_multi_ticking")
                    keys.drop(1).forEach { rule.onNodeWithTag("chk_$it").performScrollTo().performClick() }
                    rule.onNodeWithTag("quiz_check").performScrollTo().performClick()
                    shot("18_multi_checked", 600)
                    multiShot = true
                }
                q.isTable && !tableShot -> {
                    rule.onNodeWithTag("q_image").performScrollTo()
                    shot("19_figure")
                    rule.onNodeWithTag("q_image").performClick()
                    shot("20_figure_zoom")
                    UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).pressBack()
                    rule.onNodeWithTag("cell_0_${q.rowKeys[0]}").performScrollTo().performClick()
                    shot("21_table_picking")
                    answer(q, right = false)
                    shot("22_table_checked", 600)
                    tableShot = true
                }
                else -> answer(q, right = true)
            }
            rule.onNodeWithTag("quiz_next").performClick()
        }
        shot("23_complex_result", 1200)
        assertTrue(multiShot && tableShot)
        assertTrue(step.id in store.done)
    }

    @Test fun e_libraryAndBook() {
        rule.runOnUiThread { Content.steps[0].sections.indices.take(3).forEach { store.markRead(Content.steps[0], it) } }
        rule.onNodeWithTag("tab_1").performClick()
        shot("24_library", 1200)
        rule.onNodeWithTag("book_FIS").performClick()
        shot("25_book_toc", 900)
        val fis = Content.books.getValue(Subject.FIS).first()
        val idx = Content.steps.indexOf(fis)
        rule.onNodeWithTag("sub_${idx}_1").performScrollTo().performClick()
        shot("26_chapter_reader", 900)
        repeat(2) { turnPage() }
        shot("27_chapter_page")
        rule.onNodeWithTag("back").performClick()
        shot("28_book_toc_back", 900)
        assertTrue(store.bookmark(Subject.FIS)?.startsWith(fis.id) == true)
    }

    @Test fun f_progressScreen() {
        rule.runOnUiThread {
            Content.steps.take(3).forEach { s -> s.questions.forEach { q -> store.answer(q, q.id.hashCode() % 3 != 0, 1) }; store.finishStep(s, s.questions.size - 1) }
        }
        rule.onNodeWithTag("tab_2").performClick()
        shot("29_progress_top", 1500)
        rule.onNodeWithTag("ring_FIS").performClick()
        shot("30_progress_ring_open", 800)
        rule.onNodeWithTag("retry_wrong").performScrollTo()
        rule.onNodeWithTag("retry_wrong").performClick()
        shot("31_retry_quiz")
    }

    @Test fun g_sessionFlow() {
        rule.onNodeWithTag("tab_3").performClick()
        shot("32_session_setup", 800)
        rule.onNodeWithTag("preset_2").performClick()
        rule.onNodeWithTag("start_session").performScrollTo().performClick()
        shot("33_session_running", 1500)
        assertEquals(1, store.phase)
        // Jump the clock past the study phase and check it rolls into practice.
        rule.runOnUiThread { store.advance(store.phaseEndAt + 1) }
        shot("34_session_practice", 1200)
        assertEquals(2, store.phase)
        rule.onNodeWithTag("emergency").performScrollTo().performTouchInput { down(center); advanceEventTime(5600); up() }
        rule.mainClock.advanceTimeBy(6000)
        rule.waitForIdle()
        assertEquals(0, store.phase)
    }

    @Test fun h_summaryTyping() {
        rule.runOnUiThread { store.setDurations(25, 10); store.startSession(); store.advance(store.phaseEndAt + 10 * 60_000L + 2) }
        rule.onNodeWithTag("tab_3").performClick()
        assertEquals(3, store.phase)
        rule.onNodeWithTag("type_notes").performClick()
        rule.onNodeWithTag("notes_field").performTextInput(
            "GLBB: s = v0 t + 1/2 a t kuadrat. Tadi salah soal impuls karena lupa arah bola berbalik sehingga kecepatan awal harus negatif. Fluida: P = rho g h, Archimedes Fa = rho g V.",
        )
        shot("35_summary_typed")
        rule.onNodeWithTag("save_notes").performScrollTo().performClick()
        assertEquals(0, store.phase)
        assertEquals(1, store.photos.size)
    }

    @Test fun i_blockScreen() {
        rule.onNodeWithTag("tab_4").performClick()
        shot("36_block_top", 2000)
        rule.onNodeWithTag("limit_30").performScrollTo().performClick()
        assertEquals(30, store.dailyLimitMin)
    }

    @Test fun j_pageTurnAnimation() {
        rule.onNodeWithTag("node_0").performClick()
        rule.waitUntil(5000) { rule.onAllNodesWithTag("start_step").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithTag("start_step").performClick()
        rule.waitUntil(5000) { rule.onAllNodesWithTag("book_pager").fetchSemanticsNodes().isNotEmpty() }
        rule.mainClock.autoAdvance = false
        rule.onNodeWithTag("book_pager").performTouchInput { swipeLeft(startX = right - 20f, endX = centerX, durationMillis = 400) }
        rule.mainClock.advanceTimeBy(60)
        shot("37_page_turn_midway", 0)
        rule.mainClock.autoAdvance = true
        shot("38_page_turn_done", 800)
    }
}
