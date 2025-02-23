package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.StudyPlan

interface StudyPlanRepository {
    fun getAllStudyPlan(): List<StudyPlan>

    fun getStudyPlanById(id: Long): List<StudyPlan>
}
