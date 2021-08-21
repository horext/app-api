package io.octatec.horext.api.service

import io.octatec.horext.api.domain.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ScheduleServiceImpl : ScheduleService {
    @Autowired
    lateinit var database: Database

    override fun findBySubjectIdAndHourlyLoadId(subjectId: Long, hourlyLoadId: Long): List<Schedule> {
        val ss = ScheduleSubjects
        val s = ss.scheduleId.referenceTable as Schedules
        return database
            .from(s)
            .innerJoin(ss, on = ss.scheduleId eq s.id)
            .select()
            .where{(ss.subjectId eq subjectId) and
                    (ss.hourlyLoadId eq hourlyLoadId) }
            .map { row -> s.createEntity(row) }
    }
}