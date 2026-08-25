package io.octatec.horext.api.repository

import io.octatec.horext.api.repository.table.AcademicPeriodOrganizationUnits
import io.octatec.horext.api.repository.table.AcademicPeriods
import io.octatec.horext.api.repository.table.HourlyLoads
import io.octatec.horext.api.repository.table.OrganizationUnits
import io.octatec.horext.api.repository.table.ScheduleSubjects
import io.octatec.horext.api.repository.table.Schedules
import io.octatec.horext.api.repository.table.Subjects
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.jdbc.insert
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals

@SpringBootTest
@Transactional
class ScheduleRepositoryIntegrationTest(
    @Autowired private val repository: ScheduleRepository,
) {
    @Test
    fun `findBySubjectIdAndHourlyLoadId returns only schedules linked to the requested load`() {
        val graph = createScheduleGraph()
        val otherAcademicPeriodId = graph.idBase + 30
        val otherApouId = graph.idBase + 31
        val otherHourlyLoadId = graph.idBase + 32

        AcademicPeriods.insert {
            it[id] = EntityID(otherAcademicPeriodId, AcademicPeriods)
            it[code] = "OTHER-${graph.idBase}"
            it[name] = "Other test period"
        }
        AcademicPeriodOrganizationUnits.insert {
            it[id] = EntityID(otherApouId, AcademicPeriodOrganizationUnits)
            it[academicPeriodId] = EntityID(otherAcademicPeriodId, AcademicPeriods)
            it[organizationUnitId] = EntityID(graph.organizationId, OrganizationUnits)
        }

        HourlyLoads.insert {
            it[id] = EntityID(otherHourlyLoadId, HourlyLoads)
            it[name] = "Other test load"
            it[academicPeriodOrganizationUnitId] = EntityID(otherApouId, AcademicPeriodOrganizationUnits)
        }
        ScheduleSubjects.insert {
            it[id] = EntityID(graph.idBase + 33, ScheduleSubjects)
            it[scheduleId] = EntityID(graph.unrelatedScheduleId, Schedules)
            it[subjectId] = EntityID(graph.subjectId, Subjects)
            it[hourlyLoadId] = EntityID(otherHourlyLoadId, HourlyLoads)
        }

        val result = repository.findBySubjectIdAndHourlyLoadId(graph.subjectId, graph.hourlyLoadId)

        assertEquals(listOf(graph.linkedScheduleId), result.map { it.id })
    }
}
