package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.domain.OrganizationUnitTypeCode

interface OrganizationUnitRepository {
    fun getAllByType(typeId: OrganizationUnitTypeCode): List<OrganizationUnit>

    fun getAllSpecialityByFacultyId(id: Long): List<OrganizationUnit>
}
