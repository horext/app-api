package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.StudyPlan
import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.exception.ResourceNotFoundException
import io.octatec.horext.api.service.StudyPlanService
import io.octatec.horext.api.service.SubjectService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("studyPlans")
class StudyPlan(val studyPlanService: StudyPlanService, val subjectService: SubjectService) {

    @GetMapping
    fun getAllStudyPlans(
    ): List<StudyPlan> {
        try {
            return studyPlanService.getAllStudyPlan()
        } catch (e: Exception) {
            e.printStackTrace()
            throw ResourceNotFoundException("StudyPlans")
        }
    }

    @GetMapping(
        "{studyPlanId}"
    )
    fun getAllSpecialitiesByFacultyId(
        @PathVariable(name = "studyPlanId") studyPlanId: Long
    ): List<StudyPlan> {
        try {
            return studyPlanService.getStudyPlanById(studyPlanId)
        } catch (e: Exception) {
            e.printStackTrace()
            throw ResourceNotFoundException("StudyPlan")
        }
    }
    @GetMapping(
        "{studyPlanId}/subjects"
    )
    fun getAllSubjectsByStudyPlanId(
        @PathVariable(name = "studyPlanId") studyPlanId: Long
    ): List<Subject> {
        try {
            return subjectService.getAllByStudyPlanId(studyPlanId)
        } catch (e: Exception) {
            e.printStackTrace()
            throw ResourceNotFoundException("StudyPlan")
        }
    }
}
