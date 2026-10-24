package id.demonpalace.tuntas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Three settings only: theme, reading text size, and reset with confirmation. */
@Composable
fun SettingsScreen(store: Store, onBack: () -> Unit) {
    Column(
        Modifier.fillMaxSize().statusBarsPadding().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Kembali", tint = C.text,
                modifier = Modifier.clip(RoundedCornerShape(50)).clickable(onClick = onBack).testTag("settings_back").padding(8.dp))
            Text("Pengaturan", color = C.text, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(start = 4.dp))
        }

        SectionTitle("Tampilan")
        Segmented(listOf("Ikuti sistem", "Terang", "Gelap"), store.themeMode, "set_theme") { store.updateThemeMode(it) }

        SectionTitle("Ukuran huruf bacaan")
        Segmented(listOf("Kecil", "Sedang", "Besar"), store.readSize, "set_font") { store.updateReadSize(it) }
        Card {
            Text(
                "Gaya adalah tarikan atau dorongan yang dapat mengubah gerak benda.",
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = (17 * READ_SCALES[store.readSize]).sp,
                    lineHeight = (28 * READ_SCALES[store.readSize]).sp, color = C.text),
            )
        }

        SectionTitle("Progres")
        ResetRow(store)
    }
}

@Composable
private fun Segmented(labels: List<String>, selected: Int, tag: String, onSelect: (Int) -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(50)).background(C.surface2).padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        labels.forEachIndexed { i, label ->
            val on = i == selected
            Box(
                Modifier.weight(1f).clip(RoundedCornerShape(50))
                    .background(if (on) C.surface else C.surface2)
                    .then(if (on) Modifier.border(1.dp, C.line, RoundedCornerShape(50)) else Modifier)
                    .clickable { onSelect(i) }.testTag("${tag}_$i")
                    .padding(vertical = 11.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(label, color = if (on) C.text else C.muted, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun ResetRow(store: Store) {
    var confirm by remember { mutableStateOf(false) }
    Card {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Hapus semua progres, skor kuis, catatan kesalahan, dan lencana. Pengaturan tidak ikut terhapus.",
                color = C.muted, style = MaterialTheme.typography.bodyMedium)
            if (!confirm) {
                BouncyButton("Reset progres", Modifier.testTag("reset_progress"), secondary = true) { confirm = true }
            } else {
                Text("Yakin? Progres tidak bisa dikembalikan.", color = C.bad, style = MaterialTheme.typography.titleSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    BouncyButton("Batal", Modifier.weight(1f), secondary = true) { confirm = false }
                    BouncyButton("Reset", Modifier.weight(1f).testTag("reset_confirm"), color = C.bad) { store.resetAll(); confirm = false }
                }
            }
        }
    }
}
