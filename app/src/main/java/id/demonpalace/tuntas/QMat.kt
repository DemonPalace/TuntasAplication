package id.demonpalace.tuntas

/**
 * Chapter quizzes for the Matematika book, written in the style of the TKA SMA practice sets
 * listed in [Content.sources] (detik, Blog ITERA, Kompeten). Every key was recomputed.
 */
object QMat {
    private fun q(id: String, text: String, options: List<String>, answer: Int, explain: String) =
        Question(id, text, options, answer, explain)

    private fun multi(id: String, text: String, options: List<String>, keys: Set<Int>, explain: String) =
        Question(id, text, options, keys.min(), explain, multi = keys)

    private fun table(id: String, text: String, rows: List<String>, keys: List<Int>, explain: String, cols: List<String> = listOf("Benar", "Salah")) =
        Question(id, text, cols, keys.first(), explain, rows = rows, rowKeys = keys)

    val bilangan: List<Question> by lazy { bilanganQ() }
    val eksponen: List<Question> by lazy { eksponenQ() }
    val linear: List<Question> by lazy { linearQ() }
    val pertidaksamaan: List<Question> by lazy { pertidaksamaanQ() }
    val fungsi: List<Question> by lazy { fungsiQ() }
    val deret: List<Question> by lazy { deretQ() }
    val bidang: List<Question> by lazy { bidangQ() }
    val ruang: List<Question> by lazy { ruangQ() }
    val transformasi: List<Question> by lazy { transformasiQ() }
    val trigonometri: List<Question> by lazy { trigonometriQ() }
    val statistika: List<Question> by lazy { statistikaQ() }
    val peluang: List<Question> by lazy { peluangQ() }

    private fun bilanganQ() = listOf(
        q("mb1", "Bilangan berikut yang termasuk bilangan irasional adalah ....",
            listOf("0,25", "§\\sqrt{16}§", "§\\frac{22}{7}§", "§\\sqrt{7}§", "1,333..."), 3,
            "§\\sqrt{7}§ tidak bisa ditulis sebagai pecahan dan desimalnya tidak berulang. §\\sqrt{16} = 4§, sedangkan 0,25, §\\frac{22}{7}§, dan 1,333... rasional."),
        q("mb2", "Harga beras naik 10%, lalu bulan berikutnya turun 10%. Dibandingkan harga awal, harga akhirnya ....",
            listOf("sama", "naik 1%", "turun 1%", "turun 10%", "naik 10%"), 2,
            "Faktornya §1{,}1 \\times 0{,}9 = 0{,}99§, jadi harga akhir 99% harga awal, turun 1%."),
        q("mb3", "Pada peta berskala 1 : 250.000, jarak dua kota 6 cm. Jarak sebenarnya adalah ....",
            listOf("1,5 km", "15 km", "150 km", "25 km", "60 km"), 1,
            "§6 \\times 250.000 = 1.500.000§ cm = 15 km."),
        q("mb4", "Persediaan pakan cukup untuk 40 ekor ayam selama 15 hari. Jika ditambah 20 ekor ayam, pakan itu habis dalam ....",
            listOf("8 hari", "10 hari", "12 hari", "20 hari", "22 hari"), 1,
            "Perbandingan berbalik nilai: §40 \\times 15 = 60 \\times n§, jadi §n = 10§ hari."),
        q("mb5", "Setelah dipotong pajak 5%, gaji yang diterima Rp4.750.000. Gaji sebelum pajak adalah ....",
            listOf("Rp4.987.500", "Rp5.000.000", "Rp4.512.500", "Rp5.250.000", "Rp4.800.000"), 1,
            "Gaji bersih = 95% gaji kotor, jadi §4.750.000 \\div 0{,}95 = 5.000.000§."),
        q("mb6", "Hasil dari §\\dfrac{2}{3} + \\dfrac{3}{4} \\div \\dfrac{3}{2}§ adalah ....",
            listOf("§\\frac{17}{12}§", "§\\frac{7}{6}§", "§\\frac{5}{6}§", "§\\frac{13}{12}§", "§\\frac{3}{2}§"), 1,
            "Bagi dulu: §\\frac{3}{4} \\times \\frac{2}{3} = \\frac{1}{2}§. Lalu §\\frac{2}{3} + \\frac{1}{2} = \\frac{7}{6}§."),
        multi("mb7", "Pilih semua pernyataan yang benar.",
            listOf("Setiap bilangan bulat adalah bilangan rasional.", "§\\sqrt{2} \\times \\sqrt{2}§ adalah bilangan rasional.",
                "Jumlah dua bilangan irasional selalu irasional.", "0 termasuk bilangan cacah.", "§\\pi§ tepat sama dengan §\\frac{22}{7}§."),
            setOf(0, 1, 3),
            "Bilangan bulat n = §\\frac{n}{1}§; §\\sqrt{2} \\times \\sqrt{2} = 2§; cacah dimulai dari 0. Pernyataan C salah karena §\\sqrt{2} + (-\\sqrt{2}) = 0§, dan §\\frac{22}{7}§ hanya pendekatan §\\pi§."),
    )

    private fun eksponenQ() = listOf(
        q("me1", "Nilai §\\dfrac{2^5 \\cdot 2^{-2}}{2^4}§ adalah ....",
            listOf("§\\frac{1}{4}§", "§\\frac{1}{2}§", "1", "2", "8"), 1,
            "Pangkat dijumlah lalu dikurangi: §2^{5 - 2 - 4} = 2^{-1} = \\frac{1}{2}§."),
        q("me2", "Bentuk sederhana §\\sqrt{50} + \\sqrt{18} - \\sqrt{8}§ adalah ....",
            listOf("§4\\sqrt{2}§", "§5\\sqrt{2}§", "§6\\sqrt{2}§", "§8\\sqrt{2}§", "§\\sqrt{60}§"), 2,
            "§5\\sqrt{2} + 3\\sqrt{2} - 2\\sqrt{2} = 6\\sqrt{2}§."),
        q("me3", "Bentuk rasional dari §\\dfrac{6}{\\sqrt{5} - \\sqrt{2}}§ adalah ....",
            listOf("§2\\sqrt{5} - 2\\sqrt{2}§", "§2\\sqrt{5} + 2\\sqrt{2}§", "§6\\sqrt{5} + 6\\sqrt{2}§", "§\\sqrt{5} + \\sqrt{2}§", "§3\\sqrt{5} + 3\\sqrt{2}§"), 1,
            "Kali sekawan §\\sqrt{5} + \\sqrt{2}§: penyebut menjadi §5 - 2 = 3§, sehingga §\\frac{6(\\sqrt{5} + \\sqrt{2})}{3} = 2\\sqrt{5} + 2\\sqrt{2}§."),
        q("me4", "Nilai §{}^{2}\\!\\log 32 - {}^{2}\\!\\log 4 + {}^{2}\\!\\log \\frac{1}{2}§ adalah ....",
            listOf("1", "2", "3", "4", "5"), 1,
            "§5 - 2 + (-1) = 2§."),
        q("me5", "Jika §\\log 2 = 0{,}301§ dan §\\log 3 = 0{,}477§, nilai §\\log 12§ adalah ....",
            listOf("0,778", "0,903", "1,079", "1,255", "0,602"), 2,
            "§\\log 12 = \\log(2^2 \\cdot 3) = 2(0{,}301) + 0{,}477 = 1{,}079§."),
        q("me6", "Nilai §8^{\\frac{2}{3}}§ adalah ....",
            listOf("2", "4", "§\\frac{16}{3}§", "6", "16"), 1,
            "§8^{\\frac{2}{3}} = (\\sqrt[3]{8})^2 = 2^2 = 4§."),
        q("me7", "Bakteri membelah menjadi dua setiap 20 menit. Jika awalnya ada 100 bakteri, banyaknya bakteri setelah 2 jam adalah ....",
            listOf("1.200", "3.200", "6.400", "12.800", "800"), 2,
            "2 jam = 6 kali pembelahan, jadi §100 \\times 2^6 = 6.400§."),
        table("me8", "Tentukan benar atau salah setiap pernyataan berikut (a, x, y bilangan real positif).",
            listOf("§(a^m)^n = a^{m+n}§", "§a^0 = 1§", "§\\log(x + y) = \\log x + \\log y§", "§\\sqrt{a^2} = a§"),
            listOf(1, 0, 1, 0),
            "§(a^m)^n = a^{mn}§, bukan §a^{m+n}§. Sifat log berlaku untuk perkalian: §\\log(xy) = \\log x + \\log y§. Untuk a positif, §a^0 = 1§ dan §\\sqrt{a^2} = a§."),
    )

    private fun linearQ() = listOf(
        q("ml1", "Penyelesaian §3(x - 2) = 2x + 5§ adalah ....",
            listOf("x = 1", "x = 3", "x = 7", "x = 11", "x = −1"), 3,
            "§3x - 6 = 2x + 5§, jadi §x = 11§."),
        q("ml2", "Harga 2 buku dan 3 pensil Rp17.000. Harga 1 buku dan 2 pensil Rp10.000. Harga 1 buku adalah ....",
            listOf("Rp3.000", "Rp3.500", "Rp4.000", "Rp4.500", "Rp5.000"), 2,
            "Dari persamaan kedua, §b = 10.000 - 2p§. Substitusi: §20.000 - 4p + 3p = 17.000§, jadi §p = 3.000§ dan §b = 4.000§."),
        q("ml3", "Diketahui §x + y + z = 6§, §x - y + z = 2§, dan §x + y - z = 0§. Nilai §xyz§ adalah ....",
            listOf("3", "4", "6", "8", "12"), 2,
            "Persamaan 1 dikurangi 2: §2y = 4§, §y = 2§. Persamaan 1 dikurangi 3: §2z = 6§, §z = 3§. Maka §x = 1§ dan §xyz = 6§."),
        q("ml4", "Jumlah umur ayah dan anak sekarang 50 tahun. Lima tahun lalu, umur ayah empat kali umur anak. Umur anak sekarang adalah ....",
            listOf("10 tahun", "12 tahun", "13 tahun", "15 tahun", "37 tahun"), 2,
            "§a + b = 50§ dan §a - 5 = 4(b - 5)§, jadi §a = 4b - 15§. Maka §5b - 15 = 50§, §b = 13§."),
        q("ml5", "Persamaan garis yang melalui titik (1, 2) dan (3, 8) adalah ....",
            listOf("y = 3x − 1", "y = 3x + 1", "y = 2x", "y = 4x − 2", "y = 3x + 5"), 0,
            "Gradien §m = \\frac{8 - 2}{3 - 1} = 3§, lalu §y - 2 = 3(x - 1)§, sehingga §y = 3x - 1§."),
        q("ml6", "Tiket pertunjukan dijual Rp50.000 untuk dewasa dan Rp30.000 untuk anak. Terjual 120 tiket dengan pendapatan Rp4.600.000. Banyak tiket dewasa yang terjual adalah ....",
            listOf("40", "50", "60", "70", "80"), 1,
            "§d + a = 120§ dan §50d + 30a = 4.600§ (ribuan). Substitusi: §50d + 3.600 - 30d = 4.600§, jadi §d = 50§."),
        multi("ml7", "Diketahui sistem §2x + 3y = 12§ dan §4x + 6y = k§. Pilih semua pernyataan yang benar.",
            listOf("Jika k = 24, sistem punya tak hingga banyak penyelesaian.", "Jika k = 20, sistem tidak punya penyelesaian.",
                "Jika k = 24, sistem punya tepat satu penyelesaian.", "Titik (3, 2) memenuhi persamaan pertama.", "Titik (2, 3) memenuhi persamaan pertama."),
            setOf(0, 1, 3),
            "Persamaan kedua adalah persamaan pertama dikali 2 jika k = 24, sehingga kedua garis berimpit. Untuk k lain, garisnya sejajar dan tidak berpotongan. §2(3) + 3(2) = 12§, sedangkan §2(2) + 3(3) = 13§."),
    )

    private fun pertidaksamaanQ() = listOf(
        q("mp1", "Penyelesaian §2x - 5 > 7x + 10§ adalah ....",
            listOf("x > −3", "x < −3", "x > 3", "x < 3", "x < −1"), 1,
            "§-5x > 15§. Dibagi −5, tanda berbalik: §x < -3§."),
        q("mp2", "Himpunan penyelesaian §x^2 - x - 6 \\leq 0§ adalah ....",
            listOf("§-3 \\leq x \\leq 2§", "§-2 \\leq x \\leq 3§", "§x \\leq -2§ atau §x \\geq 3§", "§x \\leq -3§ atau §x \\geq 2§", "§-2 < x < 3§"), 1,
            "§(x - 3)(x + 2) \\leq 0§, pembuat nol −2 dan 3 ikut, daerahnya di antara akar."),
        q("mp3", "Penyelesaian §\\dfrac{x - 1}{x + 2} \\geq 0§ adalah ....",
            listOf("§x \\leq -2§ atau §x \\geq 1§", "§x < -2§ atau §x \\geq 1§", "§-2 < x \\leq 1§", "§-2 \\leq x \\leq 1§", "§x \\geq 1§"), 1,
            "Pembuat nol pembilang 1 ikut, penyebut −2 tidak ikut. Daerah positif ada di luar kedua titik."),
        q("mp4", "Penyelesaian §|2x - 1| < 5§ adalah ....",
            listOf("§-3 < x < 2§", "§-2 < x < 3§", "§x < -2§ atau §x > 3§", "§-2 < x < 2§", "§0 < x < 3§"), 1,
            "§-5 < 2x - 1 < 5§, lalu §-4 < 2x < 6§, sehingga §-2 < x < 3§."),
        q("mp5", "Nilai maksimum §f(x, y) = 3x + 2y§ pada daerah §x + y \\leq 4§, §x + 3y \\leq 6§, §x \\geq 0§, §y \\geq 0§ adalah ....",
            listOf("4", "9", "11", "12", "14"), 3,
            "Titik pojok: (0, 0) → 0, (4, 0) → 12, (0, 2) → 4, dan perpotongan (3, 1) → 11. Maksimumnya 12."),
        q("mp6", "Pedagang membeli tas A seharga Rp40.000 (untung Rp10.000) dan tas B seharga Rp60.000 (untung Rp15.000). Modalnya Rp1.200.000 dan kiosnya muat 25 tas. Keuntungan maksimum adalah ....",
            listOf("Rp250.000", "Rp275.000", "Rp300.000", "Rp325.000", "Rp375.000"), 2,
            "Kendala §2a + 3b \\leq 60§ dan §a + b \\leq 25§. Titik pojok (25, 0) → Rp250.000, (15, 10) → Rp300.000, (0, 20) → Rp300.000."),
        q("mp7", "Banyak bilangan bulat x yang memenuhi §x^2 - 5x + 4 < 0§ adalah ....",
            listOf("1", "2", "3", "4", "5"), 1,
            "§(x - 1)(x - 4) < 0§ memberi §1 < x < 4§. Bilangan bulatnya 2 dan 3."),
    )

    private fun fungsiQ() = listOf(
        q("mf1", "Akar-akar persamaan §2x^2 + 3x - 2 = 0§ adalah ....",
            listOf("§\\frac{1}{2}§ dan −2", "§-\\frac{1}{2}§ dan 2", "1 dan −2", "2 dan −1", "§\\frac{1}{2}§ dan 2"), 0,
            "§D = 9 + 16 = 25§, sehingga §x = \\frac{-3 \\pm 5}{4}§, yaitu §\\frac{1}{2}§ atau §-2§."),
        q("mf2", "§x_1§ dan §x_2§ adalah akar-akar §x^2 - 4x + 1 = 0§. Nilai §x_1^2 + x_2^2§ adalah ....",
            listOf("12", "14", "16", "18", "15"), 1,
            "§(x_1 + x_2)^2 - 2x_1x_2 = 16 - 2 = 14§."),
        q("mf3", "Persamaan §x^2 + kx + 9 = 0§ mempunyai akar kembar. Jika §k > 0§, nilai k adalah ....",
            listOf("3", "6", "9", "12", "18"), 1,
            "Akar kembar berarti §D = k^2 - 36 = 0§, sehingga §k = 6§."),
        q("mf4", "Tinggi bola setelah t detik adalah §h(t) = 20t - 5t^2§ meter. Tinggi maksimum bola adalah ....",
            listOf("10 m", "15 m", "20 m", "25 m", "40 m"), 2,
            "Puncak di §t = \\frac{20}{10} = 2§ s, dan §h(2) = 40 - 20 = 20§ m."),
    )

    private fun deretQ() = listOf(
        q("md1", "Deret aritmetika memiliki suku pertama 4 dan beda 3. Jumlah 10 suku pertamanya adalah ....",
            listOf("155", "165", "175", "185", "195"), 2,
            "§S_{10} = \\frac{10}{2}(2 \\cdot 4 + 9 \\cdot 3) = 5 \\times 35 = 175§."),
        q("md2", "Gedung pertunjukan memiliki 20 baris kursi. Baris pertama berisi 15 kursi dan setiap baris berikutnya bertambah 2 kursi. Banyak kursi seluruhnya adalah ....",
            listOf("560", "620", "680", "700", "720"), 2,
            "§U_{20} = 15 + 19 \\cdot 2 = 53§, maka §S_{20} = 10(15 + 53) = 680§."),
        q("md3", "Suku ke-2 barisan geometri adalah 6 dan suku ke-5 adalah 162. Suku ke-7 barisan itu adalah ....",
            listOf("486", "972", "1.458", "2.916", "729"), 2,
            "§r^3 = \\frac{162}{6} = 27§, §r = 3§, §a = 2§. Maka §U_7 = 2 \\cdot 3^6 = 1.458§."),
        q("md4", "Bola dijatuhkan dari ketinggian 10 m dan setiap kali memantul mencapai §\\frac{3}{4}§ tinggi sebelumnya. Panjang lintasan bola sampai berhenti adalah ....",
            listOf("40 m", "50 m", "60 m", "70 m", "80 m"), 3,
            "Lintasan turun 10 m, lalu pantulan naik-turun: §2 \\times \\frac{7{,}5}{1 - \\frac{3}{4}} = 60§ m. Total 70 m."),
        q("md5", "Mobil seharga Rp200.000.000 nilainya menyusut 10% per tahun. Nilai mobil setelah 2 tahun adalah ....",
            listOf("Rp160.000.000", "Rp162.000.000", "Rp164.000.000", "Rp180.000.000", "Rp158.000.000"), 1,
            "§200.000.000 \\times 0{,}9^2 = 162.000.000§."),
        q("md6", "Suku ke-n suatu barisan adalah §U_n = 3n + 2§. Jumlah 10 suku pertamanya adalah ....",
            listOf("165", "175", "185", "195", "205"), 2,
            "§U_1 = 5§ dan §U_{10} = 32§, sehingga §S_{10} = 5(5 + 32) = 185§."),
    )

    private fun bidangQ() = listOf(
        q("mg1", "Besar setiap sudut dalam segi enam beraturan adalah ....",
            listOf("§108^\\circ§", "§120^\\circ§", "§135^\\circ§", "§144^\\circ§", "§60^\\circ§"), 1,
            "Jumlah sudut §(6 - 2) \\times 180^\\circ = 720^\\circ§, dibagi 6 sudut menjadi §120^\\circ§."),
        q("mg2", "Dua sudut saling berpelurus, besarnya §3x§ dan §(2x + 20)^\\circ§. Besar sudut yang lebih besar adalah ....",
            listOf("§84^\\circ§", "§90^\\circ§", "§96^\\circ§", "§100^\\circ§", "§64^\\circ§"), 2,
            "§5x + 20 = 180§, §x = 32§. Sudutnya §96^\\circ§ dan §84^\\circ§."),
        q("mg3", "Trapesium memiliki sisi sejajar 10 cm dan 16 cm dengan tinggi 8 cm. Luasnya adalah ....",
            listOf("§96 \\text{ cm}^2§", "§104 \\text{ cm}^2§", "§128 \\text{ cm}^2§", "§208 \\text{ cm}^2§", "§80 \\text{ cm}^2§"), 1,
            "§\\frac{1}{2}(10 + 16) \\times 8 = 104§ cm²."),
        q("mg4", "Sudut pusat AOB = §110^\\circ§. Besar sudut keliling ACB yang menghadap busur AB adalah ....",
            listOf("§35^\\circ§", "§55^\\circ§", "§70^\\circ§", "§110^\\circ§", "§220^\\circ§"), 1,
            "Sudut keliling = setengah sudut pusat."),
        q("mg5", "Titik P berjarak 13 cm dari pusat lingkaran berjari-jari 5 cm. Panjang garis singgung dari P ke lingkaran adalah ....",
            listOf("8 cm", "10 cm", "12 cm", "§\\sqrt{194}§ cm", "18 cm"), 2,
            "§\\sqrt{13^2 - 5^2} = 12§ cm, karena garis singgung tegak lurus jari-jari."),
        q("mg6", "Diagonal-diagonal belah ketupat panjangnya 12 cm dan 16 cm. Keliling belah ketupat adalah ....",
            listOf("28 cm", "40 cm", "48 cm", "56 cm", "96 cm"), 1,
            "Diagonal saling membagi dua tegak lurus, jadi sisinya §\\sqrt{6^2 + 8^2} = 10§ cm. Keliling 40 cm."),
        q("mg7", "Luas juring dengan sudut pusat §90^\\circ§ dan jari-jari 14 cm (§\\pi = \\frac{22}{7}§) adalah ....",
            listOf("§77 \\text{ cm}^2§", "§154 \\text{ cm}^2§", "§308 \\text{ cm}^2§", "§616 \\text{ cm}^2§", "§44 \\text{ cm}^2§"), 1,
            "§\\frac{90}{360} \\times \\frac{22}{7} \\times 14^2 = \\frac{1}{4} \\times 616 = 154§ cm²."),
    )

    private fun ruangQ() = listOf(
        q("mr1", "Luas permukaan kerucut dengan jari-jari 6 cm dan tinggi 8 cm adalah ....",
            listOf("§60\\pi§ cm²", "§84\\pi§ cm²", "§96\\pi§ cm²", "§100\\pi§ cm²", "§132\\pi§ cm²"), 2,
            "Garis pelukis §s = \\sqrt{36 + 64} = 10§. Luas §\\pi r(r + s) = 6\\pi \\times 16 = 96\\pi§ cm²."),
        q("mr2", "Volume bola berjari-jari 3 cm adalah ....",
            listOf("§12\\pi§ cm³", "§27\\pi§ cm³", "§36\\pi§ cm³", "§108\\pi§ cm³", "§9\\pi§ cm³"), 2,
            "§\\frac{4}{3}\\pi(27) = 36\\pi§ cm³."),
        q("mr3", "Kubus ABCD.EFGH memiliki rusuk 6 cm. Jarak titik A ke titik G adalah ....",
            listOf("6 cm", "§6\\sqrt{2}§ cm", "§6\\sqrt{3}§ cm", "12 cm", "§3\\sqrt{3}§ cm"), 2,
            "AG adalah diagonal ruang, panjangnya §s\\sqrt{3}§."),
        q("mr4", "Akuarium berbentuk balok berukuran 80 cm × 60 cm × 50 cm diisi air setinggi 40 cm. Volume air adalah ....",
            listOf("192 liter", "240 liter", "160 liter", "19,2 liter", "1.920 liter"), 0,
            "§80 \\times 60 \\times 40 = 192.000§ cm³ = 192 liter."),
        q("mr5", "Luas permukaan tabung dengan jari-jari 7 cm dan tinggi 10 cm (§\\pi = \\frac{22}{7}§) adalah ....",
            listOf("§440 \\text{ cm}^2§", "§594 \\text{ cm}^2§", "§748 \\text{ cm}^2§", "§1.540 \\text{ cm}^2§", "§616 \\text{ cm}^2§"), 2,
            "§2\\pi r(r + t) = 2 \\times \\frac{22}{7} \\times 7 \\times 17 = 748§ cm²."),
        q("mr6", "Kubus ABCD.EFGH memiliki rusuk 6 cm. Jarak titik A ke bidang BDE adalah ....",
            listOf("§\\sqrt{3}§ cm", "§2\\sqrt{3}§ cm", "§3\\sqrt{2}§ cm", "§3\\sqrt{3}§ cm", "§6\\sqrt{3}§ cm"), 1,
            "Jarak A ke bidang BDE adalah §\\frac{1}{3}§ diagonal ruang: §\\frac{6\\sqrt{3}}{3} = 2\\sqrt{3}§ cm."),
    )

    private fun transformasiQ() = listOf(
        q("mt1", "Titik (3, −2) dirotasi §90^\\circ§ berlawanan arah jarum jam terhadap titik O. Bayangannya adalah ....",
            listOf("(2, 3)", "(−2, −3)", "(−3, 2)", "(2, −3)", "(3, 2)"), 0,
            "Rotasi §90^\\circ§: §(x, y) \\to (-y, x) = (2, 3)§."),
        q("mt2", "Titik (1, 4) didilatasi dengan pusat O dan faktor skala −2. Bayangannya adalah ....",
            listOf("(2, 8)", "(−2, −8)", "(−1, −4)", "(−2, 8)", "(2, −8)"), 1,
            "Kalikan kedua koordinat dengan −2."),
        q("mt3", "Titik (1, 5) ditranslasi §\\binom{2}{-3}§, lalu dicerminkan terhadap sumbu-y. Bayangan akhirnya adalah ....",
            listOf("(3, 2)", "(−3, 2)", "(3, −2)", "(−3, −2)", "(−1, 2)"), 1,
            "Translasi: (3, 2). Cermin sumbu-y mengubah tanda x: (−3, 2)."),
        q("mt4", "Pada segitiga ABC, D pada AB dan E pada AC dengan DE sejajar BC. Jika AD = 4 cm, DB = 6 cm, dan DE = 6 cm, panjang BC adalah ....",
            listOf("9 cm", "10 cm", "12 cm", "15 cm", "18 cm"), 3,
            "Segitiga ADE sebangun ABC: §\\frac{4}{10} = \\frac{6}{BC}§, jadi §BC = 15§ cm."),
        q("mt5", "Garis §y = 2x + 1§ dicerminkan terhadap sumbu-x. Persamaan bayangannya adalah ....",
            listOf("y = 2x − 1", "y = −2x + 1", "y = −2x − 1", "y = 2x + 1", "y = x + 2"), 2,
            "Ganti y dengan −y: §-y = 2x + 1§, jadi §y = -2x - 1§."),
    )

    private fun trigonometriQ() = listOf(
        q("mtr1", "Diketahui §\\sin A = \\frac{3}{5}§ dengan A sudut lancip. Nilai §\\tan A§ adalah ....",
            listOf("§\\frac{3}{4}§", "§\\frac{4}{3}§", "§\\frac{4}{5}§", "§\\frac{5}{4}§", "§\\frac{3}{5}§"), 0,
            "Sisi depan 3, miring 5, samping 4. §\\tan A = \\frac{3}{4}§."),
        q("mtr2", "Nilai §\\cos 120^\\circ§ adalah ....",
            listOf("§\\frac{1}{2}§", "§-\\frac{1}{2}§", "§\\frac{1}{2}\\sqrt{3}§", "§-\\frac{1}{2}\\sqrt{3}§", "0"), 1,
            "§\\cos(180^\\circ - 60^\\circ) = -\\cos 60^\\circ§, karena cos negatif di kuadran II."),
        q("mtr3", "Pada segitiga ABC, §b = 8§ cm, §c = 5§ cm, dan §A = 60^\\circ§. Panjang a adalah ....",
            listOf("6 cm", "7 cm", "§\\sqrt{89}§ cm", "8 cm", "§\\sqrt{39}§ cm"), 1,
            "§a^2 = 64 + 25 - 80 \\cdot \\frac{1}{2} = 49§."),
        q("mtr4", "Luas segitiga dengan dua sisi 8 cm dan 5 cm yang mengapit sudut §60^\\circ§ adalah ....",
            listOf("20 cm²", "§10\\sqrt{3}§ cm²", "§20\\sqrt{3}§ cm²", "10 cm²", "§10\\sqrt{2}§ cm²"), 1,
            "§\\frac{1}{2}(8)(5)\\sin 60^\\circ = 10\\sqrt{3}§ cm²."),
        q("mtr5", "Pada segitiga ABC, §A = 30^\\circ§, §B = 45^\\circ§, dan §a = 10§ cm. Panjang b adalah ....",
            listOf("§5\\sqrt{2}§ cm", "§10\\sqrt{2}§ cm", "§10\\sqrt{3}§ cm", "20 cm", "§5\\sqrt{6}§ cm"), 1,
            "§b = \\frac{a \\sin B}{\\sin A} = \\frac{10 \\cdot \\frac{1}{2}\\sqrt{2}}{\\frac{1}{2}} = 10\\sqrt{2}§."),
        q("mtr6", "Seorang anak bertinggi mata 1,5 m melihat puncak menara dengan sudut elevasi §45^\\circ§. Jarak anak ke kaki menara 30 m. Tinggi menara adalah ....",
            listOf("30 m", "31,5 m", "§15\\sqrt{2}§ m", "§30\\sqrt{3}§ m", "28,5 m"), 1,
            "§\\tan 45^\\circ = 1§, jadi selisih tinggi 30 m. Ditambah tinggi mata: 31,5 m."),
    )

    private fun statistikaQ() = listOf(
        q("ms1", "Diagram lingkaran menunjukkan ekstrakurikuler 360 siswa. Sektor sepak bola bersudut §90^\\circ§. Banyak siswa yang memilih sepak bola adalah ....",
            listOf("45", "60", "90", "120", "180"), 2,
            "§\\frac{90}{360} \\times 360 = 90§ siswa."),
        q("ms2", "Rata-rata nilai 30 siswa kelas A adalah 70 dan rata-rata 20 siswa kelas B adalah 80. Rata-rata gabungannya adalah ....",
            listOf("72", "74", "75", "76", "78"), 1,
            "§\\frac{30(70) + 20(80)}{50} = \\frac{3.700}{50} = 74§."),
        q("ms3", "Data: 4, 5, 5, 6, 8, 8, 8, 10. Jangkauan antarkuartil data tersebut adalah ....",
            listOf("2", "3", "4", "5", "6"), 1,
            "§Q_1 = \\frac{5 + 5}{2} = 5§ dan §Q_3 = \\frac{8 + 8}{2} = 8§, jadi §H = 3§."),
        q("ms4", "Ragam (varians) data 2, 4, 6, 8 adalah ....",
            listOf("4", "5", "6", "§\\sqrt{5}§", "20"), 1,
            "Rata-rata 5. Jumlah kuadrat simpangan §9 + 1 + 1 + 9 = 20§, dibagi 4 menjadi 5."),
        q("ms5", "Tabel nilai: 41–50 (4 siswa), 51–60 (6), 61–70 (10), 71–80 (8), 81–90 (2). Median data tersebut adalah ....",
            listOf("63,5", "65,5", "66,5", "67,5", "68,5"), 1,
            "n = 30, letak median data ke-15 di kelas 61–70. §Me = 60{,}5 + \\frac{15 - 10}{10} \\times 10 = 65{,}5§."),
        q("ms6", "Dari tabel pada soal sebelumnya, modus data adalah ....",
            listOf("65,50", "66,17", "67,17", "68,00", "70,50"), 2,
            "Kelas modus 61–70, §d_1 = 10 - 6 = 4§, §d_2 = 10 - 8 = 2§. §Mo = 60{,}5 + \\frac{4}{6} \\times 10 \\approx 67{,}17§."),
    )

    private fun peluangQ() = listOf(
        q("mpl1", "Tiga koin dilempar bersamaan. Peluang muncul tepat dua gambar adalah ....",
            listOf("§\\frac{1}{8}§", "§\\frac{1}{4}§", "§\\frac{3}{8}§", "§\\frac{1}{2}§", "§\\frac{5}{8}§"), 2,
            "Ruang sampel 8. Tepat dua gambar: GGA, GAG, AGG, ada 3."),
        q("mpl2", "Kantong berisi 5 bola merah dan 3 bola biru. Dua bola diambil sekaligus. Peluang keduanya merah adalah ....",
            listOf("§\\frac{5}{14}§", "§\\frac{5}{8}§", "§\\frac{25}{64}§", "§\\frac{3}{14}§", "§\\frac{10}{56}§"), 0,
            "§\\frac{C(5, 2)}{C(8, 2)} = \\frac{10}{28} = \\frac{5}{14}§."),
        q("mpl3", "Banyak susunan berbeda dari huruf-huruf pada kata KATAK adalah ....",
            listOf("20", "30", "60", "120", "24"), 1,
            "Permutasi dengan unsur sama: K dan A masing-masing dua. §\\frac{5!}{2!\\,2!} = 30§."),
        q("mpl4", "Sebuah dadu dilempar 120 kali. Frekuensi harapan muncul mata dadu bilangan prima adalah ....",
            listOf("40", "50", "60", "80", "20"), 2,
            "Mata prima 2, 3, 5, jadi peluangnya §\\frac{3}{6}§. §\\frac{1}{2} \\times 120 = 60§."),
    )
}
