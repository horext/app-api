package io.octatec.horext.api.repository.table

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.domain.StudyPlan
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.javatime.timestamp

object StudyPlans : LongIdTable("study_plan") {
    val fromDate = timestamp("from_date").nullable()

    val code = varchar("code", length = 50)

    val name = varchar("name", length = 255)

    val toDate = timestamp("to_date").nullable()

    val organizationUnitId = reference("organization_unit_id", OrganizationUnits)

    val createdAt = timestamp("created_at")

    val updatedAt = timestamp("updated_at")

    fun createEntity(row: ResultRow): StudyPlan =
        StudyPlan(
            row[id].value,
            row[code],
            row[name],
            row[fromDate],
            row[toDate],
            runCatching { OrganizationUnits.createEntity(row) }
                .getOrElse { OrganizationUnit(id = row[organizationUnitId].value) },
            row[createdAt],
            row[updatedAt],
        )
}
