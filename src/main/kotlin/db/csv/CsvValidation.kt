package db.csv

fun interface CsvFileValidator<T> {
    fun validate(
        file: String,
        rows: List<CsvTypedRecord<T>>,
    ): List<CsvImportError>
}

class CsvFileValidationBuilder<T> internal constructor() {
    private val validators = mutableListOf<CsvFileValidator<T>>()

    fun <K> uniqueBy(
        name: String,
        key: (T) -> K,
    ) {
        validators +=
            CsvFileValidator { file, rows ->
                val seen = mutableMapOf<K, Long>()
                rows.mapNotNull { row ->
                    val previous = seen.putIfAbsent(key(row.value), row.rowNumber)
                    previous?.let {
                        CsvImportError.DuplicateValue(CsvLocation(file, row.rowNumber), name, it)
                    }
                }
            }
    }

    fun <K> maxDistinct(
        name: String,
        maximum: Int,
        key: (T) -> K,
    ) {
        require(maximum > 0)
        validators +=
            CsvFileValidator { file, rows ->
                val actual =
                    rows
                        .asSequence()
                        .map { key(it.value) }
                        .distinct()
                        .take(maximum + 1)
                        .count()
                if (actual > maximum) {
                    listOf(
                        CsvImportError.LimitExceeded(
                            CsvLocation(file),
                            "distinct $name",
                            actual.toLong(),
                            maximum.toLong(),
                        ),
                    )
                } else {
                    emptyList()
                }
            }
    }

    fun <K : Any, V> consistentMapping(
        name: String,
        key: (T) -> K?,
        value: (T) -> V,
    ) {
        validators +=
            CsvFileValidator { file, rows ->
                val seen = mutableMapOf<K, Pair<V, Long>>()
                rows.mapNotNull { row ->
                    val currentKey = key(row.value) ?: return@mapNotNull null
                    val currentValue = value(row.value)
                    val previous = seen.putIfAbsent(currentKey, currentValue to row.rowNumber)
                    if (previous != null && previous.first != currentValue) {
                        CsvImportError.ConflictingMapping(CsvLocation(file, row.rowNumber), name, previous.second)
                    } else {
                        null
                    }
                }
            }
    }

    fun crossField(
        name: String,
        message: String,
        valid: (T) -> Boolean,
    ) {
        validators +=
            CsvFileValidator { file, rows ->
                rows
                    .filterNot { valid(it.value) }
                    .map { row ->
                        CsvImportError.InvalidValue(
                            CsvLocation(file, row.rowNumber),
                            name,
                            message,
                        )
                    }
            }
    }

    internal fun build(): List<CsvFileValidator<T>> = validators.toList()
}
