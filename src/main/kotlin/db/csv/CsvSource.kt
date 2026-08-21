package db.csv

import org.apache.commons.csv.CSVException
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.apache.commons.csv.DuplicateHeaderMode
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.UncheckedIOException

data class CsvRecordContext(
    val file: String,
    val record: CSVRecord,
) {
    val rowNumber: Long = record.recordNumber + 1
}

class CsvSource(
    private val delimiter: Char = ',',
    private val limits: CsvLimits = CsvLimits.MIGRATION_DEFAULTS,
) {
    fun <T> read(
        file: String,
        input: InputStream,
        requiredHeaders: Set<String> = emptySet(),
        map: (CsvRecordContext) -> T,
    ): List<T> {
        try {
            BoundedInputStream(input, limits.maxFileBytes).use { bounded ->
                bomAwareReader(bounded).use { reader ->
                    val format =
                        CSVFormat.RFC4180
                            .builder()
                            .setDelimiter(delimiter)
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .setIgnoreEmptyLines(true)
                            .setAllowMissingColumnNames(false)
                            .setDuplicateHeaderMode(DuplicateHeaderMode.DISALLOW)
                            .get()

                    val parser =
                        try {
                            format.parse(reader)
                        } catch (error: IllegalArgumentException) {
                            throw malformed(file, error)
                        }
                    parser.use {
                        val missing = requiredHeaders - parser.headerMap.keys
                        if (missing.isNotEmpty()) {
                            throw CsvImportException(
                                missing.sorted().map {
                                    CsvImportError.MissingColumn(CsvLocation(file), it)
                                },
                            )
                        }
                        if (parser.headerMap.size > limits.maxColumns) {
                            throw limit(file, null, "columns", parser.headerMap.size.toLong(), limits.maxColumns.toLong())
                        }

                        val rows = ArrayList<T>()
                        for (record in parser) {
                            val row = record.recordNumber + 1
                            if (record.recordNumber > limits.maxRows) {
                                throw limit(file, row, "rows", record.recordNumber, limits.maxRows)
                            }
                            validateRecord(file, row, record, parser.headerNames)
                            rows += map(CsvRecordContext(file, record))
                        }
                        return rows
                    }
                }
            }
        } catch (error: CsvImportException) {
            throw error
        } catch (error: FileSizeLimitExceededException) {
            throw limit(file, null, "file bytes", error.actual, error.maximum, error)
        } catch (error: CSVException) {
            throw malformed(file, error)
        } catch (error: UncheckedIOException) {
            val cause = error.cause
            if (cause is CSVException) throw malformed(file, cause)
            throw error
        }
    }

    private fun validateRecord(
        file: String,
        row: Long,
        record: CSVRecord,
        headers: List<String>,
    ) {
        if (record.size() > limits.maxColumns) {
            throw limit(file, row, "columns", record.size().toLong(), limits.maxColumns.toLong())
        }

        var recordLength = 0L
        record.forEachIndexed { index, value ->
            val fieldLength = value.length.toLong()
            val column = headers.getOrNull(index)
            if (fieldLength > limits.maxFieldLength) {
                throw limit(file, row, "field characters", fieldLength, limits.maxFieldLength.toLong(), column = column)
            }
            recordLength += fieldLength
        }
        recordLength += (record.size() - 1).coerceAtLeast(0)
        if (recordLength > limits.maxRecordLength) {
            throw limit(file, row, "record characters", recordLength, limits.maxRecordLength.toLong())
        }
    }

    private fun limit(
        file: String,
        row: Long?,
        name: String,
        actual: Long,
        maximum: Long,
        cause: Throwable? = null,
        column: String? = null,
    ): CsvImportException =
        CsvImportException(
            listOf(
                CsvImportError.LimitExceeded(
                    CsvLocation(file, row, column),
                    name,
                    actual,
                    maximum,
                ),
            ),
            cause,
        )

    private fun malformed(
        file: String,
        error: Throwable,
    ): CsvImportException =
        CsvImportException(
            listOf(
                CsvImportError.MalformedRecord(
                    CsvLocation(file),
                    error.message ?: "malformed CSV record",
                ),
            ),
            error,
        )

    private fun bomAwareReader(input: InputStream): BufferedReader {
        val buffered = if (input.markSupported()) input else BufferedInputStream(input)
        buffered.mark(3)
        val bom = ByteArray(3)
        val read = buffered.read(bom)
        val (charset, skip) =
            when {
                read >= 3 && bom.contentEquals(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())) -> {
                    Charsets.UTF_8 to 3
                }

                read >= 2 && bom[0] == 0xFF.toByte() && bom[1] == 0xFE.toByte() -> {
                    Charsets.UTF_16LE to 2
                }

                read >= 2 && bom[0] == 0xFE.toByte() && bom[1] == 0xFF.toByte() -> {
                    Charsets.UTF_16BE to 2
                }

                else -> {
                    Charsets.UTF_8 to 0
                }
            }
        buffered.reset()
        repeat(skip) { buffered.read() }
        return BufferedReader(InputStreamReader(buffered, charset))
    }
}
