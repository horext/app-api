package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.OrganizationUnits
import io.octatec.horext.api.domain.StudyPlan
import io.octatec.horext.api.domain.StudyPlans
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.springframework.stereotype.Repository

@Repository
class StudyPlanRepositoryImpl : StudyPlanRepository {
    override fun getAllStudyPlan(): List<StudyPlan> = StudyPlans.selectAll().map { row -> StudyPlans.createEntity(row) }

    override fun getStudyPlanById(id: Long): List<StudyPlan> {
        val sp = StudyPlans
        val ou = OrganizationUnits
        return sp
            .leftJoin(ou)
            .select(sp.columns + ou.columns)
            .where { (sp.id eq id) }
            .map { row -> sp.createEntity(row) }
    }
}
