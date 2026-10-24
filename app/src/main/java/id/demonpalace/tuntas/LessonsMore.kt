package id.demonpalace.tuntas

/**
 * Sub-chapters added when the books were cut into about ten chapters per subject.
 * [Chapters] places them; scope follows the TKA SMA 2026 kisi-kisi in [Content.sources].
 */
object LessonsMore {

    private fun ex(problem: String, vararg steps: String, answer: String) = Example(problem, steps.toList(), answer)

    // ============================== MATEMATIKA ==============================

    val persamaanKuadrat = Section(
        title = "Persamaan kuadrat",
        explain = listOf(
            "Persamaan kuadrat berbentuk §ax^2 + bx + c = 0§ dengan §a \\neq 0§. Nilai x yang memenuhinya disebut akar, dan banyaknya paling banyak dua.",
            "Ada tiga cara mencari akar. PEMFAKTORAN dipakai kalau ada dua bilangan yang hasil kalinya §ac§ dan jumlahnya §b§. RUMUS ABC selalu berhasil. MELENGKAPKAN KUADRAT jarang ditanyakan langsung, tetapi menjadi dasar rumus puncak parabola.",
            "Diskriminan §D = b^2 - 4ac§ menentukan jenis akar tanpa menghitungnya. Jika §D > 0§, ada dua akar real berbeda. Jika §D = 0§, akarnya kembar. Jika §D < 0§, tidak ada akar real.",
            "Soal TKA sering tidak meminta akarnya, tetapi meminta nilai yang tersusun dari akar, misalnya §x_1^2 + x_2^2§ atau §\\frac{1}{x_1} + \\frac{1}{x_2}§. Nilai seperti ini dihitung dari jumlah dan hasil kali akar, jadi akarnya tidak perlu dicari.",
        ),
        formulas = listOf(
            "§x_{1,2} = \\dfrac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}§",
            "§x_1 + x_2 = -\\dfrac{b}{a} \\qquad x_1 x_2 = \\dfrac{c}{a}§",
            "§x_1^2 + x_2^2 = (x_1 + x_2)^2 - 2x_1 x_2§",
            "Persamaan baru berakar §\\alpha, \\beta§: §x^2 - (\\alpha + \\beta)x + \\alpha\\beta = 0§",
        ),
        examples = listOf(
            ex("Tentukan akar-akar §x^2 - 5x + 6 = 0§.",
                "Cari dua bilangan yang hasil kalinya 6 dan jumlahnya −5, yaitu −2 dan −3.",
                "§(x - 2)(x - 3) = 0§, sehingga §x = 2§ atau §x = 3§.",
                answer = "x = 2 atau x = 3"),
            ex("Tentukan akar-akar §2x^2 + 3x - 2 = 0§.",
                "§a = 2,\\; b = 3,\\; c = -2§, sehingga §D = 9 + 16 = 25§.",
                "§x = \\dfrac{-3 \\pm 5}{4}§.",
                "§x_1 = \\dfrac{2}{4} = \\dfrac{1}{2}§ dan §x_2 = \\dfrac{-8}{4} = -2§.",
                answer = "§x = \\tfrac{1}{2}§ atau §x = -2§"),
            ex("§x_1§ dan §x_2§ akar-akar §x^2 - 4x + 1 = 0§. Hitung §x_1^2 + x_2^2§.",
                "§x_1 + x_2 = 4§ dan §x_1 x_2 = 1§.",
                "§x_1^2 + x_2^2 = 4^2 - 2(1) = 14§.",
                answer = "14"),
        ),
        pitfalls = listOf(
            "Menulis §x_1 + x_2 = \\frac{b}{a}§. Tandanya negatif: §-\\frac{b}{a}§.",
            "Menganggap §x_1^2 + x_2^2 = (x_1 + x_2)^2§. Suku §-2x_1 x_2§ hilang.",
        ),
        points = listOf(
            "§D > 0§: dua akar real berbeda; §D = 0§: kembar; §D < 0§: tidak real.",
            "§x_1 + x_2 = -\\frac{b}{a}§, §x_1 x_2 = \\frac{c}{a}§.",
        ),
    )

    val fungsiKuadrat = Section(
        title = "Fungsi kuadrat & grafiknya",
        explain = listOf(
            "Grafik §f(x) = ax^2 + bx + c§ adalah parabola. Koefisien §a§ menentukan arah: §a > 0§ terbuka ke atas dan punya nilai minimum, §a < 0§ terbuka ke bawah dan punya nilai maksimum.",
            "Titik potong dengan sumbu-y adalah §(0, c)§. Titik potong dengan sumbu-x adalah akar-akar §f(x) = 0§, sehingga banyaknya ditentukan oleh diskriminan. Sumbu simetri adalah garis §x = -\\frac{b}{2a}§, dan titik puncak terletak pada garis itu.",
            "Untuk menyusun fungsi dari grafik, pilih bentuk yang sesuai dengan data. Jika puncak §(x_p, y_p)§ diketahui, pakai §y = a(x - x_p)^2 + y_p§. Jika dua titik potong sumbu-x diketahui, pakai §y = a(x - x_1)(x - x_2)§. Nilai §a§ dicari dari satu titik lain.",
            "Masalah maksimum dan minimum, seperti tinggi bola atau luas kandang dengan pagar terbatas, diselesaikan dengan mencari puncak parabola.",
        ),
        formulas = listOf(
            "Puncak: §\\left(-\\dfrac{b}{2a},\\; -\\dfrac{D}{4a}\\right)§",
            "§y = a(x - x_p)^2 + y_p \\qquad y = a(x - x_1)(x - x_2)§",
        ),
        examples = listOf(
            ex("Gambarkan ciri grafik §f(x) = x^2 - 6x + 5§.",
                "§a = 1 > 0§, jadi parabola terbuka ke atas.",
                "Potong sumbu-y di §(0, 5)§. Akar §x^2 - 6x + 5 = (x - 1)(x - 5)§, jadi memotong sumbu-x di §(1, 0)§ dan §(5, 0)§.",
                "§x_p = \\frac{6}{2} = 3§, §y_p = 9 - 18 + 5 = -4§.",
                answer = "Puncak §(3, -4)§, nilai minimum −4"),
            ex("Parabola berpuncak di §(2, 3)§ dan melalui §(0, 7)§. Tentukan persamaannya.",
                "§y = a(x - 2)^2 + 3§.",
                "Substitusi §(0, 7)§: §7 = 4a + 3§, sehingga §a = 1§.",
                "§y = (x - 2)^2 + 3 = x^2 - 4x + 7§.",
                answer = "§y = x^2 - 4x + 7§"),
            ex("Tinggi bola setelah t detik adalah §h(t) = 20t - 5t^2§ meter. Berapa tinggi maksimumnya?",
                "§t_p = -\\dfrac{20}{2(-5)} = 2§ s.",
                "§h(2) = 40 - 20 = 20§ m.",
                answer = "20 m, dicapai saat t = 2 s"),
        ),
        pitfalls = listOf(
            "Menjawab nilai §x_p§ ketika soal meminta nilai maksimum. Nilai maksimum adalah §y_p§.",
        ),
        points = listOf(
            "§a > 0§ minimum, §a < 0§ maksimum.",
            "Sumbu simetri §x = -\\frac{b}{2a}§; nilai ekstrem §-\\frac{D}{4a}§.",
            "Puncak diketahui: §y = a(x - x_p)^2 + y_p§.",
        ),
    )

    val pertidaksamaanKuadrat = Section(
        title = "Pertidaksamaan kuadrat, rasional & nilai mutlak",
        explain = listOf(
            "Pertidaksamaan kuadrat diselesaikan dalam tiga langkah. Pertama, pindahkan semua suku ke satu ruas sehingga ruas kanan 0. Kedua, cari pembuat nol. Ketiga, uji tanda pada garis bilangan dan ambil daerah yang sesuai.",
            "Untuk §a > 0§, bentuk §(x - p)(x - q) < 0§ dengan §p < q§ selalu menghasilkan daerah di antara akar, §p < x < q§. Bentuk §> 0§ menghasilkan daerah di luar akar. Tanda §\\leq§ atau §\\geq§ membuat akarnya ikut.",
            "Pertidaksamaan rasional §\\frac{f(x)}{g(x)} \\geq 0§ juga diselesaikan dengan garis bilangan. Pembuat nol penyebut tidak boleh ikut, karena pecahan tidak terdefinisi di sana. Kedua ruas tidak boleh dikali penyebut, sebab tanda penyebut belum diketahui.",
            "Nilai mutlak §|x|§ adalah jarak x ke 0. Karena itu §|x| < a§ berarti §-a < x < a§, dan §|x| > a§ berarti §x < -a§ atau §x > a§.",
        ),
        formulas = listOf(
            "§(x - p)(x - q) < 0 \\Rightarrow p < x < q§ (untuk §p < q§)",
            "§(x - p)(x - q) > 0 \\Rightarrow x < p \\;\\text{atau}\\; x > q§",
            "§|u| < a \\iff -a < u < a \\qquad |u| > a \\iff u < -a \\;\\text{atau}\\; u > a§",
        ),
        examples = listOf(
            ex("Tentukan himpunan penyelesaian §x^2 - x - 6 < 0§.",
                "§(x - 3)(x + 2) < 0§, pembuat nol §x = -2§ dan §x = 3§.",
                "Bentuk §< 0§ dengan §a > 0§: daerah di antara akar.",
                answer = "§-2 < x < 3§"),
            ex("Tentukan penyelesaian §\\dfrac{x - 1}{x + 2} \\geq 0§.",
                "Pembuat nol pembilang §x = 1§ (ikut), penyebut §x = -2§ (tidak ikut).",
                "Uji §x = 0§: §\\frac{-1}{2} < 0§, jadi daerah §-2 < x < 1§ negatif dan dua daerah di luarnya positif.",
                answer = "§x < -2§ atau §x \\geq 1§"),
            ex("Tentukan penyelesaian §|2x - 1| < 5§.",
                "§-5 < 2x - 1 < 5§.",
                "Tambah 1: §-4 < 2x < 6§. Bagi 2: §-2 < x < 3§.",
                answer = "§-2 < x < 3§"),
        ),
        pitfalls = listOf(
            "Mengalikan kedua ruas pertidaksamaan rasional dengan penyebut.",
            "Memasukkan pembuat nol penyebut ke himpunan penyelesaian.",
        ),
        points = listOf(
            "Ruas kanan 0, cari pembuat nol, uji tanda.",
            "Kurang dari 0: di antara akar. Lebih dari 0: di luar akar.",
            "Penyebut tidak pernah ikut.",
        ),
    )

    val lingkaran = Section(
        title = "Sudut & garis singgung lingkaran",
        explain = listOf(
            "SUDUT PUSAT adalah sudut yang titik sudutnya di pusat lingkaran. SUDUT KELILING titik sudutnya di lingkaran. Jika keduanya menghadap busur yang sama, sudut pusat besarnya dua kali sudut keliling.",
            "Akibatnya, sudut keliling yang menghadap diameter selalu §90^\\circ§, karena sudut pusatnya §180^\\circ§. Sudut-sudut keliling yang menghadap busur yang sama besarnya sama. Pada segi empat tali busur, sudut yang berhadapan berjumlah §180^\\circ§.",
            "Garis singgung menyentuh lingkaran di satu titik dan tegak lurus jari-jari di titik itu. Dari titik P di luar lingkaran, jari-jari, garis singgung, dan ruas PO membentuk segitiga siku-siku. Panjang garis singgung dihitung dengan Pythagoras.",
            "Dua lingkaran dengan jari-jari R dan r yang pusatnya berjarak d punya garis singgung persekutuan luar dan dalam. Keduanya juga dihitung dengan Pythagoras.",
        ),
        formulas = listOf(
            "§\\angle\\text{pusat} = 2 \\times \\angle\\text{keliling}§",
            "Garis singgung dari titik luar: §PA = \\sqrt{d^2 - r^2}§",
            "Persekutuan luar: §\\sqrt{d^2 - (R - r)^2}§ · dalam: §\\sqrt{d^2 - (R + r)^2}§",
        ),
        examples = listOf(
            ex("Sudut pusat AOB = §110^\\circ§. Berapa sudut keliling ACB yang menghadap busur AB yang sama?",
                "Sudut keliling = setengah sudut pusat.",
                "§\\frac{110^\\circ}{2} = 55^\\circ§.",
                answer = "§55^\\circ§"),
            ex("Titik P berjarak 13 cm dari pusat lingkaran berjari-jari 5 cm. Berapa panjang garis singgung dari P?",
                "Segitiga OAP siku-siku di A.",
                "§PA = \\sqrt{13^2 - 5^2} = \\sqrt{144} = 12§ cm.",
                answer = "12 cm"),
            ex("Dua lingkaran berjari-jari 8 cm dan 3 cm, jarak pusatnya 13 cm. Hitung panjang garis singgung persekutuan luar dan dalam.",
                "Luar: §\\sqrt{13^2 - (8 - 3)^2} = \\sqrt{169 - 25} = 12§ cm.",
                "Dalam: §\\sqrt{13^2 - (8 + 3)^2} = \\sqrt{169 - 121} = \\sqrt{48} = 4\\sqrt{3}§ cm.",
                answer = "Luar 12 cm, dalam §4\\sqrt{3}§ cm"),
        ),
        pitfalls = listOf(
            "Membagi dua sudut keliling untuk mendapat sudut pusat. Yang benar dikali dua.",
            "Memakai §R - r§ untuk garis singgung persekutuan dalam. Persekutuan dalam memakai §R + r§.",
        ),
        points = listOf(
            "Sudut pusat = 2 × sudut keliling.",
            "Sudut keliling yang menghadap diameter = §90^\\circ§.",
            "Garis singgung tegak lurus jari-jari.",
        ),
    )

    val aturanSinusCosinus = Section(
        title = "Aturan sinus, aturan cosinus & luas segitiga",
        explain = listOf(
            "Perbandingan sin, cos, dan tan hanya berlaku pada segitiga siku-siku. Untuk segitiga sembarang dipakai aturan sinus dan aturan cosinus. Sisi a berhadapan dengan sudut A, sisi b dengan sudut B, dan sisi c dengan sudut C.",
            "Aturan SINUS dipakai jika diketahui dua sudut dan satu sisi, atau dua sisi dan sudut di depan salah satunya. Aturan COSINUS dipakai jika diketahui dua sisi dan sudut apitnya (untuk mencari sisi ketiga), atau ketiga sisi (untuk mencari sudut).",
            "Luas segitiga bisa dihitung dari dua sisi dan sudut apitnya tanpa mencari tinggi.",
        ),
        formulas = listOf(
            "§\\dfrac{a}{\\sin A} = \\dfrac{b}{\\sin B} = \\dfrac{c}{\\sin C} = 2R§",
            "§a^2 = b^2 + c^2 - 2bc \\cos A§",
            "§\\cos A = \\dfrac{b^2 + c^2 - a^2}{2bc}§",
            "§L = \\tfrac{1}{2}\\, b\\, c \\sin A§",
        ),
        examples = listOf(
            ex("Pada segitiga ABC, §b = 8§ cm, §c = 5§ cm, dan §A = 60^\\circ§. Hitung panjang a.",
                "§a^2 = 64 + 25 - 2(8)(5)\\cos 60^\\circ = 89 - 40 = 49§.",
                "§a = 7§ cm.",
                answer = "7 cm"),
            ex("Hitung luas segitiga pada contoh sebelumnya.",
                "§L = \\tfrac{1}{2}(8)(5)\\sin 60^\\circ = 20 \\cdot \\tfrac{1}{2}\\sqrt{3}§.",
                answer = "§10\\sqrt{3}§ cm²"),
            ex("Pada segitiga ABC, §A = 30^\\circ§, §B = 45^\\circ§, dan §a = 10§ cm. Hitung b.",
                "§\\dfrac{b}{\\sin 45^\\circ} = \\dfrac{10}{\\sin 30^\\circ}§.",
                "§b = \\dfrac{10 \\cdot \\frac{1}{2}\\sqrt{2}}{\\frac{1}{2}} = 10\\sqrt{2}§ cm.",
                answer = "§10\\sqrt{2}§ cm"),
        ),
        pitfalls = listOf(
            "Memakai sudut yang bukan sudut apit di aturan cosinus. Sudut A harus diapit sisi b dan c.",
        ),
        points = listOf(
            "Dua sudut dan satu sisi: aturan sinus.",
            "Dua sisi dan sudut apit, atau tiga sisi: aturan cosinus.",
            "§L = \\frac{1}{2} bc \\sin A§.",
        ),
    )

    // ============================== FISIKA ==============================

    val besaranVektor = Section(
        title = "Besaran, dimensi & vektor",
        explain = listOf(
            "Tujuh BESARAN POKOK dalam SI adalah panjang (m), massa (kg), waktu (s), kuat arus (A), suhu (K), intensitas cahaya (cd), dan jumlah zat (mol). Besaran lain, seperti kecepatan, gaya, dan energi, adalah besaran turunan.",
            "Dimensi menyatakan susunan besaran turunan dari besaran pokok: panjang [L], massa [M], waktu [T]. Gaya §F = ma§ berdimensi §[M][L][T]^{-2}§. Dua besaran hanya bisa dijumlahkan atau disamakan jika dimensinya sama, sehingga analisis dimensi dipakai untuk memeriksa rumus.",
            "BESARAN SKALAR hanya punya nilai, misalnya massa, suhu, dan energi. BESARAN VEKTOR punya nilai dan arah, misalnya perpindahan, kecepatan, gaya, dan momentum. Vektor dijumlahkan secara geometris, bukan dengan menambah angkanya begitu saja.",
            "Dua vektor §F_1§ dan §F_2§ yang membentuk sudut §\\theta§ menghasilkan resultan yang dihitung dengan aturan cosinus. Untuk tiga vektor atau lebih, setiap vektor diuraikan menjadi komponen x dan y, komponen sejenis dijumlahkan, lalu resultan dihitung dengan Pythagoras.",
        ),
        formulas = listOf(
            "§R = \\sqrt{F_1^2 + F_2^2 + 2F_1F_2\\cos\\theta}§",
            "§F_x = F\\cos\\theta \\qquad F_y = F\\sin\\theta§",
            "§R = \\sqrt{(\\Sigma F_x)^2 + (\\Sigma F_y)^2} \\qquad \\tan\\alpha = \\dfrac{\\Sigma F_y}{\\Sigma F_x}§",
        ),
        examples = listOf(
            ex("Dua gaya 6 N dan 8 N bekerja pada satu titik dan membentuk sudut §60^\\circ§. Berapa resultannya?",
                "§R^2 = 36 + 64 + 2(6)(8)\\cos 60^\\circ = 100 + 48 = 148§.",
                "§R = \\sqrt{148} = 2\\sqrt{37} \\approx 12{,}2§ N.",
                answer = "§2\\sqrt{37}§ N, sekitar 12,2 N"),
            ex("Gaya 10 N membentuk sudut §37^\\circ§ terhadap sumbu x (§\\sin 37^\\circ = 0{,}6§). Tentukan komponennya.",
                "§F_x = 10 \\times 0{,}8 = 8§ N.",
                "§F_y = 10 \\times 0{,}6 = 6§ N.",
                answer = "§F_x = 8§ N, §F_y = 6§ N"),
            ex("Tentukan dimensi usaha §W = Fs§.",
                "Gaya §[M][L][T]^{-2}§ dikali perpindahan §[L]§.",
                answer = "§[M][L]^2[T]^{-2}§"),
        ),
        pitfalls = listOf(
            "Menjumlahkan 6 N dan 8 N menjadi 14 N padahal arahnya berbeda. 14 N hanya benar jika kedua gaya searah.",
            "Memakai sin untuk komponen x. Komponen yang menempel pada sudut memakai cos.",
        ),
        points = listOf(
            "7 besaran pokok: panjang, massa, waktu, kuat arus, suhu, intensitas cahaya, jumlah zat.",
            "Gaya: §[M][L][T]^{-2}§. Energi: §[M][L]^2[T]^{-2}§.",
            "Resultan dua vektor: §\\sqrt{F_1^2 + F_2^2 + 2F_1F_2\\cos\\theta}§.",
        ),
    )

    val penerapanNewton = Section(
        title = "Bidang miring, katrol & gaya gesek statis",
        explain = listOf(
            "Pada bidang miring bersudut §\\theta§, berat §mg§ diuraikan menjadi komponen sejajar bidang §mg\\sin\\theta§ (penyebab benda meluncur) dan komponen tegak lurus bidang §mg\\cos\\theta§ (diimbangi gaya normal). Jadi di bidang miring §N = mg\\cos\\theta§, bukan §mg§.",
            "Benda yang dihubungkan tali melalui katrol licin bergerak dengan percepatan yang sama. Tinjau seluruh sistem: percepatan = gaya penggerak total dibagi massa total. Tegangan tali dicari dengan meninjau salah satu benda saja.",
            "Gaya gesek STATIS bekerja selama benda belum bergerak. Besarnya menyesuaikan gaya tarik, sampai batas §f_{s,\\text{maks}} = \\mu_s N§. Jika gaya tarik lebih kecil dari batas itu, benda tetap diam dan gaya geseknya sama dengan gaya tarik. Setelah benda bergerak, yang bekerja gaya gesek KINETIS §f_k = \\mu_k N§.",
        ),
        formulas = listOf(
            "Bidang miring licin: §a = g\\sin\\theta§",
            "Bidang miring kasar: §a = g(\\sin\\theta - \\mu_k\\cos\\theta)§",
            "Katrol dua beban (Atwood): §a = \\dfrac{(m_1 - m_2)\\,g}{m_1 + m_2}§",
            "Balok di meja ditarik beban tergantung: §a = \\dfrac{m_2\\, g - \\mu_k m_1 g}{m_1 + m_2}§",
        ),
        examples = listOf(
            ex("Balok 2 kg meluncur pada bidang miring licin bersudut §30^\\circ§ (g = 10 m/s²). Berapa percepatannya?",
                "§a = g\\sin 30^\\circ = 10 \\times 0{,}5§.",
                answer = "5 m/s²"),
            ex("Beban 3 kg dan 2 kg digantung pada katrol licin. Hitung percepatan dan tegangan tali (g = 10 m/s²).",
                "§a = \\dfrac{(3 - 2)(10)}{3 + 2} = 2§ m/s².",
                "Tinjau beban 2 kg yang bergerak naik: §T - 20 = 2(2)§, sehingga §T = 24§ N.",
                answer = "a = 2 m/s², T = 24 N"),
            ex("Balok 10 kg di lantai dengan §\\mu_s = 0{,}4§ dan §\\mu_k = 0{,}3§ ditarik mendatar 30 N. Berapa gaya geseknya?",
                "§f_{s,\\text{maks}} = 0{,}4 \\times 100 = 40§ N.",
                "Tarikan 30 N < 40 N, jadi balok tetap diam dan gaya gesek mengimbangi tarikan.",
                answer = "30 N, balok diam"),
        ),
        pitfalls = listOf(
            "Memakai §N = mg§ di bidang miring.",
            "Langsung memakai §\\mu_s N§ sebagai gaya gesek padahal tarikan belum melampaui batasnya.",
        ),
        points = listOf(
            "Bidang miring: §N = mg\\cos\\theta§, gaya penggerak §mg\\sin\\theta§.",
            "Sistem katrol: §a = \\frac{\\text{gaya penggerak}}{\\text{massa total}}§.",
            "Gesek statis maksimum §\\mu_s N§; kalau tarikan lebih kecil, benda diam.",
        ),
    )

    val kesetimbangan = Section(
        title = "Kesetimbangan benda tegar & titik berat",
        explain = listOf(
            "Benda tegar berada dalam kesetimbangan jika tidak bergeser dan tidak berputar. Syaratnya ada dua: resultan gaya nol, §\\Sigma F = 0§, dan resultan torsi terhadap titik mana pun nol, §\\Sigma\\tau = 0§.",
            "Dalam soal batang yang ditopang, pilih titik poros di salah satu penopang. Gaya penopang itu tidak menghasilkan torsi karena lengannya nol, sehingga gaya penopang lain bisa langsung dihitung. Berat batang homogen bekerja di tengah batang.",
            "Titik berat adalah titik tangkap resultan gaya berat. Untuk gabungan beberapa benda, titik beratnya adalah rata-rata posisi yang diberi bobot massa (atau luas, untuk bangun datar homogen).",
            "Momen inersia benda tegar bergantung pada bentuk dan letak sumbu. Benda yang menggelinding tanpa slip punya energi kinetik translasi dan rotasi sekaligus.",
        ),
        formulas = listOf(
            "§\\Sigma F = 0 \\qquad \\Sigma\\tau = 0§",
            "§x_0 = \\dfrac{\\Sigma m_i x_i}{\\Sigma m_i} \\qquad y_0 = \\dfrac{\\Sigma A_i y_i}{\\Sigma A_i}§",
            "Batang (pusat) §\\tfrac{1}{12}mL^2§ · Batang (ujung) §\\tfrac{1}{3}mL^2§ · Silinder pejal §\\tfrac{1}{2}mr^2§ · Bola pejal §\\tfrac{2}{5}mr^2§",
            "Menggelinding: §E_k = \\tfrac{1}{2}mv^2 + \\tfrac{1}{2}I\\omega^2§",
        ),
        examples = listOf(
            ex("Batang homogen 4 m bermassa 20 kg ditopang di ujung A dan B. Beban 40 kg diletakkan 1 m dari A. Hitung gaya topang di A (g = 10 m/s²).",
                "Poros di B. Berat batang 200 N bekerja 2 m dari B; beban 400 N bekerja 3 m dari B.",
                "§N_A \\times 4 = 200 \\times 2 + 400 \\times 3 = 1.600§.",
                "§N_A = 400§ N, dan §N_B = 600 - 400 = 200§ N.",
                answer = "§N_A = 400§ N"),
            ex("Silinder pejal 2 kg menggelinding dengan kelajuan 4 m/s. Hitung energi kinetik totalnya.",
                "§I\\omega^2 = \\tfrac{1}{2}mr^2 \\cdot \\frac{v^2}{r^2} = \\tfrac{1}{2}mv^2§.",
                "§E_k = \\tfrac{1}{2}mv^2 + \\tfrac{1}{4}mv^2 = \\tfrac{3}{4}(2)(16) = 24§ J.",
                answer = "24 J"),
        ),
        pitfalls = listOf(
            "Lupa memasukkan berat batang ke persamaan torsi.",
            "Mengukur lengan torsi dari ujung batang padahal porosnya di penopang.",
        ),
        points = listOf(
            "Setimbang: §\\Sigma F = 0§ dan §\\Sigma\\tau = 0§.",
            "Poros di penopang menghilangkan satu gaya yang belum diketahui.",
            "Silinder pejal menggelinding: §E_k = \\frac{3}{4}mv^2§.",
        ),
    )

    // ============================== BAHASA INDONESIA ==============================

    val jenisTeksInformasi = Section(
        title = "Jenis & struktur teks informasi",
        explain = listOf(
            "Teks informasi dalam TKA berupa teks eksposisi, teks eksplanasi, teks laporan hasil observasi, berita, dan artikel. Jenis teks dikenali dari tujuan dan strukturnya, bukan dari topiknya.",
            "TEKS EKSPOSISI menyampaikan pendapat penulis dengan alasan. Strukturnya tesis (pendapat), rangkaian argumen (data, fakta, pendapat ahli), dan penegasan ulang. Penegasan ulang biasanya diawali kata seperti \"dengan demikian\" atau \"oleh karena itu\" dan isinya mengulang tesis dengan kalimat lain.",
            "TEKS EKSPLANASI menjelaskan proses terjadinya fenomena alam atau sosial. Strukturnya identifikasi fenomena, rangkaian kejadian yang memuat hubungan sebab-akibat atau urutan waktu, dan ulasan. Contohnya teks tentang terjadinya gempa bumi atau inflasi.",
            "TEKS LAPORAN HASIL OBSERVASI menggambarkan suatu objek secara umum berdasarkan pengamatan. Strukturnya pernyataan umum atau klasifikasi, deskripsi bagian, dan simpulan. Bahasanya objektif dan memakai istilah ilmiah.",
            "BERITA melaporkan peristiwa aktual dan menjawab unsur 5W + 1H: apa, siapa, kapan, di mana, mengapa, dan bagaimana. Berita disusun dengan pola piramida terbalik: informasi terpenting ada di paragraf pertama (teras berita).",
        ),
        examples = listOf(
            ex("\"Gempa bumi tektonik terjadi akibat pergeseran lempeng. Lempeng yang saling menekan menyimpan energi. Ketika batuan tidak mampu lagi menahan tekanan, energi itu dilepaskan sebagai getaran.\" Termasuk jenis teks apa?",
                "Teks menjelaskan proses terjadinya fenomena alam secara berurutan dengan hubungan sebab-akibat.",
                answer = "Teks eksplanasi"),
            ex("Kalimat penutup teks eksposisi berbunyi \"Dengan demikian, pembatasan gawai di kelas perlu diterapkan agar siswa lebih fokus belajar.\" Bagian apakah ini?",
                "Kalimat itu menegaskan kembali pendapat penulis setelah argumen disampaikan.",
                answer = "Penegasan ulang"),
        ),
        pitfalls = listOf(
            "Menganggap teks tentang banjir pasti eksplanasi. Jika isinya pendapat penulis tentang cara mengatasi banjir, teks itu eksposisi.",
        ),
        points = listOf(
            "Eksposisi: tesis, argumen, penegasan ulang.",
            "Eksplanasi: identifikasi fenomena, rangkaian kejadian, ulasan.",
            "Laporan hasil observasi: pernyataan umum, deskripsi bagian, simpulan.",
            "Berita: 5W + 1H, piramida terbalik.",
        ),
    )

    val grafikInfografik = Section(
        title = "Membaca grafik, tabel & infografik",
        explain = listOf(
            "Soal grafik dan tabel menguji ketelitian membandingkan angka, bukan kemampuan berhitung yang rumit. Baca judul, satuan, dan keterangan sumbu atau kolom lebih dulu. Satuan yang terlewat, misalnya persen dan juta jiwa, sering menjadi sumber kesalahan.",
            "Ada tiga jenis pertanyaan yang sering muncul. Pertama, pernyataan yang SESUAI dengan data. Setiap kata pada pilihan jawaban harus didukung angka, termasuk kata \"selalu\", \"terus\", atau \"paling\". Kedua, simpulan, yaitu pola umum yang terlihat dari keseluruhan data. Ketiga, pernyataan yang TIDAK SESUAI, biasanya berisi kata \"terus meningkat\" padahal ada tahun yang turun.",
            "Pada infografik, gambar hanya membantu. Informasi yang ditanyakan selalu ada pada teks atau angka infografik. Jawaban yang memerlukan pengetahuan di luar infografik bukan jawaban yang benar.",
        ),
        examples = listOf(
            ex("Tabel pengunjung perpustakaan sekolah: 2021: 1.200 orang; 2022: 1.800 orang; 2023: 1.500 orang. Apakah pernyataan \"jumlah pengunjung terus meningkat\" sesuai dengan data?",
                "Dari 2021 ke 2022 naik 600 orang, tetapi dari 2022 ke 2023 turun 300 orang.",
                answer = "Tidak sesuai, karena pada 2023 jumlah pengunjung turun"),
            ex("Dari tabel yang sama, kenaikan pengunjung 2021 ke 2022 berapa persen?",
                "Kenaikan 600 dari 1.200.",
                "§\\frac{600}{1.200} \\times 100\\% = 50\\%§.",
                answer = "50%"),
        ),
        pitfalls = listOf(
            "Memilih pernyataan yang benar menurut pengetahuan umum tetapi tidak tercantum dalam data.",
            "Menghitung persen kenaikan dengan membagi data tahun baru, bukan tahun lama.",
        ),
        points = listOf(
            "Baca judul, satuan, dan keterangan dulu.",
            "Kata \"terus\", \"selalu\", \"paling\" harus didukung semua data.",
            "Jawaban hanya dari data yang tersedia.",
        ),
    )

    val kalimatEfektif = Section(
        title = "Kalimat efektif",
        explain = listOf(
            "Kalimat efektif menyampaikan gagasan dengan tepat dan hemat kata. Ciri-cirinya adalah memiliki subjek dan predikat yang jelas, tidak mengulang makna (tidak pleonastis), memakai kata baku, dan bagian-bagiannya sejajar.",
            "Subjek kalimat hilang jika kalimat diawali kata depan seperti \"di\", \"dalam\", \"bagi\", atau \"kepada\" yang diikuti predikat. Contoh tidak efektif: \"Bagi siswa yang terlambat harus melapor ke guru piket.\" Kata \"bagi\" dihapus agar \"siswa yang terlambat\" menjadi subjek.",
            "Pleonasme adalah pemakaian kata yang maknanya sudah tercakup kata lain, misalnya \"naik ke atas\", \"para siswa-siswa\", \"sangat indah sekali\", atau \"agar supaya\". Cukup pakai salah satu kata.",
            "Kesejajaran berarti bentuk kata dalam rincian harus seragam. \"Kegiatan itu meliputi pendataan, penyuluhan, dan memeriksa kesehatan\" tidak sejajar; kata terakhir diganti \"pemeriksaan kesehatan\".",
        ),
        examples = listOf(
            ex("Perbaiki kalimat: \"Para hadirin-hadirin dimohon berdiri.\"",
                "\"Para\" sudah menyatakan jamak, dan \"hadirin\" juga bermakna jamak.",
                answer = "Hadirin dimohon berdiri."),
            ex("Perbaiki kalimat: \"Di dalam rapat itu membahas anggaran kegiatan.\"",
                "Kalimat tidak bersubjek karena \"di dalam rapat itu\" adalah keterangan.",
                answer = "Rapat itu membahas anggaran kegiatan."),
        ),
        pitfalls = listOf(
            "Menganggap kalimat panjang pasti tidak efektif. Ukurannya adalah kejelasan struktur, bukan panjangnya.",
        ),
        points = listOf(
            "Ada subjek dan predikat; kata depan di awal sering menghilangkan subjek.",
            "Tanpa pleonasme: naik ke atas, para ...-..., sangat ... sekali.",
            "Rincian sejajar bentuknya.",
        ),
    )
}
