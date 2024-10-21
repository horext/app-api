package io.octatec.horext.api.service

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.repository.OrganizationUnitRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrganizationUnitServiceImpl(private val organizationUnitRepository: OrganizationUnitRepository) : OrganizationUnitService {

    override fun getAllSpeciality(): List<OrganizationUnit> {
        return organizationUnitRepository.getAllSpeciality()
    }

    override fun getAllFaculty(): List<OrganizationUnit> {
        return organizationUnitRepository.getAllFaculty()
    }

    override fun getAllSpecialityByFacultyId(id: Long): List<OrganizationUnit> {
        return organizationUnitRepository.getAllSpecialityByFacultyId(id)
    }
}
