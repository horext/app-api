package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.exception.ResourceNotFoundException
import io.octatec.horext.api.service.OrganizationUnitService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("specialities")
class SpecialityController(
    val organizationUnitService: OrganizationUnitService,
) {
    @GetMapping
    fun getAllByFacultyId(
        @RequestParam(name = "faculty") id: Long,
    ): List<OrganizationUnit> {
        try {
            return organizationUnitService.getAllSpecialityByFacultyId(id)
        } catch (e: Exception) {
            e.printStackTrace()
            throw ResourceNotFoundException("OrganizationUnit")
        }
    }
}
