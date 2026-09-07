package db.migration

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class SeedStudyPlansChecksumTest {
    @TempDir
    lateinit var resources: Path

    @Test
    fun `checksum covers only the three sources belonging to one study plan`() {
        val dataDirectory = Files.createDirectories(resources.resolve("db/data"))
        val subjects = dataDirectory.resolve("study_plan_subjects_PLAN-A.csv")
        val relationships = dataDirectory.resolve("study_plan_relationships_PLAN-A.csv")
        val unrelated = dataDirectory.resolve("study_plan_subjects_PLAN-B.csv")
        Files.writeString(subjects, "course_id\nCOURSE-A\n")
        Files.writeString(relationships, "from_course_id,to_course_id\nCOURSE-A,COURSE-B\n")
        Files.writeString(unrelated, "course_id\nUNRELATED\n")

        withResourceClassLoader {
            val migration = R__050_SeedStudyPlans()
            val original = migration.checksumForPlanA()

            assertNotEquals(
                original,
                migration.calculateStudyPlanChecksum(
                    code = "PLAN-A",
                    fromDate = Instant.parse("2026-01-01T00:00:00Z"),
                    organizationUnitCode = "UNIT-A-CHANGED",
                ),
            )

            Files.writeString(unrelated, "course_id\nCHANGED-UNRELATED\n")
            assertEquals(original, migration.checksumForPlanA())

            Files.writeString(subjects, "course_id\nCOURSE-A-CHANGED\n")
            val subjectsChanged = migration.checksumForPlanA()
            assertNotEquals(original, subjectsChanged)

            Files.writeString(relationships, "from_course_id,to_course_id\nCOURSE-B,COURSE-A\n")
            assertNotEquals(subjectsChanged, migration.checksumForPlanA())
        }
    }

    private fun R__050_SeedStudyPlans.checksumForPlanA() =
        calculateStudyPlanChecksum(
            code = "PLAN-A",
            fromDate = Instant.parse("2026-01-01T00:00:00Z"),
            organizationUnitCode = "UNIT-A",
        )

    private fun withResourceClassLoader(block: () -> Unit) {
        val thread = Thread.currentThread()
        val previous = thread.contextClassLoader
        URLClassLoader(arrayOf(resources.toUri().toURL()), previous).use { classLoader ->
            thread.contextClassLoader = classLoader
            try {
                block()
            } finally {
                thread.contextClassLoader = previous
            }
        }
    }
}
