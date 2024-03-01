package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

data class HourlyLoad(

    val id: Long,

    var name: String?,

    var checkedAt: Instant?,

    var updatedAt: Instant?,

    var publishedAt: Instant?,

    var academicPeriodOrganizationUnit: AcademicPeriodOrganizationUnit?

) {

    constructor(id: Long) : this(id, null, null, null, null, null)
}

object HourlyLoads : LongIdTable("hourly_load") {

    val name = varchar("name", length = 100)

    val checkedAt = timestamp("checked_at")


    val updatedAt = timestamp("updated_at")

    val publishedAt = timestamp("published_at")

    val academicPeriodOrganizationUnitId =
        reference("academic_period_organization_unit_id", AcademicPeriodOrganizationUnits)

    fun createEntity(it: ResultRow): HourlyLoad {
        return HourlyLoad(
            id = it[id].value,
            name = it[name],
            checkedAt = it[checkedAt],
            updatedAt = it[updatedAt],
            publishedAt = it[publishedAt],
            academicPeriodOrganizationUnit = AcademicPeriodOrganizationUnits.createEntity(it)
        )
    }

}
