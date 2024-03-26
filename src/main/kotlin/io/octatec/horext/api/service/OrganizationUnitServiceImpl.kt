package io.octatec.horext.api.service

import io.octatec.horext.api.domain.ORGANIZATION_UNIT_TYPES
import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.domain.OrganizationUnits
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrganizationUnitServiceImpl() : OrganizationUnitService {

    override fun getAllSpeciality(): List<OrganizationUnit> {
        return OrganizationUnits.selectAll()
            .where { OrganizationUnits.typeId eq ORGANIZATION_UNIT_TYPES.SPECIALITY.id }
            .map { row ->
                OrganizationUnits.createEntity(row)
            }
    }

    override fun getAllFaculty(): List<OrganizationUnit> {
        return OrganizationUnits.selectAll()
            .where { OrganizationUnits.typeId eq ORGANIZATION_UNIT_TYPES.FACULTY.id }
            .map { row ->
                OrganizationUnits.createEntity(row)
            }
    }

    override fun getAllSpecialityByFacultyId(id: Long): List<OrganizationUnit> {
        return OrganizationUnits.selectAll()
            .where {
                (OrganizationUnits.parentOrganizationId eq id) and
                        (OrganizationUnits.typeId eq ORGANIZATION_UNIT_TYPES.SPECIALITY.id)
            }
            .map { row ->
                OrganizationUnits.createEntity(row)
            }
    }
}