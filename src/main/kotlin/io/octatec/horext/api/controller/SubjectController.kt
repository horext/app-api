package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page
import io.octatec.horext.api.service.SubjectService
import io.octatec.horext.api.util.Pagination
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("subjects")
class SubjectController(
    val subjectService: SubjectService,
) {
    @GetMapping(params = ["speciality", "hourlyLoad"])
    fun getAllBySpeciality(
        @RequestParam(name = "speciality") specialityId: Long,
        @RequestParam(name = "hourlyLoad") hourlyLoadId: Long,
    ): List<Subject> = subjectService.getAllBySpecialityId(specialityId, hourlyLoadId)

    @GetMapping(params = ["search", "speciality", "hourlyLoad"])
    @Deprecated("speciality and hourlyLoad params are deprecated")
    fun getAllBySearch(
        @RequestParam(name = "search", required = true) search: String,
        @RequestParam(name = "speciality") specialityId: Long,
        @RequestParam(name = "hourlyLoad") hourlyLoadId: Long,
        @RequestParam(name = "offset", defaultValue = "0") offset: Int,
        @RequestParam(name = "limit", defaultValue = "10") limit: Int,
    ): Page<Subject> {
        Pagination.validatePageNumberAndSize(offset, limit)
        val page =
            subjectService.getPageBySearchAndSpecialityIdAndHourlyLoad(
                search,
                specialityId,
                hourlyLoadId,
                offset,
                limit,
            )
        return page
    }

    @GetMapping(params = ["search", "specialityId", "hourlyLoadId"])
    fun getPageBySpecialityId(
        @RequestParam(name = "search", required = true) search: String,
        @RequestParam(name = "specialityId") specialityId: Long,
        @RequestParam(name = "hourlyLoadId") hourlyLoadId: Long,
        @RequestParam(name = "offset", defaultValue = "0") offset: Int,
        @RequestParam(name = "limit", defaultValue = "10") limit: Int,
    ): Page<Subject> {
        Pagination.validatePageNumberAndSize(offset, limit)
        val page =
            subjectService.getPageBySearchAndSpecialityIdAndHourlyLoad(
                search,
                specialityId,
                hourlyLoadId,
                offset,
                limit,
            )
        return page
    }

    @GetMapping(params = ["search", "studyPlanId", "hourlyLoadId"])
    fun getPageBySearch(
        @RequestParam(name = "search", required = true) search: String,
        @RequestParam(name = "studyPlanId") studyPlanId: Long,
        @RequestParam(name = "hourlyLoad") hourlyLoadId: Long,
        @RequestParam(name = "offset", defaultValue = "0") offset: Int,
        @RequestParam(name = "limit", defaultValue = "10") limit: Int,
    ): Page<Subject> {
        Pagination.validatePageNumberAndSize(offset, limit)
        val page =
            subjectService.getPageBySearchAndStudyPlanIdAndHourlyLoad(
                search,
                studyPlanId,
                hourlyLoadId,
                offset,
                limit,
            )
        return page
    }

    @GetMapping(params = ["hourlyLoadId", "studyPlanId", "cycle"])
    fun getAllByStudyPlanIdAndCycle(
        @RequestParam(name = "hourlyLoadId") hourlyLoadId: Long,
        @RequestParam(name = "studyPlanId") studyPlanId: Long,
        @RequestParam(name = "cycle") cycle: Int,
    ): List<Subject> =
        subjectService.getAllByHourlyLoadIdAndStudyPlanIdAndCycle(
            hourlyLoadId,
            studyPlanId,
            cycle,
        )
}
