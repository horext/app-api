package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * The organization unit entity.
 */
interface OrganizationUnit : Entity<OrganizationUnit> {
    companion object : Entity.Factory<OrganizationUnit>()

    /**
     * Organization Unit ID.
     */
    val id: Int


    /**
     * The organization's parent.
     */
    var parentOrganizationUnit: OrganizationUnit?

    /**
     * Organization Unit name.
     */
    var code: String

    /**
     * Organization Unit location.
     */
    var name: String

    /**
     * Organization Unit type
     */
    var type: OrganizationUnitType

}

/**
 * The organization unit table object.
 */
object OrganizationUnits : Table<OrganizationUnit>("organization_unit") {

    /**
     * Organization Unit ID.
     */
    val id = int("id").primaryKey().bindTo { it.id }

    /**
     * Organization Unit name.
     */
    val code = varchar("code").bindTo { it.code }

    /**
     * Organization Unit location.
     */
    val name = varchar("name").bindTo { it.name }

    /**
     * Organization Unit location.
     */
    val typeId = int("organization_unit_type_id").references(OrganizationUnitTypes) { it.type }
}

/**
 * Return a default entity sequence of [OrganizationUnits].
 */
val Database.organizationUnits get() = this.sequenceOf(OrganizationUnits)