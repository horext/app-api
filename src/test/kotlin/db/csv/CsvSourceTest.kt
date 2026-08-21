package db.csv

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayInputStream
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CsvSourceTest {
    @Test
    fun `parses quoted delimiters escaped quotes and multiline fields`() {
        val csv =
            listOf(
                "first,second",
                "normal,value",
                "\"comma,value\",x",
                "\"quoted \"\"value\"\"\",x",
                "\"multi",
                "line\",x",
            ).joinToString("\n")

        val rows = read(csv)

        assertEquals(
            listOf(
                listOf("normal", "value"),
                listOf("comma,value", "x"),
                listOf("quoted \"value\"", "x"),
                listOf("multi\nline", "x"),
            ),
            rows,
        )
    }

    @Test
    fun `supports an explicit semicolon format`() {
        val rows = read("first;second\nsemicolon;value", delimiter = ';')

        assertEquals(listOf(listOf("semicolon", "value")), rows)
    }

    @Test
    fun `strips a UTF-8 BOM`() {
        val bytes = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()) + "first,second\na,b".toByteArray()
        val rows =
            CsvSource().read("bom.csv", ByteArrayInputStream(bytes), setOf("first", "second")) {
                it.record.toList()
            }

        assertEquals(listOf(listOf("a", "b")), rows)
    }

    @Test
    fun `reports a missing required header`() {
        val error = assertThrows<CsvImportException> { read("first\na", requiredHeaders = setOf("second")) }

        assertIs<CsvImportError.MissingColumn>(error.errors.single())
        assertTrue(error.message!!.contains("missing required column \"second\""))
    }

    @Test
    fun `rejects duplicate headers`() {
        val error = assertThrows<CsvImportException> { read("first,first\na,b") }

        assertIs<CsvImportError.MalformedRecord>(error.errors.single())
    }

    @Test
    fun `rejects an unmatched quote`() {
        val error = assertThrows<CsvImportException> { read("first,second\n\"unfinished,value") }

        assertIs<CsvImportError.MalformedRecord>(error.errors.single())
    }

    @Test
    fun `enforces the row limit while iterating`() {
        val error =
            assertThrows<CsvImportException> {
                read("first\na\nb", limits = CsvLimits(maxRows = 1))
            }

        val limit = assertIs<CsvImportError.LimitExceeded>(error.errors.single())
        assertEquals("rows", limit.limit)
        assertEquals(2, limit.actual)
    }

    @Test
    fun `enforces file and field limits`() {
        val fileError =
            assertThrows<CsvImportException> {
                read("first\nvalue", limits = CsvLimits(maxFileBytes = 5))
            }
        assertEquals("file bytes", assertIs<CsvImportError.LimitExceeded>(fileError.errors.single()).limit)

        val fieldError =
            assertThrows<CsvImportException> {
                read("first\nvalue", limits = CsvLimits(maxFieldLength = 4))
            }
        val limit = assertIs<CsvImportError.LimitExceeded>(fieldError.errors.single())
        assertEquals("first", limit.location.column)
        assertEquals(2, limit.location.row)
    }

    private fun read(
        csv: String,
        delimiter: Char = ',',
        limits: CsvLimits = CsvLimits(),
        requiredHeaders: Set<String> = setOf("first"),
    ): List<List<String>> =
        CsvSource(delimiter, limits).read(
            file = "test.csv",
            input = csv.byteInputStream(),
            requiredHeaders = requiredHeaders,
        ) { it.record.toList() }
}
