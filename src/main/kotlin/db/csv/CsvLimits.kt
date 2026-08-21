package db.csv

data class CsvLimits(
    val maxFileBytes: Long = 20L * 1024 * 1024,
    val maxRows: Long = 50_000,
    val maxColumns: Int = 50,
    val maxRecordLength: Int = 16_384,
    val maxFieldLength: Int = 1_000,
) {
    init {
        require(maxFileBytes > 0) { "maxFileBytes must be positive" }
        require(maxRows > 0) { "maxRows must be positive" }
        require(maxColumns > 0) { "maxColumns must be positive" }
        require(maxRecordLength > 0) { "maxRecordLength must be positive" }
        require(maxFieldLength > 0) { "maxFieldLength must be positive" }
    }

    companion object {
        val MIGRATION_DEFAULTS = CsvLimits()
    }
}
