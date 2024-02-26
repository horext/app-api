package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.exception.ResourceNotFoundException
import io.octatec.horext.api.service.OrganizationUnitService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("specialities")
class SpecialityController(val organizationUnitService: OrganizationUnitService) {

    @GetMapping
    fun getAllByFacultyId(
            @RequestParam(name = "faculty") id: Long
    ): List<OrganizationUnit> {
        try {
            return  organizationUnitService.getAllSpecialityByFacultyId(id)
        } catch (e: Exception) {
            e.printStackTrace()
            throw ResourceNotFoundException("OrganizationUnit")
        }
    }
}
