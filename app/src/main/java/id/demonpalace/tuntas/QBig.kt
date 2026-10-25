package id.demonpalace.tuntas

/**
 * Chapter quizzes for the B. Inggris book. Passages are written for this app in the format of
 * the SMA reading models; questions are in English, explanations in Indonesian.
 * The people in the stories and the school events are fictional.
 */
object QBig {
    private fun q(id: String, text: String, options: List<String>, answer: Int, explain: String, passage: String? = null) =
        Question(id, text, options, answer, explain, passage)

    private fun multi(id: String, text: String, options: List<String>, keys: Set<Int>, explain: String, passage: String? = null) =
        Question(id, text, options, keys.min(), explain, passage, multi = keys)

    private fun table(id: String, text: String, cols: List<String>, rows: List<String>, keys: List<Int>, explain: String, passage: String? = null) =
        Question(id, text, cols, keys.first(), explain, passage, rows = rows, rowKeys = keys)

    val strategies: List<Question> by lazy { strategiesQ() }
    val mainIdea: List<Question> by lazy { mainIdeaQ() }
    val descriptive: List<Question> by lazy { descriptiveQ() }
    val recount: List<Question> by lazy { recountQ() }
    val narrative: List<Question> by lazy { narrativeQ() }
    val procedure: List<Question> by lazy { procedureQ() }
    val exposition: List<Question> by lazy { expositionQ() }
    val functional: List<Question> by lazy { functionalQ() }
    val sequence: List<Question> by lazy { sequenceQ() }
    val causeEffect: List<Question> by lazy { causeEffectQ() }

    private const val RAFFLESIA =
        "Rafflesia arnoldii produces the largest single flower in the world. A fully open flower can measure about one metre across and weigh up to 11 kilograms. Unlike most plants, it has no leaves, stems, or roots of its own. It is a parasite that lives inside a forest vine and takes water and food from it. The flower appears only when it blooms, and it lasts for about five to seven days before it turns black and rots. During that time, it gives off a foul smell like rotting meat, which attracts the flies that pollinate it. Because the plant is rare and its rainforest habitat in Sumatra and Bengkulu is shrinking, it is protected by Indonesian law."

    private const val SLEEP =
        "(1) Teenagers need eight to ten hours of sleep every night, according to the American Academy of Sleep Medicine. However, many high school students sleep fewer than seven hours on school nights. They stay up late doing homework, chatting online, or watching videos, and they still have to wake up early for school.\n\n" +
            "(2) Lack of sleep affects learning in several ways. A tired student finds it harder to concentrate in class and to remember new information. It also affects mood: students who sleep less are more easily irritated. Doctors therefore advise teenagers to put their phones away at least 30 minutes before bed and to keep the same bedtime every night, including weekends."

    private const val TOBA =
        "Lake Toba in North Sumatra is the largest volcanic lake in the world. It is about 100 kilometres long and 30 kilometres wide, and its deepest point reaches around 500 metres. The lake was formed after a huge volcanic eruption about 74,000 years ago. In the middle of the lake lies Samosir Island, which is almost as large as Singapore. The water is cool and clear, and the hills around it are covered with pine trees. The Batak people who live around the lake are known for their traditional houses with high, curved roofs."

    private const val BOSSCHA =
        "Last July, our class visited the Bosscha Observatory in Lembang, West Java. We left school at 7 a.m. by bus and reached Lembang at 10 a.m. A guide first took us to the main dome, where the large Zeiss telescope has stood since 1928. After that, we watched a short film about the solar system in the visitor hall. At noon, we had lunch at a small restaurant near the gate. Unfortunately, the sky was cloudy, so we could not look at the sun through the special solar telescope. We got back to school at 4 p.m., tired but proud of the notes we had taken for our physics project."

    private const val KANCIL =
        "Once upon a time, a mouse deer named Kancil wanted to cross a wide river to eat the fruit on the other side. The river was full of hungry crocodiles. Kancil thought for a while and then called out, \"Crocodiles! The King has ordered me to count all of you. He wants to give each of you a piece of meat.\" The greedy crocodiles quickly lined up from one bank to the other. Kancil jumped onto their backs one by one and counted loudly, \"One, two, three ...\" When he reached the other bank, he laughed and said, \"There is no meat. Thank you for the bridge!\" The crocodiles were furious, but Kancil was already safe among the fruit trees."

    private const val FILTER =
        "How to Make a Simple Water Filter\n\n" +
            "Materials: a 1.5-litre plastic bottle, a pair of scissors, clean cotton, 10 tablespoons of activated charcoal, 2 cups of fine sand, 2 cups of gravel, and dirty water.\n\n" +
            "Steps:\n1. First, cut off the bottom of the bottle with the scissors.\n2. Turn the bottle upside down and push the cotton into the neck.\n3. Then, put the activated charcoal on top of the cotton.\n4. Next, add the fine sand, followed by the gravel.\n5. After that, place the bottle over a glass and slowly pour the dirty water into it.\n6. Finally, collect the filtered water in the glass.\n\n" +
            "Note: The filter makes the water clearer, but it does not kill germs. Boil the water before you drink it."

    private const val PLASTIC =
        "School canteens should stop using single-use plastic. First, plastic cups, straws, and bags are used for only a few minutes but take hundreds of years to break down. A school of 900 students that gives one plastic cup to each student every day throws away about 18,000 cups a month. Second, most of this plastic is not recycled. It ends up in landfills, rivers, and the sea, where animals swallow it. Moreover, the change is not expensive. Students can bring their own bottles, and canteens can serve food on washable plates. Therefore, every school canteen should replace single-use plastic with reusable items."

    private const val ANNOUNCEMENT =
        "ANNOUNCEMENT\n\nThe English Club of SMA Harapan will hold a speech contest on Saturday, 14 November, at 8 a.m. in the school hall. The topic is \"Young People and the Environment\". Each speech must be 5 to 7 minutes long. Students of grades 10 to 12 who want to join must register with Ms. Rina in the language lab before 7 November. The registration fee is Rp20.000. Winners will represent the school at the city-level contest in December."

    private const val ADVERT =
        "Tired of noisy study rooms? Try FOKUS Study Space!\n\nQuiet desks, fast Wi-Fi, and free drinking water. Open every day from 8 a.m. to 10 p.m. Only Rp15.000 per three hours. Show your student card and get a 20% discount on weekdays. Jl. Melati No. 12, next to the city library."

    private const val BUS =
        "Dinda looked at her watch again. It was 9:40 p.m., and the last bus to her village left the terminal at 9:45. She had stayed late at the library to finish her history assignment, and now she was running through the rain. When she reached the terminal, the bus was already moving. Dinda waved her arms and shouted, but the driver did not see her. She stood still for a moment, breathing hard. Then she took out her phone, which had only 5% battery left, and called her older brother."

    private const val SUBSIDENCE =
        "Parts of North Jakarta are sinking. In some areas the ground drops by up to 10 centimetres a year. The main cause is the pumping of groundwater. Many homes, hotels, and factories do not get enough piped water, so they dig wells and pump water from deep under the ground. As the water is removed, the layers of soil above it are pressed together and the land goes down. As a result, sea water flows into low areas more easily, and floods during high tide have become more frequent. To slow the sinking, the city government plans to extend the piped water network and limit the use of deep wells."

    private const val PAIRED =
        "Text 1\nPrinted books are still the best choice for students. Reading on paper causes less eye strain than reading on a screen, and a printed book has no notifications to distract the reader. Several studies have found that students remember details better when they read long texts on paper.\n\n" +
            "Text 2\nE-books make reading easier for many students. One tablet can hold hundreds of books, so students do not have to carry a heavy bag. E-books are often cheaper than printed books, and readers can change the font size. However, long hours in front of a screen can make the eyes tired."

    // ============================== READING STRATEGIES & VOCABULARY ==============================

    private fun strategiesQ() = listOf(
        q("bs1", "How long does the flower of Rafflesia arnoldii last?",
            listOf("One day", "About two days", "About five to seven days", "About one month", "Until the vine dies"), 2,
            "Scanning kata \"lasts\": \"it lasts for about five to seven days\".", RAFFLESIA),
        q("bs2", "The word \"foul\" in the text is closest in meaning to ....",
            listOf("sweet", "unpleasant", "strong but fresh", "weak", "familiar"), 1,
            "Bau itu \"like rotting meat\". Bau daging busuk berarti tidak sedap (unpleasant).", RAFFLESIA),
        q("bs3", "Which statement is TRUE according to the text?",
            listOf("Rafflesia has short roots.", "Rafflesia makes its own food with its leaves.", "Rafflesia gets water and food from a vine.", "Rafflesia is pollinated by bees.", "Rafflesia grows in every part of Indonesia."), 2,
            "\"It is a parasite that lives inside a forest vine and takes water and food from it.\" Pilihan C adalah parafrasenya. Teks menyebut tumbuhan ini tidak punya akar dan daun, penyerbuknya lalat.", RAFFLESIA),
        q("bs4", "Why is Rafflesia arnoldii protected by law?",
            listOf("Because it is a parasite", "Because it smells bad", "Because it is rare and its habitat is shrinking", "Because it is the national flower of Singapore", "Because it attracts flies"), 2,
            "Kalimat terakhir: \"Because the plant is rare and its rainforest habitat ... is shrinking, it is protected\".", RAFFLESIA),
        q("bs5", "\"The new bridge is sturdy; even heavy trucks can cross it safely.\" The word \"sturdy\" is OPPOSITE in meaning to ....",
            listOf("strong", "solid", "weak", "wide", "modern"), 2,
            "Truk berat aman melintas berarti sturdy = kuat. Soal meminta lawan kata: weak. Strong dan solid adalah sinonim."),
        q("bs6", "\"Unlike his talkative sister, Bimo is reserved; he rarely speaks in front of strangers.\" The word \"reserved\" means ....",
            listOf("booked in advance", "quiet and shy", "angry", "very friendly", "talkative"), 1,
            "\"Unlike his talkative sister\" adalah petunjuk pertentangan, diperkuat \"rarely speaks\". Arti \"booked in advance\" benar di kamus, tetapi tidak cocok dengan konteks."),
        multi("bs7", "Choose ALL statements that are mentioned in the text.",
            listOf("The flower can weigh up to 11 kilograms.", "The flower turns black before it rots.", "The flower can be found in Sumatra and Bengkulu.", "The flower is used as traditional medicine.", "The flower blooms every month."),
            setOf(0, 1, 2),
            "A, B, dan C ada di teks. Teks tidak menyebut obat tradisional maupun mekar setiap bulan.", RAFFLESIA),
    )

    // ============================== MAIN IDEA & REFERENCE ==============================

    private fun mainIdeaQ() = listOf(
        q("bm1", "What is the main idea of paragraph 1?",
            listOf("Teenagers like watching videos.", "Many teenagers do not get the amount of sleep they need.", "Homework should be stopped.", "Schools start too early in the morning.", "Sleep doctors work in America."), 1,
            "Paragraf 1 membandingkan kebutuhan (8 sampai 10 jam) dengan kenyataan (kurang dari 7 jam). Pilihan A dan D hanya detail.", SLEEP),
        q("bm2", "Paragraph 2 mainly discusses ....",
            listOf("how to do homework faster", "the effects of lack of sleep and advice from doctors", "the history of sleep research", "why teenagers like their phones", "the best time to start school"), 1,
            "Paragraf 2 dibuka dengan \"Lack of sleep affects learning\" lalu ditutup saran dokter.", SLEEP),
        q("bm3", "The word \"They\" in paragraph 1 refers to ....",
            listOf("teenagers' parents", "doctors", "many high school students", "eight to ten hours", "school nights"), 2,
            "\"They stay up late doing homework\": subjek jamak yang begadang adalah \"many high school students\" di kalimat sebelumnya.", SLEEP),
        q("bm4", "The word \"It\" in \"It also affects mood\" refers to ....",
            listOf("class", "new information", "lack of sleep", "a tired student", "mood"), 2,
            "Paragraf 2 membahas akibat \"lack of sleep\"; \"It also affects\" melanjutkan daftar akibat itu. \"A tired student\" adalah orang, jadi tidak cocok dengan \"affects mood\".", SLEEP),
        q("bm5", "Which of the following is NOT mentioned as a reason teenagers stay up late?",
            listOf("Doing homework", "Chatting online", "Watching videos", "Playing football", "Doing homework and chatting online"), 3,
            "Teks menyebut homework, chatting online, dan watching videos. Football tidak disebut.", SLEEP),
        q("bm6", "What does the writer suggest in the last sentence?",
            listOf("Teenagers should sleep only on weekends.", "Phones should be put away at least 30 minutes before bed.", "Students should do homework in the morning.", "Doctors should visit schools.", "Teenagers should sleep less on weekends."), 1,
            "\"put their phones away at least 30 minutes before bed and keep the same bedtime every night, including weekends\".", SLEEP),
    )

    // ============================== DESCRIPTIVE ==============================

    private fun descriptiveQ() = listOf(
        q("bd1", "What does the text describe?",
            listOf("Volcanoes in Sumatra", "Lake Toba", "Samosir Island only", "The Batak people", "The eruption 74,000 years ago"), 1,
            "Kalimat identifikasi: \"Lake Toba ... is the largest volcanic lake in the world.\" Bagian lain adalah deskripsinya.", TOBA),
        q("bd2", "How deep is the deepest point of Lake Toba?",
            listOf("About 30 metres", "About 74 metres", "About 100 metres", "About 500 metres", "About 1,000 metres"), 3,
            "\"its deepest point reaches around 500 metres\". 100 dan 30 adalah panjang dan lebar dalam kilometer.", TOBA),
        q("bd3", "\"...which is almost as large as Singapore.\" The word \"which\" refers to ....",
            listOf("Lake Toba", "North Sumatra", "Samosir Island", "the eruption", "the middle of the lake"), 2,
            "Yang dibandingkan dengan luas Singapura adalah pulau yang baru disebut: Samosir Island.", TOBA),
        q("bd4", "The text is organized as ....",
            listOf("orientation, events, reorientation", "identification, description", "goal, materials, steps", "thesis, arguments, reiteration", "orientation, complication, resolution"), 1,
            "Teks deskriptif: identifikasi di kalimat pertama, lalu ciri-ciri danau.", TOBA),
        q("bd5", "Which tense is mostly used in the text, and why?",
            listOf("Simple past, because it tells a past trip", "Simple present, because it describes how the lake is", "Future tense, because it gives plans", "Past perfect, because it tells history", "Present continuous, because it reports news"), 1,
            "Is, lies, are covered: simple present untuk keadaan yang berlaku sekarang. Satu kalimat past (\"was formed\") hanya menyebut asal-usul.", TOBA),
        table("bd6", "Decide whether each statement is true or false according to the text.", listOf("True", "False"),
            listOf("Lake Toba was formed by an eruption.", "Samosir Island is larger than Singapore.", "Pine trees grow on the hills around the lake.", "The Batak houses have flat roofs."),
            listOf(0, 1, 0, 1),
            "Samosir \"almost as large as\" Singapura, jadi lebih kecil. Atap rumah Batak \"high, curved\", bukan datar.", TOBA),
    )

    // ============================== RECOUNT ==============================

    private fun recountQ() = listOf(
        q("br1", "What is the purpose of the text?",
            listOf("To describe the Bosscha Observatory", "To retell the writer's class trip", "To persuade readers to visit Lembang", "To explain how a telescope works", "To entertain readers with a fable"), 1,
            "Past tense, urutan kejadian, dan pengalaman nyata penulis: recount, tujuannya menceritakan kembali.", BOSSCHA),
        q("br2", "How long did the bus trip from school to Lembang take?",
            listOf("One hour", "Two hours", "Three hours", "Four hours", "Nine hours"), 2,
            "Berangkat 7 a.m., tiba 10 a.m.: 3 jam. 4 p.m. adalah waktu tiba kembali di sekolah.", BOSSCHA),
        q("br3", "What did the students do right after visiting the main dome?",
            listOf("They had lunch.", "They looked at the sun.", "They watched a film about the solar system.", "They went back to school.", "They took notes on the bus."), 2,
            "\"After that, we watched a short film\" langsung setelah kubah utama.", BOSSCHA),
        q("br4", "Why could the students not use the solar telescope?",
            listOf("It was broken.", "It was closed for visitors.", "The sky was cloudy.", "They arrived too late.", "The guide did not allow it."), 2,
            "\"the sky was cloudy, so we could not look at the sun\". So menandai akibat; sebabnya langit berawan.", BOSSCHA),
        q("br5", "The last sentence of the text is called the ....",
            listOf("orientation", "event", "complication", "reorientation", "thesis"), 3,
            "Kalimat penutup berisi perasaan penulis (tired but proud): reorientasi.", BOSSCHA),
        q("br6", "\"...the large Zeiss telescope has stood since 1928.\" What can be inferred?",
            listOf("The telescope was built last July.", "The telescope has been at the observatory for nearly a century.", "The telescope can only be used on cloudy days.", "The telescope was moved from Jakarta.", "The students bought the telescope."), 1,
            "Teleskop berdiri sejak 1928, hampir seratus tahun sampai sekarang. Pilihan lain tidak didukung teks.", BOSSCHA),
    )

    // ============================== NARRATIVE & MORAL VALUE ==============================

    private fun narrativeQ() = listOf(
        q("bn1", "What was Kancil's problem?",
            listOf("He was hungry and had no friends.", "He wanted to cross a river full of crocodiles.", "The King wanted to punish him.", "He could not count.", "The fruit trees were too high."), 1,
            "Komplikasi: ingin menyeberang sungai yang penuh buaya lapar.", KANCIL),
        q("bn2", "Why did the crocodiles line up across the river?",
            listOf("They wanted to help Kancil.", "They were ordered by their leader.", "They believed they would get meat.", "They were playing a game.", "They wanted to cross the river too."), 2,
            "Kancil berbohong bahwa Raja akan memberi daging; \"the greedy crocodiles quickly lined up\".", KANCIL),
        q("bn3", "What kind of character is Kancil?",
            listOf("Honest and kind", "Clever but tricky", "Lazy and greedy", "Shy and quiet", "Brave and generous"), 1,
            "Ia berpikir lalu menipu buaya agar bisa menyeberang: cerdik tetapi licik.", KANCIL),
        q("bn4", "Which part of the story is the resolution?",
            listOf("Kancil wanted to eat the fruit.", "The river was full of crocodiles.", "Kancil told the crocodiles about the King.", "Kancil reached the other bank safely.", "The crocodiles lined up."), 3,
            "Masalah selesai saat Kancil tiba dengan selamat di seberang.", KANCIL),
        q("bn5", "What lesson can we learn from the crocodiles?",
            listOf("Always obey the King.", "Greed can make us easily fooled.", "Rivers are dangerous.", "Counting is important.", "Never eat meat."), 1,
            "Buaya tertipu karena serakah ingin daging. Pesan moral diambil dari akibat sifat itu.", KANCIL),
        q("bn6", "The text is most likely written for ....",
            listOf("farmers who grow fruit", "zoo keepers", "children who like animal stories", "scientists who study crocodiles", "government officials"), 2,
            "Fabel dengan tokoh hewan yang bisa berbicara ditujukan untuk anak-anak dan pembaca cerita.", KANCIL),
        table("bn7", "Decide whether each event is reality or fantasy.", listOf("Reality", "Fantasy"),
            listOf("Crocodiles live in rivers.", "A mouse deer talks to crocodiles.", "Crocodiles believe a message from a king.", "A mouse deer eats fruit."),
            listOf(0, 1, 1, 0),
            "Hewan berbicara dan memahami perintah raja tidak terjadi di dunia nyata.", KANCIL),
    )

    // ============================== PROCEDURE ==============================

    private fun procedureQ() = listOf(
        q("bp1", "What is the purpose of the text?",
            listOf("To describe a plastic bottle", "To tell how to make a simple water filter", "To persuade readers to drink boiled water", "To report an experiment result", "To explain why water is dirty"), 1,
            "Judul \"How to Make ...\", daftar bahan, dan langkah bernomor: teks prosedur.", FILTER),
        q("bp2", "What should be put in the bottle right after the activated charcoal?",
            listOf("Cotton", "Gravel", "Fine sand", "Dirty water", "Clean water"), 2,
            "Langkah 4: \"Next, add the fine sand, followed by the gravel.\" Pasir dulu, baru kerikil.", FILTER),
        q("bp3", "Which material is placed at the very bottom of the filter, in the neck of the bottle?",
            listOf("Gravel", "Fine sand", "Activated charcoal", "Cotton", "Scissors"), 3,
            "Botol dibalik dan kapas didorong ke leher botol sebelum bahan lain.", FILTER),
        q("bp4", "Based on the note, what can we conclude about the filtered water?",
            listOf("It is ready to drink.", "It is still dirty.", "It may still contain germs.", "It is salty.", "It must be filtered again with cotton."), 2,
            "\"it does not kill germs. Boil the water before you drink it.\"", FILTER),
        q("bp5", "\"Turn the bottle upside down ....\" The sentence is an example of ....",
            listOf("a question", "an imperative sentence", "a past tense sentence", "an opinion", "a conditional sentence"), 1,
            "Diawali kata kerja dasar tanpa subjek: kalimat perintah, ciri teks prosedur.", FILTER),
        q("bp6", "How many kinds of filter layers (not including the bottle and scissors) are used?",
            listOf("Two", "Three", "Four", "Five", "Six"), 2,
            "Kapas, arang aktif, pasir halus, kerikil: 4. Air kotor bukan lapisan filter.", FILTER),
    )

    // ============================== EXPOSITION & CONNECTORS ==============================

    private fun expositionQ() = listOf(
        q("bx1", "What is the writer's main purpose?",
            listOf("To describe a school canteen", "To persuade schools to stop using single-use plastic", "To explain how plastic is made", "To retell a visit to a landfill", "To entertain students"), 1,
            "Tesis \"should stop using single-use plastic\", argumen, lalu penegasan ulang: eksposisi analitis.", PLASTIC),
        q("bx2", "According to the text, a school of 900 students throws away about ... plastic cups a month.",
            listOf("900", "9,000", "18,000", "27,000", "90,000"), 2,
            "Angka langsung di teks: about 18,000 cups a month (900 × 20 hari sekolah).", PLASTIC),
        q("bx3", "\"Moreover, the change is not expensive.\" The word \"Moreover\" is used to ....",
            listOf("show contrast", "add another argument", "show a result", "give an example", "show time"), 1,
            "Moreover menandai tambahan (addition): argumen ketiga setelah First dan Second.", PLASTIC),
        q("bx4", "The last sentence of the text functions as the ....",
            listOf("thesis", "first argument", "reiteration", "resolution", "orientation"), 2,
            "\"Therefore, every school canteen should replace ...\" mengulang tesis di akhir.", PLASTIC),
        q("bx5", "\"____ the canteen raised its prices, many students still bought lunch there.\" The best word to fill the gap is ....",
            listOf("Because", "Despite", "Although", "Therefore", "So"), 2,
            "Harga naik tetapi siswa tetap membeli: pertentangan. Setelah celah ada kalimat lengkap (subjek + kerja), jadi Although, bukan Despite."),
        q("bx6", "\"The road was flooded. ____, the school was closed for two days.\" The best connector is ....",
            listOf("However", "As a result", "Although", "In contrast", "For example"), 1,
            "Banjir menyebabkan sekolah tutup: hubungan sebab-akibat, jadi As a result."),
        table("bx7", "Classify each sentence from the text.", listOf("Thesis", "Argument", "Reiteration"),
            listOf("School canteens should stop using single-use plastic.", "Most of this plastic is not recycled.", "Therefore, every school canteen should replace single-use plastic with reusable items."),
            listOf(0, 1, 2),
            "Kalimat pertama adalah posisi penulis, kalimat tengah alasan, kalimat dengan Therefore penegasan ulang.", PLASTIC),
    )

    // ============================== FUNCTIONAL TEXTS, FACT & OPINION ==============================

    private fun functionalQ() = listOf(
        q("bf1", "What is the announcement mainly about?",
            listOf("A city-level contest in December", "An English speech contest at SMA Harapan", "A new language lab", "Ms. Rina's English class", "An environmental cleaning day"), 1,
            "Inti pengumuman: lomba pidato English Club. Lomba tingkat kota hanya detail tambahan.", ANNOUNCEMENT),
        q("bf2", "Who can join the contest?",
            listOf("All teachers", "Grade 10 students only", "Students of grades 10 to 12", "English Club members only", "Students from other schools"), 2,
            "\"Students of grades 10 to 12 who want to join must register\".", ANNOUNCEMENT),
        q("bf3", "A student finishes registering on 10 November. What will most likely happen?",
            listOf("The student can join the contest.", "The student must pay Rp40.000.", "The student cannot join because registration closed on 7 November.", "The student will go to the city contest.", "The student must give a 10-minute speech."), 2,
            "Batas pendaftaran sebelum 7 November; 10 November sudah lewat.", ANNOUNCEMENT),
        q("bf4", "What is the purpose of the second text?",
            listOf("To describe the city library", "To persuade students to use FOKUS Study Space", "To announce a competition", "To explain how to study well", "To report news about Wi-Fi"), 1,
            "Kalimat ajakan \"Try FOKUS Study Space!\", harga, dan diskon: iklan, tujuannya membujuk.", ADVERT),
        q("bf5", "A student uses FOKUS Study Space for three hours on a Tuesday and shows a student card. How much does the student pay?",
            listOf("Rp3.000", "Rp12.000", "Rp15.000", "Rp18.000", "Rp20.000"), 1,
            "Harga Rp15.000, diskon 20% hari kerja: Rp15.000 − Rp3.000 = Rp12.000. Selasa termasuk weekday.", ADVERT),
        q("bf6", "Which sentence from the advertisement is an opinion?",
            listOf("Open every day from 8 a.m. to 10 p.m.", "Only Rp15.000 per three hours.", "Jl. Melati No. 12, next to the city library.", "Tired of noisy study rooms?", "Show your student card and get a 20% discount on weekdays."), 3,
            "Pilihan A, B, C, dan E bisa diperiksa. Pilihan D menyatakan penilaian bahwa ruang belajar lain bising; tidak ada data yang membuktikannya.", ADVERT),
        table("bf7", "Decide whether each sentence is a fact or an opinion.", listOf("Fact", "Opinion"),
            listOf("The contest starts at 8 a.m.", "The registration fee is Rp20.000.", "The environment is the most interesting topic for young people.", "Winners will represent the school in December."),
            listOf(0, 0, 1, 0),
            "\"the most interesting\" adalah penilaian. Kalimat lain bisa dicek di pengumuman.", ANNOUNCEMENT),
    )

    // ============================== SEQUENCE, SUMMARY & INFERENCE ==============================

    private fun sequenceQ() = listOf(
        q("bq1", "Why was Dinda late for the bus?",
            listOf("She forgot the schedule.", "She stayed late at the library to finish an assignment.", "Her watch was broken.", "She was waiting for her brother.", "The bus left early."), 1,
            "\"She had stayed late at the library to finish her history assignment\".", BUS),
        q("bq2", "Put the events in the correct order: (1) Dinda called her brother. (2) Dinda ran through the rain. (3) Dinda waved and shouted. (4) Dinda finished her assignment at the library.",
            listOf("4-2-3-1", "2-4-3-1", "4-3-2-1", "3-2-4-1", "4-2-1-3"), 0,
            "Past perfect \"had stayed\" menunjukkan tugas selesai lebih dulu walau disebut belakangan. Urutannya: perpustakaan, berlari, melambai, menelepon.", BUS),
        q("bq3", "What can be inferred about Dinda's situation at the end of the story?",
            listOf("She was going to take a taxi.", "She needed someone to pick her up.", "She was happy to miss the bus.", "She decided to sleep at the terminal.", "She forgot her assignment."), 1,
            "Bus terakhir sudah pergi dan ia menelepon kakaknya: kemungkinan besar minta dijemput. Taksi tidak disebut di teks.", BUS),
        q("bq4", "\"...which had only 5% battery left\" suggests that ....",
            listOf("Dinda's phone was new.", "Dinda might not be able to use her phone for long.", "Dinda's brother had no phone.", "The terminal had no electricity.", "Dinda did not want to call anyone."), 1,
            "Baterai 5% berarti ponsel bisa segera mati; ini menambah ketegangan cerita.", BUS),
        q("bq5", "Which is the best summary of the story?",
            listOf("Dinda finished her history assignment at the library.", "Dinda missed the last bus home after studying late and called her brother for help.", "The bus driver did not see Dinda.", "It rained heavily at night.", "Dinda's phone battery was low."), 1,
            "Ringkasan memuat pokok seluruh cerita. Pilihan lain hanya satu detail.", BUS),
        q("bq6", "What will Dinda's brother most likely do next?",
            listOf("Tell her to wait for tomorrow's bus", "Drive to the terminal to pick her up", "Finish her assignment", "Call the bus driver", "Buy her a new phone"), 1,
            "Prediksi berdasarkan alasan ia menelepon: tidak ada bus lagi malam itu, jadi kakaknya kemungkinan menjemput.", BUS),
    )

    // ============================== CAUSE-EFFECT & COMPARING TEXTS ==============================

    private fun causeEffectQ() = listOf(
        q("bc1", "What is the main cause of the sinking land in North Jakarta?",
            listOf("High tide", "Heavy rain", "The pumping of groundwater", "Too many hotels", "Sea water"), 2,
            "\"The main cause is the pumping of groundwater.\" Banjir rob adalah akibat, bukan sebab.", SUBSIDENCE),
        q("bc2", "Why do many homes and factories dig wells?",
            listOf("Well water is cleaner.", "They do not get enough piped water.", "The government asks them to.", "Wells are cheaper than bottled water.", "The sea water is too salty."), 1,
            "\"Many homes ... do not get enough piped water, so they dig wells\". Kata so menandai akibatnya.", SUBSIDENCE),
        q("bc3", "\"As a result, sea water flows into low areas more easily.\" This is a result of ....",
            listOf("the government's plan", "the land going down", "more piped water", "fewer deep wells", "the soil becoming harder"), 1,
            "Rantai sebab-akibat: air tanah dipompa, tanah turun, air laut lebih mudah masuk. Yang langsung menyebabkannya adalah tanah yang turun.", SUBSIDENCE),
        q("bc4", "What does the government plan to do?",
            listOf("Move people out of Jakarta", "Build more deep wells", "Extend the piped water network and limit deep wells", "Stop the high tide", "Close all factories"), 2,
            "Kalimat terakhir menyebut dua langkah itu.", SUBSIDENCE),
        q("bc5", "What do the two texts discuss?",
            listOf("How to write a book", "The prices of tablets", "Printed books and e-books for students", "Eye diseases", "Libraries in schools"), 2,
            "Teks 1 membela buku cetak, Teks 2 membahas e-book. Topik bersama: pilihan buku untuk siswa.", PAIRED),
        q("bc6", "Which point is mentioned in BOTH texts?",
            listOf("E-books are cheaper.", "Screens can make the eyes tired.", "Printed books have no notifications.", "Readers can change the font size.", "Tablets can hold hundreds of books."), 1,
            "Teks 1: \"less eye strain\" pada kertas. Teks 2: \"long hours in front of a screen can make the eyes tired\". Pilihan lain hanya ada di satu teks.", PAIRED),
        q("bc7", "How is Text 2 different from Text 1?",
            listOf("Text 2 only gives disadvantages of e-books.", "Text 2 mentions both advantages and a disadvantage of e-books, while Text 1 supports printed books only.", "Text 2 supports printed books.", "Text 1 discusses the price of books.", "Both texts reject e-books."), 1,
            "Teks 2 menyebut kelebihan e-book lalu \"However\" satu kekurangan. Teks 1 sepenuhnya membela buku cetak.", PAIRED),
    )
}
