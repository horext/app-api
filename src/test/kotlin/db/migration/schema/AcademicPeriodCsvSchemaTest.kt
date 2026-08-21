package db.migration.schema

import db.csv.CsvImportException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AcademicPeriodCsvSchemaTest {
    @Test
    fun `parses local and offset timestamps with legacy UTC behavior`() {
        val csv =
            "code,from_date,to_date,faculty_code\n" +
                "2026-1,2026-01-02 03:04:05,2026-06-01 10:00:00-05:00,I"

        val row = academicPeriodCsvSchema().parse("period.csv", csv.byteInputStream()).rows.single()

        assertEquals(Instant.parse("2026-01-02T03:04:05Z"), row.fromDate)
        assertEquals(Instant.parse("2026-06-01T15:00:00Z"), row.toDate)
        assertEquals("I", row.facultyCode)
    }

    @Test
    fun `supports optional timestamp columns`() {
        val csv = "code,faculty_code\n2026-1,I"

        val row = academicPeriodCsvSchema().parse("period.csv", csv.byteInputStream()).rows.single()

        assertEquals("2026-1", row.code)
        assertEquals(null, row.fromDate)
        assertEquals(null, row.toDate)
    }

    @Test
    fun `reports malformed timestamps with their column`() {
        val error =
            assertThrows<CsvImportException> {
                academicPeriodCsvSchema()
                    .parse(
                        "period.csv",
                        "code,from_date,faculty_code\n2026-1,bad,I".byteInputStream(),
                    )
            }

        assertTrue(error.errors.any { it.location.column == "from_date" })
    }
}
