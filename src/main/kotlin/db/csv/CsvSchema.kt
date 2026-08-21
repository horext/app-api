package db.csv

import java.io.InputStream
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.util.Locale

data class CsvImport<T>(
    val file: String,
    val records: List<CsvTypedRecord<T>>,
) {
    val rows: List<T> = records.map { it.value }
}

data class CsvTypedRecord<T>(
    val rowNumber: Long,
    val value: T,
)

class CsvColumn<T> internal constructor(
    internal val index: Int,
)

class CsvRowScope internal constructor(
    private val values: List<Any?>,
) {
    @Suppress("UNCHECKED_CAST")
    operator fun <T> CsvColumn<T>.invoke(): T = values[index] as T
}

class CsvSchema<T> internal constructor(
    val name: String,
    val limits: CsvLimits,
    private val delimiter: Char,
    private val columns: List<ColumnDefinition<*>>,
    private val mapper: CsvRowScope.() -> T,
    private val validators: List<CsvFileValidator<T>>,
) {
    fun parse(
        file: String,
        input: InputStream,
    ): CsvImport<T> {
        val errors = mutableListOf<CsvImportError>()
        val records =
            CsvSource(
                delimiter = delimiter,
                limits = limits,
            ).read(file, input, columns.filter { it.requiredHeader }.map { it.header }.toSet()) { context ->
                val outcome = decode(context)
                errors += outcome.errors
                if (errors.size >= limits.maxErrors) {
                    throw CsvImportException(errors.take(limits.maxErrors))
                }
                outcome.record
            }.filterNotNull()
        validators.forEach { validator -> errors += validator.validate(file, records) }
        if (errors.isNotEmpty()) throw CsvImportException(errors.take(limits.maxErrors))
        return CsvImport(file, records)
    }

    private fun decode(context: CsvRecordContext): DecodeOutcome<T> {
        val errors = mutableListOf<CsvImportError>()
        val values =
            columns.map { column ->
                val raw = if (context.record.isMapped(column.header)) context.record.get(column.header) else ""
                column
                    .decode(raw, CsvLocation(context.file, context.rowNumber, column.header))
                    .fold(
                        onSuccess = { it },
                        onFailure = {
                            errors += it.asCsvError(context.file, context.rowNumber, column.header, raw)
                            null
                        },
                    )
            }
        if (errors.isNotEmpty()) return DecodeOutcome(null, errors)

        return try {
            DecodeOutcome(CsvTypedRecord(context.rowNumber, CsvRowScope(values).mapper()), emptyList())
        } catch (error: CsvValueException) {
            DecodeOutcome(null, listOf(error.error))
        }
    }

    private data class DecodeOutcome<T>(
        val record: CsvTypedRecord<T>?,
        val errors: List<CsvImportError>,
    )
}

fun <T> csvSchema(
    name: String,
    block: CsvSchemaBuilder<T>.() -> Unit,
): CsvSchema<T> = CsvSchemaBuilder<T>(name).apply(block).build()

class CsvSchemaBuilder<T> internal constructor(
    private val name: String,
) {
    private var limits = CsvLimits.MIGRATION_DEFAULTS
    private var delimiter = ','
    private val columns = mutableListOf<ColumnDefinition<*>>()
    private val validators = mutableListOf<CsvFileValidator<T>>()
    private var mapper: (CsvRowScope.() -> T)? = null

    fun limits(value: CsvLimits) {
        limits = value
    }

    fun delimiter(value: Char) {
        delimiter = value
    }

    fun requiredString(
        header: String,
        block: StringColumnBuilder.() -> Unit = {},
    ): CsvColumn<String> {
        val definition = StringColumnBuilder(header, required = true, requiredHeader = true).apply(block).build()
        return add(
            object : ColumnDefinition<String>(header, requiredHeader = true) {
                override fun decode(
                    raw: String,
                    location: CsvLocation,
                ): Result<String> = definition.decode(raw, location).map { requireNotNull(it) }
            },
        )
    }

    fun optionalString(
        header: String,
        requiredHeader: Boolean = false,
        block: StringColumnBuilder.() -> Unit = {},
    ): CsvColumn<String?> = add(StringColumnBuilder(header, required = false, requiredHeader = requiredHeader).apply(block).build())

    fun intColumn(
        header: String,
        block: IntColumnBuilder.() -> Unit = {},
    ): CsvColumn<Int> = add(IntColumnBuilder(header).apply(block).build())

    fun timeColumn(
        header: String,
        block: TimeColumnBuilder.() -> Unit = {},
    ): CsvColumn<LocalTime?> = add(TimeColumnBuilder(header).apply(block).build())

    fun dateTimeColumn(
        header: String,
        block: DateTimeColumnBuilder.() -> Unit = {},
    ): CsvColumn<LocalDateTime?> = add(DateTimeColumnBuilder(header).apply(block).build())

    fun instantColumn(header: String): CsvColumn<Instant?> = add(InstantColumnDefinition(header))

    fun mapRow(block: CsvRowScope.() -> T) {
        check(mapper == null) { "mapRow may only be declared once" }
        mapper = block
    }

    fun validateFile(block: CsvFileValidationBuilder<T>.() -> Unit) {
        validators += CsvFileValidationBuilder<T>().apply(block).build()
    }

    internal fun build(): CsvSchema<T> {
        require(name.isNotBlank()) { "schema name must not be blank" }
        require(columns.isNotEmpty()) { "schema must declare at least one column" }
        val duplicates =
            columns
                .groupingBy { it.header }
                .eachCount()
                .filterValues { it > 1 }
                .keys
        require(duplicates.isEmpty()) { "duplicate schema columns: ${duplicates.sorted().joinToString()}" }
        val rowMapper = requireNotNull(mapper) { "schema must declare mapRow" }
        return CsvSchema(
            name,
            limits,
            delimiter,
            columns.toList(),
            rowMapper,
            validators.toList(),
        )
    }

    private fun <V> add(definition: ColumnDefinition<V>): CsvColumn<V> {
        columns += definition
        return CsvColumn(columns.lastIndex)
    }
}

internal fun Throwable.asCsvError(
    file: String,
    row: Long,
    column: String,
    raw: String,
): CsvImportError =
    when (this) {
        is CsvColumnException -> error
        else -> CsvImportError.InvalidValue(CsvLocation(file, row, column), raw, message ?: "invalid value")
    }

class CsvValueException(
    val error: CsvImportError,
) : RuntimeException(error.message)

internal class CsvColumnException(
    val error: CsvImportError,
) : RuntimeException(error.message)

internal abstract class ColumnDefinition<T>(
    val header: String,
    val requiredHeader: Boolean,
) {
    abstract fun decode(
        raw: String,
        location: CsvLocation,
    ): Result<T>
}

class StringColumnBuilder internal constructor(
    private val header: String,
    private val required: Boolean,
    private val requiredHeader: Boolean,
) {
    private var trim = false
    private var uppercase = false
    private var maximumLength: Int? = null
    private var rejectControls = false
    private var default: String? = null
    private val normalizers = mutableListOf<(String) -> String>()

    fun trim() {
        trim = true
    }

    fun uppercase() {
        uppercase = true
    }

    fun maxLength(value: Int) {
        require(value > 0)
        maximumLength = value
    }

    fun rejectControlCharacters() {
        rejectControls = true
    }

    fun default(value: String) {
        check(!required) { "required columns cannot have a default" }
        default = value
    }

    fun normalize(transform: (String) -> String) {
        normalizers += transform
    }

    internal fun build(): ColumnDefinition<String?> {
        val transforms = normalizers.toList()
        return object : ColumnDefinition<String?>(header, requiredHeader) {
            override fun decode(
                raw: String,
                location: CsvLocation,
            ): Result<String?> =
                runCatching {
                    var value = raw
                    if (trim) value = value.trim()
                    if (uppercase) value = value.uppercase(Locale.ROOT)
                    transforms.forEach { value = it(value) }
                    if (value.isEmpty()) {
                        if (required) throw CsvColumnException(CsvImportError.MissingValue(location))
                        return@runCatching default
                    }
                    maximumLength?.let { maximum ->
                        if (value.length > maximum) {
                            throw CsvColumnException(CsvImportError.ValueTooLong(location, value.length, maximum))
                        }
                    }
                    if (rejectControls && value.any { it == '\u0000' || it.isISOControl() }) {
                        throw CsvColumnException(
                            CsvImportError.InvalidValue(location, value, "control characters are not allowed"),
                        )
                    }
                    value
                }
        }
    }
}

class IntColumnBuilder internal constructor(
    private val header: String,
) {
    private var default = 0
    private var range: IntRange? = null

    fun default(value: Int) {
        default = value
    }

    fun range(value: IntRange) {
        range = value
    }

    internal fun build(): ColumnDefinition<Int> =
        object : ColumnDefinition<Int>(header, requiredHeader = false) {
            override fun decode(
                raw: String,
                location: CsvLocation,
            ): Result<Int> =
                runCatching {
                    val trimmed = raw.trim()
                    val value =
                        if (trimmed.isEmpty()) {
                            default
                        } else {
                            trimmed.toIntOrNull()
                                ?: throw CsvColumnException(CsvImportError.InvalidValue(location, raw, "expected an integer"))
                        }
                    range?.let { allowed ->
                        if (value !in allowed) {
                            throw CsvColumnException(
                                CsvImportError.InvalidValue(location, raw, "expected a value in ${allowed.first}..${allowed.last}"),
                            )
                        }
                    }
                    value
                }
        }
}

class TimeColumnBuilder internal constructor(
    private val header: String,
) {
    private var patterns = listOf("HH:mm")

    fun formats(vararg values: String) {
        require(values.isNotEmpty())
        patterns = values.toList()
    }

    internal fun build(): ColumnDefinition<LocalTime?> {
        val formatters = patterns.map(DateTimeFormatter::ofPattern)
        return object : ColumnDefinition<LocalTime?>(header, requiredHeader = true) {
            override fun decode(
                raw: String,
                location: CsvLocation,
            ): Result<LocalTime?> =
                runCatching {
                    val value = raw.trim()
                    if (value.isEmpty()) return@runCatching null
                    formatters.firstNotNullOfOrNull { formatter ->
                        try {
                            LocalTime.parse(value, formatter)
                        } catch (_: DateTimeParseException) {
                            null
                        }
                    } ?: throw CsvColumnException(
                        CsvImportError.InvalidValue(location, raw, "expected one of: ${patterns.joinToString()}"),
                    )
                }
        }
    }
}

class DateTimeColumnBuilder internal constructor(
    private val header: String,
) {
    private var patterns = listOf("yyyy-MM-dd HH:mm:ss")

    fun formats(vararg values: String) {
        require(values.isNotEmpty())
        patterns = values.toList()
    }

    internal fun build(): ColumnDefinition<LocalDateTime?> {
        val formatters = patterns.map(DateTimeFormatter::ofPattern)
        return object : ColumnDefinition<LocalDateTime?>(header, requiredHeader = false) {
            override fun decode(
                raw: String,
                location: CsvLocation,
            ): Result<LocalDateTime?> =
                runCatching {
                    val value = raw.trim()
                    if (value.isEmpty()) return@runCatching null
                    formatters.firstNotNullOfOrNull { formatter ->
                        try {
                            LocalDateTime.parse(value, formatter)
                        } catch (_: DateTimeParseException) {
                            null
                        }
                    } ?: throw CsvColumnException(
                        CsvImportError.InvalidValue(location, raw, "expected one of: ${patterns.joinToString()}"),
                    )
                }
        }
    }
}

private class InstantColumnDefinition(
    header: String,
) : ColumnDefinition<Instant?>(header, requiredHeader = false) {
    private val formatter =
        DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .optionalStart()
            .appendOffsetId()
            .optionalEnd()
            .toFormatter()

    override fun decode(
        raw: String,
        location: CsvLocation,
    ): Result<Instant?> =
        runCatching {
            val value = raw.trim()
            if (value.isEmpty()) return@runCatching null
            val temporal = formatter.parseBest(value, OffsetDateTime::from, LocalDateTime::from)
            when (temporal) {
                is OffsetDateTime -> temporal.toInstant()
                is LocalDateTime -> temporal.toInstant(ZoneOffset.UTC)
                else -> throw DateTimeParseException("unsupported timestamp", value, 0)
            }
        }.recoverCatching { error ->
            if (error is CsvColumnException) throw error
            throw CsvColumnException(
                CsvImportError.InvalidValue(location, raw, "expected yyyy-MM-dd HH:mm:ss with optional offset"),
            )
        }
}
