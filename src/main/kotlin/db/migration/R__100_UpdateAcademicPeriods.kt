package db.migration

import io.octatec.horext.api.domain.AcademicPeriodOrganizationUnits
import io.octatec.horext.api.domain.AcademicPeriods
import io.octatec.horext.api.domain.OrganizationUnits
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.zip.CRC32

class R__100_UpdateAcademicPeriods : BaseCsvMigration() {
    companion object {
        private const val COL_CODE = "code"
        private const val COL_FROM_DATE = "from_date"
        private const val COL_TO_DATE = "to_date"
        private const val COL_FACULTY_CODE = "faculty_code"
    }

    data class AcademicPeriodRow(
        val code: String,
        val fromDate: Instant?,
        val toDate: Instant?,
        val facultyCode: String,
    )

    override fun getChecksum(): Int = buildChecksum(prefix = "ap_")

    override fun migrate(context: Context) {
        val entries = listCsvFiles()
        if (entries.isEmpty()) {
            log.info("R__100_UpdateAcademicPeriods: no CSV files found, skipping")
            return
        }
        log.info("R__100_UpdateAcademicPeriods: processing {} file(s)", entries.size)
        val db = Database.connect(SingleConnectionDataSource(context.connection, true))
        transaction(db) {
            entries.forEach { (fileLastModified, rows) ->
                log.info(
                    "R__100_UpdateAcademicPeriods: upserting {} academic period row(s) (fileLastModified={})",
                    rows.size,
                    fileLastModified,
                )
                processRows(fileLastModified, rows)
            }
        }
        log.info("R__100_UpdateAcademicPeriods: done")
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.processRows(
        fileLastModified: Instant?,
        rows: List<AcademicPeriodRow>,
    ) {
        rows.forEach { row ->
            val apId = upsertAcademicPeriod(row, fileLastModified)
            val facultyId =
                OrganizationUnits
                    .selectAll()
                    .where { OrganizationUnits.code eq row.facultyCode }
                    .firstOrNull()
                    ?.get(OrganizationUnits.id)
                    ?.value
                    ?: error("Organization unit not found with code: '${row.facultyCode}'")

            val apouExists =
                AcademicPeriodOrganizationUnits
                    .selectAll()
                    .where {
                        (AcademicPeriodOrganizationUnits.academicPeriodId eq apId) and
                            (AcademicPeriodOrganizationUnits.organizationUnitId eq facultyId)
                    }.any()

            if (!apouExists) {
                AcademicPeriodOrganizationUnits.insertAndGetId {
                    it[AcademicPeriodOrganizationUnits.academicPeriodId] = EntityID(apId, AcademicPeriods)
                    it[AcademicPeriodOrganizationUnits.organizationUnitId] = EntityID(facultyId, OrganizationUnits)
                }
            }
        }
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.upsertAcademicPeriod(
        row: AcademicPeriodRow,
        fileLastModified: Instant?,
    ): Long {
        if (fileLastModified != null) {
            val existing =
                AcademicPeriods
                    .selectAll()
                    .where { AcademicPeriods.code eq row.code }
                    .firstOrNull()
            if (existing != null) {
                val watermark = existing[AcademicPeriods.fromDate]
                if (watermark != null && !fileLastModified.isAfter(watermark)) {
                    return existing[AcademicPeriods.id].value
                }
            }
        }

        return AcademicPeriods
            .upsert(AcademicPeriods.code) {
                it[AcademicPeriods.code] = row.code
                it[AcademicPeriods.name] = row.code
                it[AcademicPeriods.fromDate] = row.fromDate ?: fileLastModified ?: Instant.now()
                it[AcademicPeriods.toDate] = row.toDate
            }.resultedValues!!
            .first()[AcademicPeriods.id]
            .value
    }

    private fun listCsvFiles(): List<Pair<Instant?, List<AcademicPeriodRow>>> =
        listCsvEntries(prefix = "ap_").map { (filename, lastModified) ->
            lastModified to loadCsv("db/data/$filename")
        }

    private fun loadCsv(resourcePath: String): List<AcademicPeriodRow> {
        val stream =
            Thread.currentThread().contextClassLoader.getResourceAsStream(resourcePath)
                ?: return emptyList()
        val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        fun parseInstant(s: String): Instant? =
            s.trim().takeIf { it.isNotBlank() }?.let {
                LocalDateTime.parse(it, fmt).toInstant(ZoneOffset.UTC)
            }
        return stream.bufferedReader().useLines { lines ->
            val iter = lines.filter { it.isNotBlank() }.iterator()
            if (!iter.hasNext()) return@useLines emptyList()
            val headerLine = iter.next()
            val delimiter = if (headerLine.contains(';')) ';' else ','
            val header = parseCsvLine(headerLine, delimiter).map { it.trim().lowercase() }

            fun idx(name: String) =
                header.indexOf(name).also {
                    require(it >= 0) { "Column '$name' not found in CSV header of $resourcePath" }
                }

            fun optIdx(name: String) = header.indexOf(name).takeIf { it >= 0 }
            val iCode = idx(COL_CODE)
            val iFromDate = optIdx(COL_FROM_DATE)
            val iToDate = optIdx(COL_TO_DATE)
            val iFacultyCode = idx(COL_FACULTY_CODE)
            iter
                .asSequence()
                .map { line ->
                    val cols = parseCsvLine(line, delimiter)
                    AcademicPeriodRow(
                        code = cols[iCode].trim(),
                        fromDate = iFromDate?.let { parseInstant(cols[it]) },
                        toDate = iToDate?.let { parseInstant(cols[it]) },
                        facultyCode = cols[iFacultyCode].trim(),
                    )
                }.toList()
        }
    }
}
