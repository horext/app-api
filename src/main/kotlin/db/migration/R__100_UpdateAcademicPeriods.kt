package db.migration

import db.migration.schema.academicPeriodCsvSchema
import io.octatec.horext.api.repository.table.AcademicPeriodOrganizationUnits
import io.octatec.horext.api.repository.table.AcademicPeriods
import io.octatec.horext.api.repository.table.OrganizationUnits
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import java.time.Instant
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
        if (shouldSkip(context)) {
            log.info("R__100_UpdateAcademicPeriods: skipSeeds is true, skipping migration")
            return
        }
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
            val apId = upsertAcademicPeriod(row)
            val facultyId =
                OrganizationUnits
                    .select(OrganizationUnits.id)
                    .where { OrganizationUnits.code eq row.facultyCode }
                    .limit(1)
                    .firstOrNull()
                    ?.get(OrganizationUnits.id)
                    ?.value
                    ?: error("Organization unit not found with code: '${row.facultyCode}'")

            val apouId =
                AcademicPeriodOrganizationUnits
                    .select(AcademicPeriodOrganizationUnits.id)
                    .where {
                        (AcademicPeriodOrganizationUnits.academicPeriodId eq apId) and
                            (AcademicPeriodOrganizationUnits.organizationUnitId eq facultyId)
                    }.limit(1)
                    .firstOrNull()
                    ?.get(AcademicPeriodOrganizationUnits.id)
                    ?.value

            if (apouId == null) {
                AcademicPeriodOrganizationUnits.insertAndGetId {
                    it[AcademicPeriodOrganizationUnits.academicPeriodId] = EntityID(apId, AcademicPeriods)
                    it[AcademicPeriodOrganizationUnits.organizationUnitId] = EntityID(facultyId, OrganizationUnits)
                    it[AcademicPeriodOrganizationUnits.fromDate] = row.fromDate ?: fileLastModified ?: Instant.now()
                    it[AcademicPeriodOrganizationUnits.toDate] = row.toDate
                }
            } else {
                AcademicPeriodOrganizationUnits.update({ AcademicPeriodOrganizationUnits.id eq apouId }) {
                    it[AcademicPeriodOrganizationUnits.fromDate] = row.fromDate ?: fileLastModified ?: Instant.now()
                    it[AcademicPeriodOrganizationUnits.toDate] = row.toDate
                }
            }
        }
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.upsertAcademicPeriod(row: AcademicPeriodRow): Long =
        AcademicPeriods
            .upsert(AcademicPeriods.code) {
                it[AcademicPeriods.code] = row.code
                it[AcademicPeriods.name] = row.code
            }.resultedValues!!
            .first()[AcademicPeriods.id]
            .value

    private fun listCsvFiles(): List<Pair<Instant?, List<AcademicPeriodRow>>> =
        listCsvEntries(prefix = "ap_").map { (filename, lastModified) ->
            lastModified to loadCsv("db/data/$filename")
        }

    private fun loadCsv(resourcePath: String): List<AcademicPeriodRow> {
        val source = openClasspathResource(resourcePath) ?: return emptyList()
        return source.use {
            academicPeriodCsvSchema().parse(resourcePath, it).rows
        }
    }
}
