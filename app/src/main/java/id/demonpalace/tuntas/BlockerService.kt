package id.demonpalace.tuntas

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.telecom.TelecomManager
import android.view.accessibility.AccessibilityEvent
import android.view.inputmethod.InputMethodManager

/**
 * Watches which app is in front. During the strict part of a session every app except
 * this one, the phone/dialer and the keyboard is sent back to Jalur TKA. Outside a session,
 * apps on the block list are closed when "always block" is on or their daily limit is used up.
 */
class BlockerService : AccessibilityService() {
    private val handler = Handler(Looper.getMainLooper())
    private var lastPkg: String? = null
    private var lastKick = 0L
    private var usageCache = emptyMap<String, Int>()
    private var usageAt = 0L

    private val recheck = object : Runnable {
        override fun run() {
            lastPkg?.let { decide(it) }
            handler.postDelayed(this, 15_000)
        }
    }

    override fun onServiceConnected() {
        handler.postDelayed(recheck, 15_000)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val pkg = event.packageName?.toString() ?: return
        lastPkg = pkg
        decide(pkg)
    }

    private fun decide(pkg: String) {
        if (pkg == packageName) return
        val store = Store.get(this)
        if (store.focusActive()) {
            if (!isAllowedDuringSession(pkg)) kick(pkg, countHit = pkg != launcherPackage())
            return
        }
        if (pkg in store.blocked && (store.alwaysBlock || overLimit(store, pkg))) kick(pkg, countHit = true)
    }

    private fun overLimit(store: Store, pkg: String): Boolean {
        if (store.dailyLimitMin <= 0) return false
        val now = System.currentTimeMillis()
        if (now - usageAt > 30_000) { usageCache = todayUsageMinutes(this); usageAt = now }
        val used = store.blocked.sumOf { usageCache[it] ?: 0 }
        return used >= store.dailyLimitMin
    }

    private fun kick(pkg: String, countHit: Boolean) {
        val now = System.currentTimeMillis()
        if (now - lastKick < 1200) return
        lastKick = now
        if (countHit) Store.get(this).recordBlockedHit()
        val intent = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            .putExtra(MainActivity.EXTRA_BLOCKED, pkg)
        runCatching { startActivity(intent) }.onFailure { performGlobalAction(GLOBAL_ACTION_HOME) }
    }

    private fun isAllowedDuringSession(pkg: String): Boolean {
        if (pkg in ALWAYS_ALLOWED) return true
        if (pkg.contains("incallui", true) || pkg.contains("telecom", true) || pkg.contains("dialer", true)) return true
        val telecom = getSystemService(Context.TELECOM_SERVICE) as? TelecomManager
        if (pkg == telecom?.defaultDialerPackage) return true
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        return imm?.enabledInputMethodList?.any { it.packageName == pkg } == true
    }

    private fun launcherPackage(): String? {
        val home = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
        return packageManager.resolveActivity(home, 0)?.activityInfo?.packageName
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        handler.removeCallbacks(recheck)
        super.onDestroy()
    }

    companion object {
        private val ALWAYS_ALLOWED = setOf(
            "android", "com.android.systemui", "com.android.phone", "com.android.emergency",
            "com.google.android.apps.safetyhub", "com.android.server.telecom",
        )
    }
}
