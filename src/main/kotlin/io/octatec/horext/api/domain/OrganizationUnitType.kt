package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow

data class OrganizationUnitType(
    val id: Long,
    val name: String?,
) {
    constructor(id: Long) : this(id, null)
}

object OrganizationUnitTypes : LongIdTable("organization_unit_type") {
    val name = varchar("name", length = 100)

    fun createEntity(row: ResultRow): OrganizationUnitType =
        OrganizationUnitType(
            row[id].value,
            row[name],
        )
}

enum class OrganizationUnitTypeCode(
    val id: Long,
) {
    FACULTY(2L),
    SPECIALITY(3L),
}
