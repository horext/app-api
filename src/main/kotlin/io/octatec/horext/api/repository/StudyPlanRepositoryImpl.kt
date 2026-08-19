package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.StudyPlan
import io.octatec.horext.api.repository.table.OrganizationUnits
import io.octatec.horext.api.repository.table.StudyPlans
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.springframework.stereotype.Repository

@Repository
class StudyPlanRepositoryImpl : StudyPlanRepository {
    override fun getAllStudyPlan(): List<StudyPlan> =
        StudyPlans
            .selectAll()
            .orderBy(
                StudyPlans.fromDate to SortOrder.DESC,
            ).map { row -> StudyPlans.createEntity(row) }

    override fun getStudyPlanById(id: Long): StudyPlan? {
        val sp = StudyPlans
        val ou = OrganizationUnits
        return sp
            .leftJoin(ou)
            .select(sp.columns + ou.columns)
            .where { (sp.id eq id) }
            .orderBy(
                sp.fromDate to SortOrder.DESC,
            ).map { row -> sp.createEntity(row) }
            .firstOrNull()
    }

    override fun getAllSpecialityId(specialityId: Long): List<StudyPlan> {
        val sp = StudyPlans
        val ou = OrganizationUnits
        return sp
            .leftJoin(ou)
            .select(sp.columns + ou.columns)
            .where { (sp.organizationUnitId eq specialityId) }
            .orderBy(
                sp.fromDate to SortOrder.DESC,
            ).map { row -> sp.createEntity(row) }
    }
}
