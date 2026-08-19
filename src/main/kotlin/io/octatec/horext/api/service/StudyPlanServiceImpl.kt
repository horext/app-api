package io.octatec.horext.api.service

import io.octatec.horext.api.domain.StudyPlan
import io.octatec.horext.api.exception.ResourceNotFoundException
import io.octatec.horext.api.repository.StudyPlanRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class StudyPlanServiceImpl(
    private val studyPlanRepository: StudyPlanRepository,
) : StudyPlanService {
    override fun getAllStudyPlan(): List<StudyPlan> = studyPlanRepository.getAllStudyPlan()

    override fun getStudyPlanById(id: Long): StudyPlan =
        studyPlanRepository.getStudyPlanById(id) ?: throw ResourceNotFoundException(
            "No se encontró el plan de estudio con id '$id'",
        )

    override fun getAllSpecialityId(specialityId: Long): List<StudyPlan> = studyPlanRepository.getAllSpecialityId(specialityId)
}
