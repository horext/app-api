package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.domain.OrganizationUnitTypeCode
import io.octatec.horext.api.domain.OrganizationUnits
import io.octatec.horext.api.domain.StudyPlans
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.alias
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.exists
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
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

    override fun getFacultiesHavingStudyPlans(): List<OrganizationUnit> {
        val speciality = OrganizationUnits.alias("ou_s")
        val specialityWithStudyPlanExists =
            StudyPlans
                .join(speciality, JoinType.INNER, StudyPlans.organizationUnitId, speciality[OrganizationUnits.id])
                .select(speciality[OrganizationUnits.id])
                .where {
                    (speciality[OrganizationUnits.typeId] eq OrganizationUnitTypeCode.SPECIALITY.id) and
                        (speciality[OrganizationUnits.parentOrganizationId] eq OrganizationUnits.id)
                }

        return OrganizationUnits
            .selectAll()
            .where {
                (OrganizationUnits.typeId eq OrganizationUnitTypeCode.FACULTY.id) and
                    exists(specialityWithStudyPlanExists)
            }.map { row -> OrganizationUnits.createEntity(row) }
    }
}
