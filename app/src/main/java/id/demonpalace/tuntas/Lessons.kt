package id.demonpalace.tuntas

/**
 * Textbook-style lesson cards: explanation first, then the formula and why it works,
 * worked examples step by step, common traps, and a short summary to tick off.
 * Scope follows the TKA SMA 2026 kisi-kisi listed in [Content.sources].
 */
object Lessons {

    private fun ex(problem: String, vararg steps: String, answer: String) = Example(problem, steps.toList(), answer)

    val byStep: Map<String, List<Section>> = mapOf(

        // ======================= MATEMATIKA: BILANGAN & ALJABAR =======================
        "mat1" to listOf(
            Section(
                title = "Persen",
                explain = listOf(
                    "Persen artinya \"per seratus\". Jadi 20% sama dengan §\\frac{20}{100} = 0{,}2§. Soal persen dikerjakan dengan mengubah persen menjadi bilangan desimal, lalu mengalikannya.",
                    "Kalau harga NAIK p%, harga baru = harga lama + p% harga lama = harga lama × §(1 + \\frac{p}{100})§. Kalau harga TURUN atau DISKON p%, harga baru = harga lama × §(1 - \\frac{p}{100})§. Angka §(1 + \\frac{p}{100})§ atau §(1 - \\frac{p}{100})§ disebut faktor pengali.",
                    "Diskon bertingkat tidak boleh dijumlah karena diskon kedua dihitung dari harga yang SUDAH didiskon, bukan dari harga awal. Faktornya dikalikan berurutan.",
                    "Jika yang diketahui harga akhir, harga awal didapat dengan MEMBAGI harga akhir dengan faktornya.",
                ),
                formulas = listOf(
                    "Naik p%: §\\text{baru} = \\text{lama} \\times \\left(1 + \\dfrac{p}{100}\\right)§",
                    "Turun/diskon p%: §\\text{baru} = \\text{lama} \\times \\left(1 - \\dfrac{p}{100}\\right)§",
                    "Bertingkat: §\\text{baru} = \\text{lama} \\times f_1 \\times f_2§",
                    "Mencari harga awal: §\\text{lama} = \\dfrac{\\text{baru}}{\\text{faktor}}§",
                ),
                examples = listOf(
                    ex("Baju Rp200.000 didiskon 20%, lalu didiskon lagi 10% dari harga setelah diskon pertama. Berapa harga akhirnya?",
                        "Faktor diskon §20\\% = 1 - 0{,}20 = 0{,}8§. Faktor diskon §10\\% = 1 - 0{,}10 = 0{,}9§.",
                        "Harga setelah diskon pertama: §200.000 \\times 0{,}8 = 160.000§.",
                        "Harga setelah diskon kedua: §160.000 \\times 0{,}9 = 144.000§.",
                        "Cek: diskon totalnya §200.000 - 144.000 = 56.000§, yaitu 28%, bukan 30%.",
                        answer = "Rp144.000"),
                    ex("Setelah naik 25%, harga sepatu menjadi Rp50.000. Berapa harga sebelum naik?",
                        "Faktor naik §25\\% = 1 + 0{,}25 = 1{,}25§. Artinya harga awal × §1{,}25 = 50.000§.",
                        "Harga awal = §50.000 \\div 1{,}25 = 40.000§.",
                        "Cek: §40.000 \\times 1{,}25 = 50.000§.",
                        answer = "Rp40.000"),
                ),
                pitfalls = listOf(
                    "Menjumlah diskon bertingkat §(20\\% + 10\\% = 30\\%)§. Salah, harus dikalikan.",
                    "Mencari harga awal dengan §50.000 \\times 0{,}75§. Salah, karena 25% dihitung dari harga awal, bukan dari harga akhir.",
                ),
                points = listOf(
                    "Ubah persen jadi faktor: naik §(1 + \\frac{p}{100})§, turun §(1 - \\frac{p}{100})§.",
                    "Diskon bertingkat dikalikan berurutan, bukan dijumlah.",
                    "Harga awal = harga akhir ÷ faktor.",
                ),
            ),
            Section(
                title = "Perbandingan senilai & berbalik nilai",
                explain = listOf(
                    "Perbandingan SENILAI terjadi kalau dua besaran naik-turun bersama. Contoh: makin banyak buku yang dibeli, makin besar harganya. Hasil baginya selalu tetap, jadi §\\frac{a_{1}}{b_{1}} = \\frac{a_{2}}{b_{2}}§.",
                    "Perbandingan BERBALIK NILAI terjadi kalau yang satu naik, yang lain turun. Contoh: makin banyak pekerja, makin sedikit hari yang dibutuhkan. Di sini yang tetap adalah hasil kalinya, karena \"jumlah total pekerjaan\" (misalnya 72 hari-orang) tidak berubah. Jadi §a_{1} \\times b_{1} = a_{2} \\times b_{2}§.",
                    "Cara memilih: tanyakan pada dirimu, \"kalau yang satu ditambah, yang lain bertambah atau berkurang?\"",
                    "Skala peta juga perbandingan senilai: 1 : 200.000 artinya 1 cm di peta = 200.000 cm di dunia nyata.",
                ),
                formulas = listOf(
                    "Senilai: §\\dfrac{a_1}{b_1} = \\dfrac{a_2}{b_2}§",
                    "Berbalik nilai: §a_1 \\times b_1 = a_2 \\times b_2§",
                    "§\\text{jarak asli} = \\text{jarak peta} \\times \\text{penyebut skala}§ (1 km = 100.000 cm)",
                ),
                examples = listOf(
                    ex("6 pekerja menyelesaikan rumah dalam 12 hari. Kalau pekerjanya 9 orang, berapa hari?",
                        "Pekerja bertambah, jadi hari berkurang, jadi berbalik nilai.",
                        "Total pekerjaan = §6 \\times 12 = 72§ hari-orang.",
                        "§9 \\times n = 72 \\to n = 72 \\div 9 = 8§.",
                        answer = "8 hari"),
                    ex("Jarak dua kota di peta berskala 1 : 200.000 adalah 3 cm. Berapa jarak sebenarnya?",
                        "Jarak asli = §3 \\times 200.000 = 600.000§ cm.",
                        "Ubah ke km: §600.000 \\div 100.000 = 6§ km.",
                        answer = "6 km"),
                ),
                pitfalls = listOf(
                    "Memakai rumus senilai untuk soal pekerja-hari (hasilnya 18 hari, tidak masuk akal karena pekerja lebih banyak justru lebih lama).",
                    "Lupa mengubah cm ke km pada soal skala.",
                ),
                points = listOf(
                    "Senilai: hasil bagi tetap. Berbalik nilai: hasil kali tetap.",
                    "Skala: kalikan, lalu ubah satuan (1 km = 100.000 cm).",
                ),
            ),
            Section(
                title = "Pangkat & logaritma",
                explain = listOf(
                    "Pangkat adalah perkalian berulang: §2^{5} = 2 \\times 2 \\times 2 \\times 2 \\times 2 = 32§. Dari pengertian ini lahir sifat-sifatnya. Misalnya §2^{3} \\times 2^{2} = (2 \\times 2 \\times 2) \\times (2 \\times 2) = 2^{5}§, jadi pangkatnya DIJUMLAH ketika bilangan pokoknya sama dan dikalikan.",
                    "Logaritma adalah kebalikan pangkat. §^{2}\\log 8§ bertanya: \"2 dipangkatkan berapa supaya hasilnya 8?\" Jawabannya 3, karena §2^{3} = 8§. Jadi setiap soal logaritma bisa kamu ubah menjadi pertanyaan pangkat.",
                    "Sifat logaritma mengikuti sifat pangkat: karena perkalian pangkat menjumlahkan eksponen, log dari perkalian menjadi penjumlahan log.",
                ),
                formulas = listOf(
                    "§a^m \\cdot a^n = a^{m+n} \\qquad \\dfrac{a^m}{a^n} = a^{m-n} \\qquad (a^m)^n = a^{mn} \\qquad a^0 = 1§",
                    "§{}^{a}\\!\\log b = c \\iff a^c = b§",
                    "§\\log(xy) = \\log x + \\log y \\qquad \\log\\dfrac{x}{y} = \\log x - \\log y \\qquad \\log x^n = n \\log x§",
                ),
                examples = listOf(
                    ex("Hitung §2^{5} \\times 2^{3} \\div 2^{6}§.",
                        "Bilangan pokoknya sama (2), jadi cukup olah pangkatnya.",
                        "§5 + 3 - 6 = 2§.",
                        "§2^{2} = 4§.",
                        answer = "4"),
                    ex("Hitung §^{2}\\log 8 + ^{3}\\log 9§.",
                        "§^{2}\\log 8§: 2 pangkat berapa = 8? §2^{3} = 8§, jadi 3.",
                        "§^{3}\\log 9§: 3 pangkat berapa = 9? §3^{2} = 9§, jadi 2.",
                        "§3 + 2 = 5§.",
                        answer = "5"),
                ),
                pitfalls = listOf(
                    "Mengalikan pangkat saat bilangannya dikalikan §(2^{3} \\times 2^{2} \\neq 2^{6})§.",
                    "Mengira §\\log (x + y) = \\log x + \\log y§. Itu hanya berlaku untuk perkalian.",
                ),
                points = listOf(
                    "Perkalian: pangkat dijumlah. Pembagian: pangkat dikurang. Dipangkatkan lagi: pangkat dikali.",
                    "§^{a}\\log b = c§ artinya §a^{c} = b§.",
                ),
            ),
            Section(
                title = "Persamaan, SPLDV & fungsi",
                explain = listOf(
                    "Sistem persamaan linear dua variabel (SPLDV) adalah dua persamaan dengan dua bilangan yang belum diketahui, misalnya x dan y. Cara paling cepat biasanya ELIMINASI: jumlahkan atau kurangkan kedua persamaan supaya salah satu variabel hilang, lalu SUBSTITUSI hasilnya ke persamaan awal.",
                    "Persamaan kuadrat §ax^{2} + bx + c = 0§ diselesaikan dengan memfaktorkan: cari dua bilangan yang jika dikali = c dan jika dijumlah = b. Hubungan akar: jumlah akar = §- \\frac{b}{a}§ dan hasil kali akar = §\\frac{c}{a}§.",
                    "Grafik fungsi kuadrat §f(x) = ax^{2} + bx + c§ berbentuk parabola. Titik puncaknya ada di §x = - \\frac{b}{2a}§. Kalau a positif, parabola terbuka ke atas sehingga puncaknya adalah nilai MINIMUM.",
                    "Fungsi komposisi (f∘g)(x) artinya f(g(x)): kerjakan g dulu, hasilnya dimasukkan ke f. Fungsi invers membalik fungsi: kalau f mengubah 5 menjadi 9, maka §f^{-1}§ mengubah 9 kembali menjadi 5.",
                ),
                formulas = listOf(
                    "Akar kuadrat: §x_1 + x_2 = -\\dfrac{b}{a}, \\quad x_1 \\cdot x_2 = \\dfrac{c}{a}§",
                    "Puncak parabola: §x_p = -\\dfrac{b}{2a}§",
                    "§(f \\circ g)(x) = f(g(x))§",
                    "§f(x) = ax + b \\;\\Rightarrow\\; f^{-1}(x) = \\dfrac{x - b}{a}§",
                ),
                examples = listOf(
                    ex("§x + y = 10§ dan §x - y = 4§. Tentukan 2x + y.",
                        "Jumlahkan kedua persamaan: §(x + y) + (x - y) = 10 + 4 \\to 2x = 14 \\to x = 7§.",
                        "Substitusi ke §x + y = 10§: §7 + y = 10 \\to y = 3§.",
                        "§2x + y = 14 + 3 = 17§.",
                        answer = "17"),
                    ex("§f(x) = 2x^{2} - 4x + 1§. Pada x berapa nilai minimumnya?",
                        "§a = 2§, §b = - 4§.",
                        "x puncak = §- \\frac{b}{2a} = - \\frac{-4}{2\\cdot2} = \\frac{4}{4} = 1§.",
                        "Karena §a = 2 > 0§, parabola terbuka ke atas, jadi titik ini minimum.",
                        answer = "§x = 1§"),
                    ex("§f(x) = 2x - 3§, §g(x) = x^{2} + 1§. Hitung (f∘g)(2).",
                        "Dalam dulu: §g(2) = 2^{2} + 1 = 5§.",
                        "Masukkan ke f: §f(5) = 2 \\cdot 5 - 3 = 7§.",
                        answer = "7"),
                ),
                pitfalls = listOf(
                    "Mengerjakan (f∘g) dengan f dulu. Yang ditulis paling kanan dikerjakan pertama.",
                    "Lupa tanda minus pada §- \\frac{b}{2a}§ ketika b negatif.",
                ),
                points = listOf(
                    "SPLDV: eliminasi lalu substitusi.",
                    "Puncak parabola di §x = - \\frac{b}{2a}§; §a > 0§ berarti minimum.",
                    "§(f \\circ g)(x) = f(g(x))§, kerjakan g dulu.",
                ),
            ),
        ),

        // ======================= FISIKA: KINEMATIKA =======================
        "fis1" to listOf(
            Section(
                title = "Pengukuran & angka penting",
                explain = listOf(
                    "Jangka sorong punya dua skala: skala utama (dalam cm) dan skala nonius yang bisa bergeser. Skala nonius dibuat sedemikian rupa sehingga ketelitiannya 0,1 mm = 0,01 cm. Cara membaca: lihat angka skala utama tepat sebelum garis nol nonius, lalu cari garis nonius yang paling lurus (berimpit) dengan garis skala utama.",
                    "Mikrometer sekrup lebih teliti, yaitu 0,01 mm. Bacaannya = skala utama (mm) + angka skala putar × 0,01 mm.",
                    "Angka penting menunjukkan seberapa teliti hasil ukur. Aturannya: semua angka bukan nol penting; nol di antara angka bukan nol penting; nol di depan (seperti 0,00…) TIDAK penting karena hanya menunjukkan letak koma; nol di belakang koma desimal penting karena menunjukkan ketelitian.",
                ),
                formulas = listOf(
                    "Jangka sorong: §\\text{hasil} = \\text{SU} + n \\times 0{,}01 \\text{ cm}§",
                    "Mikrometer: §\\text{hasil} = \\text{SU} + \\text{SP} \\times 0{,}01 \\text{ mm}§",
                    "Kali/bagi: hasil mengikuti angka penting paling sedikit",
                ),
                examples = listOf(
                    ex("Skala utama jangka sorong 2,3 cm dan garis nonius ke-4 berimpit. Berapa hasilnya?",
                        "Bagian nonius = §4 \\times 0{,}01§ cm = 0,04 cm.",
                        "Hasil = §2{,}3 + 0{,}04 = 2{,}34§ cm.",
                        answer = "2,34 cm"),
                    ex("Berapa angka penting pada 0,0450 kg?",
                        "Nol di depan (0,0) tidak penting.",
                        "Angka 4 dan 5 penting.",
                        "Nol terakhir ada di belakang koma setelah angka bukan nol, jadi penting.",
                        answer = "3 angka penting"),
                ),
                pitfalls = listOf("Menulis §2{,}3 + 4 \\times 0{,}1 = 2{,}7§. Ketelitian jangka sorong 0,01 cm, bukan 0,1 cm."),
                points = listOf(
                    "Jangka sorong teliti 0,01 cm; mikrometer 0,01 mm.",
                    "Nol di depan tidak penting; nol di belakang koma penting.",
                ),
            ),
            Section(
                title = "Gerak lurus: GLB & GLBB",
                explain = listOf(
                    "Gerak Lurus Beraturan (GLB) adalah gerak dengan kecepatan tetap. Karena kecepatannya tidak berubah, jarak cukup dihitung dengan §s = v \\times t§.",
                    "Gerak Lurus Berubah Beraturan (GLBB) adalah gerak dengan PERCEPATAN tetap. Percepatan a artinya kecepatan bertambah §\\frac{am}{s}§ setiap detik. Jadi setelah t detik, kecepatan menjadi §v = v_{0} + a \\cdot t§.",
                    "Jarak GLBB berasal dari kecepatan rata-ratanya. Kecepatan rata-rata = §\\frac{v_{0}+v}{2}§, dikali waktu, lalu disederhanakan menjadi §s = v_{0}t + \\tfrac{1}{2}at^{2}§. Rumus ketiga §v^{2} = v_{0}^{2} + 2as§ dipakai kalau waktunya tidak diketahui.",
                    "Kalau benda direm, percepatannya negatif (perlambatan). Benda dilempar ke atas juga GLBB dengan §a = - g§; di titik tertinggi kecepatannya 0.",
                ),
                formulas = listOf(
                    "GLB: §s = v \\cdot t§",
                    "GLBB: §v = v_0 + at \\qquad s = v_0 t + \\tfrac{1}{2} a t^2 \\qquad v^2 = v_0^2 + 2as§",
                    "km/jam ke m/s: §v_{\\text{m/s}} = \\dfrac{v_{\\text{km/jam}}}{3{,}6}§",
                    "Lempar ke atas: §t_{\\text{puncak}} = \\dfrac{v_0}{g}§",
                ),
                examples = listOf(
                    ex("Mobil diam lalu dipercepat §2\\,\\mathrm{m} / \\mathrm{s}^{2}§ selama 5 s. Berapa jarak yang ditempuh?",
                        "Diketahui §v_{0} = 0§, §a = 2\\,\\mathrm{m} / \\mathrm{s}^{2}§, §t = 5\\,\\mathrm{s}§.",
                        "§s = v_{0}t + \\tfrac{1}{2}at^{2} = 0 + \\tfrac{1}{2} \\cdot 2 \\cdot 5^{2}§.",
                        "= §1 \\cdot 25 = 25\\,\\mathrm{m}§.",
                        answer = "25 m"),
                    ex("Mobil melaju §72\\,\\mathrm{km} / \\mathrm{jam}§, direm sampai berhenti dalam 4 s. Berapa perlambatannya?",
                        "Ubah satuan: §72 \\div 3{,}6 = 20\\,\\mathrm{m} / \\mathrm{s}§.",
                        "Berhenti, jadi §v = 0§. Pakai §v = v_{0} + at§: §0 = 20 + a \\cdot 4§.",
                        "§a = - \\frac{20}{4} = - 5\\,\\mathrm{m} / \\mathrm{s}^{2}§. Tanda minus berarti perlambatan.",
                        answer = "§5\\,\\mathrm{m} / \\mathrm{s}^{2}§"),
                ),
                pitfalls = listOf("Lupa mengubah km/jam ke m/s.", "Memakai rumus GLB untuk benda yang dipercepat."),
                points = listOf(
                    "GLB: v tetap, §s = vt§.",
                    "GLBB: a tetap; §v = v_{0} + at§; §s = v_{0}t + \\tfrac{1}{2}at^{2}§; §v^{2} = v_{0}^{2} + 2as§.",
                ),
            ),
            Section(
                title = "Gerak parabola",
                explain = listOf(
                    "Bola yang ditendang miring bergerak melengkung karena ia melakukan DUA gerak sekaligus. Arah mendatar (sumbu x) tidak ada gaya, jadi geraknya GLB. Arah tegak (sumbu y) ditarik gravitasi, jadi geraknya GLBB seperti benda dilempar ke atas.",
                    "Karena itu, langkah pertama selalu MENGURAIKAN kecepatan awal: §v_{x} = v_{0}\\cos \\theta§ dan vᵧ = §v_{0}\\sin \\theta§. Setelah itu tiap arah dikerjakan terpisah.",
                    "Di titik tertinggi, kecepatan tegak = 0 (bola berhenti naik), tetapi kecepatan mendatar tetap §v_{x}§. Dari sini didapat waktu ke puncak dan tinggi maksimum.",
                ),
                formulas = listOf(
                    "§v_x = v_0 \\cos\\theta \\qquad v_y = v_0 \\sin\\theta§",
                    "§t_{\\text{puncak}} = \\dfrac{v_0 \\sin\\theta}{g}§",
                    "§h_{\\text{maks}} = \\dfrac{(v_0 \\sin\\theta)^2}{2g}§",
                    "Jarak terjauh: §R = \\dfrac{v_0^2 \\sin 2\\theta}{g}§ (paling jauh di §\\theta = 45^\\circ§)",
                ),
                examples = listOf(
                    ex("Bola ditendang §20\\,\\mathrm{m} / \\mathrm{s}§ dengan sudut 30°. Berapa tinggi maksimumnya? §(g = 10\\,\\mathrm{m} / \\mathrm{s}^{2})§",
                        "Uraikan kecepatan tegak: vᵧ = §20 \\times \\sin 30^\\circ = 20 \\times \\tfrac{1}{2} = 10\\,\\mathrm{m} / \\mathrm{s}§.",
                        "h maks = vᵧ§\\frac{^{2}}{2g} = \\frac{10^{2}}{20}§.",
                        "= §\\frac{100}{20} = 5\\,\\mathrm{m}§.",
                        answer = "5 m"),
                ),
                pitfalls = listOf("Memakai cos untuk arah tegak. Tegak = sin, mendatar = cos (kalau §\\theta§ diukur dari tanah)."),
                points = listOf(
                    "Mendatar GLB (§v_{x}§ tetap), tegak GLBB §(a = - g)§.",
                    "Di puncak vᵧ = 0.",
                ),
            ),
            Section(
                title = "Gerak melingkar beraturan",
                explain = listOf(
                    "Pada gerak melingkar beraturan, besar kecepatan tetap tetapi ARAHnya terus berubah. Perubahan arah ini membutuhkan percepatan yang selalu menuju pusat lingkaran, disebut percepatan sentripetal.",
                    "Makin cepat benda berputar atau makin kecil lingkarannya, makin besar percepatan sentripetalnya. Itu sebabnya kamu terdorong keras saat motor menikung tajam dengan cepat.",
                    "Kecepatan sudut §\\omega§ menyatakan berapa radian yang ditempuh per detik. Hubungannya dengan kecepatan linear: §v = \\omega \\cdot r§.",
                ),
                formulas = listOf(
                    "§v = \\omega r \\qquad \\omega = 2\\pi f \\qquad T = \\dfrac{1}{f}§",
                    "§a_s = \\dfrac{v^2}{r} = \\omega^2 r§",
                    "Gaya sentripetal: §F_s = m\\dfrac{v^2}{r}§",
                ),
                examples = listOf(
                    ex("Benda bergerak melingkar dengan §r = 0{,}5\\,\\mathrm{m}§ dan §v = 2\\,\\mathrm{m} / \\mathrm{s}§. Berapa percepatan sentripetalnya?",
                        "§a = \\frac{v^{2}}{r} = \\frac{2^{2}}{0{,}5}§.",
                        "= §\\frac{4}{0{,}5} = 8\\,\\mathrm{m} / \\mathrm{s}^{2}§.",
                        answer = "§8\\,\\mathrm{m} / \\mathrm{s}^{2}§"),
                ),
                pitfalls = listOf("Lupa mengkuadratkan v."),
                points = listOf("Percepatan sentripetal menuju pusat: §a = \\frac{v^{2}}{r} = \\omega ^{2}r§.", "§v = \\omega r§."),
            ),
        ),

        // ======================= B. INDONESIA: TEKS INFORMASI =======================
        "bin1" to listOf(
            Section(
                title = "Menemukan ide pokok",
                explain = listOf(
                    "Setiap paragraf yang baik hanya membicarakan SATU gagasan utama, disebut ide pokok. Ide pokok biasanya dinyatakan dalam satu kalimat utama, sedangkan kalimat lain (kalimat penjelas) memberi alasan, contoh, data, atau rincian.",
                    "Letak kalimat utama menentukan jenis paragraf. Deduktif: kalimat utama di awal, lalu dijelaskan. Induktif: rincian dulu, lalu disimpulkan di akhir. Campuran: di awal dan diulang di akhir.",
                    "Cara menguji: kalimat utama bersifat UMUM dan bisa \"memayungi\" kalimat lain. Kalau sebuah kalimat hanya menyebut satu contoh atau satu angka, itu kalimat penjelas.",
                    "Di soal ujian, teks berpanjang 250–300 kata dan sering berisi beberapa paragraf. Ide pokok seluruh teks didapat dengan menggabungkan ide pokok tiap paragraf.",
                ),
                examples = listOf(
                    ex("\"Membaca setiap hari memberi banyak manfaat. Kosakata bertambah, kemampuan berpikir kritis terasa lebih tajam, dan konsentrasi meningkat. Selain itu, membaca dapat menurunkan stres.\" Apa ide pokoknya?",
                        "Kalimat 1 bersifat umum: \"memberi banyak manfaat\".",
                        "Kalimat 2 dan 3 merinci manfaat apa saja, jadi kalimat penjelas.",
                        "Jadi paragraf deduktif dengan ide pokok di kalimat pertama.",
                        answer = "Manfaat membaca setiap hari"),
                ),
                pitfalls = listOf("Memilih detail yang menarik (misalnya \"menurunkan stres\") sebagai ide pokok."),
                points = listOf(
                    "Ide pokok = gagasan umum yang memayungi kalimat lain.",
                    "Deduktif di awal, induktif di akhir, campuran di keduanya.",
                ),
            ),
            Section(
                title = "Simpulan & informasi tersirat",
                explain = listOf(
                    "Simpulan adalah intisari SELURUH teks. Simpulan yang benar mencakup masalah utama dan hal penting yang dibahas, tidak hanya satu detail, dan tidak menambahkan informasi yang tidak ada di teks.",
                    "Informasi tersirat (inferensi) adalah hal yang tidak ditulis langsung tetapi pasti benar berdasarkan teks. Misalnya teks bilang \"warga lebih sering berolahraga sejak taman dibuka\". Tersiratnya: taman berdampak baik bagi kesehatan warga.",
                    "Strategi menjawab: coret dulu pilihan yang BERTENTANGAN dengan teks, lalu pilihan yang TERLALU SEMPIT (hanya satu detail) atau TERLALU LUAS (berlebihan seperti \"semua\", \"pasti\", \"tidak pernah\").",
                ),
                examples = listOf(
                    ex("Teks: sampah plastik di sungai naik 40% sejak 2021; program bank sampah terbukti mengurangi sampah hingga seperempatnya. Simpulan yang tepat?",
                        "Masalah utama: sampah plastik terus naik.",
                        "Solusi yang didukung data: bank sampah.",
                        "Gabungkan keduanya, tanpa menambah klaim baru.",
                        answer = "Bank sampah dapat menjadi solusi untuk menekan sampah plastik yang terus meningkat."),
                ),
                pitfalls = listOf("Memilih kalimat yang benar menurut pengetahuan umum tetapi tidak ada di teks."),
                points = listOf(
                    "Simpulan mencakup seluruh isi, tidak menambah info.",
                    "Hindari pilihan yang sempit, ekstrem, atau bertentangan.",
                ),
            ),
            Section(
                title = "Fakta dan opini",
                explain = listOf(
                    "Fakta adalah informasi yang bisa dibuktikan kebenarannya, misalnya dengan data, pengukuran, atau dokumen. Ciri-cirinya sering memuat angka, tanggal, tempat, atau hasil penelitian.",
                    "Opini adalah pendapat atau penilaian seseorang. Orang lain bisa tidak setuju. Penanda opini: menurut saya, sebaiknya, seharusnya, mungkin, kiranya, terlalu, sangat bagus, paling indah, kurang tepat.",
                    "Hati-hati: kalimat berisi angka belum tentu fakta kalau disertai penilaian, misalnya \"Kenaikan 40% itu sangat memalukan\".",
                ),
                examples = listOf(
                    ex("Mana yang opini? (A) Volume sampah naik 40% sejak 2021. (B) Menurut saya, pemerintah terlalu lambat menangani masalah ini.",
                        "A berisi data yang bisa dicek ke dinas lingkungan, jadi fakta.",
                        "B memakai \"menurut saya\" dan penilaian \"terlalu lambat\", jadi opini.",
                        answer = "B"),
                ),
                points = listOf("Fakta bisa dibuktikan; opini berisi penilaian.", "Cari kata penanda opini."),
            ),
            Section(
                title = "Konjungsi: pertentangan & sebab-akibat",
                explain = listOf(
                    "Soal membaca sering memakai wacana dengan konjungsi pertentangan atau sebab-akibat. Konjungsi adalah kata penghubung yang memberi tahu hubungan antara dua gagasan.",
                    "Pertentangan menunjukkan dua hal yang berlawanan: namun, tetapi, padahal, sedangkan, meskipun, walaupun. Sebab-akibat menunjukkan alasan dan hasil: karena, sebab (sebab); sehingga, akibatnya, oleh karena itu, maka (akibat).",
                    "Konjungsi sering menjadi kunci jawaban. Kalimat setelah \"namun\" biasanya berisi gagasan yang ingin ditekankan penulis.",
                ),
                examples = listOf(
                    ex("\"Hujan turun sangat deras sejak pagi, ... pasar tetap ramai pembeli.\" Konjungsi yang tepat?",
                        "Bagian 1: hujan deras (biasanya membuat pasar sepi).",
                        "Bagian 2: pasar tetap ramai, jadi berlawanan dengan dugaan.",
                        "Hubungannya pertentangan.",
                        answer = "namun"),
                ),
                points = listOf("Pertentangan: namun, tetapi, padahal.", "Sebab-akibat: karena, sehingga, oleh karena itu."),
            ),
        ),

        // ======================= FISIKA: DINAMIKA =======================
        "fis2" to listOf(
            Section(
                title = "Hukum Newton & jenis gaya",
                explain = listOf(
                    "Hukum I Newton: benda yang diam tetap diam, dan benda yang bergerak lurus beraturan tetap begitu, selama resultan gaya padanya nol. Ini sebabnya badanmu terdorong ke depan saat bus mengerem mendadak (kelembaman).",
                    "Hukum II Newton: resultan gaya menghasilkan percepatan, §\\Sigma F = m \\cdot a§. Makin besar gaya, makin besar percepatan; makin berat benda, makin kecil percepatannya. Yang dihitung hanya komponen gaya yang SEARAH gerak. Gaya miring §\\theta§ terhadap lantai hanya menyumbang §F\\cos \\theta§ ke arah mendatar.",
                    "Hukum III Newton: setiap aksi punya reaksi yang sama besar dan berlawanan arah, tetapi bekerja pada benda yang BERBEDA.",
                    "Jenis gaya yang sering muncul: berat §w = mg§ (ke bawah), gaya normal N (tegak lurus permukaan), gaya gesek §f = \\mu N§ (melawan gerak), dan gaya pegas §F = k\\Delta x§.",
                ),
                formulas = listOf(
                    "§\\Sigma F = m \\cdot a§",
                    "§w = mg \\qquad f_k = \\mu N§ (bidang datar: §N = mg§)",
                    "Lift ke atas: §N = m(g + a)§; ke bawah: §N = m(g - a)§",
                    "Pegas: §F = k\\,\\Delta x§",
                ),
                examples = listOf(
                    ex("Balok 5 kg ditarik 20 N mendatar di lantai dengan §\\mu _{k} = 0{,}2§. Berapa percepatannya? §(g = 10)§",
                        "Gaya normal §N = mg = 5 \\times 10 = 50\\,\\mathrm{N}§.",
                        "Gaya gesek §f = \\mu N = 0{,}2 \\times 50 = 10\\,\\mathrm{N}§, arahnya melawan tarikan.",
                        "§\\Sigma F = 20 - 10 = 10\\,\\mathrm{N}§.",
                        "§a = \\frac{\\Sigma F}{m} = \\frac{10}{5} = 2\\,\\mathrm{m} / \\mathrm{s}^{2}§.",
                        answer = "§2\\,\\mathrm{m} / \\mathrm{s}^{2}§"),
                    ex("Balok 4 kg di lantai licin ditarik 20 N dengan sudut 60°. Berapa percepatannya?",
                        "Komponen mendatar: §F\\cos 60^\\circ = 20 \\times 0{,}5 = 10\\,\\mathrm{N}§.",
                        "Lantai licin, jadi tidak ada gesekan.",
                        "§a = \\frac{10}{4} = 2{,}5\\,\\mathrm{m} / \\mathrm{s}^{2}§.",
                        answer = "§2{,}5\\,\\mathrm{m} / \\mathrm{s}^{2}§"),
                ),
                pitfalls = listOf("Memakai 20 N langsung padahal gayanya miring.", "Lupa mengurangi gaya gesek."),
                points = listOf("§\\Sigma F = ma§, pakai komponen searah gerak.", "§f = \\mu N§, §w = mg§, lift naik §N = m(g + a)§."),
            ),
            Section(
                title = "Usaha & energi",
                explain = listOf(
                    "Usaha dilakukan ketika gaya membuat benda berpindah. Hanya gaya yang searah perpindahan yang melakukan usaha, makanya §W = F \\cdot s \\cdot \\cos \\theta§. Mendorong tembok yang tidak bergerak berarti usahanya nol.",
                    "Energi kinetik adalah energi karena gerak, §Ek = \\tfrac{1}{2}mv^{2}§. Perhatikan v dikuadratkan: kecepatan 2 kali berarti energi 4 kali. Energi potensial gravitasi adalah energi karena ketinggian, Ep = mgh.",
                    "Usaha total sama dengan perubahan energi kinetik §(W = \\Delta Ek)§. Kalau tidak ada gesekan, energi mekanik (Ep + Ek) kekal: energi hanya berpindah bentuk, misalnya Ep di atas berubah menjadi Ek di bawah.",
                    "Daya adalah kecepatan melakukan usaha: §P = \\frac{W}{t}§, satuannya watt §(\\frac{J}{s})§.",
                ),
                formulas = listOf(
                    "§W = F s \\cos\\theta§",
                    "§E_k = \\tfrac{1}{2} m v^2 \\qquad E_p = mgh§",
                    "§W = \\Delta E_k \\qquad E_{p1} + E_{k1} = E_{p2} + E_{k2}§",
                    "§P = \\dfrac{W}{t}§",
                ),
                examples = listOf(
                    ex("Bola 0,5 kg jatuh bebas dari 5 m. Berapa Ek saat menyentuh tanah? §(g = 10)§",
                        "Di atas: diam, jadi Ek = 0 dan §Ep = mgh = 0{,}5 \\times 10 \\times 5 = 25\\,\\mathrm{J}§.",
                        "Di tanah: §h = 0§, jadi Ep = 0.",
                        "Energi kekal: semua 25 J berubah menjadi Ek.",
                        answer = "25 J"),
                    ex("Mesin melakukan usaha 3.000 J dalam 1 menit. Berapa dayanya?",
                        "Ubah waktu ke sekon: 1 menit = 60 s.",
                        "§P = \\frac{3000}{60} = 50\\,\\mathrm{W}§.",
                        answer = "50 W"),
                ),
                pitfalls = listOf("Lupa mengubah menit ke sekon.", "Mengira Ek naik 2 kali saat v naik 2 kali (sebenarnya 4 kali)."),
                points = listOf("§W = Fs\\cos \\theta§; §Ek = \\tfrac{1}{2}mv^{2}§; Ep = mgh.", "Tanpa gesekan: Ep + Ek kekal.", "§P = \\frac{W}{t}§."),
            ),
            Section(
                title = "Momentum & impuls",
                explain = listOf(
                    "Momentum §p = m \\cdot v§ menggambarkan \"seberapa susah menghentikan\" benda. Truk lambat bisa punya momentum lebih besar dari motor cepat karena massanya jauh lebih besar. Momentum adalah VEKTOR, jadi arah penting.",
                    "Impuls adalah gaya yang bekerja dalam selang waktu singkat: §I = F \\cdot \\Delta t§. Impuls sama dengan perubahan momentum: §I = m(v_{2} - v_{1})§. Kalau bola berbalik arah, salah satu kecepatan harus diberi tanda negatif.",
                    "Hukum kekekalan momentum: pada tumbukan tanpa gaya luar, momentum total sebelum = sesudah. Kalau dua benda menempel setelah tumbukan (tidak lenting sama sekali), keduanya bergerak dengan satu kecepatan bersama.",
                    "Jenis tumbukan berdasarkan koefisien restitusi e: lenting sempurna (§e = 1§, energi kinetik kekal), lenting sebagian §(0 < e < 1)§, tidak lenting sama sekali (§e = 0§, benda menempel).",
                ),
                formulas = listOf(
                    "§p = m v§",
                    "§I = F\\,\\Delta t = \\Delta p = m(v_2 - v_1)§",
                    "§m_1 v_1 + m_2 v_2 = m_1 v_1' + m_2 v_2'§",
                    "Menempel: §m_1 v_1 + m_2 v_2 = (m_1 + m_2)\\, v'§",
                ),
                examples = listOf(
                    ex("Benda 2 kg §(6\\,\\mathrm{m} / \\mathrm{s})§ menumbuk benda 1 kg yang diam, lalu menempel. Berapa kecepatan akhirnya?",
                        "Momentum awal: §2 \\times 6 + 1 \\times 0 = 12\\,\\mathrm{kg} \\cdot \\frac{m}{s}§.",
                        "Setelah menempel massanya 3 kg.",
                        "§3 \\times v' = 12 \\to v' = 4\\,\\mathrm{m} / \\mathrm{s}§.",
                        answer = "§4\\,\\mathrm{m} / \\mathrm{s}§"),
                    ex("Bola 0,2 kg datang §10\\,\\mathrm{m} / \\mathrm{s}§ lalu dipukul balik §20\\,\\mathrm{m} / \\mathrm{s}§. Berapa impulsnya?",
                        "Pilih arah pukulan sebagai positif: §v_{2} = + 20§, §v_{1} = - 10§ (berlawanan).",
                        "§I = m(v_{2} - v_{1}) = 0{,}2 \\times (20 - (- 10))§.",
                        "= §0{,}2 \\times 30 = 6\\,\\mathrm{N} \\cdot s§.",
                        answer = "§6\\,\\mathrm{N} \\cdot s§"),
                ),
                pitfalls = listOf("Lupa tanda negatif saat benda berbalik arah (hasilnya jadi §2\\,\\mathrm{N} \\cdot s§, salah)."),
                points = listOf("§p = mv§; §I = F\\Delta t = \\Delta p§.", "Momentum total kekal; menempel → §(m_{1} + m_{2})v'§."),
            ),
            Section(
                title = "Dinamika rotasi",
                explain = listOf(
                    "Gaya membuat benda bergerak lurus; TORSI membuat benda berputar. Torsi bergantung pada besar gaya dan jaraknya dari sumbu putar (lengan gaya). Itu sebabnya membuka baut lebih mudah dengan kunci yang panjang.",
                    "Kalau gaya tegak lurus lengan, §\\tau = F \\cdot r§. Kalau miring, hanya komponen tegak lurus yang dipakai: §\\tau = F \\cdot r \\cdot \\sin \\theta§.",
                    "Momen inersia I adalah \"kelembaman\" untuk rotasi, bergantung pada massa dan seberapa jauh massa itu dari sumbu. Hukum II Newton versi rotasi: §\\tau = I \\cdot \\alpha§.",
                ),
                formulas = listOf("§\\tau = F r \\sin\\theta§", "Partikel: §I = m r^2§", "§\\Sigma\\tau = I \\alpha§"),
                examples = listOf(
                    ex("Kunci pas 25 cm diberi gaya 20 N tegak lurus di ujungnya. Berapa torsinya?",
                        "Ubah satuan: 25 cm = 0,25 m.",
                        "§\\tau = F \\cdot r = 20 \\times 0{,}25 = 5\\,\\mathrm{N} \\cdot m§.",
                        answer = "§5\\,\\mathrm{N} \\cdot m§"),
                ),
                pitfalls = listOf("Memakai cm langsung (hasilnya 500, salah)."),
                points = listOf("Torsi §\\tau = F \\cdot r§ (gaya tegak lurus).", "§\\tau = I\\alpha§."),
            ),
        ),

        // ======================= MATEMATIKA: GEOMETRI & TRIGONOMETRI =======================
        "mat2" to listOf(
            Section(
                title = "Teorema Pythagoras",
                explain = listOf(
                    "Pada segitiga siku-siku, kuadrat sisi miring (sisi terpanjang, di depan sudut siku) sama dengan jumlah kuadrat dua sisi lainnya: §c^{2} = a^{2} + b^{2}§.",
                    "Jika persegi digambar pada setiap sisi segitiga, luas persegi pada sisi miring tepat sama dengan jumlah luas dua persegi lainnya.",
                    "Menghafal tripel Pythagoras (3-4-5, 6-8-10, 5-12-13, 8-15-17, 7-24-25) sangat menghemat waktu di ujian.",
                ),
                formulas = listOf("§c^2 = a^2 + b^2§ (c = sisi miring)"),
                examples = listOf(
                    ex("Sisi tegak segitiga siku-siku 6 cm dan 8 cm. Berapa sisi miringnya?",
                        "§c^{2} = 6^{2} + 8^{2} = 36 + 64 = 100§.",
                        "§c = \\sqrt{100} = 10§ cm. (Tripel 6-8-10.)",
                        answer = "10 cm"),
                ),
                points = listOf("§c^{2} = a^{2} + b^{2}§.", "Hafalkan tripel 3-4-5, 5-12-13, 8-15-17."),
            ),
            Section(
                title = "Lingkaran & bangun ruang",
                explain = listOf(
                    "Keliling lingkaran §K = 2\\pi r§ dan luasnya §L = \\pi r^{2}§. Gunakan §\\pi = \\frac{22}{7}§ kalau jari-jari kelipatan 7, dan 3,14 untuk lainnya.",
                    "Volume bangun ruang \"tegak\" (prisma, tabung) = luas alas × tinggi. Tabung alasnya lingkaran, jadi §V = \\pi r^{2}t§. Bangun \"runcing\" (limas, kerucut) volumenya sepertiga bangun tegaknya: kerucut §V = \\tfrac{1}{3}\\pi r^{2}t§. Bola §V = \\frac{4}{3\\pi r^{3}}§.",
                ),
                formulas = listOf("§K = 2\\pi r \\qquad L = \\pi r^2§", "Tabung §V = \\pi r^2 t§ · Kerucut §V = \\tfrac{1}{3}\\pi r^2 t§ · Bola §V = \\tfrac{4}{3}\\pi r^3§"),
                examples = listOf(
                    ex("Keliling lingkaran 44 cm §(\\pi = \\frac{22}{7})§. Berapa luasnya?",
                        "§2\\pi r = 44 \\to 2 \\times \\frac{22}{7} \\times r = 44 \\to r = 44 \\times \\frac{7}{44} = 7§ cm.",
                        "§L = \\frac{22}{7} \\times 7 \\times 7 = 22 \\times 7 = 154\\,\\mathrm{cm}^{2}§.",
                        answer = "§154\\,\\mathrm{cm}^{2}§"),
                    ex("Tabung §r = 7§ cm, §t = 10§ cm. Volumenya? §(\\pi = \\frac{22}{7})§",
                        "§V = \\pi r^{2}t = \\frac{22}{7} \\times 49 \\times 10§.",
                        "= §22 \\times 7 \\times 10 = 1.540\\,\\mathrm{cm}^{3}§.",
                        answer = "§1.540\\,\\mathrm{cm}^{3}§"),
                ),
                points = listOf("Prisma/tabung: luas alas × tinggi.", "Limas/kerucut: §\\tfrac{1}{3}§ × luas alas × tinggi."),
            ),
            Section(
                title = "Kesebangunan & transformasi",
                explain = listOf(
                    "Dua bangun SEBANGUN kalau bentuknya sama tetapi ukurannya bisa berbeda. Sudut-sudut yang bersesuaian sama, dan perbandingan sisi-sisi yang bersesuaian tetap.",
                    "Soal klasik: bayangan. Pada saat yang sama, sinar matahari jatuh dengan sudut yang sama, sehingga benda dan bayangannya membentuk segitiga sebangun. Maka tinggi₁/bayangan₁ = tinggi₂/bayangan₂.",
                    "Transformasi geometri memindahkan titik. Pencerminan terhadap sumbu-x membalik tanda y; terhadap sumbu-y membalik tanda x; terhadap titik asal membalik keduanya; terhadap garis §y = x§ menukar x dan y. Translasi (a, b) menggeser x sejauh a dan y sejauh b.",
                ),
                formulas = listOf(
                    "Sebangun: §\\dfrac{a}{a'} = \\dfrac{b}{b'} = \\dfrac{c}{c'}§",
                    "Cermin sumbu-x: §(x, y) \\to (x, -y)§ · sumbu-y: §(x, y) \\to (-x, y)§",
                    "Titik asal: §(-x, -y)§ · garis §y = x§: §(y, x)§ · Translasi: §(x + a,\\, y + b)§",
                ),
                examples = listOf(
                    ex("Tongkat 1,5 m berbayangan 2 m. Pada saat yang sama, bayangan pohon 8 m. Berapa tinggi pohon?",
                        "Sebangun: §\\frac{1{,}5}{2} = \\frac{t}{8}§.",
                        "§t = 1{,}5 \\times \\frac{8}{2} = 6\\,\\mathrm{m}§.",
                        answer = "6 m"),
                    ex("Titik A(2, 3) dicerminkan terhadap sumbu-x. Bayangannya?",
                        "Cermin sumbu-x: x tetap, y berganti tanda.",
                        "A'(2, −3).",
                        answer = "(2, −3)"),
                ),
                points = listOf("Sebangun: perbandingan sisi tetap.", "Cermin sumbu-x ubah tanda y, sumbu-y ubah tanda x."),
            ),
            Section(
                title = "Trigonometri dasar",
                explain = listOf(
                    "Trigonometri membandingkan sisi-sisi segitiga siku-siku terhadap salah satu sudut lancipnya. Dari sudut itu, ada sisi DEPAN (di seberang sudut), sisi SAMPING (menempel di sudut, bukan miring), dan sisi MIRING.",
                    "sin = depan/miring, cos = samping/miring, tan = depan/samping. Jembatan keledai: \"sindemi cossami tandesa\".",
                    "Nilai sudut istimewa perlu dihafal. Polanya: sin 0°, 30°, 45°, 60°, §90^\\circ = \\tfrac{1}{2}\\sqrt{0}§, §\\tfrac{1}{2}\\sqrt{1}§, §\\tfrac{1}{2}\\sqrt{2}§, §\\tfrac{1}{2}\\sqrt{3}§, §\\tfrac{1}{2}\\sqrt{4}§. Cos adalah urutan kebalikannya.",
                    "Soal kontekstual (tangga, layang-layang, bayangan gedung) selalu dimulai dengan menggambar segitiganya dan menandai sisi depan/samping/miring dari sudut yang diketahui.",
                ),
                formulas = listOf(
                    "§\\sin\\theta = \\dfrac{\\text{de}}{\\text{mi}} \\qquad \\cos\\theta = \\dfrac{\\text{sa}}{\\text{mi}} \\qquad \\tan\\theta = \\dfrac{\\text{de}}{\\text{sa}}§",
                    "§\\sin 30^\\circ = \\tfrac{1}{2} \\qquad \\sin 45^\\circ = \\tfrac{1}{2}\\sqrt{2} \\qquad \\sin 60^\\circ = \\tfrac{1}{2}\\sqrt{3}§",
                    "§\\cos 30^\\circ = \\tfrac{1}{2}\\sqrt{3} \\qquad \\cos 45^\\circ = \\tfrac{1}{2}\\sqrt{2} \\qquad \\cos 60^\\circ = \\tfrac{1}{2} \\qquad \\tan 45^\\circ = 1§",
                    "§\\sin^2 x + \\cos^2 x = 1§",
                ),
                examples = listOf(
                    ex("Tangga 10 m bersandar di dinding, membentuk sudut 30° dengan tanah. Berapa tinggi ujung atas tangga?",
                        "Tangga = sisi miring (10 m). Tinggi dinding = sisi DEPAN sudut 30°.",
                        "Depan dan miring, jadi sin.",
                        "tinggi = §10 \\times \\sin 30^\\circ = 10 \\times \\tfrac{1}{2} = 5\\,\\mathrm{m}§.",
                        answer = "5 m"),
                ),
                pitfalls = listOf("Salah menentukan sisi depan/samping karena tidak menggambar."),
                points = listOf("sindemi cossami tandesa.", "Hafalkan §\\sin / \\cos 30^\\circ§, 45°, 60°."),
            ),
        ),

        // ======================= B. INDONESIA: FIKSI & KOSAKATA =======================
        "bin2" to listOf(
            Section(
                title = "Unsur intrinsik cerita",
                explain = listOf(
                    "Unsur intrinsik adalah unsur yang membangun cerita dari dalam: tema (gagasan dasar), alur (rangkaian peristiwa), tokoh dan penokohan (watak tokoh), latar (tempat, waktu, suasana), sudut pandang (posisi pencerita), dan amanat (pesan moral).",
                    "Teks fiksi di soal ujian sering punya tokoh berkarakter bulat (wataknya berkembang), konflik jamak, serta alur dan sudut pandang campuran. Jadi kamu perlu jeli melihat kilas balik (flashback) dan perubahan watak tokoh.",
                    "Sudut pandang orang pertama memakai \"aku\" atau \"saya\". Pelaku utama kalau \"aku\" adalah tokoh yang mengalami konflik; pelaku sampingan kalau \"aku\" hanya menceritakan tokoh lain. Sudut pandang orang ketiga memakai \"dia\" atau nama tokoh.",
                    "Amanat tidak ditulis langsung. Cari dari AKIBAT perbuatan tokoh: perbuatan yang berakibat buruk berarti jangan ditiru.",
                ),
                examples = listOf(
                    ex("\"Dimas selalu menunda tugas. Malam sebelum ujian ia panik karena belum belajar, dan nilainya jauh dari harapan.\" Amanatnya?",
                        "Perbuatan tokoh: menunda tugas.",
                        "Akibat: panik dan nilai buruk.",
                        "Pesan = jangan lakukan perbuatan yang berakibat buruk itu.",
                        answer = "Jangan menunda-nunda pekerjaan."),
                ),
                points = listOf("Tema, alur, tokoh, latar, sudut pandang, amanat.", "Amanat dicari dari akibat perbuatan tokoh."),
            ),
            Section(
                title = "Majas (gaya bahasa)",
                explain = listOf(
                    "Majas membuat bahasa sastra lebih hidup. Personifikasi memberi sifat manusia pada benda mati: \"angin berbisik\", \"langit menangis\".",
                    "Metafora membandingkan secara langsung tanpa kata pembanding: \"Dia bintang kelas.\" Simile membandingkan dengan kata seperti, bagaikan, laksana: \"Wajahnya bagaikan bulan.\"",
                    "Hiperbola melebih-lebihkan: \"Tangisnya membanjiri bumi.\" Litotes justru merendahkan diri: \"Mampirlah ke gubuk kami\" (padahal rumahnya bagus).",
                ),
                examples = listOf(
                    ex("\"Angin malam membisikkan rahasia kepada dedaunan.\" Majas apa?",
                        "Yang bisa berbisik adalah manusia.",
                        "Di sini angin (benda) melakukannya.",
                        answer = "Personifikasi"),
                ),
                points = listOf("Personifikasi: benda bersifat manusia.", "Metafora langsung; simile pakai seperti/bagaikan; hiperbola berlebihan."),
            ),
            Section(
                title = "Kosakata & kalimat",
                explain = listOf(
                    "Makna denotatif adalah makna sebenarnya (kamus). Makna konotatif adalah makna kias atau tambahan. \"Buah bibir\" secara konotatif berarti hal yang banyak dibicarakan orang, bukan buah sungguhan.",
                    "Kata abstrak tidak bisa ditangkap pancaindra (kejujuran, harapan, keadilan). Istilah teknis adalah kata khusus suatu bidang, misalnya \"fotosintesis\" (biologi) atau \"inflasi\" (ekonomi).",
                    "Kalimat inversi adalah kalimat yang predikatnya mendahului subjek. Biasanya predikat diberi partikel -lah: \"Datanglah tamu itu.\" (P = datanglah, S = tamu itu).",
                ),
                examples = listOf(
                    ex("\"Sejak menang lomba, Rani menjadi buah bibir di kampungnya.\" Makna buah bibir?",
                        "Rani tidak berubah menjadi buah sungguhan, jadi maknanya kias.",
                        "Orang menang lomba biasanya dibicarakan banyak orang.",
                        answer = "Bahan pembicaraan"),
                ),
                points = listOf("Denotatif = sebenarnya; konotatif = kias.", "Inversi: predikat sebelum subjek."),
            ),
        ),

        // ======================= FISIKA: FLUIDA =======================
        "fis3" to listOf(
            Section(
                title = "Tekanan hidrostatis",
                explain = listOf(
                    "Tekanan adalah gaya per satuan luas: §P = \\frac{F}{A}§. Di dalam zat cair, berat air di atas sebuah titik menekan titik itu. Makin dalam, makin banyak air di atasnya, jadi tekanannya makin besar. Itulah sebabnya bendungan dibuat lebih tebal di bagian bawah.",
                    "Tekanan hidrostatis hanya bergantung pada massa jenis zat cair §(\\rho)§, percepatan gravitasi (g), dan kedalaman (h) diukur dari PERMUKAAN. Bentuk wadah tidak berpengaruh.",
                ),
                formulas = listOf("§P_h = \\rho g h§", "Air: §\\rho = 1000 \\text{ kg/m}^3§", "Tekanan total: §P = P_0 + \\rho g h§"),
                examples = listOf(
                    ex("Berapa tekanan hidrostatis pada kedalaman 2 m di air? §(g = 10)§",
                        "Pₕ = §\\rho gh = 1000 \\times 10 \\times 2§.",
                        "= 20.000 Pa.",
                        answer = "20.000 Pa"),
                ),
                pitfalls = listOf("Mengukur h dari dasar wadah. Harus dari permukaan zat cair."),
                points = listOf("Pₕ = §\\rho gh§, h dari permukaan.", "Makin dalam, tekanan makin besar."),
            ),
            Section(
                title = "Hukum Pascal",
                explain = listOf(
                    "Hukum Pascal: tekanan yang diberikan pada zat cair dalam ruang tertutup diteruskan ke segala arah dengan SAMA BESAR.",
                    "Karena tekanannya sama, §\\frac{F_{1}}{A_{1}} = \\frac{F_{2}}{A_{2}}§. Artinya gaya kecil pada penampang kecil bisa menghasilkan gaya besar pada penampang besar. Inilah prinsip dongkrak hidrolik, rem hidrolik, dan kursi dokter gigi.",
                ),
                formulas = listOf("§\\dfrac{F_1}{A_1} = \\dfrac{F_2}{A_2}§"),
                examples = listOf(
                    ex("Dongkrak: penampang kecil §10\\,\\mathrm{cm}^{2}§, besar §200\\,\\mathrm{cm}^{2}§. Gaya 50 N di penampang kecil bisa mengangkat beban berapa?",
                        "§F_{2} = F_{1} \\times \\frac{A_{2}}{A_{1}} = 50 \\times \\frac{200}{10}§.",
                        "= §50 \\times 20 = 1.000\\,\\mathrm{N}§.",
                        "Satuan luas sama-sama §\\mathrm{cm}^{2}§, jadi tidak perlu diubah.",
                        answer = "1.000 N"),
                ),
                points = listOf("Tekanan diteruskan sama besar: §\\frac{F_{1}}{A_{1}} = \\frac{F_{2}}{A_{2}}§."),
            ),
            Section(
                title = "Hukum Archimedes",
                explain = listOf(
                    "Benda yang tercelup di zat cair terasa lebih ringan karena didorong ke atas oleh gaya apung. Archimedes menemukan besarnya gaya apung sama dengan BERAT zat cair yang dipindahkan oleh bagian benda yang tercelup.",
                    "Dari sini muncul tiga keadaan. Terapung: massa jenis benda lebih kecil dari zat cair (kayu di air). Melayang: sama. Tenggelam: lebih besar (batu di air). Kapal besi bisa terapung karena bentuknya berongga sehingga massa jenis rata-ratanya kecil.",
                ),
                formulas = listOf("§F_A = \\rho_{\\text{cair}}\\, g\\, V_{\\text{tercelup}}§", "§w_{\\text{semu}} = w_{\\text{udara}} - F_A§"),
                examples = listOf(
                    ex("Benda §0{,}002\\,\\mathrm{m}^{3}§ tercelup seluruhnya dalam air. Berapa gaya apungnya? §(g = 10)§",
                        "V tercelup = §0{,}002\\,\\mathrm{m}^{3}§ (seluruhnya).",
                        "§F_{a} = 1000 \\times 10 \\times 0{,}002 = 20\\,\\mathrm{N}§.",
                        answer = "20 N"),
                ),
                pitfalls = listOf("Memakai massa jenis benda. Rumus §F_{a}§ memakai massa jenis ZAT CAIR."),
                points = listOf("§F_{a} = \\rho§_cair·§g \\cdot V§_tercelup.", "Terapung/melayang/tenggelam ditentukan perbandingan massa jenis."),
            ),
            Section(
                title = "Fluida dinamis: kontinuitas & Bernoulli",
                explain = listOf(
                    "Air yang mengalir di pipa tidak bisa \"menumpuk\". Volume yang masuk tiap detik (debit) harus sama dengan yang keluar. Akibatnya, di pipa yang menyempit, air harus mengalir lebih cepat. Coba tekan ujung selang: airnya menyembur lebih jauh.",
                    "Prinsip Bernoulli: di tempat aliran fluida lebih CEPAT, tekanannya lebih KECIL. Contoh: udara di atas sayap pesawat mengalir lebih cepat daripada di bawahnya, sehingga tekanan atas lebih kecil dan sayap terdorong ke atas (gaya angkat).",
                    "Penyemprot parfum dan obat nyamuk: saat pompa ditekan, udara mengalir cepat melewati ujung atas pipa kecil. Tekanan di situ turun, sehingga tekanan udara di permukaan cairan mendorong cairan naik lalu tersembur.",
                ),
                formulas = listOf("Debit: §Q = A v = \\dfrac{V}{t}§", "Kontinuitas: §A_1 v_1 = A_2 v_2§", "Bernoulli: §P + \\tfrac{1}{2}\\rho v^2 + \\rho g h = \\text{konstan}§"),
                examples = listOf(
                    ex("Air dari pipa §4\\,\\mathrm{cm}^{2}(v = 2\\,\\mathrm{m} / \\mathrm{s})§ mengalir ke pipa §1\\,\\mathrm{cm}^{2}§. Berapa kecepatannya di pipa kecil?",
                        "§A_{1}v_{1} = A_{2}v_{2} \\to 4 \\times 2 = 1 \\times v_{2}§.",
                        "§v_{2} = 8\\,\\mathrm{m} / \\mathrm{s}§.",
                        "Masuk akal: luas jadi §\\tfrac{1}{4}§, kecepatan jadi 4 kali.",
                        answer = "§8\\,\\mathrm{m} / \\mathrm{s}§"),
                ),
                points = listOf("§A_{1}v_{1} = A_{2}v_{2}§: pipa sempit, jadi aliran cepat.", "Aliran cepat, jadi tekanan kecil (Bernoulli)."),
            ),
        ),

        // ======================= MATEMATIKA: DATA & PELUANG =======================
        "mat3" to listOf(
            Section(
                title = "Statistika deskriptif",
                explain = listOf(
                    "Ukuran pemusatan data ada tiga. Mean (rata-rata) = jumlah semua data ÷ banyak data. Median = nilai tengah setelah data DIURUTKAN; kalau banyak data genap, rata-ratakan dua data tengah. Modus = data yang paling sering muncul.",
                    "Jangkauan mengukur sebaran: data terbesar − data terkecil.",
                    "Soal rata-rata gabungan: jangan merata-ratakan rata-rata. Ubah dulu menjadi JUMLAH nilai (n × rata-rata), baru dihitung.",
                    "Di soal ujian, data sering disajikan dalam tabel atau grafik. Baca judul, satuan, dan skala sumbu sebelum menghitung.",
                ),
                formulas = listOf("§\\bar{x} = \\dfrac{\\Sigma x}{n}§", "Median: urutkan dulu; jika n genap §Me = \\dfrac{x_{n/2} + x_{n/2 + 1}}{2}§", "§J = x_{\\text{maks}} - x_{\\text{min}}§", "§\\text{Total} = n \\times \\bar{x}§"),
                examples = listOf(
                    ex("Rata-rata 5 siswa 70. Setelah 1 siswa baru masuk, rata-rata menjadi 72. Berapa nilai siswa baru?",
                        "Total lama = §5 \\times 70 = 350§.",
                        "Total baru = §6 \\times 72 = 432§.",
                        "Nilai siswa baru = §432 - 350 = 82§.",
                        answer = "82"),
                    ex("Data 6, 7, 7, 8, 9, 9, 9, 10. Tentukan median dan modus.",
                        "Data sudah urut, banyaknya 8 (genap).",
                        "Data ke-4 dan ke-5: 8 dan 9 → median §\\frac{8+9}{2} = 8{,}5§.",
                        "9 muncul tiga kali, jadi modus 9.",
                        answer = "Median 8,5; modus 9"),
                ),
                points = listOf("Median: urutkan dulu.", "Rata-rata gabungan: pakai total nilai."),
            ),
            Section(
                title = "Kaidah pencacahan",
                explain = listOf(
                    "Permutasi dipakai kalau URUTAN PENTING, misalnya memilih ketua dan sekretaris (A ketua–B sekretaris berbeda dengan B ketua–A sekretaris).",
                    "Kombinasi dipakai kalau urutan TIDAK penting, misalnya memilih 2 orang untuk tim (A dan B sama saja dengan B dan A).",
                    "Cara cepat P(n, r): kalikan n turun sebanyak r angka. P(5, 2) = §5 \\times 4 = 20§. Kombinasi = permutasi dibagi r!: C(5, 2) = §20 \\div 2 ! = 10§.",
                ),
                formulas = listOf("§P(n, r) = \\dfrac{n!}{(n - r)!}§", "§C(n, r) = \\dfrac{n!}{r!\\,(n - r)!}§"),
                examples = listOf(
                    ex("Dari 5 calon dipilih ketua dan sekretaris. Berapa susunan?",
                        "Jabatan berbeda, jadi urutan penting, jadi permutasi.",
                        "P(5, 2) = §5 \\times 4 = 20§.",
                        answer = "20"),
                    ex("Dari 5 siswa dipilih 2 untuk ikut lomba. Berapa cara?",
                        "Tidak ada jabatan, jadi kombinasi.",
                        "C(5, 2) = §\\frac{5\\times4}{2\\times1} = 10§.",
                        answer = "10"),
                ),
                points = listOf("Urutan penting, jadi permutasi; tidak penting, jadi kombinasi."),
            ),
            Section(
                title = "Peluang kejadian",
                explain = listOf(
                    "Peluang = banyak hasil yang diinginkan ÷ banyak semua hasil yang mungkin. Nilainya antara 0 (mustahil) dan 1 (pasti).",
                    "Ruang sampel yang sering dipakai: satu dadu 6 hasil, dua dadu §6 \\times 6 = 36§ hasil, satu koin 2 hasil, kartu bridge 52 kartu (4 jenis × 13; ada 4 As, 12 kartu bergambar).",
                    "Frekuensi harapan adalah berapa kali sebuah kejadian diharapkan muncul dalam n percobaan: peluang × n.",
                ),
                formulas = listOf("§P(A) = \\dfrac{n(A)}{n(S)}§", "§F_h = P(A) \\times n§", "§P(A^c) = 1 - P(A)§"),
                examples = listOf(
                    ex("Dua dadu dilempar. Peluang jumlahnya 7?",
                        "§n(S) = 36§.",
                        "Pasangan berjumlah 7: (1,6), (2,5), (3,4), (4,3), (5,2), (6,1) → 6.",
                        "§P = \\frac{6}{36} = \\frac{1}{6}§.",
                        answer = "§\\frac{1}{6}§"),
                ),
                pitfalls = listOf("Lupa bahwa (1,6) dan (6,1) adalah dua hasil berbeda."),
                points = listOf("§P(A) = \\frac{n(A)}{n}(S)§.", "Dua dadu: 36 hasil; kartu: 52."),
            ),
        ),

        // ======================= FISIKA: GELOMBANG =======================
        "fis4" to listOf(
            Section(
                title = "Besaran gelombang",
                explain = listOf(
                    "Gelombang adalah getaran yang merambat sambil membawa ENERGI, bukan membawa materi. Gabus di kolam hanya naik-turun di tempat saat ombak lewat.",
                    "Panjang gelombang §\\lambda§ adalah jarak satu gelombang penuh (satu bukit + satu lembah). Frekuensi f adalah jumlah gelombang per detik. Periode T adalah waktu untuk satu gelombang, jadi §T = \\frac{1}{f}§.",
                    "Cepat rambat §v = \\lambda \\times f§, karena tiap detik ada f gelombang yang lewat dan masing-masing sepanjang §\\lambda§.",
                    "Gelombang transversal arah getarnya tegak lurus arah rambat (tali, cahaya). Gelombang longitudinal arah getarnya searah arah rambat, berupa rapatan dan regangan (bunyi, pegas).",
                ),
                formulas = listOf("§v = \\lambda f§", "§T = \\dfrac{1}{f} = \\dfrac{t}{n} \\qquad f = \\dfrac{n}{t}§"),
                examples = listOf(
                    ex("Terbentuk 20 gelombang dalam 4 s. Berapa periodenya?",
                        "§T = \\frac{t}{n} = \\frac{4}{20} = 0{,}2\\,\\mathrm{s}§.",
                        "(Frekuensinya §f = \\frac{1}{T} = 5§ Hz.)",
                        answer = "0,2 s"),
                ),
                points = listOf("§v = \\lambda f§; §T = \\frac{1}{f}§.", "Bunyi longitudinal, cahaya transversal."),
            ),
            Section(
                title = "Bunyi: intensitas & efek Doppler",
                explain = listOf(
                    "Bunyi adalah gelombang mekanik longitudinal, jadi butuh medium (udara, air, padat) dan tidak bisa merambat di ruang hampa.",
                    "Intensitas bunyi adalah daya per luas. Bunyi menyebar ke segala arah seperti permukaan bola, luasnya §4\\pi r^{2}§. Karena itu intensitas berbanding terbalik dengan KUADRAT jarak: jarak 2 kali, jadi intensitas §\\tfrac{1}{4}§.",
                    "Efek Doppler: frekuensi yang kamu dengar berubah kalau sumber atau pendengar bergerak. Saat ambulans MENDEKAT, gelombang \"terdesak\" sehingga frekuensi terdengar LEBIH TINGGI. Saat MENJAUH, terdengar lebih rendah.",
                ),
                formulas = listOf("§I = \\dfrac{P}{4\\pi r^2} \\qquad \\dfrac{I_1}{I_2} = \\left(\\dfrac{r_2}{r_1}\\right)^2§", "§f_p = \\dfrac{v \\pm v_p}{v \\mp v_s}\\, f_s§ (tanda atas saat saling mendekat)"),
                examples = listOf(
                    ex("Pengendara motor dan ambulans sama-sama bergerak. Kapan sirine terdengar lebih tinggi?",
                        "Frekuensi naik kalau jarak sumber–pendengar makin pendek.",
                        "Itu terjadi saat keduanya saling mendekat.",
                        answer = "Saat saling mendekat"),
                ),
                points = listOf("Jarak 2× → intensitas §\\tfrac{1}{4}§.", "Mendekat, jadi frekuensi naik; menjauh, jadi turun."),
            ),
            Section(
                title = "Cahaya: cermin & lensa",
                explain = listOf(
                    "Cahaya adalah gelombang elektromagnetik, jadi bisa merambat di ruang hampa (cahaya matahari sampai ke bumi).",
                    "Cermin cekung dan lensa cembung bersifat MENGUMPULKAN cahaya, fokusnya positif. Cermin cembung dan lensa cekung MENYEBARKAN cahaya, fokusnya negatif.",
                    "Rumus yang sama dipakai untuk cermin dan lensa: §\\frac{1}{f} = \\frac{1}{s} + \\frac{1}{s'}§. Kalau s' positif, bayangan nyata (bisa ditangkap layar); kalau negatif, maya. Perbesaran §M = | \\frac{s'}{s}§|.",
                    "Kekuatan lensa §P = \\frac{1}{f}§ dengan f dalam meter, satuannya dioptri. Kacamata minus memakai lensa cekung (P negatif), kacamata plus memakai lensa cembung.",
                ),
                formulas = listOf("§\\dfrac{1}{f} = \\dfrac{1}{s} + \\dfrac{1}{s'} \\qquad f = \\dfrac{R}{2}§", "§M = \\left|\\dfrac{s'}{s}\\right|§", "§P = \\dfrac{1}{f_{\\text{(m)}}} = \\dfrac{100}{f_{\\text{(cm)}}}§"),
                examples = listOf(
                    ex("Benda 15 cm di depan cermin cekung §f = 10§ cm. Di mana bayangannya?",
                        "§\\frac{1}{s'} = \\frac{1}{f} - \\frac{1}{s} = \\frac{1}{10} - \\frac{1}{15}§.",
                        "Samakan penyebut: §\\frac{3}{30} - \\frac{2}{30} = \\frac{1}{30}§.",
                        "§s' = 30§ cm, positif, jadi nyata, di depan cermin.",
                        answer = "30 cm di depan cermin"),
                ),
                pitfalls = listOf("Lupa memberi tanda negatif pada fokus cermin cembung/lensa cekung."),
                points = listOf("§\\frac{1}{f} = \\frac{1}{s} + \\frac{1}{s'}§.", "Cekung (cermin) & cembung (lensa) mengumpulkan; f positif."),
            ),
        ),

        // ======================= FISIKA: KALOR & TERMODINAMIKA =======================
        "fis5" to listOf(
            Section(
                title = "Kalor & asas Black",
                explain = listOf(
                    "Kalor adalah energi yang berpindah karena perbedaan suhu, selalu dari benda panas ke benda dingin. Untuk menaikkan suhu, kalor yang dibutuhkan sebanding dengan massa, kalor jenis zat, dan kenaikan suhunya: §Q = m \\cdot c \\cdot \\Delta T§.",
                    "Saat zat berubah wujud (mencair, menguap), suhunya TETAP walaupun terus diberi kalor. Kalornya dipakai untuk mengubah wujud: §Q = m \\cdot L§.",
                    "Asas Black: saat dua zat dicampur, kalor yang dilepas zat panas = kalor yang diterima zat dingin. Kalau zat dan massanya sama, suhu akhir adalah rata-rata suhunya.",
                    "Perpindahan kalor: konduksi (lewat zat padat tanpa perpindahan partikel, misal sendok panas), konveksi (disertai aliran fluida, misal air mendidih), radiasi (tanpa medium, misal panas matahari).",
                ),
                formulas = listOf("§Q = m\\,c\\,\\Delta T§", "§Q = m\\,L§ (perubahan wujud)", "Asas Black: §Q_{\\text{lepas}} = Q_{\\text{terima}}§"),
                examples = listOf(
                    ex("100 g air 80 °C dicampur 100 g air 20 °C. Berapa suhu akhir?",
                        "Q lepas = m c (80 − T); Q terima = m c (T − 20).",
                        "Massa dan c sama, jadi §80 - T = T - 20§.",
                        "§2T = 100 \\to T = 50^\\circ C§.",
                        answer = "50 °C"),
                ),
                points = listOf("§Q = mc\\Delta T§.", "Asas Black: kalor lepas = kalor terima."),
            ),
            Section(
                title = "Pemanasan global",
                explain = listOf(
                    "Bumi menerima cahaya matahari lalu memancarkan kembali panas dalam bentuk radiasi inframerah. Gas rumah kaca (CO₂, metana §CH_{4}§, uap air) menyerap sebagian radiasi inframerah ini dan memancarkannya kembali ke bumi, seperti kaca pada rumah kaca. Inilah efek rumah kaca.",
                    "Efek rumah kaca alami membuat bumi cukup hangat untuk hidup. Masalahnya, pembakaran bahan bakar fosil dan penggundulan hutan menambah gas rumah kaca, sehingga panas yang tertahan berlebihan dan suhu rata-rata bumi naik (pemanasan global).",
                    "Solusinya: mengurangi bahan bakar fosil, beralih ke energi terbarukan (surya, angin, air), hemat listrik, dan menanam pohon.",
                ),
                points = listOf("Gas rumah kaca menahan radiasi inframerah.", "Penyebab utama: bahan bakar fosil & deforestasi."),
            ),
            Section(
                title = "Gas ideal",
                explain = listOf(
                    "Gas terdiri dari partikel yang bergerak acak dan menumbuk dinding wadah. Tumbukan inilah yang kita rasakan sebagai tekanan.",
                    "Hukum Boyle (suhu tetap): kalau volume diperbesar, partikel lebih jarang menumbuk dinding, jadi tekanan turun. §P_{1}V_{1} = P_{2}V_{2}§.",
                    "Hukum Boyle–Gay-Lussac menggabungkan tekanan, volume, dan suhu: §\\frac{P_{1}V_{1}}{T_{1}} = \\frac{P_{2}V_{2}}{T_{2}}§. Suhu WAJIB dalam kelvin §(K = ^\\circ C + 273)§.",
                ),
                formulas = listOf("Boyle: §P_1 V_1 = P_2 V_2§", "§\\dfrac{P_1 V_1}{T_1} = \\dfrac{P_2 V_2}{T_2}§ (T dalam kelvin)", "§PV = nRT§"),
                examples = listOf(
                    ex("Gas 2 atm, 3 L dikembangkan pada suhu tetap menjadi 6 L. Tekanannya?",
                        "Suhu tetap, jadi Boyle.",
                        "§2 \\times 3 = P \\times 6 \\to P = 1§ atm.",
                        answer = "1 atm"),
                ),
                pitfalls = listOf("Memakai °C pada rumus gas."),
                points = listOf("Boyle: PV tetap pada suhu tetap.", "Suhu gas selalu dalam kelvin."),
            ),
            Section(
                title = "Termodinamika & mesin kalor",
                explain = listOf(
                    "Hukum I Termodinamika adalah hukum kekekalan energi untuk gas: kalor yang diterima gas (Q) dipakai untuk menaikkan energi dalam §(\\Delta U)§ dan untuk melakukan usaha (W). Jadi §\\Delta U = Q - W§.",
                    "Perjanjian tanda: Q positif kalau gas menyerap kalor; W positif kalau gas melakukan usaha (mengembang).",
                    "Empat proses: isobarik (tekanan tetap, §W = P\\Delta V§), isokhorik (volume tetap, §W = 0§), isotermik (suhu tetap, §\\Delta U = 0§), adiabatik (tanpa pertukaran kalor, §Q = 0§).",
                    "Mesin kalor mengubah kalor menjadi usaha, tetapi tidak pernah 100%. Mesin paling ideal (Carnot) efisiensinya §\\eta = 1 - T§_rendah/T_tinggi dengan suhu dalam kelvin.",
                ),
                formulas = listOf("§\\Delta U = Q - W§", "Isobarik: §W = P\\,\\Delta V§ (§1 \\text{ L} = 10^{-3} \\text{ m}^3§)", "Carnot: §\\eta = 1 - \\dfrac{T_r}{T_t}§"),
                examples = listOf(
                    ex("Gas menyerap 500 J kalor dan melakukan usaha 200 J. Berapa §\\Delta U§?",
                        "§Q = + 500§ (menyerap), §W = + 200§ (melakukan usaha).",
                        "§\\Delta U = 500 - 200 = 300\\,\\mathrm{J}§.",
                        answer = "300 J"),
                    ex("Gas §2 \\times 10^{5}§ Pa mengembang dari 2 L ke 5 L pada tekanan tetap. Usahanya?",
                        "§\\Delta V = 3\\,\\mathrm{L} = 3 \\times 10^{-3}m^{3}§.",
                        "§W = P\\Delta V = 2 \\times 10^{5} \\times 3 \\times 10^{-3} = 600\\,\\mathrm{J}§.",
                        answer = "600 J"),
                ),
                points = listOf("§\\Delta U = Q - W§.", "Isobarik §W = P\\Delta V§; Carnot §\\eta = 1 - \\frac{Tr}{Tt}§."),
            ),
        ),

        // ======================= FISIKA: KELISTRIKAN =======================
        "fis6" to listOf(
            Section(
                title = "Listrik statis: hukum Coulomb",
                explain = listOf(
                    "Muatan sejenis tolak-menolak, berlawanan jenis tarik-menarik. Besar gayanya dirumuskan Coulomb: sebanding dengan hasil kali kedua muatan dan berbanding terbalik dengan KUADRAT jaraknya.",
                    "Karena jarak dikuadratkan, menjauhkan muatan 2 kali membuat gayanya menjadi §\\tfrac{1}{4}§, bukan §\\tfrac{1}{2}§.",
                    "Medan listrik E adalah gaya per satuan muatan di suatu titik. Di antara dua pelat sejajar bertegangan V dan berjarak d, medannya seragam: §E = \\frac{V}{d}§. Gaya pada muatan q di situ F = qE. Prinsip ini dipakai pada mesin fotokopi dan printer laser.",
                ),
                formulas = listOf("§F = k\\dfrac{q_1 q_2}{r^2}§ (§k = 9 \\times 10^9 \\text{ N m}^2/\\text{C}^2§)", "§E = \\dfrac{F}{q} = k\\dfrac{q}{r^2}§", "Pelat sejajar: §E = \\dfrac{V}{d}, \\quad F = \\dfrac{qV}{d}§", "§1\\,\\mu\\text{C} = 10^{-6} \\text{ C}§"),
                examples = listOf(
                    ex("Dua muatan 2 μC berjarak 30 cm. Berapa gaya Coulomb-nya?",
                        "§q = 2 \\times 10^{-6}C§, §r = 0{,}3\\,\\mathrm{m}§.",
                        "§F = 9 \\times 10^{9} \\times (2 \\times 10^{-6})\\frac{^{2}}{0{,}3^{2}} = 9 \\times 10^{9} \\times 4 \\times \\frac{10^{-12}}{0{,}09}§.",
                        "= §\\frac{0{,}036}{0{,}09} = 0{,}4\\,\\mathrm{N}§.",
                        answer = "0,4 N"),
                    ex("Partikel §10^{-7}C§ di antara pelat berjarak 2 mm bertegangan 200 V. Gayanya?",
                        "§E = \\frac{V}{d} = \\frac{200}{0{,}002} = \\frac{10^{5}V}{m}§.",
                        "F = qE = §10^{-7} \\times 10^{5} = 0{,}01\\,\\mathrm{N}§.",
                        "Kalau jarak diperkecil, E dan F makin besar.",
                        answer = "0,01 N"),
                ),
                pitfalls = listOf("Lupa mengubah μC dan cm ke satuan SI.", "Tidak mengkuadratkan jarak."),
                points = listOf("§F = \\frac{kq_{1}q_{2}}{r^{2}}§; jarak 2× → gaya §\\tfrac{1}{4}§.", "Pelat sejajar: §E = \\frac{V}{d}§, F = qE."),
            ),
            Section(
                title = "Rangkaian arus searah",
                explain = listOf(
                    "Hukum Ohm: arus I yang mengalir sebanding dengan tegangan V dan berbanding terbalik dengan hambatan R, jadi §V = I \\cdot R§.",
                    "Rangkaian SERI: komponen disusun berderet, arus hanya punya satu jalan sehingga arusnya sama di semua resistor, dan hambatan totalnya dijumlah. Rangkaian PARALEL: komponen disusun bercabang, tegangannya sama di tiap cabang, dan arus terbagi. Cabang dengan hambatan kecil dilewati arus lebih besar.",
                    "Hukum Kirchhoff I: jumlah arus masuk titik cabang = jumlah arus keluar (muatan tidak hilang). Hukum Kirchhoff II: jumlah tegangan dalam satu loop tertutup = 0 (energi kekal).",
                    "Daya listrik §P = V \\cdot I§, dan energi §W = P \\cdot t§. Tagihan listrik dihitung dalam kWh: watt × jam §\\div 1000§.",
                ),
                formulas = listOf(
                    "§V = I R§",
                    "Seri: §R_s = R_1 + R_2 + \\cdots§",
                    "Paralel: §\\dfrac{1}{R_p} = \\dfrac{1}{R_1} + \\dfrac{1}{R_2}§; dua resistor: §R_p = \\dfrac{R_1 R_2}{R_1 + R_2}§",
                    "§P = V I = I^2 R = \\dfrac{V^2}{R} \\qquad W = P\\,t§",
                ),
                examples = listOf(
                    ex("3 Ω dan 6 Ω paralel, lalu diseri dengan 2 Ω dan baterai 12 V. Berapa arus di resistor 3 Ω?",
                        "Paralel: §\\frac{3\\times6}{3+6} = \\frac{18}{9} = 2\\Omega§.",
                        "Total: §2 + 2 = 4\\Omega§. Arus total §I = \\frac{12}{4} = 3\\,\\mathrm{A}§.",
                        "Tegangan di 2 Ω seri = §3 \\times 2 = 6\\,\\mathrm{V}§, sisa §12 - 6 = 6\\,\\mathrm{V}§ untuk bagian paralel.",
                        "Arus di §3\\Omega = \\frac{6}{3} = 2\\,\\mathrm{A}§ (dan di §6\\Omega = 1\\,\\mathrm{A}§).",
                        answer = "2 A"),
                    ex("Lampu 60 W menyala 5 jam. Energinya dalam kWh?",
                        "§W = 60 \\times 5 = 300§ Wh.",
                        "§300 \\div 1000 = 0{,}3§ kWh.",
                        answer = "0,3 kWh"),
                ),
                points = listOf("Seri: arus sama, R dijumlah.", "Paralel: tegangan sama, arus terbagi.", "§P = VI§; kWh = W × jam §\\div 1000§."),
            ),
        ),
    )
}
