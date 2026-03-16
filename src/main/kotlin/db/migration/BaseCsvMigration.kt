package db.migration

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URI
import java.time.Instant
import java.util.jar.JarFile
import java.util.zip.CRC32

abstract class BaseCsvMigration : BaseJavaMigration() {
    protected val log = LoggerFactory.getLogger(javaClass)

    protected fun parseCsvLine(
        line: String,
        delimiter: Char = ',',
    ): List<String> {
        val result = mutableListOf<String>()
        var inQuotes = false
        val current = StringBuilder()
        for (ch in line) {
            when {
                ch == '"' -> {
                    inQuotes = !inQuotes
                }

                ch == delimiter && !inQuotes -> {
                    result.add(current.toString())
                    current.clear()
                }

                else -> {
                    current.append(ch)
                }
            }
        }
        result.add(current.toString())
        return result
    }

    protected fun csvResourcePaths(prefix: String): List<String> {
        val url = Thread.currentThread().contextClassLoader.getResource("db/data")
        if (url == null) {
            log.warn("Resource directory 'db/data' not found on classpath — no CSV files will be loaded")
            return emptyList()
        }
        return when (url.protocol) {
            "file" -> {
                File(url.toURI())
                    .listFiles { f -> f.name.startsWith(prefix) && f.extension == "csv" }
                    ?.sortedBy { it.name }
                    ?.map { "db/data/${it.name}" }
                    ?: emptyList()
            }

            "jar" -> {
                val jarPath = url.path.substringBefore("!/")
                JarFile(File(URI(jarPath))).use { jar ->
                    jar
                        .entries()
                        .asSequence()
                        .filter { e -> e.name.startsWith("db/data/$prefix") && e.name.endsWith(".csv") }
                        .map { it.name }
                        .sorted()
                        .toList()
                }
            }

            else -> {
                emptyList()
            }
        }
    }

    protected fun listCsvEntries(prefix: String): List<Pair<String, Instant?>> {
        val url = Thread.currentThread().contextClassLoader.getResource("db/data")
        if (url == null) {
            log.warn("Resource directory 'db/data' not found on classpath — no CSV files will be loaded")
            return emptyList()
        }
        return when (url.protocol) {
            "file" -> {
                File(url.toURI())
                    .listFiles { f -> f.name.startsWith(prefix) && f.extension == "csv" }
                    ?.map { f ->
                        f.name to f.lastModified().takeIf { it > 0L }?.let { Instant.ofEpochMilli(it) }
                    }
                    ?: emptyList()
            }

            "jar" -> {
                val jarPath = url.path.substringBefore("!/")
                JarFile(File(URI(jarPath))).use { jar ->
                    jar
                        .entries()
                        .asSequence()
                        .filter { e -> e.name.startsWith("db/data/$prefix") && e.name.endsWith(".csv") }
                        .map { entry ->
                            entry.name.removePrefix("db/data/") to
                                entry.time.takeIf { it >= 0L }?.let { Instant.ofEpochMilli(it) }
                        }.toList()
                }
            }

            else -> {
                emptyList()
            }
        }
    }

    protected fun buildChecksum(prefix: String): Int {
        val crc = CRC32()
        csvResourcePaths(prefix).forEach { path ->
            val stream = Thread.currentThread().contextClassLoader.getResourceAsStream(path)
            if (stream == null) {
                log.warn("Could not open resource '{}' for checksum — file may be missing from classpath", path)
            } else {
                stream.use { crc.update(it.readBytes()) }
            }
        }
        return crc.value.toInt()
    }
}
