package io.octatec.horext.api.repository.table

import io.octatec.horext.api.domain.AcademicPeriod
import io.octatec.horext.api.domain.AcademicPeriodOrganizationUnit
import io.octatec.horext.api.domain.OrganizationUnit
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.javatime.timestamp

object AcademicPeriodOrganizationUnits : LongIdTable("academic_period_organization_unit") {
    val fromDate = timestamp("from_date").nullable()

    val toDate = timestamp("to_date").nullable()

    val academicPeriodId = reference("academic_period_id", AcademicPeriods)

    val organizationUnitId = reference("organization_unit_id", OrganizationUnits)

    fun createEntity(row: ResultRow): AcademicPeriodOrganizationUnit =
        AcademicPeriodOrganizationUnit(
            id = row[id].value,
            fromDate = row[fromDate],
            toDate = row[toDate],
            academicPeriod =
                AcademicPeriod(
                    id = row[academicPeriodId].value,
                ),
            organizationUnit =
                OrganizationUnit(
                    id = row[organizationUnitId].value,
                ),
        )
}
