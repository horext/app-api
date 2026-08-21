package db.csv

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalTime
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CsvSchemaTest {
    data class ImportRow(
        val code: String,
        val name: String,
        val vacancies: Int,
        val start: LocalTime?,
        val teacherDni: String?,
    )

    private val schema =
        csvSchema<ImportRow>("test") {
            val code =
                requiredString("code") {
                    trim()
                    uppercase()
                    maxLength(10)
                    normalize { it.replace("-", "") }
                }
            val name =
                requiredString("name") {
                    trim()
                    maxLength(100)
                    rejectControlCharacters()
                }
            val vacancies =
                intColumn("vacancies") {
                    default(0)
                    range(0..100)
                }
            val start =
                timeColumn("start") {
                    formats("HH:mm", "H")
                }
            val teacherDni =
                optionalString("teacher_dni") {
                    trim()
                    maxLength(50)
                }

            mapRow {
                ImportRow(code(), name(), vacancies(), start(), teacherDni())
            }

            validateFile {
                uniqueBy("code") { it.code }
                maxDistinct("teachers", 2) { it.teacherDni ?: it.name }
                consistentMapping("teacher DNI to name", { it.teacherDni }, { it.name })
                crossField("start", "start time must be present when vacancies are positive") {
                    it.vacancies == 0 || it.start != null
                }
            }
        }

    @Test
    fun `maps typed normalized values without reflection`() {
        val result = schema.parse("test.csv", "code,name,vacancies,start,teacher_dni\n ab-1 ,José Álvarez,12,8, ".byteInputStream())

        assertEquals("AB1", result.rows.single().code)
        assertEquals("José Álvarez", result.rows.single().name)
        assertEquals(12, result.rows.single().vacancies)
        assertEquals(LocalTime.of(8, 0), result.rows.single().start)
        assertNull(result.rows.single().teacherDni)
        assertEquals(2, result.records.single().rowNumber)
    }

    @Test
    fun `collects typed field errors with row and column locations`() {
        val error =
            assertThrows<CsvImportException> {
                schema.parse(
                    "bad.csv",
                    "code,name,vacancies,start,teacher_dni\n,valid,101,bad,DNI\nOK,bad\u0000name,x,08:00,DNI2"
                        .byteInputStream(),
                )
            }

        assertEquals(5, error.errors.size)
        assertTrue(error.errors.any { it is CsvImportError.MissingValue && it.location.column == "code" })
        assertTrue(error.errors.any { it is CsvImportError.InvalidValue && it.location.column == "vacancies" })
        assertTrue(error.errors.any { it is CsvImportError.InvalidValue && it.location.column == "start" })
        assertTrue(error.errors.any { it is CsvImportError.InvalidValue && it.location.column == "name" })
    }

    @Test
    fun `detects duplicate keys and conflicting mappings`() {
        val error =
            assertThrows<CsvImportException> {
                schema.parse(
                    "duplicates.csv",
                    listOf(
                        "code,name,vacancies,start,teacher_dni",
                        "A,Alice,0,,1",
                        "A,Alicia,0,,1",
                    ).joinToString("\n").byteInputStream(),
                )
            }

        assertTrue(error.errors.any { it is CsvImportError.DuplicateValue })
        assertTrue(error.errors.any { it is CsvImportError.ConflictingMapping })
    }

    @Test
    fun `bounds automatically creatable cardinality through max distinct validation`() {
        val error =
            assertThrows<CsvImportException> {
                schema.parse(
                    "teachers.csv",
                    listOf(
                        "code,name,vacancies,start,teacher_dni",
                        "A,Alice,0,,1",
                        "B,Bob,0,,2",
                        "C,Carol,0,,3",
                    ).joinToString("\n").byteInputStream(),
                )
            }

        val limit = assertIs<CsvImportError.LimitExceeded>(error.errors.single())
        assertEquals("distinct teachers", limit.limit)
    }

    @Test
    fun `rejects short and extra records`() {
        val short =
            assertThrows<CsvImportException> {
                schema.parse("short.csv", "code,name,vacancies,start,teacher_dni\nA,Alice".byteInputStream())
            }
        assertIs<CsvImportError.MalformedRecord>(short.errors.single())

        val extra =
            assertThrows<CsvImportException> {
                schema.parse("extra.csv", "code,name,vacancies,start,teacher_dni\nA,Alice,0,,,extra".byteInputStream())
            }
        assertIs<CsvImportError.MalformedRecord>(extra.errors.single())
    }

    @Test
    fun `requires bounded creation policies`() {
        assertThrows<IllegalArgumentException> {
            ReferenceRule<ImportRow, String>(
                name = "teacher",
                key = { it.teacherDni },
                policy = ReferencePolicy.CREATE_IF_MISSING,
            )
        }
    }

    @Test
    fun `reports cross-field rules at the source row`() {
        val error =
            assertThrows<CsvImportException> {
                schema.parse(
                    "cross-field.csv",
                    "code,name,vacancies,start,teacher_dni\nA,Alice,1,,1".byteInputStream(),
                )
            }

        val invalid = assertIs<CsvImportError.InvalidValue>(error.errors.single())
        assertEquals(2, invalid.location.row)
        assertEquals("start time must be present when vacancies are positive", invalid.reason)
    }
}
