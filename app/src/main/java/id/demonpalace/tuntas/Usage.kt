package id.demonpalace.tuntas

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Process
import android.provider.Settings
import java.time.LocalDate
import java.time.ZoneId

data class AppInfo(val pkg: String, val label: String)

/** Apps people usually lose time on; pre-ticked suggestions in the picker. */
val distractingHints = listOf(
    "tiktok", "musically", "instagram", "facebook", "youtube", "twitter", "com.twitter", "x.android",
    "snapchat", "netflix", "mobile.legends", "moonton", "pubg", "tencent.ig", "freefire", "dts.freefire",
    "roblox", "genshin", "miHoYo", "hoyoverse", "whatsapp", "telegram", "discord", "reddit", "shopee", "likee",
    "capcut", "spotify", "line", "threads",
)

fun isDistracting(pkg: String) = distractingHints.any { pkg.contains(it, ignoreCase = true) }

fun isBlockerEnabled(ctx: Context): Boolean {
    val enabled = Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) ?: return false
    val me = ComponentName(ctx, BlockerService::class.java)
    return enabled.split(':').any { ComponentName.unflattenFromString(it) == me }
}

fun hasUsageAccess(ctx: Context): Boolean {
    val ops = ctx.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    @Suppress("DEPRECATION")
    val mode = ops.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), ctx.packageName)
    return mode == AppOpsManager.MODE_ALLOWED
}

fun appLabel(ctx: Context, pkg: String): String = runCatching {
    val pm = ctx.packageManager
    pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0)).toString()
}.getOrDefault(pkg)

fun launchableApps(ctx: Context): List<AppInfo> {
    val pm = ctx.packageManager
    val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
    @Suppress("DEPRECATION")
    return pm.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        .map { it.activityInfo.packageName }
        .distinct()
        .filter { it != ctx.packageName }
        .map { AppInfo(it, appLabel(ctx, it)) }
        .sortedWith(compareByDescending<AppInfo> { isDistracting(it.pkg) }.thenBy { it.label.lowercase() })
}

/** Foreground minutes per package since local midnight. Empty without usage access. */
fun todayUsageMinutes(ctx: Context): Map<String, Int> {
    if (!hasUsageAccess(ctx)) return emptyMap()
    val usm = ctx.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val start = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    return runCatching {
        usm.queryAndAggregateUsageStats(start, System.currentTimeMillis())
            .mapValues { (it.value.totalTimeInForeground / 60_000L).toInt() }
            .filterValues { it > 0 }
    }.getOrDefault(emptyMap())
}
