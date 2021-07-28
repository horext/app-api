package io.octatec.horext.api.service

import io.octatec.horext.api.model.OrganizationUnit

interface OrganizationUnitService {
    fun getAllSpeciality(): List<OrganizationUnit>
    fun getAllFaculty(): List<io.octatec.horext.api.domain.OrganizationUnit>
    fun getAllSpecialityByFacultyId(id: Long): List<OrganizationUnit>
}