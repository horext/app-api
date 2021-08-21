package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.Instant

interface AcademicPeriodOrganizationUnit : Entity<AcademicPeriodOrganizationUnit> {
    companion object : Entity.Factory<AcademicPeriodOrganizationUnit>()

    val id: Long

    var fromDate: Instant

    var toDate: Instant

    var academicPeriod: AcademicPeriod

    var organizationUnit: OrganizationUnit
}

open class AcademicPeriodOrganizationUnits(alias: String?)  : Table<AcademicPeriodOrganizationUnit>("academic_period_organization_unit", alias) {
    companion object : AcademicPeriodOrganizationUnits(null)
    override fun aliased(alias: String) = AcademicPeriodOrganizationUnits(alias)

    val id = long("id").primaryKey().bindTo { it.id }

    val fromDate = timestamp("from_date").bindTo { it.fromDate }

    val toDate = timestamp("to_date").bindTo { it.toDate }

    val academicPeriodId = long("academic_period_id").references(AcademicPeriods){it.academicPeriod}

    val organizationUnitId = long("organization_unit_id").references(OrganizationUnits) { it.organizationUnit }
}
