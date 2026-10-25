package id.demonpalace.tuntas

/**
 * Sub-chapters that close the gaps between [Lessons] and the full SMA 2026 kisi-kisi
 * (Pusmendik, via Brain Academy / ITERA / CNN). Each entry rebuilds a step's section list
 * from the base sections so new topics land in a sensible reading order.
 */
object LessonsExtra {

    private fun ex(problem: String, vararg steps: String, answer: String) = Example(problem, steps.toList(), answer)

    fun arrange(stepId: String, base: List<Section>): List<Section> = when (stepId) {
        "mat1" -> listOf(jenisBilangan) + base.take(3) + listOf(bentukAkar) + base.drop(3) + listOf(spltv, programLinear)
        "mat4" -> listOf(fungsiDomain, komposisiInvers, deretAritmetika, deretGeometri)
        "mat2" -> listOf(sudutGaris) + base.take(1) + listOf(bangunDatar) + base.drop(1).take(1) + listOf(luasPermukaan, jarakRuang) +
            base.drop(2).take(1) + listOf(rotasiDilatasi) + base.drop(3) + listOf(trigLengkap)
        "mat3" -> listOf(diagramData) + base.take(1) + listOf(dataKelompok, penyebaran) + base.drop(1) + listOf(peluangMajemuk)
        "bin1" -> base + listOf(kerangkaTeks, teksJamak, keakuratan, kataSerapan)
        "bin2" -> base + listOf(puisi, drama, prediksiNilai, kalimatTandaBaca)
        "big1" -> LessonsBig.big1
        "big2" -> LessonsBig.big2
        "fis1" -> base + listOf(gerakVertikal)
        "fis4" -> base.take(2) + listOf(persamaanGelombang, tarafIntensitas) + base.drop(2) + listOf(interferensi, alatOptik)
        "fis5" -> base.take(1) + listOf(pemuaian) + base.drop(1) + listOf(kinetikGas)
        "fis6" -> base + listOf(potensialKapasitor, ggl)
        else -> base
    }

    // ============================== MATEMATIKA ==============================

    private val jenisBilangan = Section(
        title = "Jenis & sifat bilangan",
        explain = listOf(
            "Bilangan dikelompokkan dari yang paling sederhana. Bilangan asli adalah 1, 2, 3, dan seterusnya. Kalau 0 ikut dimasukkan, namanya bilangan cacah. Kalau bilangan negatif juga ikut, namanya bilangan bulat: …, −2, −1, 0, 1, 2, ….",
            "Bilangan rasional adalah bilangan yang bisa ditulis sebagai pecahan §\\frac{a}{b}§ dengan a dan b bulat dan §b \\neq 0§. Bentuk desimalnya selalu berhenti (0,25) atau berulang (0,333…). Bilangan irasional tidak bisa ditulis sebagai pecahan, desimalnya tidak berhenti dan tidak berulang, misalnya §\\sqrt{2}§, §\\sqrt{5}§, dan §\\pi§. Gabungan rasional dan irasional disebut bilangan real.",
            "Bilangan prima punya tepat dua faktor: 1 dan dirinya sendiri. Angka 1 bukan prima karena faktornya hanya satu, dan 2 adalah satu-satunya prima genap.",
            "Operasi bilangan punya sifat yang membuat hitungan lebih cepat. Komutatif: §a + b = b + a§ dan §a \\times b = b \\times a§. Asosiatif: §(a + b) + c = a + (b + c)§. Distributif: §a \\times (b + c) = a \\times b + a \\times c§. Pengurangan dan pembagian tidak komutatif.",
            "Urutan mengerjakan operasi: tanda kurung dulu, lalu pangkat, lalu kali dan bagi (dari kiri ke kanan), terakhir tambah dan kurang (dari kiri ke kanan). Untuk tanda: negatif × negatif = positif, negatif × positif = negatif.",
            "Pecahan dijumlah setelah penyebutnya disamakan (pakai KPK). Pecahan dibagi dengan cara mengalikan kebalikannya.",
        ),
        formulas = listOf(
            "§\\mathbb{N} \\subset \\text{Cacah} \\subset \\mathbb{Z} \\subset \\mathbb{Q} \\subset \\mathbb{R}§",
            "§a(b + c) = ab + ac§",
            "§\\dfrac{a}{b} + \\dfrac{c}{d} = \\dfrac{ad + bc}{bd}§",
            "§\\dfrac{a}{b} \\div \\dfrac{c}{d} = \\dfrac{a}{b} \\times \\dfrac{d}{c}§",
        ),
        examples = listOf(
            ex("Hitung §- 3 \\times (4 - 10) + 12 \\div (- 4)§.",
                "Kerjakan kurung dulu: §4 - 10 = - 6§.",
                "Kali dan bagi: §- 3 \\times (- 6) = 18§ dan §12 \\div (- 4) = - 3§.",
                "Jumlahkan: §18 + (- 3) = 15§.",
                answer = "15"),
            ex("Hitung §\\frac{2}{3} + \\frac{3}{4} - \\frac{1}{6}§.",
                "KPK dari 3, 4, dan 6 adalah 12.",
                "§\\frac{2}{3} = \\frac{8}{12}§, §\\frac{3}{4} = \\frac{9}{12}§, §\\frac{1}{6} = \\frac{2}{12}§.",
                "§\\frac{8}{12} + \\frac{9}{12} - \\frac{2}{12} = \\frac{15}{12} = \\frac{5}{4} = 1\\tfrac{1}{4}§.",
                answer = "§1\\tfrac{1}{4}§"),
            ex("Manakah bilangan irasional: 0,25; §\\sqrt{16}§; §\\sqrt{5}§; §\\frac{22}{7}§?",
                "§0{,}25 = \\frac{1}{4}§, jadi rasional.",
                "§\\sqrt{16} = 4§, bulat, jadi rasional.",
                "§\\frac{22}{7}§ adalah pecahan, jadi rasional (hanya pendekatan §\\pi§).",
                "§\\sqrt{5} = 2{,}2360679§… tidak berhenti dan tidak berulang.",
                answer = "§\\sqrt{5}§"),
        ),
        pitfalls = listOf(
            "Menganggap 1 bilangan prima.",
            "Menganggap §\\frac{22}{7}§ irasional. Yang irasional adalah §\\pi§, sedangkan §\\frac{22}{7}§ hanya pendekatannya.",
            "Mengerjakan dari kiri ke kanan tanpa mendahulukan kali dan bagi.",
        ),
        points = listOf(
            "Rasional = bisa jadi pecahan; irasional = desimal tak berhenti dan tak berulang.",
            "Urutan: kurung, pangkat, kali/bagi, tambah/kurang.",
            "Pecahan: samakan penyebut dulu; bagi = kali kebalikan.",
        ),
    )

    private val bentukAkar = Section(
        title = "Bentuk akar",
        explain = listOf(
            "Akar adalah kebalikan dari pangkat. §\\sqrt{a}§ adalah bilangan yang kalau dikuadratkan hasilnya a. Dalam bentuk pangkat, §\\sqrt{a} = a ^ (\\frac{1}{2})§, dan akar pangkat n dari §a ^ m§ sama dengan §a ^ (\\frac{m}{n})§. Karena itu semua sifat pangkat juga berlaku untuk akar.",
            "Menyederhanakan akar: pecah bilangan di dalam akar menjadi perkalian bilangan kuadrat sempurna (4, 9, 16, 25, 36, …) dengan bilangan lain. Contohnya §\\sqrt{12} = \\sqrt{4\\times3} = \\sqrt{4} \\times \\sqrt{3} = 2\\sqrt{3}§.",
            "Akar hanya bisa dijumlah atau dikurang kalau sejenis, sama seperti §2x + 5x = 7x§. Jadi §2\\sqrt{3} + 5\\sqrt{3} = 7\\sqrt{3}§, tetapi §\\sqrt{2} + \\sqrt{3}§ tidak bisa digabung.",
            "Merasionalkan penyebut artinya menghilangkan akar dari penyebut. Untuk §\\frac{a}{\\sqrt{b}}§, kalikan atas dan bawah dengan §\\sqrt{b}§. Untuk penyebut berbentuk §\\sqrt{b} + \\sqrt{c}§ atau §\\sqrt{b} - \\sqrt{c}§, kalikan dengan bentuk sekawannya (tandanya dibalik). Ini bekerja karena §(x + y)(x - y) = x^{2} - y^{2}§, sehingga akarnya hilang.",
        ),
        formulas = listOf(
            "§\\sqrt{a} \\cdot \\sqrt{b} = \\sqrt{ab} \\qquad \\dfrac{\\sqrt{a}}{\\sqrt{b}} = \\sqrt{\\dfrac{a}{b}}§",
            "§\\sqrt[n]{a^m} = a^{\\frac{m}{n}}§",
            "§\\dfrac{a}{\\sqrt{b}} = \\dfrac{a\\sqrt{b}}{b}§",
            "§\\dfrac{a}{\\sqrt{b} - \\sqrt{c}} = \\dfrac{a(\\sqrt{b} + \\sqrt{c})}{b - c}§",
        ),
        examples = listOf(
            ex("Sederhanakan §\\sqrt{50} + \\sqrt{18} - \\sqrt{8}§.",
                "§\\sqrt{50} = \\sqrt{25\\times2} = 5\\sqrt{2}§.",
                "§\\sqrt{18} = \\sqrt{9\\times2} = 3\\sqrt{2}§ dan §\\sqrt{8} = \\sqrt{4\\times2} = 2\\sqrt{2}§.",
                "§5\\sqrt{2} + 3\\sqrt{2} - 2\\sqrt{2} = 6\\sqrt{2}§.",
                answer = "§6\\sqrt{2}§"),
            ex("Rasionalkan §\\frac{4}{\\sqrt{5}-1}§.",
                "Sekawan dari §\\sqrt{5} - 1§ adalah §\\sqrt{5} + 1§.",
                "§4\\frac{\\sqrt{5}+1}{(\\sqrt{5}-1)(\\sqrt{5}+1)} = 4\\frac{\\sqrt{5}+1}{5-1}§.",
                "= §4\\frac{\\sqrt{5}+1}{4} = \\sqrt{5} + 1§.",
                answer = "§\\sqrt{5} + 1§"),
            ex("Hitung §8 ^ (\\frac{2}{3})§.",
                "§8 ^ (\\frac{2}{3})§ = (∛8)².",
                "∛§8 = 2§, lalu §2^{2} = 4§.",
                answer = "4"),
        ),
        pitfalls = listOf(
            "Menulis §\\sqrt{2} + \\sqrt{3} = \\sqrt{5}§. Salah, akar tidak sejenis tidak bisa dijumlah.",
            "Lupa mengalikan pembilang juga saat merasionalkan.",
        ),
        points = listOf(
            "Sederhanakan dengan faktor kuadrat sempurna.",
            "Hanya akar sejenis yang bisa dijumlah.",
            "Rasionalkan dengan §\\sqrt{b}§ atau bentuk sekawan.",
        ),
    )

    private val spltv = Section(
        title = "Sistem persamaan linear tiga variabel",
        explain = listOf(
            "Sistem persamaan linear tiga variabel (SPLTV) terdiri dari tiga persamaan dengan tiga variabel, misalnya x, y, dan z. Soal sekolah umumnya paling banyak 3 variabel.",
            "Idenya sama dengan SPLDV: kurangi jumlah variabel satu per satu. Pilih satu variabel, lalu eliminasi variabel itu dari dua pasang persamaan. Hasilnya dua persamaan dengan dua variabel (SPLDV). Selesaikan SPLDV itu, lalu substitusi kembali untuk mendapat variabel ketiga.",
            "Selalu cek jawaban ke ketiga persamaan. Kalau satu saja tidak cocok, ada salah hitung.",
            "Soal cerita (harga barang, umur, campuran) diubah dulu menjadi tiga persamaan. Tuliskan apa arti setiap variabel supaya tidak tertukar.",
        ),
        formulas = listOf(
            "Langkah: eliminasi 1 variabel dari 2 pasang persamaan, selesaikan SPLDV-nya, lalu substitusi balik",
        ),
        examples = listOf(
            ex("Selesaikan: §x + y + z = 6§,  §x - y + z = 2§,  §2x + y - z = 1§.",
                "Persamaan (1) − (2): §2y = 4§, jadi §y = 2§.",
                "Masukkan §y = 2§ ke (1): §x + z = 4§. Masukkan ke (3): §2x + 2 - z = 1§, jadi §2x - z = - 1§.",
                "Jumlahkan §x + z = 4§ dan §2x - z = - 1§: §3x = 3§, jadi §x = 1§. Maka §z = 4 - 1 = 3§.",
                "Cek (2): §1 - 2 + 3 = 2§. Cek (3): §2 + 2 - 3 = 1§.",
                answer = "§x = 1§, §y = 2§, §z = 3§"),
            ex("1 pensil + 2 buku + 1 penghapus = Rp7.500; 2 pensil + 1 buku + 1 penghapus = Rp5.500; 1 pensil + 1 buku + 2 penghapus = Rp5.000. Berapa harga 1 pensil + 1 buku + 1 penghapus?",
                "Misalkan p, b, h. §(1)p + 2b + h = 7.500§, §(2)2p + b + h = 5.500§, §(3)p + b + 2h = 5.000§.",
                "(1) − (2): §- p + b = 2.000§, jadi §b = p + 2.000§. (1) − (3): §b - h = 2.500§, jadi §h = b - 2.500 = p - 500§.",
                "Masukkan ke (2): §2p + (p + 2.000) + (p - 500) = 5.500 \\to 4p = 4.000 \\to p = 1.000§.",
                "§b = 3.000§ dan §h = 500§. Jumlahnya §1.000 + 3.000 + 500 = 4.500§.",
                answer = "Rp4.500"),
        ),
        pitfalls = listOf("Mengeliminasi variabel yang berbeda pada dua pasang persamaan, sehingga hasilnya tetap tiga variabel."),
        points = listOf(
            "Eliminasi satu variabel yang sama dari dua pasang persamaan.",
            "Selesaikan SPLDV, lalu substitusi balik.",
            "Cek ke ketiga persamaan.",
        ),
    )

    private val programLinear = Section(
        title = "Pertidaksamaan linear & program linear",
        explain = listOf(
            "Pertidaksamaan linear diselesaikan seperti persamaan, dengan satu aturan penting: kalau kedua ruas dikali atau dibagi bilangan NEGATIF, tanda ketidaksamaan dibalik. Alasannya, §2 < 3§ tetapi §- 2 > - 3§.",
            "Pertidaksamaan dua variabel seperti §x + y \\le 4§ punya daerah penyelesaian, bukan satu titik. Cara menggambarnya: gambar garis §x + y = 4§ lewat titik potong sumbu (0, 4) dan (4, 0), lalu uji titik (0, 0). Karena §0 + 0 \\le 4§ benar, daerah yang memuat (0, 0) adalah penyelesaiannya.",
            "Program linear mencari nilai maksimum atau minimum dari fungsi objektif (misalnya keuntungan) dengan kendala berupa sistem pertidaksamaan. Langkahnya: (1) tentukan variabel, (2) tulis kendala, (3) gambar daerah penyelesaian, (4) cari titik-titik pojoknya, (5) masukkan setiap titik pojok ke fungsi objektif.",
            "Cukup titik pojok yang diuji: fungsi objektif linear nilainya berubah searah di sepanjang daerah, sehingga nilai terbesar dan terkecil pasti ada di ujung-ujung daerah, yaitu titik pojoknya.",
        ),
        formulas = listOf(
            "Dikali/dibagi negatif, jadi tanda dibalik",
            "Garis §ax + by = c§ memotong sumbu di §\\left(\\frac{c}{a}, 0\\right)§ dan §\\left(0, \\frac{c}{b}\\right)§",
            "Nilai optimum §f(x, y) = px + qy§ ada di titik pojok",
        ),
        examples = listOf(
            ex("Selesaikan §- 2x + 5 > 11§.",
                "Kurangi 5: §- 2x > 6§.",
                "Bagi −2 dan BALIK tanda: §x < - 3§.",
                answer = "§x < - 3§"),
            ex("Tentukan nilai maksimum §f = 3x + 2y§ dengan kendala §x + y \\le 4§, §x + 3y \\le 6§, §x \\ge 0§, §y \\ge 0§.",
                "Titik pojok: (0, 0), (4, 0), (0, 2), dan perpotongan kedua garis.",
                "Perpotongan: §(x + 3y) - (x + y) = 6 - 4 \\to 2y = 2 \\to y = 1§, §x = 3§.",
                "§f(0{,}0) = 0§, §f(4{,}0) = 12§, §f(0{,}2) = 4§, §f(3{,}1) = 9 + 2 = 11§.",
                answer = "Maksimum 12 di (4, 0)"),
            ex("Roti A butuh 200 g tepung dan 25 g mentega; roti B butuh 100 g tepung dan 50 g mentega. Tersedia 4 kg tepung dan 1,25 kg mentega. Untung roti A Rp1.000 dan roti B Rp1.500. Berapa untung maksimum?",
                "Misal x roti A dan y roti B. Tepung: §200x + 100y \\le 4.000 \\to 2x + y \\le 40§. Mentega: §25x + 50y \\le 1.250 \\to x + 2y \\le 50§.",
                "Titik pojok: (0, 0), (20, 0), (0, 25), dan perpotongan. Perpotongan: §2(2x + y) - (x + 2y) = 80 - 50 \\to 3x = 30 \\to x = 10§, §y = 20§.",
                "Untung = 1.000x + 1.500y: (20,0) → 20.000; (0,25) → 37.500; §(10{,}20) \\to 10.000 + 30.000 = 40.000§.",
                answer = "Rp40.000 (10 roti A dan 20 roti B)"),
        ),
        pitfalls = listOf(
            "Lupa membalik tanda saat membagi dengan bilangan negatif.",
            "Hanya mengecek titik potong sumbu dan melupakan titik perpotongan dua garis.",
        ),
        points = listOf(
            "Kali/bagi negatif, jadi balik tanda.",
            "Uji titik (0, 0) untuk menentukan daerah.",
            "Optimum selalu di titik pojok.",
        ),
    )

    private val fungsiDomain = Section(
        title = "Fungsi: domain, kodomain & range",
        explain = listOf(
            "Fungsi adalah aturan yang memasangkan SETIAP anggota himpunan asal (domain) dengan TEPAT SATU anggota himpunan kawan (kodomain). Kumpulan hasil yang benar-benar dipakai disebut daerah hasil atau range, dan range selalu bagian dari kodomain.",
            "Pada grafik, cara menguji fungsi adalah uji garis vertikal: kalau ada garis tegak yang memotong grafik di dua titik, berarti satu x punya dua pasangan, jadi bukan fungsi.",
            "Domain alami adalah semua x yang membuat rumus bisa dihitung. Dua aturan utamanya: penyebut tidak boleh nol, dan bilangan di dalam akar kuadrat tidak boleh negatif.",
            "Fungsi linear §f(x) = mx + c§ grafiknya garis lurus dengan gradien m. Fungsi kuadrat §f(x) = ax^{2} + bx + c§ grafiknya parabola: terbuka ke atas kalau §a > 0§ (punya nilai minimum) dan ke bawah kalau §a < 0§ (punya nilai maksimum). Titik puncaknya ada di §x = - \\frac{b}{2a}§. Fungsi rasional berbentuk pecahan, misalnya §f(x) = \\frac{2x+1}{x-3}§, dengan x yang membuat penyebut nol dikeluarkan dari domain.",
        ),
        formulas = listOf(
            "Pecahan: penyebut §\\neq 0§ · Akar: isi akar §\\geq 0§",
            "Kuadrat: §x_p = -\\dfrac{b}{2a}, \\quad y_p = -\\dfrac{D}{4a}, \\quad D = b^2 - 4ac§",
            "§a > 0 \\Rightarrow y \\geq y_p \\cdot a < 0 \\Rightarrow y \\leq y_p§",
        ),
        examples = listOf(
            ex("Tentukan domain §f(x) = \\frac{2x+1}{x-3}§ dan §g(x) = \\sqrt{x-2}§.",
                "f: penyebut §x - 3 \\neq 0§, jadi §x \\neq 3§.",
                "g: isi akar §x - 2 \\ge 0§, jadi §x \\ge 2§.",
                answer = "f: §x \\neq 3§;  g: §x \\ge 2§"),
            ex("Tentukan titik puncak dan range §f(x) = x^{2} - 6x + 5§.",
                "§a = 1§, §b = - 6§. §x_{p} = - \\frac{-6}{2\\times1} = 3§.",
                "§y_{p} = f(3) = 9 - 18 + 5 = - 4§. Puncak (3, −4).",
                "§a > 0§, parabola terbuka ke atas, jadi nilai terkecil −4.",
                answer = "Puncak (3, −4); range §y \\ge - 4§"),
            ex("§f(x) = 2x - 1§ dengan domain {0, 1, 2, 3}. Tentukan range-nya.",
                "§f(0) = - 1§, §f(1) = 1§, §f(2) = 3§, §f(3) = 5§.",
                answer = "{−1, 1, 3, 5}"),
        ),
        pitfalls = listOf(
            "Menganggap kodomain sama dengan range.",
            "Lupa bahwa isi akar boleh sama dengan nol (pakai ≥, bukan >).",
        ),
        points = listOf(
            "Fungsi: setiap x punya tepat satu pasangan.",
            "Domain: penyebut §\\neq 0§, isi akar §\\ge 0§.",
            "Puncak parabola di §x = - \\frac{b}{2a}§.",
        ),
    )

    private val komposisiInvers = Section(
        title = "Fungsi komposisi & fungsi invers",
        explain = listOf(
            "Fungsi komposisi menggabungkan dua fungsi berurutan. §(f \\circ g)(x) = f(g(x))§ artinya x dimasukkan ke g dulu, lalu hasilnya dimasukkan ke f. Urutannya dibaca dari kanan. Umumnya f∘g tidak sama dengan g∘f.",
            "Fungsi invers §f^{-1}§ membalik kerja f: kalau f mengubah 3 menjadi 1, maka §f^{-1}§ mengubah 1 kembali menjadi 3. Cara mencarinya: tulis §y = f(x)§, ubah persamaan sampai x sendirian di satu ruas, lalu ganti y dengan x.",
            "Untuk fungsi linear §f(x) = ax + b§, inversnya §f^{-1}(x) = \\frac{x-b}{a}§: operasinya dibalik (kurangi b, lalu bagi a). Untuk fungsi rasional §f(x) = \\frac{ax+b}{cx+d}§, ada rumus cepat §f^{-1}(x) = \\frac{-dx+b}{cx-a}§: tukar posisi a dan d sambil mengganti tandanya.",
            "Cek invers dengan satu angka: §f^{-1}(f(x))§ harus kembali ke x.",
        ),
        formulas = listOf(
            "§(f \\circ g)(x) = f(g(x)) \\qquad (g \\circ f)(x) = g(f(x))§",
            "§f(x) = ax + b \\;\\Rightarrow\\; f^{-1}(x) = \\dfrac{x - b}{a}§",
            "§f(x) = \\dfrac{ax + b}{cx + d} \\;\\Rightarrow\\; f^{-1}(x) = \\dfrac{-dx + b}{cx - a}§",
        ),
        examples = listOf(
            ex("§f(x) = 2x + 1§ dan §g(x) = x^{2}§. Tentukan (f∘g)(x), (g∘f)(x), dan (f∘g)(2).",
                "§(f \\circ g)(x) = f(x^{2}) = 2x^{2} + 1§.",
                "§(g \\circ f)(x) = (2x + 1)^{2} = 4x^{2} + 4x + 1§. Hasilnya berbeda dengan f∘g.",
                "§(f \\circ g)(2) = 2 \\times 4 + 1 = 9§.",
                answer = "§2x^{2} + 1§;  §4x^{2} + 4x + 1§;  9"),
            ex("Tentukan invers §f(x) = \\frac{3x-4}{5}§.",
                "§y = \\frac{3x-4}{5} \\to 5y = 3x - 4 \\to 3x = 5y + 4 \\to x = \\frac{5y+4}{3}§.",
                "Ganti y dengan x: §f^{-1}(x) = \\frac{5x+4}{3}§.",
                "Cek: §f(3) = \\frac{9-4}{5} = 1§, dan §f^{-1}(1) = \\frac{5+4}{3} = 3§.",
                answer = "§f^{-1}(x) = \\frac{5x+4}{3}§"),
            ex("Tentukan invers §f(x) = \\frac{2x+1}{x-3}§.",
                "§a = 2§, §b = 1§, §c = 1§, §d = - 3§.",
                "§f^{-1}(x) = \\frac{-dx+b}{cx-a} = \\frac{3x+1}{x-2}§.",
                "Cek: §f(4) = \\frac{9}{1} = 9§, dan §f^{-1}(9) = \\frac{28}{7} = 4§.",
                answer = "§f^{-1}(x) = \\frac{3x+1}{x-2}§"),
        ),
        pitfalls = listOf(
            "Mengerjakan f dulu pada (f∘g)(x). Yang benar g dulu.",
            "Menganggap §f^{-1}(x)§ sama dengan §\\frac{1}{f}(x)§.",
        ),
        points = listOf(
            "f∘g: masukkan ke g dulu, lalu ke f.",
            "Invers: §y = f(x)§, jadikan x sendirian, tukar x dan y.",
            "Cek dengan satu angka.",
        ),
    )

    private val deretAritmetika = Section(
        title = "Barisan & deret aritmetika",
        explain = listOf(
            "Barisan aritmetika adalah barisan yang selisih dua suku berurutannya selalu sama. Selisih ini disebut beda (b). Contoh: 3, 7, 11, 15, … dengan §b = 4§.",
            "Suku ke-n didapat dari suku pertama a ditambah beda sebanyak (n − 1) kali, karena dari suku ke-1 ke suku ke-n ada n − 1 langkah. Jadi §U_{n} = a + (n - 1)b§.",
            "Deret aritmetika adalah jumlah suku-sukunya. Rumusnya berasal dari trik Gauss: pasangkan suku pertama dengan suku terakhir, suku kedua dengan suku kedua dari akhir, dan seterusnya. Setiap pasangan jumlahnya sama, yaitu §a + U_{n}§, dan ada §\\frac{n}{2}§ pasangan. Jadi §S_{n} = \\frac{n}{2} \\times (a + U_{n})§.",
            "Bunga tunggal adalah contoh aritmetika dalam kehidupan sehari-hari: bunganya selalu dihitung dari modal awal, sehingga tabungan bertambah dengan jumlah yang sama setiap periode.",
        ),
        formulas = listOf(
            "§U_n = a + (n - 1)b \\qquad b = U_n - U_{n-1}§",
            "§S_n = \\dfrac{n}{2}(a + U_n) = \\dfrac{n}{2}\\big(2a + (n - 1)b\\big)§",
            "Bunga tunggal: §M_n = M(1 + n\\,i)§",
        ),
        examples = listOf(
            ex("Barisan 3, 7, 11, …. Tentukan §U_{20}§ dan §S_{20}§.",
                "§a = 3§, §b = 4§.",
                "§U_{20} = 3 + 19 \\times 4 = 79§.",
                "§S_{20} = \\frac{20}{2} \\times (3 + 79) = 10 \\times 82 = 820§.",
                answer = "§U_{20} = 79§;  §S_{20} = 820§"),
            ex("§U_{3} = 10§ dan §U_{7} = 22§. Tentukan §U_{10}§.",
                "§U_{7} - U_{3} = 4b = 12§, jadi §b = 3§.",
                "§U_{3} = a + 2b \\to 10 = a + 6 \\to a = 4§.",
                "§U_{10} = 4 + 9 \\times 3 = 31§.",
                answer = "31"),
            ex("Nopal menabung Rp10.000 di minggu pertama dan menambah Rp2.000 lebih banyak setiap minggu. Berapa total tabungan setelah 10 minggu?",
                "§a = 10.000§, §b = 2.000§, §n = 10§.",
                "§U_{10} = 10.000 + 9 \\times 2.000 = 28.000§.",
                "§S_{10} = \\frac{10}{2} \\times (10.000 + 28.000) = 5 \\times 38.000 = 190.000§.",
                answer = "Rp190.000"),
            ex("Modal Rp2.000.000 dengan bunga tunggal 6% per tahun. Berapa besar tabungan setelah 3 tahun?",
                "Bunga per tahun = §2.000.000 \\times 0{,}06 = 120.000§.",
                "Bunga 3 tahun = §3 \\times 120.000 = 360.000§.",
                "Tabungan = §2.000.000 + 360.000 = 2.360.000§.",
                answer = "Rp2.360.000"),
        ),
        pitfalls = listOf("Memakai n, bukan (n − 1), pada rumus §U_{n}§."),
        points = listOf(
            "§U_{n} = a + (n - 1)b§.",
            "§S_{n} = \\frac{n}{2}(a + U_{n})§.",
            "Bunga tunggal bertambah sama besar tiap periode.",
        ),
    )

    private val deretGeometri = Section(
        title = "Barisan & deret geometri, pertumbuhan & bunga majemuk",
        explain = listOf(
            "Barisan geometri adalah barisan yang perbandingan dua suku berurutannya selalu sama. Perbandingan ini disebut rasio (r). Contoh: 2, 6, 18, 54, … dengan §r = 3§.",
            "Suku ke-n adalah suku pertama dikali rasio sebanyak (n − 1) kali: §U_{n} = a \\cdot r^{n-1}§. Jumlah n suku pertama §S_{n} = \\frac{a(r^{n}-1)}{r-1}§. Kalau §- 1 < r < 1§, suku-sukunya makin kecil mendekati nol, sehingga jumlah tak hingganya punya nilai: §S \\infty = \\frac{a}{1-r}§.",
            "Pertumbuhan dan peluruhan adalah barisan geometri. Kalau sesuatu naik i% tiap periode, setiap periode nilainya dikali (1 + i). Setelah n periode: §M(1 + i)^{n}§. Kalau turun i% tiap periode (penyusutan harga, peluruhan zat), faktornya (1 − i).",
            "Bunga majemuk berbeda dengan bunga tunggal: bunga periode berikutnya dihitung dari modal ditambah bunga sebelumnya (\"bunga berbunga\"). Karena itu rumusnya geometri: §M_{n} = M(1 + i)^{n}§.",
        ),
        formulas = listOf(
            "§U_n = a\\,r^{n-1} \\qquad r = \\dfrac{U_n}{U_{n-1}}§",
            "§S_n = \\dfrac{a(r^n - 1)}{r - 1} \\qquad S_\\infty = \\dfrac{a}{1 - r}§, syarat §|r| < 1§",
            "Tumbuh: §M_n = M(1 + i)^n§ · Susut: §M_n = M(1 - i)^n§",
        ),
        examples = listOf(
            ex("Barisan 2, 6, 18, …. Tentukan §U_{6}§ dan §S_{6}§.",
                "§a = 2§, §r = 3§.",
                "§U_{6} = 2 \\times 3^{5} = 2 \\times 243 = 486§.",
                "§S_{6} = 2\\frac{3^{6}-1}{3-1} = 2 \\times \\frac{728}{2} = 728§.",
                answer = "§U_{6} = 486§;  §S_{6} = 728§"),
            ex("Modal Rp1.000.000 disimpan dengan bunga majemuk 10% per tahun. Berapa nilainya setelah 2 tahun?",
                "§M_{2} = 1.000.000 \\times 1{,}1^{2} = 1.000.000 \\times 1{,}21§.",
                "= 1.210.000. (Bunga tunggal hanya menghasilkan 1.200.000.)",
                answer = "Rp1.210.000"),
            ex("Bakteri membelah menjadi dua setiap 20 menit. Awalnya ada 100 bakteri. Berapa banyaknya setelah 2 jam?",
                "2 jam = 120 menit = 6 kali membelah.",
                "§100 \\times 2^{6} = 100 \\times 64 = 6.400§.",
                answer = "6.400 bakteri"),
            ex("Bola dijatuhkan dari ketinggian 10 m dan setiap memantul mencapai §\\tfrac{3}{4}§ tinggi sebelumnya. Berapa panjang lintasan sampai berhenti?",
                "Lintasan turun pertama 10 m. Setelah itu setiap pantulan naik lalu turun dengan tinggi yang sama.",
                "Pantulan naik membentuk deret tak hingga dengan §a = 7{,}5§ dan §r = \\tfrac{3}{4}§: §S \\infty = \\frac{7{,}5}{1-\\tfrac{3}{4}} = 30\\,\\mathrm{m}§.",
                "Total = §10 + 2 \\times 30 = 70\\,\\mathrm{m}§.",
                answer = "70 m"),
        ),
        pitfalls = listOf(
            "Memakai rumus bunga tunggal untuk soal bunga majemuk.",
            "Memakai S∞ padahal |§r | \\ge 1§.",
        ),
        points = listOf(
            "§U_{n} = a \\cdot r^{n-1}§; §S \\infty = \\frac{a}{1-r}§ kalau |§r | < 1§.",
            "Pertumbuhan ×§(1 + i)^{n}§, penyusutan ×§(1 - i)^{n}§.",
            "Bunga majemuk = geometri, bunga tunggal = aritmetika.",
        ),
    )

    private val sudutGaris = Section(
        title = "Hubungan sudut, garis & bidang",
        explain = listOf(
            "Dua sudut berpenyiku jumlahnya 90°, dan dua sudut berpelurus jumlahnya 180°. Dua garis yang berpotongan membentuk sudut-sudut bertolak belakang yang besarnya sama.",
            "Kalau dua garis sejajar dipotong garis ketiga (transversal), muncul pasangan sudut khusus. Sudut sehadap besarnya sama. Sudut dalam berseberangan dan luar berseberangan besarnya sama. Sudut dalam sepihak jumlahnya 180°.",
            "Jumlah sudut dalam segitiga 180°. Segi-n bisa dibagi menjadi (n − 2) segitiga dari satu titik sudut, sehingga jumlah sudut dalamnya §(n - 2) \\times 180^\\circ§.",
            "Dalam ruang (misalnya kubus), dua garis bisa sejajar, berpotongan, atau BERSILANGAN: tidak sejajar dan tidak pernah bertemu karena tidak sebidang. Dua bidang bisa sejajar atau berpotongan pada sebuah garis. Garis bisa terletak pada bidang, sejajar bidang, atau menembus bidang.",
        ),
        formulas = listOf(
            "Berpenyiku: §\\alpha + \\beta = 90^\\circ§ · Berpelurus: §\\alpha + \\beta = 180^\\circ§",
            "Sehadap & berseberangan: sama · Dalam sepihak: jumlah §180^\\circ§",
            "Jumlah sudut segi-n: §(n - 2) \\times 180^\\circ§",
        ),
        examples = listOf(
            ex("Dua garis sejajar dipotong transversal. Sepasang sudut dalam sepihak besarnya (2x + 10)° dan (3x − 5)°. Tentukan x.",
                "Sudut dalam sepihak jumlahnya 180°.",
                "§2x + 10 + 3x - 5 = 180 \\to 5x + 5 = 180 \\to 5x = 175§.",
                "§x = 35§.",
                answer = "§x = 35§"),
            ex("Berapa besar setiap sudut segi-6 beraturan?",
                "Jumlah sudut = §(6 - 2) \\times 180^\\circ = 720^\\circ§.",
                "Beraturan berarti semua sudut sama: §720^\\circ \\div 6 = 120^\\circ§.",
                answer = "120°"),
            ex("Pada kubus ABCD.EFGH, bagaimana kedudukan AB dengan HG, dan AB dengan CG?",
                "AB dan HG sama-sama sejajar DC, jadi AB sejajar HG.",
                "AB terletak di alas, CG tegak di titik C. Keduanya tidak sejajar dan tidak bertemu.",
                answer = "AB ∥ HG;  AB bersilangan dengan CG"),
        ),
        pitfalls = listOf("Menganggap dua garis yang tidak berpotongan pasti sejajar. Di ruang, bisa jadi bersilangan."),
        points = listOf(
            "Sehadap sama; dalam sepihak jumlahnya 180°.",
            "Jumlah sudut segi§- n = (n - 2) \\times 180^\\circ§.",
            "Di ruang ada garis bersilangan.",
        ),
    )

    private val bangunDatar = Section(
        title = "Keliling & luas bangun datar",
        explain = listOf(
            "Keliling adalah panjang seluruh tepi bangun, sedangkan luas adalah besar daerah di dalamnya. Satuan luas selalu berpangkat dua (§\\mathrm{cm}^{2}§, §m^{2}§).",
            "Banyak rumus luas berasal dari persegi panjang. Jajargenjang bisa dipotong lalu digeser menjadi persegi panjang, jadi luasnya alas × tinggi. Segitiga adalah setengah jajargenjang, jadi §\\tfrac{1}{2}§ × alas × tinggi. Trapesium bisa digandakan menjadi jajargenjang dengan alas (a + b), jadi luasnya §\\tfrac{1}{2} \\times (a + b) \\times t§. Belah ketupat dan layang-layang adalah setengah persegi panjang yang dibentuk diagonalnya: §\\tfrac{1}{2} \\times d_{1} \\times d_{2}§.",
            "Juring adalah \"potongan pizza\" dari lingkaran. Kalau sudut pusatnya §\\theta§, juring itu adalah §\\frac{\\theta}{360}§ bagian dari lingkaran penuh. Jadi luas juring = §\\frac{\\theta}{360} \\times \\pi r^{2}§ dan panjang busur = §\\frac{\\theta}{360} \\times 2\\pi r§.",
            "Untuk bangun gabungan, pecah menjadi bangun-bangun sederhana, hitung masing-masing, lalu jumlahkan atau kurangkan.",
        ),
        formulas = listOf(
            "Persegi panjang §L = p\\,l§ · Jajargenjang §L = a\\,t§ · Segitiga §L = \\tfrac{1}{2} a\\,t§",
            "Trapesium §L = \\tfrac{1}{2}(a + b)\\,t§ · Belah ketupat/layang-layang §L = \\tfrac{1}{2} d_1 d_2§",
            "Juring §L = \\dfrac{\\theta}{360^\\circ}\\,\\pi r^2§ · Busur §= \\dfrac{\\theta}{360^\\circ}\\,2\\pi r§",
        ),
        examples = listOf(
            ex("Trapesium dengan sisi sejajar 10 cm dan 16 cm serta tinggi 6 cm. Berapa luasnya?",
                "§L = \\tfrac{1}{2} \\times (10 + 16) \\times 6§.",
                "= §\\tfrac{1}{2} \\times 26 \\times 6 = 78\\,\\mathrm{cm}^{2}§.",
                answer = "§78\\,\\mathrm{cm}^{2}§"),
            ex("Juring bersudut 90° dengan jari-jari 14 cm. Tentukan luas juring dan panjang busurnya §(\\pi = \\frac{22}{7})§.",
                "§\\frac{90}{360} = \\tfrac{1}{4}§.",
                "Luas = §\\tfrac{1}{4} \\times \\frac{22}{7} \\times 14 \\times 14 = \\tfrac{1}{4} \\times 616 = 154\\,\\mathrm{cm}^{2}§.",
                "Busur = §\\tfrac{1}{4} \\times 2 \\times \\frac{22}{7} \\times 14 = \\tfrac{1}{4} \\times 88 = 22§ cm.",
                answer = "§154\\,\\mathrm{cm}^{2}§ dan 22 cm"),
            ex("Persegi bersisi 14 cm ditempeli setengah lingkaran pada salah satu sisinya. Berapa luas seluruhnya?",
                "Luas persegi = §14 \\times 14 = 196\\,\\mathrm{cm}^{2}§.",
                "Setengah lingkaran berdiameter §14 \\to r = 7§: §\\tfrac{1}{2} \\times \\frac{22}{7} \\times 7 \\times 7 = 77\\,\\mathrm{cm}^{2}§.",
                "Total = §196 + 77 = 273\\,\\mathrm{cm}^{2}§.",
                answer = "§273\\,\\mathrm{cm}^{2}§"),
        ),
        pitfalls = listOf("Memakai sisi miring sebagai tinggi. Tinggi selalu tegak lurus alas."),
        points = listOf(
            "Segitiga §\\tfrac{1}{2}§at, trapesium §\\tfrac{1}{2}(a + b)t§, belah ketupat §\\tfrac{1}{2}d_{1}d_{2}§.",
            "Juring dan busur: kalikan §\\frac{\\theta}{360}§.",
            "Bangun gabungan: pecah lalu jumlahkan.",
        ),
    )

    private val luasPermukaan = Section(
        title = "Luas permukaan bangun ruang",
        explain = listOf(
            "Luas permukaan adalah jumlah luas semua sisi bangun ruang. Luas itu dihitung dari jaring-jaringnya: bangun dibuka menjadi bangun datar, lalu luas tiap bagian dijumlah.",
            "Kubus punya 6 sisi persegi, jadi §6s^{2}§. Balok punya 3 pasang sisi persegi panjang, jadi 2(pl + pt + lt). Prisma punya 2 alas ditambah selimut yang berupa persegi panjang dengan panjang = keliling alas.",
            "Tabung terdiri dari 2 lingkaran dan selimut. Kalau selimut dibuka, bentuknya persegi panjang dengan panjang §2\\pi r§ (keliling alas) dan lebar t. Jadi §L = 2\\pi r^{2} + 2\\pi rt = 2\\pi r(r + t)§. Kerucut terdiri dari alas lingkaran dan selimut §\\pi rs§, dengan s = garis pelukis yang dicari dari Pythagoras: §s^{2} = r^{2} + t^{2}§. Bola §L = 4\\pi r^{2}§.",
        ),
        formulas = listOf(
            "Kubus §6s^2§ · Balok §2(pl + pt + lt)§",
            "Prisma: §2 L_{\\text{alas}} + K_{\\text{alas}} \\times t§",
            "Tabung §2\\pi r(r + t)§ · Kerucut §\\pi r(r + s),\\; s^2 = r^2 + t^2§ · Bola §4\\pi r^2§",
        ),
        examples = listOf(
            ex("Tabung berjari-jari 7 cm dan tinggi 10 cm. Berapa luas permukaannya? §(\\pi = \\frac{22}{7})§",
                "§L = 2\\pi r(r + t) = 2 \\times \\frac{22}{7} \\times 7 \\times (7 + 10)§.",
                "= §44 \\times 17 = 748\\,\\mathrm{cm}^{2}§.",
                answer = "§748\\,\\mathrm{cm}^{2}§"),
            ex("Kerucut berjari-jari 6 cm dan tinggi 8 cm. Berapa luas permukaannya? §(\\pi = 3{,}14)§",
                "§s = \\sqrt{6^{2}+8^{2}} = \\sqrt{100} = 10§ cm.",
                "§L = \\pi r(r + s) = 3{,}14 \\times 6 \\times 16§.",
                "= §301{,}44\\,\\mathrm{cm}^{2}§.",
                answer = "§301{,}44\\,\\mathrm{cm}^{2}§"),
        ),
        pitfalls = listOf("Memakai tinggi kerucut sebagai garis pelukis."),
        points = listOf(
            "Luas permukaan = jumlah luas jaring-jaring.",
            "Tabung §2\\pi r(r + t)§; kerucut §\\pi r(r + s)§.",
        ),
    )

    private val jarakRuang = Section(
        title = "Jarak titik, garis & bidang",
        explain = listOf(
            "Jarak antara dua objek geometri selalu diukur lewat ruas garis TERPENDEK yang menghubungkannya. Untuk titik ke garis atau titik ke bidang, ruas terpendek itu tegak lurus terhadap garis atau bidangnya.",
            "Di kubus bersisi s, ada dua panjang yang sering muncul. Diagonal sisi (misalnya AC) = §s\\sqrt{2}§, karena merupakan sisi miring segitiga siku-siku dengan kaki s dan s. Diagonal ruang (misalnya AG) = §s\\sqrt{3}§, karena merupakan sisi miring segitiga dengan kaki AC = §s\\sqrt{2}§ dan CG = s.",
            "Jarak titik ke garis: tarik garis tegak lurus dari titik ke garis, lalu hitung panjangnya dengan Pythagoras atau luas segitiga. Jarak titik ke bidang: cari proyeksi titik itu pada bidang. Jarak dua bidang sejajar atau dua garis sejajar sama dengan jarak sembarang titik di satu objek ke objek lainnya.",
        ),
        formulas = listOf(
            "Diagonal sisi §= s\\sqrt{2}§ · Diagonal ruang §= s\\sqrt{3}§",
            "Jarak titik ke garis/bidang = panjang ruas tegak lurus",
            "Kubus: jarak A ke bidang BDE §= \\tfrac{1}{3} AG = \\dfrac{s\\sqrt{3}}{3}§",
        ),
        examples = listOf(
            ex("Kubus ABCD.EFGH bersisi 6 cm. Berapa jarak titik A ke titik G?",
                "AG adalah diagonal ruang.",
                "AG = §6\\sqrt{3}§ cm.",
                answer = "§6\\sqrt{3}§ cm"),
            ex("Pada kubus yang sama, berapa jarak titik C ke garis BD?",
                "C dan BD sama-sama ada di alas ABCD. Diagonal AC tegak lurus BD dan memotongnya di titik tengah O.",
                "Jarak = CO = §\\tfrac{1}{2}§ × AC = §\\tfrac{1}{2} \\times 6\\sqrt{2} = 3\\sqrt{2}§ cm.",
                answer = "§3\\sqrt{2}§ cm"),
            ex("Berapa jarak titik E ke bidang ABCD, dan jarak bidang ABCD ke EFGH?",
                "EA tegak lurus bidang alas, jadi jaraknya EA = 6 cm.",
                "Bidang ABCD dan EFGH sejajar, jaraknya sama dengan rusuk tegak: 6 cm.",
                answer = "6 cm dan 6 cm"),
        ),
        pitfalls = listOf("Mengukur jarak titik ke garis lewat ruas yang tidak tegak lurus."),
        points = listOf(
            "Jarak = ruas terpendek, tegak lurus.",
            "Diagonal sisi §s\\sqrt{2}§, diagonal ruang §s\\sqrt{3}§.",
        ),
    )

    private val rotasiDilatasi = Section(
        title = "Rotasi, dilatasi & komposisi transformasi",
        explain = listOf(
            "Rotasi memutar titik terhadap pusat tertentu. Sudut positif berarti berlawanan arah jarum jam. Terhadap pusat O(0, 0): rotasi 90° mengubah (x, y) menjadi (−y, x); rotasi 180° menjadi (−x, −y); rotasi −90° (searah jarum jam) menjadi (y, −x). Cara mengingat: gambar titik (1, 0), putar 90° berlawanan jarum jam, hasilnya (0, 1).",
            "Dilatasi memperbesar atau memperkecil jarak titik dari pusat dengan faktor skala k. Pusat O: (x, y) → (kx, ky). Pusat (a, b): geser dulu sehingga pusatnya di O, kalikan k, lalu geser kembali, sehingga hasilnya (a + k(x − a), b + k(y − b)). Kalau k negatif, bayangan berada di seberang pusat.",
            "Komposisi transformasi dikerjakan berurutan sesuai urutan di soal: hasil transformasi pertama menjadi titik awal transformasi kedua.",
        ),
        formulas = listOf(
            "Rotasi pusat O: §90^\\circ \\to (-y, x) \\qquad 180^\\circ \\to (-x, -y) \\qquad -90^\\circ \\to (y, -x)§",
            "Dilatasi §[O, k]§: §(x, y) \\to (kx, ky)§",
            "Dilatasi pusat §(a, b)§: §\\big(a + k(x - a),\\; b + k(y - b)\\big)§",
        ),
        examples = listOf(
            ex("Titik P(3, −2) dirotasi 90° berlawanan arah jarum jam terhadap O. Tentukan bayangannya.",
                "Rotasi 90°: (x, y) → (−y, x).",
                "(−(−2), 3) = (2, 3).",
                answer = "(2, 3)"),
            ex("Titik B(3, 1) ditranslasi (1, 2), lalu dicerminkan terhadap sumbu-y. Bayangan akhirnya?",
                "Translasi: (3 + 1, 1 + 2) = (4, 3).",
                "Cermin sumbu-y: x berganti tanda → (−4, 3).",
                answer = "(−4, 3)"),
            ex("Titik (3, 4) didilatasi dengan pusat (1, 1) dan faktor 2. Bayangannya?",
                "§x' = 1 + 2(3 - 1) = 5§.",
                "§y' = 1 + 2(4 - 1) = 7§.",
                answer = "(5, 7)"),
        ),
        pitfalls = listOf("Tertukar arah rotasi. Positif = berlawanan arah jarum jam."),
        points = listOf(
            "Rotasi 90°: (−y, x).",
            "Dilatasi pusat O: kalikan k.",
            "Komposisi: kerjakan berurutan.",
        ),
    )

    private val trigLengkap = Section(
        title = "Enam perbandingan trigonometri & sudut berelasi",
        explain = listOf(
            "Selain sin, cos, dan tan, ada tiga perbandingan kebalikannya: kosekan §\\csc = 1 / \\sin§ = miring/depan, sekan §\\sec = 1 / \\cos§ = miring/samping, dan kotangen §\\cot = 1 / \\tan§ = samping/depan. Soal bisa memakai keenamnya.",
            "Kalau diketahui satu perbandingan, gambarlah segitiga siku-sikunya, cari sisi ketiga dengan Pythagoras, lalu semua perbandingan lain bisa dibaca.",
            "Untuk sudut lebih dari 90°, tanda nilainya bergantung kuadran. Kuadran I (0°–90°): semua positif. Kuadran II (90°–180°): hanya sin dan csc positif. Kuadran III (180°–270°): hanya tan dan cot positif. Kuadran IV (270°–360°): hanya cos dan sec positif. Jembatan keledai: \"semua–sindikat–tangan–kosong\".",
            "Sudut berelasi mengubah sudut besar ke sudut lancip: §\\sin (180^\\circ - \\alpha) = \\sin \\alpha§, §\\cos (180^\\circ - \\alpha) = - \\cos \\alpha§, §\\sin (180^\\circ + \\alpha) = - \\sin \\alpha§, §\\cos (360^\\circ - \\alpha) = \\cos \\alpha§. Nilainya sama dengan sudut lancipnya, tandanya mengikuti kuadran.",
        ),
        formulas = listOf(
            "§\\csc x = \\dfrac{1}{\\sin x} \\qquad \\sec x = \\dfrac{1}{\\cos x} \\qquad \\cot x = \\dfrac{\\cos x}{\\sin x}§",
            "Kuadran positif: I semua, II sin, III tan, IV cos",
            "§120^\\circ = 180^\\circ - 60^\\circ \\qquad 210^\\circ = 180^\\circ + 30^\\circ \\qquad 315^\\circ = 360^\\circ - 45^\\circ§",
        ),
        examples = listOf(
            ex("§\\sin A = \\frac{3}{5}§ dengan A lancip. Tentukan cos A, tan A, sec A, csc A, dan cot A.",
                "Depan 3, miring 5 → samping = §\\sqrt{25-9} = 4§.",
                "§\\cos A = \\frac{4}{5}§, §\\tan A = \\frac{3}{4}§.",
                "§\\sec A = \\frac{5}{4}§, §\\csc A = \\frac{5}{3}§, §\\cot A = \\frac{4}{3}§.",
                answer = "§\\frac{4}{5}§, §\\frac{3}{4}§, §\\frac{5}{4}§, §\\frac{5}{3}§, §\\frac{4}{3}§"),
            ex("Hitung cos 120°, sin 210°, dan tan 315°.",
                "§\\cos 120^\\circ = \\cos (180^\\circ - 60^\\circ) = - \\cos 60^\\circ = - \\tfrac{1}{2}§ (kuadran II, cos negatif).",
                "§\\sin 210^\\circ = \\sin (180^\\circ + 30^\\circ) = - \\sin 30^\\circ = - \\tfrac{1}{2}§ (kuadran III, sin negatif).",
                "§\\tan 315^\\circ = \\tan (360^\\circ - 45^\\circ) = - \\tan 45^\\circ = - 1§ (kuadran IV, tan negatif).",
                answer = "§- \\tfrac{1}{2}§, §- \\tfrac{1}{2}§, −1"),
        ),
        pitfalls = listOf("Lupa memberi tanda negatif sesuai kuadran."),
        points = listOf(
            "csc, sec, cot adalah kebalikan sin, cos, tan.",
            "Semua–sindikat–tangan–kosong.",
            "Ubah ke sudut lancip, tanda ikut kuadran.",
        ),
    )

    private val diagramData = Section(
        title = "Membaca diagram & tabel",
        explain = listOf(
            "Soal data sering dimulai dengan diagram. Diagram batang membandingkan banyak kategori. Diagram garis menunjukkan perubahan dari waktu ke waktu (naik atau turun). Diagram lingkaran menunjukkan bagian dari keseluruhan.",
            "Pada diagram lingkaran, satu lingkaran penuh = §360^\\circ = 100\\%§ = seluruh data. Jadi banyak data suatu kategori = §\\frac{\\text{sudut}}{360^\\circ} \\times \\text{total}§, atau §\\frac{\\text{persen}}{100} \\times \\text{total}§.",
            "Sebelum menghitung, baca dulu judul, satuan, dan skala sumbunya. Banyak jawaban salah muncul karena skala sumbu tidak mulai dari nol atau satuannya ribuan.",
            "Persentase perubahan = (nilai baru − nilai lama)/nilai lama × 100%. Pembaginya selalu nilai LAMA.",
        ),
        formulas = listOf(
            "Diagram lingkaran: §n = \\dfrac{\\text{sudut}}{360^\\circ} \\times \\text{total} = \\dfrac{\\text{persen}}{100} \\times \\text{total}§",
            "§\\text{Perubahan} = \\dfrac{\\text{baru} - \\text{lama}}{\\text{lama}} \\times 100\\%§",
        ),
        examples = listOf(
            ex("Dalam diagram lingkaran hobi 40 siswa, sektor sepak bola bersudut 108°. Berapa siswa yang hobi sepak bola?",
                "§\\frac{108}{360} = 0{,}3§.",
                "§0{,}3 \\times 40 = 12§.",
                answer = "12 siswa"),
            ex("Grafik garis: penjualan Januari 120 unit, Februari 150 unit. Berapa persen kenaikannya?",
                "Kenaikan = §150 - 120 = 30§.",
                "§\\frac{30}{120} \\times 100\\% = 25\\%§.",
                answer = "25%"),
        ),
        pitfalls = listOf("Membagi dengan nilai baru saat menghitung persen perubahan."),
        points = listOf(
            "Lingkaran: §360^\\circ = 100\\%§ = total.",
            "Perubahan persen dibagi nilai lama.",
            "Cek judul, satuan, dan skala sumbu.",
        ),
    )

    private val dataKelompok = Section(
        title = "Ukuran pemusatan data kelompok",
        explain = listOf(
            "Data kelompok disajikan dalam kelas-kelas interval, misalnya 41–50, 51–60, dan seterusnya, bersama frekuensinya. Karena nilai asli setiap data tidak diketahui, setiap kelas diwakili titik tengahnya.",
            "Mean data kelompok = §\\Sigma§(f × titik tengah)/§\\Sigma f§. Ini sama dengan rata-rata biasa, hanya saja setiap titik tengah dihitung sebanyak frekuensinya.",
            "Median adalah data §ke - (\\frac{n}{2})§. Cari dulu kelas yang memuatnya dengan frekuensi kumulatif. Di dalam kelas itu, posisi median diperkirakan secara sebanding: Me = Tb + §(\\frac{\\frac{n}{2}-F}{f}) \\times p§. Tb adalah tepi bawah kelas (batas bawah − 0,5), F frekuensi kumulatif sebelum kelas median, f frekuensi kelas median, dan p panjang kelas.",
            "Modus ada di kelas dengan frekuensi terbesar. Posisinya condong ke kelas tetangga yang frekuensinya lebih besar: Mo = Tb + §(\\frac{d_{1}}{d_{1}+d_{2}}) \\times p§, dengan §d_{1}§ = selisih dengan kelas sebelumnya dan §d_{2}§ = selisih dengan kelas sesudahnya.",
        ),
        formulas = listOf(
            "§\\bar{x} = \\dfrac{\\Sigma f_i x_i}{\\Sigma f_i}§ (§x_i§ = titik tengah)",
            "§Me = T_b + \\left(\\dfrac{\\frac{n}{2} - F}{f}\\right) p§",
            "§Mo = T_b + \\left(\\dfrac{d_1}{d_1 + d_2}\\right) p§",
            "§T_b = \\text{batas bawah} - 0{,}5§",
        ),
        examples = listOf(
            ex("Data nilai: 41–50 (3), 51–60 (7), 61–70 (10), 71–80 (6), 81–90 (4). Tentukan mean, median, dan modus.",
                "§n = 30§. Titik tengah: 45,5; 55,5; 65,5; 75,5; 85,5. §\\Sigma fx = 136{,}5 + 388{,}5 + 655 + 453 + 342 = 1.975§. Mean = §\\frac{1.975}{30} \\approx 65{,}83§.",
                "Median: data ke-15. Kumulatif 3, 10, 20 → kelas 61–70. Tb = 60,5; §F = 10§; §f = 10§; §p = 10§. Me = §60{,}5 + \\frac{15-10}{10} \\times 10 = 65{,}5§.",
                "Modus: kelas §61 - 70(f = 10)§. §d_{1} = 10 - 7 = 3§, §d_{2} = 10 - 6 = 4§. Mo = §60{,}5 + \\frac{3}{7} \\times 10 \\approx 64{,}79§.",
                answer = "Mean §\\approx 65{,}83§; median 65,5; modus §\\approx 64{,}79§"),
        ),
        pitfalls = listOf("Memakai batas bawah (61), bukan tepi bawah (60,5)."),
        points = listOf(
            "Setiap kelas diwakili titik tengahnya.",
            "Median: cari kelas dengan frekuensi kumulatif.",
            "Pakai tepi bawah = batas bawah − 0,5.",
        ),
    )

    private val penyebaran = Section(
        title = "Ukuran letak & penyebaran data",
        explain = listOf(
            "Kuartil membagi data terurut menjadi empat bagian sama banyak. §Q_{2}§ adalah median, §Q_{1}§ adalah median setengah bagian bawah, dan §Q_{3}§ adalah median setengah bagian atas. Jangkauan antarkuartil §H = Q_{3} - Q_{1}§ menunjukkan sebaran 50% data di tengah.",
            "Ukuran penyebaran menjawab pertanyaan \"seberapa jauh data dari rata-ratanya?\". Simpangan rata-rata adalah rata-rata dari jarak setiap data ke mean (memakai nilai mutlak supaya tidak saling menghapus).",
            "Ragam (varians) adalah rata-rata kuadrat simpangan. Simpangan baku adalah akar dari ragam, sehingga satuannya kembali sama dengan data. Makin kecil simpangan baku, makin seragam datanya.",
        ),
        formulas = listOf(
            "§H = Q_3 - Q_1§",
            "Simpangan rata-rata: §SR = \\dfrac{\\Sigma |x_i - \\bar{x}|}{n}§",
            "Ragam: §s^2 = \\dfrac{\\Sigma (x_i - \\bar{x})^2}{n}§ · Simpangan baku: §s = \\sqrt{s^2}§",
        ),
        examples = listOf(
            ex("Data: 2, 4, 4, 4, 5, 5, 7, 9. Tentukan §Q_{1}§, §Q_{2}§, §Q_{3}§, simpangan rata-rata, ragam, dan simpangan baku.",
                "§Q_{2} = \\frac{4+5}{2} = 4{,}5§. §Q_{1}§ = median dari 2, 4, 4, §4 = 4§. §Q_{3}§ = median dari 5, 5, 7, §9 = 6§.",
                "Mean = §\\frac{40}{8} = 5§. Simpangan: −3, −1, −1, −1, 0, 0, 2, 4.",
                "SR = §\\frac{3+1+1+1+0+0+2+4}{8} = \\frac{12}{8} = 1{,}5§.",
                "Ragam = §\\frac{9+1+1+1+0+0+4+16}{8} = \\frac{32}{8} = 4§, jadi simpangan baku = 2.",
                answer = "§Q_{1} = 4§, §Q_{2} = 4{,}5§, §Q_{3} = 6§; SR = 1,5; §s^{2} = 4§; §s = 2§"),
        ),
        pitfalls = listOf("Lupa mengurutkan data sebelum mencari kuartil."),
        points = listOf(
            "Kuartil: urutkan, lalu bagi empat.",
            "Ragam = rata-rata kuadrat simpangan.",
            "Simpangan baku = √ragam.",
        ),
    )

    private val peluangMajemuk = Section(
        title = "Peluang kejadian majemuk",
        explain = listOf(
            "Kejadian majemuk melibatkan dua kejadian atau lebih. Untuk \"A atau B\", kita menjumlahkan peluangnya, tetapi bagian yang dihitung dua kali (A dan B sekaligus) harus dikurangi: P(A ∪ B) = P(A) + P(B) − P(A ∩ B).",
            "Dua kejadian saling lepas kalau tidak bisa terjadi bersamaan, misalnya muncul angka 2 dan angka 5 pada satu lemparan dadu. Maka P(A ∩ B) = 0, sehingga peluangnya tinggal dijumlah.",
            "Dua kejadian saling bebas kalau hasil yang satu tidak memengaruhi yang lain, misalnya melempar dadu dan koin. Untuk \"A dan B\", peluangnya dikalikan: P(A ∩ B) = §P(A) \\times P(B)§.",
            "Pengambilan tanpa pengembalian membuat isi kantong berubah, sehingga peluang pengambilan kedua dihitung dari sisa benda. Mengambil 2 sekaligus juga bisa dihitung dengan kombinasi.",
        ),
        formulas = listOf(
            "§P(A \\cup B) = P(A) + P(B) - P(A \\cap B)§",
            "Saling lepas: §P(A \\cup B) = P(A) + P(B)§",
            "Saling bebas: §P(A \\cap B) = P(A) \\times P(B)§",
        ),
        examples = listOf(
            ex("Satu kartu diambil dari 52 kartu bridge. Berapa peluang terambil kartu As atau kartu hati?",
                "§P(As) = \\frac{4}{52}§, P(hati) = §\\frac{13}{52}§.",
                "As hati dihitung dua kali, P(As ∩ hati) = §\\frac{1}{52}§.",
                "§P = \\frac{4}{52} + \\frac{13}{52} - \\frac{1}{52} = \\frac{16}{52} = \\frac{4}{13}§.",
                answer = "§\\frac{4}{13}§"),
            ex("Sebuah dadu dan sebuah koin dilempar. Berapa peluang muncul bilangan genap dan sisi angka?",
                "P(genap) = §\\frac{3}{6} = \\tfrac{1}{2}§ dan P(angka) = §\\tfrac{1}{2}§.",
                "Saling bebas, jadi dikali: §\\tfrac{1}{2} \\times \\tfrac{1}{2} = \\tfrac{1}{4}§.",
                answer = "§\\frac{1}{4}§"),
            ex("Kantong berisi 5 bola merah dan 3 bola biru. Diambil 2 bola satu per satu tanpa pengembalian. Berapa peluang keduanya merah?",
                "Pengambilan pertama: §\\frac{5}{8}§.",
                "Sisa 4 merah dari 7 bola: §\\frac{4}{7}§.",
                "§\\frac{5}{8} \\times \\frac{4}{7} = \\frac{20}{56} = \\frac{5}{14}§. (Sama dengan §\\frac{C(5{,}2)}{C}(8{,}2) = \\frac{10}{28}§.)",
                answer = "§\\frac{5}{14}§"),
        ),
        pitfalls = listOf("Menjumlahkan peluang \"atau\" tanpa mengurangi irisannya."),
        points = listOf(
            "Atau: jumlah dikurangi irisan.",
            "Dan (saling bebas): kalikan.",
            "Tanpa pengembalian: peluang kedua dari sisa.",
        ),
    )

    // ============================== B. INDONESIA ==============================

    private val kerangkaTeks = Section(
        title = "Kerangka & bagan teks",
        explain = listOf(
            "Kamu perlu mampu menyusun kerangka atau bagan dari bagian-bagian penting teks. Kerangka adalah daftar ide pokok setiap paragraf menurut urutannya, jadi kerangka menunjukkan \"peta\" sebuah teks.",
            "Cara menyusun kerangka: (1) temukan ide pokok setiap paragraf, (2) tulis dalam bentuk frasa singkat, (3) urutkan sesuai teks. Pilihan jawaban yang benar memuat SEMUA paragraf dengan urutan yang sama.",
            "Bagan dipakai sesuai pola teks. Teks sebab-akibat memakai bagan panah (penyebab → akibat). Teks proses memakai bagan urutan langkah. Teks klasifikasi memakai bagan pohon (kelompok besar bercabang menjadi jenis-jenisnya).",
        ),
        examples = listOf(
            ex("Teks tentang banjir: paragraf 1 membahas sampah yang menyumbat saluran dan berkurangnya daerah resapan; paragraf 2 membahas kerugian rumah warga dan penyakit; paragraf 3 membahas normalisasi sungai dan biopori. Kerangka yang tepat?",
                "Paragraf 1 = penyebab, paragraf 2 = dampak, paragraf 3 = solusi.",
                "Kerangka harus memuat ketiganya dengan urutan yang sama.",
                answer = "Penyebab banjir, dampak banjir, lalu upaya mengatasi banjir"),
        ),
        pitfalls = listOf("Memilih kerangka yang urutannya tertukar atau melewatkan satu paragraf."),
        points = listOf(
            "Kerangka = ide pokok tiap paragraf, berurutan.",
            "Bagan menyesuaikan pola teks: sebab-akibat, proses, klasifikasi.",
        ),
    )

    private val teksJamak = Section(
        title = "Teks jamak: membandingkan dua teks",
        explain = listOf(
            "Soal membaca memakai teks informasi tunggal dan jamak. Teks jamak berarti kamu membaca dua teks (atau teks dan infografik) dengan topik yang sama, lalu diminta mencari persamaan, perbedaan, atau hubungan keduanya.",
            "Langkahnya: (1) tentukan topik bersama, (2) tentukan fokus atau sudut pandang masing-masing teks, (3) tentukan apakah kedua teks saling mendukung, saling melengkapi, atau bertentangan.",
            "Persamaan biasanya ada pada topik. Perbedaan biasanya ada pada fokus, sudut pandang, data yang dipakai, atau simpulan penulis.",
        ),
        examples = listOf(
            ex("Teks 1: \"Belajar daring menghemat waktu perjalanan dan memungkinkan siswa mengulang rekaman materi.\" Teks 2: \"Dalam sebuah survei sekolah, sebagian besar siswa mengaku sulit berkonsentrasi saat belajar daring karena gangguan di rumah.\" Apa perbedaan kedua teks?",
                "Topik bersama: belajar daring.",
                "Teks 1 menonjolkan kelebihan. Teks 2 menyoroti kendala dan memakai data survei.",
                "Kedua teks saling melengkapi karena membahas sisi berbeda.",
                answer = "Teks 1 membahas kelebihan belajar daring, teks 2 membahas kendalanya berdasarkan survei"),
        ),
        pitfalls = listOf("Menjawab perbedaan dengan detail yang hanya ada di salah satu teks tanpa membandingkannya."),
        points = listOf(
            "Persamaan biasanya di topik.",
            "Perbedaan di fokus, sudut pandang, atau data.",
        ),
    )

    private val keakuratan = Section(
        title = "Menilai keakuratan & kecukupan informasi",
        explain = listOf(
            "Pada level evaluasi, kamu diminta menilai teks, bukan sekadar memahaminya. Informasi disebut AKURAT kalau sesuai dengan fakta atau data yang ada di teks, tabel, atau grafik. Cocokkan angka, satuan, dan istilahnya satu per satu.",
            "Informasi disebut CUKUP kalau data yang tersedia memang mampu mendukung simpulan. Simpulan \"semua siswa suka membaca\" dari survei terhadap 10 siswa tidak cukup didukung data.",
            "Kamu juga bisa diminta menilai relevansi: apakah peristiwa dalam teks berhubungan dengan kehidupan sehari-hari, dan apakah sebuah kalimat cocok dengan isi paragrafnya.",
        ),
        examples = listOf(
            ex("Teks: \"Pengunjung perpustakaan naik dari 200 menjadi 300 orang per bulan.\" Pernyataan: \"Pengunjung naik 100%.\" Akuratkah?",
                "Kenaikan = 300 − 200 = 100 orang.",
                "Persentase = §\\frac{100}{200} \\times 100\\% = 50\\%§.",
                answer = "Tidak akurat, kenaikannya 50%"),
        ),
        pitfalls = listOf("Menilai pernyataan benar hanya karena terdengar masuk akal, tanpa mencocokkan data teks."),
        points = listOf(
            "Akurat = cocok dengan data teks.",
            "Cukup = data mampu mendukung simpulan.",
        ),
    )

    private val kataSerapan = Section(
        title = "Kata serapan & istilah",
        explain = listOf(
            "Kata serapan adalah kata dari bahasa lain yang dipakai dalam bahasa Indonesia. Sumbernya bahasa asing (Inggris, Belanda, Arab, Sanskerta) dan bahasa daerah. Contoh dari bahasa daerah: unduh (Jawa), anjangsana (Jawa), dan ngabuburit (Sunda).",
            "Kata serapan asing disesuaikan ejaannya. Beberapa pola yang sering muncul: -tion menjadi -si (education → edukasi), -ty menjadi -tas (university → universitas), ph menjadi f (photography → fotografi), x menjadi ks (taxi → taksi), dan c di depan a, o, u menjadi k (computer → komputer).",
            "Soal kata baku sering memakai kata serapan. Bentuk yang baku antara lain analisis (bukan analisa), kualitas (bukan kwalitas), apotek (bukan apotik), dan sistem (bukan sistim).",
            "Istilah teknis adalah kata yang maknanya khusus dalam suatu bidang, misalnya inflasi (ekonomi), fotosintesis (biologi), atau algoritma (informatika). Kosakata seperti ini membantu menebak bidang atau latar sebuah teks.",
        ),
        examples = listOf(
            ex("Kalimat manakah yang memakai kata baku? (a) Hasil analisa itu valid. (b) Kwalitas produknya meningkat. (c) Ia membeli obat di apotek.",
                "Yang baku analisis, bukan analisa.",
                "Yang baku kualitas, bukan kwalitas.",
                "Apotek sudah baku.",
                answer = "(c)"),
        ),
        pitfalls = listOf("Mengira bentuk yang sering dipakai sehari-hari (analisa, apotik) pasti baku."),
        points = listOf(
            "-tion menjadi -si, -ty menjadi -tas, ph menjadi f.",
            "Baku: analisis, kualitas, apotek, sistem.",
            "Istilah teknis menandakan bidang teks.",
        ),
    )

    private val puisi = Section(
        title = "Puisi: unsur, makna & suasana",
        explain = listOf(
            "Puisi memadatkan makna dalam sedikit kata. Unsur pembangunnya antara lain tema, diksi (pilihan kata), imaji (kata yang membangkitkan pancaindra, misalnya penglihatan atau pendengaran), majas, rima, tipografi, dan amanat.",
            "Soal puisi sering meminta kamu menyimpulkan respons emosional terhadap puisi. Caranya, perhatikan diksi yang membangun suasana. Kata seperti sepi, layu, dan pergi membangun suasana sedih. Kata seperti fajar, mekar, dan berlari membangun suasana semangat atau gembira.",
            "Untuk menafsirkan makna, ubah dulu bahasa kias menjadi bahasa biasa, lalu hubungkan dengan judul dan bait lainnya.",
        ),
        examples = listOf(
            ex("\"Ibu, di keningmu kubaca peta / jalan pulang yang tak pernah lelah.\" Apa makna dan suasananya?",
                "Kening ibu yang dibaca seperti peta adalah kiasan: ibu menjadi petunjuk arah hidup.",
                "\"Jalan pulang yang tak pernah lelah\" menunjukkan ibu selalu menjadi tempat kembali.",
                answer = "Ibu sebagai penuntun dan tempat kembali; suasana haru"),
        ),
        pitfalls = listOf("Menafsirkan kata kias secara harfiah."),
        points = listOf(
            "Diksi membangun suasana.",
            "Ubah kiasan menjadi bahasa biasa sebelum menafsirkan.",
        ),
    )

    private val drama = Section(
        title = "Drama: dialog, konflik & watak",
        explain = listOf(
            "Teks drama disusun dalam bentuk dialog. Bagiannya terdiri dari prolog (pengantar), dialog, dan epilog (penutup). Teks drama juga memuat petunjuk lakuan atau kramagung, biasanya ditulis dalam kurung, yang menjelaskan gerak dan ekspresi tokoh.",
            "Watak tokoh drama jarang disebut langsung. Watak itu terlihat dari ucapan, cara menanggapi tokoh lain, dan petunjuk lakuan. Konflik bisa berupa konflik batin (pertentangan dalam diri tokoh) atau konflik antartokoh.",
            "Untuk menyimpulkan respons emosional, perhatikan petunjuk lakuan seperti (menunduk), (membanting pintu), atau (tersenyum lega).",
        ),
        examples = listOf(
            ex("Raka: \"Aku sudah bilang, aku tidak mau ikut lomba!\" (membanting buku)  Ibu: (menghela napas) \"Ibu hanya ingin kamu mencoba, Nak.\" Bagaimana watak dan konfliknya?",
                "Raka berbicara keras dan membanting buku, jadi emosional, keras kepala.",
                "Ibu menghela napas dan berbicara lembut, jadi sabar.",
                "Pertentangan keinginan keduanya adalah konflik antartokoh.",
                answer = "Raka emosional, ibu sabar; konflik antartokoh"),
        ),
        pitfalls = listOf("Mengabaikan petunjuk lakuan saat menyimpulkan watak."),
        points = listOf(
            "Prolog, dialog, epilog, dan petunjuk lakuan.",
            "Watak terlihat dari ucapan dan lakuan.",
        ),
    )

    private val prediksiNilai = Section(
        title = "Memprediksi lanjutan & menilai nilai cerita",
        explain = listOf(
            "Soal memprediksi meminta kelanjutan atau akhir cerita yang paling masuk akal. Jawaban yang tepat harus konsisten dengan watak tokoh, konflik yang sedang berlangsung, dan petunjuk dalam teks. Pilihan yang tiba-tiba mengubah watak tokoh tanpa alasan biasanya salah.",
            "Nilai dalam cerita meliputi nilai moral (baik-buruk perilaku), sosial (hubungan dengan sesama), budaya (adat dan tradisi), dan religius (hubungan dengan Tuhan). Nilai ditentukan dari tindakan tokoh dan akibatnya.",
            "Menilai relevansi berarti menghubungkan peristiwa dalam cerita dengan kehidupan nyata: apakah sikap tokoh patut ditiru, dan dalam situasi apa sikap itu bisa diterapkan.",
        ),
        examples = listOf(
            ex("Sari selalu menyisihkan uang jajannya untuk membeli buku bagi adik-adik di panti asuhan dekat rumahnya. Nilai apa yang paling menonjol?",
                "Tindakan Sari: berbagi untuk orang lain.",
                "Ini menyangkut hubungan dengan sesama.",
                answer = "Nilai sosial (kepedulian)"),
        ),
        pitfalls = listOf("Memilih lanjutan cerita yang menarik tetapi tidak didukung petunjuk teks."),
        points = listOf(
            "Prediksi harus konsisten dengan watak dan konflik.",
            "Nilai: moral, sosial, budaya, religius.",
        ),
    )

    private val kalimatTandaBaca = Section(
        title = "Kalimat kompleks, konjungsi & tanda baca",
        explain = listOf(
            "Teks bacaan sering memakai kalimat kompleks berbagai pola. Kalimat majemuk setara menggabungkan dua klausa yang sederajat dengan dan, atau, tetapi. Kalimat majemuk bertingkat terdiri dari induk kalimat dan anak kalimat yang ditandai karena, ketika, jika, meskipun, sehingga, atau agar.",
            "Aturan koma pada kalimat bertingkat (PUEBI): pakai koma kalau anak kalimat berada DI DEPAN induk kalimat (\"Meskipun lelah, ia tetap belajar.\"). Jangan pakai koma kalau anak kalimat berada di belakang (\"Ia tetap belajar meskipun lelah.\").",
            "Konjungsi antarparagraf yang disebut kisi-kisi bermakna pertentangan (namun, akan tetapi, sebaliknya) dan sebab-akibat (oleh karena itu, akibatnya, dengan demikian). Kata-kata ini menunjukkan hubungan antara paragraf sebelum dan sesudahnya.",
            "Kata umum maknanya luas (bunga), sedangkan kata khusus maknanya sempit (melati). Tanda titik dua dipakai sebelum rincian, dan tanda petik dipakai untuk kutipan langsung.",
        ),
        examples = listOf(
            ex("Penulisan mana yang benar? (a) Ia tetap belajar, meskipun lelah. (b) Meskipun lelah, ia tetap belajar.",
                "Anak kalimat \"meskipun lelah\".",
                "Di (b) anak kalimat ada di depan, jadi memakai koma. Di (a) ada di belakang, jadi tidak boleh memakai koma.",
                answer = "(b)"),
            ex("Paragraf 1 membahas manfaat media sosial. Paragraf 2 membahas bahayanya. Konjungsi yang tepat di awal paragraf 2?",
                "Manfaat dan bahaya berlawanan.",
                "Perlu konjungsi pertentangan.",
                answer = "Namun / Akan tetapi"),
        ),
        pitfalls = listOf("Memakai koma sebelum \"karena\" atau \"meskipun\" yang berada di belakang induk kalimat."),
        points = listOf(
            "Anak kalimat di depan, jadi pakai koma.",
            "Namun = pertentangan; oleh karena itu = sebab-akibat.",
        ),
    )

    // ============================== FISIKA ==============================

    private val gerakVertikal = Section(
        title = "Gerak jatuh bebas & vertikal",
        explain = listOf(
            "Gerak vertikal adalah GLBB dengan percepatan gravitasi g (sekitar §9{,}8\\,\\mathrm{m} / \\mathrm{s}^{2}§, sering dibulatkan §10\\,\\mathrm{m} / \\mathrm{s}^{2}§) yang selalu mengarah ke bawah. Massa benda tidak memengaruhi gerak jatuh kalau gesekan udara diabaikan.",
            "Jatuh bebas berarti benda dilepas tanpa kecepatan awal §(v_{0} = 0)§. Kecepatan saat menyentuh tanah dari ketinggian h adalah §v = \\sqrt{2gh}§, dan waktunya §t = \\sqrt{2h/g}§.",
            "Pada gerak vertikal ke atas, kecepatan berkurang g setiap detik sampai nol di titik tertinggi. Waktu naik §t = \\frac{v_{0}}{g}§ dan tinggi maksimum §h = \\frac{v_{0}^{2}}{2g}§. Waktu turun sama dengan waktu naik, sehingga waktu di udara §\\frac{2v_{0}}{g}§.",
        ),
        formulas = listOf(
            "Jatuh bebas: §v = \\sqrt{2gh} \\qquad t = \\sqrt{\\dfrac{2h}{g}} \\qquad h = \\tfrac{1}{2} g t^2§",
            "Ke atas: §t_{\\text{naik}} = \\dfrac{v_0}{g} \\qquad h_{\\text{maks}} = \\dfrac{v_0^2}{2g}§",
        ),
        examples = listOf(
            ex("Batu jatuh bebas dari ketinggian §20\\,\\mathrm{m}(g = 10\\,\\mathrm{m} / \\mathrm{s}^{2})§. Berapa waktu dan kecepatannya saat menyentuh tanah?",
                "§t = \\sqrt{2\\times20/10} = \\sqrt{4} = 2\\,\\mathrm{s}§.",
                "§v = \\sqrt{2\\times10\\times20} = \\sqrt{400} = 20\\,\\mathrm{m} / \\mathrm{s}§.",
                answer = "2 s dan §20\\,\\mathrm{m} / \\mathrm{s}§"),
            ex("Bola dilempar ke atas dengan kecepatan §30\\,\\mathrm{m} / \\mathrm{s}(g = 10\\,\\mathrm{m} / \\mathrm{s}^{2})§. Berapa tinggi maksimum dan lama bola di udara?",
                "§h = \\frac{30^{2}}{2\\times10} = \\frac{900}{20} = 45\\,\\mathrm{m}§.",
                "t naik = §\\frac{30}{10} = 3\\,\\mathrm{s}§, jadi di udara 6 s.",
                answer = "45 m dan 6 s"),
        ),
        pitfalls = listOf("Mengira benda yang lebih berat jatuh lebih cepat (tanpa gesekan udara, sama cepat)."),
        points = listOf(
            "Jatuh bebas: §v = \\sqrt{2gh}§.",
            "Ke atas: h maks = §\\frac{v_{0}^{2}}{2g}§, waktu di udara §\\frac{2v_{0}}{g}§.",
        ),
    )

    private val persamaanGelombang = Section(
        title = "Persamaan gelombang & gelombang stasioner",
        explain = listOf(
            "Gelombang berjalan bisa ditulis sebagai §y = A\\sin (\\omega t - kx)§. A adalah amplitudo, §\\omega = 2\\pi f§ adalah frekuensi sudut, dan §k = \\frac{2\\pi}{\\lambda}§ adalah bilangan gelombang. Tanda minus di antara §\\omega t§ dan kx berarti gelombang merambat ke arah sumbu x positif. Cepat rambatnya §v = \\frac{\\omega}{k}§.",
            "Cara membaca cepat: angka di depan t dibagi §2\\pi§ memberi f, dan angka di depan x dibagi §2\\pi§ memberi §\\frac{1}{\\lambda}§.",
            "Gelombang stasioner terbentuk dari dua gelombang sama yang berlawanan arah, misalnya gelombang datang dan pantulnya. Hasilnya ada titik yang selalu diam (simpul) dan titik yang bergetar paling kuat (perut). Jarak dua simpul berdekatan = §\\tfrac{1}{2}\\lambda§.",
            "Pada dawai (senar), kedua ujungnya simpul, sehingga nada dasar terjadi ketika panjang dawai = §\\tfrac{1}{2}\\lambda§: §f_{1} = \\frac{v}{2L}§. Pipa organa terbuka juga §f_{1} = \\frac{v}{2L}§. Pipa organa tertutup (satu ujung tertutup) panjangnya §\\tfrac{1}{4}\\lambda§ pada nada dasar, sehingga §f_{1} = \\frac{v}{4L}§, dan hanya menghasilkan harmonik ganjil.",
        ),
        formulas = listOf(
            "§y = A \\sin(\\omega t - kx) \\qquad \\omega = 2\\pi f \\qquad k = \\dfrac{2\\pi}{\\lambda} \\qquad v = \\dfrac{\\omega}{k}§",
            "Dawai & pipa terbuka: §f_n = \\dfrac{n\\,v}{2L}§",
            "Pipa tertutup: §f = \\dfrac{(2n - 1)\\,v}{4L}§",
            "Cepat rambat dawai: §v = \\sqrt{\\dfrac{F}{\\mu}}§",
        ),
        examples = listOf(
            ex("§y = 0{,}05\\sin (10\\pi t - 2\\pi x)§ dalam meter dan sekon. Tentukan amplitudo, frekuensi, panjang gelombang, dan cepat rambat.",
                "§A = 0{,}05\\,\\mathrm{m}§.",
                "§\\omega = 10\\pi \\to f = \\frac{10\\pi}{2\\pi} = 5§ Hz. §k = 2\\pi \\to \\lambda = \\frac{2\\pi}{2\\pi} = 1\\,\\mathrm{m}§.",
                "§v = \\lambda f = 1 \\times 5 = 5\\,\\mathrm{m} / \\mathrm{s}§.",
                answer = "0,05 m; 5 Hz; 1 m; §5\\,\\mathrm{m} / \\mathrm{s}§"),
            ex("Pipa organa sepanjang 50 cm, cepat rambat bunyi §340\\,\\mathrm{m} / \\mathrm{s}§. Berapa nada dasarnya jika pipa terbuka dan jika tertutup?",
                "Terbuka: §f_{1} = \\frac{340}{2\\times0{,}5} = 340§ Hz.",
                "Tertutup: §f_{1} = \\frac{340}{4\\times0{,}5} = 170§ Hz.",
                answer = "340 Hz (terbuka) dan 170 Hz (tertutup)"),
        ),
        pitfalls = listOf("Membaca angka di depan t langsung sebagai f tanpa membagi §2\\pi§."),
        points = listOf(
            "§f = \\frac{\\omega}{2\\pi}§, §\\lambda = \\frac{2\\pi}{k}§, §v = \\frac{\\omega}{k}§.",
            "Pipa terbuka §\\frac{v}{2L}§, pipa tertutup §\\frac{v}{4L}§.",
        ),
    )

    private val tarafIntensitas = Section(
        title = "Taraf intensitas bunyi",
        explain = listOf(
            "Telinga manusia mampu mendengar intensitas yang rentangnya sangat lebar, dari §\\frac{10^{-12}W}{m^{2}}§ (ambang dengar) sampai sekitar §1\\,\\mathrm{W} / \\mathrm{m}^{2}§. Karena itu dipakai skala logaritma bernama taraf intensitas (TI) dengan satuan desibel (dB): TI = §10\\log (\\frac{I}{I_{0}})§.",
            "Karena logaritma, perkalian intensitas menjadi penjumlahan desibel. Kalau ada n sumber identik, intensitas menjadi n kali, sehingga TI bertambah 10 log n. Sepuluh sumber menambah 10 dB, seratus sumber menambah 20 dB.",
            "Kalau jarak menjadi §r_{2}§, intensitas berubah dengan §(\\frac{r_{1}}{r_{2}})^{2}§, sehingga TI berkurang §20\\log (\\frac{r_{2}}{r_{1}})§. Jarak 10 kali lebih jauh berarti TI turun 20 dB.",
        ),
        formulas = listOf(
            "§TI = 10 \\log\\dfrac{I}{I_0}, \\quad I_0 = 10^{-12} \\text{ W/m}^2§",
            "n sumber: §TI_n = TI_1 + 10 \\log n§",
            "Jarak: §TI_2 = TI_1 - 20 \\log\\dfrac{r_2}{r_1}§",
        ),
        examples = listOf(
            ex("Sebuah mesin menghasilkan TI 60 dB. Berapa TI 10 mesin identik yang menyala bersamaan?",
                "§TI_{10} = 60 + 10\\log 10§.",
                "= §60 + 10 = 70§ dB.",
                answer = "70 dB"),
            ex("TI di jarak 2 m adalah 80 dB. Berapa TI di jarak 20 m?",
                "§\\frac{r_{2}}{r_{1}} = 10§.",
                "§TI_{2} = 80 - 20\\log 10 = 80 - 20 = 60§ dB.",
                answer = "60 dB"),
        ),
        pitfalls = listOf("Mengalikan desibel dengan jumlah sumber (§10 \\times 60§ dB = 600 dB). Salah, desibel ditambah 10 log n."),
        points = listOf(
            "n sumber: tambah 10 log n.",
            "Jarak ×10: kurang 20 dB.",
        ),
    )

    private val interferensi = Section(
        title = "Interferensi & difraksi cahaya",
        explain = listOf(
            "Bukti bahwa cahaya adalah gelombang adalah interferensi: dua gelombang cahaya yang bertemu bisa saling menguatkan (terang) atau saling melemahkan (gelap).",
            "Pada percobaan celah ganda Young, cahaya melewati dua celah berjarak d dan membentuk pola terang-gelap di layar berjarak L. Garis terang ke-n berada pada jarak y dari terang pusat, dengan §d \\cdot \\frac{y}{L} = n\\lambda§. Artinya selisih lintasan dari kedua celah tepat n kali panjang gelombang.",
            "Kisi difraksi adalah lempeng dengan sangat banyak celah. Kalau kisi punya N garis per satuan panjang, jarak antarcelahnya §d = \\frac{1}{N}§. Terang ke-n terjadi pada sudut §\\theta§ dengan §d \\cdot \\sin \\theta = n\\lambda§.",
        ),
        formulas = listOf(
            "Celah ganda (terang): §\\dfrac{d\\,y}{L} = n\\lambda§",
            "Kisi: §d \\sin\\theta = n\\lambda, \\quad d = \\dfrac{1}{N}§",
            "§1 \\text{ nm} = 10^{-9} \\text{ m}§",
        ),
        examples = listOf(
            ex("Celah ganda berjarak 0,5 mm disinari cahaya 600 nm. Layar berjarak 1 m. Berapa jarak terang pertama dari terang pusat?",
                "§y = \\frac{n\\lambda L}{d} = 1 \\times 6 \\times 10^{-7} \\times \\frac{1}{5\\times10^{-4}}§.",
                "= §1{,}2 \\times 10^{-3}m = 1{,}2§ mm.",
                answer = "1,2 mm"),
            ex("Kisi 500 garis/mm disinari cahaya 500 nm. Berapa §\\sin \\theta§ untuk terang orde pertama?",
                "§d = \\frac{1}{500}§ mm = §2 \\times 10^{-3}§ mm = §2 \\times 10^{-6}m§.",
                "§\\sin \\theta = \\frac{n\\lambda}{d} = 5 \\times \\frac{10^{-7}}{2} \\times 10^{-6} = 0{,}25§.",
                answer = "§\\sin \\theta = 0{,}25§"),
        ),
        pitfalls = listOf("Tidak mengubah mm dan nm ke meter."),
        points = listOf(
            "Young: §\\frac{dy}{L} = n\\lambda§.",
            "Kisi: §d\\sin \\theta = n\\lambda§ dengan §d = \\frac{1}{N}§.",
        ),
    )

    private val alatOptik = Section(
        title = "Alat optik: mata & lup",
        explain = listOf(
            "Mata normal bisa melihat jelas dari titik dekat 25 cm sampai titik jauh tak hingga. Rabun jauh (miopi) membuat titik jauh lebih dekat, sehingga ditolong lensa cekung (kacamata minus). Kekuatannya §P = - \\frac{100}{PR}§ dengan PR (titik jauh) dalam cm.",
            "Rabun dekat (hipermetropi) membuat titik dekat lebih jauh dari 25 cm, sehingga ditolong lensa cembung (kacamata plus). Kekuatannya §P = \\frac{100}{25} - \\frac{100}{PP}§ dengan PP (titik dekat) dalam cm.",
            "Lup (kaca pembesar) adalah lensa cembung. Kalau mata tidak berakomodasi (benda di titik fokus), perbesarannya §M = \\frac{25}{f}§. Kalau mata berakomodasi maksimum, §M = \\frac{25}{f} + 1§.",
        ),
        formulas = listOf(
            "Miopi: §P = -\\dfrac{100}{PR}§",
            "Hipermetropi: §P = \\dfrac{100}{25} - \\dfrac{100}{PP}§",
            "Lup: §M = \\dfrac{25}{f}§ (tak berakomodasi), §M = \\dfrac{25}{f} + 1§ (akomodasi maksimum)",
        ),
        examples = listOf(
            ex("Titik jauh seseorang 200 cm. Berapa kekuatan kacamatanya?",
                "§P = - \\frac{100}{200}§.",
                "= −0,5 dioptri (lensa cekung).",
                answer = "−0,5 D"),
            ex("Titik dekat seseorang 50 cm. Berapa kekuatan kacamatanya?",
                "§P = \\frac{100}{25} - \\frac{100}{50} = 4 - 2§.",
                "= 2 dioptri (lensa cembung).",
                answer = "+2 D"),
            ex("Lup berfokus 5 cm. Berapa perbesarannya untuk mata tak berakomodasi dan berakomodasi maksimum?",
                "Tak berakomodasi: §\\frac{25}{5} = 5§ kali.",
                "Akomodasi maksimum: §5 + 1 = 6§ kali.",
                answer = "5× dan 6×"),
        ),
        pitfalls = listOf("Tertukar lensa: miopi pakai cekung, hipermetropi pakai cembung."),
        points = listOf(
            "Miopi: cekung, §P = - \\frac{100}{PR}§.",
            "Hipermetropi: cembung, §P = 4 - \\frac{100}{PP}§.",
        ),
    )

    private val pemuaian = Section(
        title = "Pemuaian zat",
        explain = listOf(
            "Saat dipanaskan, partikel zat bergetar lebih kuat dan jarak rata-ratanya bertambah, sehingga ukuran benda membesar. Inilah pemuaian.",
            "Pertambahan panjang sebanding dengan panjang awal, kenaikan suhu, dan jenis bahan (koefisien muai panjang α): §\\Delta L = L_{0} \\cdot \\alpha \\cdot \\Delta T§. Untuk luas dipakai §\\beta = 2\\alpha§, dan untuk volume §\\gamma = 3\\alpha§.",
            "Pemuaian diperhitungkan dalam kehidupan sehari-hari: celah pada sambungan rel kereta, kabel listrik yang dipasang kendur, dan keping bimetal pada termostat.",
        ),
        formulas = listOf(
            "§\\Delta L = L_0 \\alpha \\Delta T \\qquad \\Delta A = A_0 \\beta \\Delta T \\qquad \\Delta V = V_0 \\gamma \\Delta T§",
            "§\\beta = 2\\alpha \\qquad \\gamma = 3\\alpha§",
        ),
        examples = listOf(
            ex("Rel baja sepanjang §10\\,\\mathrm{m}(\\alpha = 1{,}2 \\times 10^{-5} / ^\\circ C)§ mengalami kenaikan suhu 30°C. Berapa pertambahan panjangnya?",
                "§\\Delta L = 10 \\times 1{,}2 \\times 10^{-5} \\times 30§.",
                "= §3{,}6 \\times 10^{-3}m = 3{,}6§ mm.",
                answer = "3,6 mm"),
        ),
        pitfalls = listOf("Memakai α untuk pemuaian volume. Volume memakai §\\gamma = 3\\alpha§."),
        points = listOf(
            "§\\Delta L = L_{0}\\alpha \\Delta T§.",
            "§\\beta = 2\\alpha§, §\\gamma = 3\\alpha§.",
        ),
    )

    private val kinetikGas = Section(
        title = "Teori kinetik gas",
        explain = listOf(
            "Teori kinetik gas menjelaskan sifat gas dari gerak partikelnya. Suhu gas adalah ukuran energi kinetik rata-rata partikelnya: §Ek = \\frac{3}{2} \\cdot k \\cdot T§, dengan §k = 1{,}38 \\times \\frac{10^{-23}J}{K}§ dan T dalam kelvin. Jadi kalau suhu mutlak naik dua kali, energi kinetik rata-ratanya juga naik dua kali.",
            "Kecepatan efektif partikel v_rms = §\\sqrt{3RT/M}§ sebanding dengan §\\sqrt{T}§. Kalau suhu mutlak menjadi empat kali, kecepatannya menjadi dua kali.",
            "Persamaan umum gas ideal PV = nRT menghubungkan tekanan, volume, jumlah mol, dan suhu. §R = 8{,}31\\,\\mathrm{J} / (\\mathrm{mol} \\cdot K)§.",
        ),
        formulas = listOf(
            "§PV = nRT \\qquad R = 8{,}31 \\text{ J/(mol K)}§",
            "§E_k = \\tfrac{3}{2} k T \\qquad k = 1{,}38 \\times 10^{-23} \\text{ J/K}§",
            "§v_{\\text{rms}} = \\sqrt{\\dfrac{3RT}{M}} \\propto \\sqrt{T}§",
        ),
        examples = listOf(
            ex("Suhu gas dinaikkan dari 27°C menjadi 927°C. Berapa kali kecepatan efektif dan energi kinetik partikelnya berubah?",
                "§T_{1} = 300\\,\\mathrm{K}§ dan §T_{2} = 1.200\\,\\mathrm{K}§, jadi suhu mutlak naik 4 kali.",
                "Ek ∝ T, jadi naik 4 kali.",
                "v ∝ §\\sqrt{T}§, jadi naik §\\sqrt{4} = 2§ kali.",
                answer = "v naik 2 kali, Ek naik 4 kali"),
            ex("2 mol gas ideal bersuhu 300 K berada dalam wadah §0{,}05\\,\\mathrm{m}^{3}§. Berapa tekanannya?",
                "§P = \\frac{nRT}{V} = 2 \\times 8{,}31 \\times \\frac{300}{0{,}05}§.",
                "= §\\frac{4.986}{0{,}05} = 99.720§ Pa §\\approx 1 \\times 10^{5}§ Pa.",
                answer = "§\\approx 1 \\times 10^{5}§ Pa"),
        ),
        pitfalls = listOf("Memakai suhu dalam °C sehingga perbandingannya salah."),
        points = listOf(
            "Ek ∝ T, v_rms ∝ §\\sqrt{T}§.",
            "PV = nRT dengan T dalam kelvin.",
        ),
    )

    private val potensialKapasitor = Section(
        title = "Potensial listrik & kapasitor",
        explain = listOf(
            "Potensial listrik adalah energi potensial per satuan muatan. Di sekitar muatan titik q, potensialnya §V = \\frac{kq}{r}§. Berbeda dengan medan, potensial adalah besaran skalar, jadi potensial dari beberapa muatan cukup dijumlahkan dengan memperhatikan tanda muatan.",
            "Usaha untuk memindahkan muatan q dari satu titik ke titik lain sama dengan q dikali beda potensialnya: §W = q \\cdot \\Delta V§.",
            "Kapasitor menyimpan muatan. Kapasitansinya §C = \\frac{Q}{V}§. Kapasitor keping sejajar punya §C = \\frac{\\varepsilon _{0}A}{d}§, jadi makin luas kepingnya dan makin dekat jaraknya, makin besar kapasitansinya. Energi yang tersimpan §W = \\tfrac{1}{2}CV^{2}§.",
            "Rangkaian kapasitor kebalikan dari resistor. Pada rangkaian paralel kapasitansi dijumlah langsung, sedangkan pada rangkaian seri dipakai kebalikannya.",
        ),
        formulas = listOf(
            "§V = k\\dfrac{q}{r} \\qquad W = q\\,\\Delta V§",
            "§C = \\dfrac{Q}{V} \\qquad C = \\dfrac{\\varepsilon_0 A}{d} \\qquad W = \\tfrac{1}{2} C V^2§",
            "Paralel: §C_p = C_1 + C_2§ · Seri: §\\dfrac{1}{C_s} = \\dfrac{1}{C_1} + \\dfrac{1}{C_2}§",
        ),
        examples = listOf(
            ex("Berapa potensial listrik pada jarak 30 cm dari muatan 2 μC?",
                "§V = 9 \\times 10^{9} \\times 2 \\times \\frac{10^{-6}}{0{,}3}§.",
                "= §18 \\times \\frac{10^{3}}{0{,}3} = 6 \\times 10^{4}V§.",
                answer = "§6 \\times 10^{4}V§"),
            ex("Kapasitor 2 μF dan 3 μF dirangkai seri, lalu dirangkai paralel. Berapa kapasitansi totalnya?",
                "Seri: §C = \\frac{2\\times3}{2+3} = 1{,}2\\mu F§.",
                "Paralel: §C = 2 + 3 = 5\\mu F§.",
                answer = "Seri 1,2 μF; paralel 5 μF"),
            ex("Kapasitor 10 μF diberi tegangan 12 V. Berapa energi yang tersimpan?",
                "§W = \\tfrac{1}{2} \\times 10 \\times 10^{-6} \\times 12^{2}§.",
                "= §\\tfrac{1}{2} \\times 10^{-5} \\times 144 = 7{,}2 \\times 10^{-4}J§.",
                answer = "§7{,}2 \\times 10^{-4}J§"),
        ),
        pitfalls = listOf("Menjumlahkan kapasitor seri secara langsung seperti resistor seri."),
        points = listOf(
            "§V = \\frac{kq}{r}§ adalah besaran skalar.",
            "Kapasitor paralel dijumlah, seri pakai kebalikan.",
            "§W = \\tfrac{1}{2}CV^{2}§.",
        ),
    )

    private val ggl = Section(
        title = "GGL & hambatan dalam",
        explain = listOf(
            "Baterai punya gaya gerak listrik (GGL) ε, yaitu tegangan saat tidak ada arus. Di dalam baterai sendiri ada hambatan kecil r yang disebut hambatan dalam.",
            "Saat baterai dihubungkan ke hambatan luar R, arus yang mengalir §I = \\frac{\\varepsilon}{R+r}§, karena arus melewati hambatan luar dan hambatan dalam sekaligus.",
            "Tegangan yang terukur di kutub baterai (tegangan jepit) lebih kecil dari GGL, karena sebagian tegangan \"hilang\" di hambatan dalam: §V = \\varepsilon - I \\cdot r§. Inilah alasan lampu sedikit meredup ketika banyak alat dinyalakan bersamaan dari baterai yang sama.",
        ),
        formulas = listOf(
            "§I = \\dfrac{\\varepsilon}{R + r}§",
            "Tegangan jepit: §V = \\varepsilon - I r = I R§",
        ),
        examples = listOf(
            ex("Baterai 12 V dengan hambatan dalam 1 Ω dihubungkan ke resistor 5 Ω. Berapa arus dan tegangan jepitnya?",
                "§I = \\frac{12}{5+1} = 2\\,\\mathrm{A}§.",
                "§V = 12 - 2 \\times 1 = 10\\,\\mathrm{V}§ (cek: §I \\cdot R = 2 \\times 5 = 10\\,\\mathrm{V}§).",
                answer = "2 A dan 10 V"),
        ),
        pitfalls = listOf("Lupa menambahkan hambatan dalam ke hambatan total."),
        points = listOf(
            "§I = \\frac{\\varepsilon}{R+r}§.",
            "Tegangan jepit = ε − Ir.",
        ),
    )
}
