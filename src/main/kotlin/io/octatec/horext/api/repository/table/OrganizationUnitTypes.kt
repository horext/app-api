package io.octatec.horext.api.repository.table

import io.octatec.horext.api.domain.OrganizationUnitType
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object OrganizationUnitTypes : LongIdTable("organization_unit_type") {
    val name = varchar("name", length = 100)

    fun createEntity(row: ResultRow): OrganizationUnitType =
        OrganizationUnitType(
            row[id].value,
            row[name],
        )
}
