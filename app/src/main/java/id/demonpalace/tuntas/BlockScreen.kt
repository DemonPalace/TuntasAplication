package id.demonpalace.tuntas

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val limits = listOf(0, 15, 30, 60, 90, 120)

@Composable
fun BlockScreen(store: Store) {
    val ctx = LocalContext.current
    var blockerOn by remember { mutableStateOf(isBlockerEnabled(ctx)) }
    var usageOn by remember { mutableStateOf(hasUsageAccess(ctx)) }
    var usage by remember { mutableStateOf(emptyMap<String, Int>()) }
    var apps by remember { mutableStateOf<List<AppInfo>?>(null) }
    var query by remember { mutableStateOf("") }

    LifecycleResumeEffect(Unit) {
        blockerOn = isBlockerEnabled(ctx)
        usageOn = hasUsageAccess(ctx)
        usage = todayUsageMinutes(ctx)
        onPauseOrDispose { }
    }
    LaunchedEffect(Unit) { apps = withContext(Dispatchers.Default) { launchableApps(ctx) } }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text("Blokir Distraksi", color = C.text, style = MaterialTheme.typography.headlineLarge)
        Text("Biar gak ketagihan main HP: pilih aplikasi yang bikin lupa waktu. Saat Sesi Harian berjalan, SEMUA aplikasi lain ditutup otomatis.",
            color = C.muted, style = MaterialTheme.typography.bodyLarge)

        PermissionCard(
            "Pemblokir (Aksesibilitas)", blockerOn,
            "Wajib. Buka Setelan → Aksesibilitas → Aplikasi terinstal → \"Jalur TKA: Pemblokir Distraksi\" → Aktifkan.",
            "blocker_perm",
        ) { ctx.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
        PermissionCard(
            "Akses penggunaan", usageOn,
            "Untuk batas harian dan statistik waktu layar. Pilih Jalur TKA → Izinkan.",
            "usage_perm",
        ) { ctx.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }

        ScreenTimeCard(store, usage, usageOn)

        SectionTitle("Aturan di luar sesi")
        Card(Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Blokir terus", color = C.text, style = MaterialTheme.typography.titleMedium)
                        Text("Aplikasi pilihan selalu ditutup, bahkan di luar sesi. Cocok sampai TKA selesai 5 Nov.", color = C.muted, style = MaterialTheme.typography.bodyMedium)
                    }
                    Switch(store.alwaysBlock, { store.updateAlwaysBlock(it) }, Modifier.testTag("always_block"),
                        colors = SwitchDefaults.colors(checkedTrackColor = C.bad))
                }
                Text("Batas harian total aplikasi pilihan", color = C.text, style = MaterialTheme.typography.titleMedium)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    limits.forEach { m ->
                        val on = store.dailyLimitMin == m
                        val bg by animateColorAsState(if (on) C.violet else C.surface2, label = "lim")
                        Text(
                            if (m == 0) "Mati" else "${m}m",
                            Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(bg)
                                .clickable { store.updateDailyLimit(m) }.testTag("limit_$m").padding(vertical = 10.dp),
                            color = if (on) Color.White else C.muted, fontWeight = FontWeight.Bold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        )
                    }
                }
            }
        }

        SectionTitle("Aplikasi yang diblokir (${store.blocked.size})")
        val list = apps
        if (list == null) {
            Text("Memuat daftar aplikasi…", color = C.muted)
        } else {
            val suggested = list.filter { isDistracting(it.pkg) && it.pkg !in store.blocked }
            if (suggested.isNotEmpty()) {
                BouncyButton("Blokir ${suggested.size} aplikasi yang disarankan", Modifier.fillMaxWidth().testTag("block_suggested")) {
                    suggested.forEach { store.toggleBlocked(it.pkg) }
                }
            }
            OutlinedTextField(
                query, { query = it }, Modifier.fillMaxWidth(),
                placeholder = { Text("Cari aplikasi…", color = C.muted) }, singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = C.violet, unfocusedBorderColor = C.line, focusedTextColor = C.text, unfocusedTextColor = C.text),
            )
            list.filter { query.isBlank() || it.label.contains(query, true) }.forEach { app ->
                AppRow(app, app.pkg in store.blocked, usage[app.pkg] ?: 0) { store.toggleBlocked(app.pkg) }
            }
        }

        SectionTitle("Tambahan dari HP-mu")
        Card(Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Android punya Kesehatan Digital (Digital Wellbeing):", color = C.text, style = MaterialTheme.typography.titleMedium)
                Text("• Setelan → Kesehatan Digital → Mode Fokus: pilih aplikasi yang dijeda.", color = C.muted)
                Text("• Timer aplikasi: batasi TikTok/IG per hari.", color = C.muted)
                Text("• Mode Waktu Tidur: layar abu-abu dan notifikasi senyap mulai 21.00.", color = C.muted)
                Text("• Matikan notifikasi aplikasi medsos, dan taruh HP di ruangan lain saat tidur.", color = C.muted)
            }
        }
    }
}

@Composable
private fun PermissionCard(title: String, on: Boolean, how: String, tag: String, onFix: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
            .background(if (on) C.good.copy(alpha = 0.12f) else C.gold.copy(alpha = 0.12f))
            .border(2.dp, if (on) C.good.copy(alpha = 0.5f) else C.gold.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
            .clickable(enabled = !on, onClick = onFix).testTag(tag).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(if (on) "✅" else "⚠️", fontSize = 26.sp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title + if (on) " aktif" else " belum aktif", color = C.text, style = MaterialTheme.typography.titleMedium)
            if (!on) Text("$how Ketuk di sini.", color = C.muted, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ScreenTimeCard(store: Store, usage: Map<String, Int>, usageOn: Boolean) {
    val used = store.blocked.sumOf { usage[it] ?: 0 }
    val limit = store.dailyLimitMin
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(C.surface).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        val frac = if (limit > 0) used / limit.toFloat() else 0f
        val col = when { limit == 0 -> C.muted; frac >= 1f -> C.bad; frac >= 0.7f -> C.gold; else -> C.good }
        ProgressRing(frac, col, size = 84.dp, stroke = 9.dp) {
            Text(if (usageOn) "${used}m" else "?", color = C.text, style = MaterialTheme.typography.titleLarge)
        }
        Spacer(Modifier.width(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("Waktu di aplikasi pilihan hari ini", color = C.muted, style = MaterialTheme.typography.labelMedium)
            Text(
                when {
                    !usageOn -> "Izinkan Akses penggunaan dulu"
                    limit == 0 -> "$used menit (tanpa batas)"
                    used >= limit -> "Batas $limit menit habis! Diblokir sampai besok."
                    else -> "${limit - used} menit tersisa dari $limit"
                },
                color = C.text, style = MaterialTheme.typography.titleMedium,
            )
            Text("🚫 Diblokir ${store.blockedHits()}x hari ini", color = C.bad, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun AppRow(app: AppInfo, blocked: Boolean, minutes: Int, onToggle: () -> Unit) {
    val bg by animateColorAsState(if (blocked) C.bad.copy(alpha = 0.12f) else C.surface, label = "app")
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(bg).clickable(onClick = onToggle).padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(36.dp).clip(CircleShape).background(if (blocked) C.bad else C.surface2), contentAlignment = Alignment.Center) {
            Text(app.label.take(1).uppercase(), color = if (blocked) Color.White else C.muted, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(app.label, color = C.text, style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (isDistracting(app.pkg)) Pill("Saran", C.gold)
                if (minutes > 0) Text("$minutes menit hari ini", color = C.muted, style = MaterialTheme.typography.labelMedium)
            }
        }
        Switch(blocked, { onToggle() }, colors = SwitchDefaults.colors(checkedTrackColor = C.bad))
    }
}
