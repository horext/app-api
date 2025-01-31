package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.ORGANIZATION_UNIT_TYPES
import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.domain.OrganizationUnits
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository

@Repository
class OrganizationUnitRepositoryImpl : OrganizationUnitRepository {
    override fun getAllSpeciality(): List<OrganizationUnit> =
        OrganizationUnits
            .selectAll()
            .where { OrganizationUnits.typeId eq ORGANIZATION_UNIT_TYPES.SPECIALITY.id }
            .map { row ->
                OrganizationUnits.createEntity(row)
            }

    override fun getAllFaculty(): List<OrganizationUnit> =
        OrganizationUnits
            .selectAll()
            .where { OrganizationUnits.typeId eq ORGANIZATION_UNIT_TYPES.FACULTY.id }
            .map { row ->
                OrganizationUnits.createEntity(row)
            }

    override fun getAllSpecialityByFacultyId(id: Long): List<OrganizationUnit> =
        OrganizationUnits
            .selectAll()
            .where {
                (OrganizationUnits.parentOrganizationId eq id) and
                    (OrganizationUnits.typeId eq ORGANIZATION_UNIT_TYPES.SPECIALITY.id)
            }.map { row ->
                OrganizationUnits.createEntity(row)
            }
}
