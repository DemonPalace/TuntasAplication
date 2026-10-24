package id.demonpalace.tuntas

/**
 * Chapter quizzes for the Bahasa Indonesia book. Passages are written for this app in the
 * format of the TKA SMA reading models (teks informasi and teks fiksi, tunggal and jamak).
 * Places and people in the news passages are fictional.
 */
object QBin {
    private fun q(id: String, text: String, options: List<String>, answer: Int, explain: String, passage: String? = null) =
        Question(id, text, options, answer, explain, passage)

    private fun multi(id: String, text: String, options: List<String>, keys: Set<Int>, explain: String, passage: String? = null) =
        Question(id, text, options, keys.min(), explain, passage, multi = keys)

    private fun table(id: String, text: String, cols: List<String>, rows: List<String>, keys: List<Int>, explain: String, passage: String? = null) =
        Question(id, text, cols, keys.first(), explain, passage, rows = rows, rowKeys = keys)

    val idePokok: List<Question> by lazy { idePokokQ() }
    val faktaOpini: List<Question> by lazy { faktaOpiniQ() }
    val jenisTeks: List<Question> by lazy { jenisTeksQ() }
    val teksJamak: List<Question> by lazy { teksJamakQ() }
    val kebahasaan: List<Question> by lazy { kebahasaanQ() }
    val cerita: List<Question> by lazy { ceritaQ() }
    val majas: List<Question> by lazy { majasQ() }
    val puisi: List<Question> by lazy { puisiQ() }
    val drama: List<Question> by lazy { dramaQ() }
    val kalimat: List<Question> by lazy { kalimatQ() }

    private const val PLTS =
        "Pemerintah menargetkan porsi energi baru terbarukan dalam bauran energi nasional sebesar 23 persen pada 2025. Hingga akhir 2023, capaiannya baru sekitar 13 persen. Pembangkit listrik tenaga surya (PLTS) atap dapat mempercepat pencapaian target itu karena bisa dipasang di rumah, sekolah, dan kantor tanpa membuka lahan baru. Akan tetapi, biaya pemasangan awal yang mencapai belasan juta rupiah membuat banyak rumah tangga menunda pemasangan. Skema cicilan dan insentif dari pemerintah daerah dapat menjadi jalan keluar bagi masalah biaya tersebut."

    private const val LITERASI =
        "(1) Hasil PISA 2022 menempatkan skor membaca siswa Indonesia pada angka 359, jauh di bawah rata-rata negara OECD sebesar 476. Skor itu menunjukkan bahwa sebagian besar siswa baru mampu menemukan informasi yang tersurat, tetapi kesulitan menarik simpulan dari bacaan yang panjang.\n\n" +
            "(2) Kemampuan membaca pemahaman tidak tumbuh dari kebiasaan membaca potongan teks pendek di media sosial. Kemampuan itu dilatih dengan membaca teks utuh, lalu menjawab pertanyaan tentang hubungan antarbagian teks. Beberapa sekolah mulai mewajibkan kegiatan membaca buku 15 menit sebelum pelajaran dimulai. Guru kemudian meminta siswa menuliskan ringkasan dan pertanyaan tentang bacaan itu."

    private const val JALAN =
        "Pemerintah Kota Sukamaju memperbaiki 12 ruas jalan yang rusak di empat kecamatan. Perbaikan menggunakan anggaran sebesar Rp8,4 miliar dari APBD 2024. Kepala Dinas Pekerjaan Umum Kota Sukamaju, Hendra Wijaya, mengatakan perbaikan ditargetkan selesai pada akhir Desember. \"Kami memprioritaskan jalan yang dilalui angkutan umum,\" ujarnya, Senin (14/10). Menurut saya, perbaikan ini sangat terlambat karena kerusakan sudah terjadi sejak awal tahun. Seorang pedagang di Jalan Melati mengaku pendapatannya turun sekitar 30 persen selama jalan rusak karena pembeli enggan melintas."

    private const val PERPUS =
        "Jumlah pengunjung Perpustakaan SMA Harapan\n2021: 1.200 orang\n2022: 1.800 orang\n2023: 1.500 orang\n2024: 2.100 orang\n\n" +
            "Kepala perpustakaan menyampaikan bahwa jumlah pengunjung terus meningkat setiap tahun sejak 2021. Kenaikan terbesar terjadi pada 2024 setelah perpustakaan menyediakan ruang diskusi dan akses internet."

    private const val GEMPA =
        "Gempa bumi tektonik terjadi karena pergerakan lempeng bumi. Lempeng-lempeng itu bergerak sangat lambat, hanya beberapa sentimeter per tahun. Di batas lempeng, gerakan tersebut tertahan oleh gesekan sehingga energi tersimpan dalam batuan. Ketika batuan tidak mampu lagi menahan tekanan, batuan patah dan energi dilepaskan dalam bentuk gelombang seismik. Gelombang inilah yang dirasakan sebagai guncangan di permukaan. Karena itu, wilayah yang berada di dekat batas lempeng, seperti pantai barat Sumatra dan selatan Jawa, lebih sering mengalami gempa."

    private const val GAWAI =
        "Penggunaan telepon genggam di kelas perlu dibatasi. Survei terhadap 300 siswa di sebuah SMA menunjukkan 62 persen siswa membuka media sosial saat pelajaran berlangsung. Selain itu, notifikasi yang terus muncul memecah konsentrasi sehingga siswa memerlukan waktu untuk kembali fokus. Beberapa negara, seperti Belanda, telah membatasi telepon genggam di kelas sejak 2024. Dengan demikian, sekolah sebaiknya menetapkan aturan penyimpanan telepon genggam selama jam pelajaran."

    private const val GAWAI2 =
        "Teks 1\nPenggunaan telepon genggam di kelas sebaiknya dilarang. Telepon genggam membuat siswa sering membuka media sosial dan sulit berkonsentrasi. Larangan penuh juga lebih mudah diawasi oleh guru.\n\n" +
            "Teks 2\nTelepon genggam tidak perlu dilarang sepenuhnya di kelas. Siswa memerlukannya untuk mencari referensi, mengerjakan kuis daring, dan memotret catatan. Yang diperlukan adalah aturan yang jelas: telepon genggam hanya dipakai saat guru memintanya."

    private const val SURVEI =
        "Hasil survei OSIS SMA Harapan tentang waktu belajar mandiri siswa kelas XII per hari (persentase siswa)\n\n" +
            "Kurang dari 1 jam: 2022: 45%, 2023: 38%, 2024: 30%\n1 sampai 2 jam: 2022: 40%, 2023: 42%, 2024: 44%\nLebih dari 2 jam: 2022: 15%, 2023: 20%, 2024: 26%"

    private const val CERPEN =
        "Sejak pagi, Laras duduk di depan meja jahit milik almarhumah ibunya. Mesin itu sudah berkarat, tetapi ia menolak menjualnya meskipun pedagang barang bekas menawar dengan harga tinggi. Lima tahun lalu, di meja yang sama, ibunya menjahit seragam sekolah Laras sambil bersenandung lagu lama. Laras masih ingat jari ibunya yang tertusuk jarum, tetapi ibunya hanya tertawa dan berkata, \"Kain yang bagus butuh kesabaran.\"\n\n" +
            "Kini Laras menerima pesanan jahitan dari tetangga. Hasilnya belum rapi dan beberapa pelanggan mengeluh. Laras membuka kembali buku catatan ibunya yang penuh pola. Malam itu, ia membongkar jahitan sebuah kebaya dan memulainya dari awal."

    private const val PUISI =
        "Ladang Ayah\n\nDi ladang yang retak oleh kemarau,\nayah menanam doa di setiap lubang benih.\nMatahari menggigit punggungnya,\ntetapi tangannya tak pernah berhenti.\n\nAku pulang membawa ijazah,\nia hanya tersenyum, menepuk tanah:\n\"Inilah sekolahku, Nak,\nyang mengajariku sabar menunggu hujan.\""

    private const val DRAMA =
        "(Ruang tamu sederhana. Bu Ratna duduk menjahit. Dimas masuk dengan tergesa-gesa sambil membawa amplop.)\n\n" +
            "DIMAS: Bu, Dimas diterima di kampus di Yogyakarta!\n" +
            "BU RATNA: (meletakkan jahitan, tersenyum lalu terdiam) Yogyakarta itu jauh, Dimas.\n" +
            "DIMAS: Ada beasiswa, Bu. Biaya kuliah tidak perlu dipikirkan.\n" +
            "BU RATNA: Lalu siapa yang membantu Ibu menjaga warung kalau kamu pergi?\n" +
            "DIMAS: (menunduk, suaranya pelan) Dimas sudah bicara dengan Rina. Ia mau menjaga warung sepulang sekolah.\n" +
            "BU RATNA: (menatap foto ayah Dimas di dinding, lama) Ayahmu dulu juga ingin kuliah. (menarik napas) Berangkatlah. Warung biar Ibu dan Rina yang urus."

    private fun idePokokQ() = listOf(
        q("ni1", "Ide pokok paragraf tersebut adalah ....",
            listOf("Target energi terbarukan 23 persen pada 2025", "PLTS atap dapat mempercepat target energi terbarukan, tetapi terkendala biaya awal",
                "Biaya pemasangan PLTS atap mencapai belasan juta rupiah", "Skema cicilan dari pemerintah daerah", "PLTS atap dapat dipasang di sekolah"), 1,
            "Seluruh kalimat membahas potensi PLTS atap dan hambatan biayanya. Pilihan lain hanya rincian.", PLTS),
        q("ni2", "Simpulan yang tepat berdasarkan paragraf tersebut adalah ....",
            listOf("Target energi terbarukan 2025 pasti tercapai.", "Rumah tangga tidak tertarik pada PLTS atap.",
                "Bantuan pembiayaan dapat mendorong pemasangan PLTS atap untuk mengejar target energi terbarukan.",
                "PLTS atap lebih mahal daripada pembangkit lain.", "Pemerintah daerah wajib membiayai seluruh PLTS atap."), 2,
            "Teks menyebut target belum tercapai, PLTS atap dapat membantu, dan biaya adalah hambatan yang dapat diatasi dengan cicilan atau insentif.", PLTS),
        q("ni3", "Informasi tersirat dalam paragraf tersebut adalah ....",
            listOf("PLTS atap tidak memerlukan lahan baru.", "Capaian energi terbarukan 2023 sekitar 13 persen.",
                "Tanpa bantuan pembiayaan, pemasangan PLTS atap oleh rumah tangga akan berjalan lambat.",
                "Target energi terbarukan 2025 sebesar 23 persen.", "Biaya pemasangan awal mencapai belasan juta rupiah."), 2,
            "Pilihan lain tertulis langsung di teks. Pilihan C disimpulkan dari hubungan antara biaya tinggi dan penundaan pemasangan.", PLTS),
        q("ni4", "Ide pokok paragraf (2) adalah ....",
            listOf("Skor membaca siswa Indonesia rendah.", "Media sosial merusak kemampuan membaca.",
                "Kemampuan membaca pemahaman dilatih dengan membaca teks utuh dan menjawab pertanyaan tentangnya.",
                "Sekolah mewajibkan membaca 15 menit.", "Guru meminta siswa menulis ringkasan."), 2,
            "Kalimat pertama dan kedua paragraf (2) memuat gagasan utama; kalimat berikutnya contoh penerapannya.", LITERASI),
        q("ni5", "Letak kalimat utama paragraf (1) dan jenis paragrafnya adalah ....",
            listOf("Awal paragraf, deduktif", "Akhir paragraf, induktif", "Awal dan akhir, campuran", "Tengah paragraf, deduktif", "Tidak ada kalimat utama, deskriptif"), 0,
            "Kalimat pertama menyatakan skor membaca yang rendah, lalu kalimat kedua menjelaskannya.", LITERASI),
        multi("ni6", "Pilih semua pernyataan yang sesuai dengan isi teks.",
            listOf("Skor membaca siswa Indonesia pada PISA 2022 di bawah rata-rata OECD.", "Sebagian besar siswa kesulitan menarik simpulan dari bacaan panjang.",
                "Membaca potongan teks di media sosial cukup untuk melatih pemahaman.", "Beberapa sekolah mewajibkan membaca buku sebelum pelajaran.",
                "Semua sekolah di Indonesia sudah mewajibkan membaca 15 menit."),
            setOf(0, 1, 3),
            "Teks menyatakan kebiasaan membaca potongan teks pendek tidak menumbuhkan kemampuan itu, dan yang mewajibkan baru beberapa sekolah.", LITERASI),
    )

    private fun faktaOpiniQ() = listOf(
        q("nf1", "Kalimat yang merupakan fakta dalam teks berita tersebut adalah ....",
            listOf("Perbaikan ini sangat terlambat.", "Perbaikan menggunakan anggaran sebesar Rp8,4 miliar dari APBD 2024.",
                "Jalan yang rusak membuat kota terlihat kumuh.", "Pemerintah seharusnya memperbaiki jalan sejak awal tahun.", "Anggaran itu terlalu kecil."), 1,
            "Angka anggaran dan sumbernya dapat dibuktikan. Pilihan lain berisi penilaian.", JALAN),
        q("nf2", "Kalimat opini dalam teks tersebut ditandai oleh ....",
            listOf("angka anggaran", "nama narasumber", "frasa \"menurut saya\" dan kata \"sangat terlambat\"", "nama jalan", "tanggal wawancara"), 2,
            "Frasa \"menurut saya\" dan penilaian \"sangat terlambat\" menunjukkan pendapat pribadi.", JALAN),
        multi("nf3", "Pilih semua kalimat yang berisi fakta.",
            listOf("Pemerintah Kota Sukamaju memperbaiki 12 ruas jalan di empat kecamatan.", "Perbaikan ditargetkan selesai pada akhir Desember.",
                "Perbaikan ini sangat terlambat.", "Seorang pedagang mengaku pendapatannya turun sekitar 30 persen.", "Kerusakan jalan adalah kesalahan dinas."),
            setOf(0, 1, 3),
            "Target dari kepala dinas dan pengakuan pedagang adalah peristiwa yang dilaporkan dan dapat diperiksa. Pilihan C dan E berisi penilaian.", JALAN),
        q("nf4", "Informasi yang belum terdapat dalam berita tersebut adalah ....",
            listOf("pihak yang memperbaiki jalan", "jumlah ruas jalan yang diperbaiki", "sumber anggaran", "tanggal dimulainya perbaikan", "alasan jalan angkutan umum diprioritaskan"), 3,
            "Berita menyebut target selesai, tetapi tidak menyebut kapan perbaikan dimulai.", JALAN),
        q("nf5", "Pernyataan kepala perpustakaan yang TIDAK sesuai dengan data adalah ....",
            listOf("Kenaikan terbesar terjadi pada 2024.", "Jumlah pengunjung terus meningkat setiap tahun sejak 2021.",
                "Pengunjung 2024 lebih banyak daripada 2021.", "Perpustakaan menyediakan ruang diskusi.", "Data dicatat sejak 2021."), 1,
            "Pada 2023 jumlah pengunjung turun dari 1.800 menjadi 1.500, jadi tidak terus meningkat.", PERPUS),
        table("nf6", "Tentukan apakah setiap kalimat berikut fakta atau opini.", listOf("Fakta", "Opini"),
            listOf("Perpustakaan SMA Harapan dikunjungi 2.100 orang pada 2024.", "Ruang diskusi membuat perpustakaan menjadi tempat paling nyaman di sekolah.",
                "Pengunjung tahun 2022 berjumlah 1.800 orang.", "Seharusnya perpustakaan buka sampai malam."),
            listOf(0, 1, 0, 1),
            "Kalimat 1 dan 3 berisi data dari tabel. \"Paling nyaman\" dan \"seharusnya\" adalah penilaian.", PERPUS),
    )

    private fun jenisTeksQ() = listOf(
        q("ns1", "Teks tersebut termasuk jenis teks ....",
            listOf("eksposisi", "eksplanasi", "laporan hasil observasi", "berita", "prosedur"), 1,
            "Teks menjelaskan proses terjadinya gempa secara berurutan dengan hubungan sebab-akibat.", GEMPA),
        q("ns2", "Kalimat yang merupakan identifikasi fenomena adalah ....",
            listOf("Gempa bumi tektonik terjadi karena pergerakan lempeng bumi.", "Lempeng-lempeng itu bergerak sangat lambat.",
                "Batuan patah dan energi dilepaskan.", "Gelombang inilah yang dirasakan sebagai guncangan.", "Wilayah di dekat batas lempeng lebih sering mengalami gempa."), 0,
            "Kalimat pertama memperkenalkan fenomena yang akan dijelaskan.", GEMPA),
        q("ns3", "Teks tersebut termasuk jenis teks ....",
            listOf("eksplanasi", "eksposisi", "berita", "laporan hasil observasi", "narasi"), 1,
            "Teks berisi pendapat penulis (telepon genggam perlu dibatasi), argumen berupa data, dan penegasan ulang.", GAWAI),
        q("ns4", "Kalimat \"Dengan demikian, sekolah sebaiknya menetapkan aturan penyimpanan telepon genggam selama jam pelajaran\" berfungsi sebagai ....",
            listOf("tesis", "argumen", "penegasan ulang", "identifikasi fenomena", "ulasan"), 2,
            "Kalimat itu menegaskan kembali pendapat di kalimat pertama setelah argumen disampaikan.", GAWAI),
        multi("ns5", "Pilih semua argumen yang dipakai penulis untuk mendukung pendapatnya.",
            listOf("Data survei 62 persen siswa membuka media sosial saat pelajaran", "Notifikasi memecah konsentrasi",
                "Telepon genggam berguna untuk mencari referensi", "Beberapa negara telah membatasi telepon genggam di kelas", "Harga telepon genggam makin murah"),
            setOf(0, 1, 3),
            "Pilihan C justru argumen pihak yang menolak pembatasan, dan pilihan E tidak ada di teks.", GAWAI),
        q("ns6", "Susunan kalimat berikut yang membentuk paragraf padu adalah ....\n(1) Akibatnya, air hujan langsung mengalir ke permukaan.\n(2) Hutan di hulu sungai banyak ditebang untuk perkebunan.\n(3) Banjir pun melanda permukiman di hilir.\n(4) Tanah kehilangan akar pohon yang menyerap air.",
            listOf("(2)-(4)-(1)-(3)", "(2)-(1)-(4)-(3)", "(4)-(2)-(1)-(3)", "(1)-(2)-(3)-(4)", "(2)-(3)-(4)-(1)"), 0,
            "Urutan sebab-akibat: hutan ditebang, tanah kehilangan akar, air mengalir di permukaan, lalu banjir."),
        q("ns7", "Bagian berita yang memuat informasi terpenting dan biasanya menjawab sebagian besar unsur 5W + 1H adalah ....",
            listOf("judul", "teras berita", "tubuh berita", "ekor berita", "kutipan narasumber"), 1,
            "Berita disusun dengan pola piramida terbalik: inti peristiwa ada di teras (paragraf pertama)."),
        q("ns8", "Ciri teks laporan hasil observasi adalah ....",
            listOf("berisi pendapat pribadi penulis", "menceritakan pengalaman tokoh secara kronologis",
                "menggambarkan objek secara umum berdasarkan pengamatan dengan bahasa objektif", "membujuk pembaca melakukan sesuatu", "berisi langkah-langkah membuat sesuatu"), 2,
            "Laporan hasil observasi bersifat objektif dan menyajikan klasifikasi serta deskripsi bagian objek."),
    )

    private fun teksJamakQ() = listOf(
        q("ng1", "Persamaan isi Teks 1 dan Teks 2 adalah ....",
            listOf("Keduanya menolak penggunaan telepon genggam di kelas.", "Keduanya mengakui bahwa penggunaan telepon genggam di kelas perlu diatur.",
                "Keduanya menyebut telepon genggam berguna untuk kuis daring.", "Keduanya menyarankan larangan penuh.", "Keduanya membahas harga telepon genggam."), 1,
            "Teks 1 memilih larangan dan Teks 2 memilih aturan pemakaian. Keduanya sama-sama menganggap penggunaannya perlu dikendalikan.", GAWAI2),
        q("ng2", "Perbedaan pandangan kedua teks adalah ....",
            listOf("Teks 1 mendukung larangan penuh, Teks 2 mendukung pemakaian terbatas dengan aturan.", "Teks 1 membahas siswa, Teks 2 membahas guru.",
                "Teks 1 memakai data, Teks 2 tidak.", "Teks 1 membahas media sosial, Teks 2 membahas televisi.", "Tidak ada perbedaan."), 0,
            "Teks 1 menyarankan \"dilarang\", Teks 2 menyatakan \"tidak perlu dilarang sepenuhnya\".", GAWAI2),
        q("ng3", "Argumen Teks 2 yang tidak dibahas dalam Teks 1 adalah ....",
            listOf("telepon genggam mengganggu konsentrasi", "larangan mudah diawasi", "telepon genggam berguna untuk kegiatan belajar",
                "siswa sering membuka media sosial", "guru kesulitan mengajar"), 2,
            "Teks 1 hanya membahas dampak buruk. Manfaat untuk belajar hanya ada di Teks 2.", GAWAI2),
        multi("ng4", "Pilih semua simpulan yang didukung oleh kedua teks.",
            listOf("Telepon genggam dapat mengganggu konsentrasi jika tidak diatur.", "Sekolah perlu membuat aturan tentang telepon genggam di kelas.",
                "Telepon genggam tidak bermanfaat sama sekali.", "Guru berperan dalam mengawasi penggunaan telepon genggam.", "Semua siswa memiliki telepon genggam."),
            setOf(0, 1, 3),
            "Kedua teks membahas gangguan konsentrasi, aturan, dan peran guru (mengawasi atau memberi izin). Pilihan C bertentangan dengan Teks 2, dan pilihan E tidak dibahas.", GAWAI2),
        q("ng5", "Berdasarkan data, kelompok siswa yang persentasenya terus meningkat dari 2022 sampai 2024 adalah ....",
            listOf("kurang dari 1 jam saja", "1 sampai 2 jam saja", "lebih dari 2 jam saja", "1 sampai 2 jam dan lebih dari 2 jam", "ketiga kelompok"), 3,
            "Kelompok 1 sampai 2 jam naik 40%, 42%, 44%, dan kelompok lebih dari 2 jam naik 15%, 20%, 26%.", SURVEI),
        q("ng6", "Kenaikan persentase terbesar dari 2022 ke 2024 terjadi pada kelompok ....",
            listOf("kurang dari 1 jam", "1 sampai 2 jam", "lebih dari 2 jam", "kurang dari 1 jam dan 1 sampai 2 jam", "tidak ada yang naik"), 2,
            "Lebih dari 2 jam naik 11 poin persen, sedangkan 1 sampai 2 jam naik 4 poin. Kelompok kurang dari 1 jam turun.", SURVEI),
        q("ng7", "Simpulan yang sesuai dengan data survei adalah ....",
            listOf("Waktu belajar mandiri siswa kelas XII cenderung bertambah dari tahun ke tahun.", "Sebagian besar siswa belajar lebih dari 2 jam per hari.",
                "Nilai ujian siswa meningkat setiap tahun.", "Pada 2024 tidak ada siswa yang belajar kurang dari 1 jam.", "Survei dilakukan di seluruh SMA."), 0,
            "Persentase kelompok waktu belajar singkat turun dan kelompok waktu belajar panjang naik. Data tidak memuat nilai ujian.", SURVEI),
        q("ng8", "Pernyataan yang TIDAK sesuai dengan data adalah ....",
            listOf("Pada 2024, siswa yang belajar 1 sampai 2 jam merupakan kelompok terbesar.", "Persentase siswa yang belajar kurang dari 1 jam menurun.",
                "Pada 2022, hampir separuh siswa belajar kurang dari 1 jam.", "Pada 2023, siswa yang belajar lebih dari 2 jam mencapai separuh siswa.",
                "Pada 2024, lebih dari seperempat siswa belajar lebih dari 2 jam."), 3,
            "Pada 2023 kelompok lebih dari 2 jam hanya 20%, bukan separuh.", SURVEI),
    )

    private fun kebahasaanQ() = listOf(
        q("nk1", "Konjungsi yang tepat untuk melengkapi kalimat \"... hujan turun deras, pertandingan tetap dilanjutkan\" adalah ....",
            listOf("Karena", "Meskipun", "Sehingga", "Kemudian", "Bahkan"), 1,
            "Hubungan kedua klausa bertentangan (konsesif), jadi dipakai \"meskipun\"."),
        q("nk2", "Kalimat yang memakai konjungsi akibat dengan tepat adalah ....",
            listOf("Ia belajar setiap malam sehingga nilainya meningkat.", "Ia belajar setiap malam meskipun nilainya meningkat.",
                "Ia belajar setiap malam namun nilainya meningkat.", "Sehingga ia belajar setiap malam, nilainya meningkat.", "Ia belajar setiap malam padahal nilainya meningkat."), 0,
            "\"Sehingga\" menghubungkan sebab dan akibat dalam satu kalimat dan tidak diletakkan di awal kalimat."),
        q("nk3", "Penulisan kata serapan yang baku terdapat pada kalimat ....",
            listOf("Kwalitas produk itu meningkat.", "Kualitas produk itu meningkat.", "Kwalitet produk itu meningkat.", "Kualiti produk itu meningkat.", "Kwaliti produk itu meningkat."), 1,
            "Bentuk baku menurut KBBI adalah \"kualitas\"."),
        multi("nk4", "Pilih semua kata baku.",
            listOf("analisis", "praktek", "risiko", "nasehat", "apotek"), setOf(0, 2, 4),
            "Bentuk baku: analisis, risiko, apotek. \"Praktek\" dan \"nasehat\" seharusnya \"praktik\" dan \"nasihat\"."),
        q("nk5", "Makna istilah \"mitigasi\" dalam kalimat \"Pemerintah daerah menyusun rencana mitigasi bencana banjir\" adalah ....",
            listOf("pemulihan setelah bencana", "upaya mengurangi risiko dan dampak bencana", "pemberian bantuan kepada korban", "penelitian penyebab bencana", "pemindahan penduduk"), 1,
            "Mitigasi adalah serangkaian upaya mengurangi risiko bencana, baik sebelum maupun saat bencana terjadi."),
        q("nk6", "Konjungsi antarkalimat yang tepat untuk mengisi bagian rumpang adalah ....\n\"Jumlah kendaraan bermotor di kota itu meningkat tajam. ... pemerintah memperluas layanan bus kota.\"",
            listOf("Meskipun demikian,", "Oleh karena itu,", "Sebaliknya,", "Bahkan,", "Sementara itu,"), 1,
            "Kalimat kedua adalah akibat atau tindakan yang diambil karena kalimat pertama."),
        q("nk7", "Kata berimbuhan yang penulisannya benar terdapat pada ....",
            listOf("mengkritik", "menyontek", "mempesona", "mensukseskan", "menterjemahkan"), 0,
            "\"Mengkritik\" baku karena kluster kr tidak luluh. Bentuk baku lainnya: mencontek, memesona, menyukseskan, menerjemahkan."),
    )

    private fun ceritaQ() = listOf(
        q("nc1", "Tema cerita tersebut adalah ....",
            listOf("persahabatan", "ketekunan meneruskan warisan orang tua", "kemiskinan keluarga penjahit", "persaingan dagang", "kehidupan di kota besar"), 1,
            "Laras mempertahankan mesin jahit ibunya dan belajar dari catatannya untuk memperbaiki hasil jahitannya.", CERPEN),
        q("nc2", "Alur yang digunakan pada paragraf pertama adalah ....",
            listOf("maju", "sorot balik (mundur)", "campuran tanpa kilas balik", "klimaks", "melingkar"), 1,
            "Kalimat \"Lima tahun lalu ...\" menyela cerita masa kini dengan peristiwa masa lalu.", CERPEN),
        q("nc3", "Watak Laras yang tergambar dalam cerita adalah ....",
            listOf("mudah menyerah", "sombong", "tekun dan mau belajar dari kesalahan", "boros", "pemarah"), 2,
            "Ia membongkar jahitan dan mengulang dari awal setelah pelanggan mengeluh.", CERPEN),
        q("nc4", "Amanat yang dapat diambil dari cerita tersebut adalah ....",
            listOf("Barang lama sebaiknya dijual.", "Pekerjaan yang baik memerlukan kesabaran dan kemauan memperbaiki diri.",
                "Pelanggan selalu benar.", "Anak harus bekerja sejak kecil.", "Mesin jahit modern lebih baik."), 1,
            "Pesan ibu \"Kain yang bagus butuh kesabaran\" diwujudkan Laras dengan mengulang jahitannya.", CERPEN),
        q("nc5", "Bagian cerita yang menunjukkan konflik batin tokoh adalah ....",
            listOf("Laras menolak menjual mesin jahit meskipun ditawar tinggi.", "Ibunya bersenandung lagu lama.", "Mesin itu sudah berkarat.",
                "Laras membuka buku catatan ibunya.", "Tetangga memesan jahitan."), 0,
            "Laras dihadapkan pada pilihan antara uang dan kenangan akan ibunya.", CERPEN),
        q("nc6", "Lanjutan cerita yang paling logis adalah ....",
            listOf("Laras menjual mesin jahit itu keesokan harinya.", "Laras berhenti menerima pesanan.",
                "Laras menyelesaikan kebaya dengan lebih rapi dengan mengikuti pola ibunya.", "Laras pindah ke kota lain.", "Pelanggan memaksa Laras mengganti rugi."), 2,
            "Cerita berakhir saat Laras mengulang jahitan dengan bantuan catatan ibunya, sehingga lanjutan yang sesuai adalah hasil yang lebih baik.", CERPEN),
    )

    private fun majasQ() = listOf(
        q("nm1", "\"Suaranya menggelegar sampai membelah langit.\" Majas dalam kalimat tersebut adalah ....",
            listOf("personifikasi", "metafora", "hiperbola", "litotes", "simile"), 2,
            "Pernyataan dilebih-lebihkan: suara tidak mungkin membelah langit."),
        q("nm2", "\"Mampirlah ke gubuk kami yang sederhana ini\" diucapkan pemilik rumah besar. Majas yang digunakan adalah ....",
            listOf("litotes", "hiperbola", "ironi", "metafora", "personifikasi"), 0,
            "Litotes merendahkan diri dengan menyebut rumah besar sebagai gubuk."),
        q("nm3", "Kalimat yang mengandung majas simile adalah ....",
            listOf("Dia kutu buku di kelas kami.", "Wajahnya bagai bulan purnama.", "Daun melambai memanggilku.", "Rumahnya sangat jauh sekali.", "Ia anak emas pak guru."), 1,
            "Simile membandingkan secara langsung dengan kata \"bagai\", \"seperti\", atau \"laksana\"."),
        q("nm4", "Makna ungkapan \"naik daun\" dalam kalimat \"Penyanyi muda itu sedang naik daun\" adalah ....",
            listOf("sedang sakit", "sedang terkenal", "sedang marah", "sedang bepergian", "sedang bangkrut"), 1,
            "\"Naik daun\" berarti sedang mendapat kemasyhuran atau keberuntungan."),
        q("nm5", "Sinonim kata \"signifikan\" dalam kalimat \"Jumlah pengunjung meningkat secara signifikan\" adalah ....",
            listOf("sedikit", "berarti", "perlahan", "sementara", "terus-menerus"), 1,
            "Signifikan berarti penting atau berarti."),
        q("nm6", "Kata \"memetik\" yang bermakna kias terdapat pada kalimat ....",
            listOf("Ibu memetik daun singkong di kebun.", "Petani memetik kopi saat panen.", "Ia memetik pelajaran dari kegagalannya.",
                "Anak itu memetik bunga di taman.", "Kami memetik buah mangga."), 2,
            "\"Memetik pelajaran\" berarti mengambil hikmah, bukan memetik secara harfiah."),
    )

    private fun puisiQ() = listOf(
        q("np1", "Tema puisi tersebut adalah ....",
            listOf("kegagalan panen", "keteguhan dan kesabaran seorang ayah petani", "keindahan alam desa", "pentingnya ijazah", "musim hujan"), 1,
            "Puisi menggambarkan ayah yang terus bekerja di ladang kering dan menyebut ladang sebagai tempatnya belajar sabar.", PUISI),
        q("np2", "Makna larik \"ayah menanam doa di setiap lubang benih\" adalah ....",
            listOf("ayah menanam benih sambil berharap hasilnya baik", "ayah berdoa di masjid", "ayah tidak menanam apa pun",
                "ayah menjual benih", "ayah meminta bantuan tetangga"), 0,
            "Doa yang ditanam adalah harapan yang menyertai setiap benih."),
        q("np3", "Majas pada larik \"Matahari menggigit punggungnya\" adalah ....",
            listOf("hiperbola", "personifikasi", "metonimia", "litotes", "repetisi"), 1,
            "Matahari diberi sifat manusia atau hewan, yaitu menggigit.", PUISI),
        q("np4", "Suasana yang tergambar pada bait pertama adalah ....",
            listOf("gembira", "haru dan berat", "tegang dan menakutkan", "santai", "marah"), 1,
            "Kemarau, ladang retak, dan panas matahari menggambarkan kerja keras yang berat.", PUISI),
        q("np5", "Citraan yang dominan pada larik \"Matahari menggigit punggungnya\" adalah ....",
            listOf("citraan pendengaran", "citraan perabaan", "citraan penciuman", "citraan pengecapan", "citraan gerak"), 1,
            "Rasa panas yang menyengat di punggung dirasakan melalui kulit.", PUISI),
        q("np6", "Makna \"Inilah sekolahku, Nak\" dalam bait kedua adalah ....",
            listOf("ayah pernah bersekolah di ladang", "ladang adalah tempat ayah belajar tentang kehidupan", "ayah tidak menghargai ijazah",
                "ayah ingin anaknya bertani", "ladang akan dijadikan sekolah"), 1,
            "Ayah tidak bersekolah tinggi, tetapi ladang mengajarinya kesabaran.", PUISI),
        q("np7", "Amanat puisi tersebut adalah ....",
            listOf("Bersekolahlah setinggi-tingginya.", "Kesabaran dan kerja keras adalah pelajaran hidup yang berharga.", "Jangan menjadi petani.",
                "Ijazah tidak penting.", "Berdoalah sebelum makan."), 1,
            "Ayah mengajarkan sabar menunggu hujan sambil terus bekerja.", PUISI),
        multi("np8", "Pilih semua pernyataan yang sesuai dengan puisi.",
            listOf("Ladang ayah mengalami kekeringan.", "Ayah tetap bekerja meskipun kepanasan.", "Ayah kecewa dengan ijazah anaknya.",
                "Aku dalam puisi baru menyelesaikan pendidikan.", "Hujan turun deras di akhir puisi."),
            setOf(0, 1, 3),
            "Ladang retak oleh kemarau, tangan ayah tak berhenti, dan aku pulang membawa ijazah. Ayah tersenyum, dan hujan masih ditunggu.", PUISI),
    )

    private fun dramaQ() = listOf(
        q("nd1", "Konflik utama dalam penggalan drama tersebut adalah ....",
            listOf("Dimas tidak diterima di kampus", "keinginan Dimas kuliah jauh berbenturan dengan kebutuhan ibunya menjaga warung",
                "Bu Ratna marah karena Dimas terlambat", "Rina menolak menjaga warung", "biaya kuliah terlalu mahal"), 1,
            "Bu Ratna mempertanyakan siapa yang membantu di warung jika Dimas pergi.", DRAMA),
        q("nd2", "Bagian \"(menatap foto ayah Dimas di dinding, lama)\" disebut ....",
            listOf("prolog", "epilog", "dialog", "teks samping (kramagung)", "monolog"), 3,
            "Teks dalam kurung adalah petunjuk gerak dan ekspresi tokoh."),
        q("nd3", "Watak Dimas berdasarkan dialognya adalah ....",
            listOf("egois dan keras kepala", "bertanggung jawab dan memikirkan keluarga", "pemalas", "pemarah", "penakut"), 1,
            "Dimas sudah mengatur agar Rina membantu warung sebelum meminta izin.", DRAMA),
        q("nd4", "Perubahan sikap Bu Ratna terjadi karena ....",
            listOf("Dimas memaksa", "Bu Ratna teringat keinginan ayah Dimas untuk kuliah", "Rina menangis", "warung akan ditutup", "beasiswa dibatalkan"), 1,
            "Setelah menatap foto dan berkata \"Ayahmu dulu juga ingin kuliah\", Bu Ratna mengizinkan Dimas.", DRAMA),
        q("nd5", "Latar tempat penggalan drama tersebut adalah ....",
            listOf("warung", "kampus", "ruang tamu", "sekolah", "stasiun"), 2,
            "Teks samping pembuka menyebut \"Ruang tamu sederhana\".", DRAMA),
        q("nd6", "Amanat yang paling sesuai dengan penggalan drama tersebut adalah ....",
            listOf("Anak tidak perlu meminta izin orang tua.", "Kesempatan meraih cita-cita perlu didukung keluarga dengan pengorbanan bersama.",
                "Warung lebih penting daripada kuliah.", "Beasiswa sulit didapat.", "Kuliah harus di kota besar."), 1,
            "Dimas mengatur pengganti, dan ibunya merelakan demi cita-cita yang dulu tak tercapai oleh ayah Dimas.", DRAMA),
        q("nd7", "Kalimat \"Yogyakarta itu jauh, Dimas\" diucapkan Bu Ratna untuk menyatakan ....",
            listOf("informasi tentang letak kota", "keberatan secara halus", "kebanggaan", "ajakan berlibur", "kemarahan"), 1,
            "Setelah tersenyum lalu terdiam, Bu Ratna tidak langsung menolak, tetapi menyampaikan kekhawatirannya.", DRAMA),
        q("nd8", "Lanjutan drama yang paling logis adalah ....",
            listOf("Dimas membatalkan kuliahnya.", "Dimas berpamitan dan berjanji pulang saat libur untuk membantu warung.",
                "Bu Ratna menutup warung.", "Rina menolak membantu.", "Dimas memarahi ibunya."), 1,
            "Bu Ratna sudah mengizinkan, sehingga cerita bergerak ke keberangkatan Dimas.", DRAMA),
    )

    private fun kalimatQ() = listOf(
        q("ne1", "Kalimat efektif terdapat pada ....",
            listOf("Bagi siswa yang terlambat harus melapor ke guru piket.", "Siswa yang terlambat harus melapor ke guru piket.",
                "Para siswa-siswa yang terlambat harus melapor.", "Siswa yang terlambat harus melapor kepada ke guru piket.", "Di dalam siswa yang terlambat harus melapor."), 1,
            "Kalimat B memiliki subjek \"siswa yang terlambat\" dan tidak mengandung kata berlebih."),
        q("ne2", "Kalimat yang mengandung pleonasme adalah ....",
            listOf("Mereka naik ke atas panggung.", "Mereka naik panggung.", "Hadirin dimohon berdiri.", "Kami saling membantu.", "Ia membaca buku."), 0,
            "\"Naik\" sudah bermakna ke atas, jadi \"ke atas\" berlebihan."),
        q("ne3", "Penggunaan tanda koma yang tepat terdapat pada kalimat ....",
            listOf("Ibu membeli, beras, gula, dan minyak.", "Ibu membeli beras, gula, dan minyak.", "Ibu, membeli beras gula dan minyak.",
                "Ibu membeli beras gula, dan, minyak.", "Ibu membeli beras, gula dan, minyak."), 1,
            "Koma memisahkan unsur rincian, termasuk sebelum \"dan\" pada rincian tiga unsur atau lebih."),
        q("ne4", "Penulisan huruf kapital yang tepat terdapat pada kalimat ....",
            listOf("Kami berlibur ke Danau Toba pada hari Raya Idulfitri.", "Kami berlibur ke danau Toba pada Hari Raya Idulfitri.",
                "Kami berlibur ke Danau Toba pada Hari Raya Idulfitri.", "kami berlibur ke Danau Toba pada hari raya idulfitri.", "Kami Berlibur ke Danau Toba pada Hari Raya Idulfitri."), 2,
            "Nama geografi (Danau Toba) dan nama hari raya (Hari Raya Idulfitri) diawali huruf kapital pada setiap unsurnya."),
        q("ne5", "Kalimat berikut yang tidak sejajar bentuknya adalah ....",
            listOf("Kegiatan itu meliputi pendataan, penyuluhan, dan pemeriksaan kesehatan.", "Kegiatan itu meliputi pendataan, penyuluhan, dan memeriksa kesehatan.",
                "Ia membaca, menulis, dan menghitung.", "Mereka menyapu, mengepel, dan merapikan kelas.", "Program itu mencakup pelatihan dan pendampingan."), 1,
            "\"Memeriksa\" berbentuk kata kerja, sedangkan rincian lain kata benda berimbuhan pe-an."),
        q("ne6", "Kalimat yang bermakna ganda (ambigu) adalah ....",
            listOf("Istri dokter yang cantik itu datang.", "Dokter itu datang bersama istrinya.", "Istri dokter itu, yang cantik, datang.",
                "Dokter yang cantik itu datang.", "Istri cantik dokter itu datang."), 0,
            "Tidak jelas siapa yang cantik, istrinya atau dokternya."),
        q("ne7", "Kalimat majemuk bertingkat yang menyatakan hubungan syarat adalah ....",
            listOf("Ia belajar dan adiknya bermain.", "Jika kamu rajin berlatih, kemampuanmu akan meningkat.", "Ia datang, tetapi tidak berbicara.",
                "Ayah membaca koran, ibu memasak.", "Ia lelah sehingga tertidur."), 1,
            "\"Jika\" menandai anak kalimat yang menjadi syarat bagi induk kalimat."),
    )
}
