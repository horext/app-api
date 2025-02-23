package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

data class AcademicPeriodOrganizationUnit(
    val id: Long,
    var fromDate: Instant?,
    var toDate: Instant?,
    var academicPeriod: AcademicPeriod?,
    var organizationUnit: OrganizationUnit?,
) {
    constructor(id: Long) : this(id, null, null, null, null)
}

object AcademicPeriodOrganizationUnits : LongIdTable("academic_period_organization_unit") {
    val fromDate = timestamp("from_date")

    val toDate = timestamp("to_date")

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
