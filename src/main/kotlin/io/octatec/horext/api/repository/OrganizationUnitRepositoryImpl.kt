package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.domain.OrganizationUnitTypeCode
import io.octatec.horext.api.domain.OrganizationUnits
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository

@Repository
class OrganizationUnitRepositoryImpl : OrganizationUnitRepository {
    override fun getAllByType(typeId: OrganizationUnitTypeCode): List<OrganizationUnit> =
        OrganizationUnits
            .selectAll()
            .where { OrganizationUnits.typeId eq typeId.id }
            .map { row ->
                OrganizationUnits.createEntity(row)
            }

    override fun getAllSpecialityByFacultyId(id: Long): List<OrganizationUnit> =
        OrganizationUnits
            .selectAll()
            .where {
                (OrganizationUnits.parentOrganizationId eq id) and
                    (OrganizationUnits.typeId eq OrganizationUnitTypeCode.SPECIALITY.id)
            }.map { row ->
                OrganizationUnits.createEntity(row)
            }
}
