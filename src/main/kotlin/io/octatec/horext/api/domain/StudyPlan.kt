package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

data class StudyPlan(

    val id: Long,

    val code: String?,

    var fromDate: Instant?,

    var toDate: Instant?,

    var organizationUnit: OrganizationUnit?
) {

    constructor(id: Long) : this(id, null, null, null, null)
}

object StudyPlans : LongIdTable("study_plan") {

    val fromDate = timestamp("from_date")

    val code = varchar("code", length = 50)

    val toDate = timestamp("to_date").nullable()

    val organizationUnitId = reference("organization_unit_id", OrganizationUnits)

    fun createEntity(row: ResultRow): StudyPlan {
        return StudyPlan(
            row[id].value,
            row[code],
            row[fromDate],
            row[toDate],
            OrganizationUnits.createEntity(row)
        )
    }
}
