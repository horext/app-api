package io.octatec.horext.api.controller

import io.octatec.horext.api.model.OrganizationUnit
import io.octatec.horext.api.service.OrganizationUnitService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("faculties")
class FacultyController(val organizationUnitService: OrganizationUnitService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<OrganizationUnit>> {
        return ResponseEntity<List<OrganizationUnit>>(
                organizationUnitService.getAllFaculty(),
                HttpStatus.OK)
    }
}