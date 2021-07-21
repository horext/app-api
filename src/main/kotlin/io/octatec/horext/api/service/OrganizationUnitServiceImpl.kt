package io.octatec.horext.api.service

import io.octatec.horext.api.model.OrganizationUnit
import io.octatec.horext.api.model.OrganizationUnitType
import io.octatec.horext.api.repository.OrganizationUnitRepository
import org.springframework.stereotype.Service

@Service
class OrganizationUnitServiceImpl(val organizationUnitRepository: OrganizationUnitRepository) : OrganizationUnitService {
    override fun getAllSpeciality(): List<OrganizationUnit> {
       return organizationUnitRepository.findAllByOrganizationUnitType(OrganizationUnitType(3)).
       map { o -> OrganizationUnit(o.id,o.code,o.name) }
    }
    override fun getAllFaculty(): List<OrganizationUnit> {
        return organizationUnitRepository.findAllByOrganizationUnitType(OrganizationUnitType(2)).
        map { o -> OrganizationUnit(o.id,o.code,o.name)}
    }

    override fun getAllSpecialityByFacultyId(id: Long): List<OrganizationUnit> {
        return organizationUnitRepository.findAllByParentOrganization(OrganizationUnit(id)).
        map { o -> OrganizationUnit(o.id,o.code,o.name) }
    }
}