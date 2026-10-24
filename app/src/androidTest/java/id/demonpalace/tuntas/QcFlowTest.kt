package id.demonpalace.tuntas

import android.graphics.Bitmap
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

    @Test fun a_lessonAndQuizFlow() {
        shot("01_map_start", 1200)
        rule.onNodeWithTag("node_0").performClick()
        shot("02_map_popover")
        rule.onNodeWithTag("start_step").performClick()
        shot("03_lesson_card1")
        rule.onNodeWithTag("point_0:0").performClick()
        rule.onNodeWithTag("point_0:1").performClick()
        shot("04_lesson_ticked")
        val step = Content.steps[0]
        repeat(step.sections.size) { rule.onNodeWithTag("next_card").performClick(); rule.mainClock.advanceTimeBy(500) }
        shot("05_lesson_final")
        rule.onNodeWithTag("start_quiz").performClick()
        shot("06_quiz_q1")
        step.questions.forEachIndexed { i, q ->
            // Answer the second question wrong to exercise the shake + correction path.
            val pick = if (i == 1) (q.answer + 1) % q.options.size else q.answer
            rule.onNodeWithTag("opt_$pick").performScrollTo().performClick()
            if (i == 0) { shot("07_quiz_correct_frame1", 80); shot("07_quiz_correct_frame2", 500) }
            if (i == 1) { shot("08_quiz_wrong_frame1", 120); shot("08_quiz_wrong_frame2", 600) }
            if (i == 2) shot("09_quiz_combo")
            rule.onNodeWithTag("quiz_next").performClick()
        }
        shot("10_result_frame1", 200)
        shot("10_result_frame2", 1200)
        assertEquals(step.questions.size - 1, store.bestScore(step.id))
        assertTrue(step.id in store.done)
        assertTrue(store.xp > 0)
        assertEquals(1, store.wrong.size)
        rule.onNodeWithTag("result_continue").performClick()
        shot("11_map_after_step1", 1500)
        assertTrue(store.isUnlocked(1))
    }

    @Test fun b_lockedNodeShakes() {
        rule.onNodeWithTag("node_3").performClick()
        shot("12_locked_shake", 100)
    }

    @Test fun c_progressScreen() {
        rule.runOnUiThread {
            Content.steps.take(3).forEach { s -> s.questions.forEach { q -> store.answer(q, q.id.hashCode() % 3 != 0, 1) }; store.finishStep(s, s.questions.size - 1) }
        }
        rule.onNodeWithTag("tab_1").performClick()
        shot("13_progress_top", 1500)
        rule.onNodeWithTag("ring_FIS").performClick()
        shot("14_progress_ring_open", 800)
        rule.onNodeWithTag("retry_wrong").performScrollTo()
        shot("15_progress_wrong")
        rule.onNodeWithTag("retry_wrong").performClick()
        shot("16_retry_quiz")
    }

    @Test fun d_sessionFlow() {
        rule.onNodeWithTag("tab_2").performClick()
        shot("17_session_setup", 800)
        rule.onNodeWithTag("preset_2").performClick()
        rule.onNodeWithTag("start_session").performScrollTo().performClick()
        shot("18_session_running", 1500)
        assertEquals(1, store.phase)
        // Jump the clock past the study phase and check it rolls into practice.
        rule.runOnUiThread { store.advance(store.phaseEndAt + 1) }
        shot("19_session_practice", 1200)
        assertEquals(2, store.phase)
        rule.onNodeWithTag("emergency").performScrollTo().performTouchInput { down(center); advanceEventTime(5600); up() }
        rule.mainClock.advanceTimeBy(6000)
        rule.waitForIdle()
        shot("20_after_emergency", 800)
        assertEquals(0, store.phase)
    }

    @Test fun e_summaryTyping() {
        rule.runOnUiThread { store.setDurations(25, 10); store.startSession(); store.advance(store.phaseEndAt + 10 * 60_000L + 2) }
        rule.onNodeWithTag("tab_2").performClick()
        shot("21_summary_choose", 1000)
        assertEquals(3, store.phase)
        rule.onNodeWithTag("type_notes").performClick()
        rule.onNodeWithTag("notes_field").performTextInput(
            "GLBB: s = v0 t + 1/2 a t kuadrat. Tadi salah soal impuls karena lupa arah bola berbalik sehingga kecepatan awal harus negatif. Fluida: P = rho g h, Archimedes Fa = rho g V.",
        )
        shot("22_summary_typed")
        rule.onNodeWithTag("save_notes").performScrollTo().performClick()
        shot("23_summary_saved", 1200)
        assertEquals(0, store.phase)
        assertEquals(1, store.photos.size)
    }

    @Test fun f_blockScreen() {
        rule.onNodeWithTag("tab_3").performClick()
        shot("24_block_top", 2000)
        rule.onNodeWithTag("limit_30").performScrollTo().performClick()
        assertEquals(30, store.dailyLimitMin)
        shot("25_block_limit")
    }

    @Test fun g_lessonSwipeAnimation() {
        rule.onNodeWithTag("node_0").performClick()
        rule.waitUntil(5000) { rule.onAllNodesWithTag("start_step").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithTag("start_step").performClick()
        rule.waitUntil(5000) { rule.onAllNodesWithTag("point_0:0").fetchSemanticsNodes().isNotEmpty() }
        rule.mainClock.autoAdvance = false
        rule.onNodeWithTag("point_0:0").performTouchInput { swipeLeft() }
        rule.mainClock.advanceTimeBy(120)
        shot("26_swipe_midway", 0)
        rule.mainClock.autoAdvance = true
        shot("27_swipe_done", 800)
    }
}
