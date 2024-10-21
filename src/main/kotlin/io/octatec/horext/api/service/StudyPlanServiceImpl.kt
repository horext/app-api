package io.octatec.horext.api.service

import io.octatec.horext.api.domain.StudyPlan
import io.octatec.horext.api.repository.StudyPlanRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class StudyPlanServiceImpl(private val studyPlanRepository: StudyPlanRepository) : StudyPlanService {

    override fun getAllStudyPlan(): List<StudyPlan> {
        return studyPlanRepository.getAllStudyPlan()
    }

    override fun getStudyPlanById(id: Long): List<StudyPlan> {
        return studyPlanRepository.getStudyPlanById(id)
    }
}
