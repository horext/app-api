package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.AcademicPeriodOrganizationUnits
import io.octatec.horext.api.domain.HourlyLoad
import io.octatec.horext.api.domain.HourlyLoads
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.lessEq
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.jdbc.select
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class HourlyLoadRepositoryImpl : HourlyLoadRepository {
    override fun getLatestByFaculty(facultyId: Long): HourlyLoad? {
        val hl = HourlyLoads
        val apou = AcademicPeriodOrganizationUnits
        return hl
            .innerJoin(apou)
            .select(hl.columns + apou.columns)
            .where {
                (apou.organizationUnitId eq facultyId) and
                    (hl.publishedAt lessEq Instant.now())
            }
            .orderBy(hl.publishedAt to SortOrder.DESC)
            .map { row -> hl.createEntity(row) }
            .firstOrNull()
    }
}
