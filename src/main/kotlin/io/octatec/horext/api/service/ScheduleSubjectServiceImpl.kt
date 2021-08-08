package io.octatec.horext.api.service

import io.octatec.horext.api.domain.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant


@Service
class ScheduleSubjectServiceImpl : ScheduleSubjectService {
    @Autowired
    lateinit var database: Database

    override fun findBySubjectIdAndHourlyLoadId(subjectId: Long, hourlyLoadId: Long): List<ScheduleSubject> {

        val ss = ScheduleSubjects
        val s = ss.scheduleId.referenceTable as Schedules
        return database
            .from(ss)
            .leftJoin(s, on = s.id eq ss.scheduleId)
            .select(ss.columns+s.columns)
            .where{(ss.subjectId eq subjectId) and
                    (ss.hourlyLoadId eq hourlyLoadId) }
            .map { row -> ss.createEntity(row) }

    }
}