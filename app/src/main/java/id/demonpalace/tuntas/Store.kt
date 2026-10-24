package id.demonpalace.tuntas

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.LocalDate

/** All app state lives in one SharedPreferences file so the blocker service can read it too. */
class Store private constructor(private val prefs: SharedPreferences) {

    var done by mutableStateOf(prefs.getStringSet(K_DONE, emptySet())!!.toSet())
        private set
    /** Sub-chapters finished on the map or in the book, keyed by [PathNode.id]. */
    var read by mutableStateOf(prefs.getStringSet(K_READ, emptySet())!!.toSet())
        private set
    var wrong by mutableStateOf(prefs.getStringSet(K_WRONG, emptySet())!!.toSet())
        private set
    var xp by mutableIntStateOf(prefs.getInt(K_XP, 0))
        private set
    var streak by mutableIntStateOf(prefs.getInt(K_STREAK, 0))
        private set
    /** 0 = no session, 1 = belajar, 2 = latihan soal, 3 = tulis & foto. */
    var phase by mutableIntStateOf(prefs.getInt(K_PHASE, 0))
        private set
    var phaseEndAt by mutableLongStateOf(prefs.getLong(K_PHASE_END, 0L))
        private set
    var studyMin by mutableIntStateOf(prefs.getInt(K_STUDY_MIN, 300))
        private set
    var practiceMin by mutableIntStateOf(prefs.getInt(K_PRACTICE_MIN, 60))
        private set
    var photos by mutableStateOf(prefs.getStringSet(K_PHOTOS, emptySet())!!.sorted())
        private set
    var blocked by mutableStateOf(prefs.getStringSet(K_BLOCKED, emptySet())!!.toSet())
        private set
    var dailyLimitMin by mutableIntStateOf(prefs.getInt(K_LIMIT, 0))
        private set
    var alwaysBlock by mutableStateOf(prefs.getBoolean(K_ALWAYS, false))
        private set
    var badges by mutableStateOf(prefs.getStringSet(K_BADGES, emptySet())!!.toSet())
        private set
    /** Bumped whenever a day-keyed counter changes, so Compose re-reads them. */
    var tick by mutableIntStateOf(0)
        private set

    fun bestScore(stepId: String) = prefs.getInt("score_$stepId", -1)

    fun focusMinutes(date: LocalDate = LocalDate.now()) = prefs.getInt("focus_$date", 0)
    fun questionsAnswered(date: LocalDate = LocalDate.now()) = prefs.getInt("answered_$date", 0)
    fun blockedHits(date: LocalDate = LocalDate.now()) = prefs.getInt("blocked_$date", 0)

    val level get() = xp / 100 + 1
    val levelProgress get() = (xp % 100) / 100f

    /** A step is open once the map reaches its first stop. */
    fun isUnlocked(index: Int): Boolean {
        val first = Content.nodes.indexOfFirst { it.step.id == Content.steps[index].id }
        return first <= 0 || nodeUnlocked(first)
    }

    fun nodeDone(n: PathNode): Boolean = if (n.isQuiz) n.step.id in done else n.id in read

    fun nodeUnlocked(i: Int): Boolean = i == 0 || nodeDone(Content.nodes[i - 1])

    /** Index of the first node not finished yet, i.e. where the "MULAI" bubble sits. */
    val currentNode: Int get() = Content.nodes.indexOfFirst { !nodeDone(it) }

    fun stepRead(step: Step) = step.sections.indices.all { "${step.id}#$it" in read }

    /** Last sub-chapter opened in each book, "stepId#section". */
    fun bookmark(subject: Subject): String? = prefs.getString("bookmark_${subject.name}", null)

    fun saveBookmark(subject: Subject, nodeId: String) {
        prefs.edit().putString("bookmark_${subject.name}", nodeId).apply()
        tick++
    }

    /** Marks a sub-chapter as read. Gives XP the first time; a reading-only step finishes with its last page. */
    fun markRead(step: Step, section: Int): Int {
        val id = "${step.id}#$section"
        if (id in read) return 0
        read = read + id
        prefs.edit().putStringSet(K_READ, read).apply()
        addXp(10)
        touchStreak()
        if (step.questions.isEmpty() && stepRead(step)) finishStep(step, 0) else checkBadges()
        return 10
    }

    /** True while the strict part of a session (belajar or latihan soal) is running. */
    fun focusActive(now: Long = System.currentTimeMillis()): Boolean {
        advance(now)
        return phase == 1 || phase == 2
    }

    /** Records one answered question. Returns XP gained. */
    fun answer(q: Question, correct: Boolean, combo: Int): Int {
        val gain = if (correct) 10 + (combo - 1).coerceIn(0, 4) * 2 else 2
        wrong = if (correct) wrong - q.id else wrong + q.id
        bump("answered_${LocalDate.now()}", 1)
        addXp(gain)
        touchStreak()
        prefs.edit().putStringSet(K_WRONG, wrong).apply()
        return gain
    }

    fun finishStep(step: Step, correct: Int) {
        val best = bestScore(step.id)
        val e = prefs.edit()
        if (correct > best) e.putInt("score_${step.id}", correct)
        val firstTime = step.id !in done
        done = done + step.id
        e.putStringSet(K_DONE, done).apply()
        if (firstTime) addXp(30)
        touchStreak()
        checkBadges()
    }

    fun setDurations(study: Int, practice: Int) {
        studyMin = study; practiceMin = practice
        prefs.edit().putInt(K_STUDY_MIN, study).putInt(K_PRACTICE_MIN, practice).apply()
    }

    fun startSession(now: Long = System.currentTimeMillis()) {
        setPhase(1, now + studyMin * 60_000L)
    }

    /** Moves to the next phase when the current timer has run out. Safe to call often. */
    fun advance(now: Long = System.currentTimeMillis()) {
        if (phase == 1 && now >= phaseEndAt) {
            bump("focus_${LocalDate.now()}", studyMin)
            setPhase(2, phaseEndAt + practiceMin * 60_000L)
        }
        if (phase == 2 && now >= phaseEndAt) {
            bump("focus_${LocalDate.now()}", practiceMin)
            setPhase(3, 0L)
        }
    }

    /** Last step: the handwritten summary photo (or typed summary .txt) was saved. */
    fun completeSession(photoPath: String) {
        photos = (photos + photoPath).sorted()
        bump("sessions_${LocalDate.now()}", 1)
        prefs.edit().putStringSet(K_PHOTOS, photos.toSet()).apply()
        addXp(100)
        touchStreak()
        setPhase(0, 0L)
        checkBadges()
    }

    fun chestClaimed(day: Int) = prefs.getBoolean("chest_$day", false)

    /** Opens the reward chest at the end of a finished day. Returns the XP gained, 0 if not allowed. */
    fun claimChest(day: Int): Int {
        if (chestClaimed(day) || !dayDone(day)) return 0
        prefs.edit().putBoolean("chest_$day", true).apply()
        addXp(25)
        tick++
        return 25
    }

    fun dayDone(day: Int) = Content.nodes.filter { it.step.day == day }.all { nodeDone(it) }

    fun sessionsDone(date: LocalDate = LocalDate.now()) = prefs.getInt("sessions_$date", 0)

    /** Emergency exit: only minutes already spent count, no XP. */
    fun abortSession(now: Long = System.currentTimeMillis()) {
        val left = ((phaseEndAt - now) / 60_000L).toInt().coerceAtLeast(0)
        val spent = when (phase) {
            1 -> studyMin - left
            2 -> practiceMin - left
            else -> 0
        }.coerceAtLeast(0)
        bump("focus_${LocalDate.now()}", spent)
        setPhase(0, 0L)
    }

    private fun setPhase(p: Int, end: Long) {
        phase = p; phaseEndAt = end
        prefs.edit().putInt(K_PHASE, p).putLong(K_PHASE_END, end).apply()
    }

    fun toggleBlocked(pkg: String) {
        blocked = if (pkg in blocked) blocked - pkg else blocked + pkg
        prefs.edit().putStringSet(K_BLOCKED, blocked).apply()
    }

    fun updateDailyLimit(min: Int) {
        dailyLimitMin = min
        prefs.edit().putInt(K_LIMIT, min).apply()
    }

    fun updateAlwaysBlock(on: Boolean) {
        alwaysBlock = on
        prefs.edit().putBoolean(K_ALWAYS, on).apply()
    }

    fun recordBlockedHit() = bump("blocked_${LocalDate.now()}", 1)

    fun resetAll() {
        val keepBlocked = blocked
        val keepLimit = dailyLimitMin
        prefs.edit().clear().putStringSet(K_BLOCKED, keepBlocked).putInt(K_LIMIT, keepLimit)
            .putBoolean(K_ALWAYS, alwaysBlock).apply()
        studyMin = 300; practiceMin = 60
        done = emptySet(); read = emptySet(); wrong = emptySet(); xp = 0; streak = 0; badges = emptySet()
        phase = 0; phaseEndAt = 0L; photos = emptyList(); tick++
    }

    private fun addXp(n: Int) {
        xp += n
        prefs.edit().putInt(K_XP, xp).apply()
    }

    private fun bump(key: String, n: Int) {
        prefs.edit().putInt(key, prefs.getInt(key, 0) + n).apply()
        tick++
    }

    private fun touchStreak() {
        val today = LocalDate.now().toString()
        val last = prefs.getString(K_LAST_DAY, null)
        if (last == today) return
        streak = if (last == LocalDate.now().minusDays(1).toString()) streak + 1 else 1
        prefs.edit().putString(K_LAST_DAY, today).putInt(K_STREAK, streak).apply()
    }

    private fun checkBadges() {
        val earned = Badge.entries.filter { it.check(this) }.map { it.name }.toSet()
        if (earned != badges) {
            badges = badges + earned
            prefs.edit().putStringSet(K_BADGES, badges).apply()
        }
    }

    companion object {
        const val PREFS = "tuntas"
        private const val K_DONE = "done"
        private const val K_WRONG = "wrong"
        private const val K_READ = "read"
        private const val K_XP = "xp"
        private const val K_STREAK = "streak"
        private const val K_LAST_DAY = "last_day"
        const val K_PHASE = "phase"
        const val K_PHASE_END = "phase_end"
        private const val K_STUDY_MIN = "study_min"
        private const val K_PRACTICE_MIN = "practice_min"
        private const val K_PHOTOS = "photos"
        const val K_BLOCKED = "blocked"
        const val K_LIMIT = "daily_limit"
        private const val K_BADGES = "badges"
        private const val K_ALWAYS = "always_block"

        @Volatile private var instance: Store? = null
        fun get(ctx: Context): Store = instance ?: synchronized(this) {
            instance ?: Store(ctx.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE))
                .also { instance = it }
        }
    }
}

enum class Badge(val emoji: String, val title: String, val desc: String, val check: (Store) -> Boolean) {
    FIRST_STEP("🚀", "Langkah Pertama", "Selesaikan 1 langkah", { it.done.isNotEmpty() }),
    DAY_ONE("🌅", "Hari 1 Tuntas", "Selesaikan semua langkah hari 1", { s -> s.dayDone(1) }),
    MATH("📐", "Jago Hitung", "Selesaikan semua langkah Matematika", { s -> Content.steps.filter { it.subject == Subject.MAT }.all { it.id in s.done } }),
    PHYSICS("⚡", "Fisikawan", "Selesaikan semua langkah Fisika", { s -> Content.steps.filter { it.subject == Subject.FIS }.all { it.id in s.done } }),
    PERFECT("💯", "Sempurna", "Benar semua di satu kuis", { s -> Content.steps.any { st -> st.questions.isNotEmpty() && s.bestScore(st.id) == st.questions.size } }),
    FOCUS("🧘", "Anti HP", "Tuntaskan 1 Sesi Harian penuh", { it.photos.isNotEmpty() }),
    LEVEL5("👑", "Level 5", "Kumpulkan 400 XP", { it.xp >= 400 }),
    FINISH("🏆", "Khatam!", "Selesaikan seluruh langkah", { s -> Content.steps.all { it.id in s.done } }),
}
