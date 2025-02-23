package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.AcademicPeriodOrganizationUnits
import io.octatec.horext.api.domain.HourlyLoad
import io.octatec.horext.api.domain.HourlyLoads
import org.jetbrains.exposed.sql.and
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class HourlyLoadRepositoryImpl : HourlyLoadRepository {
    override fun getLatestByFaculty(facultyId: Long): HourlyLoad {
        val hl = HourlyLoads
        val apou = AcademicPeriodOrganizationUnits
        return hl
            .innerJoin(apou)
            .select(hl.columns + apou.columns)
            .where {
                (apou.organizationUnitId eq facultyId) and
                    (hl.publishedAt lessEq Instant.now())
            }.map { row -> hl.createEntity(row) }
            .first()
    }
}
