package id.demonpalace.tuntas

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentTest {
    @Test fun stepIdsAreUnique() {
        val ids = Content.steps.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test fun questionIdsAreUnique() {
        val ids = Content.steps.flatMap { s -> s.questions.map { it.id } }
        assertEquals("duplicate: ${ids.groupBy { it }.filter { it.value.size > 1 }.keys}", ids.size, ids.toSet().size)
    }

    @Test fun answersPointAtAnOption() {
        Content.steps.flatMap { it.questions }.forEach { q ->
            when {
                q.isTable -> {
                    assertTrue("${q.id} needs at least 2 columns", q.options.size >= 2)
                    assertEquals("${q.id} rows/keys mismatch", q.rows.size, q.rowKeys.size)
                    assertTrue("${q.id} row key out of range", q.rowKeys.all { it in q.options.indices })
                }
                q.isMulti -> {
                    assertTrue("${q.id} has too few options", q.options.size >= 4)
                    val keys = q.multi!!
                    assertTrue("${q.id} multi needs 2+ keys", keys.size >= 2)
                    assertTrue("${q.id} multi key out of range", keys.all { it in q.options.indices })
                }
                else -> {
                    assertTrue("${q.id} answer out of range", q.answer in q.options.indices)
                    assertTrue("${q.id} has too few options", q.options.size >= 4)
                }
            }
            assertEquals("${q.id} has duplicate options", q.options.size, q.options.toSet().size)
            assertTrue("${q.id} missing explanation", q.explain.isNotBlank())
        }
    }

    @Test fun everyLessonHasCardsAndPracticeStepsHaveQuestions() {
        Content.steps.forEach { s ->
            if (!s.isPractice) assertTrue("${s.id} has no cards", s.sections.isNotEmpty())
            if (!s.isReview) assertTrue("${s.id} has no questions", s.questions.isNotEmpty())
        }
    }

    @Test fun materialFinishesBefore30OctAndDaysAreOrdered() {
        val material = Content.steps.filter { it.subject != Subject.MIX && !it.title.startsWith("Tryout") && !it.isPractice }
        assertTrue(material.all { it.day <= 6 })
        assertEquals(Content.steps.map { it.day }, Content.steps.map { it.day }.sorted())
    }

    @Test fun mapHasOneStopPerSubChapterAndUniqueIds() {
        val ids = Content.nodes.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
        assertEquals(Content.steps.sumOf { it.sections.size } + Content.steps.count { it.questions.isNotEmpty() }, Content.nodes.size)
        assertTrue("map too short: ${Content.nodes.size}", Content.nodes.size >= 100)
    }

    @Test fun everySectionReadsLikeABook() {
        Content.books.values.flatten().forEach { s ->
            s.sections.forEach { sec ->
                assertTrue("${s.id}/${sec.title} has no prose", sec.explain.isNotEmpty())
                assertTrue("${s.id}/${sec.title} has no summary", sec.points.isNotEmpty())
            }
        }
    }

    @Test fun bankIsImportedAndFiguresExist() {
        assertTrue("only ${Content.questionCount} questions", Content.questionCount >= 230)
        val assets = java.io.File("src/main/assets")
        Content.allQuestions.values.mapNotNull { it.second.image }.forEach {
            assertTrue("missing figure $it", java.io.File(assets, it).exists())
        }
    }

    @Test fun starsScale() {
        assertEquals(3, starsFor(5, 5))
        assertEquals(2, starsFor(4, 5))
        assertEquals(1, starsFor(3, 5))
        assertEquals(0, starsFor(-1, 5))
    }

    @Test fun clockFormatting() {
        assertEquals("5:00:00", fmtClock(5 * 3600_000L))
        assertEquals("0:01:05", fmtClock(65_000L))
        assertEquals("5 jam", fmtMin(300))
        assertEquals("1 j 30 m", fmtMin(90))
        assertEquals("25 menit", fmtMin(25))
    }
}
