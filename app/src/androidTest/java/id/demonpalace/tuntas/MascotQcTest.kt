package id.demonpalace.tuntas

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Steps every state of Tuti frame by frame, saves the frames, and checks that no frame touches
 * the edge of its box (which would mean the drawing is clipped).
 */
@RunWith(AndroidJUnit4::class)
class MascotQcTest {
    @get:Rule val rule = createComposeRule()

    private val outDir: File
        get() {
            val ctx = InstrumentationRegistry.getInstrumentation().targetContext
            return (InstrumentationRegistry.getArguments().getString("additionalTestOutputDir")?.let(::File)
                ?: File(ctx.getExternalFilesDir(null), "qc")).apply { mkdirs() }
        }

    private fun save(bmp: Bitmap, name: String) =
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 95, it) }

    /** True when every pixel on the outer 1px ring equals the corner background pixel. */
    private fun edgesClear(b: Bitmap): Boolean {
        val bg = b.getPixel(0, 0)
        for (x in 0 until b.width) if (b.getPixel(x, 0) != bg || b.getPixel(x, b.height - 1) != bg) return false
        for (y in 0 until b.height) if (b.getPixel(0, y) != bg || b.getPixel(b.width - 1, y) != bg) return false
        return true
    }

    private fun runMood(mood: MascotMood, dark: Boolean) {
        var current by mutableStateOf(MascotMood.IDLE)
        rule.mainClock.autoAdvance = false
        rule.setContent {
            JalurTheme(dark = dark) {
                Box(Modifier.background(C.bg).padding(1.dp).testTag("frame")) { Mascot(current, size = 120.dp) }
            }
        }
        rule.mainClock.advanceTimeBy(100)
        current = mood
        val theme = if (dark) "dark" else "light"
        for (t in 0..24) {
            rule.mainClock.advanceTimeBy(if (t == 0) 16 else 60)
            val bmp = rule.onNodeWithTag("frame").captureToImage().asAndroidBitmap()
            save(bmp, "40_mascot_${theme}_${mood.name.lowercase()}_${t.toString().padStart(2, '0')}")
            assertTrue("${mood.name} $theme frame $t is clipped", edgesClear(bmp))
        }
        if (mood == MascotMood.IDLE) {
            // The blink falls at 96-100% of the 3.6 s idle clock.
            rule.mainClock.advanceTimeBy(3520 - 100 - 16 - 24 * 60)
            val bmp = rule.onNodeWithTag("frame").captureToImage().asAndroidBitmap()
            save(bmp, "40_mascot_${theme}_blink")
            assertTrue("blink $theme is clipped", edgesClear(bmp))
        }
    }

    @Test fun idleLight() = runMood(MascotMood.IDLE, false)
    @Test fun correctLight() = runMood(MascotMood.CORRECT, false)
    @Test fun wrongLight() = runMood(MascotMood.WRONG, false)
    @Test fun doneLight() = runMood(MascotMood.DONE, false)
    @Test fun idleDark() = runMood(MascotMood.IDLE, true)
    @Test fun correctDark() = runMood(MascotMood.CORRECT, true)
    @Test fun wrongDark() = runMood(MascotMood.WRONG, true)
    @Test fun doneDark() = runMood(MascotMood.DONE, true)

    /** One full 15 s idle cycle: every action (wave, write, stretch, foot tap, lean, pencil spin) stays inside the box. */
    @Test fun idleCycle() {
        rule.mainClock.autoAdvance = false
        rule.setContent {
            JalurTheme(dark = false) {
                Box(Modifier.background(C.bg).padding(1.dp).testTag("frame")) { Mascot(MascotMood.IDLE, size = 120.dp) }
            }
        }
        for (t in 0 until 100) {
            rule.mainClock.advanceTimeBy(150)
            val bmp = rule.onNodeWithTag("frame").captureToImage().asAndroidBitmap()
            save(bmp, "43_tuti_idle_${t.toString().padStart(3, '0')}")
            assertTrue("idle frame $t is clipped", edgesClear(bmp))
        }
    }

    /** Three taps play three different reactions. */
    @Test fun tapReactions() {
        rule.mainClock.autoAdvance = false
        rule.setContent {
            JalurTheme(dark = false) {
                Box(Modifier.background(C.bg).padding(1.dp).testTag("frame")) { Mascot(MascotMood.IDLE, size = 120.dp) }
            }
        }
        rule.mainClock.advanceTimeBy(100)
        var n = 0
        repeat(3) {
            rule.onNodeWithTag("mascot").performClick()
            for (t in 0 until 20) {
                rule.mainClock.advanceTimeBy(60)
                val bmp = rule.onNodeWithTag("frame").captureToImage().asAndroidBitmap()
                save(bmp, "44_tuti_tap_${(n++).toString().padStart(3, '0')}")
                assertTrue("tap frame $n is clipped", edgesClear(bmp))
            }
        }
    }
}
