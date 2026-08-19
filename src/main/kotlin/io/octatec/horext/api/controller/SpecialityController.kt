package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.domain.OrganizationUnitTypeCode
import io.octatec.horext.api.domain.StudyPlan
import io.octatec.horext.api.service.OrganizationUnitService
import io.octatec.horext.api.service.StudyPlanService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("specialities")
class SpecialityController(
    val organizationUnitService: OrganizationUnitService,
    val studyPlanService: StudyPlanService,
) {
    @GetMapping
    fun getAllByFacultyId(
        @RequestParam(name = "faculty") id: Long,
    ): List<OrganizationUnit> = organizationUnitService.getAllSpecialityByFacultyId(id)

    @GetMapping("{specialityId}")
    fun getById(
        @PathVariable(name = "specialityId") specialityId: Long,
    ): OrganizationUnit = organizationUnitService.getById(specialityId, OrganizationUnitTypeCode.SPECIALITY)

    @GetMapping("{specialityId}/studyPlans")
    fun getAllBySpecialityId(
        @RequestParam(name = "specialityId") specialityId: Long,
    ): List<StudyPlan> = studyPlanService.getAllSpecialityId(specialityId)
}
