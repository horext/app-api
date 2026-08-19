package io.octatec.horext.api.service

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.domain.OrganizationUnitTypeCode

interface OrganizationUnitService {
    fun getAllSpeciality(): List<OrganizationUnit>

    fun getAllFaculty(): List<OrganizationUnit>

    fun getAllSpecialityByFacultyId(id: Long): List<OrganizationUnit>

    fun getById(
        id: Long,
        type: OrganizationUnitTypeCode,
    ): OrganizationUnit
}
