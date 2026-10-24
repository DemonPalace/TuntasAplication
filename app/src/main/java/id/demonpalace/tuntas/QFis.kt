package id.demonpalace.tuntas

/**
 * Chapter quizzes for the Fisika book. Numbers follow the style of the TKA Fisika models
 * (GNFI/Pusmendik, Blog ITERA); g = 10 m/s² unless stated. Every key was recomputed.
 */
object QFis {
    private fun q(id: String, text: String, options: List<String>, answer: Int, explain: String) =
        Question(id, text, options, answer, explain)

    private fun multi(id: String, text: String, options: List<String>, keys: Set<Int>, explain: String) =
        Question(id, text, options, keys.min(), explain, multi = keys)

    private fun table(id: String, text: String, rows: List<String>, keys: List<Int>, explain: String, cols: List<String> = listOf("Benar", "Salah")) =
        Question(id, text, cols, keys.first(), explain, rows = rows, rowKeys = keys)

    val besaran: List<Question> by lazy { besaranQ() }
    val gerakLurus: List<Question> by lazy { gerakLurusQ() }
    val parabola: List<Question> by lazy { parabolaQ() }
    val newton: List<Question> by lazy { newtonQ() }
    val energi: List<Question> by lazy { energiQ() }
    val rotasi: List<Question> by lazy { rotasiQ() }
    val fluida: List<Question> by lazy { fluidaQ() }
    val gelombang: List<Question> by lazy { gelombangQ() }
    val optik: List<Question> by lazy { optikQ() }
    val kalor: List<Question> by lazy { kalorQ() }
    val termo: List<Question> by lazy { termoQ() }
    val statis: List<Question> by lazy { statisQ() }
    val arus: List<Question> by lazy { arusQ() }

    private fun besaranQ() = listOf(
        q("fv1", "Dimensi gaya adalah ....",
            listOf("§[M][L][T]^{-1}§", "§[M][L][T]^{-2}§", "§[M][L]^2[T]^{-2}§", "§[M][L]^{-1}[T]^{-2}§", "§[M][T]^{-2}§"), 1,
            "§F = ma§: massa §[M]§ dikali percepatan §[L][T]^{-2}§."),
        multi("fv2", "Pilih semua besaran pokok dalam SI.",
            listOf("Massa", "Kecepatan", "Suhu", "Gaya", "Kuat arus listrik"), setOf(0, 2, 4),
            "Kecepatan dan gaya adalah besaran turunan. Massa, suhu, dan kuat arus termasuk tujuh besaran pokok."),
        q("fv3", "Dua gaya 3 N dan 4 N bekerja pada satu titik dan saling tegak lurus. Besar resultannya adalah ....",
            listOf("1 N", "5 N", "7 N", "12 N", "§\\sqrt{7}§ N"), 1,
            "Sudut §90^\\circ§, jadi §R = \\sqrt{3^2 + 4^2} = 5§ N."),
        q("fv4", "Dua gaya 6 N dan 8 N membentuk sudut §60^\\circ§. Besar resultannya adalah ....",
            listOf("10 N", "14 N", "§2\\sqrt{37}§ N", "§2\\sqrt{13}§ N", "§\\sqrt{52}§ N"), 2,
            "§R^2 = 36 + 64 + 2(6)(8)(0{,}5) = 148§, jadi §R = 2\\sqrt{37}§ N."),
        q("fv5", "Gaya 10 N membentuk sudut §37^\\circ§ terhadap sumbu x (§\\sin 37^\\circ = 0{,}6§). Komponen gaya pada sumbu x dan y adalah ....",
            listOf("6 N dan 8 N", "8 N dan 6 N", "5 N dan 5 N", "10 N dan 0 N", "7 N dan 3 N"), 1,
            "§F_x = 10\\cos 37^\\circ = 8§ N dan §F_y = 10\\sin 37^\\circ = 6§ N."),
        q("fv6", "Skala utama mikrometer sekrup menunjukkan 5,5 mm dan skala putar menunjukkan angka 28. Hasil pengukurannya adalah ....",
            listOf("5,28 mm", "5,78 mm", "5,53 mm", "8,30 mm", "5,58 mm"), 1,
            "§5{,}5 + 28 \\times 0{,}01 = 5{,}78§ mm."),
    )

    private fun gerakLurusQ() = listOf(
        q("fg1", "Mobil melaju tetap §72\\,\\mathrm{km} / \\mathrm{jam}§ selama 10 menit. Jarak yang ditempuh adalah ....",
            listOf("1,2 km", "7,2 km", "12 km", "20 km", "720 m"), 2,
            "§72\\,\\mathrm{km} / \\mathrm{jam} = 20\\,\\mathrm{m} / \\mathrm{s}§ dan 10 menit = 600 s. §s = 20 \\times 600 = 12.000§ m."),
        q("fg2", "Benda mula-mula diam dipercepat §4\\,\\mathrm{m} / \\mathrm{s}^{2}§. Kecepatannya setelah menempuh 50 m adalah ....",
            listOf("§10\\,\\mathrm{m} / \\mathrm{s}§", "§10\\sqrt{2}§ m/s", "§20\\,\\mathrm{m} / \\mathrm{s}§", "§40\\,\\mathrm{m} / \\mathrm{s}§", "§200\\,\\mathrm{m} / \\mathrm{s}§"), 2,
            "§v^2 = 0 + 2(4)(50) = 400§, jadi §v = 20§ m/s."),
        q("fg3", "Mobil yang melaju §20\\,\\mathrm{m} / \\mathrm{s}§ direm dengan perlambatan §5\\,\\mathrm{m} / \\mathrm{s}^{2}§. Jarak yang ditempuh sampai berhenti adalah ....",
            listOf("4 m", "20 m", "40 m", "80 m", "100 m"), 2,
            "§0 = 20^2 - 2(5)s§, jadi §s = 40§ m."),
        q("fg4", "Batu dijatuhkan bebas dari ketinggian 45 m. Waktu sampai di tanah adalah ....",
            listOf("2 s", "3 s", "4,5 s", "9 s", "§3\\sqrt{2}§ s"), 1,
            "§t = \\sqrt{\\frac{2h}{g}} = \\sqrt{9} = 3§ s."),
        q("fg5", "Bola dilempar vertikal ke atas dengan kecepatan §20\\,\\mathrm{m} / \\mathrm{s}§. Tinggi maksimumnya adalah ....",
            listOf("10 m", "20 m", "40 m", "2 m", "200 m"), 1,
            "§h = \\frac{v_0^2}{2g} = \\frac{400}{20} = 20§ m."),
        q("fg6", "Kota A dan B berjarak 90 km. Mobil P berangkat dari A dengan kelajuan §40\\,\\mathrm{km} / \\mathrm{jam}§, dan pada saat yang sama mobil Q berangkat dari B menuju A dengan kelajuan §50\\,\\mathrm{km} / \\mathrm{jam}§. Keduanya bertemu pada jarak ... dari A.",
            listOf("30 km", "40 km", "45 km", "50 km", "60 km"), 1,
            "Waktu bertemu §\\frac{90}{40 + 50} = 1§ jam, jadi P sudah menempuh 40 km."),
        table("fg7", "Tentukan benar atau salah setiap pernyataan berikut.",
            listOf("Pada GLB, percepatan benda nol.", "Benda jatuh bebas bertambah kecepatannya §10\\,\\mathrm{m} / \\mathrm{s}§ setiap sekon §(g = 10\\,\\mathrm{m} / \\mathrm{s}^{2})§.",
                "Di titik tertinggi gerak vertikal ke atas, percepatan benda nol.", "Jarak tempuh sama dengan luas daerah di bawah grafik v–t."),
            listOf(0, 0, 1, 0),
            "Di titik tertinggi kecepatannya nol, tetapi percepatannya tetap g ke bawah. Tiga pernyataan lain benar."),
    )

    private fun parabolaQ() = listOf(
        q("fp1", "Peluru ditembakkan dengan kecepatan §40\\,\\mathrm{m} / \\mathrm{s}§ dan sudut elevasi §30^\\circ§. Waktu untuk mencapai titik tertinggi adalah ....",
            listOf("1 s", "2 s", "§2\\sqrt{3}§ s", "4 s", "8 s"), 1,
            "§t = \\frac{v_0\\sin\\theta}{g} = \\frac{40 \\times 0{,}5}{10} = 2§ s."),
        q("fp2", "Pada soal sebelumnya, jarak mendatar terjauh peluru adalah ....",
            listOf("80 m", "§80\\sqrt{3}§ m", "160 m", "§40\\sqrt{3}§ m", "§160\\sqrt{3}§ m"), 1,
            "§R = \\frac{v_0^2\\sin 60^\\circ}{g} = \\frac{1.600 \\times \\frac{1}{2}\\sqrt{3}}{10} = 80\\sqrt{3}§ m."),
        q("fp3", "Bola menggelinding dari tepi meja setinggi 1,25 m dengan kecepatan mendatar §4\\,\\mathrm{m} / \\mathrm{s}§. Jarak mendatar bola saat menyentuh lantai dari kaki meja adalah ....",
            listOf("1 m", "2 m", "4 m", "5 m", "0,5 m"), 1,
            "Waktu jatuh §\\sqrt{\\frac{2(1{,}25)}{10}} = 0{,}5§ s, jadi §x = 4 \\times 0{,}5 = 2§ m."),
        q("fp4", "Roda berputar 120 putaran per menit. Kecepatan sudutnya adalah ....",
            listOf("§2\\pi§ §\\mathrm{rad} / \\mathrm{s}§", "§4\\pi§ §\\mathrm{rad} / \\mathrm{s}§", "§120\\pi§ §\\mathrm{rad} / \\mathrm{s}§", "§240\\pi§ §\\mathrm{rad} / \\mathrm{s}§", "§\\pi§ §\\mathrm{rad} / \\mathrm{s}§"), 1,
            "§f = 2§ Hz, jadi §\\omega = 2\\pi f = 4\\pi§ §\\mathrm{rad} / \\mathrm{s}§."),
        q("fp5", "Mobil bermassa 1.000 kg menikung pada jalan datar berjari-jari 50 m dengan kelajuan §10\\,\\mathrm{m} / \\mathrm{s}§. Gaya sentripetal yang diperlukan adalah ....",
            listOf("200 N", "500 N", "2.000 N", "5.000 N", "20.000 N"), 2,
            "§F = \\frac{mv^2}{r} = \\frac{1.000 \\times 100}{50} = 2.000§ N."),
        q("fp6", "Dua roda bersinggungan, jari-jarinya 10 cm dan 5 cm. Roda besar berputar dengan kecepatan sudut §20\\,\\mathrm{rad} / \\mathrm{s}§. Kecepatan sudut roda kecil adalah ....",
            listOf("§10\\,\\mathrm{rad} / \\mathrm{s}§", "§20\\,\\mathrm{rad} / \\mathrm{s}§", "§40\\,\\mathrm{rad} / \\mathrm{s}§", "§80\\,\\mathrm{rad} / \\mathrm{s}§", "§5\\,\\mathrm{rad} / \\mathrm{s}§"), 2,
            "Roda bersinggungan punya kelajuan linear sama: §20 \\times 0{,}1 = \\omega \\times 0{,}05§, jadi §\\omega = 40§ §\\mathrm{rad} / \\mathrm{s}§."),
    )

    private fun newtonQ() = listOf(
        q("fn1", "Balok 2 kg meluncur pada bidang miring licin bersudut §30^\\circ§. Percepatan balok adalah ....",
            listOf("§2{,}5\\,\\mathrm{m} / \\mathrm{s}^{2}§", "§5\\,\\mathrm{m} / \\mathrm{s}^{2}§", "§5\\sqrt{3}§ m/s²", "§10\\,\\mathrm{m} / \\mathrm{s}^{2}§", "§20\\,\\mathrm{m} / \\mathrm{s}^{2}§"), 1,
            "Bidang licin: §a = g\\sin 30^\\circ = 5§ m/s². Massa tidak berpengaruh."),
        q("fn2", "Beban 3 kg dan 2 kg digantung pada ujung-ujung tali yang melewati katrol licin. Percepatan sistem adalah ....",
            listOf("§1\\,\\mathrm{m} / \\mathrm{s}^{2}§", "§2\\,\\mathrm{m} / \\mathrm{s}^{2}§", "§5\\,\\mathrm{m} / \\mathrm{s}^{2}§", "§6\\,\\mathrm{m} / \\mathrm{s}^{2}§", "§10\\,\\mathrm{m} / \\mathrm{s}^{2}§"), 1,
            "§a = \\frac{(3 - 2)(10)}{3 + 2} = 2§ m/s²."),
        q("fn3", "Pada sistem soal sebelumnya, tegangan talinya adalah ....",
            listOf("20 N", "24 N", "25 N", "30 N", "50 N"), 1,
            "Beban 2 kg bergerak naik: §T - 20 = 2 \\times 2§, jadi §T = 24§ N."),
        q("fn4", "Orang bermassa 60 kg berdiri di dalam lift yang bergerak naik dengan percepatan §2\\,\\mathrm{m} / \\mathrm{s}^{2}§. Gaya normal lantai lift pada orang itu adalah ....",
            listOf("480 N", "600 N", "620 N", "720 N", "120 N"), 3,
            "§N = m(g + a) = 60 \\times 12 = 720§ N."),
        q("fn5", "Balok 10 kg di lantai dengan §\\mu_s = 0{,}4§ dan §\\mu_k = 0{,}3§ ditarik mendatar 30 N. Gaya gesek yang bekerja pada balok adalah ....",
            listOf("0 N", "30 N", "40 N", "300 N", "70 N"), 1,
            "Gesek statis maksimum §0{,}4 \\times 100 = 40§ N lebih besar dari tarikan, jadi balok diam dan gaya geseknya 30 N."),
        multi("fn6", "Pilih semua pasangan aksi-reaksi.",
            listOf("Berat buku dan gaya normal meja pada buku", "Tangan mendorong dinding dan dinding mendorong tangan",
                "Bumi menarik bulan dan bulan menarik bumi", "Gaya tarik dan gaya gesek pada balok yang sama",
                "Dayung mendorong air ke belakang dan air mendorong dayung ke depan"), setOf(1, 2, 4),
            "Aksi-reaksi bekerja pada dua benda yang berbeda. Pasangan A dan D bekerja pada satu benda yang sama."),
    )

    private fun energiQ() = listOf(
        q("fe1", "Gaya 50 N membentuk sudut §60^\\circ§ terhadap arah perpindahan dan memindahkan balok sejauh 4 m. Usaha gaya itu adalah ....",
            listOf("100 J", "§100\\sqrt{3}§ J", "200 J", "50 J", "400 J"), 0,
            "§W = Fs\\cos\\theta = 50 \\times 4 \\times 0{,}5 = 100§ J."),
        q("fe2", "Pompa berdaya 500 W menaikkan air setinggi 10 m selama 1 menit. Massa air yang dinaikkan adalah ....",
            listOf("50 kg", "150 kg", "300 kg", "500 kg", "3.000 kg"), 2,
            "§Pt = mgh§: §500 \\times 60 = m \\times 10 \\times 10§, jadi §m = 300§ kg."),
        q("fe3", "Bola 1 kg jatuh bebas dari ketinggian 20 m. Kecepatannya saat berada 15 m di atas tanah adalah ....",
            listOf("§5\\,\\mathrm{m} / \\mathrm{s}§", "§10\\,\\mathrm{m} / \\mathrm{s}§", "§15\\,\\mathrm{m} / \\mathrm{s}§", "§20\\,\\mathrm{m} / \\mathrm{s}§", "§10\\sqrt{3}§ m/s"), 1,
            "Bola sudah turun 5 m: §v = \\sqrt{2 \\times 10 \\times 5} = 10§ m/s."),
        q("fe4", "Peluru 20 g bergerak §400\\,\\mathrm{m} / \\mathrm{s}§ menumbuk dan bersarang di balok 1,98 kg yang diam. Kecepatan balok sesaat setelah tumbukan adalah ....",
            listOf("§2\\,\\mathrm{m} / \\mathrm{s}§", "§4\\,\\mathrm{m} / \\mathrm{s}§", "§8\\,\\mathrm{m} / \\mathrm{s}§", "§20\\,\\mathrm{m} / \\mathrm{s}§", "§40\\,\\mathrm{m} / \\mathrm{s}§"), 1,
            "§0{,}02 \\times 400 = (0{,}02 + 1{,}98)v§, jadi §v = 4§ m/s."),
    )

    private fun rotasiQ() = listOf(
        q("fr1", "Batang homogen 4 m bermassa 20 kg ditopang di ujung A dan B. Beban 40 kg diletakkan 1 m dari A. Gaya topang di A adalah ....",
            listOf("200 N", "300 N", "400 N", "500 N", "600 N"), 2,
            "Poros di B: §N_A(4) = 200(2) + 400(3)§, jadi §N_A = 400§ N."),
        q("fr2", "Anak bermassa 30 kg duduk 2 m dari poros jungkat-jungkit. Agar seimbang, anak bermassa 40 kg harus duduk di sisi lain pada jarak ....",
            listOf("1 m", "1,5 m", "2 m", "2,5 m", "2,67 m"), 1,
            "§30 \\times 2 = 40 \\times x§, jadi §x = 1{,}5§ m."),
        q("fr3", "Momen inersia batang homogen 2 kg sepanjang 1,2 m terhadap sumbu yang melalui pusatnya adalah ....",
            listOf("0,12 kg §m^{2}§", "0,24 kg §m^{2}§", "0,48 kg §m^{2}§", "0,96 kg §m^{2}§", "2,88 kg §m^{2}§"), 1,
            "§\\frac{1}{12}mL^2 = \\frac{1}{12}(2)(1{,}44) = 0{,}24§ kg §m^{2}§."),
        q("fr4", "Benda 2 kg terletak di §x = 0§ dan benda 3 kg di §x = 5\\,\\mathrm{m}§. Letak titik beratnya adalah ....",
            listOf("§x = 2\\,\\mathrm{m}§", "§x = 2{,}5\\,\\mathrm{m}§", "§x = 3\\,\\mathrm{m}§", "§x = 3{,}5\\,\\mathrm{m}§", "§x = 4\\,\\mathrm{m}§"), 2,
            "§x_0 = \\frac{2(0) + 3(5)}{5} = 3§ m."),
        q("fr5", "Roda bermomen inersia 0,5 kg §m^{2}§ diberi torsi 2 N m. Percepatan sudutnya adalah ....",
            listOf("§1\\,\\mathrm{rad} / \\mathrm{s}^{2}§", "§2\\,\\mathrm{rad} / \\mathrm{s}^{2}§", "§4\\,\\mathrm{rad} / \\mathrm{s}^{2}§", "§0{,}25\\,\\mathrm{rad} / \\mathrm{s}^{2}§", "§8\\,\\mathrm{rad} / \\mathrm{s}^{2}§"), 2,
            "§\\alpha = \\frac{\\tau}{I} = \\frac{2}{0{,}5} = 4§ §\\mathrm{rad} / \\mathrm{s}^{2}§."),
        q("fr6", "Silinder pejal bermassa 2 kg menggelinding tanpa slip dengan kelajuan §4\\,\\mathrm{m} / \\mathrm{s}§. Energi kinetik totalnya adalah ....",
            listOf("16 J", "20 J", "24 J", "32 J", "8 J"), 2,
            "§E_k = \\frac{3}{4}mv^2 = \\frac{3}{4}(2)(16) = 24§ J."),
    )

    private fun fluidaQ() = listOf(
        q("ff1", "Balok es (§\\rho = 900§ kg/m³) terapung di air (§\\rho = 1.000§ kg/m³). Bagian es yang muncul di atas permukaan air adalah ....",
            listOf("10%", "20%", "80%", "90%", "50%"), 0,
            "Bagian tercelup §\\frac{900}{1.000} = 90\\%§, jadi yang muncul 10%."),
        q("ff2", "Tekanan udara di permukaan danau §10^5§ Pa. Tekanan total pada kedalaman 10 m adalah ....",
            listOf("§10^5§ Pa", "§1{,}1 \\times 10^5§ Pa", "§2 \\times 10^5§ Pa", "§10^6§ Pa", "§2 \\times 10^6§ Pa"), 2,
            "§P = P_0 + \\rho gh = 10^5 + 1.000(10)(10) = 2 \\times 10^5§ Pa."),
        q("ff3", "Tangki terbuka berisi air memiliki lubang kecil 5 m di bawah permukaan air. Kecepatan air yang keluar dari lubang adalah ....",
            listOf("§5\\,\\mathrm{m} / \\mathrm{s}§", "§5\\sqrt{2}§ m/s", "§10\\,\\mathrm{m} / \\mathrm{s}§", "§50\\,\\mathrm{m} / \\mathrm{s}§", "§100\\,\\mathrm{m} / \\mathrm{s}§"), 2,
            "Teorema Torricelli: §v = \\sqrt{2gh} = \\sqrt{100} = 10§ m/s."),
    )

    private fun gelombangQ() = listOf(
        q("fw1", "Gelombang berjalan memenuhi §y = 0{,}05\\sin(10\\pi t - 2\\pi x)§ dalam SI. Cepat rambat gelombang adalah ....",
            listOf("§1\\,\\mathrm{m} / \\mathrm{s}§", "§2\\,\\mathrm{m} / \\mathrm{s}§", "§5\\,\\mathrm{m} / \\mathrm{s}§", "§10\\,\\mathrm{m} / \\mathrm{s}§", "§20\\,\\mathrm{m} / \\mathrm{s}§"), 2,
            "§v = \\frac{\\omega}{k} = \\frac{10\\pi}{2\\pi} = 5§ m/s."),
        q("fw2", "Pipa organa terbuka sepanjang 50 cm. Jika cepat rambat bunyi §340\\,\\mathrm{m} / \\mathrm{s}§, frekuensi nada dasarnya adalah ....",
            listOf("170 Hz", "340 Hz", "680 Hz", "85 Hz", "510 Hz"), 1,
            "§f_0 = \\frac{v}{2L} = \\frac{340}{1} = 340§ Hz."),
        q("fw3", "Taraf intensitas satu mesin 60 dB. Taraf intensitas 10 mesin identik yang berbunyi bersamaan adalah ....",
            listOf("61 dB", "70 dB", "80 dB", "600 dB", "66 dB"), 1,
            "§TI_n = 60 + 10\\log 10 = 70§ dB."),
        q("fw4", "Dawai bermassa per satuan panjang §0{,}01\\,\\mathrm{kg} / \\mathrm{m}§ ditegangkan dengan gaya 100 N. Cepat rambat gelombang pada dawai adalah ....",
            listOf("§10\\,\\mathrm{m} / \\mathrm{s}§", "§100\\,\\mathrm{m} / \\mathrm{s}§", "§1.000\\,\\mathrm{m} / \\mathrm{s}§", "§1\\,\\mathrm{m} / \\mathrm{s}§", "§50\\,\\mathrm{m} / \\mathrm{s}§"), 1,
            "§v = \\sqrt{\\frac{F}{\\mu}} = \\sqrt{10.000} = 100§ m/s."),
    )

    private fun optikQ() = listOf(
        q("fo1", "Benda diletakkan 15 cm di depan lensa cembung berfokus 10 cm. Perbesaran bayangannya adalah ....",
            listOf("0,5 kali", "1 kali", "1,5 kali", "2 kali", "3 kali"), 3,
            "§\\frac{1}{s'} = \\frac{1}{10} - \\frac{1}{15} = \\frac{1}{30}§, jadi §s' = 30§ cm dan §M = \\frac{30}{15} = 2§."),
        q("fo2", "Titik jauh mata seorang penderita miopi 200 cm. Kekuatan lensa kacamata yang ia perlukan adalah ....",
            listOf("−2 D", "−0,5 D", "0,5 D", "2 D", "−5 D"), 1,
            "§P = -\\frac{100}{200} = -0{,}5§ dioptri (lensa cekung)."),
        q("fo3", "Titik dekat mata seorang penderita hipermetropi 50 cm. Agar dapat membaca pada jarak 25 cm, kekuatan lensa kacamatanya adalah ....",
            listOf("1 D", "2 D", "−2 D", "4 D", "0,5 D"), 1,
            "§P = \\frac{100}{25} - \\frac{100}{50} = 4 - 2 = 2§ dioptri."),
        q("fo4", "Pada percobaan celah ganda, jarak antarcelah 0,2 mm, jarak layar 1 m, dan panjang gelombang cahaya 500 nm. Jarak terang pertama dari terang pusat adalah ....",
            listOf("0,25 mm", "1,25 mm", "2,5 mm", "5 mm", "25 mm"), 2,
            "§y = \\frac{\\lambda L}{d} = \\frac{5 \\times 10^{-7} \\times 1}{2 \\times 10^{-4}} = 2{,}5 \\times 10^{-3}§ m."),
        q("fo5", "Cahaya 600 nm dijatuhkan tegak lurus pada kisi 500 garis/mm. Orde terang maksimum yang dapat diamati adalah ....",
            listOf("1", "2", "3", "4", "5"), 2,
            "§d = 2 \\times 10^{-6}§ m. §n \\leq \\frac{d}{\\lambda} = 3{,}33§, jadi orde maksimum 3."),
        q("fo6", "Lup berfokus 5 cm digunakan oleh mata normal tanpa berakomodasi. Perbesaran anguler lup adalah ....",
            listOf("4 kali", "5 kali", "6 kali", "10 kali", "125 kali"), 1,
            "§M = \\frac{25}{f} = 5§ kali."),
    )

    private fun kalorQ() = listOf(
        q("fk1", "Suhu 77 °F sama dengan ....",
            listOf("25 °C", "45 °C", "35 °C", "60 °C", "298 °C"), 0,
            "§C = \\frac{5}{9}(77 - 32) = 25§ °C."),
        q("fk2", "Kalor yang diperlukan untuk melebur seluruh 500 g es bersuhu 0 °C (kalor lebur §336.000\\,\\mathrm{J} / \\mathrm{kg}§) adalah ....",
            listOf("16.800 J", "168.000 J", "336.000 J", "672.000 J", "84.000 J"), 1,
            "§Q = mL = 0{,}5 \\times 336.000 = 168.000§ J. Suhu tidak berubah selama melebur."),
        q("fk3", "Batang baja sepanjang 2 m (§\\alpha = 1{,}2 \\times 10^{-5}§ /°C) dipanaskan sehingga suhunya naik 50 °C. Pertambahan panjangnya adalah ....",
            listOf("0,12 mm", "0,6 mm", "1,2 mm", "6 mm", "12 mm"), 2,
            "§\\Delta L = 2 \\times 1{,}2 \\times 10^{-5} \\times 50 = 1{,}2 \\times 10^{-3}§ m."),
        q("fk4", "200 g air bersuhu 20 °C dicampur dengan 100 g air bersuhu 80 °C. Suhu akhir campuran adalah ....",
            listOf("30 °C", "40 °C", "50 °C", "60 °C", "35 °C"), 1,
            "§200(T - 20) = 100(80 - T)§, jadi §T = 40§ °C."),
        q("fk5", "Kalor dari matahari sampai ke bumi melalui ruang hampa dengan cara ....",
            listOf("konduksi", "konveksi", "radiasi", "induksi", "evaporasi"), 2,
            "Radiasi tidak memerlukan medium. Konduksi dan konveksi memerlukan zat perantara."),
    )

    private fun termoQ() = listOf(
        q("ft1", "Gas dalam wadah tertutup bersuhu 27 °C dipanaskan pada volume tetap hingga tekanannya dua kali semula. Suhu akhirnya adalah ....",
            listOf("54 °C", "300 °C", "327 °C", "600 °C", "273 °C"), 2,
            "Volume tetap: §\\frac{P}{T}§ tetap. §T_2 = 2 \\times 300 = 600§ §K = 327^\\circ C§."),
        q("ft2", "Suhu mutlak gas ideal dinaikkan dari 300 K menjadi 1.200 K. Kecepatan efektif (§v_{\\text{rms}}§) partikelnya menjadi ....",
            listOf("§\\frac{1}{2}§ kali", "2 kali", "4 kali", "§\\sqrt{2}§ kali", "16 kali"), 1,
            "§v_{\\text{rms}} \\propto \\sqrt{T}§, dan §\\sqrt{4} = 2§."),
        table("ft3", "Tentukan benar atau salah setiap pernyataan tentang gas ideal berikut.",
            listOf("Pada proses isotermik, perubahan energi dalamnya nol.", "Pada proses isokhorik, gas tidak melakukan usaha.",
                "Pada proses adiabatik, tidak ada kalor yang masuk atau keluar.", "Pada proses isobarik, tekanan gas berubah."),
            listOf(0, 0, 0, 1),
            "Isobarik berarti tekanan tetap. Tiga pernyataan lain benar: §\\Delta U = 0§ karena suhu tetap, §W = 0§ karena volume tetap, dan §Q = 0§ pada adiabatik."),
        q("ft4", "Efisiensi mesin Carnot 40% dan suhu reservoir tingginya 500 K. Suhu reservoir rendahnya adalah ....",
            listOf("200 K", "300 K", "320 K", "400 K", "250 K"), 1,
            "§0{,}4 = 1 - \\frac{T_r}{500}§, jadi §T_r = 300§ K."),
        q("ft5", "Mesin kalor menyerap 2.000 J dan membuang 1.500 J ke reservoir dingin dalam satu siklus. Efisiensinya adalah ....",
            listOf("15%", "25%", "33%", "75%", "50%"), 1,
            "§W = 2.000 - 1.500 = 500§ J, dan §\\eta = \\frac{500}{2.000} = 25\\%§."),
    )

    private fun statisQ() = listOf(
        q("fs1", "Kuat medan listrik pada titik yang berjarak 3 cm dari muatan 2 μC adalah ....",
            listOf("§2 \\times 10^5§ N/C", "§6 \\times 10^5§ N/C", "§2 \\times 10^7§ N/C", "§6 \\times 10^7§ N/C", "§2 \\times 10^9§ N/C"), 2,
            "§E = \\frac{kq}{r^2} = \\frac{9 \\times 10^9 \\times 2 \\times 10^{-6}}{9 \\times 10^{-4}} = 2 \\times 10^7§ N/C."),
        q("fs2", "Kapasitor 4 μF dan 12 μF dirangkai seri. Kapasitas penggantinya adalah ....",
            listOf("3 μF", "8 μF", "16 μF", "48 μF", "6 μF"), 0,
            "§\\frac{1}{C} = \\frac{1}{4} + \\frac{1}{12} = \\frac{4}{12}§, jadi §C = 3§ μF."),
        q("fs3", "Kapasitor 10 μF dihubungkan dengan sumber tegangan 12 V. Energi yang tersimpan adalah ....",
            listOf("§1{,}2 \\times 10^{-4}§ J", "§6 \\times 10^{-5}§ J", "§7{,}2 \\times 10^{-4}§ J", "§1{,}44 \\times 10^{-3}§ J", "§7{,}2 \\times 10^{-2}§ J"), 2,
            "§W = \\frac{1}{2}CV^2 = \\frac{1}{2}(10^{-5})(144) = 7{,}2 \\times 10^{-4}§ J."),
        q("fs4", "Potensial listrik pada titik yang berjarak 0,3 m dari muatan 3 μC adalah ....",
            listOf("§9 \\times 10^3§ V", "§9 \\times 10^4§ V", "§3 \\times 10^5§ V", "§2{,}7 \\times 10^4§ V", "§9 \\times 10^5§ V"), 1,
            "§V = \\frac{kq}{r} = \\frac{9 \\times 10^9 \\times 3 \\times 10^{-6}}{0{,}3} = 9 \\times 10^4§ V."),
    )

    private fun arusQ() = listOf(
        q("fa1", "Kawat A memiliki panjang L, luas penampang A, dan hambatan R. Kawat B sejenis memiliki panjang 2L dan luas penampang 2A. Hambatan kawat B adalah ....",
            listOf("§\\frac{1}{4}R§", "§\\frac{1}{2}R§", "R", "2R", "4R"), 2,
            "§R = \\rho\\frac{L}{A}§. Panjang dan luas sama-sama dikali 2, jadi hambatannya tetap."),
        q("fa2", "Pada sebuah titik cabang, arus 5 A masuk. Dari titik itu keluar arus 2 A dan arus I. Besar I adalah ....",
            listOf("2 A", "3 A", "5 A", "7 A", "10 A"), 1,
            "Hukum I Kirchhoff: arus masuk = arus keluar, §5 = 2 + I§."),
        q("fa3", "Baterai berggl 12 V dan berhambatan dalam 1 Ω dihubungkan dengan resistor 5 Ω. Tegangan jepit baterai adalah ....",
            listOf("2 V", "10 V", "11 V", "12 V", "6 V"), 1,
            "§I = \\frac{12}{5 + 1} = 2§ A, jadi §V = IR = 10§ V."),
        q("fa4", "Televisi 100 W menyala 5 jam sehari dan setrika 300 W dipakai 1 jam sehari. Jika tarif Rp§1.500 / \\mathrm{kWh}§, biaya listrik keduanya selama 30 hari adalah ....",
            listOf("Rp24.000", "Rp30.000", "Rp36.000", "Rp45.000", "Rp360.000"), 2,
            "Energi per hari §0{,}5 + 0{,}3 = 0{,}8§ kWh, sebulan 24 kWh. §24 \\times 1.500 = 36.000§."),
        q("fa5", "Tiga resistor masing-masing 6 Ω dirangkai paralel. Hambatan penggantinya adalah ....",
            listOf("2 Ω", "3 Ω", "6 Ω", "18 Ω", "12 Ω"), 0,
            "§\\frac{1}{R} = \\frac{3}{6}§, jadi §R = 2§ Ω."),
        q("fa6", "Lampu bertuliskan §60\\,\\mathrm{W} / 220\\,\\mathrm{V}§ dipasang pada tegangan 110 V. Daya lampu menjadi ....",
            listOf("15 W", "30 W", "60 W", "120 W", "7,5 W"), 0,
            "Hambatan lampu tetap, jadi §P \\propto V^2§: §60 \\times \\left(\\frac{110}{220}\\right)^2 = 15§ W."),
    )
}
