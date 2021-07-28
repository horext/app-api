package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface OrganizationUnitType : Entity<OrganizationUnitType> {
    companion object : Entity.Factory<OrganizationUnitType>()

    val id: Int

    var name: String
}

object OrganizationUnitTypes : Table<OrganizationUnitType>("organization_unit_type") {

    val id = int("id").primaryKey().bindTo { it.id }

    val name = varchar("name").bindTo { it.name }
}

val Database.organizationUnitTypes get() = this.sequenceOf(OrganizationUnitTypes)