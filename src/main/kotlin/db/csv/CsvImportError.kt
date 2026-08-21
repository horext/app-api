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
