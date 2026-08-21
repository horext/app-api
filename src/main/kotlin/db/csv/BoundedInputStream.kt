package db.csv

import java.io.FilterInputStream
import java.io.InputStream

internal class FileSizeLimitExceededException(
    val actual: Long,
    val maximum: Long,
) : RuntimeException("file bytes $actual exceeds configured limit $maximum")

internal class BoundedInputStream(
    input: InputStream,
    private val maximum: Long,
) : FilterInputStream(input) {
    private var count = 0L

    override fun read(): Int {
        val value = super.read()
        if (value >= 0) increment(1)
        return value
    }

    override fun read(
        buffer: ByteArray,
        offset: Int,
        length: Int,
    ): Int {
        val read = super.read(buffer, offset, length)
        if (read > 0) increment(read.toLong())
        return read
    }

    private fun increment(amount: Long) {
        count += amount
        if (count > maximum) throw FileSizeLimitExceededException(count, maximum)
    }
}
