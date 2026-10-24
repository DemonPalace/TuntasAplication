package id.demonpalace.tuntas

/** A body paragraph that starts with this is the Indonesian gloss of the English paragraph above it. */
const val GLOSS = "ID: "

/**
 * The B. Inggris book. The body is in English, as on the test; important explanations are
 * followed by a short Indonesian gloss ([GLOSS]), and key terms are written "Indonesian (English)".
 */
object LessonsBig {

    private fun ex(problem: String, vararg steps: String, answer: String) = Example(problem, steps.toList(), answer)

    // ============================== READING STRATEGIES ==============================

    val strategies = Section(
        title = "Strategi membaca (reading strategies)",
        explain = listOf(
            "Each exam reading text is followed by two to four questions. Read the questions first, then the text. You will know what to look for and will not read the same paragraph three times.",
            "Skimming is reading quickly for the general idea: the title, the first sentence of each paragraph, and the last paragraph. Use it for questions about the topic, main idea, and purpose.",
            "ID: Skimming (membaca sekilas) dipakai untuk soal topik, ide pokok, dan tujuan teks. Cukup baca judul, kalimat pertama tiap paragraf, dan paragraf terakhir.",
            "Scanning is searching for one piece of information: a name, a number, a date, a place. Take the key word from the question (when, where, how many) and move your eyes down the text until you find it.",
            "ID: Scanning (membaca memindai) dipakai untuk soal detail. Ambil kata kunci dari soal, lalu cari kata itu atau sinonimnya di teks.",
            "The correct option for a detail question is usually a paraphrase. The text says \"the bridge was finished in 2019\"; the option says \"the construction was completed five years ago\". An option that copies a phrase from the text word for word is often a trap.",
        ),
        examples = listOf(
            ex("Question: \"How long did the writer stay in Bandung?\" Text: \"We arrived in Bandung on Friday evening and drove back to Jakarta on Monday morning.\"",
                "Key words: how long, stay. The answer is a length of time.",
                "Friday evening to Monday morning covers Friday, Saturday, and Sunday nights.",
                answer = "Three nights"),
        ),
        pitfalls = listOf(
            "Choosing the option that repeats words from the text but changes the fact, e.g. \"arrived on Monday\" when Monday is the day they left.",
        ),
        points = listOf(
            "Read the questions first.",
            "Skimming for main idea, scanning for details.",
            "Correct answers are usually paraphrases.",
        ),
    )

    val vocabulary = Section(
        title = "Kosakata dalam konteks (vocabulary in context)",
        explain = listOf(
            "A vocabulary question asks for the meaning of a word as it is used in the passage, not its first meaning in a dictionary. The word \"run\" means something different in \"run a shop\" and \"run in the park\".",
            "Look at the context clues around the word. A definition clue gives the meaning directly (\"drought, a long period without rain\"). A contrast clue uses however, but, unlike, or in contrast, so the word means the opposite of something nearby. An example clue lists items with such as or for example.",
            "ID: Petunjuk konteks (context clues): definisi langsung, pertentangan (however, but, in contrast), dan contoh (such as). Kata yang ditanyakan dicari maknanya dari kalimat di sekitarnya.",
            "After choosing, replace the word with your option and read the sentence again. If the sentence keeps the same meaning, the option is right.",
            "Some questions ask for the opposite meaning (antonym, lawan kata). Read the question word carefully: closest in meaning, or opposite in meaning.",
        ),
        examples = listOf(
            ex("\"The old library was cramped and dark. In contrast, the new one is spacious and bright.\" The word \"spacious\" is closest in meaning to ....",
                "\"In contrast\" signals the opposite of the previous sentence.",
                "The old library was cramped (sempit), so the new one is wide.",
                "Replace: \"the new one is roomy and bright\" keeps the meaning.",
                answer = "roomy"),
            ex("\"Farmers in the area faced a severe drought; no rain fell for five months.\" The word \"severe\" is OPPOSITE in meaning to ....",
                "Five months without rain describes a serious drought.",
                "Severe means serious or very bad; the opposite is mild.",
                answer = "mild"),
        ),
        pitfalls = listOf(
            "Answering a synonym when the question asks \"opposite in meaning\".",
            "Picking the most common dictionary meaning of a word with several meanings, e.g. \"bank\" of a river.",
        ),
        points = listOf(
            "Meaning comes from the sentence, not the dictionary.",
            "Contrast words reverse the meaning.",
            "Replace and re-read to check.",
        ),
    )

    val mainIdea = Section(
        title = "Ide pokok & detail pendukung (main idea & supporting details)",
        explain = listOf(
            "The main idea of a paragraph is the point the writer wants to make about the topic. It is usually stated in the topic sentence, which is most often the first sentence and sometimes the last.",
            "Supporting details explain the main idea with examples, reasons, numbers, or descriptions. A question such as \"Which statement is NOT mentioned?\" checks the supporting details one by one.",
            "ID: Ide pokok (main idea) biasanya ada di kalimat utama (topic sentence), umumnya kalimat pertama. Kalimat lain adalah detail pendukung (supporting details): contoh, alasan, atau angka.",
            "The main idea of the whole text joins the main ideas of all paragraphs. An option that is true but describes only one paragraph is too narrow. An option that goes beyond the text is too broad.",
        ),
        examples = listOf(
            ex("\"Komodo dragons are the largest lizards in the world. An adult can grow up to 3 metres long and weigh about 70 kilograms. They live only on a few islands in East Nusa Tenggara.\" The main idea of the paragraph is ....",
                "First sentence: Komodo dragons are the largest lizards.",
                "Length, weight, and islands are supporting details.",
                answer = "Komodo dragons are the largest lizards in the world"),
        ),
        pitfalls = listOf(
            "Choosing a supporting detail, e.g. \"Komodo dragons can grow up to 3 metres\", as the main idea because it contains a number.",
        ),
        points = listOf(
            "Main idea is in the topic sentence.",
            "Too narrow: only one detail. Too broad: beyond the text.",
        ),
    )

    val reference = Section(
        title = "Kata rujukan (reference words)",
        explain = listOf(
            "Reference words are pronouns that point back to a noun already mentioned: it, they, he, she, this, these, those, them, its, their, which, who.",
            "To answer \"The word 'they' refers to ....\", look at the sentences before the pronoun and list the nouns. Keep only the nouns that match in number: it and this are singular, they and these are plural. Then replace the pronoun with each candidate and read the sentence again.",
            "ID: Kata rujukan (reference words) menunjuk kata benda sebelumnya. Cocokkan jumlahnya (it tunggal, they jamak), lalu ganti kata rujukan dengan pilihan jawaban dan baca ulang kalimatnya.",
            "\"This\" or \"it\" at the start of a sentence can refer to a whole idea in the previous sentence, not just one noun.",
        ),
        examples = listOf(
            ex("\"My cat, Milo, has soft orange fur and big green eyes. He is very playful and likes to chase small balls around the house.\" The word \"He\" refers to ....",
                "He is singular and male.",
                "Nouns before it: my cat Milo, fur, eyes. Only Milo can be \"he\".",
                "Check: \"Milo is very playful\" makes sense.",
                answer = "Milo"),
            ex("\"The government built 40 new bridges in rural areas last year. This made it easier for children to reach their schools.\" The word \"This\" refers to ....",
                "\"This\" is followed by \"made it easier\": something caused a result.",
                "The cause is the action in the previous sentence, not a single noun.",
                answer = "the building of 40 new bridges in rural areas"),
        ),
        pitfalls = listOf(
            "Picking the nearest noun even when it does not match in number: \"they\" cannot refer to \"the school\".",
        ),
        points = listOf(
            "Match singular or plural.",
            "Replace and re-read.",
            "\"This\" can refer to a whole idea.",
        ),
    )

    // ============================== TEXT TYPES 1 ==============================

    val descriptive = Section(
        title = "Teks deskriptif (descriptive text)",
        explain = listOf(
            "A descriptive text describes one particular person, animal, thing, or place: \"my cat Milo\", \"Borobudur Temple\", \"my grandmother\". It does not describe cats or temples in general; that would be a report text.",
            "Its structure has two parts. The identification introduces what is described. The description gives its parts, qualities, and habits.",
            "ID: Teks deskriptif (descriptive text) menggambarkan satu objek TERTENTU. Strukturnya: identifikasi (identification) lalu deskripsi (description).",
            "The language features are the simple present tense (is, has, lives), adjectives (tall, ancient, playful), and linking verbs (is, looks, seems).",
            "Typical questions ask what the text describes, a detail of the description, a reference word, and the meaning of an adjective.",
        ),
        examples = listOf(
            ex("\"Borobudur is a ninth-century Buddhist temple in Magelang, Central Java. It has nine stacked platforms and is decorated with 2,672 relief panels. A large stupa stands at the top.\" The text is mainly about ....",
                "One particular place is introduced: Borobudur.",
                "The rest gives its parts: platforms, panels, stupa.",
                answer = "the description of Borobudur Temple"),
        ),
        pitfalls = listOf(
            "Calling a text about \"elephants\" in general a descriptive text. Descriptive is about one particular object.",
        ),
        points = listOf(
            "Describes one particular object.",
            "Identification, then description.",
            "Simple present tense and adjectives.",
        ),
    )

    val recount = Section(
        title = "Teks recount (recount text)",
        explain = listOf(
            "A recount text retells events that really happened in the past, usually in the order they happened: a holiday, a school trip, a competition. Its purpose is to retell past events.",
            "The structure is orientation (who, when, where), events (what happened, in order), and reorientation (a closing comment, often the writer's feeling).",
            "ID: Teks recount (recount text) menceritakan kembali pengalaman nyata di masa lalu. Strukturnya: orientasi (orientation), rangkaian peristiwa (events), dan reorientasi (reorientation).",
            "The language features are the simple past tense (went, visited, ate), time signals (last Sunday, then, after that, in the afternoon), and the pronouns I or we.",
            "A recount has no complication. If the story has a problem that must be solved, it is a narrative.",
        ),
        examples = listOf(
            ex("\"Last Sunday, my family and I went to Parangtritis Beach. We left home at 6 a.m. and arrived at 8 a.m. We swam, built sandcastles, and ate grilled fish for lunch. In the afternoon, we went home tired but happy.\" When did the family arrive at the beach?",
                "Key word: arrive.",
                "The text says \"arrived at 8 a.m.\"",
                "6 a.m. is when they left home.",
                answer = "At 8 a.m."),
        ),
        pitfalls = listOf(
            "Answering with the departure time when the question asks the arrival time; both numbers are in the same sentence.",
        ),
        points = listOf(
            "Retells real past events.",
            "Orientation, events, reorientation.",
            "Simple past tense and time signals.",
        ),
    )

    val narrative = Section(
        title = "Teks naratif (narrative text)",
        explain = listOf(
            "A narrative text is a story: a legend, fable, fairy tale, or short story. Its purpose is to entertain and often to teach a lesson. The feature that separates it from a recount is the complication, a problem the characters must face.",
            "The structure is orientation (characters and setting), complication (the problem rises to a climax), and resolution (the problem is solved, happily or not). Some narratives end with a coda that states the moral.",
            "ID: Teks naratif (narrative text) adalah cerita yang memiliki konflik (complication). Strukturnya: orientasi, komplikasi, dan resolusi. Tujuannya menghibur.",
            "Questions often ask about the main problem, a character's trait, the turning point, and the moral value.",
        ),
        examples = listOf(
            ex("\"Malin Kundang became a rich merchant after years of sailing. When his ship landed in his hometown, his old mother ran to hug him. Ashamed of her poor clothes, Malin said she was not his mother.\" Which part of the story is this?",
                "The characters are already known and a problem appears: Malin rejects his mother.",
                "The problem has not been solved yet.",
                answer = "Complication"),
        ),
        pitfalls = listOf(
            "Labelling the paragraph where the problem first appears as orientation. Orientation only introduces characters and setting.",
        ),
        points = listOf(
            "A story with a complication.",
            "Orientation, complication, resolution.",
        ),
    )

    val purpose = Section(
        title = "Tujuan teks, sasaran pembaca & pesan moral (purpose, target reader & moral value)",
        explain = listOf(
            "The purpose of a text follows its type. Descriptive: to describe a particular person, place, or thing. Recount: to retell past events. Narrative: to entertain. Procedure: to tell how to make or do something. Analytical exposition: to persuade readers that something is the case. Advertisement: to persuade readers to buy or use something.",
            "ID: Tujuan teks (purpose) ditentukan oleh jenisnya. Hafalkan pasangan ini: describe, retell, entertain, explain how, persuade.",
            "The target reader is inferred from content and language. A notice about the school library's opening hours is for students; a leaflet about vaccination for babies is for parents.",
            "The moral value appears mainly in narratives. Look at what happens to a character because of an action: bad actions are punished, good actions are rewarded. The moral is the lesson behind that result.",
            "ID: Pesan moral (moral value) dicari dari AKIBAT perbuatan tokoh, bukan dari kejadian yang paling menarik.",
        ),
        examples = listOf(
            ex("In a fable, a proud hare laughs at a slow tortoise, takes a nap during the race, and loses. What is the moral value?",
                "The hare's action: being arrogant and careless.",
                "The result: he loses to the slow but steady tortoise.",
                answer = "Do not be arrogant; slow and steady wins the race."),
        ),
        pitfalls = listOf(
            "Choosing \"to entertain\" for a recount. A recount retells a real experience; only a narrative is \"to entertain\".",
        ),
        points = listOf(
            "Purpose follows text type.",
            "Target reader comes from content and language.",
            "Moral value comes from consequences.",
        ),
    )

    // ============================== TEXT TYPES 2 ==============================

    val procedure = Section(
        title = "Teks prosedur (procedure text)",
        explain = listOf(
            "A procedure text tells the reader how to make or do something: a recipe, instructions for a device, steps to register for an exam.",
            "The structure is goal (what will be made or done), materials (ingredients and tools), and steps (what to do, in order).",
            "ID: Teks prosedur (procedure text) menjelaskan cara membuat atau melakukan sesuatu. Strukturnya: tujuan (goal), bahan dan alat (materials), dan langkah-langkah (steps).",
            "The language features are imperative sentences that start with a verb (Boil, Add, Stir, Press) and sequence words (First, Then, Next, After that, Finally).",
            "Questions ask about the purpose, the step before or after a given step, the materials needed, and what happens if a step is skipped.",
        ),
        examples = listOf(
            ex("\"First, boil 400 ml of water. Then, add the noodles and cook them for three minutes. After that, turn off the stove. Finally, add the seasoning and stir well.\" What should you do right after cooking the noodles for three minutes?",
                "Find \"three minutes\" in the steps.",
                "The next sequence word is \"After that\".",
                answer = "Turn off the stove."),
        ),
        pitfalls = listOf(
            "Taking the last step (\"Finally\") when the question asks for the step right after a given one.",
        ),
        points = listOf(
            "Tells how to make or do something.",
            "Goal, materials, steps.",
            "Imperative verbs and sequence words.",
        ),
    )

    val exposition = Section(
        title = "Teks eksposisi analitis (analytical exposition)",
        explain = listOf(
            "An analytical exposition presents the writer's opinion and supports it with arguments. Its purpose is to persuade the reader that the opinion is right.",
            "The structure is thesis (the writer's position, usually in the first paragraph), arguments (reasons with evidence, often marked First, Second, Moreover), and reiteration (the position restated at the end, often with Therefore or In conclusion).",
            "ID: Teks eksposisi analitis (analytical exposition) berisi pendapat penulis beserta argumennya. Strukturnya: tesis (thesis), argumen (arguments), dan penegasan ulang (reiteration).",
            "The language features are modal verbs (should, must, need to), the simple present tense, and evaluative words (important, harmful, effective).",
            "Questions ask about the writer's purpose, the writer's position, the main argument of a paragraph, and the function of the last paragraph.",
        ),
        examples = listOf(
            ex("\"Students should be allowed to bring phones to school. First, phones help students find information quickly during lessons. Second, parents can contact their children in an emergency. Therefore, schools should allow phones as long as there are clear rules for using them.\" The last sentence functions as the ....",
                "It starts with \"Therefore\".",
                "It repeats the position of the first sentence.",
                answer = "reiteration"),
        ),
        pitfalls = listOf(
            "Calling the last paragraph a \"conclusion of the story\" or resolution. In exposition it is the reiteration.",
        ),
        points = listOf(
            "Persuades with arguments.",
            "Thesis, arguments, reiteration.",
            "Modals: should, must.",
        ),
    )

    val connectors = Section(
        title = "Kata sambung (connectors)",
        explain = listOf(
            "Connectors show how two ideas are related. Contrast: however, but, yet, although, despite, in contrast, on the other hand. Result: so, therefore, as a result, consequently. Addition: moreover, furthermore, in addition, also. Cause: because, because of, due to, since.",
            "ID: Kata sambung (connectors) menunjukkan hubungan gagasan: pertentangan (contrast), akibat (result), tambahan (addition), dan sebab (cause).",
            "Grammar matters in gap-fill questions. Although and because are followed by a clause (subject and verb): although it rained. Despite and because of are followed by a noun phrase: despite the rain, because of the rain.",
            "ID: Although/because + kalimat (ada subjek dan kata kerja). Despite/because of + frasa benda.",
        ),
        examples = listOf(
            ex("\"____ the heavy rain, the farmers continued harvesting the rice.\" Which word fills the gap: Although, Despite, Because, or Therefore?",
                "Heavy rain usually stops a harvest, but they continued: contrast.",
                "\"The heavy rain\" is a noun phrase, with no verb.",
                "Contrast + noun phrase: Despite.",
                answer = "Despite"),
        ),
        pitfalls = listOf(
            "Putting \"although\" before a noun phrase (\"although the rain\"). Although needs a full clause.",
        ),
        points = listOf(
            "However, despite: contrast. Therefore, so: result.",
            "Although + clause; despite + noun.",
        ),
    )

    val functional = Section(
        title = "Teks fungsional pendek (short functional texts)",
        explain = listOf(
            "Short functional texts are notices, announcements, advertisements, invitations, and labels. They are short, but each word carries information, so read them twice.",
            "A notice gives a warning or rule (\"Do not feed the animals\"). An announcement informs readers of an event: what, when, where, and for whom. An advertisement persuades readers to buy or use a product or service.",
            "ID: Teks fungsional pendek (short functional texts): pemberitahuan (notice) berisi larangan atau aturan, pengumuman (announcement) berisi informasi acara, iklan (advertisement) membujuk pembaca membeli.",
            "Questions ask where the text is likely found, who it is for, what readers must do, and the purpose.",
        ),
        examples = listOf(
            ex("\"ATTENTION: The school library will be closed on 30 October for stock-taking. Books due on that day can be returned on 31 October without a fine.\" What is the announcement mainly about?",
                "The key information is the closing date.",
                "The second sentence is a detail for students with books due.",
                answer = "The library will be closed on 30 October."),
        ),
        pitfalls = listOf(
            "Choosing a detail (no fine on 31 October) as the main message of the announcement.",
        ),
        points = listOf(
            "Notice: rule or warning. Announcement: event. Advertisement: persuade.",
            "Ask where, who for, and what to do.",
        ),
    )

    val factOpinion = Section(
        title = "Fakta & opini (fact & opinion)",
        explain = listOf(
            "A fact can be checked and proved true or false: \"Coral reefs cover less than one percent of the ocean floor.\" An opinion is a feeling, belief, or judgment that other people may disagree with: \"Coral reefs are the most beautiful places on Earth.\"",
            "Opinion signals: I think, I believe, in my opinion, should, must, best, worst, beautiful, boring, important. Facts often contain numbers, dates, places, and research results.",
            "ID: Fakta (fact) dapat dibuktikan. Opini (opinion) adalah penilaian dan bisa dibantah. Kata penanda opini: should, best, beautiful, I think.",
            "A sentence with a number is not automatically a fact. \"The price of Rp15.000 is too expensive\" contains a number and a judgment, so it is an opinion.",
            "In stories, reality is what can happen in real life; fantasy is what cannot, such as talking animals or magic.",
        ),
        examples = listOf(
            ex("Which sentence is an opinion? (A) The museum opened in 1995. (B) The museum has 3,000 items. (C) The museum is the best place to spend a weekend. (D) The museum is closed on Mondays.",
                "A, B, and D can be checked.",
                "\"the best place\" is a judgment.",
                answer = "C"),
        ),
        pitfalls = listOf(
            "Treating a sentence as a fact because it contains a number, even when it also contains a judgment word like \"too expensive\".",
        ),
        points = listOf(
            "Fact: can be proved. Opinion: judgment.",
            "Numbers do not guarantee a fact.",
        ),
    )

    val sequence = Section(
        title = "Urutan peristiwa & ringkasan (sequence & summary)",
        explain = listOf(
            "Sequence questions ask about the order of events. Look for time signals: first, then, next, after that, before, finally, in the afternoon, two days later. Recounts and narratives usually tell events in time order, but a flashback can break that order.",
            "ID: Soal urutan (sequence) dijawab dengan penanda waktu: first, then, after that, before, finally.",
            "A good summary contains the main points of the whole text in different words. It leaves out examples, numbers, and small details. An option that mentions only one paragraph is not a summary.",
            "ID: Ringkasan (summary) memuat pokok seluruh teks, tanpa contoh dan angka rinci.",
        ),
        examples = listOf(
            ex("\"Last Sunday, my family and I went to Parangtritis Beach. We left home at 6 a.m. and arrived at 8 a.m. We swam, built sandcastles, and ate grilled fish for lunch. In the afternoon, we went home tired but happy.\" What did they do right after arriving?",
                "Order: left at 6, arrived at 8, swam and built sandcastles, lunch, went home.",
                "Right after arriving comes swimming and building sandcastles.",
                answer = "They swam and built sandcastles."),
        ),
        pitfalls = listOf(
            "Choosing a \"summary\" option that only restates the first paragraph.",
        ),
        points = listOf(
            "Time signals show order.",
            "Summary: main points of the whole text.",
        ),
    )

    val inference = Section(
        title = "Informasi tersirat & prediksi (inference & prediction)",
        explain = listOf(
            "An inference question asks for information that is not written directly but is supported by clues. Questions use the words imply, infer, suggest, or \"It can be concluded that ....\"",
            "ID: Informasi tersirat (inference) tidak ditulis langsung, tetapi harus didukung petunjuk di teks. Kata soalnya: imply, infer, conclude.",
            "The right inference stays close to the text. Reject an option that needs information the text does not give, even if it sounds reasonable in real life.",
            "Predicting the outcome means choosing what most likely happens next, based on the characters' actions and traits so far.",
        ),
        examples = listOf(
            ex("\"Rina checked the weather app, sighed, and put her umbrella and raincoat into her bag before leaving.\" What can be inferred?",
                "Clues: she checked the weather, sighed, packed rain gear.",
                "The app probably showed rain.",
                answer = "Rina expected it to rain that day."),
        ),
        pitfalls = listOf(
            "Choosing an option that is true in real life but has no clue in the text, e.g. \"Rina does not like rain\".",
        ),
        points = listOf(
            "Inference needs clues in the text.",
            "Reject options that go beyond the text.",
        ),
    )

    val causeEffect = Section(
        title = "Sebab-akibat (cause & effect)",
        explain = listOf(
            "Cause signals: because, because of, due to, since, as. Effect signals: so, therefore, as a result, consequently, lead to, cause. Ask which event happened first and made the other happen.",
            "ID: Sebab (cause) terjadi lebih dulu dan menimbulkan akibat (effect). Penanda sebab: because, due to. Penanda akibat: so, therefore, as a result.",
            "A text can contain a chain: A causes B, and B causes C. Read the question to see which link it asks about.",
        ),
        examples = listOf(
            ex("\"Rising sea temperatures cause corals to lose the algae that feed them. Without the algae, corals turn white and may die.\" What is the direct effect of rising sea temperatures?",
                "Cause: rising sea temperatures.",
                "First effect: corals lose their algae. Second effect: they turn white and may die.",
                answer = "Corals lose the algae that feed them."),
        ),
        pitfalls = listOf(
            "Reversing cause and effect, or choosing the last effect in a chain when the question asks for the direct one.",
        ),
        points = listOf(
            "Because, due to: cause. So, therefore: effect.",
            "Check which link of the chain is asked.",
        ),
    )

    val comparing = Section(
        title = "Membandingkan dua teks (comparing two texts)",
        explain = listOf(
            "An exam can show two short texts on the same topic and ask how they are similar or different, or what conclusion follows from both.",
            "Similarity signals: similarly, likewise, both, also. Difference signals: whereas, while, unlike, on the other hand, in contrast.",
            "ID: Soal dua teks (paired texts) menanyakan persamaan, perbedaan, atau simpulan gabungan. Baca ide pokok tiap teks dulu, lalu bandingkan.",
            "Synthesizing means combining information from both texts. The correct option must be supported by Text 1 and Text 2 together, not by only one of them.",
        ),
        examples = listOf(
            ex("Text 1: \"Electric scooters cut travel time in busy cities.\" Text 2: \"Electric scooters produce no exhaust fumes, but their batteries are hard to recycle.\" Both texts discuss ....",
                "Text 1: advantage in time. Text 2: advantage and disadvantage for the environment.",
                "The common topic is the effects of electric scooters.",
                answer = "the effects of using electric scooters"),
        ),
        pitfalls = listOf(
            "Choosing an option that is true for only one text, e.g. \"batteries are hard to recycle\", when the question asks what both texts discuss.",
        ),
        points = listOf(
            "Find each text's main idea first.",
            "A shared conclusion must fit both texts.",
        ),
    )

    val big1 = listOf(strategies, vocabulary, mainIdea, reference, descriptive, recount, narrative, purpose)
    val big2 = listOf(procedure, exposition, connectors, functional, factOpinion, sequence, inference, causeEffect, comparing)
}
