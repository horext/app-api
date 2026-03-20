package db.migration

import io.octatec.horext.api.domain.OrganizationUnitTypes
import io.octatec.horext.api.domain.OrganizationUnits
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import java.util.zip.CRC32

@Suppress("ClassName")
class R__010_SeedOrganizations : BaseCsvMigration() {
    companion object {
        private const val DATA_DIR = "db/data"
        private const val UNITS_FILE = "$DATA_DIR/organization_units.csv"
    }

    override fun getChecksum(): Int {
        val crc = CRC32()
        openClasspathResource(UNITS_FILE)?.use { crc.update(it.readBytes()) }
        return crc.value.toInt()
    }

    override fun migrate(context: Context) {
        if (shouldSkip(context)) {
            log.info("R__010_SeedOrganizations: skipSeeds is true, skipping migration")
            return
        }
        val db = Database.connect(SingleConnectionDataSource(context.connection, true))
        transaction(db) { seedUnits() }
        log.info("R__010_SeedOrganizations: completed")
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.seedUnits() {
        val stream = openClasspathResource(UNITS_FILE)
        if (stream == null) {
            log.info("R__010_SeedOrganizations: file $UNITS_FILE not found, skipping")
            return
        }
        log.info("R__010_SeedOrganizations: seeding $UNITS_FILE")

        val lines =
            bomAwareReader(stream).useLines { seq ->
                seq
                    .drop(1)
                    .filter { it.isNotBlank() }
                    .map { parseCsvLine(it) }
                    .filter { it.size >= 4 }
                    .toList()
            }

        val typeIdByName =
            OrganizationUnitTypes
                .selectAll()
                .associate { it[OrganizationUnitTypes.name] to it[OrganizationUnitTypes.id].value }

        for (c in lines) {
            val typeId = typeIdByName[c[2]] ?: error("Organization unit type not found: '${c[2]}'")
            OrganizationUnits.upsert(OrganizationUnits.code) {
                it[OrganizationUnits.code] = c[0]
                it[OrganizationUnits.name] = c[1]
                it[OrganizationUnits.typeId] = EntityID(typeId, OrganizationUnitTypes)
            }
        }

        val codeToId =
            OrganizationUnits
                .selectAll()
                .associate { it[OrganizationUnits.code] to it[OrganizationUnits.id].value }

        for (c in lines) {
            val parentCode = c[3]
            if (parentCode.isNotEmpty()) {
                val parentId = codeToId[parentCode] ?: error("Parent org unit not found: '$parentCode'")
                OrganizationUnits.update({ OrganizationUnits.code eq c[0] }) {
                    it[OrganizationUnits.parentOrganizationId] = parentId
                }
            }
        }
    }
}
