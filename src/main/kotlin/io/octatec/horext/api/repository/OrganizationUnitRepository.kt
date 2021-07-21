package io.octatec.horext.api.repository

import io.octatec.horext.api.model.OrganizationUnit
import io.octatec.horext.api.model.OrganizationUnitType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface OrganizationUnitRepository : JpaRepository<OrganizationUnit, Long>{
    fun findAllByOrganizationUnitType(organizationUnitType: OrganizationUnitType):List<OrganizationUnit>
    fun findAllByParentOrganization(parentOrganization: OrganizationUnit):List<OrganizationUnit>
}