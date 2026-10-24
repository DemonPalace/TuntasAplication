package id.demonpalace.tuntas

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.HistoryEdu
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.ui.graphics.vector.ImageVector

val Subject.icon: ImageVector
    get() = when (this) {
        Subject.MAT -> Icons.Rounded.Calculate
        Subject.FIS -> Icons.Rounded.Bolt
        Subject.BIN -> Icons.Rounded.HistoryEdu
        Subject.BIG -> Icons.Rounded.Translate
        Subject.MIX -> Icons.Rounded.EmojiEvents
    }

/** Glyph for a map stop, by what you do there. */
fun nodeIcon(n: PathNode, done: Boolean, unlocked: Boolean): ImageVector = when {
    done -> Icons.Rounded.Star
    !unlocked -> Icons.Rounded.Lock
    n.step.id.startsWith("try") && n.isQuiz -> Icons.Rounded.Timer
    n.step.isPractice -> Icons.Rounded.EditNote
    n.isQuiz -> Icons.Rounded.Quiz
    n.step.isReview -> Icons.Rounded.Lightbulb
    else -> Icons.Rounded.AutoStories
}
