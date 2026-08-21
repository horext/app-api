package db.csv

data class CsvLocation(
    val file: String,
    val row: Long? = null,
    val column: String? = null,
)

sealed interface CsvImportError {
    val location: CsvLocation
    val message: String

    data class MissingColumn(
        override val location: CsvLocation,
        val expected: String,
    ) : CsvImportError {
        override val message = "missing required column \"$expected\""
    }

    data class DuplicateColumn(
        override val location: CsvLocation,
        val header: String,
    ) : CsvImportError {
        override val message = "duplicate column \"$header\""
    }

    data class MissingValue(
        override val location: CsvLocation,
    ) : CsvImportError {
        override val message = "required value is blank"
    }

    data class InvalidValue(
        override val location: CsvLocation,
        val value: String,
        val reason: String,
    ) : CsvImportError {
        override val message = "invalid value ${safeValue(value)}: $reason"
    }

    data class ValueTooLong(
        override val location: CsvLocation,
        val actual: Int,
        val maximum: Int,
    ) : CsvImportError {
        override val message = "value length $actual exceeds maximum $maximum"
    }

    data class DuplicateValue(
        override val location: CsvLocation,
        val rule: String,
        val firstRow: Long,
    ) : CsvImportError {
        override val message = "duplicate value for $rule; first seen at row $firstRow"
    }

    data class ConflictingMapping(
        override val location: CsvLocation,
        val rule: String,
        val firstRow: Long,
    ) : CsvImportError {
        override val message = "conflicting mapping for $rule; first seen at row $firstRow"
    }

    data class LimitExceeded(
        override val location: CsvLocation,
        val limit: String,
        val actual: Long,
        val maximum: Long,
    ) : CsvImportError {
        override val message = "$limit $actual exceeds configured limit $maximum"
    }

    data class MalformedRecord(
        override val location: CsvLocation,
        override val message: String,
    ) : CsvImportError
}

class CsvImportException(
    val errors: List<CsvImportError>,
    cause: Throwable? = null,
) : RuntimeException(errors.joinToString("\n\n", transform = ::renderCsvError), cause) {
    init {
        require(errors.isNotEmpty()) { "CsvImportException requires at least one error" }
    }
}

fun renderCsvError(error: CsvImportError): String =
    buildString {
        append(error.location.file)
        error.location.row?.let { append("\nrow ").append(it) }
        error.location.column?.let { append(", column \"").append(it).append('"') }
        append(":\n  ").append(error.message)
    }

private fun safeValue(value: String): String {
    val rendered =
        value
            .take(100)
            .flatMap { character ->
                when (character) {
                    '\n' -> "\\n".toList()
                    '\r' -> "\\r".toList()
                    '\t' -> "\\t".toList()
                    else -> if (character.isISOControl()) "\\u%04x".format(character.code).toList() else listOf(character)
                }
            }.joinToString("")
    return "\"$rendered${if (value.length > 100) "…" else ""}\""
}
