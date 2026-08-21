package db.migration.schema

import db.csv.CsvImportError
import db.csv.CsvImportException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class HourlyLoadCsvSchemaTest {
    private val defaultUpdatedAt = LocalDateTime.of(2026, 1, 1, 0, 0)

    @Test
    fun `maps the hourly-load shape with explicit normalization and defaults`() {
        val csv =
            """
            codigo_curso,seccion,inicio,fin,nombre_docente,dia
            AB-123, 01 ,8,09:30,"José ""Pepe"", Álvarez",LU
            """.trimIndent()

        val row = parse(csv).single()

        assertEquals("I", row.facultyCode)
        assertEquals("AB123", row.course)
        assertEquals("01", row.section)
        assertEquals(0, row.vacancies)
        assertEquals(defaultUpdatedAt, row.updatedAt)
        assertEquals("08:00", row.startTime)
        assertEquals("09:30", row.endTime)
        assertEquals("José \"Pepe\", Álvarez", row.teacherName)
        assertEquals("NO_CLASSROOM", row.classroom)
        assertEquals("UNSPECIFIED", row.sessionType)
    }

    @Test
    fun `keeps SQL and formula-like text as data`() {
        val values =
            listOf(
                "Robert'); DROP TABLE teacher;--",
                "' OR '1'='1",
                "=HYPERLINK(\"https://evil.example\")",
                "../../etc/passwd",
            )
        val csv =
            buildString {
                appendLine("codigo_curso,seccion,inicio,fin,nombre_docente,dia")
                values.forEachIndexed { index, value ->
                    append("C$index,A,08:00,09:00,")
                    append(csvValue(value))
                    appendLine(",LU")
                }
            }

        val rows = parse(csv)

        assertEquals(values, rows.map { it.teacherName })
    }

    @Test
    fun `reports missing required hourly-load headers`() {
        val error =
            assertThrows<CsvImportException> {
                parse("codigo_curso,seccion,inicio,fin,dia\nCS101,A,08:00,09:00,LU")
            }

        val missing = assertIs<CsvImportError.MissingColumn>(error.errors.single())
        assertEquals("nombre_docente", missing.expected)
    }

    @Test
    fun `reports timestamp errors and teacher identity conflicts`() {
        val invalidTimestamp =
            assertThrows<CsvImportException> {
                parse(
                    "codigo_curso,seccion,inicio,fin,nombre_docente,dia,updated_at\n" +
                        "CS101,A,08:00,09:00,Ada,LU,not-a-date",
                )
            }
        assertTrue(invalidTimestamp.errors.any { it.location.column == "updated_at" })

        val conflictingTeacher =
            assertThrows<CsvImportException> {
                parse(
                    "codigo_curso,seccion,inicio,fin,nombre_docente,dia,dni_docente\n" +
                        "CS101,A,08:00,09:00,Ada,LU,1\n" +
                        "CS102,B,10:00,11:00,Grace,MA,1",
                )
            }
        assertIs<CsvImportError.ConflictingMapping>(conflictingTeacher.errors.single())
    }

    private fun parse(csv: String) =
        hourlyLoadCsvSchema("I", defaultUpdatedAt)
            .parse("hourly.csv", csv.byteInputStream())
            .rows

    private fun csvValue(value: String): String = "\"${value.replace("\"", "\"\"")}\""
}
