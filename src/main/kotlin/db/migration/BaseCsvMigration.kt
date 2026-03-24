package db.migration

import io.octatec.horext.api.domain.Contributions
import org.flywaydb.core.api.configuration.Configuration
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.v1.jdbc.upsert
import org.slf4j.LoggerFactory
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URI
import java.net.URL
import java.nio.charset.Charset
import java.time.Instant
import java.util.concurrent.TimeUnit
import java.util.jar.JarFile
import java.util.zip.CRC32

abstract class BaseCsvMigration : BaseJavaMigration() {
    protected val log = LoggerFactory.getLogger(javaClass)

    protected fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.getOrCreateContributionId(path: String): Long? {
        val author = gitLastAuthor(path) ?: return null
        val (name, email) =
            if (author.contains("<")) {
                author.substringBefore("<").trim() to author.substringAfter("<").substringBefore(">").trim()
            } else {
                author.trim() to null
            }

        return Contributions.upsert(
            Contributions.authorName,
            Contributions.authorEmail,
            onUpdate = { it[Contributions.committedAt] = Instant.now() },
        ) {
            it[Contributions.authorName] = name
            it[Contributions.authorEmail] = email
            it[Contributions.committedAt] = Instant.now()
        }.get(Contributions.id).value
    }

    private fun resourceClassLoaders(): List<ClassLoader> =
        listOfNotNull(
            Thread.currentThread().contextClassLoader,
            javaClass.classLoader,
            ClassLoader.getSystemClassLoader(),
        ).distinct()

    private fun findResource(path: String): URL? =
        resourceClassLoaders()
            .asSequence()
            .mapNotNull { it.getResource(path) }
            .firstOrNull()

    private fun openResource(path: String): InputStream? =
        resourceClassLoaders()
            .asSequence()
            .mapNotNull { it.getResourceAsStream(path) }
            .firstOrNull()

    protected fun openClasspathResource(path: String): InputStream? = openResource(path)

    protected fun bomAwareReader(stream: InputStream): BufferedReader {
        val buf = if (stream.markSupported()) stream else BufferedInputStream(stream)
        buf.mark(4)
        val bom = ByteArray(3)
        val read = buf.read(bom)
        val (charset, skip) =
            when {
                read >= 3 && bom[0] == 0xEF.toByte() && bom[1] == 0xBB.toByte() && bom[2] == 0xBF.toByte() -> {
                    Charsets.UTF_8 to 3
                }

                // UTF-8 BOM: EF BB BF
                read >= 2 && bom[0] == 0xFF.toByte() && bom[1] == 0xFE.toByte() -> {
                    Charsets.UTF_16LE to 2
                }

                // UTF-16 LE BOM: FF FE
                read >= 2 && bom[0] == 0xFE.toByte() && bom[1] == 0xFF.toByte() -> {
                    Charsets.UTF_16BE to 2
                }

                // UTF-16 BE BOM: FE FF
                else -> {
                    Charsets.UTF_8 to 0
                } // no BOM — assume UTF-8
            }
        buf.reset()
        buf.skip(skip.toLong())
        return BufferedReader(InputStreamReader(buf, charset))
    }

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
        val url = findResource("db/data")
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
        val url = findResource("db/data")
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
            val stream = openResource(path)
            if (stream == null) {
                log.warn("Could not open resource '{}' for checksum — file may be missing from classpath", path)
            } else {
                stream.use { crc.update(it.readBytes()) }
            }
        }
        return crc.value.toInt()
    }

    protected fun shouldSkip(
        context: Context,
        placeholder: String = "skipSeeds",
    ): Boolean {
        val value = context.configuration.placeholders[placeholder]
        return value?.toBoolean() ?: false
    }

    protected fun gitLastAuthor(path: String): String? {
        val url = findResource(path) ?: return null
        if (url.protocol != "file") return null

        return try {
            val file = File(url.toURI())
            val process =
                ProcessBuilder("git", "log", "-n", "1", "--format=%an <%ae>", "--", file.absolutePath)
                    .directory(file.parentFile)
                    .start()

            val author =
                process.inputStream.bufferedReader().use { it.readLine() }
            process.waitFor(5, TimeUnit.SECONDS)

            if (process.exitValue() == 0 && !author.isNullOrBlank()) {
                author.trim()
            } else {
                null
            }
        } catch (e: Exception) {
            log.trace("Failed to get git author for {}: {}", path, e.message)
            null
        }
    }
}
