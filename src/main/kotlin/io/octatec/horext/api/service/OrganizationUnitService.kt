package io.octatec.horext.api.service

import io.octatec.horext.api.domain.OrganizationUnit

interface OrganizationUnitService {
    fun getAllSpeciality(): List<OrganizationUnit>

    fun getAllFaculty(): List<OrganizationUnit>

    fun getAllSpecialityByFacultyId(id: Long): List<OrganizationUnit>
}
