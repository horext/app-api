package io.octatec.horext.api.repository.table

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.domain.OrganizationUnitType
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object OrganizationUnits : LongIdTable("organization_unit") {
    val parentOrganizationId = long("parent_organization_id").nullable()

    val code = varchar("code", length = 50)

    val name = varchar("name", length = 50)

    val typeId = reference("organization_unit_type_id", OrganizationUnitTypes)

    fun createEntity(row: ResultRow): OrganizationUnit =
        OrganizationUnit(
            id = row[id].value,
            code = row[code],
            name = row[name],
            type =
                runCatching {
                    OrganizationUnitTypes.createEntity(row)
                }.getOrElse { OrganizationUnitType(id = row[typeId].value) },
            parentOrganizationUnit =
                row[parentOrganizationId]?.let {
                    OrganizationUnit(
                        id = it,
                    )
                },
        )
}
