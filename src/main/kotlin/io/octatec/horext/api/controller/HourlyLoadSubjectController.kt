package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page
import io.octatec.horext.api.dto.SubjectSearchQuery
import io.octatec.horext.api.service.SubjectService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("hourlyLoads/{hourlyLoadId}")
class HourlyLoadSubjectController(
    private val subjectService: SubjectService,
) {
    @GetMapping("faculties/{facultyId}/subjects")
    fun searchByFaculty(
        @PathVariable hourlyLoadId: Long,
        @PathVariable facultyId: Long,
        @ModelAttribute query: SubjectSearchQuery,
    ): Page<Subject> {
        query.validate()
        return subjectService.getPageBySearchAndFacultyIdAndHourlyLoad(
            query.search,
            facultyId,
            hourlyLoadId,
            query.offset,
            query.limit,
        )
    }

    @GetMapping("specialities/{specialityId}/subjects")
    fun searchBySpeciality(
        @PathVariable hourlyLoadId: Long,
        @PathVariable specialityId: Long,
        @ModelAttribute query: SubjectSearchQuery,
    ): Page<Subject> {
        query.validate()
        return subjectService.getPageBySearchAndSpecialityIdAndHourlyLoad(
            query.search,
            specialityId,
            hourlyLoadId,
            query.offset,
            query.limit,
        )
    }

    @GetMapping("studyPlans/{studyPlanId}/subjects")
    fun searchByStudyPlan(
        @PathVariable hourlyLoadId: Long,
        @PathVariable studyPlanId: Long,
        @ModelAttribute query: SubjectSearchQuery,
    ): Page<Subject> {
        query.validate()
        return subjectService.getPageBySearchAndStudyPlanIdAndHourlyLoad(
            query.search,
            studyPlanId,
            hourlyLoadId,
            query.offset,
            query.limit,
        )
    }

    @GetMapping("studyPlans/{studyPlanId}/cycles/{cycle}/subjects")
    fun getAllByStudyPlanAndCycle(
        @PathVariable hourlyLoadId: Long,
        @PathVariable studyPlanId: Long,
        @PathVariable cycle: Int,
    ): List<Subject> =
        subjectService.getAllByHourlyLoadIdAndStudyPlanIdAndCycle(
            hourlyLoadId,
            studyPlanId,
            cycle,
        )
}
