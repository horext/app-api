package io.octatec.horext.api.service

import io.octatec.horext.api.domain.StudyPlan

interface StudyPlanService {
    fun getAllStudyPlan(): List<StudyPlan>

    fun getStudyPlanById(id: Long): StudyPlan

    fun getAllSpecialityId(specialityId: Long): List<StudyPlan>
}
