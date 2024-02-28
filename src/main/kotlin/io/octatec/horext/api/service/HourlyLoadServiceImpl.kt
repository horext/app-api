package io.octatec.horext.api.service

import io.octatec.horext.api.domain.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate

@Service
class HourlyLoadServiceImpl(val database: Database) : HourlyLoadService {

    override fun getLatestByFaculty(facultyId: Long): HourlyLoad {
        val hl = HourlyLoads
        val apou = hl.academicPeriodOrganizationUnitId.referenceTable as AcademicPeriodOrganizationUnits
        return database
            .from(hl)
            .leftJoin(apou, on = hl.academicPeriodOrganizationUnitId eq apou.id)
            .select()
            .where{(apou.organizationUnitId eq facultyId) and
                    (hl.publishedAt lessEq Instant.now())}
            .map { row -> hl.createEntity(row) }.first()

    }
}