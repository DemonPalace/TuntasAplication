package id.demonpalace.tuntas

/**
 * The books are cut finer than the daily plan: each plan step becomes several chapters of a few
 * sub-chapters, every chapter with its own quiz. Sub-chapters from [Lessons] are picked by title;
 * new ones are passed as [Section] objects. [unused] lets the unit test catch a title typo.
 */
object Chapters {

    private class Spec(
        val id: String,
        val title: String,
        val kisi: String,
        /** A section title from the plan step, or a new [Section]. */
        val parts: List<Any>,
        /** Question ids kept from the plan step. */
        val keep: List<String>,
        val extra: List<Question> = emptyList(),
    )

    fun split(step: Step): List<Step> {
        val specs = plan[step.id] ?: return listOf(step)
        return specs.map { sp ->
            Step(
                sp.id, step.day, step.date, step.subject, sp.title, sp.kisi,
                sp.parts.mapNotNull { p -> if (p is Section) p else step.sections.firstOrNull { it.title == p } },
                sp.keep.mapNotNull { id -> step.questions.firstOrNull { it.id == id } } + sp.extra,
            )
        }
    }

    /** Section titles and question ids of [step] that no chapter picked up, plus names that match nothing. */
    fun unused(step: Step): List<String> {
        val specs = plan[step.id] ?: return emptyList()
        val titles = specs.flatMap { it.parts }.filterIsInstance<String>()
        val ids = specs.flatMap { it.keep }
        return step.sections.map { it.title }.filter { it !in titles } +
            titles.filter { t -> step.sections.none { it.title == t } }.map { "no section '$it'" } +
            step.questions.map { it.id }.filter { it !in ids } +
            ids.filter { i -> step.questions.none { it.id == i } }.map { "no question '$it'" }
    }

    private val plan: Map<String, List<Spec>> by lazy {
        mapOf(
            "fis1" to listOf(
                Spec("fis1", "Besaran, Pengukuran & Vektor", "Besaran pokok dan turunan, dimensi, alat ukur, angka penting, dan vektor.",
                    listOf(LessonsMore.besaranVektor, "Pengukuran & angka penting"), listOf("k1", "k2"), QFis.besaran),
                Spec("fis1b", "Gerak Lurus", "GLB, GLBB, gerak jatuh bebas, dan gerak vertikal.",
                    listOf("Gerak lurus: GLB & GLBB", "Gerak jatuh bebas & vertikal"), listOf("k3", "k4"), QFis.gerakLurus),
                Spec("fis1c", "Gerak Parabola & Gerak Melingkar", "Gerak parabola, gerak melingkar beraturan, dan hubungan roda-roda.",
                    listOf("Gerak parabola", "Gerak melingkar beraturan"), listOf("k5", "k6"), QFis.parabola),
            ),
            "fis2" to listOf(
                Spec("fis2", "Hukum Newton", "Hukum Newton, jenis gaya, bidang miring, katrol, dan gaya gesek.",
                    listOf("Hukum Newton & jenis gaya", LessonsMore.penerapanNewton), listOf("d1", "d2"), QFis.newton),
                Spec("fis2b", "Usaha, Energi & Momentum", "Usaha, energi kinetik dan potensial, daya, impuls, momentum, dan tumbukan.",
                    listOf("Usaha & energi", "Momentum & impuls"), listOf("d3", "d4", "d5", "d7"), QFis.energi),
                Spec("fis2c", "Rotasi & Kesetimbangan", "Torsi, momen inersia, dinamika rotasi, kesetimbangan benda tegar, dan titik berat.",
                    listOf("Dinamika rotasi", LessonsMore.kesetimbangan), listOf("d6"), QFis.rotasi),
            ),
            "fis3" to listOf(
                Spec("fis3", "Fluida", "Tekanan hidrostatis, hukum Pascal, hukum Archimedes, kontinuitas, dan Bernoulli.",
                    listOf("Tekanan hidrostatis", "Hukum Pascal", "Hukum Archimedes", "Fluida dinamis: kontinuitas & Bernoulli"),
                    listOf("fl1", "fl2", "fl3", "fl4", "fl5"), QFis.fluida),
            ),
            "fis4" to listOf(
                Spec("fis4", "Gelombang & Bunyi", "Besaran dan persamaan gelombang, gelombang stasioner, intensitas, taraf intensitas, dan efek Doppler.",
                    listOf("Besaran gelombang", "Persamaan gelombang & gelombang stasioner", "Bunyi: intensitas & efek Doppler", "Taraf intensitas bunyi"),
                    listOf("w1", "w2", "w3", "w6"), QFis.gelombang),
                Spec("fis4b", "Optik", "Cermin dan lensa, interferensi dan difraksi cahaya, mata, dan lup.",
                    listOf("Cahaya: cermin & lensa", "Interferensi & difraksi cahaya", "Alat optik: mata & lup"),
                    listOf("w4", "w5"), QFis.optik),
            ),
            "fis5" to listOf(
                Spec("fis5", "Suhu, Kalor & Pemuaian", "Kalor, perubahan wujud, asas Black, pemuaian, perpindahan kalor, dan pemanasan global.",
                    listOf("Kalor & asas Black", "Pemuaian zat", "Pemanasan global"), listOf("t1", "t2", "t6"), QFis.kalor),
                Spec("fis5b", "Gas Ideal & Termodinamika", "Hukum gas ideal, teori kinetik gas, hukum I termodinamika, dan mesin Carnot.",
                    listOf("Gas ideal", "Teori kinetik gas", "Termodinamika & mesin kalor"), listOf("t3", "t4", "t5"), QFis.termo),
            ),
            "fis6" to listOf(
                Spec("fis6", "Listrik Statis", "Hukum Coulomb, medan dan potensial listrik, serta kapasitor.",
                    listOf("Listrik statis: hukum Coulomb", "Potensial listrik & kapasitor"), listOf("l1", "l2", "l3", "l4"), QFis.statis),
                Spec("fis6b", "Listrik Arus Searah", "Hukum Ohm, rangkaian seri-paralel, hukum Kirchhoff, GGL, energi, dan daya listrik.",
                    listOf("Rangkaian arus searah", "GGL & hambatan dalam"), listOf("l5", "l6"), QFis.arus),
            ),
            "bin1" to listOf(
                Spec("bin1", "Ide Pokok & Simpulan", "Ide pokok paragraf, simpulan, dan informasi tersirat.",
                    listOf("Menemukan ide pokok", "Simpulan & informasi tersirat"), listOf("bi2", "bi3"), QBin.idePokok),
                Spec("bin1b", "Fakta, Opini & Keakuratan Informasi", "Membedakan fakta dan opini, menilai keakuratan dan kecukupan informasi.",
                    listOf("Fakta dan opini", "Menilai keakuratan & kecukupan informasi"), listOf("bi1", "bi5"), QBin.faktaOpini),
                Spec("bin1c", "Struktur & Jenis Teks Informasi", "Jenis teks informasi, strukturnya, kerangka, dan bagan teks.",
                    listOf(LessonsMore.jenisTeksInformasi, "Kerangka & bagan teks"), emptyList(), QBin.jenisTeks),
                Spec("bin1d", "Teks Jamak, Grafik & Infografik", "Membandingkan dua teks, membaca grafik, tabel, dan infografik.",
                    listOf("Teks jamak: membandingkan dua teks", LessonsMore.grafikInfografik), emptyList(), QBin.teksJamak),
                Spec("bin1e", "Kebahasaan Teks Informasi", "Konjungsi, kata serapan, dan istilah dalam teks informasi.",
                    listOf("Konjungsi: pertentangan & sebab-akibat", "Kata serapan & istilah"), listOf("bi4"), QBin.kebahasaan),
            ),
            "bin2" to listOf(
                Spec("bin2", "Unsur Cerita Fiksi", "Unsur intrinsik, prediksi lanjutan cerita, dan nilai dalam cerita.",
                    listOf("Unsur intrinsik cerita", "Memprediksi lanjutan & menilai nilai cerita"), listOf("f2", "f5"), QBin.cerita),
                Spec("bin2b", "Majas & Kosakata", "Jenis majas, makna kata dan ungkapan dalam konteks.",
                    listOf("Majas (gaya bahasa)", "Kosakata & kalimat"), listOf("f1", "f3"), QBin.majas),
                Spec("bin2c", "Puisi", "Unsur puisi, makna, suasana, dan amanat.",
                    listOf("Puisi: unsur, makna & suasana"), emptyList(), QBin.puisi),
                Spec("bin2d", "Drama", "Dialog, konflik, watak tokoh, dan petunjuk lakuan.",
                    listOf("Drama: dialog, konflik & watak"), emptyList(), QBin.drama),
                Spec("bin2e", "Kalimat Efektif & Tanda Baca", "Kalimat kompleks, konjungsi, tanda baca, dan kalimat efektif.",
                    listOf("Kalimat kompleks, konjungsi & tanda baca", LessonsMore.kalimatEfektif), listOf("f4"), QBin.kalimat),
            ),
            "big1" to listOf(
                Spec("big1", "Reading Strategies & Vocabulary", "Skimming, scanning, paraphrase, and the meaning of words in context.",
                    listOf(LessonsBig.strategies.title, LessonsBig.vocabulary.title), listOf("g6"), QBig.strategies),
                Spec("big1b", "Main Idea & Reference Words", "Topic sentences, supporting details, and what pronouns refer to.",
                    listOf(LessonsBig.mainIdea.title, LessonsBig.reference.title), listOf("g4"), QBig.mainIdea),
                Spec("big1c", "Descriptive Text", "Describing one particular person, place, or thing.",
                    listOf(LessonsBig.descriptive.title), listOf("g3"), QBig.descriptive),
                Spec("big1d", "Recount Text", "Retelling past events in time order.",
                    listOf(LessonsBig.recount.title), listOf("g1", "g2"), QBig.recount),
                Spec("big1e", "Narrative Text & Moral Value", "Stories with a complication, purpose of texts, target readers, and moral values.",
                    listOf(LessonsBig.narrative.title, LessonsBig.purpose.title), listOf("g5"), QBig.narrative),
            ),
            "big2" to listOf(
                Spec("big2", "Procedure Text", "Goal, materials, and steps; imperative sentences.",
                    listOf(LessonsBig.procedure.title), listOf("e4"), QBig.procedure),
                Spec("big2b", "Analytical Exposition & Connectors", "Thesis, arguments, reiteration, and connectors of contrast, result, and addition.",
                    listOf(LessonsBig.exposition.title, LessonsBig.connectors.title), listOf("e1", "e2", "e3"), QBig.exposition),
                Spec("big2c", "Functional Texts, Fact & Opinion", "Notices, announcements, advertisements, and telling facts from opinions.",
                    listOf(LessonsBig.functional.title, LessonsBig.factOpinion.title), listOf("e5"), QBig.functional),
                Spec("big2d", "Sequence, Summary & Inference", "Order of events, summaries, implied information, and predicting outcomes.",
                    listOf(LessonsBig.sequence.title, LessonsBig.inference.title), emptyList(), QBig.sequence),
                Spec("big2e", "Cause & Effect, Comparing Texts", "Cause-effect chains and comparing two texts on one topic.",
                    listOf(LessonsBig.causeEffect.title, LessonsBig.comparing.title), emptyList(), QBig.causeEffect),
            ),
            "mat1" to listOf(
                Spec("mat1", "Bilangan Real", "Jenis bilangan, operasi pecahan, persen, dan perbandingan.",
                    listOf("Jenis & sifat bilangan", "Persen", "Perbandingan senilai & berbalik nilai"),
                    listOf("m1", "m3"), QMat.bilangan),
                Spec("mat1b", "Eksponen, Akar & Logaritma", "Sifat pangkat, bentuk akar, merasionalkan penyebut, dan sifat logaritma.",
                    listOf("Pangkat & logaritma", "Bentuk akar"), listOf("m4"), QMat.eksponen),
                Spec("mat1c", "Persamaan & Sistem Persamaan Linear", "Persamaan linear, SPLDV, SPLTV, dan model soal cerita.",
                    listOf("Persamaan, SPLDV & fungsi", "Sistem persamaan linear tiga variabel"), listOf("m2", "m5", "m6"), QMat.linear),
                Spec("mat1d", "Pertidaksamaan & Program Linear", "Pertidaksamaan linear, kuadrat, rasional, nilai mutlak, dan program linear.",
                    listOf("Pertidaksamaan linear & program linear", LessonsMore.pertidaksamaanKuadrat), emptyList(), QMat.pertidaksamaan),
            ),
            "mat4" to listOf(
                Spec("mat4", "Fungsi & Fungsi Kuadrat", "Domain dan range, komposisi, invers, persamaan kuadrat, dan grafik fungsi kuadrat.",
                    listOf("Fungsi: domain, kodomain & range", "Fungsi komposisi & fungsi invers", LessonsMore.persamaanKuadrat, LessonsMore.fungsiKuadrat),
                    listOf("m4a", "m4b", "m4e", "m4f"), QMat.fungsi),
                Spec("mat4b", "Barisan & Deret", "Barisan dan deret aritmetika serta geometri, deret tak hingga, bunga, pertumbuhan, dan peluruhan.",
                    listOf("Barisan & deret aritmetika", "Barisan & deret geometri, pertumbuhan & bunga majemuk"),
                    listOf("m4c", "m4d", "m4g"), QMat.deret),
            ),
            "mat2" to listOf(
                Spec("mat2", "Geometri Bidang", "Hubungan sudut, Pythagoras, keliling dan luas bangun datar, sudut dan garis singgung lingkaran.",
                    listOf("Hubungan sudut, garis & bidang", "Teorema Pythagoras", "Keliling & luas bangun datar", LessonsMore.lingkaran),
                    listOf("g7", "g12"), QMat.bidang),
                Spec("mat2b", "Bangun Ruang", "Volume, luas permukaan, dan jarak pada bangun ruang.",
                    listOf("Lingkaran & bangun ruang", "Luas permukaan bangun ruang", "Jarak titik, garis & bidang"),
                    listOf("g11"), QMat.ruang),
                Spec("mat2c", "Kesebangunan & Transformasi", "Kesebangunan, refleksi, translasi, rotasi, dilatasi, dan komposisinya.",
                    listOf("Kesebangunan & transformasi", "Rotasi, dilatasi & komposisi transformasi"),
                    listOf("g9", "g10"), QMat.transformasi),
                Spec("mat2d", "Trigonometri", "Perbandingan trigonometri, sudut berelasi, aturan sinus dan cosinus, luas segitiga.",
                    listOf("Trigonometri dasar", "Enam perbandingan trigonometri & sudut berelasi", LessonsMore.aturanSinusCosinus),
                    listOf("g8"), QMat.trigonometri),
            ),
            "mat3" to listOf(
                Spec("mat3", "Statistika", "Membaca diagram, ukuran pemusatan, letak, dan penyebaran data tunggal maupun kelompok.",
                    listOf("Membaca diagram & tabel", "Statistika deskriptif", "Ukuran pemusatan data kelompok", "Ukuran letak & penyebaran data"),
                    listOf("p2", "p5"), QMat.statistika),
                Spec("mat3b", "Kaidah Pencacahan & Peluang", "Aturan perkalian, permutasi, kombinasi, peluang, dan peluang kejadian majemuk.",
                    listOf("Kaidah pencacahan", "Peluang kejadian", "Peluang kejadian majemuk"),
                    listOf("p1", "p3", "p4", "p6"), QMat.peluang),
            ),
        )
    }
}
