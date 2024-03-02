package io.octatec.horext.api.service

import io.octatec.horext.api.domain.StudyPlan
import io.octatec.horext.api.domain.StudyPlans
import io.octatec.horext.api.domain.OrganizationUnits
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class StudyPlanServiceImpl() : StudyPlanService {

    override fun getAllStudyPlan(): List<StudyPlan> {
        return StudyPlans.selectAll().map { row -> StudyPlans.createEntity(row) }
    }

    override fun getStudyPlanById(id: Long): List<StudyPlan> {
        val sp = StudyPlans
        val ou = OrganizationUnits
        return sp.select(sp.columns+ou.columns).where { (sp.id eq id) }.map { row -> sp.createEntity(row) }
    }
}
