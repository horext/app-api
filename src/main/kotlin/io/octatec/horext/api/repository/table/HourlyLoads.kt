package io.octatec.horext.api.repository.table

import io.octatec.horext.api.domain.HourlyLoad
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.javatime.timestamp

object HourlyLoads : LongIdTable("hourly_load") {
    val name = varchar("name", length = 100)

    val checkedAt = timestamp("checked_at").nullable()

    val updatedAt = timestamp("updated_at").nullable()

    val publishedAt = timestamp("published_at").nullable()

    val academicPeriodOrganizationUnitId =
        reference("academic_period_organization_unit_id", AcademicPeriodOrganizationUnits)

    fun createEntity(it: ResultRow): HourlyLoad =
        HourlyLoad(
            id = it[id].value,
            name = it[name],
            checkedAt = it[checkedAt],
            updatedAt = it[updatedAt],
            publishedAt = it[publishedAt],
            academicPeriodOrganizationUnit = AcademicPeriodOrganizationUnits.createEntity(it),
        )
}
