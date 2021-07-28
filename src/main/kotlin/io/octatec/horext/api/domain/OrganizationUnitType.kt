package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * The department entity.
 */
interface OrganizationUnitType : Entity<OrganizationUnitType> {
    companion object : Entity.Factory<OrganizationUnitType>()

    /**
     * Organization Unit Type ID.
     */
    val id: Int


    /**
     * Organization Unit Type name.
     */
    var name: String
}

/**
 * The department table object.
 */
object OrganizationUnitTypes : Table<OrganizationUnitType>("organization_unit_type") {

    /**
     * Organization Unit Type ID.
     */
    val id = int("id").primaryKey().bindTo { it.id }

    /**
     * Organization Unit Type name.
     */
    val name = varchar("name").bindTo { it.name }
}

/**
 * Return a default entity sequence of [organizationUnitTypes].
 */
val Database.organizationUnitTypes get() = this.sequenceOf(OrganizationUnitTypes)