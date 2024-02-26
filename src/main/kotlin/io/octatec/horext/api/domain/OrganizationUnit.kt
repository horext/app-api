package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar

interface OrganizationUnit : Entity<OrganizationUnit> {
    companion object : Entity.Factory<OrganizationUnit>()

    val id: Long

    var parentOrganizationUnit: OrganizationUnit?

    var code: String

    var name: String

    var type: OrganizationUnitType

}

object OrganizationUnits : Table<OrganizationUnit>("organization_unit") {

    val id = long("id").primaryKey().bindTo { it.id }

    val parentOrganizationId = long("parent_organization_id").bindTo { it.parentOrganizationUnit?.id }

    val code = varchar("code").bindTo { it.code }

    val name = varchar("name").bindTo { it.name }

    val typeId = long("organization_unit_type_id").bindTo{ it.type.id }
}

val Database.organizationUnits get() = this.sequenceOf(OrganizationUnits)