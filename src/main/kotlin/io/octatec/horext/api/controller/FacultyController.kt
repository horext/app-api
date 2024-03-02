package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.exception.ResourceNotFoundException
import io.octatec.horext.api.service.OrganizationUnitService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("faculties")
class FacultyController(val organizationUnitService: OrganizationUnitService) {

    @GetMapping
    fun getAll(): List<OrganizationUnit> {
        return organizationUnitService.getAllFaculty()
    }

    @GetMapping(
        "{facultyId}/specialities"
    )
    fun getAllSpecialitiesByFacultyId(
        @PathVariable(name = "facultyId") facultyId: Long
    ): List<OrganizationUnit> {
        try {
            return organizationUnitService.getAllSpecialityByFacultyId(facultyId)
        } catch (e: Exception) {
            e.printStackTrace()
            throw ResourceNotFoundException("OrganizationUnit")
        }
    }
}