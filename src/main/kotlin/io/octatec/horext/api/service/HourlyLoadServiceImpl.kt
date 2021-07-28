package io.octatec.horext.api.service

import io.octatec.horext.api.domain.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.find
import org.ktorm.entity.map
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate

@Service
class HourlyLoadServiceImpl() : HourlyLoadService {
    @Autowired
    lateinit var database: Database

    override fun getLatestByFaculty(facultyId: Long): HourlyLoad? {
        return database.hourlyLoads.find {
            val apou = it.academicPeriodOrganizationUnitId.referenceTable  as AcademicPeriodOrganizationUnits
            (apou.organizationUnitId eq facultyId) and
                    (it.publishedAt lessEq Instant.now()) and
                    (apou.toDate.isNull()) and
                    (apou.fromDate lessEq Instant.now())
        }
    }

}