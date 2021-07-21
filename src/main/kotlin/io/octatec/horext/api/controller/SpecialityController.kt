package io.octatec.horext.api.controller

import io.octatec.horext.api.model.OrganizationUnit
import io.octatec.horext.api.service.OrganizationUnitService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("specialities")
class SpecialityController(val organizationUnitService: OrganizationUnitService) {

    @GetMapping("{id}")
    fun getAllByFacultyId( @PathVariable id: Long): ResponseEntity<List<OrganizationUnit>> {
        return ResponseEntity<List<OrganizationUnit>>(
                organizationUnitService.getAllSpecialityByFacultyId(id),
                HttpStatus.OK)
    }
}