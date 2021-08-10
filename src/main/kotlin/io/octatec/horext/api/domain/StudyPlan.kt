package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.Instant

interface StudyPlan : Entity<StudyPlan> {
    companion object : Entity.Factory<StudyPlan>()

    val id: Long

    var fromDate: Instant

    var toDate: Instant

    var organizationUnit: OrganizationUnit
}

open class StudyPlans(alias: String?)  : Table<StudyPlan>("study_plan", alias) {
    companion object : StudyPlans(null)
    override fun aliased(alias: String) = StudyPlans(alias)

    val id = long("id").primaryKey().bindTo { it.id }

    val fromDate = timestamp("from_date").bindTo { it.fromDate }

    val toDate = timestamp("to_date").bindTo { it.toDate }

    val organizationUnitId = long("organization_unit_id").references(OrganizationUnits) { it.organizationUnit }
}
