package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow

data class OrganizationUnit(
    var id: Long,
    var parentOrganizationUnit: OrganizationUnit?,
    var code: String?,
    var name: String?,
    var type: OrganizationUnitType?,
) {
    constructor(id: Long) : this(id, null, null, null, null)
    constructor(id: Long, name: String) : this(id, null, null, name, null)
}

object OrganizationUnits : LongIdTable("organization_unit") {
    val parentOrganizationId = long("parent_organization_id")

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
                OrganizationUnit(
                    id = row[parentOrganizationId],
                ),
        )
}
