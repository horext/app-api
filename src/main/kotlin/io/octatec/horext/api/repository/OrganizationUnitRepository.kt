package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.OrganizationUnit

interface OrganizationUnitRepository {
    fun getAllSpeciality(): List<OrganizationUnit>
    fun getAllFaculty(): List<OrganizationUnit>
    fun getAllSpecialityByFacultyId(id: Long): List<OrganizationUnit>
}
