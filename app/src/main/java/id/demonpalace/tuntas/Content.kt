package id.demonpalace.tuntas

import androidx.compose.ui.graphics.Color

enum class Subject(val label: String, val color: Color, val emoji: String) {
    MAT("Matematika", Color(0xFF1CB0F6), "📐"),
    FIS("Fisika", Color(0xFFFF9600), "⚡"),
    BIN("B. Indonesia", Color(0xFFFF4B91), "📖"),
    BIG("B. Inggris", Color(0xFF58CC02), "🌏"),
    MIX("Tryout & Review", Color(0xFFCE82FF), "🎯"),
}

/** A worked example in a lesson: the problem, the solution steps, and the final answer. */
data class Example(val problem: String, val steps: List<String>, val answer: String)

/**
 * One sub-chapter of the book. [points] is the short summary the reader ticks off;
 * the other parts are the textbook body.
 */
data class Section(
    val title: String,
    val points: List<String>,
    val explain: List<String> = emptyList(),
    val formulas: List<String> = emptyList(),
    val examples: List<Example> = emptyList(),
    val pitfalls: List<String> = emptyList(),
)

/**
 * A question in one of the three TKA formats: single answer ([answer]), pilihan ganda kompleks
 * with several correct options ([multi]), or a category table where each row in [rows] takes one
 * of the [options] as its column, keyed by [rowKeys].
 */
data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val answer: Int,
    val explain: String,
    val passage: String? = null,
    val multi: Set<Int>? = null,
    val rows: List<String> = emptyList(),
    val rowKeys: List<Int> = emptyList(),
    /** Figure bundled under assets/, e.g. "soal/kp80_trapesium.jpg". */
    val image: String? = null,
) {
    val isTable get() = rows.isNotEmpty()
    val isMulti get() = multi != null

    /** Letters of the correct options, for feedback text. */
    fun keyText(): String = when {
        isTable -> rows.indices.joinToString("; ") { "${rows[it]} → ${options[rowKeys[it]]}" }
        isMulti -> multi!!.sorted().joinToString(", ") { "${'A' + it}" }
        else -> "${'A' + answer}. ${options[answer]}"
    }
}

data class Step(
    val id: String,
    val day: Int,
    val date: String,
    val subject: Subject,
    val title: String,
    val kisi: String,
    val sections: List<Section>,
    val questions: List<Question>,
    val isReview: Boolean = false,
    /** Practice packs from the question bank: no reading, just a quiz node on the map. */
    val isPractice: Boolean = false,
)

/** One stop on the map: a sub-chapter to read ([section] ≥ 0) or the step's quiz ([section] = -1). */
data class PathNode(val id: String, val step: Step, val section: Int) {
    val isQuiz get() = section < 0
}

data class Source(val title: String, val url: String, val note: String)

private const val PASSAGE_SAMPAH =
    "Sampah plastik di sungai kota meningkat tajam dalam lima tahun terakhir. Data dinas lingkungan menunjukkan volume sampah plastik naik 40% sejak 2021. Menurut saya, pemerintah daerah terlalu lambat menangani masalah ini. Padahal, program bank sampah di beberapa kelurahan terbukti mengurangi sampah plastik hingga seperempatnya."

private const val PASSAGE_SLEEP =
    "Many students believe that studying late at night helps them remember more. However, research suggests the opposite. Sleep plays an important role in moving new information into long-term memory. Students who sleep at least seven hours before a test often score higher than those who stay up late. Therefore, a regular sleep schedule may be one of the simplest ways to improve learning."

private const val PASSAGE_CORAL =
    "Coral reefs cover less than one percent of the ocean floor, yet they support about a quarter of all marine species. Rising sea temperatures cause corals to lose the algae that feed them, a process called bleaching. Without the algae, corals turn white and may die."

private const val PASSAGE_RECOUNT =
    "Last Sunday, my family and I went to Parangtritis Beach. We left home at 6 a.m. and arrived at 8 a.m. We swam, built sandcastles, and ate grilled fish for lunch. In the afternoon, we went home tired but happy."

private const val PASSAGE_DESC =
    "My cat, Milo, has soft orange fur and big green eyes. He is very playful and likes to chase small balls around the house. At night, he always sleeps at the end of my bed."

private const val PASSAGE_EXPO =
    "Students should be allowed to bring phones to school. First, phones help students find information quickly during lessons. Second, parents can contact their children in an emergency. Therefore, schools should allow phones as long as there are clear rules for using them."

object Content {

    /** TKA gelombang 2 starts 2 Nov 2026 (Pusmendik schedule via Medcom). */
    const val EXAM_DATE = "2026-11-02"
    const val MATERIAL_DEADLINE = "2026-10-30"

    val sources = listOf(
        Source("Kisi-kisi TKA SMA 2026 (CNN Indonesia, mengutip Pusmendik)",
            "https://www.cnnindonesia.com/edukasi/20260806110123-569-1389222/mata-pelajaran-yang-diujikan-di-tka-sma-2026-lengkap-kisi-kisinya",
            "Lingkup materi Matematika, B. Indonesia, B. Inggris, Fisika."),
        Source("Kisi-kisi & jadwal TKA SMA/SMK 2026 (Medcom)",
            "https://www.medcom.id/pendidikan/news-pendidikan/4KZQrq6k-kisi-kisi-materi-tka-sma-smk-2026-mapel-wajib-pilihan-dan-jadwal-pelaksanaan",
            "Jadwal gelombang 1 (26–29 Okt) dan gelombang 2 (2–5 Nov), karakter teks bahasa."),
        Source("Kisi-kisi TKA Fisika SMA 2026 (Kumparan, mengutip Pusmendik)",
            "https://kumparan.com/berita-hari-ini/kisi-kisi-tka-fisika-sma-2026-ini-muatan-materinya-28JyA5AFAzi",
            "Enam muatan Fisika: kinematika, dinamika, fluida, gelombang, kalor-termodinamika, kelistrikan."),
        Source("Kisi-kisi TKA SMA Fisika (Bisadanedu)",
            "https://www.bisadanedu.com/artikel/kisi-kisi-tka-sma-fisika",
            "Rincian sub-materi Fisika per muatan."),
        Source("Kisi-kisi TKA SMA 2026 lengkap (Blog ITERA)",
            "https://blog.itera.ac.id/kisi-kisi-tka-sma-2026-lengkap-materi-semua-mapel-wajib-pilihan/",
            "Rincian sub-materi Matematika dan B. Inggris."),
        Source("Contoh soal TKA mapel pilihan SMA (GNFI, dari Pusmendik)",
            "https://www.goodnewsfromindonesia.id/ragam/belajar/contoh-soal-tka-mata-pelajaran-pilihan-jenjang-sma-dari-matematika-tingkat-lanjut-hingga-bahasa-arab",
            "Model soal Fisika: efek Doppler, meja berputar, botol parfum, mesin fotokopi."),
        Source("Contoh soal TKA SMA 2026 (Blog ITERA)",
            "https://blog.itera.ac.id/?p=1518",
            "Model soal diskon bertingkat, nilai minimum fungsi, ide pokok, 'despite', energi kinetik."),
        Source("Laman resmi TKA Kemendikdasmen", "https://tka.kemendikdasmen.go.id", "Cek pengumuman terbaru di sini."),
    )

    private val rawSteps: List<Step> = listOf(
        // ================= HARI 1 · Sab 24 Okt =================
        Step("mat1", 1, "Sab, 24 Okt", Subject.MAT, "Bilangan & Aljabar",
            "Elemen Bilangan dan Aljabar: persen, perbandingan, pangkat, logaritma, persamaan linear & kuadrat, fungsi.",
            listOf(
                Section("Persen & perbandingan", listOf(
                    "Naik p% → dikali (1 + p/100). Diskon p% → dikali (1 − p/100).",
                    "Diskon bertingkat dikalikan berurutan, bukan dijumlah: 20% lalu 10% = ×0,8 × 0,9 = ×0,72.",
                    "Harga awal = harga akhir ÷ faktor. Naik 25% jadi Rp50.000 → 50.000 ÷ 1,25 = Rp40.000.",
                    "Senilai: a₁/b₁ = a₂/b₂. Berbalik nilai (pekerja vs hari): a₁·b₁ = a₂·b₂.",
                )),
                Section("Pangkat & logaritma", listOf(
                    "aᵐ · aⁿ = aᵐ⁺ⁿ,  aᵐ ÷ aⁿ = aᵐ⁻ⁿ,  (aᵐ)ⁿ = aᵐⁿ,  a⁰ = 1.",
                    "ᵃlog b = c artinya aᶜ = b. Contoh ²log 8 = 3 karena 2³ = 8.",
                    "log(xy) = log x + log y,  log(x/y) = log x − log y,  log xⁿ = n·log x.",
                )),
                Section("Persamaan & fungsi", listOf(
                    "SPLDV: eliminasi (jumlah/kurangkan persamaan) lalu substitusi.",
                    "Kuadrat ax² + bx + c = 0: faktorkan. Jumlah akar −b/a, hasil kali c/a.",
                    "Fungsi kuadrat f(x) = ax² + bx + c punya titik puncak di x = −b/(2a). a > 0 → nilai minimum.",
                    "Komposisi (f∘g)(x) = f(g(x)): hitung g dulu. Invers ax + b → (x − b)/a.",
                )),
            ),
            listOf(
                Question("m1", "Sebuah baju seharga Rp200.000 diberi diskon 20%, lalu diskon tambahan 10% dari harga setelah diskon pertama. Harga akhirnya adalah...",
                    listOf("Rp140.000", "Rp144.000", "Rp150.000", "Rp156.000", "Rp160.000"), 1,
                    "200.000 × 0,8 = 160.000, lalu × 0,9 = 144.000. Jebakan: menjumlah diskon jadi 30% (Rp140.000)."),
                Question("m2", "Diketahui f(x) = 2x − 3 dan g(x) = x² + 1. Nilai (f∘g)(2) adalah...",
                    listOf("5", "7", "9", "11", "13"), 1,
                    "g(2) = 5, lalu f(5) = 10 − 3 = 7."),
                Question("m3", "Pekerjaan selesai dalam 12 hari oleh 6 pekerja. Jika dikerjakan 9 pekerja, pekerjaan selesai dalam...",
                    listOf("6 hari", "8 hari", "9 hari", "10 hari", "18 hari"), 1,
                    "Berbalik nilai: 6 × 12 = 9 × n → n = 8 hari."),
                Question("m4", "Nilai ²log 8 + ³log 9 adalah...",
                    listOf("4", "5", "6", "9", "17"), 1,
                    "²log 8 = 3 (2³ = 8) dan ³log 9 = 2 (3² = 9). Jumlahnya 5."),
                Question("m5", "Diketahui f(x) = 2x² − 4x + 1. Nilai minimum fungsi dicapai pada x sama dengan...",
                    listOf("−2", "−1", "0", "1", "2"), 3,
                    "x puncak = −b/(2a) = 4/4 = 1. Karena a = 2 > 0, titik ini minimum."),
                Question("m6", "Jika x + y = 10 dan x − y = 4, nilai 2x + y adalah...",
                    listOf("13", "14", "17", "20", "24"), 2,
                    "Jumlahkan: 2x = 14 → x = 7, y = 3. 2x + y = 17."),
            ),
        ),
        Step("fis1", 1, "Sab, 24 Okt", Subject.FIS, "Kinematika",
            "Muatan Kinematika: pengukuran & angka penting, gerak lurus (GLB/GLBB), gerak parabola, gerak melingkar beraturan.",
            listOf(
                Section("Pengukuran", listOf(
                    "Jangka sorong: ketelitian 0,1 mm (0,01 cm). Hasil = skala utama + (garis nonius yang berimpit × 0,01 cm).",
                    "Mikrometer sekrup: ketelitian 0,01 mm. Hasil = skala utama + (skala putar × 0,01 mm).",
                    "Angka penting: semua angka bukan nol penting. Nol di depan (0,0045) tidak penting, nol di belakang koma (4,50) penting.",
                    "Hasil kali/bagi mengikuti angka penting paling sedikit.",
                )),
                Section("Gerak lurus", listOf(
                    "GLB: s = v·t.",
                    "GLBB: v = v₀ + at,  s = v₀t + ½at²,  v² = v₀² + 2as.",
                    "km/jam ÷ 3,6 = m/s. Direm → a negatif. Lempar ke atas: t puncak = v₀/g.",
                )),
                Section("Gerak parabola & melingkar", listOf(
                    "vₓ = v₀ cos θ (tetap), vᵧ = v₀ sin θ − gt.",
                    "h maks = (v₀ sin θ)²/(2g),  jarak terjauh R = v₀² sin 2θ / g (maksimum di 45°).",
                    "Gerak melingkar beraturan: v = ωr,  percepatan sentripetal a = v²/r = ω²r,  T = 1/f,  ω = 2πf.",
                )),
            ),
            listOf(
                Question("k1", "Pada jangka sorong, skala utama menunjukkan 2,3 cm dan garis nonius ke-4 berimpit dengan skala utama. Hasil pengukurannya adalah...",
                    listOf("2,30 cm", "2,34 cm", "2,40 cm", "2,43 cm", "6,30 cm"), 1,
                    "2,3 cm + 4 × 0,01 cm = 2,34 cm."),
                Question("k2", "Banyak angka penting pada hasil pengukuran 0,0450 kg adalah...",
                    listOf("1", "2", "3", "4", "5"), 2,
                    "Nol di depan tidak penting. Angka 4, 5, dan nol terakhir (di belakang koma) penting → 3."),
                Question("k3", "Mobil mula-mula diam, lalu dipercepat 2 m/s² selama 5 sekon. Jarak yang ditempuh adalah...",
                    listOf("10 m", "20 m", "25 m", "50 m", "100 m"), 2,
                    "s = ½ · 2 · 5² = 25 m."),
                Question("k4", "Mobil melaju 72 km/jam lalu direm hingga berhenti dalam 4 sekon. Besar perlambatannya adalah...",
                    listOf("4 m/s²", "5 m/s²", "10 m/s²", "18 m/s²", "20 m/s²"), 1,
                    "72 km/jam = 20 m/s. a = 20/4 = 5 m/s²."),
                Question("k5", "Bola ditendang 20 m/s dengan sudut 30° terhadap tanah (g = 10 m/s²). Tinggi maksimum bola adalah...",
                    listOf("5 m", "10 m", "15 m", "20 m", "40 m"), 0,
                    "vᵧ = 20 sin 30° = 10 m/s. h = 100/20 = 5 m."),
                Question("k6", "Benda bergerak melingkar beraturan dengan jari-jari 0,5 m dan kelajuan 2 m/s. Percepatan sentripetalnya adalah...",
                    listOf("1 m/s²", "2 m/s²", "4 m/s²", "8 m/s²", "16 m/s²"), 3,
                    "a = v²/r = 4/0,5 = 8 m/s²."),
            ),
        ),
        // ================= HARI 2 · Min 25 Okt =================
        Step("bin1", 2, "Min, 25 Okt", Subject.BIN, "Teks Informasi",
            "B. Indonesia hanya menguji membaca. Teks informasi (fakta, konsep, prosedur), 250–300 kata, kalimat 8–12 kata, konjungsi pertentangan/sebab-akibat.",
            listOf(
                Section("Ide pokok", listOf(
                    "Kalimat utama di awal (deduktif), di akhir (induktif), atau di keduanya (campuran).",
                    "Kalimat utama bersifat umum; kalimat lain menjelaskan atau memberi contoh dan data.",
                    "Tanya: \"paragraf ini sebenarnya membicarakan apa?\" Jawaban terpendeknya adalah ide pokok.",
                )),
                Section("Simpulan & inferensi", listOf(
                    "Simpulan harus mencakup seluruh isi, bukan satu detail.",
                    "Coret pilihan yang menambah info di luar teks atau bertentangan dengan teks.",
                )),
                Section("Fakta vs opini", listOf(
                    "Fakta bisa dibuktikan: angka, data, tempat, waktu.",
                    "Opini memakai penilaian: menurut saya, sebaiknya, seharusnya, mungkin, terlalu, paling indah.",
                )),
                Section("Konjungsi (kisi-kisi resmi)", listOf(
                    "Pertentangan: namun, tetapi, padahal, sedangkan, meskipun.",
                    "Sebab-akibat: karena, sebab, sehingga, akibatnya, oleh karena itu.",
                    "Konjungsi adalah petunjuk hubungan antarkalimat, sering jadi kunci jawaban.",
                )),
            ),
            listOf(
                Question("bi1", "Kalimat yang merupakan opini dalam teks tersebut adalah...",
                    listOf("Sampah plastik di sungai kota meningkat tajam.", "Volume sampah plastik naik 40% sejak 2021.", "Pemerintah daerah terlalu lambat menangani masalah ini.", "Program bank sampah mengurangi sampah plastik.", "Bank sampah ada di beberapa kelurahan."), 2,
                    "Ada penanda \"menurut saya\" dan penilaian \"terlalu lambat\".", PASSAGE_SAMPAH),
                Question("bi2", "Simpulan yang paling tepat dari teks tersebut adalah...",
                    listOf("Sampah plastik tidak bisa dikurangi.", "Bank sampah dapat menjadi solusi untuk menekan sampah plastik yang terus meningkat.", "Dinas lingkungan tidak memiliki data sampah.", "Semua kelurahan sudah memiliki bank sampah.", "Sungai kota bebas dari sampah plastik."), 1,
                    "Simpulan mencakup masalah (sampah naik) dan solusi yang didukung teks (bank sampah).", PASSAGE_SAMPAH),
                Question("bi3", "Ide pokok paragraf tersebut adalah...",
                    listOf("Dampak perubahan iklim terhadap dunia", "Alasan negara-negara beralih ke energi terbarukan", "Jenis-jenis energi terbarukan di dunia", "Proses pengembangan energi surya", "Definisi energi fosil"), 1,
                    "Seluruh paragraf membahas mengapa negara beralih: keterbatasan fosil dan dampak iklim.",
                    "Keterbatasan sumber energi fosil mendorong banyak negara mulai beralih pada pengembangan energi terbarukan seperti surya, angin, dan air. Transisi ini tidak hanya bertujuan menjaga ketersediaan energi jangka panjang, tetapi juga menekan dampak perubahan iklim."),
                Question("bi4", "Kata hubung yang tepat untuk melengkapi kalimat \"Hujan turun sangat deras sejak pagi, ... pasar tetap ramai pembeli.\" adalah...",
                    listOf("sehingga", "karena", "namun", "lalu", "serta"), 2,
                    "Isi kedua bagian bertentangan (hujan deras vs tetap ramai) → konjungsi pertentangan \"namun\"."),
                Question("bi5", "Kalimat berikut yang merupakan fakta adalah...",
                    listOf("Indonesia adalah negara paling indah di dunia.", "Indonesia memiliki lebih dari 17.000 pulau.", "Sebaiknya semua orang berlibur ke Bali.", "Makanan Padang pasti disukai semua orang.", "Mungkin tahun depan harga beras turun."), 1,
                    "Jumlah pulau bisa dibuktikan dengan data. Yang lain memakai penilaian."),
            ),
        ),
        Step("big1", 2, "Min, 25 Okt", Subject.BIG, "Descriptive, Recount, Narrative",
            "B. Inggris menguji membaca 5 jenis teks: descriptive, recount, narrative, procedure, analytical exposition (level A2–B1, 200–350 kata).",
            listOf(
                Section("Kenali jenis teks", listOf(
                    "Descriptive: menggambarkan orang/benda/tempat tertentu. Present tense. Struktur: identification → description.",
                    "Recount: menceritakan pengalaman masa lalu. Past tense, kata waktu (last Sunday, then). Struktur: orientation → events → reorientation.",
                    "Narrative: cerita dengan konflik (legenda, fabel). Struktur: orientation → complication → resolution. Sering ditanya moral value.",
                )),
                Section("Strategi soal", listOf(
                    "Main idea: baca kalimat pertama dan terakhir paragraf.",
                    "Detail: ambil kata kunci soal (when, where, who), lalu scanning.",
                    "Reference (he, it, they): cari kata benda sebelumnya yang cocok jumlahnya.",
                    "Vocabulary: lihat petunjuk konteks dan kata sambung (in contrast, however, so).",
                )),
            ),
            listOf(
                Question("g1", "What is the purpose of the text?",
                    listOf("To describe Parangtritis Beach", "To retell the writer's past experience", "To persuade readers to visit the beach", "To explain how to build sandcastles", "To entertain with a fable"), 1,
                    "Past tense dan urutan kejadian (left, arrived, swam) → recount, tujuannya menceritakan kembali pengalaman.", PASSAGE_RECOUNT),
                Question("g2", "When did the family arrive at the beach?",
                    listOf("At 6 a.m.", "At 8 a.m.", "At noon", "In the afternoon", "At night"), 1,
                    "\"We left home at 6 a.m. and arrived at 8 a.m.\" Jebakan: 6 a.m. adalah waktu berangkat.", PASSAGE_RECOUNT),
                Question("g3", "The text mainly describes...",
                    listOf("how to take care of a cat", "the writer's bedroom", "the writer's cat, Milo", "a game with small balls", "different kinds of cats"), 2,
                    "Descriptive text menggambarkan satu objek tertentu: Milo.", PASSAGE_DESC),
                Question("g4", "The word \"he\" in the second sentence refers to...",
                    listOf("the writer", "Milo", "a ball", "the house", "the bed"), 1,
                    "Kata benda tunggal sebelumnya yang cocok adalah Milo, kucing si penulis.", PASSAGE_DESC),
                Question("g5", "What moral value can we learn from the story?",
                    listOf("Always travel far away", "Respect and love your parents", "Money brings happiness", "Never pray to God", "Ships are dangerous"), 1,
                    "Malin dihukum karena durhaka pada ibunya → hormati orang tua.",
                    "Malin Kundang became a rich merchant after years of sailing. When his ship landed in his hometown, his old mother ran to hug him. Ashamed of her poor clothes, Malin said she was not his mother. Heartbroken, she prayed, and a storm turned Malin into stone."),
                Question("g6", "The word \"spacious\" is closest in meaning to...",
                    listOf("noisy", "roomy", "old", "dark", "expensive"), 1,
                    "\"In contrast\" membandingkan dengan \"cramped\" (sempit). Lawannya: lapang (roomy).",
                    "The old library was cramped and dark. In contrast, the new one is spacious and bright."),
            ),
        ),
        // ================= HARI 3 · Sen 26 Okt =================
        Step("fis2", 3, "Sen, 26 Okt", Subject.FIS, "Dinamika",
            "Muatan Dinamika: hukum Newton & jenis gaya, usaha-energi, momentum & impuls, dinamika rotasi (torsi, momen inersia).",
            listOf(
                Section("Hukum Newton & gaya", listOf(
                    "ΣF = m·a. Pakai komponen gaya searah gerak (F cos θ).",
                    "Berat w = mg. Gesek kinetis f = μₖ·N. Di bidang datar N = mg.",
                    "Lift naik dipercepat: N = m(g + a). Turun dipercepat: N = m(g − a).",
                    "Pegas (Hooke): F = k·Δx.",
                )),
                Section("Usaha & energi", listOf(
                    "W = F·s·cos θ. Ek = ½mv². Ep = mgh. P = W/t.",
                    "W = ΔEk. Tanpa gesekan, energi mekanik kekal: Ep₁ + Ek₁ = Ep₂ + Ek₂.",
                    "Ek sebanding v²: kecepatan 2× → Ek 4×.",
                )),
                Section("Momentum & impuls", listOf(
                    "p = mv. Impuls I = F·Δt = Δp. Berbalik arah → tanda kecepatan berlawanan.",
                    "Kekekalan: m₁v₁ + m₂v₂ = m₁v₁' + m₂v₂'. Menempel: (m₁ + m₂)v'.",
                )),
                Section("Dinamika rotasi", listOf(
                    "Torsi τ = F·r·sin θ (gaya tegak lurus lengan → τ = F·r).",
                    "Momen inersia partikel I = mr². Hukum II rotasi: τ = I·α.",
                )),
            ),
            listOf(
                Question("d1", "Balok 4 kg di lantai licin ditarik gaya 20 N membentuk sudut 60° terhadap horizontal. Percepatan balok adalah...",
                    listOf("2,5 m/s²", "4 m/s²", "5 m/s²", "8,7 m/s²", "10 m/s²"), 0,
                    "F cos 60° = 10 N. a = 10/4 = 2,5 m/s²."),
                Question("d2", "Balok 5 kg ditarik gaya horizontal 20 N di lantai dengan koefisien gesek kinetis 0,2 (g = 10 m/s²). Percepatan balok adalah...",
                    listOf("1 m/s²", "2 m/s²", "3 m/s²", "4 m/s²", "5 m/s²"), 1,
                    "f = 0,2 × 50 = 10 N. a = (20 − 10)/5 = 2 m/s²."),
                Question("d3", "Benda 2 kg bergerak 4 m/s. Jika kecepatannya dinaikkan menjadi 8 m/s, energi kinetiknya menjadi ... kali semula.",
                    listOf("2", "3", "4", "6", "8"), 2,
                    "Ek ∝ v². (8/4)² = 4 kali."),
                Question("d4", "Bola 0,5 kg jatuh bebas dari ketinggian 5 m (g = 10 m/s²). Energi kinetik saat menyentuh tanah adalah...",
                    listOf("2,5 J", "10 J", "25 J", "50 J", "100 J"), 2,
                    "Ep = mgh = 25 J berubah seluruhnya jadi Ek."),
                Question("d5", "Benda 2 kg bergerak 6 m/s menumbuk benda 1 kg yang diam, lalu keduanya menempel. Kecepatan setelah tumbukan adalah...",
                    listOf("2 m/s", "3 m/s", "4 m/s", "6 m/s", "12 m/s"), 2,
                    "12 = 3v' → v' = 4 m/s."),
                Question("d6", "Kunci pas sepanjang 25 cm diberi gaya 20 N tegak lurus di ujungnya. Torsi yang dihasilkan adalah...",
                    listOf("0,5 N·m", "5 N·m", "20 N·m", "50 N·m", "500 N·m"), 1,
                    "τ = F·r = 20 × 0,25 = 5 N·m. Ingat ubah cm ke m."),
                Question("d7", "Bola 0,2 kg datang 10 m/s lalu dipukul balik 20 m/s. Besar impulsnya adalah...",
                    listOf("2 N·s", "4 N·s", "6 N·s", "10 N·s", "30 N·s"), 2,
                    "I = 0,2 × (20 − (−10)) = 6 N·s."),
            ),
        ),
        Step("mat2", 3, "Sen, 26 Okt", Subject.MAT, "Geometri, Pengukuran & Trigonometri",
            "Elemen Geometri & pengukuran (bangun datar/ruang, kesebangunan, transformasi) dan Trigonometri (perbandingan, sudut istimewa, kontekstual).",
            listOf(
                Section("Bangun datar & ruang", listOf(
                    "Pythagoras c² = a² + b². Tripel: 3-4-5, 6-8-10, 5-12-13, 8-15-17.",
                    "Lingkaran: K = 2πr, L = πr². π = 22/7 kalau r kelipatan 7.",
                    "Tabung V = πr²t, kerucut ⅓πr²t, bola 4/3πr³, prisma = luas alas × tinggi.",
                )),
                Section("Kesebangunan & transformasi", listOf(
                    "Sebangun: sisi bersesuaian sebanding. Soal bayangan: tinggi₁/bayangan₁ = tinggi₂/bayangan₂.",
                    "Cermin sumbu-x: (x, y) → (x, −y). Sumbu-y: (−x, y). Titik asal: (−x, −y). Garis y = x: (y, x).",
                    "Translasi (a, b): (x + a, y + b).",
                )),
                Section("Trigonometri", listOf(
                    "sin = depan/miring, cos = samping/miring, tan = depan/samping.",
                    "sin 30° = ½, sin 45° = ½√2, sin 60° = ½√3; cos 30° = ½√3, cos 60° = ½; tan 45° = 1.",
                    "Identitas: sin²x + cos²x = 1.",
                )),
            ),
            listOf(
                Question("g7", "Segitiga siku-siku memiliki sisi tegak 6 cm dan 8 cm. Panjang sisi miringnya adalah...",
                    listOf("7 cm", "10 cm", "12 cm", "14 cm", "48 cm"), 1, "√(36 + 64) = 10 cm."),
                Question("g8", "Tangga 10 m bersandar di dinding dan membentuk sudut 30° dengan tanah. Tinggi ujung atas tangga adalah...",
                    listOf("5 m", "5√2 m", "5√3 m", "8 m", "10 m"), 0, "10 × sin 30° = 5 m."),
                Question("g9", "Tongkat 1,5 m memiliki bayangan 2 m. Pada saat yang sama, bayangan sebuah pohon 8 m. Tinggi pohon adalah...",
                    listOf("4 m", "6 m", "8 m", "10 m", "12 m"), 1, "1,5/2 = t/8 → t = 6 m."),
                Question("g10", "Titik A(2, 3) dicerminkan terhadap sumbu-x. Bayangannya adalah...",
                    listOf("(−2, 3)", "(2, −3)", "(−2, −3)", "(3, 2)", "(3, −2)"), 1, "Cermin sumbu-x: y berubah tanda."),
                Question("g11", "Volume tabung berjari-jari 7 cm dan tinggi 10 cm (π = 22/7) adalah...",
                    listOf("440 cm³", "770 cm³", "1.540 cm³", "3.080 cm³", "4.620 cm³"), 2, "22/7 × 49 × 10 = 1.540 cm³."),
                Question("g12", "Keliling lingkaran 44 cm (π = 22/7). Luasnya adalah...",
                    listOf("77 cm²", "154 cm²", "308 cm²", "616 cm²", "1.386 cm²"), 1, "2πr = 44 → r = 7. L = 154 cm²."),
            ),
        ),
        // ================= HARI 4 · Sel 27 Okt =================
        Step("bin2", 4, "Sel, 27 Okt", Subject.BIN, "Teks Fiksi & Kosakata",
            "Teks fiksi (realisme/absurd, tokoh bulat, konflik jamak, alur & sudut pandang campuran) serta kosakata abstrak, istilah teknis, konotatif, kalimat inversi.",
            listOf(
                Section("Unsur intrinsik", listOf(
                    "Tema, alur, tokoh & penokohan, latar, sudut pandang, amanat.",
                    "Sudut pandang orang pertama: aku/saya. Orang ketiga: dia, nama tokoh.",
                    "Amanat = pesan moral, cari dari akibat yang dialami tokoh.",
                    "Alur campuran: maju lalu kilas balik (flashback).",
                )),
                Section("Majas", listOf(
                    "Personifikasi: benda bersifat manusia (angin berbisik).",
                    "Metafora: perbandingan langsung (dia bintang kelas). Simile: pakai seperti/bagaikan.",
                    "Hiperbola: berlebihan (tangisnya membanjiri bumi).",
                )),
                Section("Kosakata & kalimat", listOf(
                    "Denotatif = makna sebenarnya. Konotatif = makna kias (buah bibir = bahan pembicaraan).",
                    "Kata abstrak: tidak bisa diindra (kejujuran, harapan). Istilah teknis: khusus bidang tertentu.",
                    "Kalimat inversi: predikat sebelum subjek. \"Datanglah tamu itu.\"",
                )),
            ),
            listOf(
                Question("f1", "\"Angin malam membisikkan rahasia kepada dedaunan.\" Majas dalam kalimat tersebut adalah...",
                    listOf("Hiperbola", "Personifikasi", "Simile", "Metafora", "Litotes"), 1, "Angin digambarkan bisa berbisik seperti manusia."),
                Question("f2", "Amanat yang paling sesuai dengan cerita tersebut adalah...",
                    listOf("Belajarlah hanya saat ujian.", "Jangan menunda-nunda pekerjaan.", "Ujian itu menakutkan.", "Nilai tidak penting.", "Tidurlah lebih awal."), 1,
                    "Akibat buruk Dimas berasal dari kebiasaan menunda.",
                    "Dimas selalu menunda tugas. Saat malam sebelum ujian, ia panik karena belum belajar sama sekali, dan nilainya pun jauh dari harapan."),
                Question("f3", "Makna kata bercetak miring dalam kalimat \"Sejak menang lomba, Rani menjadi buah bibir di kampungnya.\" adalah...",
                    listOf("makanan kesukaan", "bahan pembicaraan", "orang yang cerewet", "hadiah lomba", "orang yang dibenci"), 1,
                    "\"Buah bibir\" bermakna konotatif: sesuatu yang banyak dibicarakan orang."),
                Question("f4", "Kalimat inversi terdapat pada...",
                    listOf("Ia pergi ke pasar pagi ini.", "Datanglah tamu itu dengan tergesa-gesa.", "Ibu memasak nasi goreng.", "Kami belajar bersama di perpustakaan.", "Hujan turun sejak sore."), 1,
                    "Predikat \"datanglah\" mendahului subjek \"tamu itu\"."),
                Question("f5", "\"Aku tak menyangka surat itu akan mengubah hidupku. Malam itu, kubaca berulang kali.\" Sudut pandang penggalan cerita tersebut adalah...",
                    listOf("Orang pertama pelaku utama", "Orang pertama pelaku sampingan", "Orang ketiga serba tahu", "Orang ketiga terbatas", "Orang kedua"), 0,
                    "Tokoh \"aku\" menceritakan pengalamannya sendiri."),
            ),
        ),
        Step("fis3", 4, "Sel, 27 Okt", Subject.FIS, "Fluida",
            "Muatan Fluida: tekanan hidrostatis, hukum Pascal, hukum Archimedes, kontinuitas, prinsip Bernoulli.",
            listOf(
                Section("Fluida statis", listOf(
                    "Tekanan hidrostatis Pₕ = ρ·g·h (h diukur dari permukaan). Air: ρ = 1000 kg/m³.",
                    "Pascal: F₁/A₁ = F₂/A₂. Dongkrak hidrolik: penampang besar → gaya besar.",
                    "Archimedes: gaya apung Fₐ = ρ_fluida · g · V_tercelup.",
                    "Terapung: ρ benda < ρ fluida. Melayang: sama. Tenggelam: lebih besar.",
                )),
                Section("Fluida dinamis", listOf(
                    "Kontinuitas: A₁v₁ = A₂v₂. Pipa menyempit → aliran makin cepat.",
                    "Debit Q = A·v = V/t.",
                    "Bernoulli: di tempat aliran lebih cepat, tekanan lebih kecil.",
                    "Contoh: sayap pesawat (udara atas lebih cepat → tekanan atas kecil → gaya angkat), penyemprot parfum/obat nyamuk.",
                )),
            ),
            listOf(
                Question("fl1", "Tekanan hidrostatis pada kedalaman 2 m di dalam air (ρ = 1000 kg/m³, g = 10 m/s²) adalah...",
                    listOf("200 Pa", "2.000 Pa", "20.000 Pa", "200.000 Pa", "2.000.000 Pa"), 2, "P = 1000 × 10 × 2 = 20.000 Pa."),
                Question("fl2", "Dongkrak hidrolik memiliki penampang kecil 10 cm² dan penampang besar 200 cm². Gaya 50 N pada penampang kecil dapat mengangkat beban...",
                    listOf("2,5 N", "250 N", "500 N", "1.000 N", "10.000 N"), 3, "F₂ = 50 × 200/10 = 1.000 N."),
                Question("fl3", "Benda bervolume 0,002 m³ tercelup seluruhnya dalam air (ρ = 1000 kg/m³, g = 10 m/s²). Gaya apungnya adalah...",
                    listOf("0,2 N", "2 N", "20 N", "200 N", "2.000 N"), 2, "Fₐ = 1000 × 10 × 0,002 = 20 N."),
                Question("fl4", "Air mengalir dari pipa berpenampang 4 cm² dengan kecepatan 2 m/s ke pipa berpenampang 1 cm². Kecepatan di pipa kecil adalah...",
                    listOf("0,5 m/s", "2 m/s", "4 m/s", "8 m/s", "16 m/s"), 3, "4 × 2 = 1 × v → v = 8 m/s."),
                Question("fl5", "Saat bola karet penyemprot parfum ditekan, udara mengalir cepat melewati ujung atas pipa kecil yang tercelup parfum. Parfum naik dan menyembur karena...",
                    listOf("tekanan udara di atas pipa menjadi lebih kecil daripada tekanan pada permukaan parfum", "tekanan udara di atas pipa menjadi lebih besar", "massa jenis parfum bertambah", "gaya apung parfum hilang", "suhu parfum naik"), 0,
                    "Prinsip Bernoulli: aliran cepat → tekanan rendah, sehingga tekanan di permukaan parfum mendorongnya naik. (Model soal Pusmendik.)"),
            ),
        ),
        // ================= HARI 5 · Rab 28 Okt =================
        Step("mat4", 4, "Sel, 27 Okt", Subject.MAT, "Fungsi, Barisan & Deret",
            "Elemen Aljabar: domain-range, fungsi kuadrat, komposisi & invers, barisan-deret aritmetika & geometri, pertumbuhan, peluruhan, bunga tunggal & majemuk.",
            emptyList(),
            listOf(
                Question("m4a", "Diketahui f(x) = 2x + 3 dan g(x) = x − 1. Nilai (f∘g)(4) adalah...",
                    listOf("7", "8", "9", "10", "11"), 2, "g(4) = 3, lalu f(3) = 6 + 3 = 9."),
                Question("m4b", "Invers dari f(x) = (x + 5)/2 adalah...",
                    listOf("f⁻¹(x) = 2x − 5", "f⁻¹(x) = 2x + 5", "f⁻¹(x) = (x − 5)/2", "f⁻¹(x) = 5 − 2x", "f⁻¹(x) = x/2 − 5"), 0,
                    "y = (x + 5)/2 → 2y = x + 5 → x = 2y − 5. Cek: f(1) = 3 dan f⁻¹(3) = 1."),
                Question("m4c", "Suku ke-2 barisan aritmetika adalah 7 dan suku ke-5 adalah 16. Suku ke-10 adalah...",
                    listOf("28", "30", "31", "34", "37"), 2, "3b = 16 − 7 = 9 → b = 3, a = 4. U₁₀ = 4 + 9 × 3 = 31."),
                Question("m4d", "Uang Rp4.000.000 disimpan dengan bunga majemuk 5% per tahun. Besar simpanan setelah 2 tahun adalah...",
                    listOf("Rp4.200.000", "Rp4.400.000", "Rp4.410.000", "Rp4.420.000", "Rp4.500.000"), 2,
                    "4.000.000 × 1,05² = 4.000.000 × 1,1025 = 4.410.000. (Bunga tunggal hanya 4.400.000.)"),
                Question("m4e", "Titik puncak grafik f(x) = x² − 4x + 1 adalah...",
                    listOf("(−2, 13)", "(2, −3)", "(2, 5)", "(4, 1)", "(−2, −3)"), 1, "x = −(−4)/2 = 2; f(2) = 4 − 8 + 1 = −3."),
                Question("m4f", "Domain fungsi f(x) = √(6 − 2x) adalah...",
                    listOf("x ≥ 3", "x ≤ 3", "x > 3", "x < 3", "x ≤ −3"), 1, "6 − 2x ≥ 0 → −2x ≥ −6 → x ≤ 3 (tanda dibalik karena dibagi −2)."),
                Question("m4g", "Jumlah tak hingga deret 12 + 6 + 3 + ... adalah...",
                    listOf("18", "21", "24", "30", "36"), 2, "a = 12, r = ½. S∞ = 12/(1 − ½) = 24."),
            ),
        ),
        Step("mat3", 5, "Rab, 28 Okt", Subject.MAT, "Data & Peluang",
            "Elemen Data dan peluang: statistika deskriptif, penyajian data, kaidah pencacahan, peluang kejadian.",
            listOf(
                Section("Statistika", listOf(
                    "Mean = jumlah ÷ banyak data. Modus = paling sering. Median: urutkan dulu.",
                    "Jangkauan = terbesar − terkecil.",
                    "Rata-rata gabungan: total baru = n_baru × rata-rata baru.",
                    "Baca grafik/tabel dengan teliti: perhatikan satuan dan skala sumbu.",
                )),
                Section("Pencacahan & peluang", listOf(
                    "Permutasi (urutan penting, misal ketua-sekretaris): P(n,r) = n!/(n−r)!.",
                    "Kombinasi (urutan tidak penting, misal memilih tim): C(n,r) = n!/(r!(n−r)!).",
                    "P(A) = n(A)/n(S). Dua dadu n(S) = 36. Kartu bridge 52 kartu, 4 As, 13 per jenis.",
                    "Frekuensi harapan = P(A) × banyak percobaan.",
                )),
            ),
            listOf(
                Question("p1", "Dua dadu dilempar bersamaan. Peluang jumlah mata dadu 7 adalah...",
                    listOf("1/12", "1/9", "1/6", "5/36", "7/36"), 2, "Ada 6 pasangan dari 36 → 1/6."),
                Question("p2", "Data: 6, 7, 7, 8, 9, 9, 9, 10. Pernyataan: (1) modus 9, (2) median 8,5, (3) rata-rata lebih besar dari median, (4) jangkauan 4. Yang benar adalah...",
                    listOf("1 dan 2 saja", "1, 2, dan 4", "2 dan 3", "1, 3, dan 4", "Semua benar"), 1,
                    "Rata-rata 65/8 ≈ 8,13 < median 8,5, jadi (3) salah."),
                Question("p3", "Dari 5 siswa akan dipilih 2 orang untuk ikut lomba. Banyak cara memilih adalah...",
                    listOf("5", "10", "20", "25", "60"), 1, "C(5,2) = 10."),
                Question("p4", "Dari 5 calon akan dipilih ketua dan sekretaris. Banyak susunan yang mungkin adalah...",
                    listOf("10", "15", "20", "25", "120"), 2, "Urutan penting: P(5,2) = 5 × 4 = 20."),
                Question("p5", "Rata-rata nilai 5 siswa 70. Setelah 1 siswa baru bergabung, rata-ratanya menjadi 72. Nilai siswa baru adalah...",
                    listOf("72", "74", "80", "82", "84"), 3, "432 − 350 = 82."),
                Question("p6", "Satu kartu diambil acak dari 52 kartu bridge. Peluang terambil kartu As adalah...",
                    listOf("1/52", "1/26", "1/13", "1/4", "4/13"), 2, "4/52 = 1/13."),
            ),
        ),
        Step("big2", 5, "Rab, 28 Okt", Subject.BIG, "Procedure & Analytical Exposition",
            "Teks procedure dan analytical exposition; pemahaman tersirat dan tujuan penulis.",
            listOf(
                Section("Procedure", listOf(
                    "Tujuan: menjelaskan cara membuat/melakukan sesuatu.",
                    "Struktur: goal → materials → steps. Kalimat perintah, kata urutan First, Then, Next, Finally.",
                )),
                Section("Analytical exposition", listOf(
                    "Tujuan: meyakinkan pembaca bahwa suatu pendapat benar (persuade).",
                    "Struktur: thesis (pendapat) → arguments (First, Second) → reiteration (Therefore, In conclusion).",
                    "Soal favorit: tujuan penulis, pendapat penulis, fungsi paragraf terakhir.",
                )),
                Section("Kata sambung penting", listOf(
                    "Contrast: however, but, yet, despite, although, in contrast.",
                    "Result: therefore, so, as a result. Addition: moreover, furthermore.",
                )),
            ),
            listOf(
                Question("e1", "What is the writer's purpose?",
                    listOf("To tell a story about phones", "To describe a phone", "To persuade readers that phones should be allowed at school", "To explain how phones work", "To report school news"), 2,
                    "Ada thesis (should be allowed), dua argumen, dan reiteration → analytical exposition, tujuannya membujuk.", PASSAGE_EXPO),
                Question("e2", "The last sentence of the text functions as the...",
                    listOf("thesis", "first argument", "reiteration", "orientation", "complication"), 2,
                    "\"Therefore...\" mengulang pendapat penulis di akhir → reiteration.", PASSAGE_EXPO),
                Question("e3", "\"Despite the heavy rain, the farmers continued harvesting the rice before the flood arrived.\" The word \"despite\" indicates...",
                    listOf("cause and effect", "time sequence", "contrast", "addition", "comparison"), 2,
                    "Hujan deras tapi tetap memanen → pertentangan."),
                Question("e4", "The purpose of the text is to...",
                    listOf("tell a funny story", "describe noodles", "explain how to cook noodles", "persuade people to eat noodles", "report news about food"), 2,
                    "First, Then, Finally → teks procedure.",
                    "First, boil some water. Then, add the noodles and cook them for three minutes. Finally, add the seasoning and stir well."),
                Question("e5", "What is the purpose of the text?",
                    listOf("To entertain readers", "To describe a bottle", "To persuade readers to buy the product", "To explain how to make a bottle", "To report news"), 2,
                    "Kalimat ajakan \"Buy...today!\" → membujuk.",
                    "Buy EcoBottle today! It keeps your drink cold for 24 hours and helps reduce plastic waste."),
            ),
        ),
        Step("fis4", 5, "Rab, 28 Okt", Subject.FIS, "Gelombang: Bunyi & Cahaya",
            "Muatan Gelombang: bunyi (karakteristik, sumber, intensitas, efek Doppler) dan cahaya (gelombang elektromagnetik, cermin, lensa, alat optik).",
            listOf(
                Section("Dasar gelombang", listOf(
                    "v = λ·f. T = 1/f = t/n.",
                    "Bunyi = gelombang longitudinal (butuh medium). Cahaya = gelombang elektromagnetik (bisa merambat di ruang hampa).",
                )),
                Section("Bunyi", listOf(
                    "Intensitas I = P/(4πr²). Jarak 2× → intensitas ¼.",
                    "Efek Doppler: sumber dan pendengar saling mendekat → frekuensi terdengar lebih tinggi; saling menjauh → lebih rendah.",
                    "fₚ = (v ± vₚ)/(v ∓ vₛ) · fₛ (tanda atas saat mendekat).",
                )),
                Section("Cahaya: cermin & lensa", listOf(
                    "1/f = 1/s + 1/s'. Cermin/lensa cekung-cembung: f = R/2.",
                    "Cermin cekung & lensa cembung: f positif. Cermin cembung & lensa cekung: f negatif.",
                    "s' positif → bayangan nyata. Perbesaran M = |s'/s|.",
                    "Kekuatan lensa P = 1/f (f dalam meter) atau 100/f (f dalam cm), satuan dioptri.",
                )),
            ),
            listOf(
                Question("w1", "Pengendara motor dan ambulans yang membunyikan sirine sama-sama bergerak. Pengendara mendengar frekuensi sirine LEBIH TINGGI dari aslinya ketika...",
                    listOf("keduanya saling menjauh", "keduanya saling mendekat", "motor menjauh dengan kecepatan sama dengan ambulans yang mengejar", "keduanya diam", "ambulans menjauh dari motor yang diam"), 1,
                    "Efek Doppler: saling mendekat → frekuensi terdengar naik. (Model soal Pusmendik.)"),
                Question("w2", "Gelombang memiliki frekuensi 50 Hz dan panjang gelombang 2 m. Cepat rambatnya adalah...",
                    listOf("25 m/s", "52 m/s", "100 m/s", "200 m/s", "0,04 m/s"), 2, "v = 2 × 50 = 100 m/s."),
                Question("w3", "Terbentuk 20 gelombang dalam 4 sekon. Periode gelombangnya adalah...",
                    listOf("0,2 s", "0,5 s", "5 s", "20 s", "80 s"), 0, "T = 4/20 = 0,2 s."),
                Question("w4", "Benda diletakkan 15 cm di depan cermin cekung berfokus 10 cm. Letak bayangannya adalah...",
                    listOf("6 cm di depan cermin", "12 cm di depan cermin", "25 cm di depan cermin", "30 cm di depan cermin", "30 cm di belakang cermin"), 3,
                    "1/s' = 1/10 − 1/15 = 1/30 → s' = 30 cm (positif, nyata, di depan cermin)."),
                Question("w5", "Lensa cembung memiliki jarak fokus 50 cm. Kekuatan lensanya adalah...",
                    listOf("0,02 dioptri", "0,5 dioptri", "2 dioptri", "5 dioptri", "50 dioptri"), 2, "P = 100/50 = 2 dioptri."),
                Question("w6", "Jika jarak pendengar dari sumber bunyi dijadikan 2 kali semula, intensitas bunyi yang diterima menjadi...",
                    listOf("4 kali", "2 kali", "tetap", "½ kali", "¼ kali"), 4, "I ∝ 1/r². (1/2)² = ¼."),
            ),
        ),
        // ================= HARI 6 · Kam 29 Okt =================
        Step("fis5", 6, "Kam, 29 Okt", Subject.FIS, "Kalor & Termodinamika",
            "Muatan Kalor & Termodinamika: kalor & perpindahannya, pemanasan global (efek rumah kaca), gas ideal (Boyle, Boyle–Gay-Lussac), proses & hukum termodinamika, mesin kalor.",
            listOf(
                Section("Kalor", listOf(
                    "Q = m·c·ΔT (kenaikan suhu). Q = m·L (perubahan wujud).",
                    "Asas Black: kalor lepas = kalor terima. Dua air bermassa sama → suhu akhir = rata-rata.",
                    "Perpindahan: konduksi (zat padat), konveksi (aliran fluida), radiasi (tanpa medium).",
                )),
                Section("Pemanasan global", listOf(
                    "Efek rumah kaca: gas CO₂, CH₄, uap air menahan radiasi inframerah dari bumi sehingga suhu naik.",
                    "Solusi: kurangi bahan bakar fosil, energi terbarukan, menanam pohon.",
                )),
                Section("Gas ideal", listOf(
                    "Boyle (suhu tetap): P₁V₁ = P₂V₂.",
                    "Boyle–Gay-Lussac: P₁V₁/T₁ = P₂V₂/T₂ (T dalam kelvin = °C + 273).",
                    "PV = nRT.",
                )),
                Section("Termodinamika", listOf(
                    "Hukum I: ΔU = Q − W (Q + diserap, W + gas melakukan usaha).",
                    "Isobarik: W = P·ΔV. Isokhorik: W = 0. Isotermik: ΔU = 0. Adiabatik: Q = 0.",
                    "Mesin Carnot: η = 1 − T_rendah/T_tinggi (kelvin).",
                )),
            ),
            listOf(
                Question("t1", "Kalor yang diperlukan untuk menaikkan suhu 2 kg air sebesar 10 °C (c = 4.200 J/kg°C) adalah...",
                    listOf("8,4 kJ", "21 kJ", "42 kJ", "84 kJ", "840 kJ"), 3, "Q = 2 × 4200 × 10 = 84.000 J = 84 kJ."),
                Question("t2", "100 g air bersuhu 80 °C dicampur dengan 100 g air bersuhu 20 °C. Suhu akhir campuran adalah...",
                    listOf("30 °C", "40 °C", "50 °C", "60 °C", "100 °C"), 2, "Massa sama → (80 + 20)/2 = 50 °C."),
                Question("t3", "Gas bertekanan 2 atm bervolume 3 L dikembangkan pada suhu tetap hingga volumenya 6 L. Tekanannya menjadi...",
                    listOf("0,5 atm", "1 atm", "2 atm", "4 atm", "6 atm"), 1, "Boyle: 2 × 3 = P × 6 → P = 1 atm."),
                Question("t4", "Gas menyerap kalor 500 J dan melakukan usaha 200 J. Perubahan energi dalamnya adalah...",
                    listOf("−300 J", "200 J", "300 J", "500 J", "700 J"), 2, "ΔU = 500 − 200 = 300 J."),
                Question("t5", "Mesin Carnot bekerja antara suhu 600 K dan 300 K. Efisiensinya adalah...",
                    listOf("25%", "33%", "50%", "67%", "100%"), 2, "η = 1 − 300/600 = 50%."),
                Question("t6", "Penyebab utama efek rumah kaca adalah...",
                    listOf("Lapisan ozon menebal", "Gas seperti CO₂ menahan radiasi inframerah yang dipancarkan bumi", "Bumi makin dekat ke matahari", "Hujan asam", "Berkurangnya uap air"), 1,
                    "Gas rumah kaca menyerap dan memancarkan kembali radiasi inframerah, sehingga panas tertahan."),
            ),
        ),
        Step("fis6", 6, "Kam, 29 Okt", Subject.FIS, "Kelistrikan",
            "Muatan Kelistrikan: listrik statis (hukum Coulomb, medan & potensial listrik) dan rangkaian arus searah (Ohm, Kirchhoff, seri-paralel, daya).",
            listOf(
                Section("Listrik statis", listOf(
                    "Coulomb: F = k·q₁·q₂/r², k = 9 × 10⁹ N·m²/C². Jarak 2× → gaya ¼.",
                    "Medan listrik E = F/q = k·q/r². Di antara dua pelat sejajar: E = V/d.",
                    "Gaya pada muatan di antara pelat: F = q·E = q·V/d. Jarak pelat makin kecil → gaya makin besar.",
                    "1 μC = 10⁻⁶ C.",
                )),
                Section("Rangkaian arus searah", listOf(
                    "Ohm: V = I·R.",
                    "Seri: R = R₁ + R₂, arus sama. Paralel: 1/R = 1/R₁ + 1/R₂, tegangan sama.",
                    "Kirchhoff I: ΣI masuk = ΣI keluar. Kirchhoff II: ΣV dalam loop = 0.",
                    "Daya P = VI = I²R = V²/R. Energi W = P·t (kWh = W × jam ÷ 1000).",
                )),
            ),
            listOf(
                Question("l1", "Dua muatan masing-masing 2 μC terpisah sejauh 30 cm (k = 9 × 10⁹ N·m²/C²). Gaya Coulomb antara keduanya adalah...",
                    listOf("0,04 N", "0,12 N", "0,4 N", "1,2 N", "4 N"), 2,
                    "F = 9×10⁹ × (2×10⁻⁶)² / 0,3² = 0,036/0,09 = 0,4 N."),
                Question("l2", "Jika jarak dua muatan dijadikan 2 kali semula, gaya Coulomb-nya menjadi...",
                    listOf("4 kali", "2 kali", "tetap", "½ kali", "¼ kali"), 4, "F ∝ 1/r²."),
                Question("l3", "Pada mesin fotokopi, partikel bermuatan 1 × 10⁻⁷ C berada di antara dua pelat berjarak 2 mm dengan beda potensial 200 V. Gaya listrik pada partikel adalah...",
                    listOf("0,001 N", "0,01 N", "0,1 N", "1 N", "10 N"), 1,
                    "E = 200/0,002 = 10⁵ V/m. F = 10⁻⁷ × 10⁵ = 0,01 N. (Model soal Pusmendik.)"),
                Question("l4", "Pada soal mesin fotokopi tadi, jika jarak pelat dikurangi menjadi 1,5 mm (tegangan tetap), gaya pada partikel menjadi...",
                    listOf("lebih kecil, sekitar 0,0075 N", "tetap 0,01 N", "lebih besar, sekitar 0,013 N", "nol", "dua kali, 0,02 N"), 2,
                    "F = qV/d. d mengecil → F membesar: 10⁻⁷ × 200/0,0015 ≈ 0,0133 N."),
                Question("l5", "Resistor 3 Ω dan 6 Ω dipasang paralel, lalu diseri dengan resistor 2 Ω dan baterai 12 V. Pernyataan yang SALAH adalah...",
                    listOf("Hambatan total rangkaian 4 Ω", "Arus total 3 A", "Tegangan pada resistor 2 Ω adalah 6 V", "Arus pada resistor 3 Ω sama dengan arus pada resistor 6 Ω"), 3,
                    "Paralel = 2 Ω, total 4 Ω, I = 3 A. Arus di 3 Ω = 2 A, di 6 Ω = 1 A."),
                Question("l6", "Lampu 60 W menyala selama 5 jam. Energi listrik yang dipakai adalah...",
                    listOf("0,03 kWh", "0,3 kWh", "3 kWh", "12 kWh", "300 kWh"), 1, "60 × 5 = 300 Wh = 0,3 kWh."),
            ),
        ),
        // ================= HARI 7 · Jum 30 Okt =================
        Step("try1", 7, "Jum, 30 Okt", Subject.MAT, "Tryout Matematika",
            "Campuran semua elemen Matematika. Kerjakan tanpa catatan, sekitar 2 menit per soal.",
            listOf(Section("Aturan tryout", listOf(
                "Tutup catatan. Anggap ini ujian sungguhan.",
                "Macet lebih dari 2 menit? Pilih jawaban terbaik, lanjut, catat nomornya.",
                "Setelah selesai, buka Progres → Ulang soal salah.",
            ))),
            listOf(
                Question("tm1", "Harga sepatu naik 25% sehingga menjadi Rp50.000. Harga sebelum naik adalah...",
                    listOf("Rp37.500", "Rp40.000", "Rp42.500", "Rp45.000", "Rp62.500"), 1, "50.000 ÷ 1,25 = 40.000."),
                Question("tm2", "Toko memberi diskon 20%, lalu diskon tambahan 10% untuk member dari harga setelah diskon pertama. Harga awal barang Rp250.000. Harga yang dibayar member adalah...",
                    listOf("Rp170.000", "Rp175.000", "Rp180.000", "Rp182.000", "Rp190.000"), 2, "250.000 × 0,8 × 0,9 = 180.000."),
                Question("tm3", "Suku ke-20 dari barisan 3, 7, 11, 15, ... adalah...",
                    listOf("75", "79", "80", "83", "87"), 1, "3 + 19 × 4 = 79."),
                Question("tm4", "Akar-akar persamaan x² − 5x + 6 = 0 adalah...",
                    listOf("−2 dan −3", "1 dan 6", "2 dan 3", "−1 dan 6", "−2 dan 3"), 2, "(x − 2)(x − 3) = 0."),
                Question("tm5", "Jika f(x) = 3x − 6, maka f⁻¹(9) adalah...",
                    listOf("1", "3", "5", "7", "21"), 2, "(9 + 6)/3 = 5."),
                Question("tm6", "Jarak dua kota pada peta berskala 1 : 200.000 adalah 3 cm. Jarak sebenarnya adalah...",
                    listOf("0,6 km", "6 km", "60 km", "600 km", "66 km"), 1, "600.000 cm = 6 km."),
                Question("tm7", "Nilai 2⁵ × 2³ ÷ 2⁶ adalah...",
                    listOf("2", "4", "8", "16", "32"), 1, "2⁵⁺³⁻⁶ = 2² = 4."),
            ),
        ),
        Step("try2", 7, "Jum, 30 Okt", Subject.FIS, "Tryout Fisika",
            "Campuran 6 muatan Fisika. Tulis besaran yang diketahui dan samakan satuan dulu.",
            listOf(Section("Aturan tryout", listOf(
                "Ubah satuan: km/jam → m/s, cm → m, L → m³, °C → K, menit → s.",
                "g = 10 m/s² kecuali disebut lain.",
            ))),
            listOf(
                Question("tf1", "Bola dilempar vertikal ke atas 30 m/s (g = 10 m/s²). Waktu mencapai titik tertinggi adalah...",
                    listOf("1 s", "2 s", "3 s", "6 s", "30 s"), 2, "t = 30/10 = 3 s."),
                Question("tf2", "Beban diletakkan di atas meja putar yang berputar dengan kecepatan sudut 4 rad/s. Koefisien gesek statis 0,48 (g = 10 m/s²). Jarak terjauh beban dari pusat agar tidak terlempar adalah...",
                    listOf("8 cm", "16 cm", "24 cm", "30 cm", "40 cm"), 3,
                    "Gesek statis = gaya sentripetal: μmg = mω²r → r = μg/ω² = 4,8/16 = 0,3 m. (Model soal Pusmendik.)"),
                Question("tf3", "Benda 2 kg yang diam diberi gaya tetap 10 N selama 3 s. Kecepatan akhirnya adalah...",
                    listOf("5 m/s", "6 m/s", "10 m/s", "15 m/s", "30 m/s"), 3, "a = 5 m/s², v = 15 m/s."),
                Question("tf4", "Pegas dengan konstanta 200 N/m bertambah panjang 10 cm. Gaya tariknya adalah...",
                    listOf("2 N", "20 N", "200 N", "2.000 N", "0,5 N"), 1, "F = 200 × 0,1 = 20 N."),
                Question("tf5", "Gas pada tekanan tetap 2 × 10⁵ Pa mengembang dari 2 L menjadi 5 L. Usaha gas adalah...",
                    listOf("6 J", "60 J", "600 J", "6.000 J", "600.000 J"), 2, "2×10⁵ × 3×10⁻³ = 600 J."),
                Question("tf6", "Dua resistor 4 Ω dirangkai seri dengan baterai 12 V. Arusnya adalah...",
                    listOf("0,67 A", "1,5 A", "3 A", "6 A", "48 A"), 1, "12/8 = 1,5 A."),
                Question("tf7", "Mesin melakukan usaha 3.000 J dalam 1 menit. Dayanya adalah...",
                    listOf("30 W", "50 W", "300 W", "3.000 W", "180.000 W"), 1, "3000/60 = 50 W."),
            ),
        ),
        // ================= HARI 8 · Sab 31 Okt =================
        Step("try3", 8, "Sab, 31 Okt", Subject.MIX, "Tryout B. Indonesia + B. Inggris",
            "Membaca teks informasi, fiksi, dan 5 jenis teks bahasa Inggris.",
            listOf(Section("Aturan tryout", listOf(
                "Baca pertanyaan dulu, baru teks.",
                "Garis bawahi kata sambung: namun, sehingga, however, therefore.",
            ))),
            listOf(
                Question("tb1", "What is the main idea of the text?",
                    listOf("Students like to study at night.", "Sleep helps students learn and remember better.", "Tests are difficult for most students.", "Long-term memory is hard to build.", "Students should study for seven hours."), 1,
                    "Semua kalimat pendukung menjelaskan manfaat tidur.", PASSAGE_SLEEP),
                Question("tb2", "The word \"opposite\" is closest in meaning to...",
                    listOf("same", "contrary", "similar", "correct", "important"), 1, "\"However\" menandakan pertentangan.", PASSAGE_SLEEP),
                Question("tb3", "What can be inferred from the text?",
                    listOf("Staying up late before a test is not a good strategy.", "All students sleep seven hours.", "Research about sleep is wrong.", "Studying is not needed if you sleep well.", "Tests should be held at night."), 0,
                    "D terlalu ekstrem; teks tidak bilang belajar tidak perlu.", PASSAGE_SLEEP),
                Question("tb4", "According to the text, what causes coral bleaching?",
                    listOf("Plastic waste", "Rising sea temperatures", "Too many marine species", "Fishing activities", "Cold water"), 1,
                    "Kalimat kedua menyebutnya langsung.", PASSAGE_CORAL),
                Question("tb5", "The word \"yet\" in the first sentence shows...",
                    listOf("a cause", "an example", "a contrast", "a time order", "a conclusion"), 2,
                    "Kecil (<1%) tapi mendukung banyak spesies → pertentangan.", PASSAGE_CORAL),
                Question("tb6", "Pernyataan yang sesuai dengan isi teks adalah...",
                    listOf("Taman kota membuat warga malas berolahraga.", "Taman kota memberi dampak baik bagi kesehatan dan ekonomi warga.", "Pedagang kecil dilarang berjualan di taman.", "Taman kota hanya ramai pada malam hari.", "Warga menolak pembangunan taman kota."), 1,
                    "Warga lebih sering olahraga (kesehatan) dan pedagang mulai berjualan (ekonomi).",
                    "Sejak taman kota dibuka tahun lalu, warga sekitar lebih sering berolahraga pada pagi hari. Pedagang kecil pun mulai berjualan di sekitarnya sehingga penghasilan mereka bertambah."),
                Question("tb7", "\"Senja itu, langit menangis tanpa henti.\" Majas dalam kalimat tersebut adalah...",
                    listOf("Metafora", "Personifikasi", "Hiperbola", "Simile", "Litotes"), 1, "Langit digambarkan bisa menangis seperti manusia."),
            ),
        ),
        Step("rev1", 8, "Sab, 31 Okt", Subject.MIX, "Ulang Soal yang Salah",
            "Kerjakan ulang semua soal yang pernah salah sampai bersih.",
            listOf(Section("Cara mengulang", listOf(
                "Buka Progres → Ulang soal salah. Soal yang dijawab benar akan hilang dari daftar.",
                "Tulis di buku catatan kesalahan: soalnya, kenapa salah, cara yang benar.",
                "Target: daftar soal salah kosong sebelum 1 November.",
            ))),
            emptyList(), isReview = true,
        ),
        // ================= HARI 9 · Min 1 Nov =================
        Step("rev2", 9, "Min, 1 Nov", Subject.MIX, "Rumus Inti & Tips Hari-H",
            "Ringkasan akhir sebelum TKA gelombang 2 (2–5 November 2026).",
            listOf(
                Section("Rumus inti Matematika", listOf(
                    "Persen bertingkat: kalikan faktornya. Berbalik nilai: a₁b₁ = a₂b₂.",
                    "aᵐ·aⁿ = aᵐ⁺ⁿ. ᵃlog b = c ⇔ aᶜ = b. Puncak parabola x = −b/2a.",
                    "Uₙ = a + (n − 1)b. P(n,r) = n!/(n−r)!, C(n,r) = n!/(r!(n−r)!).",
                    "sin 30 = ½, cos 60 = ½, sin 60 = ½√3, tan 45 = 1.",
                )),
                Section("Rumus inti Fisika", listOf(
                    "v = v₀ + at, s = v₀t + ½at², a_s = v²/r.",
                    "ΣF = ma, f = μN, τ = F·r, W = Fs cos θ, Ek = ½mv², p = mv, I = Δp.",
                    "P = ρgh, F₁/A₁ = F₂/A₂, Fₐ = ρgV, A₁v₁ = A₂v₂.",
                    "v = λf, 1/f = 1/s + 1/s', P = 100/f(cm).",
                    "Q = mcΔT, P₁V₁ = P₂V₂, ΔU = Q − W, η = 1 − Tr/Tt.",
                    "F = kq₁q₂/r², E = V/d, V = IR, P = VI.",
                )),
                Section("Tips hari-H", listOf(
                    "Baca pertanyaan dulu, baru teksnya.",
                    "Pilihan ganda kompleks: nilai tiap pernyataan satu per satu.",
                    "Lewati soal yang macet lebih dari 2 menit, kembali di akhir.",
                    "Malam ini berhenti belajar sebelum 21.00 dan tidur cukup.",
                )),
            ),
            emptyList(), isReview = true,
        ),
    )

    /** The daily plan with full textbook sections, before it is cut into chapters. */
    val planSteps: List<Step> = rawSteps.map { s ->
        s.copy(sections = LessonsExtra.arrange(s.id, Lessons.byStep[s.id] ?: s.sections))
    }

    val steps: List<Step> = planSteps.flatMap { Chapters.split(it) }.let { base -> insertPractice(base) }

    /** Every stop on the map in order: each sub-chapter, then the chapter quiz. */
    val nodes: List<PathNode> = steps.flatMap { s ->
        s.sections.indices.map { PathNode("${s.id}#$it", s, it) } +
            if (s.questions.isNotEmpty()) listOf(PathNode(s.id, s, -1)) else emptyList()
    }

    /** The subject books: every chapter that has reading, grouped by subject. */
    val books: Map<Subject, List<Step>> = steps.filter { it.sections.isNotEmpty() && !it.isReview && !it.id.startsWith("try") }
        .groupBy { it.subject }

    private fun pack(id: String, day: Int, date: String, subject: Subject, title: String, qs: List<Question>) =
        Step(id, day, date, subject, title, "Paket latihan dari soal Kompeten TKA dan soal model resmi. Kerjakan seperti ujian.", emptyList(), qs, isPractice = true)

    /** Spreads the question-bank packs over the plan so every day has extra practice. */
    private fun insertPractice(base: List<Step>): List<Step> {
        val kb = Bank.kompetenBin.chunked(11)
        val kg = Bank.kompetenBig.chunked(10)
        val lb = Bank.latihanBig.chunked(10)
        val rb = Bank.resmiBig.chunked(13)
        val after = mapOf(
            "bin1" to listOf(pack("kbin1", 2, "Min, 25 Okt", Subject.BIN, "Paket B. Indonesia 1", kb[0])),
            "big1" to listOf(pack("lbig1", 2, "Min, 25 Okt", Subject.BIG, "English Practice 1", lb[0])),
            "bin2" to listOf(pack("kbin2", 4, "Sel, 27 Okt", Subject.BIN, "Paket B. Indonesia 2", kb[1])),
            "big2" to listOf(pack("lbig2", 5, "Rab, 28 Okt", Subject.BIG, "English Practice 2", lb[1])),
            "mat3" to listOf(pack("kmat1", 5, "Rab, 28 Okt", Subject.MAT, "Paket Numerasi", Bank.kompetenMat)),
            "fis6" to listOf(pack("kbin3", 6, "Kam, 29 Okt", Subject.BIN, "Paket B. Indonesia 3", kb[2])),
            "try2" to listOf(
                pack("rbig1", 7, "Jum, 30 Okt", Subject.BIG, "Soal Model Resmi B. Inggris 1", rb[0]),
                pack("rbig2", 7, "Jum, 30 Okt", Subject.BIG, "Soal Model Resmi B. Inggris 2", rb[1]),
            ),
            "try3" to listOf(
                pack("kbin4", 8, "Sab, 31 Okt", Subject.BIN, "Paket B. Indonesia 4", kb[3]),
                pack("kbig1", 8, "Sab, 31 Okt", Subject.BIG, "Paket B. Inggris 1", kg[0]),
            ),
            "rev1" to listOf(
                pack("kbig2", 9, "Min, 1 Nov", Subject.BIG, "Paket B. Inggris 2", kg[1]),
                pack("kbig3", 9, "Min, 1 Nov", Subject.BIG, "Paket B. Inggris 3", kg[2]),
            ),
        )
        // Packs go after the last chapter cut from a plan step: "mat3b" belongs to "mat3".
        val group = { id: String -> Regex("^[a-z]+\\d+").find(id)?.value ?: id }
        return base.flatMapIndexed { i, s ->
            val last = base.getOrNull(i + 1)?.let { group(it.id) != group(s.id) } ?: true
            listOf(s) + if (last) after[group(s.id)].orEmpty() else emptyList()
        }
    }

    val questionCount: Int get() = steps.sumOf { it.questions.size }

    val allQuestions: Map<String, Pair<Step, Question>> =
        steps.flatMap { s -> s.questions.map { it.id to (s to it) } }.toMap()
}
